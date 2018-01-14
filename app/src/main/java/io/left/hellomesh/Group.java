package io.left.hellomesh;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Donney on 1/13/2018.
 */

public class Group {

    private List<User> users;
    private int numUsers;
    private String groupName;

    public Group(String groupName) {
        users = new ArrayList<>();
        numUsers = 0;
        this.groupName = groupName;
    }

    public void addUser(User user) {
        users.add(user);
        numUsers++;
    }

    public void removeUser(User user) {
        if (this.containsUser(user)) {
            users.remove(user);
            numUsers--;
        }
    }

    public List<String> getAllUUIDs() {
        //return users.stream().map(user -> user.getUUID()).collect(Collectors.toList());
        ArrayList<String> UUIDs = new ArrayList<>();
        for (User user : users) {
            UUIDs.add(user.getUUID());
        }
        return UUIDs;
    }

    public boolean containsUser(User user) {
        return users.contains(user);
    }

    public boolean containsUser(String UUID) {
        for (User user : users){
            if (user.getUUID().equals(UUID)) return true;
        }
        return false;
    }

    public int getNumUsers() {
        return numUsers;
    }

    public String getGroupName(){
        return groupName;
    }
}
