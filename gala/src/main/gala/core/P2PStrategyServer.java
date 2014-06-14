package main.gala.core;

import main.gala.common.Direction;
import main.gala.wifi.ServerSockets;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Strategia działania na managera w przypadku gry jako serwer.
 *
 * @author Maciej Andrearczyk <maciej.andrearczyk@student.mimuw.edu.pl>
 */
public class P2PStrategyServer extends P2PStrategy {

    Socket client = ServerSockets.getInstance().getSocket();

    public P2PStrategyServer(AbstractManager manager) {
        super(manager);
    }

    @Override
    public void sendMyMoves(List<Direction> myMoves) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("type", "moves");
            jsonObject.put("numbers", myMoves.size());
            for (Direction direction : myMoves) {
                jsonObject.put("direction", direction);
            }

            try (OutputStreamWriter osw = new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8)) {
                osw.write(jsonObject.toString());
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Direction> getOpponentMoves() {
        return null;
    }
}
