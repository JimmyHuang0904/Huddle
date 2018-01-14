package io.left.hellomesh;

/**
 * This class is for storing the data about the user of the device the app is running on
 */

public class UserData {
    private String name;
    private String group;
    private boolean isConnected;
    private boolean isGracefulDisconnect;

    public UserData(String name) {
        this.name = name;
        this.isConnected = false;
    }

    public String getName() {
        return this.name;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getGroup() {
        return this.group;
    }

    public void setConnected() {
        this.isConnected = true;
    }

    public boolean hasConnected() {
        return this.isConnected;
    }

    public boolean isGracefulDisconnect() {return  this.isGracefulDisconnect; }

    public void setGracefulDisconnect(boolean gracefulDisconnect){
        this.isGracefulDisconnect = gracefulDisconnect;
    }
}
