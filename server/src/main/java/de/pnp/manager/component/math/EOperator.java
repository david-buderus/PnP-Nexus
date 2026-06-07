package de.pnp.manager.component.math;

/**
 * All possible operators which can appear in a {@link BinaryExpressionTree}
 */
enum EOperator {
    ADD(1, "+", true),
    SUB(1, "-", true),
    MULTIPLY(2, "*", true),
    DIVIDE(2, "/", true),
    NEGATE(3, "-", true),
    OPENING_BRACKET(0, "(", true),
    CLOSING_BRACKET(0, ")", false);

    private final int priority;

    private final String humanReadableFormat;

    private final boolean needsToBeFollowedByConstant;

    EOperator(int priority, String humanReadableFormat, boolean needsToBeFollowedByConstant) {
        this.priority = priority;
        this.humanReadableFormat = humanReadableFormat;
        this.needsToBeFollowedByConstant = needsToBeFollowedByConstant;
    }

    /**
     * Returns the matching {@link EOperator} for the given character.
     * <p>
     * Returns {@code null} if no {@link EOperator} matches.
     */
    public static EOperator from(char c) {
        return switch (c) {
            case '+' -> ADD;
            case '-' -> SUB;
            case '*' -> MULTIPLY;
            case '/' -> DIVIDE;
            case '(' -> OPENING_BRACKET;
            case ')' -> CLOSING_BRACKET;
            default -> null;
        };
    }

    public int getPriority() {
        return priority;
    }

    public String getHumanReadableFormat() {
        return humanReadableFormat;
    }

    /**
     * Returns if this operator needs to be followed by some type of constant.
     */
    public boolean needsToBeFollowedByConstant() {
        return needsToBeFollowedByConstant;
    }
}
