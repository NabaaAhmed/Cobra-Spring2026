public class GameResult {
    private String message;
    private boolean success;
    private boolean gameOver;
    private boolean puzzleStarted;
    private boolean combatStarted;
    private boolean puzzleFinished;
    private Monster monster;
    private Puzzle puzzle;

    public GameResult(String message) {
        this.message = message;
        this.success = true;
        this.gameOver = false;
        this.puzzleStarted = false;
        this.combatStarted = false;
        this.puzzleFinished = false;
        this.monster = null;
        this.puzzle = null;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isPuzzleStarted() {
        return puzzleStarted;
    }

    public boolean isCombatStarted() {
        return combatStarted;
    }

    public boolean isPuzzleFinished() {
        return puzzleFinished;
    }

    public Monster getMonster() {
        return monster;
    }

    public Puzzle getPuzzle() {
        return puzzle;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void setPuzzleStarted(boolean puzzleStarted) {
        this.puzzleStarted = puzzleStarted;
    }

    public void setCombatStarted(boolean combatStarted) {
        this.combatStarted = combatStarted;
    }

    public void setPuzzleFinished(boolean puzzleFinished) {
        this.puzzleFinished = puzzleFinished;
    }

    public void setMonster(Monster monster) {
        this.monster = monster;
    }

    public void setPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
    }
}