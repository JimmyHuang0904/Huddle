package io.left.hellomesh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import io.left.rightmesh.id.MeshID;

/**
 * This class is for storing and looking up users that the device knows about
 */

public class UserStore {
    private Map<MeshID, User> userMap;

    public UserStore() {
        userMap = new HashMap<>();
    }

    public void addUser(MeshID uuid) {
        userMap.put(uuid, new User());
    }

    public void removeUser(MeshID uuid) {
        userMap.remove(uuid);
    }

    public User getUser(MeshID uuid) {
        return userMap.get(uuid);
    }

    public User getUser(String uuid) {
        for (MeshID meshID : userMap.keySet()) {
            if (meshID.toString().equals(uuid)) {
                return this.getUser(meshID);
            }
        }
        throw new NoSuchElementException("No user found with uuid " + uuid);
    }

    public boolean containsUser(MeshID uuid) {
        return userMap.containsKey(uuid);
    }

    public Set<MeshID> getAllUuids() {
        return userMap.keySet();
    }

    public String[] getAllGroupNames() {
        Set<String> uniqueGroupNames = new HashSet<>();
        for (User user : userMap.values()) {
            uniqueGroupNames.add(user.getGroupName());
        }
        String[] namesArr = uniqueGroupNames.toArray(new String[uniqueGroupNames.size()]);
        Arrays.sort(namesArr);
        return namesArr;
    }

    public String[] getUserNamesInGroup(String groupName) {
        List<String> names = new ArrayList<>();
        for (User user : userMap.values()) {
            if (user.getGroupName().equals(groupName)) {
                names.add(user.getName());
            }
        }
        return names.toArray(new String[names.size()]);
    }
}
