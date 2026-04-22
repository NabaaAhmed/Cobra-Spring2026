import java.util.List;

public class GameView {

    public void displayWelcome() {
        System.out.println("\n========================================");
        System.out.println("=== DUNGEON OF TRIALS ===");
        System.out.println("========================================");
        System.out.println("You are in a dungeon filled with trials.");
        System.out.println("Complete puzzles, defeat monsters, and find the Catalyst!");
        System.out.println("========================================\n");
    }

    public void displayHelp() {
        System.out.println("\n========== COMMANDS ==========");
        System.out.println("help                    - Show this menu");
        System.out.println("look                    - Describe current room");
        System.out.println("inspect <room>          - Search current room");
        System.out.println("inspect <item>          - Examine an item");
        System.out.println("take <item>             - Pick up an item");
        System.out.println("drop <item>             - Drop an item");
        System.out.println("inventory               - Check inventory");
        System.out.println("equip <sword>           - Equip a sword");
        System.out.println("unequip                 - Unequip current sword");
        System.out.println("explore <trial>         - Explore a puzzle");
        System.out.println("solve <trial>           - Solve a puzzle");
        System.out.println("hint <trial>            - Get a hint");
        System.out.println("attack                  - Attack monster");
        System.out.println("consume <potion>        - Drink a potion");
        System.out.println("move <room>             - Move to a room");
        System.out.println("save                    - Save game");
        System.out.println("load                    - Load game");
        System.out.println("exit                    - Exit game");
        System.out.println("=============================\n");
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public void displayError(String message) {
        System.out.println("[ERROR] " + message);
    }

    public void displayRoom(Room room) {
        if (room == null) {
            System.out.println("No room loaded.");
            return;
        }

        System.out.println("\n========================================");
        System.out.println("=== " + room.getName() + " ===");
        System.out.println(room.getDescription());
        System.out.println("========================================");

        if (room.getItems() != null && !room.getItems().isEmpty()) {
            System.out.println("\nItems visible:");
            for (Item item : room.getItems()) {
                System.out.println("  - " + item.getName());
            }
        }

        if (room.getMonster() != null && room.getMonster().isAlive()) {
            System.out.println("\nMONSTER HERE: " + room.getMonster().getName());
        }

        if (room.getPuzzle() != null && !room.getPuzzle().isSolved()) {
            System.out.println("\nPUZZLE HERE: " + room.getPuzzle().getTrialName() + " Trial");
        }

        System.out.println("\nExits:");
        List<String> exits = room.getConnectionNames();
        for (int i = 0; i < exits.size(); i++) {
            System.out.println("  - " + exits.get(i));
        }
        System.out.println();
    }

    public void displayInventory(Player player) {
        System.out.println("\n========== INVENTORY ==========");
        List<Item> items = player.getInventory();
        if (items.isEmpty()) {
            System.out.println("Inventory is empty.");
        } else {
            for (Item item : items) {
                System.out.println("- " + item.getName());
                if (item instanceof Sword) {
                    Sword sword = (Sword) item;
                    System.out.println("  Damage: +" + sword.getDamageBonus() +
                            ", Durability: " + sword.getDurability());
                } else if (item instanceof Potion) {
                    System.out.println("  Restores HP when consumed");
                } else {
                    System.out.println("  " + item.getDescription());
                }
            }
        }

        if (player.getEquippedSword() != null) {
            System.out.println("\nEquipped: " + player.getEquippedSword().getName());
        }
        System.out.println("===============================\n");
    }

    public void displayStatus(Player player) {
        System.out.println("\n========== PLAYER STATUS ==========");
        System.out.println("HP: " + player.getCurrentHP() + "/" + player.getMaxHP());
        System.out.println("Attack Power: " + player.getAttackPower());
        System.out.println("Current Room: " + player.getCurrentRoomID());
        if (player.getEquippedSword() != null) {
            System.out.println("Equipped: " + player.getEquippedSword().getName());
        }
        System.out.println("===================================\n");
    }

    public void displayCombat(String message) {
        System.out.println("[COMBAT] " + message);
    }

    public void displayPuzzle(Puzzle puzzle) {
        System.out.println("\n========== " + puzzle.getTrialName() + " TRIAL ==========");
        System.out.println(puzzle.getDescription());
        System.out.println("==========================================\n");
    }

    public void displayHint(String hint) {
        System.out.println("[HINT] " + hint);
    }

    public void displayGameOver() {
        System.out.println("\n=========================================");
        System.out.println("              GAME OVER!                 ");
        System.out.println("        You have been defeated.          ");
        System.out.println("          Your journey ends.             ");
        System.out.println("=========================================\n");
    }

    public void displaySaveSuccess() {
        System.out.println("[SAVE] Game saved successfully!");
    }

    public void displayLoadSuccess() {
        System.out.println("[LOAD] Game loaded successfully!");
    }
}