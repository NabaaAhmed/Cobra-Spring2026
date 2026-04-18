import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Room {
    private final String roomId;
    private final String roomName;
    private final String roomDesc;
    private final ArrayList<String> connections;

    public Room(String roomId, String roomName, String roomDesc) {
        this(roomId, roomName, roomDesc, new ArrayList<>());
    }

    public Room(String roomId, String roomName, String roomDesc, List<String> connections) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.roomDesc = roomDesc;
        this.connections = new ArrayList<>(connections);
    }

    public String getRoomId() {
        return roomId;
    }

    public String getRoomName(String itemName) {
        return roomName;
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
        if (roomId == null || roomId.trim().isEmpty()) {
            return;
        }
        String trimmed = roomId.trim();
        if (!connections.contains(trimmed)) {
            connections.add(trimmed);
        }
    }

    public boolean hasConnection(String roomId) {
        if (roomId == null) {
            return false;
        }
        return connections.contains(roomId.trim());
    }

    @Override
    public String toString() {
        return roomName + ": " + roomDesc;
    }
}
