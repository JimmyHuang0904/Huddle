package io.left.hellomesh;

import android.util.Log;

/**
 * This class is for taking in the contents of messages received over the mesh network, and determining how to handle it
 */

public class MessageHandler {

    public MessageHandler() {
        // TODO: pass dependencies for setting user fields here
    }

    public void handleMessage(String messageContent) throws IllegalArgumentException{
        Log.d("handler", messageContent);
        String[] segments = messageContent.split(":");
        if (segments.length < 2 || segments.length > 3) {
            throw new IllegalArgumentException("Wrong number of : separated segments in message");
        }
        String verb = segments[0];
        String targetUser = segments[1];
        if (verb.equals("NAME")) {
            String userName = segments[2];
            // TODO: users[targetUser].name = userName;
        }else if (verb.equals("GROUP")) {
            String groupName = segments[2];
            // TODO: users[targerUser].group = groupName;
        }else if (verb.equals("UNGROUP")) {
            // TODO: users[targetUser].group = null;
        }
    }
}
