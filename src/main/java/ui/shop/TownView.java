package ui.shop;

import city.Shop;
import city.Town;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import ui.View;

public class TownView extends View {

    public TownView(Town town){
        super();
        this.stage.setTitle("Stadtansicht");

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.CENTER);

        BorderPane top = new BorderPane();

        Label name = new Label(town.getName());
        name.setFont(Font.font("", FontWeight.BOLD, 20));
        top.setLeft(name);

        root.getChildren().add(top);

        for(Shop shop : town.getShops()){

            HBox line = new HBox();

            Button show = new Button(shop.getName());
            show.setPrefWidth(250);
            show.setOnAction(ev -> shop.getView().show());
            line.getChildren().add(show);

            Button remove = new Button("X");
            remove.setOnAction(ev -> {
                town.removeShop(shop);
                root.getChildren().remove(line);
            });
            line.getChildren().add(remove);

            root.getChildren().add(line);
        }

        if(town.hasBlackMarket()) {
            BorderPane middle = new BorderPane();

            Label market = new Label("Schwarzmarkt");
            middle.setLeft(market);

            root.getChildren().add(middle);

            for(Shop shop : town.getBlackMarket()){

                HBox line = new HBox();

                Button show = new Button(shop.getName());
                show.setPrefWidth(250);
                show.setOnAction(ev -> shop.getView().show());
                line.getChildren().add(show);

                Button remove = new Button("X");
                remove.setOnAction(ev -> {
                    town.removeBlackMarketShop(shop);
                    root.getChildren().remove(line);

                    if(!town.hasBlackMarket()){
                        root.getChildren().remove(middle);
                    }
                });
                line.getChildren().add(remove);

                root.getChildren().add(line);
            }
        }

        BorderPane bottom = new BorderPane();

        Label popLabel = new Label("Bevölkerung: " + town.getPopulation());
        bottom.setLeft(popLabel);

        root.getChildren().add(bottom);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        this.stage.show();
    }
}
