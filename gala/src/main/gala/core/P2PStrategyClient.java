package main.gala.core;

import android.util.Log;
import main.gala.common.Direction;
import main.gala.wifi.ClientSockets;
import main.gala.wifi.ServerSockets;

import java.io.*;
import java.net.Socket;
import java.util.List;

/**
 * Strategia działania na managera w przypadku gry jako klient.
 *
 * @author Maciej Andrearczyk <maciej.andrearczyk@student.mimuw.edu.pl>
 */
public class P2PStrategyClient extends P2PStrategy {

    Socket mySocket = ClientSockets.getInstance().getSocket();

    public P2PStrategyClient(AbstractManager manager) {
        super(manager);
    }

    @Override
    public void sendMyMoves(List<Direction> myMoves) {
        Log.d(this.getClass().getCanonicalName(), "Send my moves - " + myMoves);
        Socket client = ServerSockets.getInstance().getClientSocket();
        ObjectOutputStream oos = null;
        try {
            Log.d(this.getClass().getCanonicalName(), "Trying send to - " + client.getInetAddress());
            final OutputStream outputStream = client.getOutputStream();
            oos = new ObjectOutputStream(outputStream);
            oos.writeObject(myMoves);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//            try {
//                if (oos != null) {
//                    oos.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            } TODO
        }
    }

    @Override
    public List<Direction> getOpponentMoves() {
        Log.d(this.getClass().getCanonicalName(), "Get opponent moves");
        ObjectInputStream ois;
        List<Direction> opponentMoves = null;

        try {
            ois = new ObjectInputStream(mySocket.getInputStream());
            opponentMoves = (List<Direction>) ois.readObject();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return opponentMoves;
    }
}
