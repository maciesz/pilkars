package main.gala.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.*;
import main.gala.broadcast.WiFiDirectBroadcastReceiver;
import main.gala.common.GameSettings;
import main.gala.enums.GameMode;
import main.gala.enums.MultiMode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Aktywność odpowiadająca za zarządzanie wifi.
 *
 * @author Maciej Andrearczyk <maciej.andrearczyk@student.mimuw.edu.pl>
 */
public class WiFiActivity extends Activity {

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private BroadcastReceiver mReceiver;
    private IntentFilter mIntentFilter;
    private List<String> peerNames; //lista nazw peerow
    private ListView listView;

    List<WifiP2pDevice> peers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
        getActionBar().hide();

        //wifi
        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, Looper.getMainLooper(), null);
        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(WiFiDirectBroadcastReceiver.class.getCanonicalName(), "discovered peers");
            }

            @Override
            public void onFailure(int reasonCode) {
                Log.d(WiFiDirectBroadcastReceiver.class.getCanonicalName(), "discovering peers failed - " + reasonCode);
            }
        });

        //lista peerow
        peers = new LinkedList<>();
        peerNames = new ArrayList<>();

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(WiFiDirectBroadcastReceiver.class.getCanonicalName(), "clicked on item | position - " + position);
                connectToPeer(position);
            }
        });

    }

    private void connectToPeer(final int position) {
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = peers.get(position).deviceAddress;
        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                Log.d(WiFiDirectBroadcastReceiver.class.getCanonicalName(), "successfully started connecting to " + peerNames.get(position));
            }

            @Override
            public void onFailure(int reason) {
                Log.d(WiFiDirectBroadcastReceiver.class.getCanonicalName(), "fail starting connecting to " + peerNames.get(position));
                WiFiActivity.this.finish();
            }
        });
    }

    /**
     * Budzi aktywność odpowiedzialną za grę.
     */
    public void startGame(MultiMode multiMode) {
        Intent intent = new Intent(this, BoardActivity.class);
        intent.putExtra(GameSettings.GAME_MODE, GameMode.WiFiP2P.name());
        intent.putExtra(GameSettings.MULTI_MODE, multiMode.name());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    /**
     * Dodaje odkrytego peera do list view.
     *
     * @param peer peer
     */
    public void addPeer(WifiP2pDevice peer) {
        peers.clear();
        peerNames.clear();
        peers.add(peer);

        peerNames.add(String.valueOf(peerNames.size() + 1) + ". " + peer.deviceName);
        listView.setAdapter(new ArrayAdapter<>(this, R.layout.row_layout, peerNames));
    }


}
