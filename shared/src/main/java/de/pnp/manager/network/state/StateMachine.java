package de.pnp.manager.network.state;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class StateMachine<Event> {

    protected Set<State> states;
    protected State currentState;
    protected Map<State, Collection<Transition<Event>>> transitions;
    protected Consumer<Event> onNoTransition;

    public StateMachine(Set<State> states, State start) {
        this.states = states;
        this.currentState = start;
        this.transitions = new HashMap<>();
        this.onNoTransition = null;
    }

    public boolean fire(Event event) {

        for (Transition<Event> transition : transitions.getOrDefault(currentState, Collections.emptyList())) {
            if (
                    transition.from == currentState &&
                            transition.eventCheck.apply(event) &&
                            states.contains(transition.to)
            ) {
                if (transition.eventHandler != null) {
                    transition.eventHandler.accept(event);
                }
                this.currentState = transition.to;
                return true;
            }
        }

        if (onNoTransition != null) {
            onNoTransition.accept(event);
        }

        return false;
    }

    public void registerTransition(State from, State to, @NotNull Function<Event, Boolean> eventCheck, Consumer<Event> eventHandler) {
        if (states.contains(from) && states.contains(to)) {
            transitions.computeIfAbsent(from, s -> new ArrayList<>()).add(new Transition<>(from, to, eventCheck, eventHandler));
        }
    }

    public void registerTransition(State fromTo, @NotNull Function<Event, Boolean> eventCheck, Consumer<Event> eventHandler) {
        this.registerTransition(fromTo, fromTo, eventCheck, eventHandler);
    }

    public void setOnNoTransition(Consumer<Event> onNoTransition) {
        this.onNoTransition = onNoTransition;
    }

    private static class Transition<T> {
        final State from;
        final State to;
        final Function<T, Boolean> eventCheck;
        final Consumer<T> eventHandler;

        public Transition(State from, State to, @NotNull Function<T, Boolean> eventCheck, Consumer<T> eventHandler) {
            this.from = from;
            this.to = to;
            this.eventCheck = eventCheck;
            this.eventHandler = eventHandler;
        }
    }
}
