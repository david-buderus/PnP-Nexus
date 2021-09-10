package de.pnp.manager.network;

import de.pnp.manager.main.Utility;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.message.MessageType;
import de.pnp.manager.network.message.login.LoginRequestMessage;
import de.pnp.manager.network.message.login.LoginResponseMessage;
import de.pnp.manager.testHelper.TestClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Calendar;

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
    public void test() throws IOException {
        BaseMessage<?> resp1 = client.sendMessage(new LoginRequestMessage("Test", Calendar.getInstance().getTime()));

        assertEquals(MessageType.loginResponse, resp1.getType());
        assertEquals("Test", ((LoginResponseMessage.LoginResponseData) resp1.getData()).getName());
    }

    @AfterAll
    @Test
    public static void tearDown() {
        client.closeConnection();
        server.stop();
    }
}
