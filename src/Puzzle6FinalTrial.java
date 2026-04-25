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
    private boolean stalkerSpawned;

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
        this.stalkerSpawned = false;
    }

    private boolean hasLessThanFiveTokens(Player player) {
        return player.getTrialTokens() < 5;
    }

    private void maybeSpawnStalker(Player player) {
        if (!stalkerSpawned && hasLessThanFiveTokens(player)) {
            stalkerSpawned = true;
            Monster stalker = new Monster("M-09", "Stalker", 5, 1, false);
            failPuzzle(player, stalker);
            System.out.println("[Final Trial] You have fewer than 5 tokens. The Stalker attacks!");
        }
    }

    @Override
    public String startPuzzle() {
        String result = "\n===== Final Trial =====\n";
        result += "A chest burns... a statue looms... the floor feels unstable.\n";
        result += "Hint: " + hint;
        return result;
    }

    @Override
    public String handleCommand(Player player, String command) {
        if (command == null) return "Invalid command.";
        if (finished) return "Trial already complete.";

        String cmd = command.trim().toLowerCase();

        if (awaitingChoice) {
            if (cmd.equals("yes")) {
                solved = true;
                finished = true;
                return "You win! You have obtained the Catalyst!";
            }
            if (cmd.equals("no")) {
                solved = true;
                finished = true;
                return "You win! You have obtained the Catalyst!";
            }
            return "Please answer yes or no.";
        }

        if (cmd.equals("burn chest")) {
            if (fireExtinguished) return "Fire is gone. Trial cannot proceed.";
            if (chestBurned) return "Chest already burning.";
            chestBurned = true;
            crackedFloorVisible = true;
            maybeSpawnStalker(player);
            return "The chest burns. A cracked floor symbol appears.";
        }

        if (cmd.equals("extinguish fire")) {
            fireExtinguished = true;
            if (!stalkerSpawned && hasLessThanFiveTokens(player)) {
                stalkerSpawned = true;
                Monster stalker = new Monster("M-09", "Stalker", 5, 1, false);
                failPuzzle(player, stalker);
                return "You extinguished the fire! A Stalker appears! Combat begins!";
            }
            return "You extinguished the fire.";
        }

        if (cmd.equals("open chest")) {
            player.takeDamage(5);
            player.modifyMaxHP(-5);
            if (!stalkerSpawned && hasLessThanFiveTokens(player)) {
                stalkerSpawned = true;
                Monster stalker = new Monster("M-09", "Stalker", 5, 1, false);
                failPuzzle(player, stalker);
            }
            if (!player.isAlive()) return "You died in the trap!";
            return "Trap triggered! -5 HP, -5 Max HP! A Stalker appears!";
        }

        if (cmd.equals("insert explosive device")) {
            if (!chestBurned) {
                player.takeDamage(player.getCurrentHP());
                finished = true;
                return "The chamber collapses! You died.";
            }
            if (statueBroken) return "Statue already shattered.";
            statueBroken = true;
            coreFragmentDropped = true;
            maybeSpawnStalker(player);
            return "Statue shatters! A Core Fragment drops.";
        }

        if (cmd.equals("place core fragment")) {
            if (!coreFragmentDropped) {
                player.takeDamage(player.getCurrentHP());
                finished = true;
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
                finished = true;
                return "The chamber collapses! You died.";
            }
            if (finalJewelAppeared) return "Final Jewel already here.";
            finalJewelAppeared = true;
            maybeSpawnStalker(player);
            return "Floor collapses! The Final Jewel appears.";
        }

        if (cmd.equals("throw final jewel")) {
            if (!finalJewelAppeared) {
                player.takeDamage(player.getCurrentHP());
                finished = true;
                return "The teleporter destabilizes! You died.";
            }
            teleporterStabilized = true;
            awaitingChoice = true;
            return "Teleporter stabilizes. Go through? (yes/no)";
        }

        return "Invalid command. Follow the sequence: burn chest, insert explosive device, place core fragment, step symbol, throw final jewel";
    }
}