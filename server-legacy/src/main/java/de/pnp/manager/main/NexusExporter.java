package de.pnp.manager.main;

import de.pnp.manager.model.Rarity;
import de.pnp.manager.model.item.IArmor;
import de.pnp.manager.model.item.IEquipment;
import de.pnp.manager.model.item.IItem;
import de.pnp.manager.model.item.IJewellery;
import de.pnp.manager.model.item.IWeapon;
import feign.FeignException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javafx.collections.ObservableSet;
import org.openapitools.client.ApiClient;
import org.openapitools.client.api.ItemServiceApi;
import org.openapitools.client.api.ItemTypeServiceApi;
import org.openapitools.client.api.ItemTypeTranslationServiceApi;
import org.openapitools.client.api.MaterialServiceApi;
import org.openapitools.client.api.UniverseServiceApi;
import org.openapitools.client.auth.HttpBasicAuth;
import org.openapitools.client.model.CurrencyCalculationDto;
import org.openapitools.client.model.ItemDto;
import org.openapitools.client.model.ItemDto.RarityEnum;
import org.openapitools.client.model.ItemTypeDto;
import org.openapitools.client.model.ItemTypeDto.TypeRestrictionEnum;
import org.openapitools.client.model.ItemTypeTranslationDto;
import org.openapitools.client.model.MaterialDto;
import org.openapitools.client.model.UniverseDto;
import org.openapitools.client.model.UniverseSettingsDto;

public class NexusExporter {

    public static void export(String url, String universe, String username, String password) {
        ApiClient client = new ApiClient();
        client.setBasePath(url);
        HttpBasicAuth basicAuth = new HttpBasicAuth();
        basicAuth.setCredentials(username, password);
        client.addAuthorization("basic", basicAuth);
        createUniverse(client, universe);
        Map<String, MaterialDto> materials = exportMaterials(client, universe);
        Map<String, ItemTypeDto> itemTypes = exportItemTypes(client, universe);
        exportItemTypeTranslations(client, universe, itemTypes);
        exportItems(client, universe, materials, itemTypes);
    }

    private static void createUniverse(ApiClient client, String universe) {
        UniverseServiceApi universeClient = client.buildClient(UniverseServiceApi.class);
        try {
            universeClient.getUniverse(universe);
        } catch (FeignException.NotFound e) {
            universeClient.createUniverse(new UniverseDto().displayName(universe).name(universe).settings(
                new UniverseSettingsDto().wearFactor(10)
                    .currencyCalculation(
                        new CurrencyCalculationDto().baseCurrency("Kupfer").baseCurrencyShortForm("K"))));
        }
    }

    private static Map<String, MaterialDto> exportMaterials(ApiClient client, String universe) {
        Set<MaterialDto> materials = new HashSet<>();

        for (IItem item : Database.itemList) {
            if (item instanceof IEquipment equipment) {
                materials.add(new MaterialDto().name(equipment.getMaterial()));
            }
        }

        MaterialServiceApi materialClient = client.buildClient(MaterialServiceApi.class);
        List<MaterialDto> insertedMaterials = materialClient.insertAllMaterials(universe, materials.stream().toList());

        Map<String, MaterialDto> materialMap = new HashMap<>();
        for (MaterialDto insertedMaterial : insertedMaterials) {
            materialMap.put(insertedMaterial.getName(), insertedMaterial);
        }
        return materialMap;
    }

    private static Map<String, ItemTypeDto> exportItemTypes(ApiClient client, String universe) {
        Map<String, Set<TypeRestrictionEnum>> itemTypes = new HashMap<>();

        for (IItem item : Database.itemList) {
            Set<TypeRestrictionEnum> typeRestrictions = itemTypes.computeIfAbsent(item.getType(),
                key -> new HashSet<>());
            typeRestrictions.add(getRestriction(item));
            itemTypes.put(item.getType(), typeRestrictions);
            Set<TypeRestrictionEnum> subtypeRestrictions = itemTypes.computeIfAbsent(item.getSubtype(),
                key -> new HashSet<>());
            subtypeRestrictions.add(getRestriction(item));
            itemTypes.put(item.getSubtype(), subtypeRestrictions);
        }

        List<ItemTypeDto> itemTypeDtos = new ArrayList<>();
        for (Entry<String, Set<TypeRestrictionEnum>> entry : itemTypes.entrySet()) {
            itemTypeDtos.add(
                new ItemTypeDto().name(entry.getKey()).typeRestriction(reduceRestrictions(entry.getValue())));
        }

        ItemTypeServiceApi itemTypeClient = client.buildClient(ItemTypeServiceApi.class);
        List<ItemTypeDto> insertedItemTypes = itemTypeClient.insertAllItemTypes(universe, itemTypeDtos);

        Map<String, ItemTypeDto> itemTypeMap = new HashMap<>();
        for (ItemTypeDto insertedItemType : insertedItemTypes) {
            itemTypeMap.put(insertedItemType.getName(), insertedItemType);
        }
        return itemTypeMap;
    }

    private static TypeRestrictionEnum getRestriction(IItem item) {
        if (item instanceof IWeapon) {
            if (Database.shieldTypes.contains(item.getType())) {
                return TypeRestrictionEnum.SHIELD;
            }
            return TypeRestrictionEnum.WEAPON;
        }
        if (item instanceof IArmor) {
            return TypeRestrictionEnum.ARMOR;
        }
        if (item instanceof IJewellery) {
            return TypeRestrictionEnum.JEWELLERY;
        }
        return TypeRestrictionEnum.ITEM;
    }

    private static TypeRestrictionEnum reduceRestrictions(Set<TypeRestrictionEnum> restrictions) {
        if (restrictions.size() == 0) {
            return TypeRestrictionEnum.ITEM;
        }
        if (restrictions.size() == 1) {
            return restrictions.stream().findFirst().get();
        }
        if (restrictions.contains(TypeRestrictionEnum.ITEM)) {
            return TypeRestrictionEnum.ITEM;
        }
        if (restrictions.size() == 2 && restrictions.containsAll(
            List.of(TypeRestrictionEnum.WEAPON, TypeRestrictionEnum.SHIELD))) {
            return TypeRestrictionEnum.HANDHELD;
        }
        if (restrictions.size() == 2 && restrictions.containsAll(
            List.of(TypeRestrictionEnum.ARMOR, TypeRestrictionEnum.SHIELD))) {
            return TypeRestrictionEnum.DEFENSIVE_ITEM;
        }
        return TypeRestrictionEnum.EQUIPMENT;
    }

    private static void exportItemTypeTranslations(ApiClient client, String universe,
        Map<String, ItemTypeDto> itemTypes) {
        List<ItemTypeTranslationDto> translations = new ArrayList<>();

        for (Entry<String, ObservableSet<String>> entry : TypTranslation.translationMap.entrySet()) {
            if (itemTypes.get(entry.getKey()) == null) {
                continue;
            }
            ItemTypeTranslationDto translation = new ItemTypeTranslationDto().type(itemTypes.get(entry.getKey()));
            for (String subType : entry.getValue()) {
                translation.addBroaderVariantsItem(itemTypes.get(subType));
            }
            translations.add(translation);
        }

        ItemTypeTranslationServiceApi translationClient = client.buildClient(
            ItemTypeTranslationServiceApi.class);
        translationClient.insertAllItemTypeTranslations(universe, translations);
    }

    private static void exportItems(ApiClient client, String universe, Map<String, MaterialDto> materials,
        Map<String, ItemTypeDto> itemTypes) {
        List<ItemDto> items = new ArrayList<>();

        for (IItem item : Database.itemList) {
            if (!isHelperItem(item) && !(item instanceof IEquipment)) {
                items.add(new ItemDto()
                    .name(item.getName())
                    .type(itemTypes.get(item.getType()))
                    .subtype(itemTypes.get(item.getSubtype()))
                    .requirement(item.getRequirement())
                    .effect(item.getEffect())
                    .rarity(convertRarity((Rarity) item.getRarity()))
                    .vendorPrice(item.getCurrency().getCoinValue())
                    .tier(item.getTier())
                    .description("")
                    .note("")
                    .minimumStackSize(0)
                    .maximumStackSize(100)
                );
            }
        }

        ItemServiceApi itemApi = client.buildClient(ItemServiceApi.class);
        itemApi.insertAllItems(universe, items);
    }

    private static RarityEnum convertRarity(Rarity rarity) {
        return switch (rarity) {
            case UNKNOWN -> RarityEnum.UNKNOWN;
            case COMMON -> RarityEnum.COMMON;
            case RARE -> RarityEnum.RARE;
            case EPIC -> RarityEnum.EPIC;
            case LEGENDARY -> RarityEnum.LEGENDARY;
            case GODLIKE -> RarityEnum.GODLIKE;
        };
    }

    private static boolean isHelperItem(IItem item) {
        return "Hilfstyp".equals(item.getType()) || "Hilfstyp".equals(item.getSubtype());
    }
}
