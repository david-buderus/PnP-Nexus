package ui.battle.state;

import javafx.beans.property.ReadOnlyStringProperty;
import manager.LanguageUtility;
import model.interfaces.WithToStringProperty;
import model.member.BattleMember;
import model.member.data.AttackTypes;
import model.member.state.implementations.defense.ArmorMinusMemberState;
import model.member.state.implementations.defense.ArmorPlusMemberState;
import model.member.state.implementations.incoming.ShieldMemberState;
import model.member.state.implementations.initiative.*;
import model.member.state.implementations.manipulating.DamageMemberState;
import model.member.state.implementations.manipulating.HealMemberState;
import model.member.state.implementations.manipulating.ManaDrainMemberState;
import model.member.state.implementations.manipulating.ManaRegenerationMemberState;
import model.member.state.implementations.other.FearMemberState;
import model.member.state.implementations.other.OtherMemberState;
import model.member.state.implementations.other.SnareMemberState;
import model.member.state.interfaces.IMemberState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MemberStateFactory implements WithToStringProperty {

    private final String key;
    private final MemberStateProducer producer;
    private final IMemberState defaultState;

    private MemberStateFactory(String key, MemberStateProducer producer) {
        this.key = key;
        this.producer = producer;
        this.defaultState = producer.create("default", 0, false, 0, false, AttackTypes.direct, null);
    }

    public IMemberState create(String name, int duration, boolean activeRounder, float power, boolean isRandom, AttackTypes type, BattleMember source) {
        return producer.create(name, duration, activeRounder, power, isRandom, type, source);
    }

    public IMemberState getDefaultState() {
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
        INTERN_FACTORIES.add(new MemberStateFactory("state.effect.armorPlus",
                (name, duration, activeRounder, power, isRandom, type, source) ->
                        new ArmorPlusMemberState(name, duration, activeRounder, source, power))
        );
        INTERN_FACTORIES.add(new MemberStateFactory("state.effect.armorMinus",
                (name, duration, activeRounder, power, isRandom, type, source) ->
                        new ArmorMinusMemberState(name, duration, activeRounder, source, power))
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
