package model.member.state.interfaces;

public interface IActiveRounderMemberState extends IMemberState {

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
