package de.pnp.manager.server;


import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import de.pnp.manager.component.Universe;
import de.pnp.manager.server.database.UniverseRepository;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

/**
 * Test base for integration tests.
 * <p>
 * Needs to be used in combination with {@link TestServer}.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class ServerTestBase {

    @Autowired
    private UniverseRepository universeRepository;

    @Autowired
    private AutowireCapableBeanFactory beanFactory;

    /**
     * Browser which can be used for testing.
     * <p>
     * Is only initialized if the test is a {@link UiTestServer}.
     */
    protected Browser browser;

    @LocalServerPort
    private int port;

    /**
     * Returns the baseUrl of the test server.
     */
    protected URL getBaseUrl() {
        try {
            return URI.create("http://localhost:" + getPort()).toURL();
        } catch (MalformedURLException e) {
            return fail(e);
        }
    }

    protected int getPort() {
        return port;
    }

    public AutowireCapableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    @BeforeEach
    protected void setup() {
        getTestServerAnnotation().value().setupTestData(this);
        if (isUiTestServer()) {
            startUiServer();
        }
    }

    @AfterEach
    protected void tearDown() {
        for (Universe universe : universeRepository.getAll()) {
            universeRepository.remove(universe.getName());
        }
        if (isUiTestServer()) {
            stopUiServer();
        }
    }

    private void stopUiServer() {
        if (browser != null) {
            browser.close();
        }
    }

    private void startUiServer() {
        try (Playwright playwright = Playwright.create(new Playwright.CreateOptions())) {
            BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions().setHeadless(false);
            browser = playwright.chromium().launch(launchOptions);
        }
    }

    private boolean isUiTestServer() {
        Annotation[] declaredAnnotations = getClass().getDeclaredAnnotations();
        List<UiTestServer> testServerAnnotations = Arrays.stream(declaredAnnotations)
            .filter(UiTestServer.class::isInstance).map(UiTestServer.class::cast).toList();
        return !testServerAnnotations.isEmpty();
    }

    private TestServer getTestServerAnnotation() {
        List<TestServer> testServerAnnotations = getAnnotationsInClassHierarchy();
        assertThat(testServerAnnotations).size()
            .withFailMessage("Inheritors of TestServerBase must have exactly one TestServer annotation.").isEqualTo(1);
        return testServerAnnotations.get(0);
    }

    private List<TestServer> getAnnotationsInClassHierarchy() {
        List<TestServer> testServerAnnotations = new ArrayList<>();
        Class<?> currentClass = getClass();
        do {
            testServerAnnotations.addAll(List.of(currentClass.getDeclaredAnnotationsByType(TestServer.class)));
            currentClass = currentClass.getSuperclass();
        } while (currentClass != null);
        return testServerAnnotations;
    }
}
