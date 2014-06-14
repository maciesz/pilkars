package main.gala.wifi;

import java.net.Socket;

/**
 * Singleton przechowujący gniazdo klienta potrzebne do komunikacji.
 *
 * @author Maciej Andrearczyk <maciej.andrearczyk@student.mimuw.edu.pl>
 */
public class ClientSockets {

    private static ClientSockets instance = null;

    private Socket socket;

    private ClientSockets() {
        socket = new Socket();
    }

    public static ClientSockets getInstance() {
        if (instance == null) {
            instance = new ClientSockets();
        }
        return instance;
    }

    protected Socket getSocket() {
        return socket;
    }
}
