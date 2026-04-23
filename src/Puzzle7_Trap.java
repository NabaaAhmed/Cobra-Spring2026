public class Puzzle7_Trap extends Puzzle {
    private boolean explosionTriggered;
    private Potion glowingRedGem;
    private Potion rubble;
    private boolean gemTaken;
    private boolean rubbleTaken;

    public Puzzle7_Trap(String roomId) {
        super("PZ-07", "Trap", roomId,
                "You find yourself in a small, dark room filled with rubble and debris.\n" +
                        "A faint glow comes from a red gem in the corner. The teleporter is broken.\n" +
                        "You must find something to stabilize the teleporter.",
                "throw rubble", "The item you need might not be visually appealing. Try throwing the rubble.");

        this.explosionTriggered = false;
        this.gemTaken = false;
        this.rubbleTaken = false;
        this.glowingRedGem = new Potion("I-09", "Glowing Red Gem", "A radiant gem.", false, 0);
        this.rubble = new Potion("I-10", "Rubble", "Broken stone.", false, 0);
    }

    public boolean isExplosionTriggered() { return explosionTriggered; }
    public Potion getGlowingRedGem() { return glowingRedGem; }
    public Potion getRubble() { return rubble; }
    public boolean isGemTaken() { return gemTaken; }
    public boolean isRubbleTaken() { return rubbleTaken; }
    public void setGemTaken(boolean t) { gemTaken = t; }
    public void setRubbleTaken(boolean t) { rubbleTaken = t; }

    @Override
    public String startPuzzle() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n===== Welcome to the Trap =====\n");
        sb.append("You must find something to stabilize the teleporter.\n");
        sb.append("Items in the room:\n");
        if (!gemTaken) sb.append("  - Glowing Red Gem\n");
        if (!rubbleTaken) sb.append("  - Rubble\n");
        return sb.toString();
    }

    @Override
    public String handleCommand(Player player, String command) {
        if (command == null) return "Invalid command.";
        if (finished) return "This puzzle is already finished.";

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

        if (cmd.equals("throw rubble")) {
            if (!rubbleTaken) return "You don't have the Rubble!";
            solved = true;
            finished = true;
            completePuzzle(player);
            return "You throw the rubble! The teleporter stabilizes!\n" +
                    "You have escaped the Trap! +1 Max HP, Token, full heal!\n" +
                    "You are teleported back to the Entrance Zone.";
        }

        if (cmd.equals("throw gem") || cmd.equals("throw glowing red gem")) {
            if (!gemTaken) return "You don't have the gem!";
            player.takeDamage(1);
            explosionTriggered = true;
            finished = true;
            player.setCurrentRoomID("EZ-01");
            return "BOOM! Explosion! You lose 1 HP!\n" +
                    "You are sent back to the Entrance Zone.";
        }

        if (cmd.equals("hint")) {
            return hint;
        }

        return "Invalid command. Available: take gem, take rubble, throw gem, throw rubble, hint";
    }
}