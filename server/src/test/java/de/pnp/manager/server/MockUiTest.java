package de.pnp.manager.server;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import de.pnp.manager.server.configurator.EServerTestConfiguration;
import org.junit.jupiter.api.Test;

/**
 * Simple ui test.
 */
@TestServer(EServerTestConfiguration.SIMPLE_UNIVERSE)
@UiTestServer
public class MockUiTest extends ServerTestBase {

    @Test
    public void test() {
        BrowserContext browserContext = browser.newContext();
        Page page = browserContext.newPage();
        page.navigate(String.valueOf(getBaseUrl()));
        page.bringToFront();
    }
}
