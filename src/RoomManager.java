import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomManager {

    private Map<String, Room> rooms = new HashMap<>();
    private Map<String, Item> items = new HashMap<>();
    private Map<String, Monster> monsters = new HashMap<>();
    private Map<String, Puzzle> puzzles = new HashMap<>();


    // -------- ROOM --------

    private Room currentRoom;

    public RoomManager() {
        loadItems();
        loadMonsters();
        loadPuzzles();
        loadRooms();
    }

    private void loadRooms() {

        String fileData = FileManager.loadGame("rooms.txt");
        String[] lines = fileData.split("\n");

        // STEP 1: create rooms
        for (int i = 1; i < lines.length; i++) {

            String[] parts = splitCSVLine(lines[i]);

            if (parts.length < 3) continue;

            String id = parts[0].trim();
            String name = parts[1].trim();
            String desc = parts[2].trim();

            rooms.put(id, new Room(id, name, desc));
        }

        // STEP 2: connect rooms + place items + monsters
        for (int i = 1; i < lines.length; i++) {

            String[] parts = splitCSVLine(lines[i]);

            Room room = rooms.get(parts[0].trim());
            if (room == null) continue;

            int option = 1;

            for (int j = 3; j < parts.length; j++) {

                String value = parts[j].trim();

                if (value.equals("") || value.equals("null")) continue;

                // ROOM CONNECTION
                if (rooms.containsKey(value)) {
                    room.addExit("Option " + option, value);
                    option++;
                }

                // ITEM
                else if (value.startsWith("I-")) {
                    Item item = items.get(value);
                    if (item != null) {
                        room.addItem(item);
                    }
                }

                // MONSTER
                else if (value.startsWith("M-")) {
                    Monster monster = monsters.get(value);
                    if (monster != null) {
                        room.setMonster(monster);
                    }
                }
            }
        }

        currentRoom = rooms.get("EZ-01");
    }

    // ---------------- ITEMS ----------------
    private void loadItems() {

        String fileData = FileManager.loadGame("items.txt");
        String[] lines = fileData.split("\n");

        for (int i = 1; i < lines.length; i++) {

            String[] parts = splitCSVLine(lines[i]);

            String id = parts[0];
            String name = parts[1];
            String desc = parts[2];
            boolean stackable = Boolean.parseBoolean(parts[3]);

            items.put(id, new Item(id, name, desc, stackable) {
                @Override
                public void use(Player player) {

                }
            });
        }
    }

    // ---------------- MONSTERS ----------------
    private void loadMonsters() {

        String fileData = FileManager.loadGame("monsters.txt");
        String[] lines = fileData.split("\n");

        for (int i = 1; i < lines.length; i++) {

            String[] parts = splitCSVLine(lines[i]);

            String id = parts[0].trim();
            String name = parts[1].trim();
            int hp = Integer.parseInt(parts[2].trim());
            int atk = Integer.parseInt(parts[3].trim());

            monsters.put(id, new Monster(id, name, hp, atk));
        }
    }

    // ---------------- PUZZLES ----------------
    private void loadPuzzles() {

        String fileData = FileManager.loadGame("puzzles.txt");
        String[] lines = fileData.split("\n");

        for (int i = 1; i < lines.length; i++) {

            String[] parts = splitCSVLine(lines[i]);

            String id = parts[0].trim();
            String name = parts[1].trim();
            String roomID = parts[2].trim();
            boolean solved = Boolean.parseBoolean(parts[3].trim());

            puzzles.put(id, new Puzzle(id, name, roomID, solved));
        }
    }

    // ---------------- ASSIGN PUZZLES ----------------
    private void assignPuzzlesToRooms() {

        for (Puzzle puzzle : puzzles.values()) {

            Room room = rooms.get(puzzle.getRoomID());

            if (room != null) {
                room.setPuzzle(puzzle);
            }
        }
    }

    // ---------------- SHOW ROOM ----------------
    public void showRoom() {

        if (currentRoom == null) {
            System.out.println("No room loaded.");
            return;
        }

        System.out.println("\n" + currentRoom.getRoomName());
        System.out.println(currentRoom.getRoomDesc());

        // ITEMS
        if (!currentRoom.getItems().isEmpty()) {
            System.out.println("\nItems:");
            for (Item item : currentRoom.getItems()) {
                System.out.println("- " + item.getitemName());
            }
        }

        // MONSTER
        if (currentRoom.hasMonster()) {
            System.out.println("\nMonster: " + currentRoom.getMonster().getName());
        }

        // PUZZLE
        if (currentRoom.hasPuzzle()) {
            System.out.println("\nPuzzle: " + currentRoom.getPuzzle().getTrialName());
        }

        // EXITS
        System.out.println("\nConnections:");

        List<String> exits = new ArrayList<>(currentRoom.getExits().keySet());

        for (int i = 0; i < exits.size(); i++) {

            String key = exits.get(i);
            String targetId = currentRoom.getExits().get(key);

            Room target = rooms.get(targetId);

            String name = (target != null) ? target.getRoomName() : targetId;

            System.out.println(i + ": " + name);
        }
    }

    // ---------------- MOVE ----------------
    public void move(int index) {

        List<String> exits = new ArrayList<>(currentRoom.getExits().keySet());

        if (index < 0 || index >= exits.size()) {
            System.out.println("Invalid move.");
            return;
        }

        String key = exits.get(index);
        String targetId = currentRoom.getExits().get(key);

        currentRoom = rooms.get(targetId);

        showRoom();
    }

    // ---------------- CSV PARSER ----------------
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

    // ---------------- GET CURRENT ROOM ----------------
    public Room getCurrentRoom() {
        return currentRoom;
    }
}