import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class FileManager {

    // File paths (corrected to match actual file names)
    private final String ROOM_FILE = "rooms.txt";
    private final String ITEM_FILE = "item.txt";
    private final String PUZZLE_FILE = "puzzle.txt";
    private final String SAVE_FILE = "savegame.txt";
    private final String MONSTER_FILE = "monster.txt";

    private String dataPath;

    public FileManager(String dataPath) {
        this.dataPath = dataPath;
    }

    // --- 1. LOAD ROOMS ---
    public ArrayList<Room> loadRooms(Map<String, Item> itemsMap, Map<String, Monster> monstersMap) {
        ArrayList<Room> rooms = new ArrayList<>();
        try (Scanner reader = new Scanner(new File(dataPath + "\\" + ROOM_FILE))) {
            if (reader.hasNextLine()) reader.nextLine(); // Skip header
            while (reader.hasNextLine()) {
                String[] parts = reader.nextLine().split(",", 6);
                if (parts.length >= 3) {
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    String desc = parts[2].replace("\"", "").trim();
                    Room r = new Room(id, name, desc);

                    // Parse exits (assign indices as directions)
                    if (parts.length > 3 && !parts[3].trim().isEmpty()) {
                        String[] exitIds = parts[3].split(",");
                        for (int i = 0; i < exitIds.length; i++) {
                            String exitId = exitIds[i].trim();
                            if (!exitId.isEmpty()) {
                                r.addExit(String.valueOf(i), exitId);
                            }
                        }
                    }

                    // Parse items
                    if (parts.length > 4 && !parts[4].trim().isEmpty()) {
                        String[] itemIds = parts[4].split(",");
                        for (String itemId : itemIds) {
                            Item item = itemsMap.get(itemId.trim());
                            if (item != null) r.addItem(item);
                        }
                    }

                    // Parse monsters (assuming one per room)
                    if (parts.length > 5 && !parts[5].trim().isEmpty()) {
                        String[] monsterIds = parts[5].split(",");
                        for (String mid : monsterIds) {
                            Monster m = monstersMap.get(mid.trim());
                            if (m != null) r.setMonster(m);
                        }
                    }

                    rooms.add(r);
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading rooms: " + e.getMessage());
        }
        return rooms;
    }

    // --- 2. LOAD ITEMS ---
    public ArrayList<Item> loadItems() {
        ArrayList<Item> items = new ArrayList<>();
        try (Scanner reader = new Scanner(new File(dataPath + "\\" + ITEM_FILE))) {
            if (reader.hasNextLine()) reader.nextLine(); // Skip header
            while (reader.hasNextLine()) {
                String[] parts = reader.nextLine().split(",");
                if (parts.length == 4) {
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    String desc = parts[2].trim();
                    boolean stackable = Boolean.parseBoolean(parts[3].trim());
                    items.add(new Item(id, name, desc, stackable));
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading items: " + e.getMessage());
        }
        return items;
    }

    // --- 3. LOAD PUZZLES ---
    public ArrayList<Puzzle> loadPuzzles() {
        ArrayList<Puzzle> puzzles = new ArrayList<>();
        try (Scanner reader = new Scanner(new File(dataPath + "\\" + PUZZLE_FILE))) {
            if (reader.hasNextLine()) reader.nextLine(); // Skip header
            while (reader.hasNextLine()) {
                String[] parts = reader.nextLine().split(",");
                if (parts.length == 4) {
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    String roomId = parts[2].trim();
                    boolean isSolved = Boolean.parseBoolean(parts[3].trim());
                    puzzles.add(new Puzzle(id, name, roomId, isSolved));
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading puzzles: " + e.getMessage());
        }
        return puzzles;
    }

    // --- 4. LOAD MONSTERS ---
    public ArrayList<Monster> loadMonsters() {
        ArrayList<Monster> monsters = new ArrayList<>();
        try (Scanner reader = new Scanner(new File(dataPath + "\\" + MONSTER_FILE))) {
            if (reader.hasNextLine()) reader.nextLine(); // Skip header
            while (reader.hasNextLine()) {
                String[] parts = reader.nextLine().split(",", 5);
                if (parts.length == 5) {
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    int hp = Integer.parseInt(parts[2].trim());
                    int atk = Integer.parseInt(parts[3].trim());
                    String reward = parts[4].trim();

                    Monster m = new Monster(id, name, hp, atk);
                    if (!reward.equalsIgnoreCase("null")) {
                        m.addReward(reward);
                    }
                    monsters.add(m);
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading monsters: " + e.getMessage());
        }
        return monsters;
    }

    // --- 5. LOAD ALL DATA ---
    public void loadAllData(RoomManager rm) {
        // Load and add items first
        ArrayList<Item> items = loadItems();
        for (Item i : items) rm.addItem(i);

        // Load and add monsters
        ArrayList<Monster> monsters = loadMonsters();
        for (Monster m : monsters) rm.addMonster(m);

        // Load and add puzzles
        ArrayList<Puzzle> puzzles = loadPuzzles();
        for (Puzzle p : puzzles) rm.addPuzzle(p);

        // Load and add rooms (now with items/monsters/exits populated)
        ArrayList<Room> rooms = loadRooms(rm.getItems(), rm.getMonsters());
        for (Room r : rooms) rm.addRoom(r);
    }

    // --- 6. SAVE GAME STATE ---
    public void saveGame(String roomID, int hp, ArrayList<String> inventory, ArrayList<Puzzle> puzzles) {
        try {
            PrintWriter writer = new PrintWriter(dataPath + "\\" + SAVE_FILE);

            // Save player stats
            writer.println(roomID);
            writer.println(hp);

            // Save inventory
            String invString = String.join(",", inventory);
            writer.println(invString);

            // Save puzzle statuses
            for (Puzzle p : puzzles) {
                writer.println(p.getPuzzleID() + "," + p.isSolved());
            }

            writer.close();
            System.out.println("Game saved successfully!");
        } catch (Exception e) {
            System.out.println("Error saving game: " + e.getMessage());
        }
    }

    // --- 7. LOAD GAME STATE ---
    public void loadGame() {
        try {
            File file = new File(dataPath + "\\" + SAVE_FILE);
            Scanner reader = new Scanner(file);
            String savedRoom = reader.nextLine();
            int savedHP = Integer.parseInt(reader.nextLine());
            String invLine = reader.nextLine();
            String[] savedInventory = invLine.split(",");

            while (reader.hasNextLine()) {
                String puzzleLine = reader.nextLine();
                String[] puzzleParts = puzzleLine.split(",");
                String pID = puzzleParts[0];
                boolean isSolved = Boolean.parseBoolean(puzzleParts[1]);
            }

            reader.close();
            System.out.println("Welcome back! You are currently in " + savedRoom);
        } catch (Exception e) {
            System.out.println("No save file found.");
        }
    }
}