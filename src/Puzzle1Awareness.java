public class Puzzle1Awareness extends Puzzle {
    private boolean explosionTriggered;
    private Item glowingRedGem;
    private Item rubble;
    private boolean gemTaken;
    private boolean rubbleTaken;

    public Puzzle1Awareness() {
        super("PZ-01", "Awareness", "AW-02",
                "You see an unstable teleporter, a glowing red gem, and some rubble.",
                "throw gem", "Try throwing the glowing gem at the teleporter");

        this.explosionTriggered = false;
        this.gemTaken = false;
        this.rubbleTaken = false;
        this.glowingRedGem = new QuestItems("I-09", "Glowing Red Gem", "A radiant gem.", "AW-02", false);
        this.rubble = new QuestItems("I-10", "Rubble", "Broken stone.", "AW-02", false);
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
        String result = "\n===== Trial of Awareness =====\n";
        result += "A teleporter crackles with unstable energy.\n";
        result += "Items in the room:\n";
        if (!gemTaken) result += "  - Glowing Red Gem\n";
        if (!rubbleTaken) result += "  - Rubble\n";
        result += "\nFind the right item to throw.";
        return result;
    }

    @Override
    public String handleCommand(Player player, String command) {
        if (command == null) return "Invalid command.";
        if (isFinished()) return "This trial is already complete.";

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

        if (cmd.equals("throw gem")) {
            if (!gemTaken) return "You don't have the gem!";
            setSolved(true);
            setFinished(true);
            completePuzzle(player);
            return "You throw the gem! The teleporter stabilizes!\nTrial of Awareness Complete!\n+1 Max HP, Token, Full Heal!";
        }

        if (cmd.equals("throw rubble")) {
            if (!rubbleTaken) return "You don't have rubble!";
            player.takeDamage(1);
            explosionTriggered = true;
            setFinished(true);
            player.setCurrentRoomID("TP-TRAP-01");
            return "BOOM! Explosion! You lose 1 HP and are sent to the Trap Room!";
        }

        return "Invalid command. Try: take gem, take rubble, throw gem, throw rubble";
    }
}
