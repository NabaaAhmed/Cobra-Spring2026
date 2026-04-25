public class Puzzle3Trust extends Puzzle {
    private boolean guardianBroken;
    private boolean chestAppeared;
    private boolean awaitingChoice;
    private boolean combatTriggered;
    private Monster failureMonster;

    public Puzzle3Trust() {
        super("PZ-03", "Trial of Trust", "TR-02");
        this.guardianBroken = false;
        this.chestAppeared = false;
        this.awaitingChoice = false;
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
        return "==== Welcome to the Trial of Trust =====\n"
                + "You stand before a guardian statue. It seems to watch your every move.";
    }

    @Override
    public String getHint() {
        return "Hint: Not everything should be taken at face value.\n"
                + "Sometimes trust must be placed in action, not reward.";
    }

    private String completeTrustWithRewardToExitHall(Player player, String completionMessage) {
        player.modifyMaxHP(1);
        player.heal(player.getMaxHP());
        player.addTrialToken();

        player.setCurrentRoomId("TR-03");

        isSolved = true;
        isFinished = true;
        trialComplete = true;
        rewardEarned = true;

        return completionMessage
                + "\nYou have completed the Trial of Trust!"
                + "\nYou get +1 Max HP, Trial Token, full HP restore."
                + "\nA safe path opens into the Trust Exit Hall.";
    }

    @Override
    public String handleCommand(Player player, String command) {
        if (command == null) {
            return "Invalid command.";
        }

        String cmd = command.trim().toLowerCase();

        if (awaitingChoice) {
            if (cmd.equals("yes") || cmd.equals("enter") || cmd.equals("enter teleporter")) {
                return completeTrustWithRewardToExitHall(player,
                        "You saw through the illusion and made the right choice.");
            }

            if (cmd.equals("no")) {
                return completeTrustWithRewardToExitHall(player,
                        "You choose not to leave immediately, but the trial has accepted your decision.");
            }

            return "Please answer yes or no.";
        }

        if (cmd.equals("attack guardian") || cmd.equals("attack guardian statue")) {
            if (guardianBroken) {
                return "The guardian statue is already broken.";
            }

            guardianBroken = true;
            chestAppeared = true;
            return "The guardian shatters.\nA chest appears.";
        }

        if (cmd.equals("inspect chest")) {
            if (!chestAppeared) {
                return "There is no chest here yet.";
            }

            return "The chest looks tempting, but something about it feels wrong.";
        }

        if (cmd.equals("destroy chest")) {
            if (!guardianBroken) {
                return "You need to break the guardian first.";
            }

            awaitingChoice = true;
            return "You saw through the illusion and made the right choice.\n"
                    + "Would you like to leave the room? Yes or no";
        }

        if (cmd.equals("open chest")) {
            if (!chestAppeared) {
                return "There is no chest here to open.";
            }

            player.takeDamage(1);

            failureMonster = new Monster("M-02", "Guardian", 2, 1);
            failureMonster.setRewardItemName("Silver Sigil");

            combatTriggered = true;
            isFinished = true;
            trialComplete = true;
            rewardEarned = false;

            return "The guardian reforms and attacks you!\n"
                    + "You lose 1 HP.";
        }

        return "Invalid command.";
    }
}