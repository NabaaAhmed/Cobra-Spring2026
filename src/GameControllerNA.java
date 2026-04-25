import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class GameControllerNA {

    private Player player;
    private RoomManager roomManager;
    private GameView gameView;
    private Scanner input;
    private boolean isRunning;

    private Set<String> solvedPuzzles;
    private Set<String> defeatedMonsterRooms;

    public GameControllerNA(Player player, RoomManager roomManager, GameView gameView) {
        this.player = player;
        this.roomManager = roomManager;
        this.gameView = gameView;
        this.input = new Scanner(System.in);
        this.isRunning = true;
        this.solvedPuzzles = new HashSet<>();
        this.defeatedMonsterRooms = new HashSet<>();
    }

    public void startGame() {
        gameView.displayMessage("=== Dungeon of Trials ===");

        boolean menuActive = true;

        while (menuActive) {
            gameView.displayMessage("[1] Start New Game");
            gameView.displayMessage("[2] Load Game");
            gameView.displayMessage("[3] About");
            gameView.displayMessage("[4] Quit");
            gameView.displayMessage("\nEnter your choice (1-4):");

            String choice = input.nextLine().trim().toLowerCase();

            switch (choice) {
                case "1":
                case "start":
                    gameView.displayMessage("Starting new game...");
                    menuActive = false;
                    runGameLoop();
                    break;

                case "2":
                case "load":
                    Player loaded = FileManager.loadPlayer("save.txt");
                    if (loaded != null) {
                        player = loaded;
                        roomManager.setRoom(player.getCurrentRoomID());
                        solvedPuzzles = new HashSet<>();
                        defeatedMonsterRooms = new HashSet<>();
                        gameView.displayMessage("Game Loaded!");
                        menuActive = false;
                        runGameLoop();
                    } else {
                        gameView.displayError("Could not load save file.");
                    }
                    break;

                case "3":
                case "about":
                    gameView.displayMessage("Dungeon of Trials\nTeam Cobra\nText-based adventure game.");
                    break;

                case "4":
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
            roomManager.setRoom(player.getCurrentRoomID());
            gameView.displayRoom(roomManager.getCurrentRoom());
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
                gameView.displayRoom(roomManager.getCurrentRoom());
                break;

            case "status":
                gameView.displayStatus(player);
                break;

            case "inventory":
                gameView.displayInventory(player);
                break;

            case "move":
                handleMove(parts);
                break;

            case "take":
                handleTake(command);
                break;

            case "drop":
                handleDrop(command);
                break;

            case "use":
                handleUse(command);
                break;

            case "fight":
                startCombat();
                break;

            case "puzzle":
                startPuzzle();
                break;

            case "save":
                FileManager.savePlayer("save.txt", player);
                gameView.displayMessage("Game saved.");
                break;

            case "load":
                Player loaded = FileManager.loadPlayer("save.txt");
                if (loaded != null) {
                    player = loaded;
                    roomManager.setRoom(player.getCurrentRoomID());
                    solvedPuzzles = new HashSet<>();
                    defeatedMonsterRooms = new HashSet<>();
                    gameView.displayMessage("Game loaded.");
                } else {
                    gameView.displayError("Could not load save file.");
                }
                break;

            case "exit":
                isRunning = false;
                gameView.displayMessage("Exiting game...");
                break;

            default:
                gameView.displayError("Invalid command.");
        }
    }

    private void handleMove(String[] parts) {
        if (parts.length <= 1) {
            gameView.displayError("Move where? Example: move 0");
            return;
        }

        try {
            int index = Integer.parseInt(parts[1]);

            Room current = roomManager.getCurrentRoom();
            if (index < 0 || index >= current.getConnections().size()) {
                gameView.displayError("Invalid room connection.");
                return;
            }

            Room target = current.getConnections().get(index);
            String targetId = target.getRoomId();

            if ((targetId.equals("EZ-02") || targetId.equals("FN-02")) && player.getTrialTokens() < 5) {
                gameView.displayError("That area is not unlocked yet. You need 5 Trial Tokens.");
                return;
            }

            roomManager.move(index);
            player.setCurrentRoomID(roomManager.getRoomId());
        } catch (NumberFormatException e) {
            gameView.displayError("Use a connection number. Example: move 0");
        }
    }

    private void handleTake(String command) {
        if (command.length() <= 5) {
            gameView.displayError("Take what?");
            return;
        }

        String itemName = command.substring(5).trim();
        Room room = roomManager.getCurrentRoom();
        Item item = room.takeItemByName(itemName);

        if (item == null) {
            gameView.displayError("That item is not in this room.");
            return;
        }

        player.addItem(item);
        gameView.displayMessage("You picked up: " + item.getitemName());
    }

    private void handleDrop(String command) {
        if (command.length() <= 5) {
            gameView.displayError("Drop what?");
            return;
        }

        String itemName = command.substring(5).trim();
        Item item = player.findItemByName(itemName);

        if (item == null) {
            gameView.displayError("You do not have that item.");
            return;
        }

        player.removeItem(item);
        roomManager.getCurrentRoom().addItem(item);
        gameView.displayMessage("You dropped: " + item.getitemName());
    }

    private void handleUse(String command) {
        if (command.length() <= 4) {
            gameView.displayError("Use what?");
            return;
        }

        String itemName = command.substring(4).trim();
        Item item = player.findItemByName(itemName);

        if (item == null) {
            gameView.displayError("You do not have that item.");
            return;
        }

        item.use(player);

        if (item instanceof Potion) {
            player.removeItem(item);
            gameView.displayMessage("You used a potion.");
        } else {
            gameView.displayMessage("You used " + item.getitemName() + ".");
        }
    }

    private void startCombat() {
        String roomID = player.getCurrentRoomID();

        if (defeatedMonsterRooms.contains(roomID)) {
            gameView.displayMessage("There is no monster here.");
            return;
        }

        Monster monster = getMonsterForCurrentRoom(roomID);

        if (monster == null) {
            gameView.displayMessage("There is no monster here.");
            return;
        }

        Combat combat = new Combat(player, monster);
        combat.startBattle(gameView, input);

        if (!player.isAlive()) {
            isRunning = false;
            return;
        }

        if (!combat.isMonsterAlive()) {
            defeatedMonsterRooms.add(roomID);
            Item reward = monster.dropReward();

            if (reward != null) {
                player.addItem(reward);
                gameView.displayMessage("Monster dropped: " + reward.getitemName());
            }
        }
    }

    private Monster getMonsterForCurrentRoom(String roomID) {
        switch (roomID) {
            case "TR-02":
                return new Monster("M-02", "Guardian", 2, 1, "Silver Sigil/Emerald Fragment");
            case "SC-02":
                return new Monster("M-03", "Golem", 2, 1, "Gold Chain");
            case "TP-TRAP-01":
                return new Monster("M-07", "Warden", 2, 1, "Potion");
            case "FN-02":
                return new Monster("M-09", "Final Trial Stalker", 5, 1, "Stalker Corpse");
            default:
                return null;
        }
    }

    private void startPuzzle() {
        Puzzle puzzle = getPuzzleForCurrentRoom(player.getCurrentRoomID());

        if (puzzle == null) {
            gameView.displayMessage("There is no puzzle in this room.");
            return;
        }

        if (solvedPuzzles.contains(puzzle.getPuzzleID())) {
            gameView.displayMessage("This puzzle is already solved.");
            return;
        }

        gameView.displayMessage(puzzle.startPuzzle());

        while (!puzzle.isFinished()) {
            gameView.displayMessage("Enter puzzle command:");
            String command = input.nextLine().trim();

            if (command.equalsIgnoreCase("help")) {
                gameView.displayMessage("Type the puzzle actions exactly as needed.");
                continue;
            }

            String result = puzzle.handleCommand(player, command);
            gameView.displayMessage(result);
        }

        if (puzzle.isSolved()) {
            solvedPuzzles.add(puzzle.getPuzzleID());
        }

        roomManager.setRoom(player.getCurrentRoomID());
    }

    private Puzzle getPuzzleForCurrentRoom(String roomID) {
        switch (roomID) {
            case "AW-02":
                return new Puzzle("PZ-01", "Awareness", "AW-02", false);
            case "RS-02":
                return new Puzzle("PZ-02", "Restraint", "RS-02", false);
            case "TR-02":
                return new Puzzle("PZ-03", "Trust", "TR-02", false);
            case "SC-01":
                return new Puzzle("PZ-04", "Sacrifice", "SC-01", false);
            case "CM-07":
                return new Puzzle("PZ-05", "Commitment", "CM-07", false);
            case "FN-02":
                return new Puzzle("PZ-06", "Final", "FN-02", false);
            default:
                return null;
        }
    }

    public void updateState() {
        if (!player.isAlive()) {
            gameView.displayMessage("Game Over.");
            isRunning = false;
        }
    }
}