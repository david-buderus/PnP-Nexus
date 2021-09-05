package ui.battle;

import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import model.Battle;
import model.member.interfaces.IBattleMember;
import ui.View;

import static manager.LanguageUtility.getMessageProperty;

public class StatisticView extends View {

    public StatisticView(ObservableList<IBattleMember> members, Battle battle) {
        super("statistics.title");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20, 5, 20, 4));

        TableView<IBattleMember> battleView = new TableView<>();
        battleView.setPrefHeight(100);
        battleView.setItems(members);
        battleView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        root.setCenter(battleView);

        TableColumn<IBattleMember, String> nameC = new TableColumn<>();
        nameC.textProperty().bind(getMessageProperty("statistics.name"));
        nameC.setCellValueFactory(b -> new ReadOnlyStringWrapper(b.getValue().getName()));
        battleView.getColumns().add(nameC);

        TableColumn<IBattleMember, Integer> damageC = new TableColumn<>();
        damageC.textProperty().bind(getMessageProperty("statistics.damage"));
        damageC.setCellValueFactory(b -> new ReadOnlyIntegerWrapper(battle.getDamageDealt(b.getValue())).asObject());
        battleView.getColumns().add(damageC);

        TableColumn<IBattleMember, Double> damagePRC = new TableColumn<>();
        damagePRC.textProperty().bind(getMessageProperty("statistics.damagePerRound"));
        damagePRC.setCellValueFactory(b ->
                new ReadOnlyDoubleWrapper(
                        round((double) battle.getDamageDealt(b.getValue()) / battle.roundProperty().get()))
                        .asObject());
        battleView.getColumns().add(damagePRC);

        TableColumn<IBattleMember, Integer> healC = new TableColumn<>();
        healC.textProperty().bind(getMessageProperty("statistics.heal"));
        healC.setCellValueFactory(b -> new ReadOnlyIntegerWrapper(battle.getDamageHealed(b.getValue())).asObject());
        battleView.getColumns().add(healC);

        TableColumn<IBattleMember, Double> healPRC = new TableColumn<>();
        healPRC.textProperty().bind(getMessageProperty("statistics.healPerRound"));
        healPRC.setCellValueFactory(b ->
                new ReadOnlyDoubleWrapper(
                        round((double) battle.getDamageHealed(b.getValue()) / battle.roundProperty().get()))
                        .asObject());
        battleView.getColumns().add(healPRC);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private double round(double value) {
        double d = Math.pow(10, 2);
        return Math.round(value * d) / d;
    }
}
