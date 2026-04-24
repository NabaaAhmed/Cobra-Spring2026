public class Puzzle4Sacrifice extends Puzzle {
    private boolean swordTaken;
    private boolean reachedBridge;
    private boolean swordThrown;
    private boolean combatTriggered;
    private Monster failureMonster;

    public Puzzle4Sacrifice() {
        super("PZ-04", "Trial of Sacrifice", "SC-01");
        this.swordTaken = false;
        this.reachedBridge = false;
        this.swordThrown = false;
        this.combatTriggered = false;
        this.failureMonster = null;
    }

    public boolean isCombatTriggered() {
        return combatTriggered;
    }

    public Monster getFailureMonster() {
        return failureMonster;
    }

    @Override
    public String startPuzzle() {
        return "=== Welcome to the Trial of Sacrifice ===\n"
                + "A powerful sword rests before you... but not everything is meant to be kept.\n"
                + "A bridge lies ahead... something waits at the end.";
    }

    @Override
    public String getHint() {
        return "Hint: Not all strength should be carried forward.";
    }

    @Override
    public String handleCommand(Player player, String command) {
        if (command == null) {
            return "Invalid command";
        }

        String cmd = command.trim().toLowerCase();

        if (cmd.equals("take sword") || cmd.equals("take strong trial sword")) {
            if (swordTaken) {
                return "You already took the sword.";
            }

            swordTaken = true;

            Item sword = new Sword(
                    "TRIAL-SWORD",
                    "Strong Trial Sword",
                    "A glowing blade filled with unstable power.",
                    null,
                    false,
                    2
            );

            player.addItem(sword);
            return "You took the sword.";
        }

        if (cmd.equals("move bridge")) {
            if (!swordTaken) {
                return "You need to take the sword first.";
            }

            reachedBridge = true;
            return "You move onto the bridge.";
        }

        if (cmd.equals("inspect bridge")) {
            if (!reachedBridge) {
                return "You are not at the bridge yet.";
            }

            if (swordThrown) {
                return "The bridge seems calm now.";
            }

            return "The bridge feels unsafe while you still carry the sword.";
        }

        if (cmd.equals("throw sword") || cmd.equals("throw strong trial sword")) {
            if (!reachedBridge) {
                return "You need to reach the bridge first.";
            }

            if (swordThrown) {
                return "You already threw the sword.";
            }

            Item sword = player.findItemByName("Strong Trial Sword");
            if (sword == null) {
                sword = player.findItemByName("Trial Sword");
            }
            if (sword != null) {
                player.removeItem(sword);
            }

            swordThrown = true;
            return "You throw the sword away before reaching the end.";
        }

        if (cmd.equals("move forward")) {
            if (!swordTaken) {
                return "You need to take the sword first.";
            }

            if (!reachedBridge) {
                return "You need to move to the bridge first.";
            }

            if (swordThrown) {
                return completeWithReward(player,
                        "You chose to let go of power and were spared.");
            }

            failureMonster = new Monster("M-04", "Wraith", 2, 1, null);
            combatTriggered = true;
            isFinished = true;

            return "The power you held has betrayed you.\nYou are attacked by the Wraith!";
        }

        return "Invalid command";
    }
}