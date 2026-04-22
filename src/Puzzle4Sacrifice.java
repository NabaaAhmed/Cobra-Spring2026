public class Puzzle4Sacrifice {
    private final String puzzleID;
    private final String trialName;
    private final String roomID;

    private boolean isSolved;
    private boolean isFinished;

    private boolean swordTaken;
    private boolean reachedBridge;
    private boolean swordThrown;

    private boolean combatTriggered;
    private Monster failureMonster;

    public Puzzle4Sacrifice() {
        this.puzzleID = "PZ-04";
        this.trialName = "Sacrifice";
        this.roomID = "SC-01";

        this.isSolved = false;
        this.isFinished = false;

        this.swordTaken = false;
        this.reachedBridge = false;
        this.swordThrown = false;

        this.combatTriggered = false;
        this.failureMonster = null;
    }

    public String getPuzzleID() {
        return puzzleID;
    }

    public String getTrialName() {
        return trialName;
    }

    public String getRoomID() {
        return roomID;
    }

    public boolean isSolved() {
        return isSolved;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public boolean isCombatTriggered() {
        return combatTriggered;
    }

    public Monster getFailureMonster() {
        return failureMonster;
    }

    public String startPuzzle() {
        return "=== Welcome to the Trial of Sacrifice ===\n"
                + "A powerful sword rests before you... but not everything is meant to be kept.\n"
                + "A bridge lies ahead... something waits at the end.\n"
                + "Hint: Not all strength should be carried forward.";
    }

    public String handleCommand(Player player, String command) {
        if (command == null) {
            return "Invalid command.";
        }

        if (isFinished) {
            return "This puzzle is already finished.";
        }

        String cmd = command.trim().toLowerCase();

        if (cmd.equals("take sword")) {
            if (swordTaken) {
                return "You already took the sword.";
            }

            swordTaken = true;

            Item sword = new Item(
                    "TRIAL-SWORD",
                    "Trial Sword",
                    "A powerful sword used in the Trial of Sacrifice.",
                    null,
                    false
            ) {
                @Override
                public void use(Player player) {
                    // no special effect
                }
            };

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

        if (cmd.equals("throw sword")) {
            if (!reachedBridge) {
                return "You need to reach the bridge first.";
            }

            if (swordThrown) {
                return "You already threw the sword.";
            }

            Item sword = player.findItemByName("Trial Sword");
            if (sword != null) {
                player.removeItem(sword);
            }

            swordThrown = true;
            return "You throw the sword away before reaching the end.";
        }

        if (cmd.equals("inspect bridge") || cmd.equals("examine bridge")) {
            if (!reachedBridge) {
                return "You are not at the bridge yet.";
            }

            if (!swordThrown) {
                return "The bridge feels unsafe while you still carry the sword.";
            }

            return "The bridge seems calm now.";
        }

        if (cmd.equals("move forward")) {
            if (!swordTaken) {
                return "You need to take the sword first.";
            }

            if (!reachedBridge) {
                return "You need to move to the bridge first.";
            }

            if (swordThrown) {
                player.modifyMaxHP(1);
                player.healToFull();
                player.addTrialToken();
                player.setCurrentRoomID("EZ-01");

                isSolved = true;
                isFinished = true;

                return "You chose to let go of power and were spared.\n"
                        + "You have completed the Trial of Sacrifice and have been teleported to the entrance zone!\n"
                        + "You get +1 Max HP, Trial Token, full HP restore.";
            }

            Item sword = player.findItemByName("Trial Sword");
            if (sword != null) {
                player.removeItem(sword);
            }

            failureMonster = new Monster("M-SAC", "Wraith", 2, 1, null);
            combatTriggered = true;
            isFinished = true;

            return "The power you held has betrayed you.\n"
                    + "You are attacked by the Wraith!\n"
                    + "Combat begins!";
        }

        return "Nothing important happens.";
    }
}