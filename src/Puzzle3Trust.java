public class Puzzle3Trust extends Puzzle {
    private boolean guardianBroken;
    private boolean chestAppeared;

    public Puzzle3Trust() {
        super("PZ-03", "Trust", "TR-03",
                "A guardian statue watches your every move.\n" +
                        "The statue seems to be protecting something, but what?",
                "attack guardian then destroy chest",
                "The guardian must be destroyed to reveal the truth. Then destroy the chest.");

        this.guardianBroken = false;
        this.chestAppeared = false;
    }

    @Override
    public String startPuzzle() {
        return "==== Welcome to the Trial of Trust ====\n" +
                "You stand before a guardian statue... it watches your every move.\n" +
                "Hint: Not everything should be taken at face value.\n" +
                "Sometimes trust must be placed in action, not reward.";
    }

    @Override
    public String handleCommand(Player player, String command) {
        if (command == null) return "Invalid command.";
        if (finished) return "This puzzle is already finished.";

        String cmd = command.trim().toLowerCase();

        if (cmd.equals("attack guardian") || cmd.equals("attack guardian statue")) {
            if (guardianBroken) return "The guardian statue is already broken.";
            guardianBroken = true;
            chestAppeared = true;
            return "The guardian breaks.\nA chest appears.";
        }

        if (cmd.equals("inspect chest") || cmd.equals("examine chest")) {
            if (!chestAppeared) return "There is no chest here yet.";
            return "The chest looks tempting, but something feels wrong about it.";
        }

        if (cmd.equals("destroy chest")) {
            if (!guardianBroken) return "You need to break the guardian first.";
            solved = true;
            finished = true;
            completePuzzle(player);
            return "You saw through the illusion and made the right choice.\n" +
                    "You have completed the Trial of Trust!\n" +
                    "You get +1 Max HP, Trial Token, full HP restore!\n" +
                    "You are teleported back to the Entrance Zone.";
        }

        if (cmd.equals("open chest")) {
            if (!chestAppeared) return "There is no chest here to open.";
            player.takeDamage(1);
            Monster guardian = new Monster("M-TRUST", "Guardian", 2, 1, false);
            failPuzzle(player, guardian);
            return "The guardian reforms and attacks you!\n" +
                    "You lose 1 HP.\n" +
                    "Combat begins!";
        }

        if (cmd.equals("hint")) {
            return hint;
        }

        return "Nothing important happens. Try: attack guardian, inspect chest, destroy chest, open chest";
    }
}