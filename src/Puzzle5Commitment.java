public class Puzzle5Commitment extends Puzzle {
    private int forwardMoves;
    private int takeCount;
    private boolean awaitingChoice;

    public Puzzle5Commitment() {
        super("PZ-05", "Trial of Commitment", "CM-01");
        this.forwardMoves = 0;
        this.takeCount = 0;
        this.awaitingChoice = false;
    }

    @Override
    public String startPuzzle() {
        return "==== Welcome to the Trial of Commitment =====\n"
                + "You hear heavy footsteps echoing from the entrance... Commit to your path. Do not linger.";
    }

    @Override
    public String getHint() {
        return "Hint: The Pursuer is two rooms behind you. Every time you stop to examine or take an item, it gets closer.";
    }

    @Override
    public String handleCommand(Player player, String command) {
        if (command == null) {
            return "Invalid command";
        }

        String cmd = command.trim().toLowerCase();

        if (awaitingChoice) {
            if (cmd.equals("yes") || cmd.equals("no")) {
                return completeWithReward(player,
                        "You stayed the course. Commitment proven.");
            }
            return "Type yes or no.";
        }

        if (cmd.equals("move forward")) {
            forwardMoves++;

            if (forwardMoves >= 6) {
                awaitingChoice = true;
                return "Would you like to go through teleporter? Yes or no";
            }

            return "You move forward.\nProgress: " + forwardMoves + "/6";
        }

        if (cmd.equals("examine item") || cmd.equals("inspect item")) {
            player.takeDamage(1);
            player.setCurrentRoomId("EZ-01");
            isFinished = true;
            trialComplete = false;
            rewardEarned = false;
            return "You're too slow. The Pursuer has caught you! You lose 1 HP.\n"
                    + "You have failed the Trial of Commitment. You must try again.";
        }

        if (cmd.equals("take item")) {
            takeCount++;

            if (takeCount >= 2) {
                player.takeDamage(1);
                player.setCurrentRoomId("EZ-01");
                isFinished = true;
                trialComplete = false;
                rewardEarned = false;
                return "You're too slow. The Pursuer has caught you! You lose 1 HP.\n"
                        + "You have failed the Trial of Commitment. You must try again.";
            }

            return "The Pursuer gets closer!";
        }

        if (cmd.equals("kill pursuer")) {
            player.takeDamage(1);
            return completeNoReward(player,
                    "You killed the Pursuer. BOOM! The Pursuer implodes, destroying the path forward and you lose 1 HP.\n"
                            + "You have completed Trial of Commitment (No Reward).");
        }

        return "Invalid command";
    }
}
