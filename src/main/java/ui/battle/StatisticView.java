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
import model.member.BattleMember;
import ui.View;

public class StatisticView extends View {

    public StatisticView(ObservableList<BattleMember> members, Battle battle) {

        stage.setTitle("Statistik");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20, 5, 20, 4));

        TableView<BattleMember> battleView = new TableView<>();
        battleView.setPrefHeight(100);
        battleView.setItems(members);
        battleView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        root.setCenter(battleView);

        TableColumn<BattleMember, String> nameC = new TableColumn<>("Name");
        nameC.setCellValueFactory(b -> new ReadOnlyStringWrapper(b.getValue().getName()));
        battleView.getColumns().add(nameC);

        TableColumn<BattleMember, Integer> damageC = new TableColumn<>("Schaden");
        damageC.setCellValueFactory(b -> new ReadOnlyIntegerWrapper(battle.getDamageDealt(b.getValue())).asObject());
        battleView.getColumns().add(damageC);

        TableColumn<BattleMember, Double> damagePRC = new TableColumn<>("Schaden pro Runde");
        damagePRC.setCellValueFactory(b ->
                new ReadOnlyDoubleWrapper(
                        round((double) battle.getDamageDealt(b.getValue()) / battle.roundProperty().get()))
                        .asObject());
        battleView.getColumns().add(damagePRC);

        TableColumn<BattleMember, Integer> healC = new TableColumn<>("Heilung");
        healC.setCellValueFactory(b -> new ReadOnlyIntegerWrapper(battle.getDamageHealed(b.getValue())).asObject());
        battleView.getColumns().add(healC);

        TableColumn<BattleMember, Double> healPRC = new TableColumn<>("Heilung pro Runde");
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
