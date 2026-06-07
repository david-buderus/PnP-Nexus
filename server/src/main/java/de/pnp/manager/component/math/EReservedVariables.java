package de.pnp.manager.component.math;

import de.pnp.manager.component.attributes.SecondaryAttribute;

import java.util.EnumSet;
import java.util.Optional;

/**
 * Reserved variables in expressions
 */
public enum EReservedVariables {
    LEVEL("LVL"), TIER("TIER");

    /**
     * All string variables allowed in {@link SecondaryAttribute}.
     */
    public static final EnumSet<EReservedVariables> RESERVED_SECONDARY_ATTRIBUTE_STRING_VARIABLES = EnumSet.of(LEVEL, TIER);

    /**
     * All string variables allowed in simple character expressions.
     */
    public static final EnumSet<EReservedVariables> RESERVED_TIER_STRING_VARIABLES = EnumSet.of(LEVEL);

    /**
     * All string variables allowed in simple character expressions.
     */
    public static final EnumSet<EReservedVariables> RESERVED_TALENT_STRING_VARIABLES = EnumSet.of(LEVEL, TIER);

    private final String constant;

    EReservedVariables(String constant) {
        this.constant = constant;
    }

    public String getConstant() {
        return constant;
    }

    /**
     * As a {@link IExpressionVariable}
     */
    public IExpressionVariable asVariable() {
        return new IExpressionVariable.StringVariable(constant);
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
