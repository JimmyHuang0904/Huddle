package io.left.hellomesh;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.Build;
import android.content.Intent;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import io.left.rightmesh.android.AndroidMeshManager;
import io.left.rightmesh.android.MeshService;
import io.left.rightmesh.id.MeshID;
import io.left.rightmesh.mesh.MeshManager;
import io.left.rightmesh.mesh.MeshStateListener;
import io.left.rightmesh.util.MeshUtility;
import io.left.rightmesh.util.RightMeshException;
import io.reactivex.functions.Consumer;

import static io.left.rightmesh.mesh.MeshManager.DATA_RECEIVED;
import static io.left.rightmesh.mesh.MeshManager.PEER_CHANGED;
import static io.left.rightmesh.mesh.MeshManager.REMOVED;

/**
 * Main activity of the app. Should only be on the user's screen when the user has created a group
 * Or is currently in a group
 */
public class MainActivity extends FragmentActivity implements MeshStateListener {
    // Port to bind app to.
    private static final int HELLO_PORT = 9090;

    // MeshManager instance - interface to the mesh network.
    AndroidMeshManager mm = null;

    // Object to send messages over the mesh. Initialized in onCreate
    MessageSender messageSender = null;

    // Object to parse and handle message coming over the mesh. Initialized in onCreate
    MessageHandler messageHandler = null;

    // Keep track of users connected to the mesh
    PeerStore peerStore = null;

    // Keep track of data related to the device's user
    UserData userData = null;

    // Screen fragments
    MyGroupFragment myGroupFragment;
    ConnectedHandleFragment connectedHandleFragment;

    private String getUsername() {
        // Intent from first activity
        //TextView txtStatus = (TextView) findViewById(R.id.txtStatus);

        // Intent intent = getIntent();
        return getIntent().getStringExtra("username");
    }

    /**
     * Called when app first opens, initializes {@link AndroidMeshManager} reference (which will
     * start the {@link MeshService} if it isn't already running.
     *
     * @param savedInstanceState passed from operating system
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userData = new UserData(this.getUsername());

        String groupName = getIntent().getExtras().getString("group_name");
        if (groupName != null && !groupName.equals("")) {
            userData.setGroup(getIntent().getExtras().getString("group_name"));
            Toast.makeText(this, "GROUP ADD SUCCESSFUL", Toast.LENGTH_SHORT).show();
        } else {
            showUsersList();
        }

        setContentView(R.layout.activity_idlinglist);
        myGroupFragment = new MyGroupFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.idlinglist, myGroupFragment).commit();
        //setContentView(R.layout.activity_main);

        mm = AndroidMeshManager.getInstance(MainActivity.this, MainActivity.this);
        messageSender = new MessageSender(mm, HELLO_PORT);
        peerStore = new PeerStore();
        messageHandler = new MessageHandler(peerStore);

/*        String welcomeMsg = "Hello " + getUsername();
        TextView txtStatus = (TextView) findViewById(R.id.username);
        txtStatus.setText(welcomeMsg);*/
    }

    /**
     * Called when activity is on screen.
     */
    @Override
    protected void onResume() {
        try {
            super.onResume();
            mm.resume();
        } catch (MeshService.ServiceDisconnectedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when the app is being closed (not just navigated away from). Shuts down
     * the {@link AndroidMeshManager} instance.
     */
    @Override
    protected void onDestroy() {
        try {
            super.onDestroy();
            mm.stop();
        } catch (MeshService.ServiceDisconnectedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called by the {@link MeshService} when the mesh state changes. Initializes mesh connection
     * on first call.
     *
     * @param uuid our own user id on first detecting
     * @param state state which indicates SUCCESS or an error code
     */
    @Override
    public void meshStateChanged(MeshID uuid, int state) {
        if (state == MeshStateListener.SUCCESS) {
            try {
                // Binds this app to MESH_PORT.
                // This app will now receive all events generated on that port.
                mm.bind(HELLO_PORT);

                // Subscribes handlers to receive events from the mesh.
                mm.on(DATA_RECEIVED, new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        handleDataReceived((MeshManager.RightMeshEvent) o);
                    }
                });
                mm.on(PEER_CHANGED, new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        handlePeerChanged((MeshManager.RightMeshEvent) o);
                    }
                });

                // If you are using Java 8 or a lambda backport like RetroLambda, you can use
                // a more concise syntax, like the following:
                // mm.on(PEER_CHANGED, this::handlePeerChanged);
                // mm.on(DATA_RECEIVED, this::dataReceived);

                // Enable buttons now that mesh is connected.
                Button btnConfigure = (Button) findViewById(R.id.btnConfigure);
                Button btnSend = (Button) findViewById(R.id.btnHello);
                btnConfigure.setEnabled(true);
                btnSend.setEnabled(true);

                // initialized, so say that the user is now connected
                userData.setConnected();
            } catch (RightMeshException e) {
                String status = "Error initializing the library" + e.toString();
                Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
                TextView txtStatus = (TextView) findViewById(R.id.txtStatus);
                txtStatus.setText(status);
                return;
            }
        }

        // Update display on successful calls (i.e. not FAILURE or DISABLED).
        if (state == MeshStateListener.SUCCESS || state == MeshStateListener.RESUME) {
            updateStatus();
        }
    }

    /**
     * Update the {@link TextView} with a list of all peers.
     */
    private void updateStatus() {
        String status = "uuid: " + mm.getUuid().toString() + "\npeers:\n";
        for (MeshID peer : peerStore.getAllUuids()) {
            status += peer.toString() + "\n";
        }
        TextView txtStatus = (TextView) findViewById(R.id.txtStatus);
        txtStatus.setText(status);
    }

    /**
     * Handles incoming data events from the mesh - toasts the contents of the data.
     *
     * @param e event object from mesh
     */
    private void handleDataReceived(MeshManager.RightMeshEvent e) {
        final MeshManager.DataReceivedEvent event = (MeshManager.DataReceivedEvent) e;
        messageHandler.handleMessage(new String(event.data));

        // TODO: remove the toasts once we dont need them
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Toast data contents.
                String message = new String(event.data);
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

                // Play a notification.
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(MainActivity.this, notification);
                r.play();
            }
        });
    }

    /**
     * Handles peer update events from the mesh - maintains a list of peers and updates the display.
     *
     * @param e event object from mesh
     */
    private void handlePeerChanged(MeshManager.RightMeshEvent e) {
        // Update peer list.
        MeshManager.PeerChangedEvent event = (MeshManager.PeerChangedEvent) e;
        if (event.state != REMOVED && !peerStore.containsPeer(event.peerUuid)) {
            peerStore.addPeer(event.peerUuid);
            try {
                // tell the new person your name
                messageSender.sendName(event.peerUuid, this.userData.getName());

                // if you're in a group, tell the new person you're in that group
                boolean isInGroup = this.userData.getGroup() != null;
                if (isInGroup) {
                    messageSender.sendGroupToIndividual(event.peerUuid, this.userData.getGroup());
                }
            }catch (RightMeshException exception) {
                exception.printStackTrace();
            }

        } else if (event.state == REMOVED){
            peerStore.removePeer(event.peerUuid);
            // SOMEBODY DISCONNECTED OH NO!!!!
            // if theyre part of your group, then you should be alarmed
            if (userData.getGroup() != null && peerStore.getPeer(event.peerUuid).getGroupName().equals(userData.getGroup())) {
                startAlarm(userData.getName());
            }
        }

        // Update display.
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateStatus();
            }
        });
    }

    /**
     * Sends "hello" to all known peers.
     *
     * @param v calling view
     */
    public void sendHello(View v) throws RightMeshException {
        String ownName = userData.getName() != null ? userData.getName() : mm.getUuid().toString();
        for(MeshID receiver : peerStore.getAllUuids()) {
            String theirName = peerStore.getPeer(receiver).getName() != null ? peerStore.getPeer(receiver).getName(): receiver.toString();
            String msg = String.format("Hello to: %s from %s", theirName, ownName);

            MeshUtility.Log(this.getClass().getCanonicalName(), "MSG: " + msg);
            byte[] testData = msg.getBytes();
            mm.sendDataReliable(receiver, HELLO_PORT, testData);
        }
    }

    /**
     * Open mesh settings screen.
     *
     * @param v calling view
     */
    public void configure(View v)
    {
        try {
            mm.showSettingsActivity();
        } catch(RightMeshException ex) {
            MeshUtility.Log(this.getClass().getCanonicalName(), "Service not connected");
        }
    }

    public void onUserDisconnect(View view){
        startAlarm("asdf");
    }

    private void startAlarm(String userDisconnected) {
        // Remove yourself from the group
        // Mark yourself as disconnected
        // Throw up an error message
        // If the error is acknowledged, return user back to the main menu

        // Alarm sound
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        final Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);

        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(userDisconnected +" have been disconnected. Others will be notified shortly.");
        // Alert dialog button
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        r.stop();
                        dialog.dismiss();// use dismiss to cancel alert dialog

                        // Remove yourself from the group and return to main screen
                    }
                });
        alertDialog.show();
        r.play();

        shakeItBaby();
    }

    // Vibrate for 1000 milliseconds
    private void shakeItBaby() {
        int milliSeconds = 1000;
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(milliSeconds, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(milliSeconds);
        }
    }

    public void onChooseGroup(String groupName) throws RightMeshException {
        this.userData.setGroup(groupName);
        messageSender.sendGroupToMany(peerStore.getAllUuids(), groupName);
    }

    public void showUsersList(){
        Intent intent = new Intent(this, UsersListActivity.class);
        startActivity(intent);
    }
}

