import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class GameControllerMaiTesting {
    private Scanner scanner;
    private Map<String, Item> items;
    private Map<Integer, Room> rooms;
    private Player player;

    public GameControllerMaiTesting() {
        this.scanner = new Scanner(System.in);
        this.items = new HashMap<>();
        this.rooms = new HashMap<>();
    }

    public void loadItems(String filename) {
        try {
            Scanner fileScanner = new Scanner(new File(filename));
            boolean firstLine = true;
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                if (line.isEmpty()) continue;

                String[] parts = line.split(",", 5);
                if (parts.length >= 5) {
                    String itemId = parts[0].trim();
                    String itemName = parts[1].trim();
                    String description = parts[2].trim();
                    String roomID = parts[3].trim();
                    boolean stackable = Boolean.parseBoolean(parts[4].trim());

                    // Create a concrete item instead of abstract Item
                    Potion item = new Potion(itemId, itemName, description, stackable, 0);
                    items.put(itemId, item);
                }
            }
            fileScanner.close();
            System.out.println("Items loaded successfully!");
        } catch (FileNotFoundException e) {
            System.out.println("Error: Item file not found!");
        }
    }

    /**
     * Main game loop - process commands and manage gameplay
     */
    public void play() {

        System.out.println("     /\\      /\\      /\\      /\\      /\\      /\\      /\\\n" +
                                        "    /  \\    /  \\    /  \\    /  \\    /  \\    /  \\    /  \\\n" +
                                       "    \\  /    \\  /    \\  /    \\  /    \\  /    \\  /    \\  /\n" +
                                       "     \\/      \\/      \\/      \\/      \\/      \\/      \\/" );


        System.out.println("==============WELCOME TO THE DUNGEON OF TRIALS==============");
        System.out.println("The Dungeon of Trials was constructed by a king to find a worthy successor.\nThe dungeon tests character across five themed trials. The player starts in " +
                "\nthe Entrance Zone, where five teleporters (each connected to a wall jewel by \na glowing line) provide access to each trial in any order. Each time a trial is \n" +
                "completed, its teleporter deactivates and its line to the jewel darkens. Once \nall five trials are complete, the ground beneath the teleporters crumbles to \n" +
                "reveal a sixth teleporter leading to the Final Trial, and the wall jewel becomes \nremovable, opening a hidden room. Completing the Final Trial and claiming the \n" +
                "Catalyst wins the game. A detached Teleport Trap Room serves as a penalty zone \nreachable via incorrect puzzle actions or entering an unstabilized Final Trial teleporter.");
        System.out.println("Good Luck!");
    }
}