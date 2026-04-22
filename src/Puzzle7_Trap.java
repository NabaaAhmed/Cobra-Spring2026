import java.util.Scanner;

public class Puzzle7_Trap {
    private boolean completed;
    private boolean solved;
    private boolean explosionTriggered;
    private String roomId;

    // Items needed
    private Item glowingRedGem;
    private Item rubble;
    private boolean gemTaken;
    private boolean rubbleTaken;

    public Puzzle7_Trap(String roomId) {
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
        view.displayMessage("\n===== Welcome to the Trap =====");
        view.displayMessage("This is your second chance. You must find something to stabilize the teleporter.");
        view.displayMessage("The teleporter crackles with unstable energy.");
        view.displayMessage("");
        view.displayMessage("Items in the room:");
        if (!gemTaken) view.displayMessage("  - A Glowing Red Gem (radiating heat and light)");
        if (!rubbleTaken) view.displayMessage("  - Rubble (broken stone fragments)");
        view.displayMessage("");
        view.displayMessage("The item you need might not be visually appealing.");
    }

    public void displayHint(GameView view) {
        view.displayHint("The item you need might not be visually appealing.");
    }

    //Process throw action for this puzzle
    public int processThrow(String itemName, GameView view, Player player, Scanner scanner) {
        if (completed) {
            view.displayMessage("This trial is already complete.");
            return 0;
        }

        if (itemName.equalsIgnoreCase("rubble")) {
            // WIN: Throwing rubble stabilizes the teleporter (inverse of Puzzle 1)
            if (!rubbleTaken) {
                view.displayMessage("You don't have the Rubble!");
                return 0;
            }

            view.displayMessage("\nYou throw the rubble onto the teleporter...");
            view.displayMessage("The teleporter hums and stabilizes! The crackling energy subsides.");
            solved = true;
            return 1; // Win
        }

        if (itemName.equalsIgnoreCase("gem") || itemName.equalsIgnoreCase("glowing red gem")) {
            // LOSE: Throwing gem causes explosion (inverse of Puzzle 1)
            if (!gemTaken) {
                view.displayMessage("You don't have the Glowing Red Gem!");
                return 0;
            }

            view.displayMessage("\nYou throw the Glowing Red Gem onto the teleporter...");
            view.displayMessage("BOOM! You caused an explosion!");
            view.displayMessage("You lose 1 HP!");
            player.takeDamage(1);
            explosionTriggered = true;
            return -1; // Lose - explosion
        }

        view.displayMessage("You can't throw that at the teleporter.");
        return 0;
    }


    //Complete the puzzle and give rewards (win path)

    public void completePuzzle(GameView view, Player player, RoomManager roomManager) {
        if (completed) return;

        completed = true;

        if (solved) {
            view.displayMessage("\nYou have completed the Trap!");
            view.displayMessage("You get +1 Max HP, a Trial Token, and full HP restore!");

            // CORRECT ORDER: Increase Max HP first, then heal
            player.setMaxHP(player.getMaxHP() + 1);
            player.fullHeal();

            // Add Trial Token
            Item token = new Potion("TKN-01", "Trial Token",
                    "A small glowing token awarded for completing a trial.", false, 0);
            player.addItem(token);

            // Teleport to entrance on win
            player.setCurrentRoomID("EZ-01");
            view.displayMessage("\nYou are teleported back to the Entrance Zone.");
        } else if (explosionTriggered) {
            view.displayMessage("\nYou have completed the Trap. (No Reward)");
            // Teleport to entrance on loss
            player.setCurrentRoomID("EZ-01");
            view.displayMessage("\nYou are teleported back to the Entrance Zone.");
        }
    }


    //Handle penalty path

    public void handlePenalty(GameView view, Player player, RoomManager roomManager) {
        if (completed) return;

        completed = true;
        explosionTriggered = true;

        view.displayMessage("\nThe unstable energy surges around you...");
        view.displayMessage("You are pulled back to the Entrance Zone!");

        // Send to Entrance
        player.setCurrentRoomID("EZ-01");
        view.displayMessage("\nYou wake up in the Main Hall.");
    }
}
