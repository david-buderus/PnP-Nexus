package de.pnp.manager.network.state;

import de.pnp.manager.network.message.BaseMessage;

import java.util.Set;
import java.util.function.Consumer;

public class BaseMessageStateMachine extends StateMachine<BaseMessage> {

    public BaseMessageStateMachine(Set<State> states, State start) {
        super(states, start);
    }

    public void registerTransition(State from, State to, int messageID, IEventHandler<BaseMessage> eventHandler) {
        registerTransition(from, to, event -> event.getId() == messageID, eventHandler);
    }

    public void registerTransition(State from, State to, int messageID, INonConditionalEventHandler<BaseMessage> eventHandler) {
        registerTransition(from, to, event -> event.getId() == messageID, eventHandler);
    }

    public void registerTransition(State fromTo, int messageID, IEventHandler<BaseMessage> eventHandler) {
        registerTransition(fromTo, fromTo, messageID, eventHandler);
    }

    public void registerTransition(State fromTo, int messageID, INonConditionalEventHandler<BaseMessage> eventHandler) {
        registerTransition(fromTo, fromTo, messageID, eventHandler);
    }
}
