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
import model.Battle;
import model.member.BattleMember;
import model.member.MemberState;
import model.member.data.AttackTypes;
import model.member.data.MemberStateEffect;
import ui.View;

import java.util.HashMap;

import static ui.ViewFactory.labelRegion;
import static ui.ViewFactory.labelTextField;

public class AllMemberStateView extends View {

    private final HashMap<BattleMember, BattleMemberPane> panes;
    private final ListProperty<BattleMemberPane> selected;

    public AllMemberStateView(Battle battle, BattleMember source) {
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
            headline.textProperty().bind(
                    new ReadOnlyStringWrapper("Statusverteilung von ").concat(source.nameProperty()));
        } else {
            headline.textProperty().bind(new ReadOnlyStringWrapper("Statusverteilung"));
        }

        HBox memberPart = new HBox(10);
        root.setCenter(memberPart);

        VBox memberLists = new VBox(5);
        memberLists.setPadding(new Insets(10));
        memberLists.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(memberLists, Priority.ALWAYS);
        memberPart.getChildren().add(memberLists);

        Label playerLabel = new Label("Spieler");
        playerLabel.setFont(new Font(20));
        memberLists.getChildren().add(playerLabel);

        FlowPane players = new FlowPane();
        memberLists.getChildren().add(players);

        battle.playersProperty().addListener((ListChangeListener<? super BattleMember>) change -> {
            while (change.next()) {

                for (BattleMember member : change.getAddedSubList()) {
                    BattleMemberPane pane = new BattleMemberPane(member);
                    pane.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> select(pane));
                    panes.put(member, pane);

                    players.getChildren().add(pane);
                }

                for (BattleMember member : change.getRemoved()) {
                    players.getChildren().remove(panes.get(member));
                    panes.remove(member);
                }
            }
        });

        for (BattleMember member : battle.playersProperty()) {
            BattleMemberPane pane = new BattleMemberPane(member);
            pane.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> select(pane));
            panes.put(member, pane);

            players.getChildren().add(pane);
        }

        Label enemyLabel = new Label("Gegner");
        enemyLabel.setFont(new Font(20));
        memberLists.getChildren().add(enemyLabel);

        FlowPane enemies = new FlowPane();
        memberLists.getChildren().add(enemies);

        battle.enemiesProperty().addListener((ListChangeListener<? super BattleMember>) change -> {
            while (change.next()) {

                for (BattleMember member : change.getAddedSubList()) {
                    BattleMemberPane pane = new BattleMemberPane(member);
                    pane.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> select(pane));
                    panes.put(member, pane);

                    enemies.getChildren().add(pane);
                }

                for (BattleMember member : change.getRemoved()) {
                    enemies.getChildren().remove(panes.get(member));
                    panes.remove(member);
                }
            }
        });

        for (BattleMember member : battle.enemiesProperty()) {
            BattleMemberPane pane = new BattleMemberPane(member);
            pane.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> select(pane));
            panes.put(member, pane);

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
            createButton.setOnAction(ev -> {
                for (BattleMemberPane pane : selected) {
                    pane.getBattleMember().addState(
                            new MemberState(name.get(), effectComboBox.getValue(), Integer.parseInt(durationField.getText()),
                                    activeComboBox.getValue().equals("aktive Runden"), Double.parseDouble(powerField.getText()),
                                    randomComboBox.getValue().equals("D"), typeComboBox.getValue(), source));
                }
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

    private void select(BattleMemberPane pane) {
        pane.setPrimarySelected(!pane.isPrimarySelected());
        if (pane.isPrimarySelected()) {
            selected.add(pane);
        } else {
            selected.remove(pane);
        }
    }
}
