package ui.utility.helper;

import javafx.beans.binding.StringExpression;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import ui.IView;
import ui.ViewPart;
import ui.part.NumStringConverter;

import static manager.LanguageUtility.getMessageProperty;

public class CalenderView extends ViewPart {

    private static final int newMoon = 1440;

    private final IntegerProperty day, month, year;
    private final Label info;

    public CalenderView(IView parent) {
        super("helper.calender.title", parent);

        this.year = new SimpleIntegerProperty(0);
        this.year.addListener((ob, o, n) -> update());

        this.month = new SimpleIntegerProperty(1);
        this.month.addListener((ob, o, n) -> {
            int yearChange;
            if (n.intValue() < 1) {
                yearChange = (n.intValue() - 1) / 10 - 1;
            } else {
                yearChange = (n.intValue() - 1) / 10;
            }
            this.year.set(this.year.get() + yearChange);
            this.month.set((n.intValue() + 9) % 10 + 1);
            update();
        });

        this.day = new SimpleIntegerProperty(1);
        this.day.addListener((ob, o, n) -> {
            int monthChange;
            if (n.intValue() < 1) {
                monthChange = (n.intValue() - 1) / 40 - 1;
            } else {
                monthChange = (n.intValue() - 1) / 40;
            }
            this.month.set(this.month.getValue() + monthChange);
            this.day.set((n.intValue() + 39) % 40 + 1);
            update();
        });

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.CENTER);

        info = new Label();
        info.setAlignment(Pos.CENTER);
        info.setTextAlignment(TextAlignment.CENTER);
        root.getChildren().add(info);
        update();

        HBox fieldLine = new HBox(5);
        fieldLine.setAlignment(Pos.CENTER);

        TextField dayField = new TextField();
        dayField.textProperty().bindBidirectional(day, new NumStringConverter(1));
        dayField.setPrefWidth(50);
        fieldLine.getChildren().add(dayField);

        TextField monthField = new TextField();
        monthField.textProperty().bindBidirectional(month, new NumStringConverter(1));
        monthField.setPrefWidth(50);
        fieldLine.getChildren().add(monthField);

        TextField yearField = new TextField();
        yearField.textProperty().bindBidirectional(year, new NumStringConverter(1));
        yearField.setPrefWidth(100);
        fieldLine.getChildren().add(yearField);

        root.getChildren().add(fieldLine);

        HBox buttonLine = new HBox(20);
        buttonLine.setAlignment(Pos.CENTER);

        Button prev = new Button();
        prev.textProperty().bind(getMessageProperty("helper.calender.button.previous"));
        prev.setOnAction(ev -> day.set(day.get() - 1));
        buttonLine.getChildren().add(prev);

        Button next = new Button();
        next.textProperty().bind(getMessageProperty("helper.calender.button.next"));
        next.setOnAction(ev -> day.set(day.get() + 1));
        buttonLine.getChildren().add(next);

        root.getChildren().add(buttonLine);

        this.setContent(root);
    }

    private void update() {
        this.info.textProperty().bind(
                format("Grodon", 40).concat("\n")
                        .concat(format("Heros", 43)).concat("\n")
                        .concat(format("Ados", 27))

        );
    }

    private StringExpression format(String moonName, int circle) {
        StringExpression moon = new ReadOnlyStringWrapper(moonName + " ");
        long phase = ((getAbsoluteDate() - newMoon) % circle + circle) % circle;

        if (phase == 0) {
            return moon.concat(getMessageProperty("helper.calender.isInNewMoon")).concat(".");
        }
        if (phase < circle / 2) {
            return moon.concat(getMessageProperty("helper.calender.isInXDaysFullMoon"))
                    .concat(" ").concat(circle / 2 - phase).concat(" ")
                    .concat(getMessageProperty("helper.calender.isInXDaysFullMoon.ending"));
        }
        if (phase == circle / 2) {
            return moon.concat(getMessageProperty("helper.calender.isInFullMoon")).concat(".");
        }

        return moon.concat(getMessageProperty("helper.calender.isInXDaysNewMoon"))
                .concat(" ").concat(circle - phase).concat(" ")
                .concat(getMessageProperty("helper.calender.isInXDaysNewMoon.ending"));
    }

    private long getAbsoluteDate() {
        return this.day.get() - 1 + (this.month.get() - 1) * 40L + this.year.get() * 400L;
    }
}
