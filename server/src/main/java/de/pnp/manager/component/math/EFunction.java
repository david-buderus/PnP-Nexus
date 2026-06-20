package de.pnp.manager.component.math;

/**
 * All possible functions which can appear in a {@link BinaryExpressionTree}
 */
public enum EFunction {
    ABS("abs", 1), CEIL("ceil", 1), FLOOR("floor", 1),
    ROUND("round", 1), MAX("max", 2), MIN("min", 2);

    private final String humanReadableFormat;
    private final int parameters;

    EFunction(String humanReadableFormat, int parameters) {
        this.humanReadableFormat = humanReadableFormat;
        this.parameters = parameters;
    }

    /**
     * Returns the matching {@link EFunction} for the given string.
     * <p>
     * Returns {@code null} if no {@link EFunction} matches.
     */
    public static EFunction from(String s) {
        return switch (s.toLowerCase()) {
            case "abs" -> ABS;
            case "ceil" -> CEIL;
            case "floor" -> FLOOR;
            case "round" -> ROUND;
            case "max" -> MAX;
            case "min" -> MIN;
            default -> null;
        };
    }

    public String getHumanReadableFormat() {
        return humanReadableFormat;
    }

    public int getParameters() {
        return parameters;
    }
}
