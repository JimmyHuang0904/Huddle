package io.left.hellomesh;

import android.util.Log;

/**
 * This class is for taking in the contents of messages received over the mesh network, and determining how to handle it
 */

public class MessageHandler {
    private UserStore userStore = null;

    public MessageHandler(UserStore userStore) {
        this.userStore = userStore;
    }

    public void handleMessage(String messageContent) throws IllegalArgumentException{
        String[] segments = messageContent.split(":");
        if (segments.length < 2 || segments.length > 3) {
            throw new IllegalArgumentException("Wrong number of : separated segments in message");
        }

        String verb = segments[0];
        String targetUser = segments[1];

        if (verb.equals("NAME")) {
            String userName = segments[2];
            userStore.getUser(targetUser).setName(userName);
        }else if (verb.equals("GROUP")) {
            String groupName = segments[2];
            userStore.getUser(targetUser).setGroupName(groupName);
        }else if (verb.equals("UNGROUP")) {
            userStore.getUser(targetUser).setGroupName(null);
        }
    }
}
