public class Puzzle {
    private String puzzleId;
    private String trialName;
    private String roomId;
    private boolean solved;
    private String description;
    private String solution;
    private String hint;

    public Puzzle(String puzzleId, String trialName, String roomId, String description, String solution, String hint) {
        this.puzzleId = puzzleId;
        this.trialName = trialName;
        this.roomId = roomId;
        this.solved = false;
        this.description = description;
        this.solution = solution.toLowerCase();
        this.hint = hint;
    }

    public String getPuzzleId() { return puzzleId; }
    public String getTrialName() { return trialName; }
    public String getRoomId() { return roomId; }
    public boolean isSolved() { return solved; }
    public String getDescription() { return description; }
    public String getHint() { return hint; }

    public void setSolved(boolean solved) { this.solved = solved; }

    public boolean checkSolution(String attempt) {
        return solution.equals(attempt.toLowerCase().trim());
    }

    public String getSolution() { return solution; }
}