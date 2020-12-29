package ui.battle;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import manager.Database;
import model.Battle;
import model.member.generation.GenerationBase;
import model.member.generation.TypedGenerationBase;
import model.member.generation.specs.*;
import ui.View;
import ui.part.NumStringConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class SpawnView extends View {

    private static final Random random = new Random();

    private final List<SpawnBox> spawns;
    private final Battle battle;
    private final boolean enemy;

    public SpawnView(Battle battle, boolean enemy) {
        VBox root = new VBox(10);
        root.setAlignment(Pos.TOP_CENTER);

        this.spawns = new ArrayList<>();
        this.battle = battle;
        this.enemy = enemy;

        ScrollPane scroll = new ScrollPane();
        VBox.setVgrow(scroll, Priority.ALWAYS);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        root.getChildren().add(scroll);

        FlowPane flow = new FlowPane();
        flow.setPrefHeight(400);
        flow.setPadding(new Insets(10));
        scroll.setContent(flow);

        BorderPane buttons = new BorderPane();
        buttons.setPadding(new Insets(0, 10, 10, 10));
        root.getChildren().add(buttons);

        Button addButton = new Button("Hinzufügen");
        addButton.setPrefWidth(100);
        addButton.setOnAction(ev -> {
            SpawnBox box = new SpawnBox(battle.getAveragePlayerLevel(),
                    spawnBox -> {
                        flow.getChildren().remove(spawnBox);
                        spawns.remove(spawnBox);
                    });
            flow.getChildren().add(box);
            spawns.add(box);

        });
        buttons.setLeft(addButton);

        Button spawnButton = new Button("Spawnen");
        spawnButton.setPrefWidth(100);
        spawnButton.setOnAction(event -> spawn());
        buttons.setRight(spawnButton);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        show();
    }

    private void spawn() {
        for (SpawnBox box : spawns) {
            for (SpawnParameter parameter : box.getParameters()) {
                battle.spawnMember(enemy, parameter.level, parameter.characterisation, parameter.race,
                        parameter.profession, parameter.fightingStyle, parameter.specialisation);
            }
        }
    }

    private static class SpawnBox extends VBox {

        private final IntegerProperty amount;
        private final DoubleProperty level;
        private final DoubleProperty fluctuation;
        private final ObjectProperty<Characterisation> characterisation;
        private final ObjectProperty<Race> race;
        private final ObjectProperty<Profession> profession;
        private final ObjectProperty<FightingStyle> fightingType;
        private final ObjectProperty<Specialisation> specificType;
        private final BooleanBinding randomCharacterisation;
        private final BooleanBinding randomRace;
        private final BooleanBinding randomProfession;
        private final BooleanBinding randomFightingType;
        private final BooleanBinding randomSpecificType;

        public SpawnBox(double level, Consumer<SpawnBox> removeCallback) {
            super(5);
            this.amount = new SimpleIntegerProperty(1);
            this.level = new SimpleDoubleProperty(level);
            this.fluctuation = new SimpleDoubleProperty(level / 3);
            this.characterisation = new SimpleObjectProperty<>();
            this.race = new SimpleObjectProperty<>();
            this.profession = new SimpleObjectProperty<>();
            this.fightingType = new SimpleObjectProperty<>();
            this.specificType = new SimpleObjectProperty<>();
            this.setPrefWidth(150);
            this.setPadding(new Insets(5));
            this.setAlignment(Pos.CENTER);
            this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY, BorderWidths.DEFAULT)));

            VBox labelBox = new VBox(1);
            this.getChildren().add(labelBox);

            labelBox.getChildren().add(createLabelInteger("Menge", this.amount));
            labelBox.getChildren().add(createLabelInteger("Level", this.level));
            labelBox.getChildren().add(createLabelInteger("Abweichung", this.fluctuation));


            ObservableList<Race> races = FXCollections.observableArrayList();
            ObservableList<Profession> professions = FXCollections.observableArrayList();
            ObservableList<FightingStyle> fightingStyles = FXCollections.observableArrayList();
            ObservableList<Specialisation> specialisations = FXCollections.observableArrayList();

            this.addDefaultListener(characterisation, races);
            this.addDefaultListener(race, professions);
            this.addDefaultListener(profession, fightingStyles);
            this.addDefaultListener(fightingType, specialisations);

            this.randomCharacterisation = createSelectBox(characterisation, Database.characterisationList, this);
            this.randomRace = createSelectBox(race, races, this, randomCharacterisation);
            this.randomProfession = createSelectBox(profession, professions, this, randomRace);
            this.randomFightingType = createSelectBox(fightingType, fightingStyles, this, randomProfession);
            this.randomSpecificType = createSelectBox(specificType, specialisations, this, randomFightingType);

            Button removeButton = new Button("Entfernen");
            removeButton.prefWidthProperty().bind(this.widthProperty().subtract(10));
            removeButton.setOnAction(ev -> removeCallback.accept(this));
            this.getChildren().add(removeButton);
        }

        private <T extends GenerationBase> void addDefaultListener(ObjectProperty<? extends TypedGenerationBase<T>> property, ObservableList<T> list) {
            property.addListener((ob, o, n) -> {
                list.clear();
                if (n != null) {
                    list.addAll(n.getSubTypes());
                }
            });
        }

        private HBox createLabelInteger(String text, Property<Number> property) {
            HBox box = new HBox(5);
            box.setAlignment(Pos.CENTER);

            Label label = new Label(text);
            box.getChildren().add(label);

            TextField field = new TextField();
            field.textProperty().bindBidirectional(property, new NumStringConverter(1));
            field.setPrefWidth(50);
            box.getChildren().add(field);

            label.prefWidthProperty().bind(box.widthProperty().subtract(10).subtract(field.widthProperty()));

            return box;
        }

        private <T> BooleanBinding createSelectBox(ObjectProperty<T> property, ObservableList<T> list,
                                                   Pane parent) {
            return createSelectBox(property, list, parent, new BooleanBinding() {
                @Override
                protected boolean computeValue() {
                    return false;
                }
            });
        }

        private <T> BooleanBinding createSelectBox(ObjectProperty<T> property, ObservableList<T> list,
                                                   Pane parent, BooleanBinding lock) {
            HBox box = new HBox(5);
            box.setAlignment(Pos.CENTER);
            parent.getChildren().add(box);

            ComboBox<T> comboBox = new ComboBox<>();
            comboBox.setItems(list);
            property.bind(comboBox.getSelectionModel().selectedItemProperty());
            box.getChildren().add(comboBox);

            CheckBox checkBox = new CheckBox();
            checkBox.setPrefWidth(25);
            lock.addListener((ob, o, n) -> {
                if (n) {
                    checkBox.setSelected(false);
                }
            });
            checkBox.disableProperty().bind(lock);
            box.getChildren().add(checkBox);

            comboBox.disableProperty().bind(checkBox.selectedProperty().not());
            comboBox.prefWidthProperty().bind(box.widthProperty().subtract(10).subtract(checkBox.widthProperty()));

            return checkBox.selectedProperty().not();
        }

        public Collection<SpawnParameter> getParameters() {
            Collection<SpawnParameter> spawnParameters = new ArrayList<>();

            for (int i = 0; i < amount.get(); i++) {
                SpawnParameter spawnParameter = new SpawnParameter();
                spawnParameter.level = (int) Math.round(this.level.get() + random.nextGaussian() * fluctuation.get() / 3);
                if (randomCharacterisation.get()) {
                    spawnParameter.characterisation = Database.characterisationList.get(random.nextInt(Database.characterisationList.size()));
                } else {
                    spawnParameter.characterisation = characterisation.get();
                }
                if (randomRace.get()) {
                    spawnParameter.race = spawnParameter.characterisation.getRandomSubType();
                } else {
                    spawnParameter.race = race.get();
                }
                if (randomProfession.get()) {
                    spawnParameter.profession = spawnParameter.race.getRandomSubType();
                } else {
                    spawnParameter.profession = profession.get();
                }
                if (randomFightingType.get()) {
                    spawnParameter.fightingStyle = spawnParameter.profession.getRandomSubType();
                } else {
                    spawnParameter.fightingStyle = fightingType.get();
                }
                if (randomSpecificType.get()) {
                    spawnParameter.specialisation = spawnParameter.fightingStyle.getRandomSubType();
                } else {
                    spawnParameter.specialisation = specificType.get();
                }

                spawnParameters.add(spawnParameter);
            }

            return spawnParameters;
        }
    }

    private static class SpawnParameter {
        public int level;
        public Characterisation characterisation;
        public Race race;
        public Profession profession;
        public FightingStyle fightingStyle;
        public Specialisation specialisation;
    }
}
