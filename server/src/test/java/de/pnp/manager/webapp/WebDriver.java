package de.pnp.manager.webapp;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import de.pnp.manager.webapp.pages.MainMenu;
import java.net.URL;

/**
 * Basic web driver to test the webapp.
 */
public class WebDriver {

    private final URL baseUrl;

    private final Browser browser;

    public WebDriver(URL baseUrl, Browser browser) {
        this.baseUrl = baseUrl;
        this.browser = browser;
    }

    /**
     * Opens the {@link MainMenu} and logs into the webapp.
     */
    public MainMenu openMainMenu(String username, String password) {
        BrowserContext browserContext = browser.newContext();
        Page page = browserContext.newPage();
        page.navigate(String.valueOf(baseUrl));
        page.bringToFront();
        
        page.locator("id=username").fill(username);
        page.locator("id=password").fill(password);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign in")).click();

        return new MainMenu(page);
    }
}
