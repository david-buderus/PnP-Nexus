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

    /**
     * Opens the overview page for spells.
     */
    public OverviewBasePage openSpellPage() {
        openMenu("characters-menu", "spells-menu");
        return new OverviewBasePage(page);
    }

    /**
     * Opens the overview page for primary attributes.
     */
    public OverviewBasePage openPrimaryAttributePage() {
        openMenu("characters-menu", "primary-attributes-menu");
        return new OverviewBasePage(page);
    }

    /**
     * Opens the overview page for secondary attributes.
     */
    public OverviewBasePage openSecondaryAttributePage() {
        openMenu("characters-menu", "secondary-attributes-menu");
        return new OverviewBasePage(page);
    }

    /**
     * Opens the {@link UserOverviewPage}.
     */
    public UserOverviewPage openUserOverviewPage() {
        openMenu("admin-menu", "users-menu");
        return new UserOverviewPage(page);
    }

    /**
     * Opens the {@link UserPage}.
     */
    public UserPage openUserPage() {
        openAppBarMenu("user-menu");
        return new UserPage(page);
    }

    /**
     * Opens the {@link UserPreferencePage}.
     */
    public UserPreferencePage openUserPreferencesPage() {
        openAppBarMenu("preferences-menu");
        return new UserPreferencePage(page);
    }

    private void openMenu(String menu, String... submenus) {
        page.getByTestId(menu).click();
        for (String submenu : submenus) {
            page.getByTestId(submenu).click();
        }
    }

    private void openAppBarMenu(String menu) {
        page.getByTestId("menu-appbar").click();
        page.getByTestId(menu).click();
        page.mouse().click(0, 0); // Closes the popup menu
    }
}
