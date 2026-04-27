//team
public class GameView {

    public void displayMessage(String message) {
        System.out.println(message);
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

    public void displayPuzzleComplete(Player player, String roomHeader) {
        System.out.println("");
        System.out.println("Puzzle complete.");
        displayStatus(player);
        System.out.println("");
        System.out.println(roomHeader);
    }

    public void displayTrialComplete(String trialName, Player player, String roomHeader) {
        System.out.println("You have completed the " + trialName + ". (No Reward)");
        System.out.println("You have been teleported back to the Main Hall.");
        displayStatus(player);
        System.out.println("");
        System.out.println(roomHeader);
    }

    public void displayCommitmentExplore(Room room, boolean atFinalRoom) {
        if (room == null) {
            System.out.println("No room loaded.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(room.getRoomName())
                .append(" (").append(room.getRoomId()).append(") ===\n");
        sb.append(room.getRoomDesc());
        sb.append("\n\nConnections:");

        if (atFinalRoom) {
            sb.append("\n- No forward exits. The path to the Main Hall is open.");
        } else if (room.getConnections().size() > 1) {
            Room forwardRoom = room.getConnections().get(1);
            sb.append("\n1: ")
                    .append(forwardRoom.getRoomName())
                    .append(" (")
                    .append(forwardRoom.getRoomId())
                    .append(")");
        } else {
            sb.append("\n- No forward exits.");
        }

        System.out.println(sb.toString());
    }

    public void displayMainHelp() {
        System.out.println("=== Commands ===");
        System.out.println("explore room    - read the current room description and exits");
        System.out.println("inspect room    - check items in the current room");
        System.out.println("move [number]   - move to a connected room");
        System.out.println("take [item]     - pick up an item");
        System.out.println("drop [item]     - drop an item");
        System.out.println("consume [potion]- consume potion");
        System.out.println("equip [sword]   - equip a sword");
        System.out.println("unequip [sword] - unequip sword");
        System.out.println("inventory       - view inventory");
        System.out.println("status          - view player status");
        System.out.println("save            - save the game");
        System.out.println("load            - load the game");
        System.out.println("restart        - restart the game from the beginning");
        System.out.println("exit            - quit the game");
    }

    public void displayPuzzleHelp(Puzzle puzzle) {
        if (puzzle == null) {
            System.out.println("No active puzzle.");
            return;
        }

        System.out.println("=== Puzzle Commands ===");
        System.out.println("hint");

        if (puzzle instanceof Puzzle1Awareness) {
            System.out.println("take glowing red gem");
            System.out.println("take rubble");
            System.out.println("throw glowing red gem");
            System.out.println("throw rubble");
            System.out.println("inspect teleporter");
            System.out.println("enter");
            return;
        }

        if (puzzle instanceof Puzzle2Restraint) {
            System.out.println("take coin");
            System.out.println("inspect coin");
            System.out.println("inspect chest");
            System.out.println("open chest");
            System.out.println("yes / no");
            return;
        }

        if (puzzle instanceof Puzzle3Trust) {
            System.out.println("attack guardian");
            System.out.println("inspect chest");
            System.out.println("destroy chest");
            System.out.println("open chest");
            System.out.println("yes / no");
            return;
        }

        if (puzzle instanceof Puzzle4Sacrifice) {
            System.out.println("take sword");
            System.out.println("move [number]");
            System.out.println("inspect bridge");
            System.out.println("throw sword");
            return;
        }

        if (puzzle instanceof Puzzle5Commitment) {
            System.out.println("move [number]");
            System.out.println("examine item");
            System.out.println("take [item]");
            System.out.println("kill pursuer");
            System.out.println("yes / no");
            return;
        }

        if (puzzle instanceof Puzzle6FinalTrial) {
            System.out.println("burn chest");
            System.out.println("extinguish fire");
            System.out.println("open chest");
            System.out.println("insert explosive device");
            System.out.println("place core fragment");
            System.out.println("step symbol");
            System.out.println("throw final jewel");
            System.out.println("enter unstable teleporter");
            System.out.println("yes / no");
            return;
        }

        if (puzzle instanceof Puzzle7AwarenessTrap) {
            System.out.println("inspect room");
            System.out.println("take rubble");
            System.out.println("take red gem");
            System.out.println("throw rubble");
            System.out.println("throw red gem");
            System.out.println("enter teleporter");
            System.out.println("yes / no");
        }
    }
}