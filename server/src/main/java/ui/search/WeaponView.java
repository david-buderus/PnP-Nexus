package ui.search;

import manager.Database;
import model.item.Equipment;
import model.item.Item;
import model.item.Weapon;
import ui.IView;

public class WeaponView extends EquipmentView<Weapon> {

    public WeaponView(IView parent) {
        super("search.weapon.title", parent, Database.weaponList);

        // TODO add initivateModifier from weapon
        tableView.addObservableColumn("column.amount", Item::amountProperty);
        tableView.addColumn("column.name", Item::getName);
        tableView.addColumn("column.type", Item::getSubtype);
        tableView.addColumn("column.weapon.initiative", Weapon::getInitiative);
        tableView.addColumn("column.dice_weight", Weapon::getDice);
        tableView.addColumn("column.damage_protection", Weapon::getDamage);
        tableView.addColumn("column.weapon.hit", Weapon::getHit);
        tableView.addColumn("column.tier", Item::getTier);
        tableView.addColumn("column.item.rarity", Item::getRarity);
        tableView.addColumn("column.item.price", Item::getCurrency);
        tableView.addColumn("column.effect", Item::getEffect);
        tableView.addColumn("column.equipment.slots", Equipment::getUpgradeSlots);
        tableView.addColumn("column.equipment.requirement", Equipment::getRequirement);
        tableView.addColumn("column.equipment.upgrades", Equipment::upgradesAsString);

        this.addControls();
        update();
    }
}
