public abstract class Puzzle {
    protected String puzzleID;
    protected String trialName;
    protected String roomID;
    protected boolean isSolved;
    protected boolean isFinished;
    protected boolean trialComplete;
    protected boolean rewardEarned;

    public Puzzle(String puzzleID, String trialName, String roomID) {
        this.puzzleID = puzzleID;
        this.trialName = trialName;
        this.roomID = roomID;
        this.isSolved = false;
        this.isFinished = false;
        this.trialComplete = false;
        this.rewardEarned = false;
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

    public boolean isTrialComplete() {
        return trialComplete;
    }

    public boolean isRewardEarned() {
        return rewardEarned;
    }

    protected String completeWithReward(Player player, String completionMessage) {
        player.modifyMaxHP(1);
        player.heal(player.getMaxHP());
        player.addTrialToken();
        player.setCurrentRoomID("EZ-01");
        isSolved = true;
        isFinished = true;
        trialComplete = true;
        rewardEarned = true;

        return completionMessage
                + "\nYou have completed the " + trialName + " and have been teleported to the entrance zone!"
                + "\nYou get +1 Max HP, Trial Token, full HP restore.";
    }

    protected String completeNoReward(Player player, String completionMessage) {
        player.setCurrentRoomID("EZ-01");
        isFinished = true;
        trialComplete = true;
        rewardEarned = false;
        return completionMessage;
    }

    public abstract String startPuzzle();

    public abstract String getHint();

    public abstract String handleCommand(Player player, String command);
}
