package de.pnp.manager.testHelper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import de.pnp.manager.impl.ClientImpl;
import de.pnp.manager.impl.PlayerCharacterImpl;
import de.pnp.manager.impl.PnPCharacterImpl;
import de.pnp.manager.impl.SessionImpl;
import de.pnp.manager.model.character.IPlayerCharacter;
import de.pnp.manager.model.character.IPnPCharacter;
import de.pnp.manager.model.other.Container;
import de.pnp.manager.network.client.IClient;
import de.pnp.manager.network.interfaces.Client;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.serializer.IPnPCharacterDeserializer;
import de.pnp.manager.network.serializer.ServerModule;
import de.pnp.manager.network.session.ISession;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TestClient {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private final ObjectMapper mapper;
    private String clientID;
    private String sessionID;
    private IPlayerCharacter character;
    private Container container;

    public TestClient() {
        this.socket = null;
        this.in = new BufferedReader(BufferedReader.nullReader());
        this.out = new PrintWriter(OutputStream.nullOutputStream());
        this.mapper = new ObjectMapper();

        SimpleModule module = new ServerModule();
        module.addAbstractTypeMapping(IClient.class, ClientImpl.class);
        module.addAbstractTypeMapping(Client.class, ClientImpl.class);
        module.addAbstractTypeMapping(ISession.class, SessionImpl.class);
        module.addDeserializer(IPnPCharacter.class, new IPnPCharacterDeserializer(PnPCharacterImpl.class, PlayerCharacterImpl.class));
        this.mapper.registerModule(module);
    }

    public void connect(String ip, int port){
        try {
            socket = new Socket(ip, port);
            socket.setSoTimeout(2000);
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public BaseMessage sendMessage(BaseMessage message) throws IOException {
        out.println(mapper.writeValueAsString(message));
        return receiveMessage();
    }

    public BaseMessage receiveMessage() throws IOException {
        return mapper.readValue(in.readLine(), BaseMessage.class);
    }

    public String sendMessageWithRawResponse(BaseMessage message) throws IOException {
        out.println(mapper.writeValueAsString(message));
        return in.readLine();
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public IPlayerCharacter getCharacter() {
        return character;
    }

    public void setCharacter(IPlayerCharacter character) {
        this.character = character;
    }

    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }
}
