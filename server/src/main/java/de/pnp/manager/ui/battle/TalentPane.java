package de.pnp.manager.ui.battle;

import javafx.beans.binding.StringExpression;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import de.pnp.manager.model.member.GeneratedExtendedBattleMember;
import de.pnp.manager.model.member.data.PrimaryAttribute;
import de.pnp.manager.model.other.Talent;
import de.pnp.manager.ui.part.NumberField;

import java.util.Arrays;

public class TalentPane extends HBox {

    public TalentPane(GeneratedExtendedBattleMember battleMember, Talent talent) {
        this.setPadding(new Insets(5));
        this.setAlignment(Pos.CENTER);

        Label name = new Label(talent.toString());
        name.setPrefWidth(150);
        this.getChildren().add(name);

        Label attributes = new Label();
        attributes.textProperty().bind(
                Arrays.stream(talent.getAttributes())
                        .map(attribute -> (PrimaryAttribute) attribute)
                        .map(PrimaryAttribute::toShortStringProperty)
                        .map(r -> (StringExpression) r)
                        .reduce((a, b) -> a.concat("/").concat(b))
                        .orElse(new ReadOnlyStringWrapper("--/--/--")));
        attributes.setPrefWidth(80);
        this.getChildren().add(attributes);

        NumberField points = new NumberField();
        points.setPrefWidth(30);

        points.numberProperty().bindBidirectional(battleMember.getTalent(talent));
        this.getChildren().add(points);
    }
}
