//Mai
public abstract class Puzzle
{
    private String puzzleID;
    private String trialName;
    private String roomID;
    private String description;
    private String hint;
    private boolean isSolved;

    public Puzzle(String puzzleID, String trialName, String roomID, String description, String hint, boolean isSolved) {
        this.puzzleID = puzzleID;
        this.trialName = trialName;
        this.roomID = roomID;
        this.description = description;
        this.hint = hint;
        this.isSolved = false;
    }

    public String getDescription() {
        return description;
    }

    public String getHint() {
        return hint;
    }

    public boolean isSolved() {
        return isSolved;
    }

    public abstract void play(Player player, String command56);

    public void applyWin(Player player) {
        this.isSolved = true;
        player.modifyMaxHP(1);
        player.heal(player.getMaxHP());
        player.addTrialToken();
        System.out.println("You have completed the " + this.trialName + " and gained 1 max HP, 1 trial token and HP restore! Your current max HP is now " + player.getMaxHP() + ".");
    }
}
