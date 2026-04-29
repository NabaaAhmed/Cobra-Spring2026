//danny
import java.util.ArrayList;
import java.util.HashMap;

public class RoomManager {
    private HashMap<String, Room> roomMap = new HashMap<>();
    private Room currentRoom;

    private HashMap<String, Monster> monsterTemplates = new HashMap<>();
    private HashMap<String, String> puzzleRoomMap = new HashMap<>();
    private HashMap<String, String> puzzleHintMap = new HashMap<>();
    private static HashMap<String, Item> pendingRewards = new HashMap<>();

    public RoomManager() {
        loadRooms("rooms.txt");
        loadItems("item.txt");
        loadMonsters("monster.txt");
        loadPuzzles("puzzle.txt");
        this.currentRoom = roomMap.get("EZ-01");
    }

    private void loadRooms(String filename) {
        String fileData = FileManager.load(filename);
        String[] lines = fileData.split("\n");

        ArrayList<String[]> rawData = new ArrayList<>();

        // First pass: create all rooms from rooms.txt.
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();

            if (line.isEmpty()) {
                continue;
            }

            if (line.startsWith("//")) {
                continue;
            }

            if (line.toLowerCase().startsWith("id,")) {
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
            roomMap.put(id, room);
            rawData.add(parts);
        }

        // Second pass: connect rooms after all rooms already exist.
        for (String[] parts : rawData) {
            String roomId = parts[0].trim();
            Room room = roomMap.get(roomId);

            if (room == null || parts.length < 4) {
                continue;
            }

            StringBuilder exitsBuilder = new StringBuilder();

            for (int i = 3; i < parts.length; i++) {
                if (parts[i] != null && !parts[i].trim().isEmpty()) {
                    if (exitsBuilder.length() > 0) {
                        exitsBuilder.append(";");
                    }
                    exitsBuilder.append(parts[i].trim());
                }
            }

            String exits = exitsBuilder.toString().trim();

            if (exits.equals("0") || exits.isEmpty()) {
                continue;
            }

            String[] exitIds = exits.split(";");

            for (String exitId : exitIds) {
                exitId = exitId.trim();

                if (exitId.isEmpty() || exitId.equals("0")) {
                    continue;
                }

                Room connectedRoom = roomMap.get(exitId);

                if (connectedRoom != null) {
                    room.connection(connectedRoom);
                }
            }
        }

        currentRoom = roomMap.get("EZ-01");
    }

    private void loadItems(String filename) {
        String fileData = FileManager.load(filename);
        String[] lines = fileData.split("\n");

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();

            if (line.isEmpty()) {
                continue;
            }

            if (line.startsWith("//")) {
                continue;
            }

            if (line.toLowerCase().startsWith("id,")) {
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

            // If an item has a monster ID, save it as a possible monster reward.
            if (!monsterField.equals("0")) {
                Item rewardItem = createItem(itemId, itemName, description, stackable);
                pendingRewards.put(monsterField.trim(), rewardItem);
            }

            // Do not place items that are not assigned to a room.
            if (roomField.equals("0")) {
                continue;
            }

            // These are revealed by Final Trial logic, not placed in the room at game start.
            if (itemName.equalsIgnoreCase("Core Fragment") ||
                    itemName.equalsIgnoreCase("Final Jewel")) {
                continue;
            }

            String[] roomIds = roomField.split(";");

            for (String rawRoomId : roomIds) {
                String roomID = rawRoomId.trim();

                if (roomMap.containsKey(roomID)) {
                    Item roomItem = createItem(itemId, itemName, description, stackable);
                    roomMap.get(roomID).addItem(roomItem);
                }
            }
        }
    }

    private void loadMonsters(String filename) {
        String fileData = FileManager.load(filename);
        String[] lines = fileData.split("\n");

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();

            if (line.isEmpty() || line.startsWith("//") || line.toLowerCase().startsWith("monsterid")) {
                continue;
            }

            String[] parts = line.split(",");

            if (parts.length < 4) {
                continue;
            }

            String monsterID = parts[0].trim();
            String name = parts[1].trim();
            int hp = Integer.parseInt(parts[2].trim());
            int atkValue = Integer.parseInt(parts[3].trim());

            Monster monster = new Monster(monsterID, name, hp, atkValue);

            if (pendingRewards.containsKey(monsterID)) {
                Item reward = pendingRewards.get(monsterID);
                monster.setRewardItemName(reward.getItemName());
            }

            monsterTemplates.put(monsterID, monster);
        }
    }

    private void loadPuzzles(String filename) {
        String fileData = FileManager.load(filename);
        String[] lines = fileData.split("\n");

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();

            if (line.isEmpty() || line.startsWith("//") || line.toLowerCase().startsWith("puzzleid")) {
                continue;
            }

            String[] parts = line.split(",", 5);

            if (parts.length < 3) {
                continue;
            }

            String puzzleID = parts[0].trim();
            String roomId = parts[2].trim();

            puzzleRoomMap.put(roomId, puzzleID);

            if (parts.length >= 5) {
                String hint = parts[4].trim();

                if (!hint.isEmpty()) {
                    puzzleHintMap.put(puzzleID, hint);
                }
            }
        }
    }

    private Item createItem(String itemId, String itemName, String description, boolean stackable) {
        if (itemName.equalsIgnoreCase("Potion") || itemName.equalsIgnoreCase("Monster potion")) {
            return new Potion(itemId, itemName, description, "0", stackable, 2);
        } else if (itemName.toLowerCase().contains("sword")) {
            return new Sword(itemId, itemName, description, "0", stackable, 1);
        } else {
            return new QuestItems(itemId, itemName, description, "0", stackable);
        }
    }

    public void move(int index) {
        if (currentRoom == null) {
            return;
        }

        if (index >= 0 && index < currentRoom.getConnections().size()) {
            currentRoom = currentRoom.getConnections().get(index);
        }
    }

    public String getRoomId() {
        if (currentRoom == null) {
            return "";
        }

        return currentRoom.getRoomId();
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setRoom(String id) {
        if (id == null) {
            return;
        }

        String cleanId = id.trim().toUpperCase();

        if (roomMap.containsKey(cleanId)) {
            currentRoom = roomMap.get(cleanId);
        }
    }

    public Room getRoomById(String id) {
        if (id == null) {
            return null;
        }

        return roomMap.get(id.trim().toUpperCase());
    }

    public boolean hasRoom(String id) {
        if (id == null) {
            return false;
        }

        return roomMap.containsKey(id.trim().toUpperCase());
    }

    public ArrayList<Room> getAllRooms() {
        return new ArrayList<>(roomMap.values());
    }

    public int getRoomCount() {
        return roomMap.size();
    }

    public HashMap<String, Monster> getMonsterTemplates() {
        return monsterTemplates;
    }

    public HashMap<String, String> getPuzzleRoomMap() {
        return puzzleRoomMap;
    }

    public HashMap<String, String> getPuzzleHintMap() {
        return puzzleHintMap;
    }

    private String[] splitCSVLine(String line) {
        ArrayList<String> result = new ArrayList<>();
        StringBuilder currentCell = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(currentCell.toString().trim());
                currentCell = new StringBuilder();
            } else {
                currentCell.append(c);
            }
        }

        result.add(currentCell.toString().trim());
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

            return new String[] {
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