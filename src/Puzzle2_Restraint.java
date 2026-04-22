import java.util.Scanner;

public class Puzzle2_Restraint {
    private boolean completed;
    private boolean solved;
    private boolean mimicTriggered;
    private boolean coinTaken;
    private boolean chestExamined;
    private boolean chestOpened;
    private boolean coinPlaced;
    private String roomId;

    private Item baitCoin;
    private boolean chestVisible;

    public Puzzle2_Restraint(String roomId) {
        this.completed = false;
        this.solved = false;
        this.mimicTriggered = false;
        this.coinTaken = false;
        this.chestExamined = false;
        this.chestOpened = false;
        this.coinPlaced = false;
        this.roomId = roomId;
        this.chestVisible = true;

        // Create the bait coin
        this.baitCoin = new Potion("I-11", "Bait Coin",
                "A dull coin placed near the chest.", false, 0);
    }

    public boolean isCompleted() { return completed; }
    public boolean isSolved() { return solved; }
    public boolean isMimicTriggered() { return mimicTriggered; }
    public boolean isCoinTaken() { return coinTaken; }
    public Item getBaitCoin() { return baitCoin; }

    public void setCoinTaken(boolean taken) {
        this.coinTaken = taken;
    }

    public void setChestExamined(boolean examined) {
        this.chestExamined = examined;
    }

    public void setChestOpened(boolean opened) {
        this.chestOpened = opened;
    }

    public void setCoinPlaced(boolean placed) {
        this.coinPlaced = placed;
    }

    public void displayDescription(GameView view) {
        view.displayMessage("\n===== Welcome to the Trial of Restraint =====");
        view.displayMessage("A golden chest sits in the center of the circular room.");

        if (chestVisible) {
            view.displayMessage("The chest looks normal, almost inviting.");
        }

        if (!coinTaken && baitCoin != null) {
            view.displayMessage("\nA dull coin lies on the floor nearby.");
        }

        view.displayMessage("");
        view.displayMessage("The coin may not help you. Be careful how you interact with the chest.");
    }

    public void displayHint(GameView view) {
        view.displayHint("The coin may not help you. Be careful how you interact with the chest.");
    }

    /**
     * Handle examining the chest
     * @return true if Mimic triggered, false otherwise
     */
    public boolean examineChest(GameView view, Player player) {
        if (completed) {
            view.displayMessage("The trial is already complete.");
            return false;
        }

        chestExamined = true;

        // WIN CONDITION: Examining chest WITHOUT coin in inventory is safe
        if (!coinTaken) {
            view.displayMessage("\nYou examine the chest carefully.");
            view.displayMessage("It appears to be a normal chest. Nothing happens.");
            view.displayMessage("\nWould you like to leave the room? (yes/no)");
            return false; // No mimic, safe examination
        }
        // LOSE CONDITION: Coin in inventory + examine chest = Mimic ambush
        else {
            view.displayMessage("\nYou examine the chest while holding the coin...");
            view.displayMessage("The chest suddenly springs to life!");
            view.displayMessage("The Mimic ambushes and attacks you! You lose 1 HP!");
            player.takeDamage(1);
            mimicTriggered = true;
            return true; // Mimic triggered
        }
    }

    /**
     * Handle opening the chest
     */
    public boolean openChest(GameView view, Player player) {
        if (completed) {
            view.displayMessage("The trial is already complete.");
            return false;
        }

        chestOpened = true;

        if (coinTaken) {
            view.displayMessage("\nYou open the chest. Inside, there's a slot for the coin.");
            view.displayMessage("Would you like to place the coin inside? (yes/no)");
            return false;
        } else {
            view.displayMessage("\nYou try to open the chest, but it's locked.");
            view.displayMessage("Maybe the coin nearby is needed...");
            return false;
        }
    }

    /**
     * Handle placing the coin in the chest
     */
    public boolean placeCoin(GameView view) {
        if (completed) {
            view.displayMessage("The trial is already complete.");
            return false;
        }

        if (coinTaken && chestOpened && !coinPlaced) {
            coinPlaced = true;
            view.displayMessage("\nYou place the coin inside the chest.");
            view.displayMessage("The chest accepts the coin and remains still.");
            view.displayMessage("\nWould you like to leave the room? (yes/no)");
            return true;
        } else if (!coinTaken) {
            view.displayMessage("You don't have the coin to place.");
            return false;
        } else if (!chestOpened) {
            view.displayMessage("You need to open the chest first.");
            return false;
        } else {
            view.displayMessage("You've already placed the coin.");
            return false;
        }
    }

    /**
     * Complete the puzzle (called when player leaves room)
     */
    public void completePuzzle(GameView view, Player player, RoomManager roomManager, boolean leaving) {
        if (completed) return;

        // WIN CONDITION: Never held the coin (coin not taken)
        if (!coinTaken && chestExamined && !mimicTriggered) {
            solved = true;
            completed = true;
            view.displayMessage("\n✨ You have completed the Trial of Restraint! ✨");
            view.displayMessage("You get +1 Max HP, a Trial Token, and full HP restore!");

            // CORRECT ORDER: Increase Max HP first, then heal
            player.setMaxHP(player.getMaxHP() + 1);
            player.fullHeal();

            Item token = new Potion("TKN-01", "Trial Token",
                    "A small glowing token awarded for completing a trial.", false, 0);
            player.addItem(token);
        }
        // WIN CONDITION: Took coin, opened chest, placed coin, then left
        else if (coinTaken && chestOpened && coinPlaced && !mimicTriggered) {
            solved = true;
            completed = true;
            view.displayMessage("\n✨ You have completed the Trial of Restraint! ✨");
            view.displayMessage("You get +1 Max HP, a Trial Token, and full HP restore!");

            // CORRECT ORDER: Increase Max HP first, then heal
            player.setMaxHP(player.getMaxHP() + 1);
            player.fullHeal();

            Item token = new Potion("TKN-01", "Trial Token",
                    "A small glowing token awarded for completing a trial.", false, 0);
            player.addItem(token);
        }
        // LOSE CONDITION: Mimic triggered
        else if (mimicTriggered) {
            completed = true;
            view.displayMessage("\n⚠️ You have completed the Trial of Restraint. (No Reward) ⚠️");
            // No rewards
        }
        // Partial - left without completing properly
        else if (leaving && !completed) {
            completed = true;
            view.displayMessage("\n⚠️ You have completed the Trial of Restraint. (No Reward) ⚠️");
        }

        // Teleport to entrance if completed
        if (completed) {
            player.setCurrentRoomID("EZ-01");
            view.displayMessage("\nYou are teleported back to the Entrance Zone.");
        }
    }

    /**
     * Called when Mimic is defeated in combat
     */
    public void onMimicDefeated(GameView view, Player player, RoomManager roomManager) {
        if (!mimicTriggered || completed) return;

        completed = true;
        view.displayMessage("\n⚠️ You have completed the Trial of Restraint. (No Reward) ⚠️");

        player.setCurrentRoomID("EZ-01");
        view.displayMessage("\nYou are teleported back to the Entrance Zone.");
    }
}