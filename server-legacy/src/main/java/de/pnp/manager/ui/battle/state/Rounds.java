package de.pnp.manager.ui.battle.state;

import de.pnp.manager.model.interfaces.WithUnlocalizedName;

public enum Rounds implements WithUnlocalizedName {
    ROUNDS("state.info.rounds"), ACTIVE_ROUNDS("state.info.activeRounds");

    private final String unlocalizedName;

    Rounds(String unlocalizedName) {
        this.unlocalizedName = unlocalizedName;
    }

    @Override
    public String getUnlocalizedName() {
        return unlocalizedName;
    }
}
