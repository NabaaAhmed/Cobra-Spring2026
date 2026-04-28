//team
import java.util.Scanner;

public class GameController {
    private GameModel model;
    private GameView view;
    private Scanner input;
    private boolean isRunning;

    public GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;
        this.input = new Scanner(System.in);
        this.isRunning = true;
    }

    public void startGame() {
        printFreshGameStart();

        while (isRunning) {
            view.displayMessage("");
            view.displayMessage("Enter command:");
            System.out.print("> ");

            String command = input.nextLine().trim();

            if (model.hasActivePuzzle()) {
                handlePuzzleLoop(command);
            } else {
                handleMainCommand(command);
            }

            if (!model.getPlayer().isAlive()) {
                handlePlayerDeath();
            }
        }
    }

    private void printFreshGameStart() {
        view.displayIntro();
        view.displayMessage("");
        view.displayMessage(model.roomHeader().getMessage());
    }

    private void handlePlayerDeath() {
        view.displayMessage("");
        view.displayMessage("You died.");

        while (true) {
            view.displayMessage("Restart from the beginning? yes/no");
            System.out.print("> ");

            String choice = input.nextLine().trim().toLowerCase();

            if (choice.equals("yes") || choice.equals("y")) {
                restartGame();
                return;
            }

            if (choice.equals("no") || choice.equals("n")) {
                view.displayMessage("Game over.");
                isRunning = false;
                return;
            }

            view.displayError("Please type yes or no.");
        }
    }

    private void handleRestartCommand() {
        while (true) {
            view.displayMessage("Restart from the beginning? yes/no");
            System.out.print("> ");

            String choice = input.nextLine().trim().toLowerCase();

            if (choice.equals("yes") || choice.equals("y")) {
                restartGame();
                return;
            }

            if (choice.equals("no") || choice.equals("n")) {
                view.displayMessage("Restart cancelled.");
                return;
            }

            view.displayError("Please type yes or no.");
        }
    }

    private void restartGame() {
        Player newPlayer = new Player("EZ-01");
        RoomManager newRoomManager = new RoomManager();

        this.model = new GameModel(newPlayer, newRoomManager);

        view.displayMessage("");
        view.displayMessage("Restarting game...");
        view.displayMessage("");

        printFreshGameStart(); //prints intro
    }

    private void handleMainCommand(String command) {
        if (command.isEmpty()) {
            view.displayError("Please enter a command.");
            return;
        }

        String action = command.split(" ")[0].toLowerCase(); //takes first word of command as action
        GameResult result;

        switch (action) {
            case "help":
                view.displayMainHelp();
                return;

            case "explore":
                if (command.equalsIgnoreCase("explore") || command.equalsIgnoreCase("explore room")) {
                    view.displayMessage(model.exploreRoom().getMessage());
                } else {
                    view.displayError("Invalid command. Use 'explore room'.");
                }
                return;

            case "inspect":
                if (command.equalsIgnoreCase("inspect room")) {
                    view.displayMessage(model.lookRoom().getMessage());
                } else {
                    view.displayError("Invalid command. Use 'inspect room'.");
                }
                return;

            case "move":
                result = model.move(command);
                displayResult(result);

                if (result.isSuccess()) {
                    view.displayMessage(model.roomHeader().getMessage());
                    autoStartPuzzleAfterMove();
                }
                return;

            case "take":
                displayResult(model.takeItem(command));
                return;

            case "drop":
                displayResult(model.dropItem(command));
                return;

            case "consume":
                if (command.equalsIgnoreCase("consume potion")) {
                    displayResult(model.useItem(command));
                } else {
                    view.displayError("Invalid command. Use 'consume potion'.");
                }
                return;

            case "equip":
            case "unequip":
                displayResult(model.useItem(command));
                return;

            case "inventory":
                view.displayInventory(model.getPlayer());
                return;

            case "status":
                view.displayStatus(model.getPlayer());
                return;

            case "hint":
            case "givehint":
                view.displayError("There is no active puzzle.");
                return;

            case "save":
                displayResult(model.saveGame());
                return;

            case "load":
                displayResult(model.loadGame());
                return;

            case "restart":
                handleRestartCommand();
                return;

            case "exit":
                view.displayMessage("Exiting game.");
                isRunning = false;
                return;

            default:
                view.displayError("Invalid command.");
        }
    }

    private void handlePuzzleLoop(String command) {
        if (command.isEmpty()) {
            view.displayError("Please enter a command.");
            return;
        }

        String action = command.split(" ")[0].toLowerCase();

        if (command.equalsIgnoreCase("help")) {
            view.displayPuzzleHelp(model.getActivePuzzle());
            return;
        }

        if (command.equalsIgnoreCase("hint") || command.equalsIgnoreCase("givehint")) {
            view.displayMessage(model.getActivePuzzleHint());
            return;
        }

        if (command.equalsIgnoreCase("explore") || command.equalsIgnoreCase("explore room")) {
            if (model.getActivePuzzle() instanceof Puzzle5Commitment) {
                Room room = model.getRoomManager().getCurrentRoom();
                boolean atFinalRoom = model.getPlayer().getCurrentRoomId().equals("CM-07");
                view.displayCommitmentExplore(room, atFinalRoom);
            } else {
                view.displayMessage(model.exploreRoom().getMessage());
            }
            return;
        }

        if (command.equalsIgnoreCase("inspect room")) {
            view.displayMessage(model.lookRoom().getMessage());
            return;
        }

        if (command.equalsIgnoreCase("status")) {
            view.displayStatus(model.getPlayer());
            return;
        }

        if (command.equalsIgnoreCase("inventory")) {
            view.displayInventory(model.getPlayer());
            return;
        }

        if (command.equalsIgnoreCase("save")) {
            displayResult(model.saveGame());
            return;
        }

        if (command.equalsIgnoreCase("load")) {
            displayResult(model.loadGame());
            return;
        }

        if (command.equalsIgnoreCase("restart")) {
            handleRestartCommand();
            return;
        }

        if (command.equalsIgnoreCase("exit")) {
            view.displayMessage("Exiting game.");
            isRunning = false;
            return;
        }

        /*
         * Sacrifice special case:
         * Only sword commands should update Puzzle4's sword state.
         * Other take commands, like "take potion", should behave like normal item pickup.
         */
        if (model.getActivePuzzle() instanceof Puzzle4Sacrifice && action.equals("take")) {
            if (command.equalsIgnoreCase("take sword") ||
                    command.equalsIgnoreCase("take strong trial sword")) {

                String takeCommand = command;

                if (command.equalsIgnoreCase("take sword")) {
                    takeCommand = "take strong trial sword";
                }

                GameResult takeResult = model.takeItem(takeCommand);

                if (!takeResult.isSuccess()) {
                    displayResult(takeResult);
                    return;
                }

                GameResult puzzleTakeResult = model.handlePuzzleCommand("confirm sword taken");
                handlePuzzleResult(puzzleTakeResult);
                return;
            }

            displayResult(model.takeItem(command));
            return;
        }

        if (isNumberMoveCommand(command)) {
            if (model.getActivePuzzle() instanceof Puzzle4Sacrifice) {
                handleSacrificeMovement(command);
                return;
            }

            if (model.getActivePuzzle() instanceof Puzzle5Commitment) {
                handleCommitmentMovement(command);
                return;
            }

            view.displayError("Finish the active puzzle before using numbered movement.");
            return;
        }

        /*
         * Commitment special case:
         * The actual room item should be picked up first, then Puzzle5 is told
         * that the player stopped to take an item. This lets taking items be dangerous
         * instead of being blocked as an invalid puzzle command.
         */
        if (model.getActivePuzzle() instanceof Puzzle5Commitment && action.equals("take")) {
            GameResult takeResult = model.takeItem(command);
            displayResult(takeResult);

            if (takeResult.isSuccess()) {
                GameResult takePuzzleResult = model.handlePuzzleCommand("take item");
                handlePuzzleResult(takePuzzleResult);
            }

            return;
        }

        /*
         * Commitment accepts these as dangerous lingering actions.
         * "inspect room" remains safe because it is just checking the room list,
         * but "examine item" / "inspect item" triggers the Pursuer logic.
         */
        if (model.getActivePuzzle() instanceof Puzzle5Commitment &&
                (command.equalsIgnoreCase("examine item") || command.equalsIgnoreCase("inspect item"))) {
            GameResult examineResult = model.handlePuzzleCommand(command);
            handlePuzzleResult(examineResult);
            return;
        }

        GameResult puzzleResult = model.handlePuzzleCommand(command);

        if (!puzzleResult.getMessage().equalsIgnoreCase("Invalid command.")) {
            handlePuzzleResult(puzzleResult);
            return;
        }

        if (action.equals("take")) {
            displayResult(model.takeItem(command));
            return;
        }

        if (action.equals("drop")) {
            displayResult(model.dropItem(command));
            return;
        }

        if (action.equals("consume") || action.equals("equip") || action.equals("unequip")) {
            displayResult(model.useItem(command));
            return;
        }

        if (action.equals("use")) {
            view.displayError("Use 'consume potion' instead of 'use potion'.");
            return;
        }

        displayResult(puzzleResult);
    }

    private void handleSacrificeMovement(String command) {
        Puzzle4Sacrifice sacrifice = (Puzzle4Sacrifice) model.getActivePuzzle();

        GameResult moveResult = model.move(command);
        displayResult(moveResult);

        if (!moveResult.isSuccess()) {
            return;
        }

        view.displayMessage(model.roomHeader().getMessage());

        String movementResult = sacrifice.handleRoomMovement(model.getPlayer());

        if (movementResult != null && !movementResult.isEmpty()) {
            view.displayMessage(movementResult);
        }

        if (sacrifice.isCombatTriggered()) {
            runCombat(sacrifice.getFailureMonster());
            return;
        }

        if (sacrifice.isFinished()) {
            if (!model.getPlayer().isAlive()) {
                return;
            }

            model.markTrialCompletedForPuzzle(sacrifice);
            model.clearActivePuzzle();

            view.displayPuzzleComplete(model.getPlayer(), model.roomHeader().getMessage());
        }
    }

    private void handleCommitmentMovement(String command) {
        if (model.getPlayer().getCurrentRoomId().equals("CM-07")) {
            view.displayError("Answer yes or no to complete the Commitment trial before moving on.");
            return;
        }

        int moveIndex = getMoveIndex(command);

        if (moveIndex != 1) {
            view.displayError("The path behind you has crumbled away. You cannot go back.");
            view.displayMessage("Use 'move 1' to keep moving forward through the Commitment trial.");
            return;
        }

        Puzzle5Commitment commitment = (Puzzle5Commitment) model.getActivePuzzle();

        GameResult moveResult = model.move(command);
        displayResult(moveResult);

        if (!moveResult.isSuccess()) {
            return;
        }

        view.displayMessage(model.roomHeader().getMessage());

        String movementResult = commitment.handleRoomMovement(model.getPlayer());

        if (movementResult != null && !movementResult.isEmpty()) {
            view.displayMessage(movementResult);
        }
    }

    private int getMoveIndex(String command) {
        String[] parts = command.trim().split(" ");

        if (parts.length != 2) {
            return -1;
        }

        try {
            return Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private boolean isNumberMoveCommand(String command) {
        String[] parts = command.trim().split(" ");

        if (parts.length != 2) {
            return false;
        }

        if (!parts[0].equalsIgnoreCase("move")) {
            return false;
        }

        try {
            Integer.parseInt(parts[1]);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void handlePuzzleResult(GameResult result) {
        if (result.isCombatStarted()) {
            view.displayMessage(result.getMessage());

            if (!model.getPlayer().isAlive()) {
                return;
            }

            runCombat(result.getMonster());

            if (model.getActivePuzzle() == null || !model.getPlayer().isAlive()) {
                return;
            }
        } else {
            displayResult(result);
        }

        if (!model.getPlayer().isAlive()) {
            return;
        }

        if (result.isPuzzleFinished()) {
            Puzzle finishedPuzzle = result.getPuzzle();
            boolean completedTrial = finishedPuzzle != null && finishedPuzzle.isTrialComplete();

            if (completedTrial) {
                model.markTrialCompletedForPuzzle(finishedPuzzle);
            }

            model.clearActivePuzzle();

            if (!model.getPlayer().isAlive()) {
                return;
            }

            if (completedTrial) {
                view.displayPuzzleComplete(model.getPlayer(), model.roomHeader().getMessage());
            }

            autoStartPuzzleAfterMove();
        }
    }

    private void autoStartPuzzleAfterMove() {
        if (!model.getPlayer().isAlive()) {
            return;
        }

        GameResult puzzleResult = model.autoStartPuzzleIfPresent();

        if (puzzleResult != null && puzzleResult.isPuzzleStarted()) {
            view.displayMessage("");
            view.displayMessage(puzzleResult.getMessage());
            view.displayPuzzleHelp(puzzleResult.getPuzzle());
        }
    }

    private void runCombat(Monster monster) {
        Puzzle activePuzzleBeforeCombat = model.getActivePuzzle();

        Combat combat = new Combat(model.getPlayer(), monster);
        combat.startBattle(view, input);

        if (!model.getPlayer().isAlive()) {
            return;
        }

        if (!combat.isMonsterAlive()) {
            Item reward = monster.dropReward();

            if (reward != null) {
                model.getPlayer().addItem(reward);
                view.displayMessage("Monster dropped: " + reward.getItemName());
            }

            if (!model.getPlayer().isAlive()) {
                return;
            }

            if (activePuzzleBeforeCombat instanceof Puzzle2Restraint) {
                model.markTrialCompletedForPuzzle(activePuzzleBeforeCombat);
                model.getPlayer().setCurrentRoomId("EZ-01");
                model.getRoomManager().setRoom("EZ-01");
                model.clearActivePuzzle();

                if (!model.getPlayer().isAlive()) {
                    return;
                }

                view.displayTrialComplete("Trial of Restraint", model.getPlayer(), model.roomHeader().getMessage());
                return;
            }

            if (activePuzzleBeforeCombat instanceof Puzzle3Trust) {
                model.markTrialCompletedForPuzzle(activePuzzleBeforeCombat);
                model.getPlayer().setCurrentRoomId("EZ-01");
                model.getRoomManager().setRoom("EZ-01");
                model.clearActivePuzzle();

                if (!model.getPlayer().isAlive()) {
                    return;
                }

                view.displayTrialComplete("Trial of Trust", model.getPlayer(), model.roomHeader().getMessage());
                return;
            }

            if (activePuzzleBeforeCombat instanceof Puzzle4Sacrifice) {
                model.markTrialCompletedForPuzzle(activePuzzleBeforeCombat);
                model.getPlayer().setCurrentRoomId("EZ-01");
                model.getRoomManager().setRoom("EZ-01");
                model.clearActivePuzzle();

                if (!model.getPlayer().isAlive()) {
                    return;
                }

                view.displayTrialComplete("Trial of Sacrifice", model.getPlayer(), model.roomHeader().getMessage());
                return;
            }

            if (activePuzzleBeforeCombat instanceof Puzzle5Commitment) {
                Puzzle5Commitment commitment = (Puzzle5Commitment) activePuzzleBeforeCombat;
                view.displayMessage(commitment.finishAfterPursuerDefeated(model.getPlayer()));

                if (!model.getPlayer().isAlive()) {
                    return;
                }

                model.markTrialCompletedForPuzzle(activePuzzleBeforeCombat);
                model.getPlayer().setCurrentRoomId("EZ-01");
                model.getRoomManager().setRoom("EZ-01");
                model.clearActivePuzzle();

                view.displayMessage("You have been teleported back to the Main Hall.");
                view.displayStatus(model.getPlayer());
                view.displayMessage("");
                view.displayMessage(model.roomHeader().getMessage());
                return;
            }

            if (activePuzzleBeforeCombat instanceof Puzzle6FinalTrial) {
                Puzzle6FinalTrial finalTrial = (Puzzle6FinalTrial) activePuzzleBeforeCombat;
                finalTrial.onStalkerDefeated();

                if (!model.getPlayer().isAlive()) {
                    return;
                }

                view.displayMessage("The Stalker falls. The teleporter stabilizes.");
                view.displayMessage("Would you like to go through the teleporter? Yes or no");
                return;
            }

            if (activePuzzleBeforeCombat instanceof Puzzle7AwarenessTrap) {
                Puzzle7AwarenessTrap trap = (Puzzle7AwarenessTrap) activePuzzleBeforeCombat;
                view.displayMessage(trap.finishAfterWardenDefeated(model.getPlayer()));

                if (!model.getPlayer().isAlive()) {
                    return;
                }

                model.markTrialCompletedForPuzzle(activePuzzleBeforeCombat);
                model.getPlayer().setCurrentRoomId("EZ-01");
                model.getRoomManager().setRoom("EZ-01");
                model.clearActivePuzzle();

                view.displayMessage("You have been teleported back to the Main Hall.");
                view.displayStatus(model.getPlayer());
                view.displayMessage("");
                view.displayMessage(model.roomHeader().getMessage());
                return;
            }
        }

        model.getRoomManager().setRoom(model.getPlayer().getCurrentRoomId());
    }

    private void displayResult(GameResult result) {
        if (result.isSuccess()) {
            view.displayMessage(result.getMessage());
        } else {
            view.displayError(result.getMessage());
        }
    }
}