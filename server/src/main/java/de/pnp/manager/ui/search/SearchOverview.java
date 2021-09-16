package de.pnp.manager.ui.search;

import de.pnp.manager.ui.IView;
import de.pnp.manager.ui.ViewPart;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class SearchOverview extends ViewPart {

    public SearchOverview(IView parent) {
        super("search.title", parent);

        TabPane root = new TabPane();

        Tab weaponTab = new WeaponView(this);
        root.getTabs().add(weaponTab);

        Tab armorTab = new ArmorView(this);
        root.getTabs().add(armorTab);

        Tab jewelleryTab = new JewelleryView(this);
        root.getTabs().add(jewelleryTab);

        Tab spellTab = new SpellView(this);
        root.getTabs().add(spellTab);

        Tab plantTab = new PlantView(this);
        root.getTabs().add(plantTab);

        Tab dungeonTab = new DungeonLootView(this);
        root.getTabs().add(dungeonTab);

        Tab craftingTab = new CraftingView(this);
        root.getTabs().add(craftingTab);

        this.setContent(root);
    }
}
