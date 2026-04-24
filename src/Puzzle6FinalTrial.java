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
                "The final chamber! The Catalyst glows in the center.",
                "burn chest then insert explosive device then place core fragment then step symbol then throw final jewel",
                "Order: burn chest, insert explosive device, place core fragment, step symbol, throw final jewel");

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
        String result = "\n===== Final Trial =====\n";
        result += "A chest burns... a statue looms... the floor feels unstable.\n";
        result += "Hint: " + getHint();
        return result;
    }

    @Override
    public String handleCommand(Player player, String command) {
        if (command == null) return "Invalid command.";
        if (isFinished()) return "Trial already complete.";

        String cmd = command.trim().toLowerCase();

        if (awaitingChoice) {
            if (cmd.equals("yes")) {
                setSolved(true);
                setFinished(true);
                return "You win! You have obtained the Catalyst!";
            }
            if (cmd.equals("no")) {
                setSolved(true);
                setFinished(true);
                return "You win! You have obtained the Catalyst!";
            }
            return "Please answer yes or no.";
        }

        if (cmd.equals("burn chest")) {
            if (fireExtinguished) return "Fire is gone. Trial cannot proceed.";
            if (chestBurned) return "Chest already burning.";
            chestBurned = true;
            crackedFloorVisible = true;
            return "The chest burns. A cracked floor symbol appears.";
        }

        if (cmd.equals("extinguish fire")) {
            fireExtinguished = true;
            Monster stalker = new Monster("Stalker", 3, 1, false);
            failPuzzle(player, stalker);
            return "You extinguished the fire! A Stalker appears! Combat begins!";
        }

        if (cmd.equals("open chest")) {
            player.takeDamage(5);
            player.modifyMaxHP(-5);
            Monster stalker = new Monster("Stalker", 3, 1, false);
            failPuzzle(player, stalker);
            if (!player.isAlive()) return "You died in the trap!";
            return "Trap triggered! -5 HP, -5 Max HP! A Stalker appears!";
        }

        if (cmd.equals("insert explosive device")) {
            if (!chestBurned) {
                player.takeDamage(player.getCurrentHP());
                setFinished(true);
                return "The chamber collapses! You died.";
            }
            if (statueBroken) return "Statue already shattered.";
            statueBroken = true;
            coreFragmentDropped = true;
            return "Statue shatters! A Core Fragment drops.";
        }

        if (cmd.equals("place core fragment")) {
            if (!coreFragmentDropped) {
                player.takeDamage(player.getCurrentHP());
                setFinished(true);
                return "The chamber collapses! You died.";
            }
            if (corePlaced) return "Core already placed.";
            corePlaced = true;
            teleporterActivated = true;
            return "Teleporter activates, but is unstable.";
        }

        if (cmd.equals("step symbol")) {
            if (!crackedFloorVisible || !corePlaced) {
                player.takeDamage(player.getCurrentHP());
                setFinished(true);
                return "The chamber collapses! You died.";
            }
            if (finalJewelAppeared) return "Final Jewel already here.";
            finalJewelAppeared = true;
            return "Floor collapses! The Final Jewel appears.";
        }

        if (cmd.equals("throw final jewel")) {
            if (!finalJewelAppeared) {
                player.takeDamage(player.getCurrentHP());
                setFinished(true);
                return "The teleporter destabilizes! You died.";
            }
            teleporterStabilized = true;
            awaitingChoice = true;
            return "Teleporter stabilizes. Go through? (yes/no)";
        }

        return "Invalid command. Follow the sequence: burn chest, insert explosive device, place core fragment, step symbol, throw final jewel";
    }
}