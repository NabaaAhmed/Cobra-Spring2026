import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class RoomManager {

    HashMap<String, Room> rooms = new HashMap<>();
    Room currentRoom;
    private Map<String, Monster> monsterData;

    // Puzzle states
    private Puzzle1_Awareness puzzle1;
    private Puzzle2_Restraint puzzle2;
    private boolean puzzle1Active;
    private boolean puzzle2Active;
    private boolean puzzle3Active;
    private boolean puzzle4Active;
    private boolean puzzle5Active;
    private boolean puzzle6Active;
    private boolean puzzle7Active;

    public RoomManager() {
        FileManager fileManager = new FileManager();
        monsterData = fileManager.loadMonsters("monster.txt");

        // Initialize puzzles
        puzzle1 = new Puzzle1_Awareness("AW-02");
        puzzle2 = new Puzzle2_Restraint("RS-02");
        puzzle1Active = true;
        puzzle2Active = true;
        puzzle3Active = true;
        puzzle4Active = true;
        puzzle5Active = true;
        puzzle6Active = true;
        puzzle7Active = true;

        loadRoomsFromFile();
    }

    private void loadRoomsFromFile() {
        try {
            File file = new File("rooms.txt");
            Scanner scanner = new Scanner(file);
            boolean firstLine = true;

            // First pass: store exit data
            Map<String, String[]> roomExitsData = new HashMap<>();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (firstLine || line.isEmpty()) {
                    firstLine = false;
                    continue;
                }

                String[] parts = line.split(",", 4);
                if (parts.length >= 3) {
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    String description = parts[2].trim().replace("\"", "");
                    String exitsStr = parts.length > 3 ? parts[3].trim() : "";

                    Room room = new Room(id, name, description);
                    rooms.put(id, room);

                    if (!exitsStr.isEmpty() && !exitsStr.equals("0")) {
                        roomExitsData.put(id, exitsStr.split(","));
                    }
                }
            }
            scanner.close();

            // Second pass: add connections
            for (Map.Entry<String, String[]> entry : roomExitsData.entrySet()) {
                String roomId = entry.getKey();
                Room room = rooms.get(roomId);
                String[] exitIds = entry.getValue();

                if (room != null) {
                    for (String exitId : exitIds) {
                        exitId = exitId.trim();
                        if (!exitId.isEmpty() && !exitId.equals("0")) {
                            Room connectedRoom = rooms.get(exitId);
                            if (connectedRoom != null) {
                                room.addConnection(connectedRoom);
                            }
                        }
                    }
                }
            }

            // Set current room to EZ-01
            currentRoom = rooms.get("EZ-01");
            if (currentRoom == null && !rooms.isEmpty()) {
                currentRoom = rooms.values().iterator().next();
            }

            System.out.println("Loaded " + rooms.size() + " rooms from rooms.txt");

        } catch (FileNotFoundException e) {
            System.out.println("rooms.txt not found! Using default rooms.");
            loadDefaultRooms();
        }
    }

    private void loadDefaultRooms() {
        // Fallback rooms if rooms.txt is missing
        Room ez01 = new Room("EZ-01", "Main Hall",
                "Large cracked hall. 5 teleporters in arc, each connected by a glowing line to a jewel embedded in the far wall.");
        Room aw02 = new Room("AW-02", "Awareness Trial Room",
                "Circular room with an unstable teleporter in the center. A glowing red gem sits on the floor. Rubble is scattered nearby.");
        Room rs02 = new Room("RS-02", "Restraint Trial Chamber",
                "Circular room with a golden chest in the center. A dull coin lies on the floor nearby.");
        Room rs03 = new Room("RS-03", "Restraint Exit Corridor",
                "Plain corridor with a cracked wall and loose floor tiles.");
        Room tr02 = new Room("TR-02", "Guardian Chamber",
                "A guardian statue stands before you, pointing toward a pedestal.");
        Room tr03 = new Room("TR-03", "Trust Exit Hall",
                "Narrow hallway with a small alcove.");
        Room sc01 = new Room("SC-01", "Sacrifice Antechamber",
                "A valuable-looking sword with a faint glow rests on a pedestal.");
        Room sc02 = new Room("SC-02", "Long Bridge",
                "A windy stone bridge stretches before you.");
        Room sc03 = new Room("SC-03", "Wraith End",
                "The end of the bridge. Broken lanterns and carved pillars surround you.");
        Room cm01 = new Room("CM-01", "Commitment Entry",
                "Entry room with an inscription: 'Forward is safety.' A broken shelf sits against the wall.");
        Room cm07 = new Room("CM-07", "Commitment Exit",
                "A clean stone chamber. Safe at last.");
        Room trapRoom = new Room("TP-TRAP-01", "Warden Arena",
                "Circular arena with a broken teleporter frame, burned corpse, scattered crystals, and pillars. The Warden stands ready.");
        Room end01 = new Room("END-01", "Ascension Chamber",
                "A bright chamber. A glowing jewel floats above the center.");

        // Set up connections
        ez01.addConnection(aw02);
        ez01.addConnection(rs03);
        ez01.addConnection(tr03);
        ez01.addConnection(sc03);
        ez01.addConnection(cm07);
        ez01.addConnection(trapRoom);
        ez01.addConnection(end01);

        aw02.addConnection(ez01);
        rs02.addConnection(rs03);
        rs03.addConnection(rs02);
        rs03.addConnection(ez01);
        tr02.addConnection(tr03);
        tr03.addConnection(tr02);
        tr03.addConnection(ez01);
        sc01.addConnection(sc02);
        sc02.addConnection(sc01);
        sc02.addConnection(sc03);
        sc03.addConnection(sc02);
        sc03.addConnection(ez01);
        cm01.addConnection(cm07);
        cm07.addConnection(cm01);
        cm07.addConnection(ez01);
        trapRoom.addConnection(ez01);
        end01.addConnection(ez01);

        rooms.put(ez01.getId(), ez01);
        rooms.put(aw02.getId(), aw02);
        rooms.put(rs02.getId(), rs02);
        rooms.put(rs03.getId(), rs03);
        rooms.put(tr02.getId(), tr02);
        rooms.put(tr03.getId(), tr03);
        rooms.put(sc01.getId(), sc01);
        rooms.put(sc02.getId(), sc02);
        rooms.put(sc03.getId(), sc03);
        rooms.put(cm01.getId(), cm01);
        rooms.put(cm07.getId(), cm07);
        rooms.put(trapRoom.getId(), trapRoom);
        rooms.put(end01.getId(), end01);

        currentRoom = ez01;
        System.out.println("Created " + rooms.size() + " default rooms.");
    }

    public void addPuzzleItemsToRooms() {
        // Add Puzzle 1 items to AW-02
        Room aw02 = rooms.get("AW-02");
        if (aw02 != null && puzzle1Active) {
            aw02.addItem(puzzle1.getGlowingRedGem());
            aw02.addItem(puzzle1.getRubble());
            aw02.setPuzzle(new Puzzle("PZ-01", "Awareness", "AW-02",
                    "You see an unstable teleporter, a glowing red gem, and some rubble.",
                    "throw gem", "Try throwing the glowing gem at the teleporter"));
        }

        // Add Puzzle 2 items to RS-02
        Room rs02 = rooms.get("RS-02");
        if (rs02 != null && puzzle2Active) {
            rs02.addItem(puzzle2.getBaitCoin());
            rs02.setPuzzle(new Puzzle("PZ-02", "Restraint", "RS-02",
                    "A golden chest sits in the center with a bait coin nearby.",
                    "ignore coin", "Don't pick up the coin before opening the chest"));
        }

        // Add Guardian to TR-02
        Room tr02 = rooms.get("TR-02");
        if (tr02 != null) {
            Monster guardian = new Monster("M-02", "Guardian", 2, 1, false);
            guardian.addReward(new Potion("I-13", "Silver Sigil", "A valuable silver sigil.", false, 0));
            tr02.setMonster(guardian);
        }
    }

    public void move(int index) {
        if (index >= 0 && index < currentRoom.getConnections().size()) {
            currentRoom = currentRoom.getConnections().get(index);
            showRoom();
        } else {
            System.out.println("Invalid move.");
        }
    }

    public void moveToRoom(String roomName) {
        for (Room room : currentRoom.getConnections()) {
            if (room.getName().equalsIgnoreCase(roomName)) {
                currentRoom = room;
                showRoom();
                return;
            }
        }
        System.out.println("Cannot move to '" + roomName + "' from here.");
    }

    public Room findRoomByName(String name) {
        for (Room room : rooms.values()) {
            if (room.getName().equalsIgnoreCase(name)) {
                return room;
            }
        }
        return null;
    }

    public void showRoom() {
        System.out.println("\n" + currentRoom.getName());
        System.out.println(currentRoom.getDescription());

        if (currentRoom.getMonster() != null && currentRoom.getMonster().isAlive()) {
            System.out.println("MONSTER HERE: " + currentRoom.getMonster().getName());
            System.out.println("   (Type 'attack' to fight)");
        }

        if (currentRoom.getItems() != null && !currentRoom.getItems().isEmpty()) {
            System.out.println("\nItems here:");
            for (Item item : currentRoom.getItems()) {
                System.out.println("  - " + item.getName());
            }
        }

        if (currentRoom.getPuzzle() != null && !currentRoom.getPuzzle().isSolved()) {
            System.out.println("\nPUZZLE HERE: " + currentRoom.getPuzzle().getTrialName() + " Trial");
            System.out.println("   (Type 'explore " + currentRoom.getPuzzle().getTrialName().toLowerCase() + "' to begin)");
        }

        System.out.println("\nExits:");
        for (int i = 0; i < currentRoom.getConnections().size(); i++) {
            System.out.println(i + ": " + currentRoom.getConnections().get(i).getName());
        }
        System.out.println();
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public String getRoomId() {
        return currentRoom != null ? currentRoom.getId() : null;
    }

    // Puzzle getters
    public Puzzle1_Awareness getPuzzle1() { return puzzle1; }
    public Puzzle2_Restraint getPuzzle2() { return puzzle2; }

    public boolean isPuzzle1Active() { return puzzle1Active; }
    public boolean isPuzzle2Active() { return puzzle2Active; }
    public boolean isPuzzle3Active() { return puzzle3Active; }
    public boolean isPuzzle4Active() { return puzzle4Active; }
    public boolean isPuzzle5Active() { return puzzle5Active; }
    public boolean isPuzzle6Active() { return puzzle6Active; }
    public boolean isPuzzle7Active() { return puzzle7Active; }

    public void setPuzzle1Active(boolean active) { this.puzzle1Active = active; }
    public void setPuzzle2Active(boolean active) { this.puzzle2Active = active; }
    public void setPuzzle3Active(boolean active) { this.puzzle3Active = active; }
    public void setPuzzle4Active(boolean active) { this.puzzle4Active = active; }
    public void setPuzzle5Active(boolean active) { this.puzzle5Active = active; }
    public void setPuzzle6Active(boolean active) { this.puzzle6Active = active; }
    public void setPuzzle7Active(boolean active) { this.puzzle7Active = active; }

    // Method to spawn Mimic in the Restraint room (called when player triggers it)
    public void spawnMimicInRestraintRoom() {
        Room rs02 = rooms.get("RS-02");
        if (rs02 != null) {
            Monster mimic = new Monster("M-01", "Mimic", 2, 1, true);
            rs02.setMonster(mimic);
        }
    }

    // Method to spawn Warden in Trap Room (called when player fails Awareness trial)
    public void spawnWardenInTrapRoom() {
        Room trapRoom = rooms.get("TP-TRAP-01");
        if (trapRoom != null) {
            Monster warden = new Monster("M-07", "Warden", 2, 1, false);
            warden.addReward(new Potion("I-07", "Potion", "A small vial of restorative red liquid.", true, 5));
            trapRoom.setMonster(warden);
        }
    }
}