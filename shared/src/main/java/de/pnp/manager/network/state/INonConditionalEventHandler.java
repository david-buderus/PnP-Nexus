package de.pnp.manager.network.state;

public interface INonConditionalEventHandler<Event> extends IEventHandler<Event> {

    void applyNonConditionalEvent(Event event);

    @Override
    default boolean applyEvent(Event event) {
        applyNonConditionalEvent(event);
        return true;
    }
}
