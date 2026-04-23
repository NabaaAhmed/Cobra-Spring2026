public class Puzzle1_Awareness extends Puzzle {
    private boolean explosionTriggered;
    private Item glowingRedGem;
    private Item rubble;
    private boolean gemTaken;
    private boolean rubbleTaken;

    public Puzzle1_Awareness(String roomId) {
        super("PZ-01", "Awareness", roomId,
                "You see an unstable teleporter, a glowing red gem, and some rubble.\n" +
                        "The teleporter crackles with unstable energy. You need to find the right item to throw into it.",
                "throw gem", "Try throwing the glowing gem at the teleporter");

        this.explosionTriggered = false;
        this.gemTaken = false;
        this.rubbleTaken = false;

        this.glowingRedGem = new Potion("I-09", "Glowing Red Gem",
                "A radiant gem emitting visible heat and light.", false, 0);
        this.rubble = new Potion("I-10", "Rubble",
                "Broken stone fragments.", false, 0);
    }

    public boolean isExplosionTriggered() { return explosionTriggered; }
    public Item getGlowingRedGem() { return glowingRedGem; }
    public Item getRubble() { return rubble; }
    public void setGemTaken(boolean taken) { this.gemTaken = taken; }
    public void setRubbleTaken(boolean taken) { this.rubbleTaken = taken; }
    public boolean isGemTaken() { return gemTaken; }
    public boolean isRubbleTaken() { return rubbleTaken; }

    @Override
    public String startPuzzle() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n===== Welcome to the Trial of Awareness =====\n");
        sb.append("You stand in a chamber with an unstable teleporter in the center.\n");
        sb.append("The teleporter crackles with unstable energy.\n\n");
        sb.append("Items in the room:\n");
        if (!gemTaken) sb.append("  - A Glowing Red Gem (radiating heat and light)\n");
        if (!rubbleTaken) sb.append("  - Rubble (broken stone fragments)\n");
        sb.append("\nYou must find something to stabilize the teleporter.");
        return sb.toString();
    }

    @Override
    public String handleCommand(Player player, String command) {
        if (command == null) return "Invalid command.";
        if (finished) return "This trial is already complete.";

        String cmd = command.trim().toLowerCase();

        if (cmd.equals("take gem") || cmd.equals("take glowing red gem")) {
            if (gemTaken) return "The gem is already taken.";
            player.addItem(glowingRedGem);
            gemTaken = true;
            return "You took the Glowing Red Gem.";
        }

        if (cmd.equals("take rubble")) {
            if (rubbleTaken) return "The rubble is already taken.";
            player.addItem(rubble);
            rubbleTaken = true;
            return "You took the rubble.";
        }

        if (cmd.equals("throw gem") || cmd.equals("throw glowing red gem")) {
            if (!gemTaken) return "You don't have the Glowing Red Gem!";
            solved = true;
            finished = true;
            completePuzzle(player);
            return "You throw the Glowing Red Gem onto the teleporter...\n" +
                    "The teleporter hums and stabilizes! The crackling energy subsides.\n" +
                    "You have completed the Trial of Awareness!\n" +
                    "You get +1 Max HP, a Trial Token, and full HP restore!\n" +
                    "You are teleported back to the Entrance Zone.";
        }

        if (cmd.equals("throw rubble")) {
            if (!rubbleTaken) return "You don't have the Rubble!";
            player.takeDamage(1);
            explosionTriggered = true;
            finished = true;
            player.setCurrentRoomID("TP-TRAP-01");
            return "You throw the rubble onto the teleporter...\n" +
                    "BOOM! You caused an explosion!\n" +
                    "You lose 1 HP and are sent to the Trap Room.\n" +
                    "The Warden awaits you!";
        }

        if (cmd.equals("hint")) {
            return hint;
        }

        return "Invalid command. Available: take gem, take rubble, throw gem, throw rubble, hint";
    }
}