package de.pnp.manager.model.member;

import de.pnp.manager.model.member.data.FoodRequirement;
import javafx.beans.property.*;

import static de.pnp.manager.main.LanguageUtility.getMessageProperty;

public class FoodMember extends Member {

    private final IntegerProperty starvationLevel;
    private final StringProperty starvationString;
    private final IntegerProperty thirstLevel;
    private final StringProperty thirstString;
    private final ObjectProperty<FoodRequirement> requiredFood;
    private final ObjectProperty<FoodRequirement> neededDrinking;
    private final DoubleProperty eatenFood;
    private final DoubleProperty drunkDrinking;

    public FoodMember() {
        super();
        starvationLevel = new SimpleIntegerProperty(0);
        starvationString = new SimpleStringProperty();
        starvationString.bind(getMessageProperty("helper.food.full"));

        starvationLevel.addListener((ob, o, n) -> {
            switch (n.intValue()) {
                case 1:
                    starvationString.bind(getMessageProperty("helper.food.wellFed"));
                    break;
                case 0:
                    starvationString.bind(getMessageProperty("helper.food.full"));
                    break;
                case -1:
                    starvationString.bind(getMessageProperty("helper.food.hungry"));
                    break;
                case -2:
                    starvationString.bind(getMessageProperty("helper.food.starving"));
                    break;
                case -3:
                    starvationString.bind(getMessageProperty("helper.food.starved"));
                    break;
            }
        });

        thirstLevel = new SimpleIntegerProperty(0);
        thirstString = new SimpleStringProperty();
        thirstString.bind(getMessageProperty("helper.food.noThirst"));

        thirstLevel.addListener((ob, o, n) -> {
            switch (n.intValue()) {
                case 0:
                    thirstString.bind(getMessageProperty("helper.food.noThirst"));
                    break;
                case -1:
                    thirstString.bind(getMessageProperty("helper.food.thirsty"));
                    break;
                case -2:
                    thirstString.bind(getMessageProperty("helper.food.dyingOfThirst"));
                    break;
                case -3:
                    thirstString.bind(getMessageProperty("helper.food.diedOfThirst"));
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

    public ObjectProperty<FoodRequirement> requiredFoodProperty() {
        return requiredFood;
    }

    public ObjectProperty<FoodRequirement> neededDrinkingProperty() {
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
