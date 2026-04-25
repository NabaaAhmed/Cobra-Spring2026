public class GameView {

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public void displayRoom(Room room) {
        if (room == null) {
            System.out.println("No room is currently loaded.");
            return;
        }
        System.out.println("=== " + room.getRoomName() + " ===");
        System.out.println(room.getRoomDesc());

        if (!room.getItems().isEmpty()) {
            System.out.println("\nItems in room:");
            for (Item item : room.getItems()) {
                System.out.println("- " + item.getItemName());
            }
        }

        if (!room.getExits().isEmpty()) {
            System.out.println("\nExits:");
            int i = 0;
            for (String direction : room.getExits().keySet()) {
                System.out.println(i + ": " + direction);
                i++;
            }
        }
    }

    public void displayInventory(Player player) {
        System.out.println("=== Inventory ===");
        if (player.getInventory().isEmpty()) {
            System.out.println("Inventory is empty.");
        } else {
            for (Item item : player.getInventory()) {
                System.out.println("- " + item.getItemName());
            }
        }
    }

    public void displayCombat(String message) {
        System.out.println("[COMBAT] " + message);
    }

    public void displayError(String message) {
        System.out.println("[ERROR] " + message);
    }

    public void displayStatus(Player player) {
        System.out.println("=== Player Status ===");
        System.out.println("Current Room: " + player.getCurrentRoomID());
        System.out.println("HP: " + player.getCurrentHP() + "/" + player.getMaxHP());
        System.out.println("Attack Power: " + player.getAttackPower());
        System.out.println("Trial Tokens: " + player.getTrialTokens());
    }

    public void displayHint(String hint) {
        System.out.println("[HINT] " + hint);
    }

    public void displaySaveSuccess() {
        System.out.println("[SAVE] Game saved successfully!");
    }

    public void displayLoadSuccess() {
        System.out.println("[LOAD] Game loaded successfully!");
    }

    public void displayGameOver() {
        System.out.println("\n========================================");
        System.out.println("            GAME OVER                   ");
        System.out.println("========================================");
        System.out.println("You have died. Your journey ends here.");
    }

    public void displayWin() {
        System.out.println("\n========================================");
        System.out.println("         CONGRATULATIONS!               ");
        System.out.println("========================================");
        System.out.println("You have obtained the Catalyst!");
        System.out.println("You are the worthy successor!");
        System.out.println("         YOU WIN!                       ");
        System.out.println("========================================");
    }

    public void displayHelp() {
        System.out.println("=== Commands ===");
        System.out.println("help - Displays a list of all valid commands");
        System.out.println("move [number] - Move to a connected room by its number");
        System.out.println("inventory - Displays all items held by the player");
        System.out.println("status - Displays player status");
        System.out.println("take [item] - Picks up an item from the current room");
        System.out.println("drop [item] - Drops an item from inventory");
        System.out.println("inspect [item] - Provides a description of an item");
        System.out.println("consume [potion] - Consumes a potion");
        System.out.println("equip [sword] - Equips a weapon");
        System.out.println("unequip - Unequips current weapon");
        System.out.println("attack - Initiates combat with a monster");
        System.out.println("save - Saves current game state");
        System.out.println("load - Loads a saved game state");
        System.out.println("exit - Exits current game");
    }

    public static void displayIntro() {
        System.out.println("     /\\      /\\      /\\      /\\      /\\      /\\      /\\");
        System.out.println("    /  \\    /  \\    /  \\    /  \\    /  \\    /  \\    /  \\");
        System.out.println("    \\  /    \\  /    \\  /    \\  /    \\  /    \\  /    \\  /");
        System.out.println("     \\/      \\/      \\/      \\/      \\/      \\/      \\/");
        System.out.println("==============WELCOME TO THE DUNGEON OF TRIALS==============");
        System.out.println("The Dungeon of Trials was constructed by a king to find a worthy successor.");
        System.out.println("The dungeon tests character across five themed trials.");
        System.out.println("Good Luck!");
    }
}