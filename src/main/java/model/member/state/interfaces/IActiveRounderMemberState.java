package model.member.state.interfaces;

/**
* Interface for MemberStates, that either decrease duration in active or inactive rounds (never both)
*/
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
