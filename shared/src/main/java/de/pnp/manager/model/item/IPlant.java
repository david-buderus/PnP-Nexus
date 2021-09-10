package de.pnp.manager.model.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.pnp.manager.network.serializer.DeserializerIdentifier;

import java.util.Collection;

public interface IPlant extends IItem {

    @DeserializerIdentifier
    Collection<String> getLocations();

    void setLocations(Collection<String> locations);

    @JsonIgnore
    default String locationsAsString() {
        return String.join(", ", this.getLocations());
    }

    @Override
    IPlant copy();
}