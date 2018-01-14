package io.left.hellomesh;

/**
 * This class is for taking in the contents of messages received over the mesh network, and determining how to handle it
 */

public class MessageHandler {
    private PeerStore peerStore = null;

    public MessageHandler(PeerStore peerStore) {
        this.peerStore = peerStore;
    }

    public void handleMessage(String messageContent) throws IllegalArgumentException{
        String[] segments = messageContent.split(":");
        if (segments.length < 2 || segments.length > 3) {
            throw new IllegalArgumentException("Wrong number of : separated segments in message");
        }

        String verb = segments[0];
        String targetPeer = segments[1];

        if (verb.equals("NAME")) {
            String peerName = segments[2];
            peerStore.getPeer(targetPeer).setName(peerName);
        }else if (verb.equals("GROUP")) {
            String groupName = segments[2];
            peerStore.getPeer(targetPeer).setGroupName(groupName);
        }else if (verb.equals("UNGROUP")) {
            peerStore.getPeer(targetPeer).setGroupName(null);
        }
    }
}
