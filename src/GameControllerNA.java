import java.util.Scanner;

public class GameControllerNA {

    // Fields
    private Player player;
    private RoomManager roomManager;
    private GameView gameView;
    private FileManager saveLoad;

    private Scanner input;
    private boolean isRunning;

    // Optional (from your example)
    private CombatEngine combatEngine;

    // Constructor
    public GameControllerNA(Player player, RoomManager roomManager,
                            GameView gameView, FileManager saveLoad) {
        this.player = player;
        this.roomManager = roomManager;
        this.gameView = gameView;
        this.saveLoad = saveLoad;

        this.input = new Scanner(System.in);
        this.isRunning = true;
        this.combatEngine = new CombatEngine(player);
    }

    public GameControllerNA(Player player, RoomManager roomManager, GameView view, FileManager fileManager) {
    }

    // =========================
    // START GAME (MENU)
    // =========================
    public void startGame() {

        gameView.displayMessage("=== Dungeon of Trials ===");

        boolean menuActive = true;

        while (menuActive) {
            gameView.displayMessage("[1] Start New Game");
            gameView.displayMessage("[2] Load Game");
            gameView.displayMessage("[3] Quit");

            String choice = input.nextLine().toLowerCase();

            switch (choice) {
                case "1":
                case "start":
                    gameView.displayMessage("Starting new game...");
                    runGameLoop();
                    menuActive = false;
                    break;

                case "2":
                case "load":
                    player = saveLoad.loadGame();
                    gameView.displayMessage("Game Loaded!");
                    runGameLoop();
                    menuActive = false;
                    break;

                case "3":
                case "quit":
                    gameView.displayMessage("Goodbye!");
                    System.exit(0);
                    break;

                default:
                    gameView.displayError("Invalid option.");
            }
        }
    }

    // =========================
    // MAIN GAME LOOP
    // =========================
    private void runGameLoop() {

        while (isRunning) {

            gameView.displayRoom(roomManager.getCurrentRoom());
            gameView.displayMessage("Enter command:");

            String command = input.nextLine();
            processCommand(command);
            updateState();
        }
    }

    // =========================
    // COMMAND HANDLER
    // =========================
    public void processCommand(String command) {

        String[] parts = command.split(" ");
        String action = parts[0].toLowerCase();

        switch (action) {

            case "status":
                gameView.displayMessage("HP: " + player.getHealth());
                break;

            case "inventory":
                gameView.displayInventory(player);
                break;

            case "inspect":
                String result = player.inspectMonster();
                gameView.displayMessage(result);

                if (!result.equals("No Monsters detected")) {
                    gameView.displayMessage("Type 'engage' to fight or anything else to ignore:");
                    String choice = input.nextLine();

                    if (choice.equals("engage")) {
                        startCombat();
                    }
                }
                break;

            case "move":
                if (parts.length > 1) {
                    roomManager.moveToRoom(parts[1]);
                } else {
                    gameView.displayError("Move where?");
                }
                break;

            case "equip":
                if (parts.length > 1) {
                    String itemName = command.substring(6);
                    boolean success = player.equipWeapon(itemName);

                    if (success) {
                        gameView.displayMessage("Equipped " + itemName);
                    } else {
                        gameView.displayError("Item not found.");
                    }
                }
                break;

            case "save":
                saveLoad.saveGame(player);
                gameView.displayMessage("Game saved.");
                break;

            case "exit":
                isRunning = false;
                break;

            default:
                gameView.displayError("Invalid command.");
        }
    }

    // =========================
    // COMBAT SYSTEM
    // =========================
    private void startCombat() {

        combatEngine.resetEngine();

        while (!combatEngine.isBattleOver()) {

            gameView.displayCombat("Turn: " + combatEngine.getTurns());

            String command = input.nextLine();

            if (command.equals("help")) {
                gameView.displayMessage("Attack / Defend / Run");
                continue;
            }

            String result = combatEngine.action(command);
            gameView.displayCombat(result);
        }

        if (!combatEngine.getMonsterAlive()) {
            gameView.displayMessage("You won!");
        } else if (!player.isAlive()) {
            gameView.displayMessage("You died...");
            isRunning = false;
        }
    }


    public void updateState() {

        if (!player.isAlive()) {
            gameView.displayMessage("Game Over.");
            isRunning = false;
        }
    }
}