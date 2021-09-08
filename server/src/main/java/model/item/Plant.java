package model.item;

import java.util.Collection;
import java.util.Collections;

public class Plant extends Item implements IPlant {

    private Collection<String> locations;

    public Plant() {
        super();
        this.locations = Collections.emptyList();
    }

    public Collection<String> getLocations() {
        return locations;
    }

    public void setLocations(Collection<String> locations) {
        this.locations = locations;
    }

    @Override
    public Plant copy() {
        Plant plant = (Plant) super.copy();
        plant.setLocations(this.getLocations());
        return plant;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof Plant) || !super.equals(o)) {
            return false;
        }

        Plant other = (Plant) o;

        return this.getLocations().containsAll(other.getLocations())
                && other.getLocations().containsAll(this.getLocations());

    }
}
