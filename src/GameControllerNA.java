import java.util.Scanner;

public class GameControllerNA {

    // Fields
    private Player player;
    private RoomManager roomManager;
    private GameView gameView;
    private FileManager saveLoad;

    private Scanner input;
    private boolean isRunning;

    // Constructor
    public GameControllerNA(Player player, RoomManager roomManager,
                            GameView gameView, FileManager saveLoad) {
        this.player = player;
        this.roomManager = roomManager;
        this.gameView = gameView;
        this.saveLoad = saveLoad;

        this.input = new Scanner(System.in);
        this.isRunning = true;
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
                    Player loaded = saveLoad.loadGame();
                    if (loaded != null) {
                        player = loaded;
                        roomManager.setRoom(player.getCurrentRoomID());
                    }
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
                gameView.displayMessage("HP: " + player.getCurrentHP() + "/" + player.getMaxHP());
                break;

            case "inventory":
                gameView.displayInventory(player);
                break;

            case "inspect":
                Monster monster = roomManager.getCurrentRoom().getMonster();

                if (monster == null) {
                    gameView.displayMessage("No Monsters detected");
                } else {
                    String result = monster.getName() + " (HP: " + monster.getHp() + ")";
                    gameView.displayMessage(result);
                    gameView.displayMessage("Type 'engage' to fight or anything else to ignore:");
                    String choice = input.nextLine();

                    if (choice.equals("engage")) {
                        startCombat(monster);
                    }
                }
                break;

            case "move":
                if (parts.length > 1) {
                    if (!roomManager.moveToRoom(parts[1])) {
                        gameView.displayError("Can't move there.");
                    } else {
                        player.setCurrentRoomID(roomManager.getRoomId());
                    }
                } else {
                    gameView.displayError("Move where?");
                }
                break;

            case "equip":
                if (parts.length > 1) {
                    String itemName = command.substring(6).trim();
                    boolean success = false;

                    for (Item item : player.getInventory()) {
                        if (item.getName().equalsIgnoreCase(itemName)) {
                            item.use(player);
                            success = true;
                            break;
                        }
                    }

                    if (success) {
                        gameView.displayMessage("Equipped " + itemName);
                    } else {
                        gameView.displayError("Item not found.");
                    }
                }
                break;

            case "save":
                FileManager.saveGame(player);
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
    private void startCombat(Monster monster) {

        if (monster == null) {
            gameView.displayError("There is nothing to fight.");
            return;
        }

        Combat combat = new Combat(player);
        combat.resetEngine(monster);

        gameView.displayCombat("A " + monster.getName() + " appears!");

        while (!combat.isBattleOver()) {
            gameView.displayCombat("Turn: " + combat.getTurns());
            String command = input.nextLine();

            if (command.equalsIgnoreCase("help")) {
                gameView.displayMessage("Attack / Wait / Retreat");
                continue;
            }

            String result = combat.action(command);
            gameView.displayCombat(result);
        }

        if (!monster.isAlive()) {
            gameView.displayMessage("You won!");
            roomManager.getCurrentRoom().setMonster(null);
        } else if (!player.isAlive()) {
            gameView.displayMessage("You died...");
            isRunning = false;
        } else {
            gameView.displayMessage("You fled the battle.");
        }

        if (!player.isAlive()) {
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