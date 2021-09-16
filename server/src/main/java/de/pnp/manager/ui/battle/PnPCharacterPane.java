package de.pnp.manager.ui.battle;

import de.pnp.manager.main.LanguageUtility;
import de.pnp.manager.model.character.PnPCharacter;
import de.pnp.manager.model.character.state.IMemberState;
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

public class PnPCharacterPane extends StackPane {

    private static final Image ICONS = new Image("Icons.png");

    private final PnPCharacter character;
    private boolean primarySelected, secondarySelected;

    public PnPCharacterPane(PnPCharacter character) {
        this.character = character;
        this.primarySelected = false;
        this.secondarySelected = false;

        this.setPadding(new Insets(5));

        VBox root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);

        Label name = new Label();
        name.textProperty().bind(this.character.nameProperty()
                .concat(" ").concat(LanguageUtility.getMessageProperty("character.level.short"))
                .concat(": ").concat(this.character.levelProperty()));
        name.setMaxWidth(100);
        name.setWrapText(true);
        name.setTextAlignment(TextAlignment.CENTER);
        name.setAlignment(Pos.CENTER);
        root.getChildren().add(name);

        ProgressBar lifeBar = new ProgressBar();
        lifeBar.setStyle("-fx-accent: red;");
        updateLife(lifeBar);
        this.character.healthProperty().addListener((ob, o, n) -> updateLife(lifeBar));
        this.character.maxHealthProperty().addListener((ob, o, n) -> updateLife(lifeBar));
        root.getChildren().add(lifeBar);

        ProgressBar manaBar = new ProgressBar();
        manaBar.setStyle("-fx-accent: blue;");
        updateLife(manaBar);
        this.character.manaProperty().addListener((ob, o, n) -> updateMana(manaBar));
        this.character.maxManaProperty().addListener((ob, o, n) -> updateMana(manaBar));
        root.getChildren().add(manaBar);

        Canvas iconBar = new Canvas();
        iconBar.widthProperty().bind(this.widthProperty().subtract(12));
        iconBar.setHeight(13);
        root.getChildren().add(iconBar);

        this.character.memberStatesProperty().addListener((ListChangeListener<? super IMemberState>) change -> {
            iconBar.getGraphicsContext2D().clearRect(0, 0, iconBar.getWidth(), 13);
            for (int i = 0; i < change.getList().size(); i++) {
                IMemberState state = change.getList().get(i);
                iconBar.getGraphicsContext2D().drawImage(ICONS,
                        state.getImageID() * 13, 0, 13, 13, i * 17, 0, 13, 13);
            }
        });
        iconBar.getGraphicsContext2D().clearRect(0, 0, iconBar.getWidth(), 13);
        for (int i = 0; i < this.character.memberStatesProperty().size(); i++) {
            IMemberState state = this.character.memberStatesProperty().get(i);
            iconBar.getGraphicsContext2D().drawImage(ICONS,
                    state.getImageID() * 13, 0, 13, 13, i * 17, 0, 13, 13);
        }

        this.getChildren().add(root);

        Canvas canvas = new Canvas();
        canvas.widthProperty().bind(this.widthProperty().multiply(0.9));
        canvas.heightProperty().bind(this.heightProperty().multiply(0.9));
        this.getChildren().add(canvas);

        this.character.healthProperty().addListener((ob, o, n) -> {

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

        this.character.turnsProperty().addListener((ob, o, n) -> {
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

        switch (this.character.getTurns()) {
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

    public PnPCharacter getCharacter() {
        return character;
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
        lifeBar.setProgress(Math.max(0, (double) character.getHealth() / (double) character.getMaxHealth()));
    }

    private void updateMana(ProgressBar manaBar) {
        manaBar.setProgress(Math.max(0, (double) character.getMana() / (double) character.getMaxMana()));
    }
}
