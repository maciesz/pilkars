package main.gala.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.*;
import android.os.AsyncTask;
import android.util.Log;
import main.gala.activities.WiFiActivity;
import main.gala.common.StaticContent;
import main.gala.enums.MultiMode;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

/**
 * Klasa do obsługi zdarzeń związanych z wifi.
 *
 * @author Maciej Andrearczyk <maciej.andrearczyk@student.mimuw.edu.pl>
 */
public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private WiFiActivity wiFiActivity;

    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel,
                                       WiFiActivity activity) {
        super();
        this.mManager = manager;
        this.mChannel = channel;
        this.wiFiActivity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        Log.d(WiFiDirectBroadcastReceiver.class.getCanonicalName(), "ON RECEIVE - " + action);
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Check to see if Wi-Fi is enabled and notify appropriate activity
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            if (mManager != null) {
                mManager.requestPeers(mChannel, new WifiP2pManager.PeerListListener() {
                    @Override
                    public void onPeersAvailable(WifiP2pDeviceList peers) {
                        for (WifiP2pDevice device : peers.getDeviceList()) {
                            Log.d(WiFiDirectBroadcastReceiver.class.getCanonicalName(), "peer - " + device.deviceName);
                            wiFiActivity.addPeer(device);
                        }
                    }
                });
            }
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            Log.d(this.getClass().getCanonicalName(), "Connection changed");
            if (mManager == null) {
                return;
            }

            NetworkInfo networkInfo = (NetworkInfo) intent
                    .getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            if (networkInfo.isConnected()) {

                // we are connected with the other device, request connection
                // info to find group owner IP

                mManager.requestConnectionInfo(mChannel, new WifiP2pManager.ConnectionInfoListener() {
                    @Override
                    public void onConnectionInfoAvailable(WifiP2pInfo info) {
                        Log.d(WiFiDirectBroadcastReceiver.class.getCanonicalName(), "Group owner address - " + info.groupOwnerAddress);

                        // InetAddress from WifiP2pInfo struct.
                        String groupOwnerAddress = info.groupOwnerAddress.getHostAddress();

                        Log.d(WiFiDirectBroadcastReceiver.class.getCanonicalName(), "Group owner address INET - " + groupOwnerAddress);
                        // After the group negotiation, we can determine the group owner.
                        if (info.groupFormed && info.isGroupOwner) {
                            // Do whatever tasks are specific to the group owner.
                            // One common case is creating a server thread and accepting
                            // incoming connections.
                            Log.d(WiFiDirectBroadcastReceiver.class.getCanonicalName(), "I AM GROUP OWNER - " + groupOwnerAddress);
                            new ConnectServerAsyncTask().execute();
                        } else if (info.groupFormed) {
                            // The other device acts as the client. In this case,
                            // you'll want to create a client thread that connects to the group
                            // owner.
                            Log.d(WiFiDirectBroadcastReceiver.class.getCanonicalName(), "I AM NOT GROUP OWNER - " + groupOwnerAddress);
                            new ConnectClientAsync().execute(groupOwnerAddress);
                        }
                    }
                });

            } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
                // Respond to this device's wifi state changing
            }
        }
    }


    class ConnectClientAsync extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... params) {
            String host = params[0];
            int port = StaticContent.defaultPort;
            Socket socket = new Socket();

            try {

                socket.bind(null);
                Log.d(WiFiDirectBroadcastReceiver.class.getCanonicalName(), "Trying to connect to host - " + host + ":" + port);

                socket.connect((new InetSocketAddress(host, port)), 500);

                Log.d(WiFiDirectBroadcastReceiver.class.getCanonicalName(), "Successfully connected to host");

//                String sztryng = "WIADOMOŚĆ Z DUPY";
//                byte[] buf = sztryng.getBytes();
//                OutputStream outputStream = socket.getOutputStream();
//                outputStream.write(buf);
//                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (socket != null) {
                    if (socket.isConnected()) {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            //catch logic
                        }
                    }
                }
            }
            return "hehe";
        }

        protected void onPostExecute(String feed) {
            //Sukces wykonania, gramy!
            Log.d(this.getClass().getCanonicalName(), "Successfully connected to server!");
            wiFiActivity.startGame(MultiMode.SERVER);
        }
    }

    /**
     * Asynchronicznie zadanie tworzące połącznie z serwerem.
     */
    public class ConnectServerAsyncTask extends AsyncTask<String, Void, String> {

        ServerSocket serverSocket;
        Socket client = null;

        @Override
        protected String doInBackground(String... params) {
            Log.d(this.getClass().getCanonicalName(), "Waiting for client...");
            try {
                serverSocket = new ServerSocket(StaticContent.defaultPort);
                client = serverSocket.accept();

//                InputStream is = client.getInputStream();
//                byte[] buf = new byte[10000];
//                is.read(buf);
//                Log.d(this.getClass().getCanonicalName(), "received msg - " + new String(buf));

            } catch (IOException e) {
                e.printStackTrace();
            }
            assert client != null;
            return client.getInetAddress().getHostName();
        }

        @Override
        protected void onPostExecute(String result) {
            //Sukces wykonania, gramy!
            Log.d(this.getClass().getCanonicalName(), "Successfully connected to client!");
            wiFiActivity.startGame(MultiMode.CLIENT);
        }
    }
}