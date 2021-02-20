package model.member.state.interfaces;

public interface IActiveRounderMemberState extends IMemberState {

    boolean isActiveRounder();

    @Override
    default void decreaseDuration(boolean isActiveRoundDecrease, int amount) {
        if (isActiveRounder()) {
            if (isActiveRoundDecrease) {
                this.setDuration(this.getDuration() - amount);
            }
        } else {
            if (!isActiveRoundDecrease) {
                this.setDuration(this.getDuration() - amount);
            }
        }
    }
}
