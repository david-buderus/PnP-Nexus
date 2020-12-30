package ui.battle.state;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import model.member.MemberState;

public class MemberStatePane extends VBox {

    private static final Image ICONS = new Image("Icons.png");

    private final MemberState state;
    private boolean selected;

    public MemberStatePane(MemberState state) {
        this.selected = false;
        this.state = state;

        this.setPadding(new Insets(5));
        this.setAlignment(Pos.CENTER);

        HBox nameBox = new HBox(2);
        nameBox.setAlignment(Pos.CENTER);
        this.getChildren().add(nameBox);

        Canvas iconCanvas = new Canvas();
        iconCanvas.setHeight(13);
        iconCanvas.setWidth(13);
        nameBox.getChildren().add(iconCanvas);
        iconCanvas.getGraphicsContext2D().drawImage(ICONS,
                state.getEffect().getImageID() * 13, 0, 13, 13, 0, 0, 13, 13);

        Label name = new Label(state.getName());
        nameBox.getChildren().add(name);

        ProgressBar durationBar = new ProgressBar();
        durationBar.setStyle("-fx-accent: #ff0000;");
        state.durationProperty().addListener((ob, o, n) ->
                durationBar.setProgress((double) state.getDuration() / state.getMaxDuration()));
        durationBar.setProgress((double) state.getDuration() / state.getMaxDuration());
        this.getChildren().add(durationBar);

        Label strength = new Label("Stärke: " + state.getPowerAsString());
        strength.setPadding(new Insets(5, 0, 5, 0));
        this.getChildren().add(strength);

        Label duration = new Label();
        duration.textProperty().bind(state.durationDisplayProperty());
        this.getChildren().add(duration);
    }

    public void setSelected(boolean bool) {
        selected = bool;

        if (selected) {
            this.setPadding(new Insets(4));
            this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        } else {
            this.setPadding(new Insets(5));
            this.setBorder(Border.EMPTY);
        }
    }

    public MemberState getState() {
        return state;
    }
}
