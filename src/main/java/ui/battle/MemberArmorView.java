package ui.battle;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.member.BattleMember;
import model.member.data.ArmorPiece;
import ui.View;

import static manager.LanguageUtility.getMessageProperty;
import static ui.ViewFactory.labelTextField;

public class MemberArmorView extends View {

    public MemberArmorView(BattleMember member) {
        super("armor.title");

        VBox root = new VBox(5);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.TOP_CENTER);

        Label infoLabel = new Label();
        infoLabel.textProperty().bind(getMessageProperty("armor.of").concat(" ").concat(member.nameProperty()));
        infoLabel.setFont(Font.font("", FontWeight.BOLD, 20));
        root.getChildren().add(infoLabel);

        root.getChildren().add(labelTextField("armor.defense", member.baseDefenseProperty()));
        root.getChildren().add(labelTextField("armor.head", member.armorProperty(ArmorPiece.head)));
        root.getChildren().add(labelTextField("armor.body", member.armorProperty(ArmorPiece.upperBody)));
        root.getChildren().add(labelTextField("armor.legs", member.armorProperty(ArmorPiece.legs)));
        root.getChildren().add(labelTextField("armor.arms", member.armorProperty(ArmorPiece.arm)));
        root.getChildren().add(labelTextField("armor.shield", member.armorProperty(ArmorPiece.shield)));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        show();
    }
}
