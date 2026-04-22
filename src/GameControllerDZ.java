import java.util.Scanner;

public class GameControllerDZ {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        GameView view = new GameView();

        boolean isRunning = true;

        while (isRunning) {
            Player player = new Player("EZ-01");

            System.out.println();
            System.out.println("=== Dungeon of Trials ===");
            System.out.println("Choose a trial to enter:");
            System.out.println("3 - Trial of Trust");
            System.out.println("4 - Trial of Sacrifice");
            System.out.println("5 - Trial of Commitment");
            System.out.println("Type 3, 4, 5, or exit.");
            System.out.print("> ");

            String choice = input.nextLine().trim();

            if (choice.equalsIgnoreCase("exit")) {
                System.out.println("Exiting game.");
                isRunning = false;
                continue;
            }

            if (choice.equals("3")) {
                player.setCurrentRoomID("TR-02");
                Puzzle3Trust puzzle = new Puzzle3Trust();
                runPuzzle3(input, player, puzzle, view);

            } else if (choice.equals("4")) {
                player.setCurrentRoomID("SC-01");
                Puzzle4Sacrifice puzzle = new Puzzle4Sacrifice();
                runPuzzle4(input, player, puzzle, view);

            } else if (choice.equals("5")) {
                player.setCurrentRoomID("CM-01");
                Puzzle5Commitment puzzle = new Puzzle5Commitment();
                runPuzzle5(input, player, puzzle, view);

            } else {
                System.out.println("Invalid choice.");
            }
        }

        input.close();
    }

    private static void runPuzzle3(Scanner input, Player player, Puzzle3Trust puzzle, GameView view) {
        System.out.println();
        System.out.println(puzzle.startPuzzle());
        System.out.println();

        while (!puzzle.isFinished()) {
            System.out.println("Enter a command. Type 'help' for trial commands.");
            System.out.print("> ");

            String command = input.nextLine().trim();

            if (command.equalsIgnoreCase("help")) {
                System.out.println("attack guardian");
                System.out.println("inspect chest");
                System.out.println("destroy chest");
                System.out.println("open chest");
                continue;
            }

            String result = puzzle.handleCommand(player, command);

            System.out.println();
            System.out.println(result);
            System.out.println();
            printPlayerStatus(player);
        }

        if (puzzle.isCombatTriggered()) {
            System.out.println();
            System.out.println("Punishment combat has started.");

            Combat combat = new Combat(player, puzzle.getFailureMonster());
            combat.startBattle(view, input);

            if (player.isAlive()) {
                player.setCurrentRoomID("EZ-01");
                System.out.println("You have completed Trial of Trust (No Reward).");
            } else {
                System.out.println("You died during the Trial of Trust.");
            }

            System.out.println();
            printPlayerStatus(player);
        }
    }

    private static void runPuzzle4(Scanner input, Player player, Puzzle4Sacrifice puzzle, GameView view) {
        System.out.println();
        System.out.println(puzzle.startPuzzle());
        System.out.println();

        while (!puzzle.isFinished()) {
            System.out.println("Enter a command. Type 'help' for trial commands.");
            System.out.print("> ");

            String command = input.nextLine().trim();

            if (command.equalsIgnoreCase("help")) {
                System.out.println("take sword");
                System.out.println("move bridge");
                System.out.println("throw sword");
                System.out.println("inspect bridge");
                System.out.println("move forward");
                continue;
            }

            String result = puzzle.handleCommand(player, command);

            System.out.println();
            System.out.println(result);
            System.out.println();
            printPlayerStatus(player);
        }

        if (puzzle.isCombatTriggered()) {
            System.out.println();
            System.out.println("Punishment combat has started.");

            Combat combat = new Combat(player, puzzle.getFailureMonster());
            combat.startBattle(view, input);

            if (player.isAlive()) {
                player.setCurrentRoomID("EZ-01");
                System.out.println("You have completed Trial of Sacrifice (No Reward).");
            } else {
                System.out.println("You died during the Trial of Sacrifice.");
            }

            System.out.println();
            printPlayerStatus(player);
        }
    }

    private static void runPuzzle5(Scanner input, Player player, Puzzle5Commitment puzzle, GameView view) {
        System.out.println();
        System.out.println(puzzle.startPuzzle());
        System.out.println();

        while (!puzzle.isFinished()) {
            System.out.println("Enter a command. Type 'help' for trial commands.");
            System.out.print("> ");

            String command = input.nextLine().trim();

            if (command.equalsIgnoreCase("help")) {
                System.out.println("move forward");
                System.out.println("examine item");
                System.out.println("take item");
                System.out.println("kill pursuer");
                continue;
            }

            String result = puzzle.handleCommand(player, command);

            System.out.println();
            System.out.println(result);
            System.out.println();
            printPlayerStatus(player);
        }
    }

    private static void printPlayerStatus(Player player) {
        System.out.println("Current Room: " + player.getCurrentRoomID());
        System.out.println("Current HP: " + player.getCurrentHP());
        System.out.println("Max HP: " + player.getMaxHP());
        System.out.println("Trial Tokens: " + player.getTrialTokens());

        System.out.println("Inventory:");
        if (player.getInventory().isEmpty()) {
            System.out.println("- empty");
        } else {
            for (Item item : player.getInventory()) {
                System.out.println("- " + item.getitemName());
            }
        }

        System.out.println("----------------------------------");
    }
}