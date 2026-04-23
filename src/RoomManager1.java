//Mai
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class RoomManager1 {

    private Map<String, Room> rooms = new HashMap<>();
    private Map<String, Item> items = new HashMap<>();
    private Map<String, Monster> monsters = new HashMap<>();
    private Map<String, Puzzle> puzzles = new HashMap<>();
    private Room currentRoom;


    // initialize maps
    public RoomManager() {
        loadRooms();
        loadItems();
        loadMonsters();
        loadPuzzles();
        this.currentRoom = rooms.get("EZ-01");
    }

    //loading data
    private void loadRooms() {
        // String id, String name, String description, ArrayList exits/connections

        //loads rooms.txt data into data variable
        String data = FileManager.loadGame("rooms.txt");
        try {
            Scanner fileScanner = new Scanner((data));
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) continue;

                // Parse room data: roomNumber,name,description,exits
                String[] parts = line.split(",", 4);
                String roomId =parts[0].trim();
                String name = parts[1].trim();
                String description = parts[2].trim();

                // Create room object
                Room room = new Room(roomId, name, description);

                // Parse exits: roomID
                if (parts.length > 3) {
                    String[] exits = parts[3].split(",");
                    for (String exit : exits) {
                        String[] exitParts = exit.split(";");
                        String direction = exitParts[0].trim().toUpperCase();
                        String nextRoom = exitParts[1].trim();
                        room.addExit( nextRoom);
                    }
                }
                rooms.put(roomId, room);
            }
            fileScanner.close();
            System.out.println("Rooms loaded successfully!");
        } catch (FileNotFoundException e) {
            System.out.println("Error: Room file not found!");
        }
    }

    // ---------------- ITEMS ----------------
    private void loadItems() {
        //String id,String name,String description,String roomID,String monsterID, boolean stackable
        String data = FileManager.loadGame("items.txt");
        try{
            Scanner fileScanner = new Scanner(new File(data));
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) continue;

                // Parse item data: itemId,name,description,roomNumber
                String[] parts = line.split(",", 4);
                String itemId = parts[0].trim();
                String name = parts[1].trim();
                String description = parts[2].trim();
                int roomNumber = Integer.parseInt(parts[3].trim());

                // Create item and add to map
                Item item = new Item(itemId, name, description, roomNumber);
                items.put(itemId, item);

                // Add item to room
                Room room = rooms.get(roomNumber);
                if (room != null) {
                    room.addItem(item);
                }
            }
            fileScanner.close();
            System.out.println("Items loaded successfully!");
        } catch (FileNotFoundException e) {
            System.out.println("Error: Item file not found!");
        }
    }
}

// ---------------- MONSTERS ----------------
private void loadMonsters() {
    // String monsterID,String name,int HP,int attackValue

    String data = FileManager.loadGame("monsters.txt");
    try {
        Scanner fileScanner = new Scanner(new File(data));
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine().trim();
            if (line.isEmpty()) continue;

            // Parse item data: itemId,name,description,roomNumber
            String[] parts = line.split(",", 4);
            String itemId = parts[0].trim();
            String name = parts[1].trim();
            String description = parts[2].trim();
            int roomNumber = Integer.parseInt(parts[3].trim());

            // Create item and add to map
            Item item = new Item(itemId, name, description, roomNumber);
            items.put(itemId, item);

            // Add item to room
            Room room = rooms.get(roomNumber);
            if (room != null) {
                room.addItem(item);
            }
        }
        fileScanner.close();
        System.out.println("Items loaded successfully!");
    } catch (FileNotFoundException e) {
        System.out.println("Error: Item file not found!");
    }
}

// ---------------- PUZZLES ----------------
private void loadPuzzles() {
    //String puzzleID, String trialName, String roomID, String description, String hint, boolean isSolved
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

// ---------------- GET CURRENT ROOM ----------------
public Room getCurrentRoom() {
    return currentRoom;
}
}