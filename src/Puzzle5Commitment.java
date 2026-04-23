public class Puzzle5Commitment extends Puzzle {
    private int forwardMoves;
    private int takeCount;

    public Puzzle5Commitment() {
        super("PZ-05", "Commitment", "CM-05",
                "A long corridor with many tempting items.\n" +
                        "You hear footsteps behind you. Don't stop to pick up items!",
                "move forward six times",
                "The Pursuer is behind you. Every time you stop to examine or take an item, it gets closer.");

        this.forwardMoves = 0;
        this.takeCount = 0;
    }

    @Override
    public String startPuzzle() {
        return "==== Welcome to the Trial of Commitment ====\n" +
                "You hear heavy footsteps echoing from the entrance... Commit to your path. Do not linger.\n" +
                "Hint: The Pursuer is two rooms behind you. Every time you stop to examine or take an item, it gets closer.";
    }

    @Override
    public String handleCommand(Player player, String command) {
        if (command == null) return "Invalid command.";
        if (finished) return "This puzzle is already finished.";

        String cmd = command.trim().toLowerCase();

        if (cmd.equals("move forward")) {
            forwardMoves++;
            if (forwardMoves >= 6) {
                solved = true;
                finished = true;
                completePuzzle(player);
                return "You stayed the course. Commitment proven.\n" +
                        "You have completed the Trial of Commitment!\n" +
                        "You get +1 Max HP, Trial Token, full HP restore!\n" +
                        "You are teleported back to the Entrance Zone.";
            }
            return "You move forward.\nProgress: " + forwardMoves + "/6";
        }

        if (cmd.equals("examine item") || cmd.equals("inspect item")) {
            player.takeDamage(1);
            player.setCurrentRoomID("EZ-01");
            finished = true;
            return "You're too slow. The Pursuer has caught you! You lose 1 HP.\n" +
                    "You have failed the Trial of Commitment. You must try again.";
        }

        if (cmd.equals("take item")) {
            takeCount++;
            if (takeCount >= 2) {
                player.takeDamage(1);
                player.setCurrentRoomID("EZ-01");
                finished = true;
                return "You're too slow. The Pursuer has caught you! You lose 1 HP.\n" +
                        "You have failed the Trial of Commitment. You must try again.";
            }
            return "You stop to take an item.\nThe Pursuer gets closer!";
        }

        if (cmd.equals("kill pursuer")) {
            player.takeDamage(1);
            player.setCurrentRoomID("EZ-01");
            finished = true;
            return "You killed the Pursuer. BOOM! The Pursuer implodes, destroying the path forward and you lose 1 HP.\n" +
                    "You have completed Trial of Commitment (No Reward).";
        }

        if (cmd.equals("hint")) {
            return hint;
        }

        return "That command does not work in this puzzle. Try: move forward, take item, examine item, hint";
    }
}