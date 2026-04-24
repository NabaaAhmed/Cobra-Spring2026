public class GameView {
    //Nabaa
    public void displayMessage(String message) {
        System.out.println(message);
    }
    //Nabaa
    public void displayHint(String hint) {
        System.out.println("[HINT] " + hint);
    }

    public void displayPuzzle(Puzzle puzzle) {
        System.out.println("\n========== " + puzzle.getTrialName() + " TRIAL ==========");
        System.out.println(puzzle.getDescription());
        System.out.println("==========================================\n");
    }

    //Nabaa and Danny
    public void displayRoom(Room room) {
        if (room == null) {
            System.out.println("No room loaded.");
            return;
        }

        System.out.println("\n=== " + room.getRoomName() + " ===");
        System.out.println(room.getRoomDesc());

        if (!room.getItems().isEmpty()) {
            System.out.println("\nItems in room:");
            for (Item item : room.getItems()) {
                System.out.println("- " + item.getItemName());
            }
        }

        if (!room.getConnections().isEmpty()) {
            System.out.println("\nConnections:");
            for (int i = 0; i < room.getConnections().size(); i++) {
                System.out.println(i + ": " + room.getConnections().get(i).getRoomName());
            }
        }
    }

    //Nabaa
    public void displayInventory(Player player) {
        System.out.println("=== Inventory ===");
        if (player.getInventory().isEmpty()) {
            System.out.println("Inventory is empty.");
        } else {
            player.getInventory().forEach(item ->
                    System.out.println("- " + item.getItemName())
            );
        }
    }

    //Nabaa
    public void displayCombat(String message) {
        System.out.println("[COMBAT] " + message);
    }

    //Nabaa
    public void displayError(String message) {
        System.out.println("[ERROR] " + message);
    }

    //Nabaa
    public void displayStatus(Player player) {
        System.out.println("=== Player Status ===");
        System.out.println("Current Room: " + player.getCurrentRoomID());
        System.out.println("HP: " + player.getCurrentHP() + "/" + player.getMaxHP());
        System.out.println("Attack Power: " + player.getAttackPower());
        System.out.println("Trial Tokens: " + player.getTrialTokens());
    }

    //Danny and Mai
    public void displayHelp() {
        System.out.println("=== Commands ===");
        System.out.println("Help <Displays a list of all valid commands and their descriptions>");
        System.out.println("Move to [room number] <Move to a connected room by its name>");
        System.out.println("Inventory <Displays all items currently held by the player>");
        System.out.println("Status <Displays the player's current status, including HP, max HP, attack power, and trial tokens>");
        System.out.println("Take [item] <Picks up an item from the current room into the inventory");
        System.out.println("Drop [item] <Drops an item from the inventory into the current room");
        System.out.println("Use [item] <Uses an item from inventory, applying its effect or interacting with a target in the room>");
        System.out.println("Inspect [item] <Provides a description of an item in the inventory or the current room>");
        System.out.println("Consume [potion] <Consumes a consumable item (e.g., potion), applying its effect and removing it from inventory>");
        System.out.println("Throw [item] <Throws an item at a target in the current room>");
        System.out.println("Equip [sword] <Equips a weapon, applying its stat bonuses to the player>");
        System.out.println("Fight <Initiates combat with a monster in the current room>");
        System.out.println("Choose trial <Allows player to choose to attempt from the entrance room>");
        System.out.println("Save <Saves current game state to file>");
        System.out.println("Load <Loads a previously saved game state from file>");
        System.out.println("Exit <Exits current game>");
    }

    //Mai
    public void displayIntro() {
        System.out.println("     /\\      /\\      /\\      /\\      /\\      /\\      /\\");
        System.out.println("    /  \\    /  \\    /  \\    /  \\    /  \\    /  \\    /  \\");
        System.out.println("    \\  /    \\  /    \\  /    \\  /    \\  /    \\  /    \\  /");
        System.out.println("     \\/      \\/      \\/      \\/      \\/      \\/      \\/");

        System.out.println("==============WELCOME TO THE DUNGEON OF TRIALS==============");
        System.out.println("The Dungeon of Trials was constructed by a king to find a worthy successor.");
        System.out.println("The dungeon tests character across five themed trials.");
        System.out.println("A detached Teleport Trap Room serves as a penalty zone for incorrect puzzle actions.");
        System.out.println("Good Luck!");
        System.out.println("Type 'help' for commands.");
    }

    //Nabaa
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