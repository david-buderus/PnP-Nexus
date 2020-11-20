package ui.battle;

import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import model.member.BattleMember;
import model.member.MemberState;

public class BattleMemberPane extends StackPane {

    private static final Image ICONS = new Image("Icons.png");

    private final BattleMember battleMember;
    private boolean primarySelected, secondarySelected;

    public BattleMemberPane(BattleMember member) {
        this.battleMember = member;
        this.primarySelected = false;
        this.secondarySelected = false;

        this.setPadding(new Insets(5));

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);

        Label name = new Label();
        name.textProperty().bind(battleMember.nameProperty().concat(" Lvl: ").concat(battleMember.levelProperty()));
        name.setMaxWidth(100);
        name.setWrapText(true);
        name.setTextAlignment(TextAlignment.CENTER);
        name.setAlignment(Pos.CENTER);
        root.getChildren().add(name);

        ProgressBar lifeBar = new ProgressBar();
        lifeBar.setStyle("-fx-accent: red;");
        updateLife(lifeBar);
        battleMember.lifeProperty().addListener((ob, o, n) -> updateLife(lifeBar));
        battleMember.maxLifeProperty().addListener((ob, o, n) -> updateLife(lifeBar));
        root.getChildren().add(lifeBar);

        ProgressBar manaBar = new ProgressBar();
        manaBar.setStyle("-fx-accent: blue;");
        updateLife(manaBar);
        battleMember.manaProperty().addListener((ob, o, n) -> updateMana(manaBar));
        battleMember.maxManaProperty().addListener((ob, o, n) -> updateMana(manaBar));
        root.getChildren().add(manaBar);

        Canvas iconBar = new Canvas();
        iconBar.widthProperty().bind(this.widthProperty().subtract(12));
        iconBar.setHeight(13);
        root.getChildren().add(iconBar);

        battleMember.statesProperty().addListener((ListChangeListener<? super MemberState>) change -> {
            iconBar.getGraphicsContext2D().clearRect(0, 0, iconBar.getWidth(), 13);
            for (int i = 0; i < change.getList().size(); i++) {
                MemberState state = change.getList().get(i);
                iconBar.getGraphicsContext2D().drawImage(ICONS,
                        state.getEffect().getImageID() * 13, 0, 13, 13, i * 17, 0, 13, 13);
            }
        });
        iconBar.getGraphicsContext2D().clearRect(0, 0, iconBar.getWidth(), 13);
        for (int i = 0; i < battleMember.statesProperty().size(); i++) {
            MemberState state = battleMember.statesProperty().get(i);
            iconBar.getGraphicsContext2D().drawImage(ICONS,
                    state.getEffect().getImageID() * 13, 0, 13, 13, i * 17, 0, 13, 13);
        }

        this.getChildren().add(root);

        Canvas canvas = new Canvas();
        canvas.widthProperty().bind(this.widthProperty().multiply(0.9));
        canvas.heightProperty().bind(this.heightProperty().multiply(0.9));
        this.getChildren().add(canvas);

        battleMember.lifeProperty().addListener((ob, o, n) -> {

            GraphicsContext gc = canvas.getGraphicsContext2D();
            if (n.intValue() < 1) {

                gc.setStroke(Color.RED);
                gc.setLineWidth(4);
                gc.strokeLine(0, 0, canvas.getWidth(), canvas.getHeight());
                gc.strokeLine(canvas.getWidth(), 0, 0, canvas.getHeight());
            } else {
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            }
        });

        battleMember.turnsProperty().addListener((ob, o, n) -> {
            switch (n.intValue()) {
                case 0:
                    this.setBackground(Background.EMPTY);
                    break;
                case 1:
                    this.setBackground(new Background(new BackgroundFill(Color.SANDYBROWN, null, null)));
                    break;
                case 2:
                    this.setBackground(new Background(new BackgroundFill(Color.BURLYWOOD, null, null)));
                    break;
            }
        });

        switch (battleMember.getTurns()) {
            case 0:
                this.setBackground(Background.EMPTY);
                break;
            case 1:
                this.setBackground(new Background(new BackgroundFill(Color.SANDYBROWN, null, null)));
                break;
            case 2:
                this.setBackground(new Background(new BackgroundFill(Color.BURLYWOOD, null, null)));
                break;
        }
    }

    public BattleMember getBattleMember() {
        return battleMember;
    }

    public void setPrimarySelected(boolean bool) {
        primarySelected = bool;
        updateSelected();
    }

    public void setSecondarySelected(boolean bool) {
        secondarySelected = bool;
        updateSelected();
    }

    public boolean isPrimarySelected() {
        return primarySelected;
    }

    private void updateSelected() {
        if (primarySelected) {
            this.setPadding(new Insets(4));
            this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        } else if (secondarySelected) {
            this.setPadding(new Insets(4));
            this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.DASHED,
                    CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        } else {
            this.setPadding(new Insets(5));
            this.setBorder(Border.EMPTY);
        }
    }

    private void updateLife(ProgressBar lifeBar) {
        lifeBar.setProgress(Math.max(0, (double) battleMember.getLife() / (double) battleMember.getMaxLife()));
    }

    private void updateMana(ProgressBar manaBar) {
        manaBar.setProgress(Math.max(0, (double) battleMember.getMana() / (double) battleMember.getMaxMana()));
    }
}
