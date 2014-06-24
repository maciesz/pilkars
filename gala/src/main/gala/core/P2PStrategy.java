package main.gala.core;

import android.util.Log;
import main.gala.common.Direction;
import main.gala.enums.MultiMode;
import main.gala.utils.Converter;
import main.gala.wifi.BoardSizeWifiMessage;
import main.gala.wifi.ClientSockets;
import main.gala.wifi.LostConnectionWifiMessage;
import main.gala.wifi.ServerSockets;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

/**
 * Abstrakcyjna strategia działania dla managera P2P.
 *
 * @author Maciej Andrearczyk <maciej.andrearczyk@student.mimuw.edu.pl>
 */
public abstract class P2PStrategy {

    private final Socket socket;
    protected AbstractManager manager;

    public P2PStrategy(AbstractManager manager, MultiMode multiMode){
        this.manager = manager;
        if (multiMode == MultiMode.CLIENT) {
            this.socket = ClientSockets.getInstance().getSocket();
        } else {
            this.socket = ServerSockets.getInstance().getClientSocket();
        }
    }

    public void sendMyMoves(List<Direction> myMoves) {
        Log.d(this.getClass().getCanonicalName(), "Send my moves - " + myMoves);

        List<Direction> convertedList = new LinkedList<>();
        for (Direction direction : myMoves) {
            convertedList.add(Converter.cuseMVConversion(direction));
        }

        ObjectOutputStream oos = null;
        try {
            Log.d(this.getClass().getCanonicalName(), "Trying send to - " + socket.getInetAddress());
            final OutputStream outputStream = socket.getOutputStream();
            oos = new ObjectOutputStream(outputStream);
            oos.writeObject(convertedList);
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

    public List<Direction> getOpponentMoves() {
        Log.d(this.getClass().getCanonicalName(), "Get opponent moves");
        ObjectInputStream ois;
        List<Direction> result = new LinkedList<>();

        try {
            ois = new ObjectInputStream(socket.getInputStream());
            Object object = ois.readObject();
            if (object instanceof BoardSizeWifiMessage) {
                //TODO
            } else if (object instanceof LostConnectionWifiMessage) {
                Log.d(this.getClass().getCanonicalName(), "RECEIVED LOST_CONNECTION_WIFI_MESSAGE");
                manager.finishGame();
            } else {
                List<Direction> opponentMoves = (List<Direction>) object;
                for (Direction direction : opponentMoves) {
                    result.add(Converter.reverseXY(direction));
                }
            }
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    public void sendParameters(int width, int height, int goalWidth) {
        Socket mySocket = ClientSockets.getInstance().getSocket();
        ObjectOutputStream oos = null;
        try {
            Log.d(this.getClass().getCanonicalName(), "Trying send to - " + mySocket.getInetAddress());
            final OutputStream outputStream = mySocket.getOutputStream();

            BoardSizeWifiMessage message = new BoardSizeWifiMessage(width, height, goalWidth);
            oos = new ObjectOutputStream(outputStream);
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendLostConnectionMessage() {
        Log.d(this.getClass().getCanonicalName(), "send lost connection message");

        Socket mySocket = ClientSockets.getInstance().getSocket();
        ObjectOutputStream oos = null;
        try {
            Log.d(this.getClass().getCanonicalName(), "Trying send to - " + mySocket.getInetAddress());
            final OutputStream outputStream = mySocket.getOutputStream();

            LostConnectionWifiMessage message = new LostConnectionWifiMessage();
            oos = new ObjectOutputStream(outputStream);
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
