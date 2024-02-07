package de.pnp.manager.webapp.pages;

import com.microsoft.playwright.Page;

/**
 * Represents the main menu of the webapp.
 */
public class MainMenu extends PageBase {

    public MainMenu(Page page) {
        super(page);
    }

    /**
     * Opens the basic {@link ItemPage}.
     */
    public ItemPage openItemPage() {
        openMenu("items-menu");
        return new ItemPage(page);
    }

    /**
     * Opens the weapon {@link ItemPage}.
     */
    public ItemPage openWeaponPage() {
        openMenu("items-menu", "weapons-menu");
        return new ItemPage(page);
    }

    /**
     * Opens the jewellery {@link ItemPage}.
     */
    public ItemPage openJewelleryPage() {
        openMenu("items-menu", "jewellery-menu");
        return new ItemPage(page);
    }

    /**
     * Opens the armor {@link ItemPage}.
     */
    public ItemPage openArmorPage() {
        openMenu("items-menu", "armor-menu");
        return new ItemPage(page);
    }

    /**
     * Opens the shield {@link ItemPage}.
     */
    public ItemPage openShieldPage() {
        openMenu("items-menu", "shields-menu");
        return new ItemPage(page);
    }

    private void openMenu(String menu, String... submenus) {
        page.getByTestId(menu).click();
        for (String submenu : submenus) {
            page.getByTestId(submenu).click();
        }
    }
}
