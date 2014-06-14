package main.gala.wifi;

import android.util.Log;
import main.gala.common.StaticContent;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Singleton przechowujący obiekty gniazd dla urządzenia pełniącego funkcję serwera (groupOwner).
 *
 * @author Maciej Andrearczyk <maciej.andrearczyk@student.uw.edu.pl>
 */
public class ServerSockets {

    private static ServerSockets instance = null;

    private ServerSocket serverSocket;
    private Socket clientSocket = null;

    private ServerSockets() {
        try {
            serverSocket = new ServerSocket(StaticContent.defaultPort);
        } catch (IOException e) {
            Log.e(this.getClass().getCanonicalName(), "Error while creating server socket");
            e.printStackTrace();
        }
    }

    public static ServerSockets getInstance() {
        if (instance == null) {
            instance = new ServerSockets();
        }
        return instance;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public Socket getSocket() {
        return clientSocket;
    }
}
