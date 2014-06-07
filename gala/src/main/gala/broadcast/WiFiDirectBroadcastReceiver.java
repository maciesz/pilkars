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

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

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
                        new SendAsync().execute(info.groupOwnerAddress.toString());
                    }
                });

            } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
                // Respond to this device's wifi state changing
            }
        }
    }

    class SendAsync extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... params) {
            String host = params[0];
            int port = StaticContent.defaultPort;
            Socket socket = new Socket();

            String sztryng = "WIADOMOŚĆ Z DUPY";
            byte[] buf = sztryng.getBytes();
            try {

                socket.bind(null);
                socket.connect((new InetSocketAddress(host, port)), 500);

                Log.d(WiFiDirectBroadcastReceiver.class.getCanonicalName(), "Group owner address connect success");

                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(buf);
                outputStream.close();
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
        }
    }
}