package de.pnp.manager.testHelper;

import de.pnp.manager.model.manager.Manager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ManagerHelper {

    protected static Manager manager;

    @BeforeAll
    @Test
    public static void setup() {
        manager = new Manager();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    @Test
    public static void close() {
        try {
            manager.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
