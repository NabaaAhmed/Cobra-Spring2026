//mai
public abstract class Puzzle {
    protected String puzzleId;
    protected String trialName;
    protected String roomId;
    protected boolean isSolved;
    protected boolean isFinished;
    protected boolean trialComplete;
    protected boolean rewardEarned;

    public Puzzle(String puzzleId, String trialName, String roomId) {
        this.puzzleId = puzzleId;
        this.trialName = trialName;
        this.roomId = roomId;
        this.isSolved = false;
        this.isFinished = false;
        this.trialComplete = false;
        this.rewardEarned = false;
    }

    public String getPuzzleId() {
        return puzzleId;
    }

    public String getPuzzleID() {
        return getPuzzleId();
    }

    public String getTrialName() {
        return trialName;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getRoomID() {
        return getRoomId();
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
        isSolved = true;
        isFinished = true;
        trialComplete = true;
        rewardEarned = true;

        return completionMessage
                + "\nYou have completed the " + trialName + "!"
                + "\nYou get +1 Max HP, Trial Token, full HP restore.";
    }

    protected String completeNoReward(Player player, String completionMessage) {
        isFinished = true;
        trialComplete = true;
        rewardEarned = false;
        return completionMessage;
    }

    public abstract String startPuzzle();

    public abstract String getHint();

    public abstract String handleCommand(Player player, String command);
}
