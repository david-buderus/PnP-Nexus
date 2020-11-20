package model.item;

import java.util.Collection;

public class Plant extends Item {

    private Collection<String> locations;

    public Collection<String> getLocations() {
        return locations;
    }

    public void setLocations(Collection<String> locations) {
        this.locations = locations;
    }

    public String locationsAsString() {
        return String.join(", ", this.locations);
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
