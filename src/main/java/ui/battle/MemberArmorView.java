package ui.battle;

import javafx.beans.property.ReadOnlyStringWrapper;
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

import static ui.ViewFactory.labelTextField;

public class MemberArmorView extends View {

    public MemberArmorView(BattleMember member) {
        super("armor.title");

        VBox root = new VBox(5);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.TOP_CENTER);

        Label infoLabel = new Label();
        infoLabel.textProperty().bind(new ReadOnlyStringWrapper("Rüstung von ").concat(member.nameProperty()));
        infoLabel.setFont(Font.font("", FontWeight.BOLD, 20));
        root.getChildren().add(infoLabel);

        root.getChildren().add(labelTextField("Abwehr", member.baseDefenseProperty()));
        root.getChildren().add(labelTextField("Kopf", member.armorProperty(ArmorPiece.head)));
        root.getChildren().add(labelTextField("Oberkörper", member.armorProperty(ArmorPiece.upperBody)));
        root.getChildren().add(labelTextField("Beine", member.armorProperty(ArmorPiece.legs)));
        root.getChildren().add(labelTextField("Arme", member.armorProperty(ArmorPiece.arm)));
        root.getChildren().add(labelTextField("Schild", member.armorProperty(ArmorPiece.shield)));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        show();
    }
}
