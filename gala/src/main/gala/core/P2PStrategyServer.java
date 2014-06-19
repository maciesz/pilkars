package main.gala.core;

import android.util.Log;
import main.gala.common.Direction;
import main.gala.utils.Converter;
import main.gala.wifi.ServerSockets;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

/**
 * Strategia działania na managera w przypadku gry jako serwer.
 *
 * @author Maciej Andrearczyk <maciej.andrearczyk@student.mimuw.edu.pl>
 */
public class P2PStrategyServer extends P2PStrategy {

    Socket client = ServerSockets.getInstance().getClientSocket();

    public P2PStrategyServer(AbstractManager manager) {
        super(manager);
    }

    @Override
    public void sendMyMoves(List<Direction> myMoves) {
        Log.d(this.getClass().getCanonicalName(), "Send my moves - " + myMoves);

        List<Direction> convertedList = new LinkedList<>();
        for (Direction direction : myMoves) {
            convertedList.add(Converter.cuseMVConversion(direction));
        }

        ObjectOutputStream oos = null;
        try {
            Log.d(this.getClass().getCanonicalName(), "Trying send to - " + client.getInetAddress());
            final OutputStream outputStream = client.getOutputStream();
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

    @Override
    public List<Direction> getOpponentMoves() {
        Log.d(this.getClass().getCanonicalName(), "Get opponent moves");
        ObjectInputStream ois;
        List<Direction> result = new LinkedList<>();

        try {
            ois = new ObjectInputStream(client.getInputStream());
            List<Direction> opponentMoves = (List<Direction>) ois.readObject();
            for (Direction direction : opponentMoves) {
                result.add(Converter.reverseXY(direction));
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
}
