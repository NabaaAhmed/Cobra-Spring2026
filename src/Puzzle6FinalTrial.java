public class Puzzle6FinalTrial extends Puzzle {
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

    public Puzzle6FinalTrial() {
        super("PZ-06", "Final", "FT-06",
                "The final chamber! The Catalyst glows in the center, protected by ancient magic.",
                "burn chest then insert explosive device then place core fragment then step symbol then throw final jewel",
                "Do not extinguish the fire. Order: burn chest, insert explosive device, place core fragment, step symbol, throw final jewel");

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
    }

    @Override
    public String startPuzzle() {
        return "==== Welcome to the Final Trial ====\n" +
                "Everything you have learned will now be tested.\n" +
                "A chest burns before you... a statue looms nearby... the floor beneath you feels unstable.\n" +
                "Hint: Do not extinguish the fire.\n" +
                "Timing and order matter.\n" +
                "Some actions cannot be undone.";
    }

    @Override
    public String handleCommand(Player player, String command) {
        if (command == null) return "Invalid command.";
        if (finished) return "This puzzle is already finished.";

        String cmd = command.trim().toLowerCase();

        if (awaitingChoice) {
            if (cmd.equals("yes")) {
                solved = true;
                finished = true;
                return "You have completed the Final Trial!\n" +
                        "You obtain the Catalyst... You Win!";
            }
            if (cmd.equals("no")) {
                solved = true;
                finished = true;
                return "You must enter the teleporter to complete your journey.\n" +
                        "You obtain the Catalyst... You Win!";
            }
            return "Please answer yes or no.";
        }

        if (cmd.equals("burn chest")) {
            if (fireExtinguished) return "The fire is already gone. The trial cannot proceed normally.";
            if (chestBurned) return "The chest is already burning.";
            chestBurned = true;
            crackedFloorVisible = true;
            return "The chest continues to burn.\nA cracked floor symbol appears.";
        }

        if (cmd.equals("extinguish fire")) {
            fireExtinguished = true;
            Monster stalker = new Monster("M-FINAL-01", "Stalker", 3, 1, false);
            failPuzzle(player, stalker);
            return "The trial cannot progress... something is missing.\nA Stalker appears!\nCombat begins!";
        }

        if (cmd.equals("open chest")) {
            player.takeDamage(5);
            player.modifyMaxHP(-5);
            Monster stalker = new Monster("M-FINAL-02", "Stalker", 3, 1, false);
            failPuzzle(player, stalker);
            if (!player.isAlive()) {
                return "A trap is triggered!\nYou lose 5 HP and 5 Max HP.\nYou died during the Final Trial.";
            }
            return "A trap is triggered!\nYou lose 5 HP and 5 Max HP.\nA Stalker appears!\nCombat begins!";
        }

        if (cmd.equals("insert explosive device") || cmd.equals("insert explosive device into statue")) {
            if (!chestBurned) {
                player.takeDamage(player.getCurrentHP());
                finished = true;
                return "The chamber collapses around you!\nYou died.";
            }
            if (statueBroken) return "The statue has already been shattered.";
            statueBroken = true;
            coreFragmentDropped = true;
            return "You insert the explosive device into the statue.\nThe statue shatters.\nA Core Fragment drops.";
        }

        if (cmd.equals("place core fragment") || cmd.equals("place core fragment into broken pillar")) {
            if (!coreFragmentDropped) {
                player.takeDamage(player.getCurrentHP());
                finished = true;
                return "The chamber collapses around you!\nYou died.";
            }
            if (corePlaced) return "The Core Fragment is already placed.";
            corePlaced = true;
            teleporterActivated = true;
            return "You place the Core Fragment into the broken pillar.\nThe teleporter activates, but it is unstable.";
        }

        if (cmd.equals("step symbol") || cmd.equals("step onto cracked floor symbol")) {
            if (!crackedFloorVisible || !corePlaced) {
                player.takeDamage(player.getCurrentHP());
                finished = true;
                return "The chamber collapses around you!\nYou died.";
            }
            if (finalJewelAppeared) return "The floor has already collapsed. The Final Jewel is already here.";
            finalJewelAppeared = true;
            return "You step onto the cracked floor symbol.\nThe floor collapses.\nThe Final Jewel appears.";
        }

        if (cmd.equals("throw final jewel") || cmd.equals("throw final jewel onto teleporter")) {
            if (!finalJewelAppeared) {
                player.takeDamage(player.getCurrentHP());
                finished = true;
                return "The teleporter destabilizes violently!\nYou died.";
            }
            teleporterStabilized = true;
            awaitingChoice = true;
            return "The teleporter stabilizes.\nWould you like to go through the teleporter? Yes or no";
        }

        if (cmd.equals("enter unstable teleporter")) {
            if (teleporterActivated && !teleporterStabilized) {
                player.setCurrentRoomID("TP-TRAP-01");
                finished = true;
                return "You are pulled into a distorted space...\nYou are sent to the Trap Room.";
            }
            return "There is no unstable teleporter to enter right now.";
        }

        if (cmd.equals("hint")) {
            return hint;
        }

        return "Invalid trial command.";
    }
}