package de.pnp.manager.model;

import java.util.Collection;

public class Inconsistency {

    private String name;
    private String inconsistency;
    private Collection<String> info;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInconsistency() {
        return inconsistency;
    }

    public void setInconsistency(String inconsistency) {
        this.inconsistency = inconsistency;
    }

    public Collection<String> getInfo() {
        return info;
    }

    public void setInfo(Collection<String> info) {
        this.info = info;
    }

    public String getInfoAsString() {
        return String.join(", ", info);
    }
}
