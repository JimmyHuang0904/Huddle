package io.left.hellomesh;

/**
 * Created by Donney on 1/13/2018.
 */

public class User {

    private String UUID;
    private String name;
    // Connected or disconnected
    private boolean isConnected;

    public User(String UUID, String name){
        this.UUID = UUID;
        this.name = name;
        isConnected = false;
    }

    public String getUUID() {
        return UUID;
    }

    public String getName() {
        return name;
    }

    public boolean getConnectedStatus() {
        return isConnected;
    }

    public void setConnectedStatus(boolean isConnected){
        this.isConnected = isConnected;
    }
}
