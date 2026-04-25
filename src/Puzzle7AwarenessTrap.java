//nabaa
public class Puzzle7AwarenessTrap extends Puzzle {
    private boolean rubbleTaken;
    private boolean redGemTaken;
    private boolean awaitingChoice;
    private boolean combatTriggered;
    private Monster failureMonster;

    public Puzzle7AwarenessTrap() {
        super("PZ-07", "Trap", "TP-TRAP-01");
        this.rubbleTaken = false;
        this.redGemTaken = false;
        this.awaitingChoice = false;
        this.combatTriggered = false;
        this.failureMonster = null;
    }

    public boolean isCombatTriggered() {
        return combatTriggered;
    }

    public Monster getFailureMonster() {
        return failureMonster;
    }

    public void clearCombatTrigger() {
        combatTriggered = false;
        failureMonster = null;
    }

    public String finishAfterWardenDefeated(Player player) {
        return completeNoReward(player,
                "You defeated the Warden and escaped the trap.\n"
                        + "You have completed the Trap. (No Reward)");
    }

    @Override
    public String startPuzzle() {
        return "==== Welcome to the Trap =====\n"
                + "This is your second chance. You must find the correct way to stabilize the teleporter.";
    }

    @Override
    public String getHint() {
        return "Hint: The item you need might not be visually appealing.";
    }

    @Override
    public String handleCommand(Player player, String command) {
        if (command == null) {
            return "Invalid command.";
        }

        String cmd = command.trim().toLowerCase();

        if (awaitingChoice) {
            if (cmd.equals("yes") || cmd.equals("enter") || cmd.equals("enter teleporter")) {
                return completeWithReward(player,
                        "The teleporter stabilizes and carries you safely away.");
            }

            if (cmd.equals("no")) {
                return completeWithReward(player,
                        "The teleporter remains stable, and you return to the entrance zone to receive your reward.");
            }

            return "Please answer yes or no.";
        }

        if (cmd.equals("inspect room")) {
            return "Broken stone, scorched ground, and debris are scattered across the room.\n"
                    + "Something useful here may not look valuable.";
        }

        if (cmd.equals("take rubble")) {
            if (rubbleTaken) {
                return "You already picked up the rubble.";
            }

            rubbleTaken = true;
            return "You picked up the rubble.";
        }

        if (cmd.equals("take red gem") || cmd.equals("take glowing red gem")) {
            if (redGemTaken) {
                return "You already picked up the glowing red gem.";
            }

            redGemTaken = true;
            return "You picked up the glowing red gem.";
        }

        if (cmd.equals("throw rubble")) {
            if (!rubbleTaken) {
                return "You need to take the rubble first.";
            }

            awaitingChoice = true;
            return "The teleporter stabilizes.\nWould you like to go through the teleporter? Yes or no";
        }

        if (cmd.equals("throw red gem") || cmd.equals("throw glowing red gem")) {
            if (!redGemTaken) {
                return "You need to take the glowing red gem first.";
            }

            player.takeDamage(1);
            failureMonster = new Monster("M-07", "Warden", 2, 1);
            combatTriggered = true;

            return "BOOM! The teleporter erupts and you lose 1 HP.\n"
                    + "Wrong choice. You have to fight the Warden to leave.";
        }

        if (cmd.equals("enter teleporter")) {
            return "The teleporter is still unstable. Try stabilizing it first.";
        }

        return "Invalid command.";
    }
}