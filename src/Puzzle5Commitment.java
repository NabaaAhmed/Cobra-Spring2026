public class Puzzle5Commitment extends Puzzle {
    private int forwardMoves;
    private int takeCount;

    public Puzzle5Commitment() {
        super("PZ-05", "Commitment", "CM-05",
                "A long corridor with tempting items. Footsteps echo behind you.",
                "move forward six times",
                "Keep moving forward. Don't stop to take or examine items.");

        this.forwardMoves = 0;
        this.takeCount = 0;
    }

    @Override
    public String startPuzzle() {
        String result = "\n===== Trial of Commitment =====\n";
        result += "Footsteps echo behind you. Keep moving!\n";
        result += "Hint: " + getHint();
        return result;
    }

    @Override
    public String handleCommand(Player player, String command) {
        if (command == null) return "Invalid command.";
        if (isFinished()) return "Trial already complete.";

        String cmd = command.trim().toLowerCase();

        switch (cmd) {
            case "move forward" -> {
                forwardMoves++;
                if (forwardMoves >= 6) {
                    setSolved(true);
                    setFinished(true);
                    completePuzzle(player);
                    return "You stayed the course!\nTrial of Commitment Complete!\n+1 Max HP, Token, Full Heal!";
                }
                return "You move forward. Progress: " + forwardMoves + "/6";
            }
            case "examine item", "inspect item" -> {
                player.takeDamage(1);
                player.setCurrentRoomID("EZ-01");
                setFinished(true);
                return "Too slow! The Pursuer caught you! You lose 1 HP. Trial failed.";
            }
            case "take item" -> {
                takeCount++;
                if (takeCount >= 2) {
                    player.takeDamage(1);
                    player.setCurrentRoomID("EZ-01");
                    setFinished(true);
                    return "Too slow! The Pursuer caught you! You lose 1 HP. Trial failed.";
                }
                return "You take an item. The Pursuer gets closer!";
            }
        }

        return "Invalid command. Try: move forward, take item, examine item";
    }
}