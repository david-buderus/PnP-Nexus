package ui.shop;

import city.shop.WeaponTrader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import model.item.Weapon;

public class WeaponTraderView extends ShopView {

    private WeaponTrader trader;

    public WeaponTraderView(WeaponTrader trader){
        this.trader = trader;

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        String[] labels = new String[]{"Name", "Typ", "Initiative", "Würfel", "Schaden", "Treffer", "Seltenheit", "Preis", "Effekt", "Slots", "Voraussetzung"};
        String[] names = new String[]{"name", "subTyp", "initiative", "dice", "damage", "hit", "rarity", "cost", "effect", "slots", "requirement"};

        root.getChildren().add(createTable(trader.getWeapons(), labels, names, Weapon.class));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    protected void format(Object object, Text text){
        if(object instanceof Weapon){
            Weapon weapon = (Weapon) object;

            if(weapon.getAmount() > 0){
                text.setFill(Paint.valueOf("#000000"));
            } else {
                text.setFill(Paint.valueOf("#ff0000"));
            }
        }
    }
}
