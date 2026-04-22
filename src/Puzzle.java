//Mai
public abstract class Puzzle
{

    private String puzzleID;
    private String trialName;
    private String roomID;
    private String description;
    private String hint;
    private boolean isCompleted;

    public Puzzle(String puzzleID, String trialName, String roomID, String description, String hint, boolean isCompleted) {
        this.puzzleID = puzzleID;
        this.trialName = trialName;
        this.roomID = roomID;
        this.description = description;
        this.hint = hint;
    }

    public abstract void play(Player player);

    public void applyWin(Player player) {
        this.isCompleted = true;
        player.setMaxHP(player.getMaxHP() + 1);
        player.setCurrentHP(player.getMaxHP());
        System.out.println("You have completed the Trial and gained 1 max HP, 1 trial token and HP restore! Your current max HP is now " + player.getMaxHP() + ".");
    }
}
