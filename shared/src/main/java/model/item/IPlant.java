package model.item;

import java.util.Collection;

public interface IPlant extends IItem {

    Collection<String> getLocations();

    void setLocations(Collection<String> locations);

    default String locationsAsString() {
        return String.join(", ", this.getLocations());
    }

    @Override
    IPlant copy();
}
