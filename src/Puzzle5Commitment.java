//danny
public class Puzzle5Commitment extends Puzzle {
    private int takeCount;
    private boolean awaitingChoice;
    private boolean combatTriggered;
    private Monster pursuerMonster;
    private int furthestRoomIndex; // Track furthest progress
    private static final String[] ROOM_ORDER = {"CM-01", "CM-02", "CM-03", "CM-04", "CM-05", "CM-06", "CM-07"};
    private static final String[] ROOM_NAMES = {
            "Commitment Entry", "Dagger Chamber", "Hall of Echoes",
            "Corridor of Doubt", "Chamber of Persistence", "Path of Resolve", "End of Commitment"
    };

    public Puzzle5Commitment() {
        super("PZ-05", "Trial of Commitment", "CM-01");
        this.takeCount = 0;
        this.awaitingChoice = false;
        this.combatTriggered = false;
        this.pursuerMonster = null;
        this.furthestRoomIndex = 0;
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

        // Find current position in sequence
        int currentIndex = -1;
        for (int i = 0; i < ROOM_ORDER.length; i++) {
            if (ROOM_ORDER[i].equals(currentRoom)) {
                currentIndex = i;
                break;
            }
        }

        if (currentIndex < 0) {
            return "";
        }

        // If player is trying to go backward (moved to a lower index than furthest)
        if (currentIndex < furthestRoomIndex) {
            // Teleport them back to their furthest progress
            String correctRoom = ROOM_ORDER[furthestRoomIndex];
            player.setCurrentRoomId(correctRoom);
            return "[ERROR] The path behind you has crumbled away!\n"
                    + "You cannot go back through the Commitment trial.\n"
                    + "You find yourself back at " + ROOM_NAMES[furthestRoomIndex] + ".";
        }

        // Update furthest progress if moving forward
        if (currentIndex > furthestRoomIndex) {
            furthestRoomIndex = currentIndex;
        }

        if (currentRoom.equals("CM-07")) {
            awaitingChoice = true;
            return "You escaped the Pursuer and reached the end of the Commitment trial.\n"
                    + "A teleporter hums in front of you, ready to return you to the Main Hall.\n"
                    + "Would you like to go through the teleporter? Yes or no";
        }

        if (currentRoom.startsWith("CM-")) {
            String directionMsg = furthestRoomIndex > 0 ? "further " : "";
            return "You continue " + directionMsg + "forward through the Commitment trial.\n"
                    + "The Pursuer's footsteps echo behind you. Do not linger.";
        }

        return "";
    }

    private String handleLingerAction(Player player, String actionType) {
        takeCount++;

        if (takeCount < 2) {
            return "You stop to " + actionType + ".\n"
                    + "The footsteps behind you stop. The Pursuer is right outside the door!\n"
                    + "Do not stop again.";
        } else {
            // Second strike: Combat and Damage
            player.takeDamage(1);
            pursuerMonster = new Monster("M-05", "Pursuer", 2, 1);
            combatTriggered = true;

            return "You stopped to " + actionType + " again.\n"
                    + "You're too slow! The Pursuer bursts into the room and catches you!\n"
                    + "You lose 1 HP.";
        }
    }

    @Override
    public String startPuzzle() {
        this.takeCount = 0;
        this.awaitingChoice = false;
        this.combatTriggered = false;
        this.pursuerMonster = null;
        this.furthestRoomIndex = 0;
        return "==== Welcome to the Trial of Commitment =====\n"
                + "Heavy footsteps echo from behind you. Commit to your path and do not linger.\n"
                + "Move through the connected rooms without stopping to take or examine items.\n"
                + "\n[WARNING] You cannot go back! Once you move forward, the path behind crumbles.\n"
                + "Use numbered moves: 'move 1' to go forward through the trial.";
    }

    @Override
    public String getHint() {
        return "Hint: Keep moving forward through the rooms. You cannot go back.\n"
                + "Use 'move 1' to advance through the trial.\n"
                + "Taking or examining items gives the Pursuer time to catch you.";
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
            return handleLingerAction(player, "inspect the item");
        }

        if (cmd.equals("take item")) {
            return handleLingerAction(player, "take an item");
        }

        if (cmd.equals("kill pursuer") || cmd.equals("attack pursuer")) {
            pursuerMonster = new Monster("M-05", "Pursuer", 2, 1);
            combatTriggered = true;

            return "You turn to face the Pursuer!\n"
                    + "It charges at you with relentless fury.";
        }

        return "Invalid command.\n"
                + "In the Commitment trial, use: 'move 1' to go forward, 'status', 'inventory', 'hint'";
    }
}