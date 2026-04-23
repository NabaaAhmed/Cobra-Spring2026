public class Puzzle7_Trap extends Puzzle {
    private boolean explosionTriggered;
    private Item glowingRedGem;
    private Item rubble;
    private boolean gemTaken;
    private boolean rubbleTaken;

    public Puzzle7_Trap(String roomId) {
        super("PZ-07", "Trap", roomId,
                "A dark room with rubble and a glowing red gem. The teleporter is broken.",
                "throw rubble", "Throw the rubble at the teleporter.");

        this.explosionTriggered = false;
        this.gemTaken = false;
        this.rubbleTaken = false;
        this.glowingRedGem = new Potion("I-09", "Glowing Red Gem", "A radiant gem.", false, 0);
        this.rubble = new Potion("I-10", "Rubble", "Broken stone.", false, 0);
    }

    public boolean isExplosionTriggered() { return explosionTriggered; }
    public Item getGlowingRedGem() { return glowingRedGem; }
    public Item getRubble() { return rubble; }
    public boolean isGemTaken() { return gemTaken; }
    public boolean isRubbleTaken() { return rubbleTaken; }
    public void setGemTaken(boolean taken) { this.gemTaken = taken; }
    public void setRubbleTaken(boolean taken) { this.rubbleTaken = taken; }

    @Override
    public String startPuzzle() {
        String result = "\n===== Trap Room =====\n";
        result += "The teleporter is broken. Items in the room:\n";
        if (!gemTaken) result += "  - Glowing Red Gem\n";
        if (!rubbleTaken) result += "  - Rubble\n";
        return result;
    }

    @Override
    public String handleCommand(Player player, String command) {
        if (command == null) return "Invalid command.";
        if (isFinished()) return "You already escaped!";

        String cmd = command.trim().toLowerCase();

        if (cmd.equals("take gem")) {
            if (gemTaken) return "Gem already taken.";
            player.addItem(glowingRedGem);
            gemTaken = true;
            return "You took the Glowing Red Gem.";
        }

        if (cmd.equals("take rubble")) {
            if (rubbleTaken) return "Rubble already taken.";
            player.addItem(rubble);
            rubbleTaken = true;
            return "You took the rubble.";
        }

        if (cmd.equals("throw rubble")) {
            if (!rubbleTaken) return "You don't have rubble!";
            setSolved(true);
            setFinished(true);
            completePuzzle(player);
            return "You throw the rubble! The teleporter stabilizes!\nYou escaped the Trap! +1 Max HP, Token, Full Heal!";
        }

        if (cmd.equals("throw gem")) {
            if (!gemTaken) return "You don't have the gem!";
            player.takeDamage(1);
            explosionTriggered = true;
            setFinished(true);
            player.setCurrentRoomID("EZ-01");
            return "BOOM! Explosion! You lose 1 HP and are sent back to Entrance!";
        }

        return "Invalid command. Try: take gem, take rubble, throw gem, throw rubble";
    }
}