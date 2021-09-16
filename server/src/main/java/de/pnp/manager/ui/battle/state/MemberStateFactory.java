package de.pnp.manager.ui.battle.state;

import de.pnp.manager.main.LanguageUtility;
import de.pnp.manager.model.interfaces.WithToStringProperty;
import de.pnp.manager.model.character.PnPCharacter;
import de.pnp.manager.model.character.data.AttackTypes;
import de.pnp.manager.model.character.state.implementations.defense.ArmorBonusMemberState;
import de.pnp.manager.model.character.state.implementations.defense.ArmorMalusMemberState;
import de.pnp.manager.model.character.state.implementations.incoming.ShieldMemberState;
import de.pnp.manager.model.character.state.implementations.initiative.*;
import de.pnp.manager.model.character.state.implementations.manipulating.DamageMemberState;
import de.pnp.manager.model.character.state.implementations.manipulating.HealMemberState;
import de.pnp.manager.model.character.state.implementations.manipulating.ManaDrainMemberState;
import de.pnp.manager.model.character.state.implementations.manipulating.ManaRegenerationMemberState;
import de.pnp.manager.model.character.state.implementations.other.FearMemberState;
import de.pnp.manager.model.character.state.implementations.other.OtherMemberState;
import de.pnp.manager.model.character.state.implementations.other.SnareMemberState;
import de.pnp.manager.model.character.state.interfaces.IMemberStateImpl;
import javafx.beans.property.ReadOnlyStringProperty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MemberStateFactory implements WithToStringProperty {

    private final String key;
    private final MemberStateProducer producer;
    private final IMemberStateImpl defaultState;

    private MemberStateFactory(String key, MemberStateProducer producer) {
        this.key = key;
        this.producer = producer;
        this.defaultState = producer.create("default", 0, false, 0, false, AttackTypes.direct, null);
    }

    public IMemberStateImpl create(String name, int duration, boolean activeRounder, float power, boolean isRandom, AttackTypes type, PnPCharacter source) {
        return producer.create(name, duration, activeRounder, power, isRandom, type, source);
    }

    public IMemberStateImpl getDefaultState() {
        return defaultState;
    }

    @Override
    public ReadOnlyStringProperty toStringProperty() {
        return LanguageUtility.getMessageProperty(key);
    }

    // --- STATIC --- //

    private static final List<MemberStateFactory> INTERN_FACTORIES = new ArrayList<>();
    public static final Collection<MemberStateFactory> FACTORIES;

    static {
        // DEFENSE
        INTERN_FACTORIES.add(new MemberStateFactory("state.effect.armorBonus",
                (name, duration, activeRounder, power, isRandom, type, source) ->
                        new ArmorBonusMemberState(name, duration, activeRounder, source, power))
        );
        INTERN_FACTORIES.add(new MemberStateFactory("state.effect.armorMalus",
                (name, duration, activeRounder, power, isRandom, type, source) ->
                        new ArmorMalusMemberState(name, duration, activeRounder, source, power))
        );

        // INCOMING
        INTERN_FACTORIES.add(new MemberStateFactory("state.effect.shield",
                (name, duration, activeRounder, power, isRandom, type, source) ->
                        new ShieldMemberState(name, duration, activeRounder, source, power))
        );

        // INITIATIVE
        INTERN_FACTORIES.add(new MemberStateFactory("state.effect.relativeSlow",
                (name, duration, activeRounder, power, isRandom, type, source) ->
                        new RelativeSlowMemberState(name, duration, activeRounder, source, power))
        );
        INTERN_FACTORIES.add(new MemberStateFactory("state.effect.slow",
                (name, duration, activeRounder, power, isRandom, type, source) ->
                        new SlowMemberState(name, duration, activeRounder, source, power))
        );
        INTERN_FACTORIES.add(new MemberStateFactory("state.effect.relativeSpeed",
                (name, duration, activeRounder, power, isRandom, type, source) ->
                        new RelativeSpeedMemberState(name, duration, activeRounder, source, power))
        );
        INTERN_FACTORIES.add(new MemberStateFactory("state.effect.speed",
                (name, duration, activeRounder, power, isRandom, type, source) ->
                        new SpeedMemberState(name, duration, activeRounder, source, power))
        );
        INTERN_FACTORIES.add(new MemberStateFactory("state.effect.stun",
                (name, duration, activeRounder, power, isRandom, type, source) ->
                        new StunMemberState(name, duration, source))
        );

        // MANIPULATING
        INTERN_FACTORIES.add(new MemberStateFactory("state.effect.damage",
                (name, duration, activeRounder, power, isRandom, type, source) ->
                        new DamageMemberState(name, duration, activeRounder, source, power, isRandom, type))
        );
        INTERN_FACTORIES.add(new MemberStateFactory("state.effect.heal",
                (name, duration, activeRounder, power, isRandom, type, source) ->
                        new HealMemberState(name, duration, activeRounder, source, power, isRandom))
        );
        INTERN_FACTORIES.add(new MemberStateFactory("state.effect.manaDrain",
                (name, duration, activeRounder, power, isRandom, type, source) ->
                        new ManaDrainMemberState(name, duration, activeRounder, source, power, isRandom))
        );
        INTERN_FACTORIES.add(new MemberStateFactory("state.effect.manaRegeneration",
                (name, duration, activeRounder, power, isRandom, type, source) ->
                        new ManaRegenerationMemberState(name, duration, activeRounder, source, power, isRandom))
        );

        // OTHER
        INTERN_FACTORIES.add(new MemberStateFactory("state.effect.fear",
                (name, duration, activeRounder, power, isRandom, type, source) ->
                        new FearMemberState(name, duration, activeRounder, source))
        );
        INTERN_FACTORIES.add(new MemberStateFactory("state.effect.other",
                (name, duration, activeRounder, power, isRandom, type, source) ->
                        new OtherMemberState(name, duration, activeRounder, source))
        );
        INTERN_FACTORIES.add(new MemberStateFactory("state.effect.snare",
                (name, duration, activeRounder, power, isRandom, type, source) ->
                        new SnareMemberState(name, duration, activeRounder, source))
        );

        FACTORIES = Collections.unmodifiableList(INTERN_FACTORIES);
    }
}
