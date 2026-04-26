//team
public class GameView {

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public void displayRoom(Room room) {
        displayRoom(room, null, null);
    }

    public void displayRoom(Room room, Player player, Puzzle activePuzzle) {
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

            // Check if we're in Commitment trial (Puzzle5)
            boolean isCommitmentTrial = (activePuzzle instanceof Puzzle5Commitment) &&
                    player != null &&
                    player.getCurrentRoomId().startsWith("CM-");

            if (isCommitmentTrial) {
                String currentRoomId = player.getCurrentRoomId();
                int currentNum = Integer.parseInt(currentRoomId.substring(3));

                // Only show connections that go forward (higher number = CM-02, CM-03, etc.)
                for (int i = 0; i < room.getConnections().size(); i++) {
                    Room connected = room.getConnections().get(i);
                    String connectedId = connected.getRoomId();
                    if (connectedId != null && connectedId.startsWith("CM-")) {
                        int connectedNum = Integer.parseInt(connectedId.substring(3));
                        if (connectedNum > currentNum) {
                            System.out.println(i + ": " + connected.getRoomName());
                        }
                    } else {
                        // Non-CM connections (like teleporter back) - show normally
                        System.out.println(i + ": " + connected.getRoomName());
                    }
                }
            } else {
                // Normal display - show all connections
                for (int i = 0; i < room.getConnections().size(); i++) {
                    System.out.println(i + ": " + room.getConnections().get(i).getRoomName());
                }
            }
        }
    }

    public void displayInventory(Player player) {
        System.out.println("=== Inventory ===");
        if (player.getInventory().isEmpty()) {
            System.out.println("- empty");
        } else {
            for (Item item : player.getInventory()) {
                System.out.println("- " + item.getItemName());
            }
        }
    }

    public void displayStatus(Player player) {
        System.out.println("=== Player Status ===");
        System.out.println("Current Room: " + player.getCurrentRoomId());
        System.out.println("HP: " + player.getCurrentHP() + "/" + player.getMaxHP());
        System.out.println("Attack Power: " + player.getAttackPower());
        System.out.println("Trial Tokens: " + player.getTrialTokens());
    }

    public void displayCombat(String message) {
        System.out.println("[COMBAT] " + message);
    }

    public void displayError(String message) {
        System.out.println("[ERROR] " + message);
    }

    public void displayIntro() {
        System.out.println("======================================");
        System.out.println("         DUNGEON OF TRIALS");
        System.out.println("======================================");
        System.out.println("You stand in a dungeon built to test those who enter.");
        System.out.println("Each path leads to a different trial.");
        System.out.println("Solve the trials, survive their punishments, and reach the end.");
        System.out.println("Type 'help' for commands.");
    }

    public void displayMainHelp() {
        System.out.println("=== Commands ===");
        System.out.println("inspect room  - look at the current room");
        System.out.println("move [number] - move to a connected room");
        System.out.println("take [item]   - pick up an item");
        System.out.println("drop [item]   - drop an item");
        System.out.println("consume [item]");
        System.out.println("equip [item]");
        System.out.println("unequip [item]");
        System.out.println("inventory     - view inventory");
        System.out.println("status        - view player status");
        System.out.println("save          - save the game");
        System.out.println("load          - load the game");
        System.out.println("exit          - quit the game");
    }
}