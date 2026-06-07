package de.pnp.manager.component.math;

import java.util.Optional;

/**
 * Reserved variables in expressions
 */
public enum EReservedVariables {
    LEVEL("LVL"), TIER("TIER");

    private final String constant;

    EReservedVariables(String constant) {
        this.constant = constant;
    }

    public String getConstant() {
        return constant;
    }

    /**
     * Returns the {@link EReservedVariables} matching the constant.
     */
    public static Optional<EReservedVariables> of(String constant) {
        for (EReservedVariables e : values()) {
            if (e.getConstant().equals(constant)) {
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }
}
