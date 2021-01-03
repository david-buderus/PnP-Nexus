package ui.search;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;
import manager.Database;
import manager.Utility;
import model.Rarity;
import model.item.Jewellery;
import ui.IView;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static manager.LanguageUtility.getMessageProperty;

public class JewelleryView extends EquipmentView<Jewellery> {

    public JewelleryView(IView parent) {
        super("search.jewellery.title", parent, Jewellery.class, Database.jewelleryList);
        this.defaultName = getMessageProperty("search.default.gem");

        VBox root = this.createRoot(
                new String[]{"column.name", "column.typ", "column.equipment.material", "column.jewellery.gem",
                        "column.item.rarity", "column.item.price", "column.effect", "column.equipment.slots",
                        "column.equipment.requirement"},
                new String[]{"name", "subTyp", "material", "gem", "rarity", "cost", "effect", "slots", "requirement"});

        this.addControls(root);

        update();
        this.setContent(root);
    }

    protected void search() {
        for (int i = 0; i < searchCount.intValue(); i++) {
            Rarity rarity = this.rarity.get() == Rarity.unknown ? Utility.getRandomRarity() : this.rarity.get();
            Collection<String> material = this.material.get().equals(defaultMaterial.get()) ? Database.getRandomMaterial()
                    : Collections.singletonList(this.material.get());

            Stream<Jewellery> stream = Database.jewelleryList.stream().filter(w -> w.getRarity() == rarity);
            stream = stream.filter(w -> material.contains(w.getMaterial()));

            if (!this.name.get().equals(defaultName.get())) {
                stream = stream.filter(w -> w.getGem().equals(this.name.get()));
            }
            if (!this.typ.get().equals(defaultTyp.get())) {
                stream = stream.filter(w -> w.getSubTyp().equals(this.typ.get()));
            }
            List<Jewellery> result = stream.collect(Collectors.toList());

            if (result.size() > 0) {
                Jewellery found = (Jewellery) result.get(rand.nextInt(result.size())).getWithUpgrade();
                Jewellery other = getItem(found);

                if (other != null) {
                    other.addAmount(1);
                } else {
                    fullList.add(found);
                }
            }
        }
    }

    protected void update() {
        if (!Database.jewelleryList.isEmpty()) {
            this.disabled.set(false);

            ObservableList<String> gem = FXCollections.observableArrayList(defaultName.get());
            ObservableList<String> material = FXCollections.observableArrayList(defaultMaterial.get());
            ObservableList<String> typ = FXCollections.observableArrayList(defaultTyp.get());

            for (Jewellery jewellery : Database.jewelleryList) {
                String m = jewellery.getMaterial();
                String g = jewellery.getGem();
                String t = jewellery.getSubTyp();

                if (!material.contains(m)) {
                    material.add(m);
                }
                if (!gem.contains(g)) {
                    gem.add(g);
                }
                if (!typ.contains(t)) {
                    typ.add(t);
                }
            }
            materials.set(material);
            names.set(gem.sorted());
            types.set(typ.sorted());
        } else {
            disabled.set(true);
            names.set(FXCollections.observableArrayList(defaultName.get()));
            materials.set(FXCollections.observableArrayList(defaultMaterial.get()));
            types.set(FXCollections.observableArrayList(defaultTyp.get()));
        }
    }
}
