package ui.battle;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import model.member.ExtendedBattleMember;
import model.member.generation.PrimaryAttribute;
import model.member.generation.Talent;
import ui.part.NumStringConverter;

import java.util.Arrays;
import java.util.stream.Collectors;

public class TalentPane extends HBox {

    public TalentPane(ExtendedBattleMember battleMember, Talent talent) {
        this.setPadding(new Insets(5));
        this.setAlignment(Pos.CENTER);

        Label name = new Label(talent.toString());
        name.setPrefWidth(150);
        this.getChildren().add(name);

        Label attributes = new Label(Arrays.stream(talent.getNeededAttributes(battleMember))
                .map(PrimaryAttribute::toShortString).collect(Collectors.joining(" / ")));
        attributes.setPrefWidth(80);
        this.getChildren().add(attributes);

        TextField points = new TextField();
        points.setPrefWidth(30);
        points.textProperty().bindBidirectional(battleMember.getTalent(talent), new NumStringConverter());
        this.getChildren().add(points);
    }
}
