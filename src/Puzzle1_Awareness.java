import java.util.Scanner;

public class Puzzle1_Awareness {
    private boolean completed;
    private boolean solved;
    private boolean explosionTriggered;
    private String roomId;

    // Items needed
    private Item glowingRedGem;
    private Item rubble;
    private boolean gemTaken;
    private boolean rubbleTaken;

    public Puzzle1_Awareness(String roomId) {
        this.completed = false;
        this.solved = false;
        this.explosionTriggered = false;
        this.roomId = roomId;
        this.gemTaken = false;
        this.rubbleTaken = false;

        // Create items for this puzzle
        this.glowingRedGem = new Potion("I-09", "Glowing Red Gem",
                "A radiant gem emitting visible heat and light.", false, 0);
        this.rubble = new Potion("I-10", "Rubble",
                "Broken stone fragments.", false, 0);
    }

    public boolean isCompleted() { return completed; }
    public boolean isSolved() { return solved; }
    public boolean isExplosionTriggered() { return explosionTriggered; }

    public Item getGlowingRedGem() { return glowingRedGem; }
    public Item getRubble() { return rubble; }

    public void setGemTaken(boolean taken) { this.gemTaken = taken; }
    public void setRubbleTaken(boolean taken) { this.rubbleTaken = taken; }
    public boolean isGemTaken() { return gemTaken; }
    public boolean isRubbleTaken() { return rubbleTaken; }

    public void displayDescription(GameView view) {
        view.displayMessage("");
        view.displayMessage("==== Welcome to the Trial of Awareness ====");
        view.displayMessage("You must find something to stabilize the teleporter.");
        view.displayMessage("");
        view.displayMessage("Items in the room:");
        if (!gemTaken) view.displayMessage("  - Glowing Red Gem");
        if (!rubbleTaken) view.displayMessage("  - Rubble");
        view.displayMessage("");
    }

    public void displayHint(GameView view) {
        view.displayMessage("The teleporter's core is shifting toward a deep crimson. The teleporter's erratic pulse matches the hue of the item you need.");
    }

    /**
     * Process throw action for this puzzle
     * @return 0 = nothing special, 1 = win, -1 = lose (explosion + trap room)
     */
    public int processThrow(String itemName, GameView view, Player player, Scanner scanner) {
        if (completed) {
            view.displayMessage("This trial is already complete.");
            return 0;
        }

        if (itemName.equalsIgnoreCase("gem") || itemName.equalsIgnoreCase("glowing red gem")) {
            // WIN: Throwing the gem stabilizes the teleporter
            if (!gemTaken) {
                view.displayMessage("You don't have the Glowing Red Gem!");
                return 0;
            }

            view.displayMessage("");
            view.displayMessage("The teleporter is stabilizing.");
            view.displayMessage("Enter to return to the entrance zone. Type <enter>");

            // Wait for user to press enter
            scanner.nextLine();

            solved = true;
            return 1; // Win
        }

        if (itemName.equalsIgnoreCase("rubble")) {
            // LOSE: Throwing rubble causes explosion
            if (!rubbleTaken) {
                view.displayMessage("You don't have the Rubble!");
                return 0;
            }

            view.displayMessage("");
            view.displayMessage("BOOM! You caused an explosion, and you lose 1 HP!");
            player.takeDamage(1);
            explosionTriggered = true;
            return -1; // Lose - explosion + trap room
        }

        view.displayMessage("You can't throw that at the teleporter.");
        return 0;
    }

    /**
     * Complete the puzzle and give rewards (win path)
     */
    public void completePuzzle(GameView view, Player player, RoomManager roomManager) {
        if (completed) return;

        completed = true;

        if (solved) {
            view.displayMessage("");
            view.displayMessage("You have completed the Trial of Awareness and have been teleported to the entrance zone!");
            view.displayMessage("You get +1 Max HP, Trial Token, full HP restore.");

            player.modifyMaxHP(1);
            player.fullHeal();
            player.addTrialToken();

            player.setCurrentRoomID("EZ-01");
        } else if (explosionTriggered) {
            view.displayMessage("");
            view.displayMessage("You have completed the Trial of Awareness. (No Reward)");
            player.setCurrentRoomID("TP-TRAP-01");
        }
    }

    /**
     * Handle penalty path - send to Trap Room
     */
    public void handlePenalty(GameView view, Player player, RoomManager roomManager) {
        if (completed) return;

        completed = true;
        explosionTriggered = true;

        view.displayMessage("");
        view.displayMessage("The unstable energy surges around you...");
        view.displayMessage("You are pulled into a distorted space!");

        player.setCurrentRoomID("TP-TRAP-01");
        view.displayMessage("");
        view.displayMessage("You wake up in a circular arena with a broken teleporter frame...");
    }
}