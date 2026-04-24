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
    private boolean stalkerDefeated = false;
    private boolean stalkerPathRequired = false;

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
        this.stalkerDefeated = false;
        this.stalkerPathRequired = false;
    }

    public void onStalkerDefeated() {
        this.stalkerDefeated = true;
        this.setCombatTriggered(false);
        this.setFailureMonster(null);
    }
    public void clearCombatTrigger() {
        // This resets the internal flags so the Model stops trying to start combat
        this.setCombatTriggered(false);
        this.setFailureMonster(null);
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

        String cmd = command.trim().toLowerCase();

        if (awaitingChoice) {
            if (cmd.equals("yes")) {
                player.setCurrentRoomID("END-01");
                setSolved(true);
                setFinished(true);
                return "You step through the teleporter... You Win! You have obtained the Catalyst!";
            }
            if (cmd.equals("no")) {
                player.setCurrentRoomID("END-01");
                setSolved(true);
                setFinished(true);
                return "You must enter the teleporter to complete your journey.";
            }
            return "Please answer yes or no.";
        }

        switch (cmd) {
            case "burn chest" -> {
                if (chestBurned) return "Chest already burning.";
                chestBurned = true;
                crackedFloorVisible = true;
                return "The chest burns. A cracked floor symbol appears.";
            }
            case "extinguish fire" -> {
                fireExtinguished = true;
                stalkerPathRequired = true;
                Monster stalker = new Monster("M-09", "Final Trial Stalker", 1, 1, false);
                failPuzzle(player, stalker);
                return "You extinguished the fire! A Stalker appears! Combat begins!";
            }
            case "open chest" -> {
                player.takeDamage(5);
                player.modifyMaxHP(-5);
                stalkerPathRequired = true;
                Monster stalker = new Monster("M-09", "Final Trial Stalker", 1, 1, false);
                failPuzzle(player, stalker);
                if (!player.isAlive()) return "You died in the trap!";
                return "Trap triggered! -5 HP, -5 Max HP! A Stalker appears!";
            }
            case "insert explosive device" -> {
                if (!chestBurned || fireExtinguished) {
                    player.takeDamage(player.getCurrentHP());
                    setFinished(true);
                    return "The chamber collapses! You died.";
                }
                if (statueBroken) return "Statue already shattered.";
                statueBroken = true;
                coreFragmentDropped = true;
                return "Statue shatters! A Core Fragment drops.";
            }
            case "place core fragment" -> {
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
            case "step symbol" -> {
                if (!crackedFloorVisible || !corePlaced) {
                    player.takeDamage(player.getCurrentHP());
                    setFinished(true);
                    return "The chamber collapses! You died.";
                }
                if (finalJewelAppeared) return "Final Jewel already here.";
                finalJewelAppeared = true;
                return "Floor collapses! The Final Jewel appears.";
            }
            case "throw final jewel" -> {
                if (!finalJewelAppeared) {
                    player.takeDamage(player.getCurrentHP());
                    setFinished(true);
                    return "The teleporter destabilizes! You died.";
                }
                teleporterStabilized = true;
                awaitingChoice = true;
                return "Teleporter stabilizes. Go through? (yes/no)";
            }
        }

        return "Invalid command. Follow the sequence: burn chest, insert explosive device, place core fragment, step symbol, throw final jewel";
    }
}
