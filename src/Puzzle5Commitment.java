public class Puzzle5Commitment extends Puzzle {
    private int forwardMoves;
    private int takeCount;
    private boolean pursuerKilled;

    public Puzzle5Commitment() {
        super("PZ-05", "Commitment", "CM-05",
                "A long corridor with tempting items. Footsteps echo behind you.",
                "move forward six times",
                "Keep moving forward. Don't stop to take or examine items.");

        this.forwardMoves = 0;
        this.takeCount = 0;
        this.pursuerKilled = false;
    }

    @Override
    public String startPuzzle() {
        String result = "\n===== Trial of Commitment =====\n";
        result += "Footsteps echo behind you. Keep moving!\n";
        result += "Hint: " + hint;
        return result;
    }

    @Override
    public String handleCommand(Player player, String command) {
        if (command == null) return "Invalid command.";
        if (finished) return "Trial already complete.";

        String cmd = command.trim().toLowerCase();

        if (cmd.equals("move forward")) {
            forwardMoves++;
            if (forwardMoves >= 6 && !pursuerKilled) {
                solved = true;
                finished = true;
                completePuzzle(player);
                return "You stayed the course!\nTrial of Commitment Complete!\n+1 Max HP, Token, Full Heal!";
            }
            if (forwardMoves >= 6 && pursuerKilled) {
                finished = true;
                return "You have completed the Trial of Commitment (No Reward).";
            }
            return "You move forward. Progress: " + forwardMoves + "/6";
        }

        if (cmd.equals("examine item") || cmd.equals("inspect item")) {
            player.takeDamage(1);
            player.setCurrentRoomID("EZ-01");
            // Do NOT finish the trial – retryable
            return "Too slow! The Pursuer caught you! You lose 1 HP. Return to Entrance. You may try again.";
        }

        if (cmd.equals("take item")) {
            takeCount++;
            if (takeCount >= 2) {
                player.takeDamage(1);
                player.setCurrentRoomID("EZ-01");
                // Retryable
                return "Too slow! The Pursuer caught you! You lose 1 HP. Return to Entrance. You may try again.";
            }
            return "You take an item. The Pursuer gets closer!";
        }

        if (cmd.equals("kill pursuer")) {
            player.takeDamage(1);
            player.setCurrentRoomID("EZ-01");
            pursuerKilled = true;
            forwardMoves = 6;
            solved = false;
            finished = true;
            return "You killed the Pursuer. BOOM! The Pursuer implodes, destroying the path forward and you lose 1 HP.\n" +
                    "You have completed the Trial of Commitment (No Reward).";
        }

        if (cmd.equals("hint")) {
            return hint;
        }

        return "Invalid command. Try: move forward, take item, examine item, kill pursuer";
    }
}