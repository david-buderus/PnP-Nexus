package model.member;

import javafx.beans.property.*;
import model.member.data.Requirement;

public class FoodMember extends Member {

    private IntegerProperty starvationLevel;
    private StringProperty starvationString;
    private IntegerProperty thirstLevel;
    private StringProperty thirstString;
    private ObjectProperty<Requirement> requiredFood;
    private ObjectProperty<Requirement> neededDrinking;
    private DoubleProperty eatenFood;
    private DoubleProperty drunkDrinking;

    public FoodMember() {
        super();
        starvationLevel = new SimpleIntegerProperty(0);
        starvationString = new SimpleStringProperty("Satt");

        starvationLevel.addListener((ob, o, n) -> {
            switch (n.intValue()) {
                case 1:
                    starvationString.set("Wohl Gen\u00e4hrt");
                    break;
                case 0:
                    starvationString.set("Satt");
                    break;
                case -1:
                    starvationString.set("Hungrig");
                    break;
                case -2:
                    starvationString.set("Am Verhungern");
                    break;
                case -3:
                    starvationString.set("Verhungert");
                    break;
            }
        });

        thirstLevel = new SimpleIntegerProperty(0);
        thirstString = new SimpleStringProperty("Sitt");

        thirstLevel.addListener((ob, o, n) -> {
            switch (n.intValue()) {
                case 0:
                    thirstString.set("Sitt");
                    break;
                case -1:
                    thirstString.set("Durstig");
                    break;
                case -2:
                    thirstString.set("Am Verdursten");
                    break;
                case -3:
                    thirstString.set("Verdurstet");
                    break;
            }
        });


        requiredFood = new SimpleObjectProperty<>();
        neededDrinking = new SimpleObjectProperty<>();
        eatenFood = new SimpleDoubleProperty(0);
        drunkDrinking = new SimpleDoubleProperty(0);
    }

    public void nextDay() {
        if (eatenFood.get() > requiredFood.get().toInteger()) {
            starvationLevelUp();
        } else if (eatenFood.get() < requiredFood.get().toInteger()) {
            starvationLevelDown();
        }

        if (drunkDrinking.get() > neededDrinking.get().toInteger()) {
            thirstLevelUp();
        } else if (drunkDrinking.get() < neededDrinking.get().toInteger()) {
            thirstLevelDown();
        }

        eatenFood.set(0);
        drunkDrinking.set(0);
    }

    private void starvationLevelUp() {
        if (starvationLevel.get() == 1) {
            return;
        }
        starvationLevel.set(starvationLevel.get() + 1);
    }

    private void starvationLevelDown() {
        if (starvationLevel.get() == -3) {
            return;
        }
        starvationLevel.set(starvationLevel.get() - 1);
    }

    private void thirstLevelUp() {
        if (thirstLevel.get() == 0) {
            return;
        }
        thirstLevel.set(thirstLevel.get() + 1);
    }

    private void thirstLevelDown() {
        if (thirstLevel.get() == -3) {
            return;
        }
        thirstLevel.set(thirstLevel.get() - 1);
    }

    public ReadOnlyStringProperty starvationProperty() {
        return starvationString;
    }

    public ReadOnlyStringProperty thirstProperty() {
        return thirstString;
    }

    public ObjectProperty<Requirement> requiredFoodProperty() {
        return requiredFood;
    }

    public ObjectProperty<Requirement> neededDrinkingProperty() {
        return neededDrinking;
    }

    public DoubleProperty drunkDrinkingProperty() {
        return drunkDrinking;
    }

    public DoubleProperty eatenFoodProperty() {
        return eatenFood;
    }

    public void drink(Double ammount) {
        drunkDrinking.set(drunkDrinking.get() + ammount);
    }

    public void eat(Double ammount) {
        eatenFood.set(eatenFood.get() + ammount);
    }
}
