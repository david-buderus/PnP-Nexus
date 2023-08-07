package de.pnp.manager.server;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;

@TestServer(EServerTestConfiguration.BASIC_TEST_SERVER)
@UiTestServer
public class BasicTestServer extends ServerTestBase {

    @Test
    public void test() {
        BrowserContext browserContext = browser.newContext();
        Page page = browserContext.newPage();
        try {
            page.navigate(String.valueOf(testServerParameters.uri().toURL()));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        page.bringToFront();
    }
}
