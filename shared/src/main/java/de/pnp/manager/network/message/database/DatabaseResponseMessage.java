package de.pnp.manager.network.message.database;

import de.pnp.manager.model.IFabrication;
import de.pnp.manager.model.item.IItem;
import de.pnp.manager.model.other.ITalent;
import de.pnp.manager.model.upgrade.UpgradeModel;
import de.pnp.manager.network.message.DataMessage;

import java.util.Collection;
import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.DATABASE_RESPONSE;

public class DatabaseResponseMessage extends DataMessage<DatabaseResponseMessage.DatabaseData> {

    public DatabaseResponseMessage() {
    }

    public DatabaseResponseMessage(
            Collection<? extends IItem> items,
            Collection<? extends ITalent> talents,
            Collection<? extends IFabrication> fabrications,
            Collection<UpgradeModel> upgrades,
            Date timestamp) {
        super(DATABASE_RESPONSE, timestamp);
        DatabaseData data = new DatabaseData();
        data.setItems(items);
        data.setTalents(talents);
        data.setFabrications(fabrications);
        data.setUpgrades(upgrades);
        this.setData(data);
    }

    public static class DatabaseData {
        protected Collection<? extends IItem> items;
        protected Collection<? extends ITalent> talents;
        protected Collection<? extends IFabrication> fabrications;
        protected Collection<UpgradeModel> upgrades;

        public Collection<? extends IItem> getItems() {
            return items;
        }

        public void setItems(Collection<? extends IItem> items) {
            this.items = items;
        }

        public Collection<? extends ITalent> getTalents() {
            return talents;
        }

        public void setTalents(Collection<? extends ITalent> talents) {
            this.talents = talents;
        }

        public Collection<? extends IFabrication> getFabrications() {
            return fabrications;
        }

        public void setFabrications(Collection<? extends IFabrication> fabrications) {
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
