//nabaa
public class Puzzle1Awareness extends Puzzle {
    private boolean redGemTaken;
    private boolean rubbleTaken;
    private boolean awaitingEnterChoice;

    public Puzzle1Awareness() {
        super("PZ-01", "Trial of Awareness", "AW-02");
        this.redGemTaken = false;
        this.rubbleTaken = false;
        this.awaitingEnterChoice = false;
    }

    @Override
    public String startPuzzle() {
        return "==== Welcome to the Trial of Awareness =====\n"
                + "A damaged teleporter crackles in front of you.\n"
                + "You must find the correct item to stabilize it.";
    }

    @Override
    public String getHint() {
        return "Hint: The teleporter's core is shifting toward a deep crimson.\n"
                + "Its unstable pulse matches the color of the item you need.";
    }

    @Override
    public String handleCommand(Player player, String command) {
        if (command == null) {
            return "Invalid command.";
        }

        String cmd = command.trim().toLowerCase();

        if (awaitingEnterChoice) {
            if (cmd.equals("enter") || cmd.equals("yes")) {
                return completeWithReward(player,
                        "The teleporter stabilizes and carries you safely away.");
            } else if (cmd.equals("no")) {
                return "The teleporter hums steadily in front of you. Type 'enter' when you are ready.";
            }
        }

        if (cmd.equals("take red gem") || cmd.equals("take glowing red gem")) {
            if (redGemTaken) {
                return "You already picked up the glowing red gem.";
            }
            redGemTaken = true;
            return "You picked up the glowing red gem.";
        }

        if (cmd.equals("take rubble")) {
            if (rubbleTaken) {
                return "You already picked up the rubble.";
            }
            rubbleTaken = true;
            return "You picked up the rubble.";
        }

        if (cmd.equals("throw red gem") || cmd.equals("throw glowing red gem")) {
            if (!redGemTaken) {
                return "You need to take the glowing red gem first.";
            }

            awaitingEnterChoice = true;
            return "The teleporter begins to stabilize.\n"
                    + "Enter to return to the entrance zone. Type <enter>.";
        }

        if (cmd.equals("throw rubble")) {
            if (!rubbleTaken) {
                return "You need to take the rubble first.";
            }

            player.takeDamage(1);
            player.setCurrentRoomId("TP-TRAP-01");
            isFinished = true;
            trialComplete = true;
            rewardEarned = false;

            return "BOOM! The teleporter erupts and you lose 1 HP.\n"
                    + "You have completed the Trial of Awareness. (No Reward)";
        }

        if (cmd.equals("inspect teleporter")) {
            return "The teleporter crackles violently. It looks unstable and seems to need the correct item.";
        }

        return "Invalid command.";
    }
}
