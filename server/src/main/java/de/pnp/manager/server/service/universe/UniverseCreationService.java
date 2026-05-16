package de.pnp.manager.server.service.universe;

import com.google.common.base.MoreObjects;
import de.pnp.manager.Tag;
import de.pnp.manager.component.attributes.SecondaryAttribute;
import de.pnp.manager.component.attributes.SecondaryAttributeDTO;
import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.item.Material.MaterialItem;
import de.pnp.manager.component.math.BinaryExpressionTree;
import de.pnp.manager.component.math.IExpressionVariable;
import de.pnp.manager.component.math.IExpressionVariable.PrimaryAttributeVariable;
import de.pnp.manager.component.math.IExpressionVariable.StringVariable;
import de.pnp.manager.component.universe.CharacterSettings;
import de.pnp.manager.component.universe.EquipmentSettings.JewelleryDefinition;
import de.pnp.manager.component.universe.Universe;
import de.pnp.manager.security.UniverseOwner;
import de.pnp.manager.server.contoller.SecondaryAttributeDTOController;
import de.pnp.manager.server.database.MaterialRepository;
import de.pnp.manager.server.database.attributes.PrimaryAttributeRepository;
import de.pnp.manager.server.database.item.ItemRepository;
import de.pnp.manager.server.database.universe.UniverseSettingsRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Service to help create {@link Universe universes}.
 */
@RestController
@Validated
@RequestMapping("/api/{universe}/universe-creation")
public class UniverseCreationService {

    private final ItemRepository itemRepository;

    private final MaterialRepository materialRepository;

    private final PrimaryAttributeRepository primaryAttributeRepository;

    private final UniverseSettingsRepository settingsRepository;

    private final SecondaryAttributeDTOController secondaryAttributeController;

    public UniverseCreationService(
            @Autowired ItemRepository itemRepository,
            @Autowired MaterialRepository materialRepository,
            @Autowired PrimaryAttributeRepository primaryAttributeRepository,
            @Autowired UniverseSettingsRepository settingsRepository,
            @Autowired SecondaryAttributeDTOController secondaryAttributeController) {
        this.itemRepository = itemRepository;
        this.materialRepository = materialRepository;
        this.primaryAttributeRepository = primaryAttributeRepository;
        this.settingsRepository = settingsRepository;
        this.secondaryAttributeController = secondaryAttributeController;
    }

    @GetMapping("equipment-types/jewellery-definitions")
    @UniverseOwner
    @Operation(summary = "Gets the basic armor definitions most universes need", operationId = "getDefaultJewelleryDefinitions")
    public List<JewelleryDefinition> getDefaultJewelleryDefinitions(@PathVariable ObjectId universe,
                                                                    @RequestParam String language) {
        ResourceBundle bundle = ResourceBundle.getBundle("universeCreation", Locale.of(language));

        String ring = bundle.getString("ring");
        String necklace = bundle.getString("necklace");
        String bracelet = bundle.getString("bracelet");

        return List.of(
                new JewelleryDefinition(ring, ring, 8),
                new JewelleryDefinition(necklace, necklace, 1),
                new JewelleryDefinition(bracelet, bracelet, 2)
        );
    }

    @PostMapping("materials")
    @UniverseOwner
    @Operation(summary = "Creates a few basic materials most universes need", operationId = "createDefaultMaterials")
    public void createDefaultMaterials(@PathVariable ObjectId universe, @RequestParam String language) {
        ResourceBundle bundle = ResourceBundle.getBundle("universeCreation", Locale.of(language));

        Tag material = new Tag(bundle.getString("material"));
        Tag ore = new Tag(bundle.getString("ore"));
        Tag ingot = new Tag(bundle.getString("ingot"));
        Tag wood = new Tag(bundle.getString("wood"));

        Item ironIngot = itemRepository.insert(universe,
                new Item(null, bundle.getString("iron_ingot"), Set.of(material, ingot), "", List.of(), ERarity.COMMON, 100, 1,
                        bundle.getString("iron_ingot_description"), "", 100, 0));
        itemRepository.insert(universe,
                new Item(null, bundle.getString("iron_ore"), Set.of(material, ore), "", List.of(), ERarity.COMMON, 30, 1,
                        bundle.getString("iron_ore_description"), "", 100, 0));
        Item woodPlank = itemRepository.insert(universe,
                new Item(null, bundle.getString("wood_plank"), Set.of(material, wood), "", List.of(), ERarity.COMMON, 10, 1,
                        bundle.getString("wood_plank_description"), "", 100, 0));
        Item rawWood = itemRepository.insert(universe,
                new Item(null, bundle.getString("raw_wood"), Set.of(material, wood), "", List.of(), ERarity.COMMON, 3, 1,
                        bundle.getString("raw_wood_description"), "", 100, 0));

        materialRepository.insertAll(universe, List.of(
                new Material(null, bundle.getString("iron"), List.of(new MaterialItem(1, ironIngot))),
                new Material(null, bundle.getString("wood"),
                        List.of(new MaterialItem(1, woodPlank), new MaterialItem(1, rawWood)))
        ));
    }

    @PostMapping("secondary-attribute-info")
    @UniverseOwner
    @Operation(summary = "Creates a few basic materials most universes need", operationId = "getSecondaryAttributeInfo")
    public List<SecondaryAttributeInfo> getSecondaryAttributeInfo(@PathVariable ObjectId universe,
                                                                  @RequestBody List<SecondaryAttributeDTO> attributeDTOS) {

        List<SecondaryAttribute> secondaryAttributes = secondaryAttributeController.convertRaw(universe, attributeDTOS);
        CharacterSettings settings = settingsRepository.getSettings(universe, CharacterSettings.class);
        double averageAttributeValue = Math.max(
                settings.getMinPrimaryAttributeValue(),
                Math.min(
                        settings.getMaxPrimaryAttributeValue(),
                        ((double) settings.getMaxPrimaryAttributeSum()) / primaryAttributeRepository.getAll(universe).size()
                )
        );
        List<SecondaryAttributeInfo> boundaries = new ArrayList<>();

        for (SecondaryAttribute secondaryAttribute : secondaryAttributes) {
            if (secondaryAttribute == null || secondaryAttribute.getCalculationFormula() == null
                    || secondaryAttribute.getCalculationFormula().isEmpty()) {
                boundaries.add(null);
                continue;
            }

            try (ExecutorService executor = Executors.newSingleThreadExecutor()) {
                boundaries.add(executor.submit(
                                () -> calculateSecondaryAttributeInfo(secondaryAttribute.getCalculationFormula(),
                                        settings.getMinPrimaryAttributeValue(), settings.getMaxPrimaryAttributeValue(),
                                        settings.getMaxPrimaryAttributeSum(), averageAttributeValue))
                        .get(500, TimeUnit.MILLISECONDS));
            } catch (ExecutionException | InterruptedException | TimeoutException e) {
                int average = (int) Math.round(secondaryAttribute.getCalculationFormula()
                        .calculate(secondaryAttribute.getCalculationFormula().getVariables().stream()
                                .collect(Collectors.toMap(v -> v,
                                        v -> v instanceof PrimaryAttributeVariable ? averageAttributeValue : 1))));

                boundaries.add(new SecondaryAttributeInfo(0, 0, average, true));
            }
        }
        return boundaries;
    }

    private SecondaryAttributeInfo calculateSecondaryAttributeInfo(BinaryExpressionTree tree, int minBoundary,
                                                                   int maxBoundary, int sumBoundary, double averageValue) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        Queue<CalculationEntry> queue = new LinkedList<>();

        PrimaryAttributeVariable[] variables = tree.getVariables().stream()
                .filter(PrimaryAttributeVariable.class::isInstance)
                .map(PrimaryAttributeVariable.class::cast).toArray(PrimaryAttributeVariable[]::new);
        int[] startValues = new int[variables.length];
        Arrays.fill(startValues, minBoundary);
        queue.add(new CalculationEntry(startValues, 0));

        while (!queue.isEmpty()) {
            if (Thread.currentThread().isInterrupted()) {
                return null;
            }

            CalculationEntry state = queue.poll();

            if (state.sum() > sumBoundary) {
                continue;
            }

            int result = (int) Math.round(tree.calculate(state.getVariables(variables)));
            min = Math.min(min, result);
            max = Math.max(max, result);

            queue.addAll(state.increase(maxBoundary));
        }

        int average = (int) Math.round(tree.calculate(tree.getVariables().stream()
                .collect(Collectors.toMap(v -> v, v -> v instanceof PrimaryAttributeVariable ? averageValue : 1))));

        return new SecondaryAttributeInfo(min, max, average, false);
    }

    private record CalculationEntry(int[] values, int increasePointer) {

        private Map<IExpressionVariable, Double> getVariables(PrimaryAttributeVariable[] variables) {
            HashMap<IExpressionVariable, Double> map = new HashMap<>();

            for (int i = 0; i < variables.length; i++) {
                map.put(variables[i], (double) values[i]);
            }
            map.put(new StringVariable("LVL"), 1d);

            return map;
        }

        private int sum() {
            return Arrays.stream(values).sum();
        }

        private Set<CalculationEntry> increase(int maxBoundary) {
            HashSet<CalculationEntry> set = new HashSet<>();

            for (int i = increasePointer; i < values.length; i++) {
                int[] newValues = Arrays.copyOf(values, values.length);
                newValues[i] += 1;
                if (newValues[i] <= maxBoundary) {
                    set.add(new CalculationEntry(newValues, i));
                }
            }

            return set;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            CalculationEntry that = (CalculationEntry) o;
            return increasePointer() == that.increasePointer() && Objects.deepEquals(values(), that.values());
        }

        @Override
        public int hashCode() {
            return Objects.hash(Arrays.hashCode(values()), increasePointer());
        }

        @Override
        public @NotNull String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("values", values)
                    .add("increasePointer", increasePointer)
                    .toString();
        }
    }

    /**
     * A data bundle to hold information about a secondary attribute
     */
    public record SecondaryAttributeInfo(int min, int max, int average, boolean notPrecise) {

    }
}
