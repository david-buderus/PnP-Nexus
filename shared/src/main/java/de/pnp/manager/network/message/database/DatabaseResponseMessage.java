package de.pnp.manager.network.message.database;

import de.pnp.manager.model.IFabrication;
import de.pnp.manager.model.item.IItem;
import de.pnp.manager.model.upgrade.UpgradeModel;
import de.pnp.manager.network.message.DataMessage;

import java.util.Collection;
import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.DATABASE_RESPONSE;

public class DatabaseResponseMessage extends DataMessage<DatabaseResponseMessage.DatabaseData> {

    public DatabaseResponseMessage() {
    }

    public DatabaseResponseMessage(
            Collection<IItem> items,
            Collection<IFabrication> fabrications,
            Collection<UpgradeModel> upgrades,
            Date timestamp) {
        super(DATABASE_RESPONSE, timestamp);
        DatabaseData data = new DatabaseData();
        data.setItems(items);
        data.setFabrications(fabrications);
        data.setUpgrades(upgrades);
        this.setData(data);
    }

    public static class DatabaseData {
        protected Collection<IItem> items;
        protected Collection<IFabrication> fabrications;
        protected Collection<UpgradeModel> upgrades;

        public Collection<IItem> getItems() {
            return items;
        }

        public void setItems(Collection<IItem> items) {
            this.items = items;
        }

        public Collection<IFabrication> getFabrications() {
            return fabrications;
        }

        public void setFabrications(Collection<IFabrication> fabrications) {
            this.fabrications = fabrications;
        }

        public Collection<UpgradeModel> getUpgrades() {
            return upgrades;
        }

        public void setUpgrades(Collection<UpgradeModel> upgrades) {
            this.upgrades = upgrades;
        }
    }
}
