package de.pnp.manager.ui.utility.enemygraph;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.layout.Region;
import de.pnp.manager.model.graph.Graph;
import de.pnp.manager.model.member.generation.GenerationBase;
import de.pnp.manager.model.member.generation.TypedGenerationBase;
import de.pnp.manager.model.member.generation.specs.*;
import de.pnp.manager.ui.part.GraphView;
import de.pnp.manager.ui.part.interfaces.IPositioningStrategy;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GenerationPositioningStrategy implements IPositioningStrategy<GenerationBase> {

    protected final double minSpacing;

    public GenerationPositioningStrategy(double minSpacing) {
        this.minSpacing = minSpacing;
    }

    @Override
    public void position(Graph<GenerationBase, ?> graph, Map<GenerationBase, Region> generationBaseRegionMap, GraphView<GenerationBase, ?> graphView) {
        Random rand = new Random();

        List<Characterisation> characterisationList = graph.getNodes().stream()
                .filter(base -> base instanceof Characterisation)
                .map(base -> (Characterisation) base)
                .collect(Collectors.toList());
        List<Race> raceList = createChildList(characterisationList, graph.getNodes().stream()
                .filter(base -> base instanceof Race)
                .map(base -> (Race) base));
        List<Profession> professionList = createChildList(raceList, graph.getNodes().stream()
                .filter(base -> base instanceof Profession)
                .map(base -> (Profession) base));
        List<FightingStyle> fightingStyleList = createChildList(professionList, graph.getNodes().stream()
                .filter(base -> base instanceof FightingStyle)
                .map(base -> (FightingStyle) base));
        List<Specialisation> specialisationList = createChildList(fightingStyleList, graph.getNodes().stream()
                .filter(base -> base instanceof Specialisation)
                .map(base -> (Specialisation) base));

        for (GenerationBase node : graph.getNodes()) {

            Region region = generationBaseRegionMap.get(node);

            region.layoutXProperty().bind(graphView.widthProperty().multiply(rand.nextDouble()));

            if (node instanceof Characterisation) {
                region.layoutXProperty().bind(createXBinding(region, (Characterisation) node, characterisationList));
                region.layoutYProperty().bind(graphView.heightProperty().multiply(0.1));
            }
            if (node instanceof Race) {
                region.layoutXProperty().bind(createXBinding(region, (Race) node, raceList));
                region.layoutYProperty().bind(graphView.heightProperty().multiply(0.3));
            }
            if (node instanceof Profession) {
                region.layoutXProperty().bind(createXBinding(region, (Profession) node, professionList));
                region.layoutYProperty().bind(graphView.heightProperty().multiply(0.5));
            }
            if (node instanceof FightingStyle) {
                region.layoutXProperty().bind(createXBinding(region, (FightingStyle) node, fightingStyleList));
                region.layoutYProperty().bind(graphView.heightProperty().multiply(0.7));
            }
            if (node instanceof Specialisation) {
                region.layoutXProperty().bind(createXBinding(region, (Specialisation) node, specialisationList));
                region.layoutYProperty().bind(graphView.heightProperty().multiply(0.9));
            }
        }
    }

    public <U extends GenerationBase> DoubleBinding createXBinding(Region region, U base, List<U> list) {
        return Bindings.max(region.widthProperty(), minSpacing).multiply(list.indexOf(base));
    }

    public <P extends TypedGenerationBase<U>, U extends GenerationBase> List<U> createChildList
            (Collection<P> parentList, Stream<U> fullStream) {
        List<U> childList = new ArrayList<>();

        for (P parent : parentList) {
            for (U subType : parent.getSubTypes()) {
                if (!childList.contains(subType)) {
                    childList.add(subType);
                }
            }
        }

        fullStream.forEach(base -> {
            if (!childList.contains(base)) {
                childList.add(base);
            }
        });

        return childList;
    }
}
