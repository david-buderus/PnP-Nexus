package de.pnp.manager.model.interfaces;

import de.pnp.manager.main.LanguageUtility;
import javafx.beans.property.ReadOnlyStringProperty;

public interface WithUnlocalizedName extends WithToStringProperty {

    String getUnlocalizedName();

    @Override
    default ReadOnlyStringProperty toStringProperty() {
        return LanguageUtility.getMessageProperty(getUnlocalizedName());
    }
}
