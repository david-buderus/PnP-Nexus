package de.pnp.manager.testHelper;

import java.io.*;
import java.net.Socket;

public class TestClient {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public TestClient() {
        this.socket = null;
        this.in = new BufferedReader(BufferedReader.nullReader());
        this.out = new PrintWriter(OutputStream.nullOutputStream());
    }

    public void connect(String ip, int port){
        try {
            socket = new Socket(ip, port);
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

    public String sendMessage(String message) {
        out.println(message);
        try {
            return in.readLine();
        } catch (IOException e) {
            return "";
        }
    }
}
