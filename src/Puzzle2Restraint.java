public class Puzzle2Restraint extends Puzzle {
    private boolean coinTaken;
    private boolean awaitingChoice;
    private boolean combatTriggered;
    private Monster failureMonster;

    public Puzzle2Restraint() {
        super("PZ-02", "Trial of Restraint", "RS-02");
        this.coinTaken = false;
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
                + "A chest is in the center of the room with a coin nearby.";
    }

    @Override
    public String getHint() {
        return "Hint: The coin may not help you. Be careful how you interact with the chest.";
    }

    @Override
    public String handleCommand(Player player, String command) {
        if (command == null) {
            return "Invalid command";
        }

        String cmd = command.trim().toLowerCase();

        if (awaitingChoice) {
            if (cmd.equals("yes") || cmd.equals("no")) {
                return completeWithReward(player,
                        "You resisted greed and interacted with the chest safely.");
            }
        }

        if (cmd.equals("take coin") || cmd.equals("take bait coin")) {
            if (coinTaken) {
                return "You already picked up the coin.";
            }
            coinTaken = true;
            return "You picked up the coin.";
        }

        if (cmd.equals("inspect chest") || cmd.equals("open chest")) {
            if (coinTaken) {
                player.takeDamage(1);
                failureMonster = new Monster("M-01", "Mimic", 2, 1, null);
                combatTriggered = true;
                isFinished = true;
                return "The Mimic ambushes and attacks you! You lose 1 HP!";
            }

            awaitingChoice = true;
            return "The chest is safe.\nWould you like to leave the room? Yes or no";
        }

        if (cmd.equals("inspect coin")) {
            return "A dull coin lies nearby.";
        }

        return "Invalid command";
    }
}