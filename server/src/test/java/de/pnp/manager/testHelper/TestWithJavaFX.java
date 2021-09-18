package de.pnp.manager.testHelper;

import javafx.application.Application;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestWithJavaFX extends Application {

    @BeforeAll
    @Test
    public static void initJavaFX() {
        new Thread(Application::launch).start();
    }

    @Override
    public void start(Stage primaryStage) { }
}
