//danny
public class Puzzle4Sacrifice extends Puzzle {
    private boolean swordTaken;
    private boolean swordThrown;
    private boolean combatTriggered;
    private Monster failureMonster;

    public Puzzle4Sacrifice() {
        super("PZ-04", "Trial of Sacrifice", "SC-01");
        this.swordTaken = false;
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
                + "A powerful sword rests before you, but not everything is meant to be kept.\n"
                + "A bridge lies ahead, and something waits beyond it.";
    }

    @Override
    public String getHint() {
        return "Hint: Not all strength should be carried forward.";
    }

    @Override
    public String handleCommand(Player player, String command) {
        if (command == null) {
            return "Invalid command.";
        }

        String cmd = command.trim().toLowerCase();
        String currentRoom = player.getCurrentRoomId();

        // Handle sword taking - only works in SC-01
        if (cmd.equals("take sword") || cmd.equals("take strong trial sword")) {
            if (!currentRoom.equals("SC-01")) {
                return "There is no sword here.";
            }

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
            return "You take the sword.";
        }

        // Handle throw sword - works in SC-02 (bridge room)
        if (cmd.equals("throw sword") || cmd.equals("throw strong trial sword")) {
            if (!currentRoom.equals("SC-02")) {
                return "You need to be on the bridge to throw the sword.";
            }

            if (!swordTaken) {
                return "You don't have the sword to throw.";
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
            return "You throw the sword off the bridge into the darkness below.";
        }

        // Handle reaching the end of bridge - auto-triggered when entering SC-03
        if (currentRoom.equals("SC-03") && swordTaken && !swordThrown && !combatTriggered) {
            failureMonster = new Monster("M-03", "Wraith", 2, 1);
            combatTriggered = true;
            isFinished = true;
            trialComplete = true;
            rewardEarned = false;
            return "The power you held has betrayed you.\n"
                    + "The Wraith attacks!";
        }

        // Handle successful completion (threw sword before reaching SC-03)
        if (currentRoom.equals("SC-03") && swordThrown && !combatTriggered) {
            return completeWithReward(player,
                    "You chose to let go of power and were spared.");
        }

        return "Invalid command.";
    }
}