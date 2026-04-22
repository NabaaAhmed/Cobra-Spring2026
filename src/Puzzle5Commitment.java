//Danny
    public class Puzzle5Commitment {
        private final String puzzleID;
        private final String trialName;
        private final String roomID;

        private boolean isSolved;
        private boolean isFinished;

        private int forwardMoves;
        private int takeCount;

        public Puzzle5Commitment() {
            this.puzzleID = "PZ-05";
            this.trialName = "Commitment";
            this.roomID = "CM-01";

            this.isSolved = false;
            this.isFinished = false;

            this.forwardMoves = 0;
            this.takeCount = 0;
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
            return "==== Welcome to the Trial of Commitment =====\n"
                    + "You hear heavy footsteps echoing from the entrance... Commit to your path. Do not linger.\n"
                    + "Hint: The Pursuer is two rooms behind you. Every time you stop to examine or take an item, it gets closer.";
        }

        public String handleCommand(Player player, String command) {
            if (command == null) {
                return "Invalid command.";
            }

            if (isFinished) {
                return "This puzzle is already finished.";
            }

            String cmd = command.trim().toLowerCase();

            if (cmd.equals("move forward")) {
                forwardMoves++;

                if (forwardMoves >= 6) {
                    isSolved = true;
                    isFinished = true;

                    player.modifyMaxHP(1);
                    player.healToFull();
                    player.addTrialToken();
                    player.setCurrentRoomID("EZ-01");

                    return "You stayed the course. Commitment proven.\n"
                            + "You have completed the Trial of Commitment and have been teleported to the entrance zone!\n"
                            + "You get +1 Max HP, Trial Token, full HP restore.";
                }

                return "You move forward.\n"
                        + "Progress: " + forwardMoves + "/6";
            }

            if (cmd.equals("examine item") || cmd.equals("inspect item")) {
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

                return "You stop to take an item.\n"
                        + "The Pursuer gets closer!";
            }

            if (cmd.equals("kill pursuer")) {
                player.takeDamage(1);
                player.setCurrentRoomID("EZ-01");
                isFinished = true;

                return "You killed the Pursuer. BOOM! The Pursuer implodes, destroying the path forward and you lose 1 HP.\n"
                        + "You have completed Trial of Commitment (No Reward).";
            }

            return "That command does not work in this puzzle. Try something else.";
        }
    }

