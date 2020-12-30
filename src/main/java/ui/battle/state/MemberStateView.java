package ui.battle.state;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import manager.LanguageUtility;
import model.member.BattleMember;
import model.member.MemberState;
import model.member.data.AttackTypes;
import model.member.data.MemberStateEffect;
import ui.View;
import ui.part.NumberField;
import ui.part.UpdatingListCell;

import java.util.HashMap;
import java.util.Objects;

import static ui.ViewFactory.labelRegion;
import static ui.ViewFactory.labelTextField;

public class MemberStateView extends View {

    private final HashMap<MemberState, MemberStatePane> panes;
    private final ObjectProperty<MemberStatePane> selected;

    public MemberStateView(BattleMember target, BattleMember source) {
        super("state.title");
        this.panes = new HashMap<>();
        this.selected = new SimpleObjectProperty<>();

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
            headline.textProperty().bind(
                    LanguageUtility.getMessageProperty("state.target")
                            .concat(" ")
                            .concat(target.nameProperty())
                            .concat(". ")
                            .concat(LanguageUtility.getMessageProperty("state.source"))
                            .concat(": ")
                            .concat(source.nameProperty()));
        } else {
            headline.textProperty().bind(
                    LanguageUtility.getMessageProperty("state.target")
                            .concat(" ")
                            .concat(target.nameProperty()));
        }

        HBox memberPart = new HBox(10);
        root.setCenter(memberPart);

        VBox memberLists = new VBox(5);
        memberLists.setPadding(new Insets(10));
        memberLists.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(memberLists, Priority.ALWAYS);
        memberPart.getChildren().add(memberLists);

        Label playerLabel = new Label();
        playerLabel.textProperty().bind(LanguageUtility.getMessageProperty("state.effects"));
        playerLabel.setFont(new Font(20));
        memberLists.getChildren().add(playerLabel);

        FlowPane states = new FlowPane();
        memberLists.getChildren().add(states);

        target.statesProperty().addListener((ListChangeListener<? super MemberState>) change -> {
            while (change.next()) {

                for (MemberState state : change.getAddedSubList()) {
                    MemberStatePane pane = new MemberStatePane(state);
                    pane.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> select(pane));
                    panes.put(state, pane);

                    states.getChildren().add(pane);
                }

                for (MemberState member : change.getRemoved()) {
                    states.getChildren().remove(panes.get(member));
                    panes.remove(member);
                }
            }
        });

        for (MemberState state : target.statesProperty()) {
            MemberStatePane pane = new MemberStatePane(state);
            pane.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> select(pane));
            panes.put(state, pane);

            states.getChildren().add(pane);
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
        ComboBox<MemberStateEffect> effectComboBox = new ComboBox<>();
        effectComboBox.setItems(FXCollections.observableArrayList(MemberStateEffect.values()));
        effectComboBox.setCellFactory(list -> new UpdatingListCell<>());
        effectComboBox.setButtonCell(new UpdatingListCell<>());
        HBox effectBox = labelRegion("state.info.type", effectComboBox);

        //Power
        ComboBox<Dice> randomComboBox = new ComboBox<>();
        randomComboBox.setItems(FXCollections.observableArrayList(Dice.values()));
        randomComboBox.setCellFactory(list -> new UpdatingListCell<>());
        randomComboBox.setButtonCell(new UpdatingListCell<>());
        randomComboBox.getSelectionModel().selectFirst();

        TextField powerField = new NumberField();
        HBox powerBox = labelRegion("state.info.power", 55, randomComboBox, 95, powerField);

        //Duration
        TextField durationField = new NumberField(1);

        ComboBox<Rounds> activeComboBox = new ComboBox<>();
        activeComboBox.setButtonCell(new UpdatingListCell<>());
        activeComboBox.setCellFactory(list -> new UpdatingListCell<>());
        activeComboBox.setItems(FXCollections.observableArrayList(Rounds.values()));
        activeComboBox.getSelectionModel().selectFirst();
        HBox durationBox = labelRegion("state.info.duration", 30, durationField, 120, activeComboBox);

        //Type
        ComboBox<AttackTypes> typeComboBox = new ComboBox<>();
        typeComboBox.setItems(FXCollections.observableArrayList(AttackTypes.values()));
        typeComboBox.setCellFactory(list -> new UpdatingListCell<>());
        typeComboBox.setButtonCell(new UpdatingListCell<>());
        typeComboBox.getSelectionModel().select(AttackTypes.direct);
        HBox typeBox = labelRegion("state.info.target", typeComboBox);

        effectComboBox.getSelectionModel().selectedItemProperty().addListener((ob, o, effect) -> {
            info.getChildren().clear();
            info.getChildren().add(infoLabel);
            info.getChildren().add(nameBox);
            info.getChildren().add(effectBox);


            switch (effect) {
                case damage:
                    info.getChildren().add(powerBox);
                    info.getChildren().add(typeBox);
                    info.getChildren().add(durationBox);
                    break;
                case heal:
                case manaDrain:
                case manaRegeneration:
                case speed:
                case relativeSpeed:
                case slow:
                case relativeSlow:
                case armorPlus:
                case armorMinus:
                    info.getChildren().add(powerBox);
                    info.getChildren().add(durationBox);
                    break;
                default:
                    info.getChildren().add(durationBox);
                    break;
            }

            Button createButton = new Button();
            createButton.textProperty().bind(LanguageUtility.getMessageProperty("state.info.add"));
            createButton.setPrefWidth(215);
            createButton.setOnAction(ev ->
                    target.addState(
                            new MemberState(name.get(), effectComboBox.getValue(), Integer.parseInt(durationField.getText()),
                                    activeComboBox.getValue() == Rounds.activeRounds, Double.parseDouble(powerField.getText()),
                                    randomComboBox.getValue() == Dice.with, typeComboBox.getValue(), Objects.requireNonNullElse(source, target))));
            info.getChildren().add(createButton);
        });
        effectComboBox.getSelectionModel().selectFirst();

        //Buttons
        BorderPane buttonPane = new BorderPane();
        root.setBottom(buttonPane);

        Button removeButton = new Button();
        removeButton.textProperty().bind(LanguageUtility.getMessageProperty("state.button.remove"));
        buttonPane.setCenter(removeButton);
        removeButton.setOnAction(ev -> {
            if (selected.get() != null) {
                target.removeState(selected.get().getState());
            }
        });

        Scene scene = new Scene(root);
        stage.setScene(scene);
        show();
    }

    private void select(MemberStatePane pane) {
        if (selected.isNotNull().get()) {
            selected.get().setSelected(false);
        }
        if (pane != null) {
            pane.setSelected(true);
        }
        selected.set(pane);
    }
}
