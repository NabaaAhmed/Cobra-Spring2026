public class Puzzle4Sacrifice extends Puzzle {
    private boolean swordTaken;
    private boolean reachedBridge;
    private boolean swordThrown;

    public Puzzle4Sacrifice() {
        super("PZ-04", "Sacrifice", "SC-04",
                "A powerful sword rests on a pedestal before a long bridge.\n" +
                        "The bridge seems unstable. Carrying the sword across might be dangerous.",
                "take sword then move bridge then throw sword then move forward",
                "You must give up the sword to proceed safely. Take the sword, move to the bridge, throw it away, then move forward.");

        this.swordTaken = false;
        this.reachedBridge = false;
        this.swordThrown = false;
    }

    @Override
    public String startPuzzle() {
        return "=== Welcome to the Trial of Sacrifice ===\n" +
                "A powerful sword rests before you... but not everything is meant to be kept.\n" +
                "A bridge lies ahead... something waits at the end.\n" +
                "Hint: Not all strength should be carried forward.";
    }

    @Override
    public String handleCommand(Player player, String command) {
        if (command == null) return "Invalid command.";
        if (isSolved()) return "This puzzle is already finished.";

        String cmd = command.trim().toLowerCase();

        if (cmd.equals("take sword")) {
            if (swordTaken) return "You already took the sword.";
            swordTaken = true;
            Sword trialSword = new Sword("TRIAL-SWORD", "Trial Sword",
                    "A powerful sword used in the Trial of Sacrifice.", "SC-04", false, 1);
            player.takeItem(trialSword);
            return "You took the sword.";
        }

        if (cmd.equals("move bridge")) {
            if (!swordTaken) return "You need to take the sword first.";
            reachedBridge = true;
            return "You move onto the bridge.";
        }

        if (cmd.equals("throw sword")) {
            if (!reachedBridge) return "You need to reach the bridge first.";
            if (swordThrown) return "You already threw the sword.";
            Item sword = player.findItemByName("Trial Sword");
            if (sword != null) player.removeItem(sword);
            swordThrown = true;
            return "You throw the sword away before reaching the end.";
        }

        if (cmd.equals("inspect bridge") || cmd.equals("examine bridge")) {
            if (!reachedBridge) return "You are not at the bridge yet.";
            if (!swordThrown) return "The bridge feels unsafe while you still carry the sword.";
            return "The bridge seems calm now.";
        }

        if (cmd.equals("move forward")) {
            if (!swordTaken) return "You need to take the sword first.";
            if (!reachedBridge) return "You need to move to the bridge first.";
            if (swordThrown) {
                setFinished(true);
                setSolved(true);
                completePuzzle(player);
                return "You chose to let go of power and were spared.\n" +
                        "You have completed the Trial of Sacrifice!\n" +
                        "You get +1 Max HP, Trial Token, full HP restore!\n" +
                        "You are teleported back to the Entrance Zone.";
            }
            Item sword = player.findItemByName("Trial Sword");
            if (sword != null) player.removeItem(sword);
            Monster wraith = new Monster("M-SAC", "Wraith", 2, 1, false);
            failPuzzle(player, wraith);
            return "The power you held has betrayed you.\n" +
                    "You are attacked by the Wraith!\n" +
                    "Combat begins!";
        }

        if (cmd.equals("hint")) {
            return getHint();
        }

        return "Nothing important happens. Try: take sword, move bridge, throw sword, move forward";
    }
}