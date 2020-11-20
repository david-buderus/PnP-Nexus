package ui.battle;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import model.Battle;
import model.loot.LootTable;
import model.member.BattleMember;
import model.member.ExtendedBattleMember;
import model.member.data.AttackTypes;
import ui.IView;
import ui.ViewPart;
import ui.part.NumStringConverter;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import static ui.ViewFactory.labelRegion;
import static ui.ViewFactory.labelTextField;

public class BattleView extends ViewPart {

    private final Battle battle;

    private final HashMap<BattleMember, BattleMemberPane> panes;
    private final ObjectProperty<BattleMemberPane> selectedSource;
    private final ObjectProperty<BattleMemberPane> selectedTarget;

    private final IntegerProperty damage;
    private final IntegerProperty heal;
    private final IntegerProperty penetration;

    private final FlowPane players;
    private final FlowPane enemies;

    public BattleView(IView parent) {
        super("Kampfhelfer", parent);
        this.battle = new Battle();
        this.panes = new HashMap<>();
        this.selectedSource = new SimpleObjectProperty<>();
        this.selectedTarget = new SimpleObjectProperty<>();

        this.damage = new SimpleIntegerProperty(0);
        this.heal = new SimpleIntegerProperty(0);
        this.penetration = new SimpleIntegerProperty(0);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        HBox headlineBox = new HBox();
        headlineBox.setAlignment(Pos.CENTER);
        headlineBox.setPadding(new Insets(0, 0, 5, 0));
        root.setTop(headlineBox);

        Label headline = new Label("Kampf in Runde 1");
        battle.roundProperty().addListener((ob, o, n) -> headline.setText("Kampf in Runde " + n.intValue()));
        headline.setFont(Font.font("", FontWeight.EXTRA_BOLD, 20));
        headlineBox.getChildren().add(headline);

        HBox memberPart = new HBox(10);
        root.setCenter(memberPart);

        ScrollPane memberScroll = new ScrollPane();
        memberScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        memberScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        memberScroll.setBackground(Background.EMPTY);
        memberScroll.setFitToWidth(true);
        memberScroll.setPrefWidth(600);
        memberPart.getChildren().add(memberScroll);

        VBox memberLists = new VBox(5);
        memberLists.setPadding(new Insets(10));
        memberLists.setAlignment(Pos.TOP_LEFT);
        memberScroll.setContent(memberLists);

        Label playerLabel = new Label("Spieler");
        playerLabel.setFont(new Font(20));
        memberLists.getChildren().add(playerLabel);

        this.players = new FlowPane();
        memberLists.getChildren().add(players);

        battle.playersProperty().addListener((ListChangeListener<? super BattleMember>) change -> {
            while (change.next()) {

                for (BattleMember member : change.getAddedSubList()) {
                    BattleMemberPane pane = new BattleMemberPane(member);
                    pane.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                        if (event.isControlDown()) {
                            selectSource(pane);
                        } else {
                            selectTarget(pane);
                        }
                    });
                    panes.put(member, pane);

                    players.getChildren().add(pane);
                }

                for (BattleMember member : change.getRemoved()) {
                    players.getChildren().remove(panes.get(member));
                    panes.remove(member);
                }
            }
        });

        Label enemyLabel = new Label("Gegner");
        enemyLabel.setFont(new Font(20));
        memberLists.getChildren().add(enemyLabel);

        this.enemies = new FlowPane();
        memberLists.getChildren().add(enemies);

        battle.enemiesProperty().addListener((ListChangeListener<? super BattleMember>) change -> {
            while (change.next()) {

                for (BattleMember member : change.getAddedSubList()) {
                    BattleMemberPane pane = new BattleMemberPane(member);
                    pane.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                        if (event.isControlDown()) {
                            selectSource(pane);
                        } else {
                            selectTarget(pane);
                        }
                    });
                    panes.put(member, pane);

                    enemies.getChildren().add(pane);
                }

                for (BattleMember member : change.getRemoved()) {
                    enemies.getChildren().remove(panes.get(member));
                    panes.remove(member);
                }
            }
        });

        Separator separator = new Separator();
        separator.setOrientation(Orientation.VERTICAL);
        memberPart.getChildren().add(separator);

        VBox info = new VBox(5);
        info.setPadding(new Insets(10));
        info.setAlignment(Pos.TOP_CENTER);
        info.setPrefSize(280, 520);
        memberPart.getChildren().add(info);

        Label infoLabel = new Label("Info");
        infoLabel.setFont(Font.font("", FontWeight.BOLD, 20));
        info.getChildren().add(infoLabel);

        Label emptyLabel = new Label("Nichts ausgewählt");
        emptyLabel.setAlignment(Pos.CENTER);
        info.getChildren().add(emptyLabel);

        HBox damageBox = labelTextField("Schaden:", damage);
        HBox penBox = labelTextField("Pen. in %:", penetration);
        HBox healBox = labelTextField("Heilung", heal);

        ComboBox<AttackTypes> attackCombo = new ComboBox<>();
        attackCombo.setItems(FXCollections.observableArrayList(AttackTypes.values()));
        attackCombo.getSelectionModel().select(AttackTypes.upperBody);
        HBox attackBox = labelRegion("Ziel:", attackCombo);

        TextField blockField = new TextField("1");

        ComboBox<shieldEnum> blockCombo = new ComboBox<>();
        blockCombo.setItems(FXCollections.observableArrayList(shieldEnum.values()));
        blockCombo.getSelectionModel().select(shieldEnum.without);

        HBox blockBox = labelRegion("Block:", 40, blockField, 110, blockCombo);

        ChangeListener<BattleMemberPane> changeListener = (ob, o, memberPane) -> {
            info.getChildren().clear();
            info.getChildren().add(infoLabel);

            final BattleMember target = selectedTarget.get() != null ? selectedTarget.get().getBattleMember() : null;
            final BattleMember source = selectedSource.get() != null ? selectedSource.get().getBattleMember() : null;

            if (target == null) {
                info.getChildren().add(emptyLabel);
                getStage().sizeToScene();
                return;
            }

            TextField nameField = new TextField();
            nameField.textProperty().bindBidirectional(target.nameProperty());

            TextField lvlField = new TextField();
            lvlField.textProperty().bindBidirectional(target.levelProperty(), new NumStringConverter());

            info.getChildren().add(labelRegion("Name", 120, nameField, 30, lvlField));
            info.getChildren().add(labelTextField("Leben: ", target.lifeProperty(), target.maxLifeProperty()));
            info.getChildren().add(labelTextField("Mana: ", target.manaProperty(), target.maxManaProperty()));
            info.getChildren().add(labelTextField("Init: ", target.initiativeProperty()));
            info.getChildren().add(labelTextField("Zähler: ", target.counterProperty()));
            info.getChildren().add(labelTextField("Start: ", target.startValueProperty()));

            Button armorButton;
            if (target instanceof ExtendedBattleMember) {
                armorButton = new Button("Charakterbogen");
                armorButton.setPrefWidth(215);
                armorButton.setOnAction(ev -> new CharacterView((ExtendedBattleMember) target));
            } else {
                armorButton = new Button("Rüstung");
                armorButton.setPrefWidth(215);
                armorButton.setOnAction(ev -> new MemberArmorView(target));
            }
            info.getChildren().add(armorButton);

            Button statusButton = new Button("Status");
            statusButton.setPrefWidth(215);
            statusButton.setOnAction(ev -> new MemberStateView(target, source));
            info.getChildren().add(statusButton);

            if (source != null) {
                if (battle.isSameTeam(source, target)) {

                    Label battleInfo = new Label();
                    battleInfo.setPadding(new Insets(30, 0, 5, 0));
                    battleInfo.textProperty().bind(
                            source.nameProperty().concat(" heilt ").concat(target.nameProperty()));
                    battleInfo.setFont(Font.font("", FontWeight.BOLD, 12));
                    info.getChildren().add(battleInfo);

                    info.getChildren().add(healBox);

                    Button healButton = new Button("Heilen");
                    healButton.setPrefWidth(215);
                    healButton.setOnAction(ev -> target.heal(heal.get(), source));
                    info.getChildren().add(healButton);

                } else {
                    if (source != target) {

                        Label battleInfo = new Label();
                        battleInfo.setPadding(new Insets(30, 0, 5, 0));
                        battleInfo.textProperty().bind(
                                source.nameProperty().concat(" greift ").concat(target.nameProperty()).concat(" an."));
                        battleInfo.setFont(Font.font("", FontWeight.BOLD, 12));
                        info.getChildren().add(battleInfo);

                        info.getChildren().add(damageBox);
                        info.getChildren().add(penBox);
                        info.getChildren().add(attackBox);
                        info.getChildren().add(blockBox);

                        Button attackButton = new Button("Angreifen");
                        attackButton.setPrefWidth(215);
                        attackButton.setOnAction(ev -> target.takeDamage(
                                damage.get(), attackCombo.getSelectionModel().getSelectedItem(),
                                blockCombo.getSelectionModel().getSelectedItem().toBool(),
                                (double) penetration.get() / 100, Double.parseDouble(blockField.getText()), source));
                        info.getChildren().add(attackButton);
                    }
                }
            }
        };
        selectedTarget.addListener(changeListener);
        selectedSource.addListener(changeListener);

        //Buttons
        BorderPane buttonPane = new BorderPane();
        buttonPane.setPadding(new Insets(20, 0, 0, 0));
        root.setBottom(buttonPane);

        //Left
        GridPane createButtons = new GridPane();
        createButtons.setHgap(5);
        createButtons.setVgap(5);
        buttonPane.setLeft(createButtons);

        Button playerButton = new Button("Neuer Spieler");
        playerButton.setPrefWidth(110);
        playerButton.setOnAction(ev -> battle.createPlayer());
        createButtons.add(playerButton, 0, 0);

        Button loadPlayerButton = new Button("Spieler Laden");
        loadPlayerButton.setPrefWidth(110);
        loadPlayerButton.setOnAction(ev -> load(false));
        createButtons.add(loadPlayerButton, 1, 0);

        Button spawnPlayerButton = new Button("Spieler Spawnen");
        spawnPlayerButton.setPrefWidth(110);
        spawnPlayerButton.setOnAction(ev -> spawn(false));
        createButtons.add(spawnPlayerButton, 2, 0);

        Button cloneButton = new Button("Klonen");
        cloneButton.setPrefWidth(110);
        cloneButton.setOnAction(ev -> cloneMember());
        createButtons.add(cloneButton, 3, 0);

        cloneButton.setDisable(true);
        selectedTarget.addListener((ob, o, n) -> cloneButton.setDisable(n == null));

        Button enemyButton = new Button("Neuer Gegner");
        enemyButton.setPrefWidth(110);
        enemyButton.setOnAction(ev -> battle.createEnemy());
        createButtons.add(enemyButton, 0, 1);

        Button loadEnemyButton = new Button("Gegner Laden");
        loadEnemyButton.setPrefWidth(110);
        loadEnemyButton.setOnAction(ev -> load(true));
        createButtons.add(loadEnemyButton, 1, 1);

        Button spawnEnemyButton = new Button("Gegner Spawnen");
        spawnEnemyButton.setPrefWidth(110);
        spawnEnemyButton.setOnAction(ev -> spawn(true));
        createButtons.add(spawnEnemyButton, 2, 1);

        Button removeButton = new Button("Entfernen");
        removeButton.setPrefWidth(110);
        removeButton.setOnAction(ev -> removeMember());
        createButtons.add(removeButton, 3, 1);

        removeButton.setDisable(true);
        selectedTarget.addListener((ob, o, n) -> removeButton.setDisable(n == null));

        //Middle
        Button nextTurnButton = new Button("Nächster Zug");
        nextTurnButton.setPrefWidth(110);
        nextTurnButton.setOnAction(ev -> battle.nextTurn());
        buttonPane.setCenter(nextTurnButton);

        //Right
        GridPane utilityButtons = new GridPane();
        utilityButtons.setHgap(5);
        utilityButtons.setVgap(5);
        buttonPane.setRight(utilityButtons);

        Button lootButton = new Button("Plündern");
        lootButton.setPrefWidth(110);
        lootButton.setOnAction(event -> loot());
        utilityButtons.add(lootButton, 0, 0);

        Button statisticButton = new Button("Statistik");
        statisticButton.setPrefWidth(110);
        statisticButton.setOnAction(event -> new StatisticView(battle.playersProperty(), battle));
        utilityButtons.add(statisticButton, 1, 0);

        Button resetButton = new Button("Reset");
        resetButton.setPrefWidth(110);
        resetButton.setOnAction(event -> reset());
        utilityButtons.add(resetButton, 0, 1);

        Button allStatusButton = new Button("Flächenstatus");
        allStatusButton.setPrefWidth(110);
        allStatusButton.setOnAction(event -> {
            BattleMember source = selectedSource.get() != null ? selectedSource.get().getBattleMember() : null;
            new AllMemberStateView(battle, source);
        });
        utilityButtons.add(allStatusButton, 1, 1);

        this.setContent(root);
    }

    private void reset() {
        selectTarget(null);
        selectSource(null);
        battle.reset();
    }

    private void loot() {
        LootTable lootTable = new LootTable();

        for (BattleMember member : battle.enemiesProperty()) {
            lootTable.add(member.getLootTable());
        }

        Collection<LootView.EXP> expCollection = new LinkedList<>();

        for (BattleMember player : battle.playersProperty()) {
            double amount = 0;

            for (BattleMember enemy : battle.enemiesProperty()) {
                int level = enemy.getLevel();

                if (enemy.getTier() > player.getTier()) {
                    amount += (level - 1 + Math.pow(5, enemy.getTier() - player.getTier()))
                            / battle.playersProperty().size();
                } else if (enemy.getTier() >= player.getTier() - 1) {
                    amount += (double) level / battle.playersProperty().size();
                }
            }

            LootView.EXP exp = new LootView.EXP();
            exp.player = player.getName();
            exp.amount = (int) Math.ceil(amount);

            expCollection.add(exp);
        }

        new LootView(lootTable.getLoot(), expCollection, battle.playersProperty().getSize());
    }

    private void removeMember() {
        if (selectedTarget.get() != null) {
            battle.removeMember(selectedTarget.get().getBattleMember());
            players.getChildren().remove(selectedTarget.get());
            enemies.getChildren().remove(selectedTarget.get());
            selectTarget(null);
        }
    }

    private void cloneMember() {
        if (selectedTarget.get() != null) {
            BattleMember member = selectedTarget.get().getBattleMember();
            if (battle.isPlayer(member)) {
                battle.createPlayer(member);
            } else {
                battle.createEnemy(member);
            }
        }
    }

    private void selectTarget(BattleMemberPane pane) {
        if (selectedTarget.get() != null) {
            selectedTarget.get().setPrimarySelected(false);
        }
        selectedTarget.set(pane);
        if (pane != null) {
            pane.setPrimarySelected(true);
        }
    }

    private void selectSource(BattleMemberPane pane) {
        if (selectedSource.get() != null) {
            selectedSource.get().setSecondarySelected(false);
        }
        selectedSource.set(pane);
        if (pane != null) {
            pane.setSecondarySelected(true);
        }
    }

    private void load(boolean enemy) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Exeldatei", "*.xlsx"),
                new FileChooser.ExtensionFilter("Alle Dateien", "*.*"));
        battle.load(chooser.showOpenDialog(getStage()), enemy);
    }

    private void spawn(boolean enemy) {
        new SpawnView(battle, enemy);
    }

    private enum shieldEnum {
        with, without;

        @Override
        public String toString() {
            switch (this) {
                case with:
                    return "Mit Schild";
                case without:
                    return "Ohne Schild";
                default:
                    return "";
            }
        }

        public boolean toBool() {
            return this == shieldEnum.with;
        }
    }
}
