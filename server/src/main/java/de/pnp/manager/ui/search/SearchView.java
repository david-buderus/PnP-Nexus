package de.pnp.manager.ui.search;

import de.pnp.manager.ui.IView;
import de.pnp.manager.ui.ViewPart;
import de.pnp.manager.ui.part.FilteredTableView;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.Random;

public abstract class SearchView<Typ> extends ViewPart {

    protected FilteredTableView<Typ> tableView;
    protected ListProperty<Typ> fullList;
    protected Pane root;
    protected final Random rand;

    public SearchView(String title, IView parent) {
        super(title, parent);
        this.fullList = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.tableView = new FilteredTableView<>(fullList);
        this.rand = new Random();

        VBox root = new VBox(5);
        root.setPadding(new Insets(10, 20, 20, 20));
        root.setAlignment(Pos.CENTER);

        root.getChildren().add(tableView);

        this.setContent(root);
        this.root = root;
    }
}
