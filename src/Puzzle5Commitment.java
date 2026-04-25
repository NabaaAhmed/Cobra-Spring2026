//danny
public class Puzzle5Commitment extends Puzzle {
    private int takeCount;
    private boolean awaitingChoice;
    private boolean combatTriggered;
    private Monster pursuerMonster;

    public Puzzle5Commitment() {
        super("PZ-05", "Trial of Commitment", "CM-01");
        this.takeCount = 0;
        this.awaitingChoice = false;
        this.combatTriggered = false;
        this.pursuerMonster = null;
    }

    public boolean isCombatTriggered() {
        return combatTriggered;
    }

    public Monster getPursuerMonster() {
        return pursuerMonster;
    }

    public void clearCombatTrigger() {
        combatTriggered = false;
        pursuerMonster = null;
    }

    public String finishAfterPursuerDefeated(Player player) {
        player.takeDamage(1);
        return completeNoReward(player,
                "You killed the Pursuer, but it implodes and destroys the path forward.\n"
                        + "You lose 1 HP.\n"
                        + "You have completed the Trial of Commitment. (No Reward)");
    }

    /*
     * This method is called by GameController after the player uses normal movement
     * like move 1 while inside the Commitment trial.
     */
    public String handleRoomMovement(Player player) {
        String currentRoom = player.getCurrentRoomId();

        if (currentRoom.equals("CM-07")) {
            awaitingChoice = true;
            return "You escaped the Pursuer and reached the end of the Commitment trial.\n"
                    + "A teleporter hums in front of you, ready to return you to the Main Hall.\n"
                    + "Would you like to go through the teleporter? Yes or no";
        }

        if (currentRoom.startsWith("CM-")) {
            return "You continue forward through the Commitment trial.\n"
                    + "The Pursuer's footsteps echo behind you. Do not linger.";
        }

        return "";
    }

    /*
     * This method is called by GameController when the player takes a real room item
     * during the Commitment trial.
     */
    public String punishItemTaking(Player player) {
        takeCount++;

        player.takeDamage(1);
        pursuerMonster = new Monster("M-05", "Pursuer", 2, 1);
        combatTriggered = true;

        return "You stopped to take an item.\n"
                + "You're too slow. The Pursuer has caught you!\n"
                + "You lose 1 HP.\n"
                + "It charges at you with relentless fury.";
    }

    @Override
    public String startPuzzle() {
        return "==== Welcome to the Trial of Commitment =====\n"
                + "Heavy footsteps echo from behind you. Commit to your path and do not linger.\n"
                + "Move through the connected rooms without stopping to take or examine items.";
    }

    @Override
    public String getHint() {
        return "Hint: Keep moving through the rooms. Taking or examining items gives the Pursuer time to catch you.";
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
                        "You escaped the Pursuer and stayed the course. Commitment proven.");
            }

            if (cmd.equals("no")) {
                return completeWithReward(player,
                        "You hesitate at the end, but the trial has already accepted your commitment.");
            }

            return "Please answer yes or no.";
        }

        if (cmd.equals("examine item") || cmd.equals("inspect item")) {
            player.takeDamage(1);
            pursuerMonster = new Monster("M-05", "Pursuer", 2, 1);
            combatTriggered = true;

            return "You hesitated for too long.\n"
                    + "You're too slow. The Pursuer has caught you!\n"
                    + "You lose 1 HP.\n"
                    + "It charges at you with relentless fury.";
        }

        if (cmd.equals("take item")) {
            return punishItemTaking(player);
        }

        if (cmd.equals("kill pursuer") || cmd.equals("attack pursuer")) {
            pursuerMonster = new Monster("M-05", "Pursuer", 2, 1);
            combatTriggered = true;

            return "You turn to face the Pursuer!\n"
                    + "It charges at you with relentless fury.";
        }

        return "Invalid command.";
    }
}