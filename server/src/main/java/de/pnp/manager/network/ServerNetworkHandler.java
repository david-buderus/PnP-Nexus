package de.pnp.manager.network;

import de.pnp.manager.main.Utility;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerNetworkHandler {

    protected ServerSocket serverSocket;
    protected Thread serverThread;
    protected boolean active;

    public ServerNetworkHandler() {
        this.active = false;
    }

    public void stop() {
        try {
            this.active = false;
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {

        try {

            /*SSLContext sslContext = SSLContext.getInstance("TLS", new BouncyCastleJsseProvider(new BouncyCastleFipsProvider()));
            KeyManagerFactory keyMgrFact = KeyManagerFactory.getInstance("PKIX", new BouncyCastleJsseProvider(new BouncyCastleFipsProvider()));
            keyMgrFact.init(null, "password".toCharArray());
            sslContext.init(keyMgrFact.getKeyManagers(), null, null);

            SSLServerSocketFactory fact = sslContext.getServerSocketFactory();
            serverSocket = (SSLServerSocket) fact.createServerSocket(Utility.getConfig().getInt("server.port"));*/

            serverSocket = new ServerSocket(Utility.getConfig().getInt("server.port"));
            active = true;

            serverThread = new Thread(() -> {
                while (active) {
                    try {
                        new ClientHandler(serverSocket.accept()).start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            serverThread.start();

        } catch (IOException /* | UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException | NoSuchProviderException | KeyManagementException*/ e) {
            e.printStackTrace();
        }
    }
}
