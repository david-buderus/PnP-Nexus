package de.pnp.manager.testHelper;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.serializer.ServerModule;

import java.io.*;
import java.net.Socket;

public class TestClient {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private final ObjectMapper mapper;

    public TestClient() {
        this.socket = null;
        this.in = new BufferedReader(BufferedReader.nullReader());
        this.out = new PrintWriter(OutputStream.nullOutputStream());
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new ServerModule());
    }

    public void connect(String ip, int port){
        try {
            socket = new Socket(ip, port);
            socket.setSoTimeout(2000);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
        return mapper.readValue(in.readLine(), BaseMessage.class);
    }

    public String sendMessageWithRawResponse(BaseMessage message) throws IOException {
        out.println(mapper.writeValueAsString(message));
        return in.readLine();
    }
}
