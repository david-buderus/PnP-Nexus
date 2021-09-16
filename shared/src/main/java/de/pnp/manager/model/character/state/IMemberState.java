package de.pnp.manager.model.character.state;

public interface IMemberState {

    String getName();

    /**
     * The ids are defined in {@link MemberStateIcon}
     */
    int getImageID();

    int getMaxDuration();

    int getDuration();

    void setDuration(int duration);

    String getSourceID();

    default void decreaseDuration(boolean isActiveRound) {
        decreaseDuration(isActiveRound, 1);
    }

    default void decreaseDuration(boolean isActiveRound, int amount) {
        if (!isActiveRound) {
            this.setDuration(this.getDuration() - amount);
        }
    }
}
