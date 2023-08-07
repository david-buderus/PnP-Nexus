package de.pnp.manager.server;


import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import de.pnp.manager.component.Universe;
import de.pnp.manager.server.database.UniverseRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(initializers = ServerTestBase.ServerTestBaseInitializer.class)
public abstract class ServerTestBase {

    @Autowired
    private UniverseRepository universeRepository;

    private @Autowired AutowireCapableBeanFactory beanFactory;

    protected @Nullable Browser browser;

    protected TestServerParameters testServerParameters;

    ServerTestBase() {
        initializeConfiguration();
    }

    protected int getPort() {
        return getTestServerAnnotation().value().getPort();
    }

    public AutowireCapableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    private void initializeConfiguration() {
        ServerTestBaseInitializer.PORT = getPort();
    }

    @BeforeEach
    protected void setup() {
        testServerParameters = getTestServerAnnotation().value().setupTestData(this);
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
        Playwright playwright = Playwright.create(new Playwright.CreateOptions());
        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions().setHeadless(false);
        browser = playwright.chromium().launch(launchOptions);

    }

    private boolean isUiTestServer() {
        Annotation[] declaredAnnotations = getClass().getDeclaredAnnotations();
        List<UiTestServer> testServerAnnotations = Arrays.stream(declaredAnnotations).filter(UiTestServer.class::isInstance).map(UiTestServer.class::cast).toList();
        return !testServerAnnotations.isEmpty();
    }

    private TestServer getTestServerAnnotation() {
        List<TestServer> testServerAnnotations = getAnnotationsInClassHierarchy();
        assertThat(testServerAnnotations).size().withFailMessage("Inheritors of TestServerBase must have exactly one TestServer annotation.").isEqualTo(1);
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

    public static class ServerTestBaseInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public static int PORT;

        @Override
        public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
            System.setProperty("server.port", String.valueOf(PORT));
        }
    }
}
