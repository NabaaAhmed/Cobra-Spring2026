import java.util.Scanner;

public class GameControllerNA {

    private Player player;
    private RoomManager roomManager;
    private GameView gameView;
    private FileManager fileManager;

    private Scanner input;
    private boolean isRunning;

    public GameControllerNA(Player player, RoomManager roomManager,
                            GameView gameView, FileManager fileManager) {
        this.player = player;
        this.roomManager = roomManager;
        this.gameView = gameView;
        this.fileManager = fileManager;
        this.input = new Scanner(System.in);
        this.isRunning = true;
    }

    public void startGame() {
        gameView.displayMessage("=== Dungeon of Trials ===");

        boolean menuActive = true;

        while (menuActive) {
            gameView.displayMessage("[1] Start New Game");
            gameView.displayMessage("[2] About");
            gameView.displayMessage("[3] Quit");

            String choice = input.nextLine().trim().toLowerCase();

            switch (choice) {
                case "1":
                case "start":
                    gameView.displayMessage("Starting new game...");
                    menuActive = false;
                    runGameLoop();
                    break;

                case "2":
                case "about":
                    gameView.displayMessage("Dungeon of Trials - simplified text version.");
                    break;

                case "3":
                case "quit":
                    gameView.displayMessage("Goodbye!");
                    menuActive = false;
                    break;

                default:
                    gameView.displayError("Invalid option.");
            }
        }
    }

    private void runGameLoop() {
        while (isRunning) {
            if (roomManager.currentRoom != null) {
                gameView.displayRoom(roomManager.currentRoom);
            }

            gameView.displayMessage("Enter command (type help for commands):");
            String command = input.nextLine().trim();
            processCommand(command);
            updateState();
        }
    }

    public void processCommand(String command) {
        if (command.isEmpty()) {
            gameView.displayError("Please enter a command.");
            return;
        }

        String[] parts = command.split(" ");
        String action = parts[0].toLowerCase();

        switch (action) {
            case "help":
                gameView.displayHelp();
                break;

            case "room":
                if (roomManager.currentRoom != null) {
                    gameView.displayRoom(roomManager.currentRoom);
                } else {
                    gameView.displayError("No room loaded.");
                }
                break;

            case "status":
                gameView.displayStatus(player);
                break;

            case "inventory":
                gameView.displayInventory(player);
                break;

            case "move":
                if (parts.length > 1) {
                    try {
                        int index = Integer.parseInt(parts[1]);
                        roomManager.move(index);
                        player.setCurrentRoomID(roomManager.getRoomId());
                    } catch (NumberFormatException e) {
                        gameView.displayError("Use a room connection number. Example: move 0");
                    }
                } else {
                    gameView.displayError("Move where? Example: move 0");
                }
                break;

            case "wait":
                gameView.displayMessage(player.waitTurn());
                break;

            case "fight":
                startCombat();
                break;

            case "exit":
                isRunning = false;
                gameView.displayMessage("Exiting game...");
                break;

            default:
                gameView.displayError("Invalid command.");
        }
    }

    private void startCombat() {
        Monster monster = new Monster("Goblin", 2);
        Combat combat = new Combat(player, monster);
        combat.startBattle(gameView, input);
    }

    public void updateState() {
        if (!player.isAlive()) {
            gameView.displayMessage("Game Over.");
            isRunning = false;
        }
    }
}