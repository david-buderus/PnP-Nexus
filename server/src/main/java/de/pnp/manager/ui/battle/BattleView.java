package de.pnp.manager.ui.battle;

import de.pnp.manager.main.LanguageUtility;
import de.pnp.manager.model.Battle;
import de.pnp.manager.model.interfaces.ILootable;
import de.pnp.manager.model.interfaces.WithToStringProperty;
import de.pnp.manager.model.loot.LootTable;
import de.pnp.manager.model.manager.BattleHandler;
import de.pnp.manager.model.character.PnPCharacter;
import de.pnp.manager.model.character.data.AttackTypes;
import de.pnp.manager.ui.ConfigurableViewPart;
import de.pnp.manager.ui.IView;
import de.pnp.manager.ui.battle.state.AllMemberStateView;
import de.pnp.manager.ui.battle.state.MemberStateView;
import de.pnp.manager.ui.part.NumStringConverter;
import de.pnp.manager.ui.part.UpdatingListCell;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.*;
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

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import static de.pnp.manager.ui.ViewFactory.labelRegion;
import static de.pnp.manager.ui.ViewFactory.labelTextField;

public class BattleView extends ConfigurableViewPart {

    private final Battle battle;

    private final HashMap<PnPCharacter, PnPCharacterPane> panes;
    private final ObjectProperty<PnPCharacterPane> selectedSource;
    private final ObjectProperty<PnPCharacterPane> selectedTarget;

    private final IntegerProperty damage;
    private final IntegerProperty heal;
    private final IntegerProperty penetration;

    private final FlowPane players;
    private final FlowPane enemies;

    public BattleView(IView parent, BattleHandler battleHandler) {
        super(parent);
        this.battle = battleHandler.createBattle();
        this.nameProperty().bindBidirectional(battle.nameProperty());
        this.panes = new HashMap<>();
        this.selectedSource = new SimpleObjectProperty<>();
        this.selectedTarget = new SimpleObjectProperty<>();
        this.setClosable(true);

        this.damage = new SimpleIntegerProperty(0);
        this.heal = new SimpleIntegerProperty(0);
        this.penetration = new SimpleIntegerProperty(0);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        HBox headlineBox = new HBox();
        headlineBox.setAlignment(Pos.CENTER);
        headlineBox.setPadding(new Insets(0, 0, 5, 0));
        root.setTop(headlineBox);

        Label headline = new Label();
        headline.textProperty().bind(LanguageUtility.getMessageProperty("battle.round").concat(" ").concat(battle.roundProperty()));
        headline.setFont(Font.font("", FontWeight.EXTRA_BOLD, 20));
        headlineBox.getChildren().add(headline);

        BorderPane memberPart = new BorderPane();
        root.setCenter(memberPart);

        ScrollPane memberScroll = new ScrollPane();
        memberScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        memberScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        memberScroll.setBackground(Background.EMPTY);
        memberScroll.setFitToWidth(true);
        memberPart.setCenter(memberScroll);

        VBox memberLists = new VBox(5);
        memberLists.setPadding(new Insets(10));
        memberLists.setAlignment(Pos.TOP_LEFT);
        memberScroll.setContent(memberLists);

        Label playerLabel = new Label();
        playerLabel.textProperty().bind(LanguageUtility.getMessageProperty("players"));
        playerLabel.setFont(new Font(20));
        memberLists.getChildren().add(playerLabel);

        this.players = new FlowPane();
        memberLists.getChildren().add(players);

        battle.playersProperty().addListener((ListChangeListener<PnPCharacter>) change -> {
            while (change.next()) {

                for (PnPCharacter character : change.getAddedSubList()) {
                    PnPCharacterPane pane = new PnPCharacterPane(character);
                    pane.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                        if (event.isControlDown()) {
                            selectSource(pane);
                        } else {
                            selectTarget(pane);
                        }
                    });
                    panes.put(character, pane);

                    players.getChildren().add(pane);
                }

                for (PnPCharacter member : change.getRemoved()) {
                    players.getChildren().remove(panes.get(member));
                    panes.remove(member);
                }
            }
        });

        Label enemyLabel = new Label();
        enemyLabel.textProperty().bind(LanguageUtility.getMessageProperty("enemies"));
        enemyLabel.setFont(new Font(20));
        memberLists.getChildren().add(enemyLabel);

        this.enemies = new FlowPane();
        memberLists.getChildren().add(enemies);

        battle.enemiesProperty().addListener((ListChangeListener<PnPCharacter>) change -> {
            while (change.next()) {

                for (PnPCharacter member : change.getAddedSubList()) {
                    PnPCharacterPane pane = new PnPCharacterPane(member);
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

                for (PnPCharacter member : change.getRemoved()) {
                    enemies.getChildren().remove(panes.get(member));
                    panes.remove(member);
                }
            }
        });

        HBox rightBox = new HBox(10);
        memberPart.setRight(rightBox);

        Separator separator = new Separator();
        separator.setOrientation(Orientation.VERTICAL);
        rightBox.getChildren().add(separator);

        VBox info = new VBox(5);
        info.setPadding(new Insets(10));
        info.setAlignment(Pos.TOP_CENTER);
        info.setPrefSize(280, 520);
        rightBox.getChildren().add(info);

        Label infoLabel = new Label();
        infoLabel.textProperty().bind(LanguageUtility.getMessageProperty("battle.info"));
        infoLabel.setFont(Font.font("", FontWeight.BOLD, 20));
        info.getChildren().add(infoLabel);

        Label emptyLabel = new Label();
        emptyLabel.textProperty().bind(LanguageUtility.getMessageProperty("battle.info.nothingSelected"));
        emptyLabel.setAlignment(Pos.CENTER);
        info.getChildren().add(emptyLabel);

        HBox damageBox = labelTextField("battle.info.damage", damage);
        HBox penBox = labelTextField("battle.info.penetration", penetration);
        HBox healBox = labelTextField("battle.info.healing", heal);

        ComboBox<AttackTypes> attackCombo = new ComboBox<>();
        attackCombo.setItems(FXCollections.observableArrayList(AttackTypes.values()));
        attackCombo.setCellFactory(list -> new UpdatingListCell<>());
        attackCombo.setButtonCell(new UpdatingListCell<>());
        attackCombo.getSelectionModel().select(AttackTypes.upperBody);
        HBox attackBox = labelRegion("battle.info.target", attackCombo);

        TextField blockField = new TextField("1");

        ComboBox<ShieldEnum> blockCombo = new ComboBox<>();
        blockCombo.setCellFactory(list -> new UpdatingListCell<>());
        blockCombo.setButtonCell(new UpdatingListCell<>());
        blockCombo.setItems(FXCollections.observableArrayList(ShieldEnum.values()));
        blockCombo.getSelectionModel().select(ShieldEnum.without);

        HBox blockBox = labelRegion("battle.info.block", 40, blockField, 110, blockCombo);

        ChangeListener<PnPCharacterPane> changeListener = (ob, o, memberPane) -> {
            info.getChildren().clear();
            info.getChildren().add(infoLabel);

            final PnPCharacter target = selectedTarget.get() != null ? selectedTarget.get().getCharacter() : null;
            final PnPCharacter source = selectedSource.get() != null ? selectedSource.get().getCharacter() : null;

            if (target == null) {
                info.getChildren().add(emptyLabel);
                return;
            }

            TextField nameField = new TextField();
            nameField.textProperty().bindBidirectional(target.nameProperty());

            TextField lvlField = new TextField();
            lvlField.textProperty().bindBidirectional(target.levelProperty(), new NumStringConverter());

            info.getChildren().add(labelRegion("battle.info.name", 120, nameField, 30, lvlField));
            info.getChildren().add(labelTextField("battle.info.hitPoints", target.healthProperty(), target.maxHealthProperty()));
            info.getChildren().add(labelTextField("battle.info.mana", target.manaProperty(), target.maxManaProperty()));
            info.getChildren().add(labelTextField("battle.info.initiative", target.staticInitiativeProperty()));
            info.getChildren().add(labelTextField("battle.info.counter", target.counterProperty()));
            info.getChildren().add(labelTextField("battle.info.start", target.startValueProperty()));

            Button armorButton = new Button();
            armorButton.setPrefWidth(215);
            armorButton.textProperty().bind(LanguageUtility.getMessageProperty("battle.info.characterSheet"));
            armorButton.setOnAction(ev -> new CharacterView(target));
            info.getChildren().add(armorButton);

            Button statusButton = new Button();
            statusButton.textProperty().bind(LanguageUtility.getMessageProperty("battle.info.status"));
            statusButton.setPrefWidth(215);
            statusButton.setOnAction(ev -> new MemberStateView(target, source));
            info.getChildren().add(statusButton);

            if (source != null) {
                if (battle.isSameTeam(source, target)) {

                    Label battleInfo = new Label();
                    battleInfo.setPadding(new Insets(30, 0, 5, 0));
                    battleInfo.textProperty().bind(
                            source.nameProperty()
                                    .concat(" ")
                                    .concat(LanguageUtility.getMessageProperty("battle.info.heals"))
                                    .concat(" ").concat(target.nameProperty()));
                    battleInfo.setFont(Font.font("", FontWeight.BOLD, 12));
                    info.getChildren().add(battleInfo);

                    info.getChildren().add(healBox);

                    Button healButton = new Button();
                    healButton.textProperty().bind(LanguageUtility.getMessageProperty("battle.info.heal"));
                    healButton.setPrefWidth(215);
                    healButton.setOnAction(ev -> target.heal(heal.get(), source));
                    info.getChildren().add(healButton);

                } else {
                    if (source != target) {

                        Label battleInfo = new Label();
                        battleInfo.setPadding(new Insets(30, 0, 5, 0));
                        StringExpression infoExpression = source.nameProperty()
                                .concat(" ")
                                .concat(LanguageUtility.getMessageProperty("battle.info.attacks.verb"))
                                .concat(" ")
                                .concat(target.nameProperty());

                        if (!LanguageUtility.getMessage("battle.info.attacks.ending").isBlank()) {
                            infoExpression = infoExpression
                                    .concat(" ")
                                    .concat(LanguageUtility.getMessageProperty("battle.info.attacks.ending"));
                        }
                        battleInfo.textProperty().bind(infoExpression);
                        battleInfo.setFont(Font.font("", FontWeight.BOLD, 12));
                        info.getChildren().add(battleInfo);

                        info.getChildren().add(damageBox);
                        info.getChildren().add(penBox);
                        info.getChildren().add(attackBox);
                        info.getChildren().add(blockBox);

                        Button attackButton = new Button();
                        attackButton.textProperty().bind(LanguageUtility.getMessageProperty("battle.info.attack"));
                        attackButton.setPrefWidth(215);
                        attackButton.setOnAction(ev -> {
                            target.takeDamage(
                                    damage.get(), attackCombo.getSelectionModel().getSelectedItem(),
                                    blockCombo.getSelectionModel().getSelectedItem().toBool(),
                                    (double) penetration.get() / 100, Double.parseDouble(blockField.getText()), source);
                            source.applyWearOnWeapons();
                        });
                        info.getChildren().add(attackButton);
                    }
                }
            } else {
                Label selectionHint = new Label();
                selectionHint.textProperty().bind(LanguageUtility.getMessageProperty("battle.info.selectionHint"));
                selectionHint.setPadding(new Insets(30, 0, 5, 0));
                selectionHint.setWrapText(true);
                info.getChildren().add(selectionHint);
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

        Button playerButton = new Button();
        playerButton.textProperty().bind(LanguageUtility.getMessageProperty("battle.button.newPlayer"));
        playerButton.setPrefWidth(110);
        playerButton.setOnAction(ev -> battle.createPlayer());
        createButtons.add(playerButton, 0, 0);

        Button loadPlayerButton = new Button();
        loadPlayerButton.textProperty().bind(LanguageUtility.getMessageProperty("battle.button.loadPlayer"));
        loadPlayerButton.setPrefWidth(110);
        loadPlayerButton.setOnAction(ev -> load(false));
        createButtons.add(loadPlayerButton, 1, 0);

        Button spawnPlayerButton = new Button();
        spawnPlayerButton.textProperty().bind(LanguageUtility.getMessageProperty("battle.button.spawnPlayer"));
        spawnPlayerButton.setPrefWidth(110);
        spawnPlayerButton.setOnAction(ev -> spawn(false));
        createButtons.add(spawnPlayerButton, 2, 0);

        Button cloneButton = new Button();
        cloneButton.textProperty().bind(LanguageUtility.getMessageProperty("battle.button.clone"));
        cloneButton.setPrefWidth(110);
        cloneButton.setOnAction(ev -> cloneMember());
        createButtons.add(cloneButton, 3, 0);

        cloneButton.setDisable(true);
        selectedTarget.addListener((ob, o, n) -> cloneButton.setDisable(n == null));

        Button enemyButton = new Button();
        enemyButton.textProperty().bind(LanguageUtility.getMessageProperty("battle.button.newEnemy"));
        enemyButton.setPrefWidth(110);
        enemyButton.setOnAction(ev -> battle.createEnemy());
        createButtons.add(enemyButton, 0, 1);

        Button loadEnemyButton = new Button();
        loadEnemyButton.textProperty().bind(LanguageUtility.getMessageProperty("battle.button.loadEnemy"));
        loadEnemyButton.setPrefWidth(110);
        loadEnemyButton.setOnAction(ev -> load(true));
        createButtons.add(loadEnemyButton, 1, 1);

        Button spawnEnemyButton = new Button();
        spawnEnemyButton.textProperty().bind(LanguageUtility.getMessageProperty("battle.button.spawnEnemy"));
        spawnEnemyButton.setPrefWidth(110);
        spawnEnemyButton.setOnAction(ev -> spawn(true));
        createButtons.add(spawnEnemyButton, 2, 1);

        Button removeButton = new Button();
        removeButton.textProperty().bind(LanguageUtility.getMessageProperty("battle.button.remove"));
        removeButton.setPrefWidth(110);
        removeButton.setOnAction(ev -> removeMember());
        createButtons.add(removeButton, 3, 1);

        removeButton.setDisable(true);
        selectedTarget.addListener((ob, o, n) -> removeButton.setDisable(n == null));

        //Middle
        Button nextTurnButton = new Button();
        nextTurnButton.textProperty().bind(LanguageUtility.getMessageProperty("battle.button.nextTurn"));
        nextTurnButton.setPrefWidth(110);
        nextTurnButton.setOnAction(ev -> battle.nextTurn());
        buttonPane.setCenter(nextTurnButton);

        //Right
        GridPane utilityButtons = new GridPane();
        utilityButtons.setHgap(5);
        utilityButtons.setVgap(5);
        buttonPane.setRight(utilityButtons);

        Button lootButton = new Button();
        lootButton.textProperty().bind(LanguageUtility.getMessageProperty("battle.button.loot"));
        lootButton.setPrefWidth(110);
        lootButton.setOnAction(event -> loot());
        utilityButtons.add(lootButton, 0, 0);

        Button statisticButton = new Button();
        statisticButton.textProperty().bind(LanguageUtility.getMessageProperty("battle.button.statistics"));
        statisticButton.setPrefWidth(110);
        statisticButton.setOnAction(event -> new StatisticView(battle.playersProperty(), battle));
        utilityButtons.add(statisticButton, 1, 0);

        Button resetButton = new Button();
        resetButton.textProperty().bind(LanguageUtility.getMessageProperty("battle.button.reset"));
        resetButton.setPrefWidth(110);
        resetButton.setOnAction(event -> reset());
        utilityButtons.add(resetButton, 0, 1);

        Button allStatusButton = new Button();
        allStatusButton.textProperty().bind(LanguageUtility.getMessageProperty("battle.button.allStatus"));
        allStatusButton.setPrefWidth(110);
        allStatusButton.setOnAction(event -> {
            PnPCharacter source = selectedSource.get() != null ? selectedSource.get().getCharacter() : null;
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

        for (PnPCharacter character : battle.enemiesProperty()) {
            lootTable.add(character.getFinishedLootTable());
        }

        Collection<LootView.EXP> expCollection = new LinkedList<>();

        for (PnPCharacter player : battle.playersProperty()) {
            double amount = 0;

            for (PnPCharacter enemy : battle.enemiesProperty()) {
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
            battle.removeMember(selectedTarget.get().getCharacter());
            players.getChildren().remove(selectedTarget.get());
            enemies.getChildren().remove(selectedTarget.get());
            selectTarget(null);
        }
    }

    private void cloneMember() {
        if (selectedTarget.get() != null) {
            PnPCharacter member = selectedTarget.get().getCharacter();
            if (battle.isPlayer(member)) {
                battle.createPlayer(member);
            } else {
                battle.createEnemy(member);
            }
        }
    }

    private void selectTarget(PnPCharacterPane pane) {
        if (selectedTarget.get() != null) {
            selectedTarget.get().setPrimarySelected(false);
        }
        selectedTarget.set(pane);
        if (pane != null) {
            pane.setPrimarySelected(true);
        }
    }

    private void selectSource(PnPCharacterPane pane) {
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
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(LanguageUtility.getMessage("excelFile"), "*.xlsx"),
                new FileChooser.ExtensionFilter(LanguageUtility.getMessage("allFiles"), "*.*"));
        battle.load(chooser.showOpenDialog(getStage()), enemy);
    }

    private void spawn(boolean enemy) {
        new SpawnView(battle, enemy);
    }

    private enum ShieldEnum implements WithToStringProperty {
        with, without;

        @Override
        public String toString() {
            return toStringProperty().get();
        }

        public boolean toBool() {
            return this == ShieldEnum.with;
        }

        @Override
        public ReadOnlyStringProperty toStringProperty() {
            return LanguageUtility.getMessageProperty("battle.shieldEnum." + super.toString());
        }
    }

    public Battle getBattle() {
        return battle;
    }
}
