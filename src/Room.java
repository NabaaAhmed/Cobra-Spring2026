import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Room {
    private final String roomID;
    private final String roomName;
    private final String roomDesc;
    private final ArrayList<String> connections;
    private final ArrayList<Item> itemsInRoom;

    public Room(String roomID, String roomName, String roomDesc) {
        this.roomID = roomID;
        this.roomName = roomName;
        this.roomDesc = roomDesc;
        this.connections = new ArrayList<>();
        this.itemsInRoom = new ArrayList<>();
    }

    public ArrayList<Item> getItemsInRoom() {return itemsInRoom;}

    public String getRoomID() {
        return roomID;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getRoomDesc() {
        return roomDesc;
    }

    public List<String> getConnections() {
        return Collections.unmodifiableList(connections);
    }

    public void addConnection(String roomId) {
        if (roomId == null || roomId.isEmpty()) {
            return;
        }

        if (!connections.contains(roomId)) {
            connections.add(roomId);
        }
    }

    public boolean hasConnection(String roomId) {
        if (roomId == null) {
            return false;
        }
        return connections.contains(roomId);
    }

    public boolean isConnectedTo(String roomId) {
        return hasConnection(roomId);
    }
    



    @Override
    public String toString() {
        return roomName + ": " + roomDesc;
    }

    public void addItem(Item item) {
        if (item != null) {
            item.moveToRoom(roomID);
        }
    }

    public void addExit(String roomID) {
        connections.add(roomID);
    }

}