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
     * Opens the overview page for items.
     */
    public OverviewBasePage openItemPage() {
        openMenu("items-menu");
        return new OverviewBasePage(page);
    }

    /**
     * Opens the overview page for weapons.
     */
    public OverviewBasePage openWeaponPage() {
        openMenu("items-menu", "weapons-menu");
        return new OverviewBasePage(page);
    }

    /**
     * Opens the overview page for jewellery.
     */
    public OverviewBasePage openJewelleryPage() {
        openMenu("items-menu", "jewellery-menu");
        return new OverviewBasePage(page);
    }

    /**
     * Opens the overview page for armor.
     */
    public OverviewBasePage openArmorPage() {
        openMenu("items-menu", "armor-menu");
        return new OverviewBasePage(page);
    }

    /**
     * Opens the overview page for shield.
     */
    public OverviewBasePage openShieldPage() {
        openMenu("items-menu", "shields-menu");
        return new OverviewBasePage(page);
    }

    /**
     * Opens the overview page for material.
     */
    public OverviewBasePage openMaterialPage() {
        openMenu("items-menu", "materials-menu");
        return new OverviewBasePage(page);
    }

    /**
     * Opens the overview page for material.
     */
    public OverviewBasePage openUpgradePage() {
        openMenu("items-menu", "upgrades-menu");
        return new OverviewBasePage(page);
    }

    /**
     * Opens the overview page for crafting recipes.
     */
    public OverviewBasePage openCraftingRecipePage() {
        openMenu("crafting-recipes-menu");
        return new OverviewBasePage(page);
    }

    /**
     * Opens the overview page for upgrade recipes.
     */
    public OverviewBasePage openUpgradeRecipePage() {
        openMenu("crafting-recipes-menu", "upgrade-recipes-menu");
        return new OverviewBasePage(page);
    }

    /**
     * Opens the overview page for spells.
     */
    public OverviewBasePage openSpellPage() {
        openMenu("characters-menu", "spells-menu");
        return new OverviewBasePage(page);
    }

    /**
     * Opens the overview page for talents.
     */
    public OverviewBasePage openTalentPage() {
        openMenu("characters-menu", "talents-menu");
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
        if (submenus.length == 0) {
            page.getByTestId(menu + "-inner").click();
        }
        for (String submenu : submenus) {
            page.getByTestId(submenu).click();
        }
    }

    private void openAppBarMenu(String menu) {
        page.getByTestId("menu-appbar").click();
        page.getByTestId(menu).click();
    }
}
