import java.io.*;
import java.util.*;

public class FileManager {

    public Map<String, Monster> loadMonsters(String filename) {
        Map<String, Monster> monsters = new HashMap<>();

        try {
            File file = new File(filename);
            if (!file.exists()) {
                System.out.println("Monster file not found. Using defaults.");
                return getDefaultMonsters();
            }

            Scanner scanner = new Scanner(file);
            boolean firstLine = true;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (firstLine || line.isEmpty()) {
                    firstLine = false;
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    int hp = Integer.parseInt(parts[2].trim());
                    int atk = Integer.parseInt(parts[3].trim());
                    boolean ambush = name.equalsIgnoreCase("Mimic");

                    Monster monster = new Monster(id, name, hp, atk, ambush);

                    // Add rewards based on monster type
                    if (name.equalsIgnoreCase("Guardian")) {
                        Item reward = new Potion("I-13", "Silver Sigil", "A valuable silver sigil.", false, 0);
                        monster.addReward(reward);
                    } else if (name.equalsIgnoreCase("Golem")) {
                        Item reward = new Potion("I-14", "Gold Chain", "A valuable gold chain.", false, 0);
                        monster.addReward(reward);
                    } else if (name.equalsIgnoreCase("Watcher")) {
                        Item reward = new Potion("I-21", "Watcher Corpse", "The corpse of the Watcher.", false, 0);
                        monster.addReward(reward);
                    } else if (name.equalsIgnoreCase("Final Trial Stalker")) {
                        Item reward = new Potion("I-20", "Stalker Corpse", "The corpse of the Stalker.", false, 0);
                        monster.addReward(reward);
                    } else if (name.equalsIgnoreCase("Warden")) {
                        Item reward = new Potion("I-07", "Potion", "A small vial of restorative red liquid.", true, 5);
                        monster.addReward(reward);
                    }

                    monsters.put(id, monster);
                }
            }
            scanner.close();
            System.out.println("Loaded " + monsters.size() + " monsters.");

        } catch (Exception e) {
            System.out.println("Error loading monsters: " + e.getMessage());
            return getDefaultMonsters();
        }

        return monsters;
    }

    private Map<String, Monster> getDefaultMonsters() {
        Map<String, Monster> monsters = new HashMap<>();

        // Create monsters with rewards
        Monster mimic = new Monster("M-01", "Mimic", 2, 1, true);
        Monster guardian = new Monster("M-02", "Guardian", 2, 1, false);
        guardian.addReward(new Potion("I-13", "Silver Sigil", "A valuable silver sigil.", false, 0));

        Monster golem = new Monster("M-03", "Golem", 2, 1, false);
        golem.addReward(new Potion("I-14", "Gold Chain", "A valuable gold chain.", false, 0));

        Monster wraith = new Monster("M-04", "Wraith", 2, 1, false);
        Monster pursuer = new Monster("M-05", "Pursuer", 2, 1, false);
        Monster shadeBoss = new Monster("M-06", "Shade Boss", 2, 1, false);

        Monster warden = new Monster("M-07", "Warden", 2, 1, false);
        warden.addReward(new Potion("I-07", "Potion", "A small vial of restorative red liquid.", true, 5));

        Monster watcher = new Monster("M-08", "Watcher", 2, 1, false);
        watcher.addReward(new Potion("I-21", "Watcher Corpse", "The corpse of the Watcher.", false, 0));

        Monster stalker = new Monster("M-09", "Final Trial Stalker", 5, 1, false);
        stalker.addReward(new Potion("I-20", "Stalker Corpse", "The corpse of the Stalker.", false, 0));

        monsters.put("M-01", mimic);
        monsters.put("M-02", guardian);
        monsters.put("M-03", golem);
        monsters.put("M-04", wraith);
        monsters.put("M-05", pursuer);
        monsters.put("M-06", shadeBoss);
        monsters.put("M-07", warden);
        monsters.put("M-08", watcher);
        monsters.put("M-09", stalker);

        return monsters;
    }

    public Map<String, Item> loadItems(String filename) {
        Map<String, Item> items = new HashMap<>();

        try {
            File file = new File(filename);
            if (!file.exists()) {
                System.out.println("Item file not found. Using defaults.");
                return getDefaultItems();
            }

            Scanner scanner = new Scanner(file);
            boolean firstLine = true;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (firstLine || line.isEmpty()) {
                    firstLine = false;
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    String desc = parts[2].trim();
                    String roomID = parts[3].trim();
                    boolean stackable = Boolean.parseBoolean(parts[4].trim());

                    if (name.equalsIgnoreCase("Potion")) {
                        items.put(id, new Potion(id, name, desc, stackable, 5));
                    } else if (name.toLowerCase().contains("sword")) {
                        items.put(id, new Sword(id, name, desc, stackable, 3, 3));
                    } else {
                        items.put(id, new Potion(id, name, desc, stackable, 0));
                    }
                }
            }
            scanner.close();
            System.out.println("Loaded " + items.size() + " items.");

        } catch (Exception e) {
            System.out.println("Error loading items: " + e.getMessage());
            return getDefaultItems();
        }

        return items;
    }

    private Map<String, Item> getDefaultItems() {
        Map<String, Item> items = new HashMap<>();
        items.put("POT-01", new Potion("POT-01", "Potion", "Restores 5 HP", true, 5));
        items.put("SWD-01", new Sword("SWD-01", "Iron Sword", "A basic sword", false, 3, 3));
        items.put("I-09", new Potion("I-09", "Glowing Red Gem", "A radiant gem emitting visible heat and light.", false, 0));
        items.put("I-10", new Potion("I-10", "Rubble", "Broken stone fragments.", false, 0));
        items.put("I-11", new Potion("I-11", "Bait Coin", "A dull coin placed near the chest.", false, 0));
        return items;
    }

    public Map<String, Puzzle> loadPuzzles(String filename) {
        Map<String, Puzzle> puzzles = new HashMap<>();

        puzzles.put("PZ-01", new Puzzle("PZ-01", "Awareness", "AW-02",
                "You see an unstable teleporter, a glowing red gem, and some rubble.",
                "throw gem", "Try throwing the glowing gem at the teleporter"));

        puzzles.put("PZ-02", new Puzzle("PZ-02", "Restraint", "RS-02",
                "A golden chest sits in the center with a bait coin nearby.",
                "ignore coin", "Don't pick up the coin before opening the chest"));

        puzzles.put("PZ-03", new Puzzle("PZ-03", "Trust", "TR-02",
                "A guardian statue watches your every move.",
                "attack guardian", "The guardian must be destroyed"));

        puzzles.put("PZ-04", new Puzzle("PZ-04", "Sacrifice", "SC-01",
                "A powerful sword rests on a pedestal before a long bridge.",
                "throw sword", "You must give up the sword to proceed"));

        puzzles.put("PZ-05", new Puzzle("PZ-05", "Commitment", "CM-07",
                "A long corridor with many tempting items.",
                "ignore items", "Don't pick up or examine anything"));

        return puzzles;
    }
}