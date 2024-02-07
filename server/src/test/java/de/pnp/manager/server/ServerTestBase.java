package de.pnp.manager.server;


import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import de.pnp.manager.EJvmFlag;
import de.pnp.manager.component.universe.Universe;
import de.pnp.manager.server.configurator.EServerTestConfiguration;
import de.pnp.manager.server.contoller.UserController;
import de.pnp.manager.server.database.UniverseRepository;
import de.pnp.manager.utils.TestUtils;
import de.pnp.manager.webapp.WebDriver;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
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

    /**
     * {@link UserController} used in the test.
     */
    @Autowired
    protected UserController userController;

    @Autowired
    private AutowireCapableBeanFactory beanFactory;

    /**
     * Browser which can be used for testing.
     * <p>
     * Is only initialized if the test is a {@link UiTestServer}.
     */
    protected Browser browser;

    /**
     * WebDriver which can be used for testing.
     * <p>
     * Is only initialized if the test is a {@link UiTestServer}.
     */
    protected WebDriver webDriver;

    private Playwright playwright;

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

    /**
     * Returns the default universe name specified by the corresponding {@link EServerTestConfiguration}.
     */
    protected String getUniverseName() {
        return getTestServerAnnotation().value().getDefaultUniverse();
    }

    /**
     * Returns the default universe specified by the corresponding {@link EServerTestConfiguration}.
     */
    protected Universe getUniverse() {
        return universeRepository.get(getUniverseName()).orElse(null);
    }

    @BeforeAll
    static void setupFlags() {
        System.setProperty(EJvmFlag.DEV_MODE.getFlag(), "true");
    }

    @BeforeEach
    protected void setup() {
        getTestServerAnnotation().value().setupTestData(this);
        if (isUiTestServer()) {
            startUiServer();
            webDriver = new WebDriver(getBaseUrl(), browser);
        }
    }

    @AfterEach
    protected void tearDown() {
        for (Universe universe : universeRepository.getAll()) {
            universeRepository.remove(universe.getName());
        }
        for (String username : userController.getAllUsernames()) {
            userController.removeUser(username);
        }
        if (isUiTestServer()) {
            stopUiServer();
            webDriver = null;
        }
    }

    private void stopUiServer() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    private void startUiServer() {
        playwright = Playwright.create(new Playwright.CreateOptions());
        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions().setHeadless(
            TestUtils.isRunningInCI());
        browser = playwright.chromium().launch(launchOptions);

    }

    private boolean isUiTestServer() {
        List<UiTestServer> testServerAnnotations = getAnnotationsInClassHierarchy(UiTestServer.class);
        return !testServerAnnotations.isEmpty();
    }

    private TestServer getTestServerAnnotation() {
        List<TestServer> testServerAnnotations = getAnnotationsInClassHierarchy(TestServer.class);
        assertThat(testServerAnnotations).size()
            .withFailMessage("Inheritors of TestServerBase must have exactly one TestServer annotation.").isEqualTo(1);
        return testServerAnnotations.get(0);
    }

    private <A extends Annotation> List<A> getAnnotationsInClassHierarchy(Class<A> annotationClass) {
        List<A> testServerAnnotations = new ArrayList<>();
        Class<?> currentClass = getClass();
        do {
            testServerAnnotations.addAll(List.of(currentClass.getDeclaredAnnotationsByType(annotationClass)));
            currentClass = currentClass.getSuperclass();
        } while (currentClass != null);
        return testServerAnnotations;
    }
}
