package de.pnp.manager.component.universe;

import java.util.Objects;
import org.bson.types.ObjectId;

public final class UniverseId {

    private final ObjectId id;

    public UniverseId() {
        id = new ObjectId();
    }

    public UniverseId(String hexString) {
        id = new ObjectId(hexString);
    }

    /**
     * @see ObjectId#toHexString()
     */
    public String toHexString() {
        return id.toHexString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UniverseId that = (UniverseId) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
