package de.pnp.manager.ui.setting;

import de.pnp.manager.main.Utility;
import de.pnp.manager.ui.IView;
import de.pnp.manager.ui.ViewPart;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static de.pnp.manager.main.LanguageUtility.getMessageProperty;

public class SettingsView extends ViewPart {

    protected Collection<SettingPane<?>> settingPanes;

    public SettingsView(String title, Collection<String> settingFile, IView parent) {
        super(title, parent);
        this.settingPanes = new ArrayList<>();

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        this.setContent(root);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPrefHeight(500);
        scrollPane.setFitToWidth(true);
        root.setCenter(scrollPane);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5);
        grid.setVgap(5);
        scrollPane.setContent(grid);

        BorderPane controls = new BorderPane();
        controls.setPadding(new Insets(10, 0, 0, 0));
        root.setBottom(controls);

        HBox buttonBox = new HBox(5);
        buttonBox.setAlignment(Pos.CENTER);
        controls.setLeft(buttonBox);

        Button resetButton = new Button();
        resetButton.textProperty().bind(getMessageProperty("settings.button.reset"));
        resetButton.setPrefWidth(200);
        resetButton.setOnAction(ev -> resetChanges());
        buttonBox.getChildren().add(resetButton);

        Button defaultButton = new Button();
        defaultButton.textProperty().bind(getMessageProperty("settings.button.default"));
        defaultButton.setPrefWidth(200);
        defaultButton.setOnAction(ev -> toDefault());
        buttonBox.getChildren().add(defaultButton);

        Button applyButton = new Button();
        applyButton.textProperty().bind(getMessageProperty("settings.button.apply"));
        applyButton.setPrefWidth(200);
        applyButton.setOnAction(ev -> applyChanges());
        controls.setRight(applyButton);

        int row = 0;
        for (String line : settingFile) {

            if (line.startsWith("#")) {
                Label headline = new Label();
                headline.setFont(Font.font(20));
                headline.setPadding(new Insets(10, 0, 0, 0));

                final String[] unlocalizedHeadline = line.substring(1).split(",");

                headline.textProperty().bind(getMessageProperty(unlocalizedHeadline[0]));

                if (unlocalizedHeadline.length > 1) {
                    Tooltip tooltip = new Tooltip();
                    tooltip.setFont(Font.font(10));
                    tooltip.textProperty().bind(getMessageProperty(unlocalizedHeadline[1]));
                    headline.setTooltip(tooltip);
                }

                grid.add(headline, 0, row, 3, 1);
            } else {
                SettingPane<?> pane = SettingPane.convertSettingLine(line);
                this.settingPanes.add(pane);
                pane.addTo(grid, row);
            }

            row += 1;
        }


        BooleanExpression dirty = new ReadOnlyBooleanWrapper(false);

        for (SettingPane<?> pane : settingPanes) {
            dirty = dirty.or(pane.dirtyProperty());
        }

        applyButton.disableProperty().bind(dirty.not());
    }

    protected void resetChanges() {
        for (SettingPane<?> pane : settingPanes) {
            pane.reset();
        }
    }

    protected void toDefault() {
        for (SettingPane<?> pane : settingPanes) {
            pane.toDefault();
        }
    }

    protected void applyChanges() {
        Collection<ImmutablePair<String, Object>> changedProperties = new ArrayList<>();

        for (SettingPane<?> pane : settingPanes) {
            if (pane.isDirty()) {
                changedProperties.add(new ImmutablePair<>(pane.getKey(), pane.getContent()));
            }
        }

        Utility.saveToCustomConfig(changedProperties);

        for (SettingPane<?> pane : settingPanes) {
            pane.checkDirty();
        }
    }
}
