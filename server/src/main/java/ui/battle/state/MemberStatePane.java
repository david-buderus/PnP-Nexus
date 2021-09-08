package ui.battle.state;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import manager.LanguageUtility;
import model.member.state.interfaces.IMemberState;
import model.member.state.interfaces.IPowerMemberState;

public class MemberStatePane extends VBox {

    private static final Image ICONS = new Image("Icons.png");

    private final IMemberState state;
    private boolean selected;

    public MemberStatePane(IMemberState state) {
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
                state.getImageID() * 13, 0, 13, 13, 0, 0, 13, 13);

        Label name = new Label(state.getName());
        nameBox.getChildren().add(name);

        ProgressBar durationBar = new ProgressBar();
        durationBar.setStyle("-fx-accent: #ff0000;");
        state.durationProperty().addListener((ob, o, n) ->
                durationBar.setProgress((double) state.getDuration() / state.getMaxDuration()));
        durationBar.setProgress((double) state.getDuration() / state.getMaxDuration());
        this.getChildren().add(durationBar);

        if (state instanceof IPowerMemberState) {
            Label power = new Label();
            power.textProperty().bind(LanguageUtility.getMessageProperty("state.info.power").concat(": ").concat(((IPowerMemberState) state).getPowerAsString()));
            power.setPadding(new Insets(5, 0, 5, 0));
            this.getChildren().add(power);

        }

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

    public IMemberState getState() {
        return state;
    }
}
