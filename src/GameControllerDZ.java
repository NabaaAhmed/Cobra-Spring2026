import java.util.Scanner;

public class GameControllerDZ {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Player player = new Player("EZ-01");

        System.out.println("=== Dan Puzzle Testing Controller ===");

        boolean keepPlaying = true;

        while (keepPlaying) {
            Puzzle puzzle = choosePuzzle(input, player);

            if (puzzle == null) {
                System.out.println("Exiting puzzle tester.");
                break;
            }

            System.out.println();
            System.out.println(puzzle.startPuzzle());
            System.out.println();

            while (!puzzle.isFinished()) {
                System.out.println("Type a command for this puzzle.");
                System.out.println("Type 'help' to see sample commands.");
                System.out.print("> ");

                String command = input.nextLine().trim();

                if (command.equalsIgnoreCase("help")) {
                    printHelp(puzzle.getPuzzleID());
                    continue;
                }

                String result = puzzle.handleCommand(player, command);

                System.out.println();
                System.out.println(result);
                System.out.println();
                System.out.println("Current Room: " + player.getCurrentRoomID());
                System.out.println("Current HP: " + player.getCurrentHP());
                System.out.println("Max HP: " + player.getMaxHP());
                System.out.println("Trial Tokens: " + player.getTrialTokens());
                System.out.println("----------------------------------");
            }

            System.out.println();
            System.out.println("Puzzle test finished.");
            System.out.println("Type 'yes' to test another puzzle, or anything else to quit.");
            System.out.print("> ");
            String again = input.nextLine().trim();

            if (!again.equalsIgnoreCase("yes")) {
                keepPlaying = false;
            }
        }

        input.close();
    }

    private static Puzzle choosePuzzle(Scanner input, Player player) {
        while (true) {
            System.out.println();
            System.out.println("Choose a puzzle to test:");
            System.out.println("1 - Awareness");
            System.out.println("2 - Restraint");
            System.out.println("3 - Trust");
            System.out.println("4 - Sacrifice");
            System.out.println("5 - Commitment");
            System.out.println("6 - Final Trial");
            System.out.println("7 - Trial of Hell");
            System.out.println("Type the number only, or type 'exit' to quit.");
            System.out.print("> ");

            String choice = input.nextLine().trim();

            switch (choice) {
                case "1":
                    player.setCurrentRoomID("AW-02");
                    return new Puzzle("PZ-01", "Awareness", "AW-02", false);
                case "2":
                    player.setCurrentRoomID("RS-02");
                    return new Puzzle("PZ-02", "Restraint", "RS-02", false);
                case "3":
                    player.setCurrentRoomID("TR-02");
                    return new Puzzle("PZ-03", "Trust", "TR-02", false);
                case "4":
                    player.setCurrentRoomID("SC-01");
                    return new Puzzle("PZ-04", "Sacrifice", "SC-01", false);
                case "5":
                    player.setCurrentRoomID("CM-01");
                    return new Puzzle("PZ-05", "Commitment", "CM-01", false);
                case "6":
                    player.setCurrentRoomID("FN-02");
                    return new Puzzle("PZ-06", "Final", "FN-02", false);
                case "7":
                    player.setCurrentRoomID("START-01");
                    return new Puzzle("PZ-07", "Hell", "START-01", false);
                case "exit":
                    return null;
                default:
                    System.out.println("Invalid choice. Please type 1, 2, 3, 4, 5, 6, 7, or exit.");
            }
        }
    }

    private static void printHelp(String puzzleID) {
        System.out.println();
        System.out.println("Sample commands:");

        switch (puzzleID) {
            case "PZ-01":
                System.out.println("take red gem");
                System.out.println("throw red gem");
                System.out.println("yes / no");
                System.out.println("take rubble");
                System.out.println("throw rubble");
                break;

            case "PZ-02":
                System.out.println("take coin");
                System.out.println("interact chest");
                System.out.println("inspect chest");
                System.out.println("open chest");
                System.out.println("yes / no");
                break;

            case "PZ-03":
                System.out.println("attack guardian");
                System.out.println("destroy chest");
                System.out.println("open chest");
                break;

            case "PZ-04":
                System.out.println("take sword");
                System.out.println("move bridge");
                System.out.println("throw sword");
                System.out.println("move forward");
                break;

            case "PZ-05":
                System.out.println("move forward");
                System.out.println("examine item");
                System.out.println("take item");
                System.out.println("use smoke bomb");
                System.out.println("kill pursuer");
                System.out.println("yes / no");
                break;

            case "PZ-06":
                System.out.println("burn chest");
                System.out.println("wait");
                System.out.println("insert explosive device");
                System.out.println("place core fragment");
                System.out.println("step symbol");
                System.out.println("throw final jewel");
                System.out.println("enter unstable teleporter");
                System.out.println("yes / no");
                break;

            case "PZ-07":
                System.out.println("inspect room");
                System.out.println("ignore false items");
                System.out.println("use correct item");
                System.out.println("move correct position");
                System.out.println("activate object");
                System.out.println("yes / no");
                break;

            default:
                System.out.println("No help available.");
        }

        System.out.println();
    }
}