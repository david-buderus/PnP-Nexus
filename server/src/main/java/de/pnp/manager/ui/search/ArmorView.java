package de.pnp.manager.ui.search;

import de.pnp.manager.main.Database;
import de.pnp.manager.model.item.Armor;
import de.pnp.manager.model.item.Equipment;
import de.pnp.manager.model.item.Item;
import de.pnp.manager.ui.IView;

public class ArmorView extends EquipmentView<Armor> {

    public ArmorView(IView parent) {
        super("search.armor.title", parent, Database.armorList);

        tableView.addObservableColumn("column.amount", Item::amountProperty);
        tableView.addColumn("column.name", Item::getName);
        tableView.addColumn("column.type", Item::getSubtype);
        tableView.addColumn("column.armor.protection", Armor::getProtection);
        tableView.addColumn("column.armor.weight", Armor::getWeight);
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