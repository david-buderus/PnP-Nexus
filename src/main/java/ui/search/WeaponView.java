package ui.search;

import javafx.scene.layout.VBox;
import manager.Database;
import model.item.Weapon;
import ui.IView;

public class WeaponView extends EquipmentView<Weapon> {

    public WeaponView(IView parent) {
        super("search.weapon.title", parent, Weapon.class, Database.weaponList);

        VBox root = this.createRoot(
                new String[]{"column.name", "column.type", "column.weapon.initiative", "column.weapon.dice",
                        "column.weapon.damage", "column.weapon.hit", "column.item.rarity", "column.item.price",
                        "column.effect", "column.equipment.slots", "column.equipment.requirement"},
                new String[]{"name", "subtype", "initiative", "dice", "damage", "hit", "rarity", "currency", "effect", "slots", "requirement"});

        this.addControls(root);

        update();
        this.setContent(root);
    }
}
