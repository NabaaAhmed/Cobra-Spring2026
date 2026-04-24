import java.util.ArrayList;
import java.util.HashMap;

public class RoomManager {
    private HashMap<String, Room> rooms;
    private Room currentRoom;

    public RoomManager() {
        rooms = new HashMap<>();
        loadRooms("rooms.txt");
        loadItems("item.txt");
    }

    private void loadRooms(String filename) {
        String fileData = FileManager.load(filename);
        String[] lines = fileData.split("\n");

        ArrayList<String[]> rawData = new ArrayList<>();

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) {
                continue;
            }

            String[] parts = splitCSVLine(line);
            if (parts.length < 4) {
                continue;
            }

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

                if (targetId.equals("0")) {
                    continue;
                }

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

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) {
                continue;
            }

            if (i == 0 && line.toLowerCase().startsWith("id,")) {
                continue;
            }

            String[] parts = splitItemLine(line);
            if (parts.length < 6) {
                continue;
            }

            String itemId = parts[0].trim();
            String itemName = parts[1].trim();
            String description = parts[2].trim();
            String roomField = parts[3].trim();
            String monsterField = parts[4].trim();
            boolean stackable = Boolean.parseBoolean(parts[5].trim());

            if (!monsterField.equals("0")) {
                continue;
            }

            if (roomField.equals("0")) {
                continue;
            }

            if (itemName.equalsIgnoreCase("Core Fragment") ||
                    itemName.equalsIgnoreCase("Final Jewel")) {
                continue;
            }

            String[] roomIds = roomField.split(";");
            for (String rawRoomId : roomIds) {
                String roomID = rawRoomId.trim();
                if (rooms.containsKey(roomID)) {
                    Item roomItem = createItem(itemId, itemName, description, stackable);
                    rooms.get(roomID).addItem(roomItem);
                }
            }
        }
    }

    private Item createItem(String itemId, String itemName, String description, boolean stackable) {
        if (itemName.equalsIgnoreCase("Potion") || itemName.equalsIgnoreCase("Monster potion")) {
            return new Potion(itemId, itemName, description, "0", stackable, 2);
        } else if (itemName.toLowerCase().contains("sword")) {
            return new Sword(itemId, itemName, description, "0", stackable, 2);
        } else {
            return new QuestItems(itemId, itemName, description, "0", stackable);
        }
    }

    public void move(int index) {
        if (index >= 0 && index < currentRoom.getConnections().size()) {
            currentRoom = currentRoom.getConnections().get(index);
        }
    }

    public String getRoomId() {
        return currentRoom.getRoomId();
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

    private String[] splitItemLine(String line) {
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

        if (result.size() > 6) {
            String itemId = result.get(0).trim();
            String itemName = result.get(1).trim();
            String roomField = result.get(result.size() - 3).trim();
            String monsterField = result.get(result.size() - 2).trim();
            String stackable = result.get(result.size() - 1).trim();

            StringBuilder description = new StringBuilder();
            for (int i = 2; i < result.size() - 3; i++) {
                if (i > 2) {
                    description.append(",");
                }
                description.append(result.get(i));
            }

            return new String[]{
                    itemId,
                    itemName,
                    description.toString().trim(),
                    roomField,
                    monsterField,
                    stackable
            };
        }

        return result.toArray(new String[0]);
    }
}