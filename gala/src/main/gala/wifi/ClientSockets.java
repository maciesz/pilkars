package main.gala.wifi;

import java.io.IOException;
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
        try {
            socket.bind(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ClientSockets getInstance() {
        if (instance == null) {
            instance = new ClientSockets();
        }
        return instance;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) { this.socket = socket; }
}
