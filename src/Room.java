import java.util.ArrayList;
import java.util.Collection;
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

    public boolean isConnectedTo(String roomId) {
        return hasConnection(roomId);
    }

    public Item findItemInRoom(Collection<Item> items, String itemName) {
        if (items == null || itemName == null) {
            return null;
        }

        for (Item item : items) {
            if (item != null && item.isInRoom(roomId) && item.matchesName(itemName)) {
                return item;
            }
        }
        return null;
    }

    public String getExploreText(Collection<Item> items) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n=== ").append(roomName).append(" ===\n");
        builder.append(roomDesc).append("\n");
        builder.append("Connections: ").append(connections).append("\n");
        builder.append("Items in room:\n");

        boolean foundAny = false;
        if (items != null) {
            for (Item item : items) {
                if (item != null && item.isInRoom(roomId)) {
                    builder.append("  - ").append(item.getItemName()).append("\n");
                    foundAny = true;
                }
            }
        }

        if (!foundAny) {
            builder.append("  - None\n");
        }

        return builder.toString();
    }

    @Override
    public String toString() {
        return roomName + ": " + roomDesc;
    }
}
