package ui.utility;

import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import manager.Database;
import manager.LanguageUtility;
import model.Spell;
import model.item.*;
import model.upgrade.UpgradeModel;
import ui.IView;
import ui.ViewPart;
import ui.part.FilteredTableView;

public class SQLView extends ViewPart {

    public SQLView(IView parent) {
        super("sql.title", parent);

        TabPane root = new TabPane();
        root.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab itemTab = new Tab();
        itemTab.textProperty().bind(LanguageUtility.getMessageProperty("sql.tab.items"));
        root.getTabs().add(itemTab);

        FilteredTableView<Item> itemTable = new FilteredTableView<>(Database.itemList);
        itemTable.setPadding(new Insets(10, 20, 20, 20));
        itemTable.addColumn("column.name", Item::getName);
        itemTable.addColumn("column.type", Item::getType);
        itemTable.addColumn("column.item.subtype", Item::getSubtype);
        itemTable.addColumn("column.tier", Item::getTier);
        itemTable.addColumn("column.item.rarity", Item::getRarity);
        itemTable.addColumn("column.item.price", Item::getCurrency);
        itemTable.addColumn("column.effect", Item::getEffect);
        itemTab.setContent(itemTable);

        Tab weaponTab = new Tab();
        weaponTab.textProperty().bind(LanguageUtility.getMessageProperty("sql.tab.weapons"));
        root.getTabs().add(weaponTab);

        // TODO add initivateModifier from weapon
        FilteredTableView<Weapon> weaponTable = new FilteredTableView<>(Database.weaponList);
        weaponTable.setPadding(new Insets(10, 20, 20, 20));
        weaponTable.addColumn("column.name", Item::getName);
        weaponTable.addColumn("column.type", Item::getSubtype);
        weaponTable.addColumn("column.weapon.initiative", Weapon::getInitiative);
        weaponTable.addColumn("column.dice_weight", Weapon::getDice);
        weaponTable.addColumn("column.damage_protection", Weapon::getDamage);
        weaponTable.addColumn("column.weapon.hit", Weapon::getHit);
        weaponTable.addColumn("column.tier", Item::getTier);
        weaponTable.addColumn("column.item.rarity", Item::getRarity);
        weaponTable.addColumn("column.item.price", Item::getCurrency);
        weaponTable.addColumn("column.effect", Item::getEffect);
        weaponTable.addColumn("column.equipment.slots", Equipment::getUpgradeSlots);
        weaponTable.addColumn("column.equipment.requirement", Equipment::getRequirement);
        weaponTab.setContent(weaponTable);

        Tab armorTab = new Tab();
        armorTab.textProperty().bind(LanguageUtility.getMessageProperty("sql.tab.armor"));
        root.getTabs().add(armorTab);

        FilteredTableView<Armor> armorTable = new FilteredTableView<>(Database.armorList);
        armorTable.setPadding(new Insets(10, 20, 20, 20));
        armorTable.addColumn("column.name", Item::getName);
        armorTable.addColumn("column.type", Item::getSubtype);
        armorTable.addColumn("column.armor.protection", Armor::getProtection);
        armorTable.addColumn("column.armor.weight", Armor::getWeight);
        armorTable.addColumn("column.tier", Item::getTier);
        armorTable.addColumn("column.item.rarity", Item::getRarity);
        armorTable.addColumn("column.item.price", Item::getCurrency);
        armorTable.addColumn("column.effect", Item::getEffect);
        armorTable.addColumn("column.equipment.slots", Equipment::getUpgradeSlots);
        armorTable.addColumn("column.equipment.requirement", Equipment::getRequirement);
        armorTab.setContent(armorTable);

        Tab jewelleryTab = new Tab();
        jewelleryTab.textProperty().bind(LanguageUtility.getMessageProperty("sql.tab.jewellery"));
        root.getTabs().add(jewelleryTab);

        FilteredTableView<Jewellery> jewelleryTable = new FilteredTableView<>(Database.jewelleryList);
        jewelleryTable.setPadding(new Insets(10, 20, 20, 20));
        jewelleryTable.addColumn("column.name", Item::getName);
        jewelleryTable.addColumn("column.type", Item::getSubtype);
        jewelleryTable.addColumn("column.equipment.material", Equipment::getMaterial);
        jewelleryTable.addColumn("column.jewellery.gem", Jewellery::getGem);
        jewelleryTable.addColumn("column.tier", Item::getTier);
        jewelleryTable.addColumn("column.item.rarity", Item::getRarity);
        jewelleryTable.addColumn("column.item.price", Item::getCurrency);
        jewelleryTable.addColumn("column.effect", Item::getEffect);
        jewelleryTable.addColumn("column.equipment.slots", Equipment::getUpgradeSlots);
        jewelleryTable.addColumn("column.equipment.requirement", Equipment::getRequirement);
        jewelleryTab.setContent(jewelleryTable);

        Tab spellTab = new Tab();
        spellTab.textProperty().bind(LanguageUtility.getMessageProperty("sql.tab.spell"));
        root.getTabs().add(spellTab);

        FilteredTableView<Spell> spellTable = new FilteredTableView<>(Database.spellList);
        spellTable.setPadding(new Insets(10, 20, 20, 20));
        spellTable.addColumn("column.name", Spell::getName);
        spellTable.addColumn("column.effect", Spell::getEffect);
        spellTable.addColumn("column.type", Spell::getType);
        spellTable.addColumn("column.spell.cost", Spell::getCost);
        spellTable.addColumn("column.spell.castTime", Spell::getCastTime);
        spellTable.addColumn("column.tier", Spell::getTier);
        spellTab.setContent(spellTable);

        Tab upgradeTab = new Tab();
        upgradeTab.textProperty().bind(LanguageUtility.getMessageProperty("sql.tab.upgrades"));
        root.getTabs().add(upgradeTab);

        FilteredTableView<UpgradeModel> upgradeTable = new FilteredTableView<>(Database.upgradeModelList);
        upgradeTable.setPadding(new Insets(10, 20, 20, 20));
        upgradeTable.addColumn("column.name", UpgradeModel::getName);
        upgradeTable.addColumn("column.upgrade.level", UpgradeModel::getLevel);
        upgradeTable.addColumn("column.target", UpgradeModel::getTarget);
        upgradeTable.addColumn("column.effect", UpgradeModel::getEffect);
        upgradeTable.addColumn("column.equipment.slots", UpgradeModel::getSlots);
        upgradeTable.addColumn("column.item.price", UpgradeModel::getCost);
        upgradeTable.addColumn("column.upgrade.mana", UpgradeModel::getMana);
        upgradeTable.addColumn("column.upgrade.materials", UpgradeModel::getMaterials);
        upgradeTab.setContent(upgradeTable);

        Tab enemyTab = new Tab();
        enemyTab.textProperty().bind(LanguageUtility.getMessageProperty("sql.tab.enemies"));
        root.getTabs().add(enemyTab);
        enemyTab.setContent(new EnemyOverview());

        this.setContent(root);
    }
}
