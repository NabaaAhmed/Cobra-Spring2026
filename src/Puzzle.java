public class Puzzle {
    private String puzzleID;
    private String trialName;
    private String roomID;
    private boolean isSolved;
    private boolean isFinished;

    // shared state
    private boolean awaitingChoice;

    // Puz 2 state
    private boolean coinTaken;

    // Puz 3 state
    private boolean guardianBroken;

    // Puz 4 state
    private boolean swordTaken;
    private boolean reachedBridge;
    private boolean swordThrown;

    // Puz 5 state
    private int forwardMoves;
    private int takeCount;

    // Puz 6 state
    private boolean chestBurned;
    private int waitCount;
    private boolean statueBroken;
    private boolean corePlaced;
    private boolean jewelRevealed;
    private boolean teleporterStabilized;

    // Puz 7 state
    private int hellStep;

    public Puzzle(String puzzleID, String trialName, String roomID, boolean isSolved) {
        this.puzzleID = puzzleID;
        this.trialName = trialName;
        this.roomID = roomID;
        this.isSolved = isSolved;
        this.isFinished = false;
    }

    public String getPuzzleID() {
        return puzzleID;
    }

    public String getTrialName() {
        return trialName;
    }

    public String getRoomID() {
        return roomID;
    }

    public boolean isSolved() {
        return isSolved;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public String startPuzzle() {
        switch (puzzleID) {
            case "PZ-01":
                return "==== Welcome to the Trial of Awareness =====\n"
                        + "You must find something to stabilize the teleporter.\n"
                        + "Hint: Throw the GLOWING RED GEM onto the unstable teleporter to stabilize it, then enter it.";

            case "PZ-02":
                return "====== Welcome to the Trial of Restraint =====\n"
                        + "A chest is in the center of the room with a coin nearby.\n"
                        + "Hint: The coin may not help you. Be careful how you interact with the chest.";

            case "PZ-03":
                return "==== Welcome to the Trial of Trust =====\n"
                        + "You stand before a guardian statue... it watches your every move.\n"
                        + "Hint: Not everything should be taken at face value.\n"
                        + "Sometimes trust must be placed in action, not reward.";

            case "PZ-04":
                return "=== Welcome to the Trial of Sacrifice ===\n"
                        + "A powerful sword rests before you... but not everything is meant to be kept.\n"
                        + "A bridge lies ahead... something waits at the end.\n"
                        + "Hint: Not all strength should be carried forward.";

            case "PZ-05":
                return "==== Welcome to the Trial of Commitment =====\n"
                        + "You hear heavy footsteps echoing from the entrance... Commit to your path. Do not linger.\n"
                        + "Hint: The Pursuer is two rooms behind you. Every time you stop to examine or take an item, it gets closer.";

            case "PZ-06":
                return "==== Welcome to the Final Trial =====\n"
                        + "Everything you have learned will now be tested.\n"
                        + "A chest burns before you... a statue looms nearby... the floor beneath you feels unstable.\n"
                        + "Hint: Do not extinguish the fire. Timing and order matter. Some actions cannot be undone.";

            case "PZ-07":
                return "=== Welcome to the Trial of HELL ===\n"
                        + "This is your final judgement...\n"
                        + "NO SECOND CHANCES SHALL BE GIVEN!!!\n"
                        + "Hint: There is only one correct path. Hesitation or error will lead to your end.";

            default:
                return "Unknown puzzle.";
        }
    }

    public String handleCommand(Player player, String command) {
        if (command == null) {
            return "Invalid command.";
        }

        String cmd = command.trim().toLowerCase();

        switch (puzzleID) {
            case "PZ-01":
                return handleAwareness(player, cmd);
            case "PZ-02":
                return handleRestraint(player, cmd);
            case "PZ-03":
                return handleTrust(player, cmd);
            case "PZ-04":
                return handleSacrifice(player, cmd);
            case "PZ-05":
                return handleCommitment(player, cmd);
            case "PZ-06":
                return handleFinalTrial(player, cmd);
            case "PZ-07":
                return handleHellTrial(player, cmd);
            default:
                return "Unknown puzzle.";
        }
    }

    // -------------------------
    // PUZ 1 - Awareness
    // -------------------------
    private String handleAwareness(Player player, String cmd) {
        if (awaitingChoice) {
            if (cmd.equals("yes")) {
                return rewardToEntrance(player, "Trial of Awareness");
            } else if (cmd.equals("no")) {
                return rewardToEntranceNoChoice(player, "Trial of Awareness");
            }
        }

        if (cmd.equals("take red gem")) {
            return "You picked up the RED GEM.";
        }

        if (cmd.equals("throw red gem") || cmd.equals("throw red gem to teleporter") || cmd.equals("throw red gem on teleporter")) {
            awaitingChoice = true;
            return "The teleporter stabilizes.\nWould you like to go through the teleporter? Yes or no";
        }

        if (cmd.equals("take rubble")) {
            return "You picked up the RUBBLE.";
        }

        if (cmd.equals("throw rubble") || cmd.equals("throw rubble to teleporter") || cmd.equals("throw rubble on teleporter")) {
            player.takeDamage(1);
            player.setCurrentRoomID("EZ-01");
            isFinished = true;
            return "BOOM! You caused an explosion, and you lose 1 HP.\n"
                    + "You have failed the Trial of Awareness. You must try again.";
        }

        return "Nothing important happens.";
    }

    // -------------------------
    // PUZ 2 - Restraint
    // -------------------------
    private String handleRestraint(Player player, String cmd) {
        if (awaitingChoice) {
            if (cmd.equals("yes")) {
                return rewardToEntrance(player, "Trial of Restraint");
            } else if (cmd.equals("no")) {
                return rewardToEntranceNoChoice(player, "Trial of Restraint");
            }
        }

        if (cmd.equals("take coin")) {
            coinTaken = true;
            return "You picked up the coin.";
        }

        if (cmd.equals("interact chest") || cmd.equals("inspect chest") || cmd.equals("open chest")) {
            if (coinTaken) {
                player.takeDamage(1);
                player.setCurrentRoomID("EZ-01");
                isFinished = true;
                return "The chest reveals itself as a Mimic!\n"
                        + "You are attacked and lose 1 HP!\n"
                        + "You have failed the Trial of Restraint. You must try again.";
            } else {
                awaitingChoice = true;
                return "The chest is safe.\nWould you like to leave the room? Yes or no";
            }
        }

        return "Nothing important happens.";
    }

    // -------------------------
    // PUZ 3 - Trust
    // -------------------------
    private String handleTrust(Player player, String cmd) {
        if (cmd.equals("attack guardian")) {
            guardianBroken = true;
            return "The guardian breaks.\nA chest appears.";
        }

        if (cmd.equals("destroy chest")) {
            if (guardianBroken) {
                player.modifyMaxHP(1);
                player.healToFull();
                player.addTrialToken();
                player.setCurrentRoomID("EZ-01");
                isSolved = true;
                isFinished = true;
                return "You saw through the illusion and made the right choice.\n"
                        + "You have completed the Trial of Trust and have been teleported to the entrance zone!\n"
                        + "You get +1 Max HP, Trial Token, full HP restore.";
            } else {
                return "You need to break the guardian first.";
            }
        }

        if (cmd.equals("open chest")) {
            player.takeDamage(1);
            player.setCurrentRoomID("EZ-01");
            isFinished = true;
            return "The guardian reforms and attacks you!\n"
                    + "You lose 1 HP.\n"
                    + "You have completed Trial of Trust (No Reward).";
        }

        return "Nothing important happens.";
    }

    // -------------------------
    // PUZ 4 - Sacrifice
    // -------------------------
    private String handleSacrifice(Player player, String cmd) {
        if (cmd.equals("take sword")) {
            swordTaken = true;
            return "You took the sword.";
        }

        if (cmd.equals("move bridge")) {
            if (!swordTaken) {
                return "You need to take the sword first.";
            }
            reachedBridge = true;
            return "You move onto the bridge.";
        }

        if (cmd.equals("throw sword")) {
            if (!reachedBridge) {
                return "You need to reach the bridge first.";
            }
            swordThrown = true;
            return "You throw the sword away before reaching the end.";
        }

        if (cmd.equals("move forward")) {
            if (!swordTaken) {
                return "You need to take the sword first.";
            }

            if (reachedBridge && swordThrown) {
                player.modifyMaxHP(1);
                player.healToFull();
                player.addTrialToken();
                player.setCurrentRoomID("EZ-01");
                isSolved = true;
                isFinished = true;
                return "You chose to let go of power... and were spared.\n"
                        + "You have completed the Trial of Sacrifice and have been teleported to the entrance zone!\n"
                        + "You get +1 Max HP, Trial Token, full HP restore.";
            }

            if (reachedBridge && !swordThrown) {
                player.takeDamage(1);
                player.setCurrentRoomID("EZ-01");
                isFinished = true;
                return "The power you held has betrayed you.\n"
                        + "You are attacked by the Wraith!\n"
                        + "You have completed Trial of Sacrifice (No Reward).";
            }
        }

        return "Nothing important happens.";
    }

    // -------------------------
    // PUZ 5 - Commitment
    // -------------------------
    private String handleCommitment(Player player, String cmd) {
        if (awaitingChoice) {
            if (cmd.equals("yes")) {
                return rewardToEntrance(player, "Trial of Commitment");
            } else if (cmd.equals("no")) {
                return rewardToEntranceNoChoice(player, "Trial of Commitment");
            }
        }

        if (cmd.equals("move forward")) {
            forwardMoves++;

            if (forwardMoves >= 6) {
                awaitingChoice = true;
                return "You reached the Commitment Exit.\nWould you like to go through teleporter? Yes or no";
            }

            return "You move forward. Rooms cleared: " + forwardMoves + "/6";
        }

        if (cmd.equals("examine item")) {
            player.takeDamage(1);
            player.setCurrentRoomID("EZ-01");
            isFinished = true;
            return "You're too slow. The Pursuer has caught you! You lose 1 HP.\n"
                    + "You have failed the Trial of Commitment. You must try again.";
        }

        if (cmd.equals("take item")) {
            takeCount++;
            if (takeCount >= 2) {
                player.takeDamage(1);
                player.setCurrentRoomID("EZ-01");
                isFinished = true;
                return "You're too slow. The Pursuer has caught you! You lose 1 HP.\n"
                        + "You have failed the Trial of Commitment. You must try again.";
            }
            return "You stopped to take an item. The Pursuer gets closer!";
        }

        if (cmd.equals("use smoke bomb")) {
            takeCount = 0;
            return "You used a Smoke Bomb and escaped the ambush.";
        }

        if (cmd.equals("kill pursuer")) {
            player.takeDamage(1);
            player.setCurrentRoomID("EZ-01");
            isFinished = true;
            return "You killed the Pursuer. BOOM! The Pursuer implodes, destroying the path forward and you lose 1 HP.\n"
                    + "You have failed the Trial of Commitment. You must try again.";
        }

        return "Nothing important happens.";
    }

    // -------------------------
    // PUZ 6 - Final Trial
    // -------------------------
    private String handleFinalTrial(Player player, String cmd) {
        if (awaitingChoice) {
            if (cmd.equals("yes")) {
                player.setCurrentRoomID("END-01");
                isSolved = true;
                isFinished = true;
                return "You have completed the Final Trial and have been teleported to the end room!\n"
                        + "You obtain the Catalyst... You Win!";
            } else if (cmd.equals("no")) {
                player.setCurrentRoomID("END-01");
                isSolved = true;
                isFinished = true;
                return "You must enter the teleporter to complete your journey.\n"
                        + "You obtain the Catalyst... You Win!";
            }
        }

        if (cmd.equals("burn chest")) {
            chestBurned = true;
            return "The chest burns. Do not extinguish the fire.";
        }

        if (cmd.equals("extinguish fire")) {
            return "The trial cannot progress... something is missing.\n"
                    + "You must defeat Stalker and use the corpse to stabilize the teleporter.";
        }

        if (cmd.equals("open chest")) {
            player.takeDamage(5);
            return "A trap is triggered!\n"
                    + "You lose 5 HP.\n"
                    + "Stalker spawns.";
        }

        if (cmd.equals("wait")) {
            if (chestBurned && waitCount < 3) {
                waitCount++;
                if (waitCount == 3) {
                    return "A cracked floor symbol appears.";
                }
                return "You wait... (" + waitCount + "/3)";
            }
            return "Nothing happens.";
        }

        if (cmd.equals("insert explosive device")) {
            if (waitCount >= 3) {
                statueBroken = true;
                return "The statue shatters. Core Fragment drops.";
            }
            return "It is too early for that.";
        }

        if (cmd.equals("place core fragment")) {
            if (statueBroken) {
                corePlaced = true;
                return "The teleporter activates, but it is unstable.";
            }
            return "You do not have the Core Fragment yet.";
        }

        if (cmd.equals("step symbol")) {
            if (corePlaced) {
                jewelRevealed = true;
                return "The floor collapses. Final Jewel appears.";
            }
            return "Nothing happens.";
        }

        if (cmd.equals("throw final jewel")) {
            if (jewelRevealed) {
                teleporterStabilized = true;
                awaitingChoice = true;
                return "The teleporter stabilizes.\nWould you like to go through the teleporter? Yes or no";
            }
            return "You do not have the Final Jewel yet.";
        }

        if (cmd.equals("enter unstable teleporter")) {
            player.setCurrentRoomID("TP-TRAP-01");
            isFinished = true;
            return "You are pulled into a distorted space...\nYou are sent to the Trap Room.";
        }

        return "Nothing important happens.";
    }

    // -------------------------
    // PUZ 7 - Trial of Hell
    // -------------------------
    private String handleHellTrial(Player player, String cmd) {
        if (awaitingChoice) {
            if (cmd.equals("yes")) {
                player.setCurrentRoomID("START-01");
                isSolved = true;
                isFinished = true;
                return "You have conquered the Trial of Hell.\n"
                        + "You have escaped the dungeon...\n"
                        + "YOU WIN THE GAME.";
            } else if (cmd.equals("no")) {
                player.setCurrentRoomID("START-01");
                isSolved = true;
                isFinished = true;
                return "There is nothing left but to finish what you started.\n"
                        + "YOU WIN THE GAME.";
            }
        }

        if (hellStep == 0 && cmd.equals("inspect room")) {
            hellStep++;
            return "You study the room carefully.";
        }

        if (hellStep == 1 && cmd.equals("ignore false items")) {
            hellStep++;
            return "You ignored the false items.";
        }

        if (hellStep == 2 && cmd.equals("use correct item")) {
            hellStep++;
            return "You used the correct item.";
        }

        if (hellStep == 3 && cmd.equals("move correct position")) {
            hellStep++;
            return "You move into the correct position.";
        }

        if (hellStep == 4 && cmd.equals("activate object")) {
            awaitingChoice = true;
            return "You have proven yourself worthy...\nWould you like to claim your victory? Yes or no";
        }

        player.setCurrentRoomID("START-01");
        isFinished = true;
        return "You have failed the Trial of Hell...\n"
                + "There are no retries.\n"
                + "GAME OVER";
    }

    // -------------------------
    // helper methods
    // -------------------------
    private String rewardToEntrance(Player player, String name) {
        player.modifyMaxHP(1);
        player.healToFull();
        player.addTrialToken();
        player.setCurrentRoomID("EZ-01");
        isSolved = true;
        isFinished = true;
        return "You have completed the " + name + " and have been teleported to the entrance zone!\n"
                + "You get +1 Max HP, Trial Token, full HP restore.";
    }

    private String rewardToEntranceNoChoice(Player player, String name) {
        player.modifyMaxHP(1);
        player.healToFull();
        player.addTrialToken();
        player.setCurrentRoomID("EZ-01");
        isSolved = true;
        isFinished = true;
        return "You have completed the " + name + " and you need to go to entrance zone to receive your reward!\n"
                + "You get +1 Max HP, Trial Token, full HP restore.";
    }
}