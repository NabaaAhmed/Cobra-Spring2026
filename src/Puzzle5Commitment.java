//danny
public class Puzzle5Commitment extends Puzzle {
    private int forwardMoves;
    private int takeCount;
    private boolean awaitingChoice;
    private boolean combatTriggered;
    private Monster pursuerMonster;

    public Puzzle5Commitment() {
        super("PZ-05", "Trial of Commitment", "CM-01");
        this.forwardMoves = 0;
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

    public String handleRoomMovement(Player player) {
        String currentRoom = player.getCurrentRoomId();

        if (currentRoom.equals("CM-07")) {
            awaitingChoice = true;
            return "You reach the teleporter.\nWould you like to go through it? Yes or no";
        }

        if (currentRoom.startsWith("CM-")) {
            forwardMoves++;
            return "You continue through the Commitment trial.\n"
                    + "Keep moving. Do not linger.";
        }

        return "";
    }

    public String punishItemTaking(Player player) {
        takeCount++;

        if (takeCount >= 1) {
            player.takeDamage(1);
            pursuerMonster = new Monster("M-05", "Pursuer", 2, 1);
            combatTriggered = true;

            return "You stopped to take an item. The Pursuer catches you!\n"
                    + "You lose 1 HP.\n"
                    + "It charges at you with relentless fury.";
        }

        return "You stop to take an item. The Pursuer gets closer.";
    }

    @Override
    public String startPuzzle() {
        return "==== Welcome to the Trial of Commitment =====\n"
                + "Heavy footsteps echo from behind you. Commit to your path and do not linger.";
    }

    @Override
    public String getHint() {
        return "Hint: Move through the rooms without stopping to examine or take items.";
    }

    @Override
    public String handleCommand(Player player, String command) {
        if (command == null) {
            return "Invalid command.";
        }

        String cmd = command.trim().toLowerCase();

        if (awaitingChoice) {
            if (cmd.equals("yes") || cmd.equals("no") || cmd.equals("enter") || cmd.equals("enter teleporter")) {
                return completeWithReward(player,
                        "You stayed the course. Commitment proven.");
            }

            return "Please answer yes or no.";
        }

        if (cmd.equals("move forward")) {
            forwardMoves++;

            if (forwardMoves >= 6) {
                awaitingChoice = true;
                return "You reach the teleporter.\nWould you like to go through it? Yes or no";
            }

            return "You move forward.\nProgress: " + forwardMoves + "/6";
        }

        if (cmd.equals("examine item") || cmd.equals("inspect item")) {
            player.takeDamage(1);
            pursuerMonster = new Monster("M-05", "Pursuer", 2, 1);
            combatTriggered = true;

            return "You hesitated for too long. The Pursuer catches you!\n"
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