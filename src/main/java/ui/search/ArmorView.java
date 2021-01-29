package ui.search;

import javafx.scene.layout.VBox;
import manager.Database;
import model.item.Armor;
import ui.IView;

public class ArmorView extends EquipmentView<Armor> {

    public ArmorView(IView parent) {
        super("search.armor.title", parent, Armor.class, Database.armorList);

        VBox root = this.createRoot(
                new String[]{"column.name", "column.type", "column.armor.protection", "column.armor.weight",
                        "column.item.rarity", "column.item.price", "column.effect", "column.equipment.slots",
                        "column.equipment.requirement"},
                new String[]{"name", "subtype", "protection", "weight", "rarity", "currency", "effect", "upgradeSlots", "requirement"});

        this.addControls(root);

        update();
        this.setContent(root);
    }
}