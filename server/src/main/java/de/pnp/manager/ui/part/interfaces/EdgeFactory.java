package de.pnp.manager.ui.part.interfaces;

import javafx.beans.value.ObservableDoubleValue;
import javafx.scene.Node;

public interface EdgeFactory<E> {

    Node create(ObservableDoubleValue startX, ObservableDoubleValue startY,
                ObservableDoubleValue endX, ObservableDoubleValue endY, E content);
}
