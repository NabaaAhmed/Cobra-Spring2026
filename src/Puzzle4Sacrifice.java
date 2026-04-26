//danny
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

    /*
     * Called by GameController after the player uses normal movement
     * while the Sacrifice puzzle is active.
     */
    public String handleRoomMovement(Player player) {
        String currentRoom = player.getCurrentRoomId();

        if (currentRoom.equals("SC-02")) {
            reachedBridge = true;

            if (!swordTaken) {
                return "You step onto the bridge, but the trial feels incomplete.\n"
                        + "The sword was left behind in the first chamber.";
            }

            if (swordThrown) {
                return "You step onto the bridge. The air feels calmer now that the sword is gone.";
            }

            return "You step onto the long bridge.\n"
                    + "The sword grows heavier in your hands as the far end comes into view.";
        }

        if (currentRoom.equals("SC-03")) {
            if (!swordTaken) {
                isFinished = true;
                trialComplete = true;
                rewardEarned = false;

                return "You reach the end of the bridge without taking the sword.\n"
                        + "The trial ends, but you did not prove sacrifice. (No Reward)";
            }

            if (swordThrown) {
                isSolved = true;
                isFinished = true;
                trialComplete = true;
                rewardEarned = true;

                player.modifyMaxHP(1);
                player.heal(player.getMaxHP());
                player.addTrialToken();

                return "You reach the end of the bridge safely.\n"
                        + "Because you threw away the sword before reaching the end, the Wraith does not appear.\n"
                        + "You chose to let go of power and were spared.\n"
                        + "You have completed the Trial of Sacrifice!\n"
                        + "You get +1 Max HP, Trial Token, full HP restore.\n"
                        + "The path back to the Main Hall is now open.";
            }

            failureMonster = new Monster("M-04", "Wraith", 2, 1);
            combatTriggered = true;
            isFinished = true;
            trialComplete = true;
            rewardEarned = false;

            return "You reach the end of the bridge while still carrying the sword.\n"
                    + "The power you held has betrayed you.\n"
                    + "The Wraith appears and attacks!";
        }

        return "";
    }

    @Override
    public String startPuzzle() {
        return "=== Welcome to the Trial of Sacrifice ===\n"
                + "A powerful sword rests before you, but not everything is meant to be kept.\n"
                + "The bridge ahead leads to the end of the trial.\n"
                + "If you carry the sword too far, something will be waiting for you.";
    }

    @Override
    public String getHint() {
        return "Hint: The bridge is the place of sacrifice. Not all strength should be carried to the end.";
    }

    @Override
    public String handleCommand(Player player, String command) {
        if (command == null) {
            return "Invalid command.";
        }

        String cmd = command.trim().toLowerCase();

        if (cmd.equals("take sword") || cmd.equals("take strong trial sword")) {
            if (swordTaken) {
                return "You already took the sword.";
            }

            if (!player.getCurrentRoomId().equals("SC-01")) {
                return "The sword can only be taken from the Sacrifice Antechamber.";
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
            return "You take the sword from the pedestal.";
        }

        if (cmd.equals("inspect bridge")) {
            if (!player.getCurrentRoomId().equals("SC-02")) {
                return "You are not standing on the bridge yet.";
            }

            if (swordThrown) {
                return "The bridge feels calm. The sacrifice has already been made.";
            }

            if (swordTaken) {
                return "The bridge feels unsafe while you still carry the sword.";
            }

            return "The bridge stretches toward the end of the trial.";
        }

        if (cmd.equals("throw sword") || cmd.equals("throw strong trial sword")) {
            if (!player.getCurrentRoomId().equals("SC-02")) {
                return "You can only throw the sword away while standing on the bridge.";
            }

            if (!swordTaken) {
                return "You do not have the sword.";
            }

            if (swordThrown) {
                return "You already threw the sword away.";
            }

            Item sword = player.findItemByName("Strong Trial Sword");
            if (sword == null) {
                sword = player.findItemByName("Trial Sword");
            }

            if (sword != null) {
                player.removeItem(sword);
            }

            swordThrown = true;
            return "You throw the sword off the bridge.\n"
                    + "It disappears into the darkness below.\n"
                    + "The weight in the air lifts.";
        }

        return "Invalid command.";
    }
}