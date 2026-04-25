public class Puzzle6FinalTrial extends Puzzle {
    private boolean chestBurned;
    private boolean fireExtinguished;
    private int actionsSinceBurn;
    private boolean crackedSymbolAppeared;
    private boolean statueBroken;
    private boolean corePlaced;
    private boolean finalJewelAppeared;
    private boolean teleporterStabilized;
    private boolean awaitingChoice;
    private boolean stalkerDefeated;
    private boolean stalkerPathRequired;

    private boolean combatTriggered;
    private Monster failureMonster;

    public Puzzle6FinalTrial() {
        super("PZ-06", "Final Trial", "FN-02");
        this.chestBurned = false;
        this.fireExtinguished = false;
        this.actionsSinceBurn = 0;
        this.crackedSymbolAppeared = false;
        this.statueBroken = false;
        this.corePlaced = false;
        this.finalJewelAppeared = false;
        this.teleporterStabilized = false;
        this.awaitingChoice = false;
        this.stalkerDefeated = false;
        this.stalkerPathRequired = false;
        this.combatTriggered = false;
        this.failureMonster = null;
    }

    public boolean isCombatTriggered() {
        return combatTriggered;
    }

    public Monster getFailureMonster() {
        return failureMonster;
    }

    public void clearCombatTrigger() {
        combatTriggered = false;
        failureMonster = null;
    }

    public void onStalkerDefeated() {
        stalkerDefeated = true;
        clearCombatTrigger();
    }

    private void advanceBurnCounter() {
        if (chestBurned && !fireExtinguished && !crackedSymbolAppeared) {
            actionsSinceBurn++;
            if (actionsSinceBurn >= 3) {
                crackedSymbolAppeared = true;
            }
        }
    }

    @Override
    public String startPuzzle() {
        return "==== Welcome to the Final Trial =====\n"
                + "Everything you have learned will now be tested.\n"
                + "A chest, a looming statue, a cracked floor symbol, and an unstable teleporter pad stand before you.";
    }

    @Override
    public String getHint() {
        return "Hint: Do not extinguish the fire.\n"
                + "Timing and order matter.\n"
                + "Some actions cannot be undone.";
    }

    @Override
    public String handleCommand(Player player, String command) {
        if (command == null) {
            return "Invalid command.";
        }

        String cmd = command.trim().toLowerCase();

        if (awaitingChoice) {
            if (cmd.equals("yes") || cmd.equals("no")) {
                player.setCurrentRoomId("END-01");
                isSolved = true;
                isFinished = true;
                trialComplete = true;
                rewardEarned = false;
                return "You have completed the Final Trial and reached the end room!\n"
                        + "You obtain the Catalyst... You Win!";
            }
            return "Please answer yes or no.";
        }

        if (cmd.equals("burn chest")) {
            if (chestBurned) {
                return "The chest is already burning.";
            }

            chestBurned = true;
            actionsSinceBurn = 1;
            return "The chest catches fire.\nThe room grows hotter. Keep moving in the correct order.";
        }

        if (cmd.equals("extinguish fire")) {
            if (!chestBurned) {
                return "There is no fire to extinguish yet.";
            }
            if (fireExtinguished) {
                return "The fire is already out.";
            }

            fireExtinguished = true;
            stalkerPathRequired = true;
            failureMonster = new Monster("M-09", "Final Trial Stalker", 6, 1, null);
            combatTriggered = true;
            return "The trial can no longer proceed normally.\n"
                    + "The Stalker appears!";
        }

        if (cmd.equals("open chest")) {
            player.takeDamage(5);
            player.modifyMaxHP(-5);

            if (!player.isAlive()) {
                isFinished = true;
                return "A trap is triggered!\nYou lose 5 HP and 5 Max HP.\nYou died during the Final Trial.";
            }

            stalkerPathRequired = true;
            failureMonster = new Monster("M-09", "Final Trial Stalker", 6, 1, null);
            combatTriggered = true;
            return "A trap is triggered!\n"
                    + "You lose 5 HP and 5 Max HP.\n"
                    + "The Stalker appears!";
        }

        if (cmd.equals("insert explosive device")) {
            if (!chestBurned || fireExtinguished) {
                player.takeDamage(player.getCurrentHP());
                isFinished = true;
                return "The chamber collapses around you.\nYou died.";
            }
            if (statueBroken) {
                return "The statue has already been shattered.";
            }

            advanceBurnCounter();
            statueBroken = true;
            String message = "The statue shatters.\nCore Fragment drops.";
            if (crackedSymbolAppeared) {
                message += "\nA cracked floor symbol is now visible.";
            }
            return message;
        }

        if (cmd.equals("place core fragment")) {
            if (!statueBroken) {
                return "You do not have the Core Fragment yet.";
            }
            if (corePlaced) {
                return "The Core Fragment is already placed.";
            }

            advanceBurnCounter();
            corePlaced = true;
            String message = "The teleporter activates, but it remains unstable.";
            if (crackedSymbolAppeared) {
                message += "\nA cracked floor symbol is now visible.";
            }
            return message;
        }

        if (cmd.equals("step symbol")) {
            if (!crackedSymbolAppeared || !corePlaced) {
                player.takeDamage(player.getCurrentHP());
                isFinished = true;
                return "The chamber collapses around you.\nYou died.";
            }
            if (finalJewelAppeared) {
                return "The floor has already collapsed.";
            }

            finalJewelAppeared = true;
            return "The floor collapses.\nFinal Jewel appears.";
        }

        if (cmd.equals("throw final jewel")) {
            if (!finalJewelAppeared) {
                player.takeDamage(player.getCurrentHP());
                isFinished = true;
                return "The teleporter destabilizes violently.\nYou died.";
            }
            if (stalkerPathRequired && !stalkerDefeated) {
                return "The teleporter still rejects the jewel. The Stalker must be dealt with first.";
            }
            if (teleporterStabilized) {
                return "The teleporter is already stabilized.";
            }
            if (player.getTrialTokens() < 5 && !stalkerDefeated) {
                stalkerPathRequired = true;
                failureMonster = new Monster("M-09", "Final Trial Stalker", 6, 1, null);
                combatTriggered = true;
                return "The teleporter rejects your progress.\nThe Stalker appears!";
            }

            teleporterStabilized = true;
            awaitingChoice = true;
            return "The teleporter stabilizes.\nWould you like to go through the teleporter? Yes or no";
        }

        if (cmd.equals("throw stalker corpse")) {
            if (!stalkerDefeated) {
                return "You do not have the Stalker's corpse to use.";
            }
            if (teleporterStabilized) {
                return "The teleporter is already stabilized.";
            }

            teleporterStabilized = true;
            awaitingChoice = true;
            return "The teleporter stabilizes using the Stalker's remains.\n"
                    + "Would you like to go through the teleporter? Yes or no";
        }

        if (cmd.equals("enter unstable teleporter")) {
            if (teleporterStabilized) {
                awaitingChoice = true;
                return "The teleporter is ready. Would you like to go through the teleporter? Yes or no";
            }
            player.setCurrentRoomId("TP-TRAP-01");
            isFinished = true;
            return "You are pulled into a distorted space...\nYou are sent to the Trap Room.";
        }

        return "Invalid command.";
    }
}
