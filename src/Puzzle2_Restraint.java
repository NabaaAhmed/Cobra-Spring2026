public class Puzzle2_Restraint extends Puzzle {
    private boolean mimicTriggered;
    private boolean coinTaken;
    private boolean chestExamined;
    private boolean chestOpened;
    private boolean coinPlaced;
    private Item baitCoin;

    public Puzzle2_Restraint(String roomId) {
        super("PZ-02", "Restraint", roomId,
                "A golden chest sits in the center with a bait coin nearby.\n" +
                        "The chest looks tempting, but the coin on the floor might be a trap.",
                "ignore coin", "Don't pick up the coin before examining the chest");

        this.mimicTriggered = false;
        this.coinTaken = false;
        this.chestExamined = false;
        this.chestOpened = false;
        this.coinPlaced = false;

        this.baitCoin = new Potion("I-11", "Bait Coin",
                "A dull coin placed near the chest.", false, 0);
    }

    public boolean isMimicTriggered() { return mimicTriggered; }
    public boolean isCoinTaken() { return coinTaken; }
    public Item getBaitCoin() { return baitCoin; }
    public void setCoinTaken(boolean taken) { this.coinTaken = taken; }

    @Override
    public String startPuzzle() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n===== Welcome to the Trial of Restraint =====\n");
        sb.append("A golden chest sits in the center of the circular room.\n");
        sb.append("The chest looks normal, almost inviting.\n");
        if (!coinTaken && baitCoin != null) {
            sb.append("\nA dull coin lies on the floor nearby.\n");
        }
        sb.append("\nThe coin may not help you. Be careful how you interact with the chest.");
        return sb.toString();
    }

    @Override
    public String handleCommand(Player player, String command) {
        if (command == null) return "Invalid command.";
        if (finished) return "This trial is already complete.";

        String cmd = command.trim().toLowerCase();

        if (cmd.equals("take coin")) {
            if (coinTaken) return "The coin is already taken.";
            player.addItem(baitCoin);
            coinTaken = true;
            return "You took the Bait Coin.";
        }

        if (cmd.equals("examine chest")) {
            chestExamined = true;
            if (!coinTaken) {
                return "You examine the chest carefully.\n" +
                        "It appears to be a normal chest. Nothing happens.\n" +
                        "Type 'leave' to complete the trial.";
            } else {
                player.takeDamage(1);
                mimicTriggered = true;
                finished = true;
                return "You examine the chest while holding the coin...\n" +
                        "The chest suddenly springs to life!\n" +
                        "The Mimic ambushes and attacks you! You lose 1 HP!\n" +
                        "You have failed the Trial of Restraint. (No Reward)";
            }
        }

        if (cmd.equals("open chest")) {
            chestOpened = true;
            if (coinTaken) {
                return "You open the chest. Inside, there's a slot for the coin.\n" +
                        "Type 'place coin' to insert it.";
            } else {
                return "You try to open the chest, but it's locked.\n" +
                        "Maybe the coin nearby is needed...";
            }
        }

        if (cmd.equals("place coin")) {
            if (!coinTaken) return "You don't have the coin to place.";
            if (!chestOpened) return "You need to open the chest first.";
            if (coinPlaced) return "You've already placed the coin.";
            coinPlaced = true;
            return "You place the coin inside the chest.\n" +
                    "The chest accepts the coin and remains still.\n" +
                    "Type 'leave' to complete the trial.";
        }

        if (cmd.equals("leave")) {
            if ((!coinTaken && chestExamined && !mimicTriggered) ||
                    (coinTaken && chestOpened && coinPlaced && !mimicTriggered)) {
                solved = true;
                finished = true;
                completePuzzle(player);
                return "✨ You have completed the Trial of Restraint! ✨\n" +
                        "You get +1 Max HP, a Trial Token, and full HP restore!\n" +
                        "You are teleported back to the Entrance Zone.";
            } else if (mimicTriggered) {
                finished = true;
                return "You leave the room. The trial is complete, but you receive no reward.";
            }
            return "You haven't solved the puzzle yet. Try examining the chest without the coin.";
        }

        if (cmd.equals("hint")) {
            return hint;
        }

        return "Invalid command. Available: take coin, examine chest, open chest, place coin, leave, hint";
    }
}