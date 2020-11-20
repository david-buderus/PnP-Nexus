package model.item;

public class Jewellery extends Equipment {

    private String gem = "";

    public String getGem() {
        return gem;
    }

    public void setGem(String gem) {
        this.gem = gem;
    }

    @Override
    public Jewellery copy() {
        Jewellery jewellery = (Jewellery) super.copy();
        jewellery.setGem(this.getGem());

        return jewellery;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof Jewellery) || !super.equals(o)) {
            return false;
        }

        Jewellery other = (Jewellery) o;

        return this.getGem().equals(other.getGem());
    }
}
