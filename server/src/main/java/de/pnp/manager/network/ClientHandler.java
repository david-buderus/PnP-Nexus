package de.pnp.manager.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.message.MessageType;
import de.pnp.manager.network.serializer.ServerModule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Calendar;

public class ClientHandler extends Thread {

    protected Socket clientSocket;
    protected PrintWriter out;
    protected BufferedReader in;
    protected ObjectMapper mapper;
    protected Calendar calendar;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new ServerModule());
        this.calendar = Calendar.getInstance();
    }

    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

            String input;
            while ((input = in.readLine()) != null) {
                handleMessage(mapper.readValue(input, BaseMessage.class));
            }

            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void write(BaseMessage message) {
        try {
            out.println(mapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    protected void handleMessage(BaseMessage message) {
        switch (message.type) {

            case createSession:
                write(new BaseMessage(MessageType.sessionCreated, calendar.getTime()));
                break;
        }
    }
}
