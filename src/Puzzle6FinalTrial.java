public class Puzzle6FinalTrial {
    private final String puzzleID;
    private final String trialName;
    private final String roomID;

    private boolean isSolved;
    private boolean isFinished;

    private boolean chestBurned;
    private boolean fireExtinguished;
    private boolean crackedFloorVisible;
    private boolean statueBroken;
    private boolean coreFragmentDropped;
    private boolean corePlaced;
    private boolean teleporterActivated;
    private boolean finalJewelAppeared;
    private boolean teleporterStabilized;
    private boolean awaitingChoice;

    private boolean combatTriggered;
    private Monster failureMonster;

    public Puzzle6FinalTrial() {
        this.puzzleID = "PZ-06";
        this.trialName = "Final";
        this.roomID = "FN-02";

        this.isSolved = false;
        this.isFinished = false;

        this.chestBurned = false;
        this.fireExtinguished = false;
        this.crackedFloorVisible = false;
        this.statueBroken = false;
        this.coreFragmentDropped = false;
        this.corePlaced = false;
        this.teleporterActivated = false;
        this.finalJewelAppeared = false;
        this.teleporterStabilized = false;
        this.awaitingChoice = false;

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
        return "==== Welcome to the Final Trial =====\n"
                + "Everything you have learned will now be tested.\n"
                + "A chest burns before you... a statue looms nearby... the floor beneath you feels unstable.\n"
                + "Hint: Do not extinguish the fire.\n"
                + "Timing and order matter.\n"
                + "Some actions cannot be undone.";
    }

    public String handleCommand(Player player, String command) {
        if (command == null) {
            return "Invalid command.";
        }

        if (isFinished) {
            return "This puzzle is already finished.";
        }

        String cmd = command.trim().toLowerCase();

        if (awaitingChoice) {
            if (cmd.equals("yes")) {
                player.setCurrentRoomID("END-01");
                isSolved = true;
                isFinished = true;

                return "You have completed the Final Trial and have been teleported to the end room!\n"
                        + "You obtain the Catalyst... You Win!";
            }

            if (cmd.equals("no")) {
                player.setCurrentRoomID("END-01");
                isSolved = true;
                isFinished = true;

                return "You must enter the teleporter to complete your journey.\n"
                        + "You obtain the Catalyst... You Win!";
            }

            return "Please answer yes or no.";
        }

        if (cmd.equals("burn chest")) {
            if (fireExtinguished) {
                return "The fire is already gone. The trial cannot proceed normally.";
            }

            if (chestBurned) {
                return "The chest is already burning.";
            }

            chestBurned = true;
            crackedFloorVisible = true;

            return "The chest continues to burn.\n"
                    + "A cracked floor symbol appears.";
        }

        if (cmd.equals("extinguish fire")) {
            fireExtinguished = true;
            failureMonster = new Monster("M-FINAL-01", "Stalker", 3, 1, null);
            combatTriggered = true;
            isFinished = true;

            return "The trial cannot progress... something is missing.\n"
                    + "A Stalker appears!\n"
                    + "Combat begins!";
        }

        if (cmd.equals("open chest")) {
            player.takeDamage(5);
            player.modifyMaxHP(-5);
            isFinished = true;

            if (!player.isAlive()) {
                return "A trap is triggered!\n"
                        + "You lose 5 HP and 5 Max HP.\n"
                        + "You died during the Final Trial.";
            }

            failureMonster = new Monster("M-FINAL-02", "Stalker", 3, 1, null);
            combatTriggered = true;

            return "A trap is triggered!\n"
                    + "You lose 5 HP and 5 Max HP.\n"
                    + "A Stalker appears!\n"
                    + "Combat begins!";
        }

        if (cmd.equals("insert explosive device") || cmd.equals("insert explosive device into statue")) {
            if (!chestBurned) {
                player.takeDamage(player.getCurrentHP());
                isFinished = true;
                return "The chamber collapses around you!\nYou died.";
            }

            if (statueBroken) {
                return "The statue has already been shattered.";
            }

            statueBroken = true;
            coreFragmentDropped = true;

            return "You insert the explosive device into the statue.\n"
                    + "The statue shatters.\n"
                    + "A Core Fragment drops.";
        }

        if (cmd.equals("place core fragment") || cmd.equals("place core fragment into broken pillar")) {
            if (!coreFragmentDropped) {
                player.takeDamage(player.getCurrentHP());
                isFinished = true;
                return "The chamber collapses around you!\nYou died.";
            }

            if (corePlaced) {
                return "The Core Fragment is already placed.";
            }

            corePlaced = true;
            teleporterActivated = true;

            return "You place the Core Fragment into the broken pillar.\n"
                    + "The teleporter activates, but it is unstable.";
        }

        if (cmd.equals("step symbol") || cmd.equals("step onto cracked floor symbol")) {
            if (!crackedFloorVisible || !corePlaced) {
                player.takeDamage(player.getCurrentHP());
                isFinished = true;
                return "The chamber collapses around you!\nYou died.";
            }

            if (finalJewelAppeared) {
                return "The floor has already collapsed. The Final Jewel is already here.";
            }

            finalJewelAppeared = true;

            return "You step onto the cracked floor symbol.\n"
                    + "The floor collapses.\n"
                    + "The Final Jewel appears.";
        }

        if (cmd.equals("throw final jewel") || cmd.equals("throw final jewel onto teleporter")) {
            if (!finalJewelAppeared) {
                player.takeDamage(player.getCurrentHP());
                isFinished = true;
                return "The teleporter destabilizes violently!\nYou died.";
            }

            teleporterStabilized = true;
            awaitingChoice = true;

            return "The teleporter stabilizes.\nWould you like to go through the teleporter? Yes or no";
        }

        if (cmd.equals("enter unstable teleporter")) {
            if (teleporterActivated && !teleporterStabilized) {
                player.setCurrentRoomID("TP-TRAP-01");
                isFinished = true;

                return "You are pulled into a distorted space...\n"
                        + "You are sent to the Trap Room.";
            }

            return "There is no unstable teleporter to enter right now.";
        }

        if (cmd.startsWith("throw ") && !cmd.equals("throw final jewel") && !cmd.equals("throw final jewel onto teleporter")) {
            if (teleporterActivated && !teleporterStabilized) {
                player.takeDamage(player.getCurrentHP());
                isFinished = true;
                return "The teleporter destabilizes violently!\nYou died.";
            }
        }

        return "Invalid trial command.";
    }
}