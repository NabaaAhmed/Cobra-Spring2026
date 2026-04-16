import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class RoomManager {

    HashMap<String, Room> rooms = new HashMap<>();
    Room currentRoom;

    public RoomManager() {
        loadRooms("rooms.txt");
    }

    private void loadRooms(String filename) {
        // 1. Get file text using FileManager
        String fileData = FileManager.load(filename);
        String[] lines = fileData.split("\n");

        ArrayList<String[]> rawData = new ArrayList<>();

        // 2. Create rooms (FIRST PASS)
        for (int i = 1; i < lines.length; i++) { // skip header

            String[] parts = splitCSVLine(lines[i]);

            String id = parts[0];
            String name = parts[1];
            String desc = parts[2];

            Room room = new Room(id, name, desc);
            rooms.put(id, room);

            rawData.add(parts);
        }

        // 3. Connect rooms (SECOND PASS)
        for (String[] parts : rawData) {

            Room room = rooms.get(parts[0]);

            for (int i = 3; i < parts.length; i++) {
                String targetId = parts[i].trim();

                if (rooms.containsKey(targetId)) {
                    room.connection(rooms.get(targetId));
                }
            }
        }

        // 4. Set starting room
        currentRoom = rooms.get("EZ-01");
    }

    // -------------------------
    // MOVE SYSTEM
    // -------------------------
    public void move(int index) {
        if (index >= 0 && index < currentRoom.connections.size()) {
            currentRoom = currentRoom.connections.get(index);
            showRoom();
        } else {
            System.out.println("You can't go there.");
        }
    }

    // -------------------------
    // DISPLAY ROOM INFO
    // -------------------------
    public void showRoom() {
        System.out.println("\n" + currentRoom.name);
        System.out.println(currentRoom.description);

        System.out.println("\nConnections:");

        for (int i = 0; i < currentRoom.connections.size(); i++) {
            System.out.println(i + ": " + currentRoom.connections.get(i).name);
        }
    }
    //Save and load current room
    public String getRoomId() {
        return currentRoom.id;
    }

    public void setRoom(String id) {
        if (rooms.containsKey(id)) {
            currentRoom = rooms.get(id);
        }
    }

    // CSV PARSER (IMPORTANT)
    private String[] splitCSVLine(String line) {
        ArrayList<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (char c : line.toCharArray()) {

            if (c == '"') {
                inQuotes = !inQuotes;
            }
            else if (c == ',' && !inQuotes) {
                result.add(current.toString());
                current = new StringBuilder();
            }
            else {
                current.append(c);
            }
        }

        result.add(current.toString());
        return result.toArray(new String[0]);
    }
}
