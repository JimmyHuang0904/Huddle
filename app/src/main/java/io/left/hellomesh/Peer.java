package io.left.hellomesh;

/**
 * Created by Donney on 1/13/2018.
 */

public class Peer {
    private String name;
    // Connected or disconnected
    private boolean isConnected;
    private String groupName;

    public Peer(){
        isConnected = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getConnectedStatus() {
        return isConnected;
    }

    public void setConnectedStatus(boolean isConnected){
        this.isConnected = isConnected;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
