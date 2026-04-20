import java.util.ArrayList;
import java.util.HashMap;

public class RoomManager {

    HashMap<String, Room> rooms = new HashMap<>();
    Room currentRoom;

    public RoomManager() {
        loadRooms("rooms.txt");
        loadItems("item.txt");
    }

    private void loadRooms(String filename) {
        String fileData = FileManager.load(filename);
        String[] lines = fileData.split("\n");

        ArrayList<String[]> rawData = new ArrayList<>();

        for (int i = 1; i < lines.length; i++) {
            String[] parts = splitCSVLine(lines[i]);

            String id = parts[0].trim();
            String name = parts[1].trim();
            String desc = parts[2].trim();

            Room room = new Room(id, name, desc);
            rooms.put(id, room);

            rawData.add(parts);
        }

        for (String[] parts : rawData) {
            Room room = rooms.get(parts[0].trim());

            for (int i = 3; i < parts.length; i++) {
                String targetId = parts[i].trim();

                if (rooms.containsKey(targetId)) {
                    room.connection(rooms.get(targetId));
                }
            }
        }

        currentRoom = rooms.get("EZ-01");
    }

    private void loadItems(String filename) {
        String fileData = FileManager.load(filename);
        String[] lines = fileData.split("\n");

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) continue;

            String[] parts = splitCSVLine(line);
            if (parts.length < 5) continue;

            String itemId = parts[0].trim();
            String itemName = parts[1].trim();
            String description = parts[2].trim();
            String roomID = parts[3].trim();
            boolean stackable = Boolean.parseBoolean(parts[4].trim());

            Item item;
            if (itemName.equalsIgnoreCase("Potion")) {
                item = new Potion(itemId, itemName, description, roomID, stackable, 2);
            } else if (itemName.toLowerCase().contains("sword")) {
                item = new Sword(itemId, itemName, description, roomID, stackable, 2);
            } else {
                item = new QuestItems(itemId, itemName, description, roomID, stackable);
            }

            if (rooms.containsKey(roomID)) {
                rooms.get(roomID).addItem(item);
            }
        }
    }

    public void move(int index) {
        if (index >= 0 && index < currentRoom.connections.size()) {
            currentRoom = currentRoom.connections.get(index);
            showRoom();
        } else {
            System.out.println("You can't go there.");
        }
    }

    public void showRoom() {
        System.out.println("\n" + currentRoom.name);
        System.out.println(currentRoom.description);

        System.out.println("\nConnections:");
        for (int i = 0; i < currentRoom.connections.size(); i++) {
            System.out.println(i + ": " + currentRoom.connections.get(i).name);
        }
    }

    public String getRoomId() {
        return currentRoom.id;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setRoom(String id) {
        if (rooms.containsKey(id)) {
            currentRoom = rooms.get(id);
        }
    }

    private String[] splitCSVLine(String line) {
        ArrayList<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }

        result.add(current.toString());
        return result.toArray(new String[0]);
    }
}