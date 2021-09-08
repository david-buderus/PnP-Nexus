package model;

public interface ICurrency {

    ICurrency add(ICurrency other);

    ICurrency add(int value);

    ICurrency sub(ICurrency other);

    ICurrency sub(int value);

    ICurrency multiply(float multiplicative);

    ICurrency divide(float d);

    /**
     * The amount of copper coins this currency object represents
     */
    int getCoinValue();

    /**
     * A human readable String of the value that this object represents
     */
    String getCoinString();

    /**
     * If this currency object represents a tradeable value
     */
    boolean isTradeable();
}
