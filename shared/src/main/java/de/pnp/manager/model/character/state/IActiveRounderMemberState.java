package de.pnp.manager.model.character.state;

public interface IActiveRounderMemberState extends IMemberState {

    /**
     * Method that returns, whether this MemberState decreses during active (true) or inactive (false) rounds
     */
    boolean isActiveRounder();

    @Override
    default void decreaseDuration(boolean isActiveRound, int amount) {
        if (isActiveRounder()) {
            if (isActiveRound) {
                this.setDuration(this.getDuration() - amount);
            }
        } else {
            if (!isActiveRound) {
                this.setDuration(this.getDuration() - amount);
            }
        }
    }
}
