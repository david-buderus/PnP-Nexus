package model.member.interfaces;

import javafx.beans.property.StringProperty;

public interface IMember {
    default String getName() {
        return this.nameProperty().get();
    }

    StringProperty nameProperty();
}
