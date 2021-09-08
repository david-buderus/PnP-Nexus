package ui.utility.helper;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import ui.IView;
import ui.ViewPart;

public class HelperOverview extends ViewPart {

    public HelperOverview(IView parent) {
        super("helper.title", parent);

        TabPane root = new TabPane();

        Tab calenderTab = new CalenderView(this);
        root.getTabs().add(calenderTab);

        Tab foodTab = new FoodView(this);
        root.getTabs().add(foodTab);

        this.setContent(root);
    }
}
