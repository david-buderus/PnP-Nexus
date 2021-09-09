package de.pnp.manager.network;

import de.pnp.manager.main.Utility;
import de.pnp.manager.testHelper.TestClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConnectionTest {

    protected static TestClient client;
    protected static ServerNetworkHandler server;

    @BeforeAll
    @Test
    public static void setup() {
        server = new ServerNetworkHandler();
        server.start();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        client = new TestClient();
        client.connect("127.0.0.1", Utility.getConfig().getInt("server.port"));
    }

    @Test
    public void test() {
        String resp1 = client.sendMessage("hello");
        String resp2 = client.sendMessage("world");
        String resp3 = client.sendMessage("!");
        String resp4 = client.sendMessage(".");

        assertEquals("hello", resp1);
        assertEquals("world", resp2);
        assertEquals("!", resp3);
        assertEquals("bye", resp4);
    }

    @AfterAll
    @Test
    public static void tearDown() {
        client.closeConnection();
        server.stop();
    }
}
