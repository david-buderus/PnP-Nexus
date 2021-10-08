package de.pnp.manager.ui.battle.state;

import de.pnp.manager.main.LanguageUtility;
import de.pnp.manager.model.Battle;
import de.pnp.manager.model.character.PnPCharacter;
import de.pnp.manager.model.character.data.AttackTypes;
import de.pnp.manager.model.character.state.IActiveRounderMemberState;
import de.pnp.manager.model.character.state.IAttackTypeMemberState;
import de.pnp.manager.model.character.state.interfaces.IPowerMemberStateImpl;
import de.pnp.manager.model.character.state.interfaces.IRandomPowerMemberStateImpl;
import de.pnp.manager.ui.View;
import de.pnp.manager.ui.battle.PnPCharacterPane;
import de.pnp.manager.ui.part.NumberField;
import de.pnp.manager.ui.part.UpdatingListCell;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.HashMap;

import static de.pnp.manager.ui.ViewFactory.labelRegion;
import static de.pnp.manager.ui.ViewFactory.labelTextField;

public class AllMemberStateView extends View {

    private final HashMap<PnPCharacter, PnPCharacterPane> panes;
    private final ListProperty<PnPCharacterPane> selected;

    public AllMemberStateView(Battle battle, PnPCharacter source) {
        super("allState.title");
        this.panes = new HashMap<>();
        this.selected = new SimpleListProperty<>(FXCollections.observableArrayList());

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        HBox headlineBox = new HBox();
        headlineBox.setAlignment(Pos.CENTER);
        headlineBox.setPadding(new Insets(0, 0, 5, 0));
        root.setTop(headlineBox);

        Label headline = new Label();
        headline.setFont(Font.font("", FontWeight.EXTRA_BOLD, 20));
        headlineBox.getChildren().add(headline);

        if (source != null) {
            headline.textProperty().bind(LanguageUtility.getMessageProperty("allState.distribution.of")
                    .concat(" ")
                    .concat(source.nameProperty()));
        } else {
            headline.textProperty().bind(LanguageUtility.getMessageProperty("allState.distribution"));
        }

        HBox memberPart = new HBox(10);
        root.setCenter(memberPart);

        VBox memberLists = new VBox(5);
        memberLists.setPadding(new Insets(10));
        memberLists.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(memberLists, Priority.ALWAYS);
        memberPart.getChildren().add(memberLists);

        Label playerLabel = new Label();
        playerLabel.textProperty().bind(LanguageUtility.getMessageProperty("players"));
        playerLabel.setFont(new Font(20));
        memberLists.getChildren().add(playerLabel);

        FlowPane players = new FlowPane();
        memberLists.getChildren().add(players);

        battle.playersProperty().addListener((ListChangeListener<PnPCharacter>) change -> {
            while (change.next()) {

                for (PnPCharacter character : change.getAddedSubList()) {
                    PnPCharacterPane pane = new PnPCharacterPane(character);
                    pane.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> select(pane));
                    panes.put(character, pane);

                    players.getChildren().add(pane);
                }

                for (PnPCharacter character : change.getRemoved()) {
                    players.getChildren().remove(panes.get(character));
                    panes.remove(character);
                }
            }
        });

        for (PnPCharacter character : battle.playersProperty()) {
            PnPCharacterPane pane = new PnPCharacterPane(character);
            pane.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> select(pane));
            panes.put(character, pane);

            players.getChildren().add(pane);
        }

        Label enemyLabel = new Label();
        enemyLabel.textProperty().bind(LanguageUtility.getMessageProperty("enemies"));
        enemyLabel.setFont(new Font(20));
        memberLists.getChildren().add(enemyLabel);

        FlowPane enemies = new FlowPane();
        memberLists.getChildren().add(enemies);

        battle.enemiesProperty().addListener((ListChangeListener<PnPCharacter>) change -> {
            while (change.next()) {

                for (PnPCharacter character : change.getAddedSubList()) {
                    PnPCharacterPane pane = new PnPCharacterPane(character);
                    pane.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> select(pane));
                    panes.put(character, pane);

                    enemies.getChildren().add(pane);
                }

                for (PnPCharacter character : change.getRemoved()) {
                    enemies.getChildren().remove(panes.get(character));
                    panes.remove(character);
                }
            }
        });

        for (PnPCharacter character : battle.enemiesProperty()) {
            PnPCharacterPane pane = new PnPCharacterPane(character);
            pane.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> select(pane));
            panes.put(character, pane);

            enemies.getChildren().add(pane);
        }

        Separator separator = new Separator();
        separator.setOrientation(Orientation.VERTICAL);
        memberPart.getChildren().add(separator);

        VBox info = new VBox(5);
        info.setPadding(new Insets(10));
        info.setAlignment(Pos.TOP_CENTER);
        info.setPrefSize(280, 240);
        memberPart.getChildren().add(info);

        Label infoLabel = new Label();
        infoLabel.textProperty().bind(LanguageUtility.getMessageProperty("state.info.create"));
        infoLabel.setFont(Font.font("", FontWeight.BOLD, 20));

        //Name
        StringProperty name = new SimpleStringProperty("");
        HBox nameBox = labelTextField("state.info.name", name);

        //Effect
        ComboBox<MemberStateFactory> effectComboBox = new ComboBox<>();
        effectComboBox.setCellFactory(list -> new UpdatingListCell<>());
        effectComboBox.setButtonCell(new UpdatingListCell<>());
        effectComboBox.setItems(FXCollections.observableArrayList(MemberStateFactory.FACTORIES));
        HBox effectBox = labelRegion("state.info.type", effectComboBox);

        //Power
        ComboBox<Dice> randomComboBox = new ComboBox<>();
        randomComboBox.setItems(FXCollections.observableArrayList(Dice.values()));
        randomComboBox.setCellFactory(list -> new UpdatingListCell<>());
        randomComboBox.setButtonCell(new UpdatingListCell<>());
        randomComboBox.getSelectionModel().selectFirst();

        FloatProperty powerProperty = new SimpleFloatProperty(0);
        HBox powerBox = labelTextField("state.info.power", powerProperty);

        NumberField powerField = new NumberField();
        powerField.numberProperty().bindBidirectional(powerProperty);
        HBox randomPowerBox = labelRegion("state.info.power", 55, randomComboBox, 95, powerField);

        //Duration
        IntegerProperty durationProperty = new SimpleIntegerProperty(1);

        HBox durationBox = labelTextField("state.info.duration", durationProperty);

        NumberField durationField = new NumberField();
        durationField.numberProperty().bindBidirectional(durationProperty);

        ComboBox<Rounds> activeComboBox = new ComboBox<>();
        activeComboBox.setButtonCell(new UpdatingListCell<>());
        activeComboBox.setCellFactory(list -> new UpdatingListCell<>());
        activeComboBox.setItems(FXCollections.observableArrayList(Rounds.values()));
        activeComboBox.getSelectionModel().selectFirst();

        HBox activeDurationBox = labelRegion("state.info.duration", 30, durationField, 120, activeComboBox);

        //Type
        ComboBox<AttackTypes> typeComboBox = new ComboBox<>();
        typeComboBox.setItems(FXCollections.observableArrayList(AttackTypes.values()));
        typeComboBox.setCellFactory(list -> new UpdatingListCell<>());
        typeComboBox.setButtonCell(new UpdatingListCell<>());
        typeComboBox.getSelectionModel().select(AttackTypes.DIRECT);
        HBox typeBox = labelRegion("state.info.target", typeComboBox);

        effectComboBox.getSelectionModel().selectedItemProperty().addListener((ob, o, factory) -> {
            info.getChildren().clear();
            info.getChildren().add(infoLabel);
            info.getChildren().add(nameBox);
            info.getChildren().add(effectBox);

            if (factory.getDefaultState() instanceof IPowerMemberStateImpl) {
                if (factory.getDefaultState() instanceof IRandomPowerMemberStateImpl) {
                    info.getChildren().add(randomPowerBox);
                } else {
                    info.getChildren().add(powerBox);
                }
            }
            if (factory.getDefaultState() instanceof IAttackTypeMemberState) {
                info.getChildren().add(typeBox);
            }
            if (factory.getDefaultState() instanceof IActiveRounderMemberState) {
                info.getChildren().add(activeDurationBox);
            } else {
                info.getChildren().add(durationBox);
            }

            Button createButton = new Button();
            createButton.textProperty().bind(LanguageUtility.getMessageProperty("state.info.add"));
            createButton.setPrefWidth(215);
            createButton.setOnAction(ev -> {
                for (PnPCharacterPane pane : selected) {
                    pane.getCharacter().addState(
                            effectComboBox.getValue().create(name.getName(), Integer.parseInt(durationField.getText()),
                                    activeComboBox.getValue() == Rounds.ACTIVE_ROUNDS, Float.parseFloat(powerField.getText()),
                                    randomComboBox.getValue() == Dice.WITH, typeComboBox.getValue(), source));
                }
                info.getChildren().add(createButton);
            });
            info.getChildren().add(createButton);
        });
        effectComboBox.getSelectionModel().selectFirst();

        //Buttons
        BorderPane buttonPane = new BorderPane();
        root.setBottom(buttonPane);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        show();
    }

    private void select(PnPCharacterPane pane) {
        pane.setPrimarySelected(!pane.isPrimarySelected());
        if (pane.isPrimarySelected()) {
            selected.add(pane);
        } else {
            selected.remove(pane);
        }
    }
}
