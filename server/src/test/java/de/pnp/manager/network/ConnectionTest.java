package de.pnp.manager.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.pnp.manager.main.Utility;
import de.pnp.manager.model.manager.Manager;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.message.DataMessage;
import de.pnp.manager.network.message.login.LoginRequestMessage;
import de.pnp.manager.network.message.login.LoginResponseMessage;
import de.pnp.manager.network.message.session.QuerySessions;
import de.pnp.manager.testHelper.TestClient;
import de.pnp.manager.testHelper.TestWithJavaFX;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConnectionTest extends TestWithJavaFX {

    protected static ServerNetworkHandler server;

    @BeforeAll
    @Test
    public static void setup() {
        server = new ServerNetworkHandler(new Manager());
        server.start();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() throws IOException {
        TestClient client = createTestClient();
        BaseMessage resp1 = client.sendMessage(new LoginRequestMessage("Test", Calendar.getInstance().getTime()));

        assertEquals(1000, resp1.getId());
        assertEquals("Test", ((LoginResponseMessage) resp1).getData().getName());
    }

    @Test
    public void sessionTest() throws IOException {
        TestClient client = createTestClient();
        client.sendMessage(new LoginRequestMessage("Test", Calendar.getInstance().getTime()));
        String resp1 = client.sendMessageWithRawResponse(new QuerySessions(Calendar.getInstance().getTime()));

        //Sessions deserialization is not needed at the server side
        SessionMessage message = new ObjectMapper().readValue(resp1, SessionMessage.class);

        assertEquals(message.getId(), 2000);
        assertTrue(message.getData().stream().findFirst().isPresent());
        assertEquals(message.getData().stream().findFirst().get().get("sessionID"), "0");
        assertEquals(message.getData().stream().findFirst().get().get("sessionName"), "PnP");
    }

    @AfterAll
    @Test
    public static void tearDown() {
        server.stop();
    }

    private static class SessionMessage extends DataMessage<Collection<Map<String, Object>>> {
    }

    private TestClient createTestClient() {
        TestClient client = new TestClient();
        client.connect("127.0.0.1", Utility.getConfig().getInt("server.port"));
        return client;
    }
}
