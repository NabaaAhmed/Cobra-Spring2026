public class Puzzle3Trust {
    private final String puzzleID;
    private final String trialName;
    private final String roomID;

    private boolean isSolved;
    private boolean isFinished;

    private boolean guardianBroken;
    private boolean chestAppeared;

    private boolean combatTriggered;
    private Monster failureMonster;

    public Puzzle3Trust() {
        this.puzzleID = "PZ-03";
        this.trialName = "Trust";
        this.roomID = "TR-02";

        this.isSolved = false;
        this.isFinished = false;

        this.guardianBroken = false;
        this.chestAppeared = false;

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
        return "==== Welcome to the Trial of Trust ====\n"
                + "You stand before a guardian statue... it watches your every move.\n"
                + "Hint: Not everything should be taken at face value.\n"
                + "Sometimes trust must be placed in action, not reward.";
    }

    public String handleCommand(Player player, String command) {
        if (command == null) {
            return "Invalid command.";
        }

        if (isFinished) {
            return "This puzzle is already finished.";
        }

        String cmd = command.trim().toLowerCase();

        if (cmd.equals("attack guardian") || cmd.equals("attack guardian statue")) {
            if (guardianBroken) {
                return "The guardian statue is already broken.";
            }

            guardianBroken = true;
            chestAppeared = true;

            return "The guardian breaks.\nA chest appears.";
        }

        if (cmd.equals("inspect chest") || cmd.equals("examine chest")) {
            if (!chestAppeared) {
                return "There is no chest here yet.";
            }

            return "The chest looks tempting, but something feels wrong about it.";
        }

        if (cmd.equals("destroy chest")) {
            if (!guardianBroken) {
                return "You need to break the guardian first.";
            }

            isSolved = true;
            isFinished = true;

            player.modifyMaxHP(1);
            player.healToFull();
            player.addTrialToken();
            player.setCurrentRoomID("EZ-01");

            return "You saw through the illusion and made the right choice.\n"
                    + "You have completed the Trial of Trust and have been teleported to the entrance zone!\n"
                    + "You get +1 Max HP, Trial Token, full HP restore.";
        }

        if (cmd.equals("open chest")) {
            if (!chestAppeared) {
                return "There is no chest here to open.";
            }

            player.takeDamage(1);

            // changed null to false
            failureMonster = new Monster("M-TRUST", "Guardian", 2, 1, false);
            combatTriggered = true;
            isFinished = true;

            return "The guardian reforms and attacks you!\n"
                    + "You lose 1 HP.\n"
                    + "Combat begins!";
        }

        return "Nothing important happens.";
    }
}