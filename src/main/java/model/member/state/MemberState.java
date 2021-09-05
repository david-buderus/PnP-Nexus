package model.member.state;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import manager.LanguageUtility;
import model.interfaces.WithToStringProperty;
import model.member.interfaces.IBattleMember;
import model.member.state.interfaces.IMemberState;

public abstract class MemberState implements IMemberState, WithToStringProperty {

    protected final int maxDuration;
    protected final int imageID;
    protected final String name;
    protected final IntegerProperty duration;
    protected final StringProperty durationDisplay;
    protected final IBattleMember source;

    public MemberState(String name, int imageID, int duration, IBattleMember source) {
        this.name = name;
        this.imageID = imageID;
        this.duration = new SimpleIntegerProperty(duration);
        this.maxDuration = duration;
        this.source = source;
        this.durationDisplay = new SimpleStringProperty("");
        this.durationDisplay.bind(this.duration.asString().concat(" ").concat(LanguageUtility.getMessageProperty("state.info.rounds")));
    }

    @Override
    public IntegerProperty durationProperty() {
        return duration;
    }

    @Override
    public StringProperty durationDisplayProperty() {
        return durationDisplay;
    }

    @Override
    public int getMaxDuration() {
        return maxDuration;
    }

    @Override
    public IBattleMember getSource() {
        return source;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getImageID() {
        return imageID;
    }
}
