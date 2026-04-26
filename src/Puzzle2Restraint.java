//team
public class Puzzle2Restraint extends Puzzle {
    private boolean coinTaken;
    private boolean chestInspected;
    private boolean awaitingChoice;
    private boolean combatTriggered;
    private Monster failureMonster;

    public Puzzle2Restraint() {
        super("PZ-02", "Trial of Restraint", "RS-02");
        this.coinTaken = false;
        this.chestInspected = false;
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
        return "====== Welcome to the Trial of Restraint =====\n"
                + "A chest rests in the center of the room, with a single coin lying nearby.";
    }

    @Override
    public String getHint() {
        return "Hint: The coin may not help you. Be careful how you interact with the chest.";
    }

    private String completeRestraintWithRewardToExitHall(Player player, String completionMessage) {
        player.modifyMaxHP(1);
        player.heal(player.getMaxHP());
        player.addTrialToken();

        player.setCurrentRoomId("RS-03");

        isSolved = true;
        isFinished = true;
        trialComplete = true;
        rewardEarned = true;

        return completionMessage
                + "\nYou have completed the Trial of Restraint!"
                + "\nYou get +1 Max HP, Trial Token, full HP restore."
                + "\nYou are guided into the Restraint Exit Corridor.";
    }

    @Override
    public String handleCommand(Player player, String command) {
        if (command == null) {
            return "Invalid command.";
        }

        String cmd = command.trim().toLowerCase();

        if (awaitingChoice) {
            if (cmd.equals("yes") || cmd.equals("enter") || cmd.equals("enter teleporter")) {
                return completeRestraintWithRewardToExitHall(player,
                        "You resisted greed and interacted with the chest safely.");
            }

            if (cmd.equals("no")) {
                return completeRestraintWithRewardToExitHall(player,
                        "You choose not to leave immediately, but the trial has accepted your restraint.");
            }

            return "Please answer yes or no.";
        }

        if (cmd.equals("take coin") || cmd.equals("take bait coin")) {
            if (coinTaken) {
                return "You already picked up the coin.";
            }

            coinTaken = true;
            return "You picked up the coin.";
        }

        if (cmd.equals("inspect coin")) {
            return "A dull coin lies nearby. It looks tempting, but unimportant.";
        }

        if (cmd.equals("inspect chest") || cmd.equals("open chest")) {
            if (coinTaken) {
                player.takeDamage(1);

                failureMonster = new Monster("M-01", "Mimic", 2, 1);
                combatTriggered = true;
                isFinished = true;
                trialComplete = true;
                rewardEarned = false;

                return "The Mimic ambushes you as you approach the chest!\n"
                        + "You lose 1 HP.";
            }

            chestInspected = true;
            awaitingChoice = true;

            return "The chest remains still.\n"
                    + "Would you like to leave the room? Yes or no";
        }

        return "Invalid command.";
    }
}