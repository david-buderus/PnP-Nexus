package de.pnp.manager.network.state;

public interface IEventHandler<Event> {

    boolean applyEvent(Event event);
}
