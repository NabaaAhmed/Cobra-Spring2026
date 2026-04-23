public abstract class Puzzle {
    protected String puzzleId;
    protected String trialName;
    protected String roomId;
    protected boolean solved;
    protected boolean finished;
    protected String description;
    protected String solution;
    protected String hint;
    protected boolean combatTriggered;
    protected Monster failureMonster;

    public Puzzle(String puzzleId, String trialName, String roomId, String description, String solution, String hint) {
        this.puzzleId = puzzleId;
        this.trialName = trialName;
        this.roomId = roomId;
        this.solved = false;
        this.finished = false;
        this.description = description;
        this.solution = solution != null ? solution.toLowerCase() : "";
        this.hint = hint;
        this.combatTriggered = false;
        this.failureMonster = null;
    }

    // Getters
    public String getPuzzleId() { return puzzleId; }
    public String getTrialName() { return trialName; }
    public String getRoomId() { return roomId; }
    public boolean isSolved() { return solved; }
    public boolean isFinished() { return finished; }
    public String getDescription() { return description; }
    public String getHint() { return hint; }
    public boolean isCombatTriggered() { return combatTriggered; }
    public Monster getFailureMonster() { return failureMonster; }

    // Setters
    public void setSolved(boolean solved) { this.solved = solved; }
    public void setFinished(boolean finished) { this.finished = finished; }
    public void setCombatTriggered(boolean triggered) { this.combatTriggered = triggered; }
    public void setFailureMonster(Monster monster) { this.failureMonster = monster; }

    public boolean checkSolution(String attempt) {
        return solution.equals(attempt.toLowerCase().trim());
    }

    public String getSolution() { return solution; }

    // Abstract methods to be implemented by subclasses
    public abstract String startPuzzle();
    public abstract String handleCommand(Player player, String command);

    // Template method for completing puzzle
    protected void completePuzzle(Player player) {
        if (finished) return;

        if (solved) {
            player.modifyMaxHP(1);
            player.fullHeal();
            player.addTrialToken();
            player.setCurrentRoomID("EZ-01");
        }
        finished = true;
    }

    protected void failPuzzle(Player player, Monster monster) {
        this.combatTriggered = true;
        this.failureMonster = monster;
        this.finished = true;
    }
}