public class Puzzle3Trust extends Puzzle {
    private boolean guardianBroken;
    private boolean chestAppeared;

    public Puzzle3Trust() {
        super("PZ-03", "Trust", "TR-03",
                "A guardian statue watches your every move.",
                "attack guardian then destroy chest",
                "Destroy the guardian, then destroy the chest.");

        this.guardianBroken = false;
        this.chestAppeared = false;
    }

    @Override
    public String startPuzzle() {
        String result = "\n===== Trial of Trust =====\n";
        result += "A guardian statue watches you.\n";
        result += "Hint: " + getHint();
        return result;
    }

    @Override
    public String handleCommand(Player player, String command) {
        if (command == null) return "Invalid command.";
        if (isFinished()) return "Trial already complete.";

        String cmd = command.trim().toLowerCase();

        if (cmd.equals("attack guardian")) {
            if (guardianBroken) return "Guardian already broken.";
            guardianBroken = true;
            chestAppeared = true;
            return "Guardian breaks! A chest appears.";
        }

        if (cmd.equals("destroy chest")) {
            if (!guardianBroken) return "Break the guardian first.";
            setSolved(true);
            setFinished(true);
            completePuzzle(player);
            return "You made the right choice! Trial of Trust Complete!\n+1 Max HP, Token, Full Heal!";
        }

        if (cmd.equals("open chest")) {
            if (!chestAppeared) return "No chest here.";
            player.takeDamage(1);

            Monster guardian = new Monster("M-02", "Guardiaan", 2, 1, true);
            failPuzzle(player, guardian);
            return "Guardian reforms! You lose 1 HP! Combat begins!";
        }

        return "Invalid command. Try: attack guardian, destroy chest, open chest";
    }
}