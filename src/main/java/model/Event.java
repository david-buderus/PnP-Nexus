package model;

import java.util.Collection;

public class Event {
    private String name;
    private String typ;
    private String info;
    private String trigger;
    private double chance;
    private Collection<String> continents;
    private Collection<String> lands;
    private Collection<String> locations;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public double getChance() {
        return chance;
    }

    public void setChance(double chance) {
        this.chance = chance;
    }

    public Collection<String> getContinents() {
        return continents;
    }

    public void setContinents(Collection<String> continents) {
        this.continents = continents;
    }

    public Collection<String> getLands() {
        return lands;
    }

    public void setLands(Collection<String> lands) {
        this.lands = lands;
    }

    public Collection<String> getLocations() {
        return locations;
    }

    public void setLocations(Collection<String> locations) {
        this.locations = locations;
    }

    public String continentsAsString() {
        return String.join(", ", this.continents);
    }

    public String landsAsString() {
        return String.join(", ", this.lands);
    }

    public String locationsAsString() {
        return String.join(", ", this.locations);
    }

}
