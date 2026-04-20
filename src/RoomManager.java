import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

            String roomId = parts[0].trim();
            Room room = rooms.get(roomId);

            if (room == null) continue;

            int optionNumber = 1;

            for (int i = 3; i < parts.length; i++) {
                String targetId = parts[i].trim();

                if (targetId.isEmpty()) continue;

                if (rooms.containsKey(targetId)) {
                    room.addExit("Option " + optionNumber, targetId);
                    optionNumber++;
                }
            }
        }

        // 4. Set starting room
        currentRoom = rooms.get("EZ-01");
    }

    // MOVE SYSTEM
    public void move(int index) {
        if (currentRoom == null) {
            System.out.println("There is no current room set.");
            return;
        }

        List<String> directions = new ArrayList<>(currentRoom.getExits().keySet());

        if (index >= 0 && index < directions.size()) {
            String dir = directions.get(index);
            String destId = currentRoom.getExits().get(dir);

            if (destId != null && rooms.containsKey(destId)) {
                currentRoom = rooms.get(destId);
                showRoom();
            } else {
                System.out.println("Destination room not found: " + destId);
            }
        } else {
            System.out.println("You can't go there.");
        }
    }

    // DISPLAY ROOM INFO
    public void showRoom() {
        if (currentRoom == null) {
            System.out.println("No room to show.");
            return;
        }

        System.out.println("\n" + currentRoom.getRoomName());
        System.out.println(currentRoom.getRoomDesc());

        System.out.println("\nConnections:");

        List<String> directions = new ArrayList<>(currentRoom.getExits().keySet());

        for (int i = 0; i < directions.size(); i++) {
            String direction = directions.get(i);
            String destId = currentRoom.getExits().get(direction);
            Room destRoom = rooms.get(destId);
            String destName = destRoom != null ? destRoom.getRoomName() : destId;
            System.out.println(i + ": " + direction + " -> " + destName);
        }
    }
    //Save and load current room
    public String getRoomId() {
        return currentRoom.getRoomID();
    }

    public void setRoom(String id) {
        if (id == null) return;
        if (rooms.containsKey(id)) {
            currentRoom = rooms.get(id);
        }
    }

    // CSV PARSER
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
