public class Puzzle7Trap extends Puzzle{
    private boolean explosionTriggered;
    private Item glowingRedGem;
    private Item rubble;
    private boolean gemTaken;
    private boolean rubbleTaken;

    public Puzzle7Trap() {
        super("PZ-07", "Trap", "TP-TRAP-01",
                "A dark room with rubble and a glowing red gem. The teleporter is broken.",
                "throw rubble", "Throw the rubble at the teleporter.");

        this.explosionTriggered = false;
        this.gemTaken = false;
        this.rubbleTaken = false;
        this.glowingRedGem = new QuestItems("I-09", "Glowing Red Gem", "A radiant gem.", "TP-TRAP-01", false);
        this.rubble = new QuestItems("I-10", "Rubble", "Broken stone.", "TP-TRAP-01", false);
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
            player.takeItem(glowingRedGem);
            gemTaken = true;
            return "You took the Glowing Red Gem.";
        }

        if (cmd.equals("take rubble")) {
            if (rubbleTaken) return "Rubble already taken.";
            player.takeItem(rubble);
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