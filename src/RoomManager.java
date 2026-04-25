import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomManager {

    private Map<String, Room> rooms = new HashMap<>();
    private Map<String, Item> items = new HashMap<>();
    private Map<String, Monster> monsters = new HashMap<>();

    private Room currentRoom;

    private Puzzle1_Awareness puzzle1;
    private Puzzle2_Restraint puzzle2;
    private Puzzle3Trust puzzle3;
    private Puzzle4Sacrifice puzzle4;
    private Puzzle5Commitment puzzle5;
    private Puzzle6FinalTrial puzzle6;
    private Puzzle7_Trap puzzle7;

    private boolean allTrialsComplete = false;

    public RoomManager() {
        loadItems();
        loadMonsters();
        createPuzzles();
        loadRooms();
        assignPuzzlesToRooms();
    }

    private void createPuzzles() {
        puzzle1 = new Puzzle1_Awareness("AW-02");
        puzzle2 = new Puzzle2_Restraint("RS-02");
        puzzle3 = new Puzzle3Trust();
        puzzle4 = new Puzzle4Sacrifice();
        puzzle5 = new Puzzle5Commitment();
        puzzle6 = new Puzzle6FinalTrial();
        puzzle7 = new Puzzle7_Trap("TP-TRAP-01");
    }

    private void loadRooms() {
        String fileData = FileManager.loadGame("rooms.txt");
        String[] lines = fileData.split("\n");

        for (int i = 1; i < lines.length; i++) {
            String[] parts = splitCSVLine(lines[i]);
            if (parts.length < 3) continue;

            String id = parts[0].trim();
            String name = parts[1].trim();
            String desc = parts[2].trim();

            rooms.put(id, new Room(id, name, desc));
        }

        for (int i = 1; i < lines.length; i++) {
            String[] parts = splitCSVLine(lines[i]);
            Room room = rooms.get(parts[0].trim());
            if (room == null) continue;

            int option = 1;

            for (int j = 3; j < parts.length; j++) {
                String value = parts[j].trim();
                if (value.equals("") || value.equals("null")) continue;

                if (rooms.containsKey(value)) {
                    room.addExit("Option " + option, value);
                    option++;
                }
                else if (value.startsWith("I-")) {
                    Item item = items.get(value);
                    if (item != null) {
                        room.addItem(item);
                    }
                }
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

    private void loadItems() {
        String fileData = FileManager.loadGame("items.txt");
        String[] lines = fileData.split("\n");

        for (int i = 1; i < lines.length; i++) {
            String[] parts = splitCSVLine(lines[i]);
            if (parts.length < 4) continue;

            String id = parts[0].trim();
            String name = parts[1].trim();
            String desc = parts[2].trim();
            boolean stackable = false;
            if (parts.length > 3) {
                try {
                    stackable = Boolean.parseBoolean(parts[3].trim());
                } catch(Exception e) {
                    stackable = false;
                }
            }

            final String itemId = id;
            final String itemName = name;
            final String itemDesc = desc;
            final boolean isStackable = stackable;

            items.put(id, new Item(id, name, desc, stackable) {
                @Override
                public void use(Player player) {
                }
            });
        }
    }

    private void loadMonsters() {
        String fileData = FileManager.loadGame("monsters.txt");
        String[] lines = fileData.split("\n");

        for (int i = 1; i < lines.length; i++) {
            String[] parts = splitCSVLine(lines[i]);
            if (parts.length < 4) continue;

            String id = parts[0].trim();
            String name = parts[1].trim();
            int hp = 2;
            int atk = 1;
            try {
                hp = Integer.parseInt(parts[2].trim());
                atk = Integer.parseInt(parts[3].trim());
            } catch(Exception e) {}

            monsters.put(id, new Monster(id, name, hp, atk));
        }
    }

    private void assignPuzzlesToRooms() {
        Room aw02 = rooms.get("AW-02");
        if (aw02 != null) aw02.setPuzzle(puzzle1);

        Room rs02 = rooms.get("RS-02");
        if (rs02 != null) rs02.setPuzzle(puzzle2);

        Room tr02 = rooms.get("TR-02");
        if (tr02 != null) tr02.setPuzzle(puzzle3);

        Room sc01 = rooms.get("SC-01");
        if (sc01 != null) sc01.setPuzzle(puzzle4);

        Room cm07 = rooms.get("CM-07");
        if (cm07 != null) cm07.setPuzzle(puzzle5);

        Room fn02 = rooms.get("FN-02");
        if (fn02 != null) fn02.setPuzzle(puzzle6);

        Room trapRoom = rooms.get("TP-TRAP-01");
        if (trapRoom != null) trapRoom.setPuzzle(puzzle7);
    }

    public void showRoom() {
        if (currentRoom == null) {
            System.out.println("No room loaded.");
            return;
        }

        System.out.println("\n" + currentRoom.getRoomName());
        System.out.println(currentRoom.getRoomDesc());

        if (!currentRoom.getItems().isEmpty()) {
            System.out.println("\nItems:");
            for (Item item : currentRoom.getItems()) {
                System.out.println("- " + item.getItemName());
            }
        }

        if (currentRoom.hasMonster()) {
            System.out.println("\nMonster: " + currentRoom.getMonster().getName());
        }

        if (currentRoom.hasPuzzle() && !currentRoom.getPuzzle().isSolved()) {
            System.out.println("\nPuzzle: " + currentRoom.getPuzzle().getTrialName() + " Trial");
        }

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

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public String getRoomId() {
        return currentRoom != null ? currentRoom.getRoomID() : null;
    }

    public void moveToRoom(String roomId) {
        Room room = rooms.get(roomId);
        if (room != null) {
            currentRoom = room;
        }
    }

    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }

    public Room findRoomById(String roomId) {
        return rooms.get(roomId);
    }

    public void addPuzzleItemsToRooms() {
    }

    public Puzzle getPuzzle1() { return puzzle1; }
    public Puzzle getPuzzle2() { return puzzle2; }
    public Puzzle getPuzzle3() { return puzzle3; }
    public Puzzle getPuzzle4() { return puzzle4; }
    public Puzzle getPuzzle5() { return puzzle5; }
    public Puzzle getPuzzle6() { return puzzle6; }
    public Puzzle getPuzzle7() { return puzzle7; }

    public void unlockFinalTrial() {
        allTrialsComplete = true;
        Room ez01 = rooms.get("EZ-01");
        if (ez01 != null && ez01.getExit("FN-02") == null) {
            ez01.addExit("Final Trial Teleporter", "FN-02");
        }
        System.out.println("\n========================================");
        System.out.println("All 5 trials complete! The Final Trial is now unlocked!");
        System.out.println("========================================\n");
    }

    public boolean areAllTrialsComplete() {
        return allTrialsComplete;
    }
}