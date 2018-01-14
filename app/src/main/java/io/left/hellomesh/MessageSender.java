package io.left.hellomesh;

import java.util.Set;

import io.left.rightmesh.android.AndroidMeshManager;
import io.left.rightmesh.id.MeshID;
import io.left.rightmesh.util.MeshUtility;
import io.left.rightmesh.util.RightMeshException;

/**
 * Created by Thomas on 2018-01-13.
 */

public class MessageSender {
    private AndroidMeshManager mm = null;
    private int port = -1;

    public MessageSender(AndroidMeshManager meshManager, int port) {
        this.mm = meshManager;
        this.port = port;
    }
    public void sendName(MeshID receiver, String name) throws RightMeshException {
        String msg = String.format("NAME:%s:%s", mm.getUuid(), name);
        this.sendMessageToIndividual(msg, receiver);
    }

    public void sendGroupToIndividual(MeshID receiver, String groupName) throws RightMeshException {
        String msg = String.format("GROUP:%s:%s", mm.getUuid(), groupName);
        this.sendMessageToIndividual(msg, receiver);
    }

    public void sendGroupToMany(Set<MeshID> users, String groupName) throws RightMeshException {
        for(MeshID receiver : users) {
            sendGroupToIndividual(receiver, groupName);
        }
    }

    private void sendMessageToIndividual(String message, MeshID receiver) throws RightMeshException {
        MeshUtility.Log(this.getClass().getCanonicalName(), "MSG: " + message);
        byte[] testData = message.getBytes();
        mm.sendDataReliable(receiver, port, testData);
    }
}
