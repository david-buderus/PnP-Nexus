package ui.battle;

import javafx.beans.property.*;
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
import model.member.BattleMember;
import model.member.MemberState;
import model.member.data.AttackTypes;
import model.member.data.MemberStateEffect;
import ui.View;

import java.util.HashMap;
import java.util.Objects;

import static ui.ViewFactory.labelRegion;
import static ui.ViewFactory.labelTextField;

public class MemberStateView extends View {

    private final HashMap<MemberState, MemberStatePane> panes;
    private final ObjectProperty<MemberStatePane> selected;

    public MemberStateView(BattleMember target, BattleMember source) {
        this.stage.setTitle("Effekte");
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
                    new ReadOnlyStringWrapper("Status von ").concat(target.nameProperty())
                            .concat(". Angreifer: ").concat(source.nameProperty()));
        } else {
            headline.textProperty().bind(
                    new ReadOnlyStringWrapper("Status von ").concat(target.nameProperty()));
        }

        HBox memberPart = new HBox(10);
        root.setCenter(memberPart);

        VBox memberLists = new VBox(5);
        memberLists.setPadding(new Insets(10));
        memberLists.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(memberLists, Priority.ALWAYS);
        memberPart.getChildren().add(memberLists);

        Label playerLabel = new Label("Effekte");
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

        Label infoLabel = new Label("Erstellen");
        infoLabel.setFont(Font.font("", FontWeight.BOLD, 20));

        //Name
        StringProperty name = new SimpleStringProperty("");
        HBox nameBox = labelTextField("Name:", name);

        //Effelt
        ComboBox<MemberStateEffect> effectComboBox = new ComboBox<>();
        effectComboBox.setItems(FXCollections.observableArrayList(MemberStateEffect.values()));
        HBox effectBox = labelRegion("Art: ", effectComboBox);

        //Power
        ComboBox<String> randomComboBox = new ComboBox<>();
        randomComboBox.setItems(FXCollections.observableArrayList("", "D"));
        randomComboBox.getSelectionModel().selectFirst();

        TextField powerField = new TextField("0");
        HBox powerBox = labelRegion("Stärke:", 55, randomComboBox, 95, powerField);

        //Duration
        TextField durationField = new TextField("1");

        ComboBox<String> activeComboBox = new ComboBox<>();
        activeComboBox.setItems(FXCollections.observableArrayList("Runden", "aktive Runden"));
        activeComboBox.getSelectionModel().selectFirst();
        HBox durationBox = labelRegion("Dauer:", 30, durationField, 120, activeComboBox);

        //Type
        ComboBox<AttackTypes> typeComboBox = new ComboBox<>();
        typeComboBox.setItems(FXCollections.observableArrayList(AttackTypes.values()));
        typeComboBox.getSelectionModel().select(AttackTypes.direct);
        HBox typeBox = labelRegion("Ziel:", typeComboBox);

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

            Button createButton = new Button("Hinzufügen");
            createButton.setPrefWidth(215);
            createButton.setOnAction(ev ->
                    target.addState(
                            new MemberState(name.get(), effectComboBox.getValue(), Integer.parseInt(durationField.getText()),
                                    activeComboBox.getValue().equals("aktive Runden"), Double.parseDouble(powerField.getText()),
                                    randomComboBox.getValue().equals("D"), typeComboBox.getValue(), Objects.requireNonNullElse(source, target))));
            info.getChildren().add(createButton);
        });
        effectComboBox.getSelectionModel().selectFirst();

        //Buttons
        BorderPane buttonPane = new BorderPane();
        root.setBottom(buttonPane);

        Button removeButton = new Button("Entfernen");
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
