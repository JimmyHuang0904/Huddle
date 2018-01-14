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
 * This class is for storing and looking up peers that the device knows about
 */

public class PeerStore {
    private Map<MeshID, Peer> peerMap;

    public PeerStore() {
        peerMap = new HashMap<>();
    }

    public void addPeer(MeshID uuid) {
        peerMap.put(uuid, new Peer());
    }

    public void removePeer(MeshID uuid) {
        peerMap.remove(uuid);
    }

    public Peer getPeer(MeshID uuid) {
        return peerMap.get(uuid);
    }

    public Peer getPeer(String uuid) {
        for (MeshID meshID : peerMap.keySet()) {
            if (meshID.toString().equals(uuid)) {
                return this.getPeer(meshID);
            }
        }
        throw new NoSuchElementException("No peer found with uuid " + uuid);
    }

    public boolean containsPeer(MeshID uuid) {
        return peerMap.containsKey(uuid);
    }

    public Set<MeshID> getAllUuids() {
        return peerMap.keySet();
    }

    public String[] getAllGroupNames() {
        Set<String> uniqueGroupNames = new HashSet<>();
        for (Peer peer : peerMap.values()) {
            uniqueGroupNames.add(peer.getGroupName());
        }
        String[] namesArr = uniqueGroupNames.toArray(new String[uniqueGroupNames.size()]);
        Arrays.sort(namesArr);
        return namesArr;
    }

    public String[] getPeerNamesInGroup(String groupName) {
        List<String> names = new ArrayList<>();
        for (Peer peer : peerMap.values()) {
            if (peer.getGroupName().equals(groupName)) {
                names.add(peer.getName());
            }
        }
        return names.toArray(new String[names.size()]);
    }
}
