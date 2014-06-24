package main.gala.core;

import android.util.Log;
import main.gala.common.Direction;
import main.gala.enums.MultiMode;
import main.gala.utils.Converter;
import main.gala.wifi.BoardSizeWifiMessage;
import main.gala.wifi.LostConnectionWifiMessage;
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

    public P2PStrategyServer(AbstractManager manager, MultiMode multiMode) {
        super(manager, multiMode);
    }

}
