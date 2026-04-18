import java.util.ArrayList;

public class Room {
    String id;
    String name;
    String description;
    ArrayList<Room> connections;

    public Room(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.connections = new ArrayList<>();
    }

    public void connection(Room room) {
        if (room != null && !connections.contains(room)) {
            connections.add(room);
        }
    }

    public String getRoomId() {
        return id;
    }

    public String getRoomName() {
        return name;
    }

    public String getRoomDesc() {
        return description;
    }

    public ArrayList<Room> getConnections() {
        return connections;
    }
}
