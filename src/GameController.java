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
        view.displayIntro();
        view.displayMessage("");
        view.displayMessage(model.lookRoom().getMessage());

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
                view.displayMessage("You died.");
                isRunning = false;
            }
        }
    }

    private void handleMainCommand(String command) {
        if (command.isEmpty()) {
            view.displayError("Please enter a command.");
            return;
        }

        String action = command.split(" ")[0].toLowerCase();
        GameResult result;

        switch (action) {
            case "help":
                printMainHelp();
                return;

            case "inspect":
                if (command.equalsIgnoreCase("inspect room")) {
                    view.displayMessage(model.lookRoom().getMessage());
                } else {
                    view.displayError("Invalid command.");
                }
                return;

            case "move":
                result = model.move(command);
                displayResult(result);

                if (result.isSuccess()) {
                    view.displayMessage(model.lookRoom().getMessage());
                    autoStartPuzzleAfterMove();
                    checkPuzzleRoomTrigger();
                }
                return;

            case "take":
                displayResult(model.takeItem(command));
                checkPuzzleRoomTrigger();
                return;

            case "drop":
                displayResult(model.dropItem(command));
                return;

            case "consume":
            case "equip":
            case "unequip":
                displayResult(model.useItem(command));
                return;

            case "use":
                view.displayError("Use 'consume potion' instead of 'use potion'.");
                return;

            case "inventory":
                view.displayMessage(model.showInventory().getMessage());
                return;

            case "status":
                view.displayMessage(model.showStatus().getMessage());
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
            printPuzzleHelp(model.getActivePuzzle());
            return;
        }

        if (command.equalsIgnoreCase("hint") || command.equalsIgnoreCase("givehint")) {
            view.displayMessage(model.getActivePuzzle().getHint());
            return;
        }

        if (command.equalsIgnoreCase("inspect room")) {
            view.displayMessage(model.lookRoom().getMessage());
            return;
        }

        if (command.equalsIgnoreCase("status")) {
            view.displayMessage(model.showStatus().getMessage());
            return;
        }

        if (command.equalsIgnoreCase("inventory")) {
            view.displayMessage(model.showInventory().getMessage());
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

        if (command.equalsIgnoreCase("exit")) {
            view.displayMessage("Exiting game.");
            isRunning = false;
            return;
        }

        // Allow numbered movement for ALL puzzles
        if (isNumberMoveCommand(command)) {
            GameResult moveResult = model.move(command);
            displayResult(moveResult);

            if (moveResult.isSuccess()) {
                view.displayMessage(model.lookRoom().getMessage());

                // Handle Puzzle5Commitment specific room movement
                if (model.getActivePuzzle() instanceof Puzzle5Commitment) {
                    Puzzle5Commitment commitment = (Puzzle5Commitment) model.getActivePuzzle();
                    String movementResult = commitment.handleRoomMovement(model.getPlayer());
                    if (movementResult != null && !movementResult.isEmpty()) {
                        view.displayMessage(movementResult);
                    }
                }

                // Check for room triggers (for Puzzle4Sacrifice SC-03)
                checkPuzzleRoomTrigger();
            }
            return;
        }

        GameResult puzzleResult = model.handlePuzzleCommand(command);

        if (!puzzleResult.getMessage().equalsIgnoreCase("Invalid command.")) {
            handlePuzzleResult(puzzleResult);
            return;
        }

        if (action.equals("take")) {
            if (model.getActivePuzzle() instanceof Puzzle5Commitment) {
                GameResult takeResult = model.takeItem(command);
                displayResult(takeResult);

                if (takeResult.isSuccess()) {
                    GameResult takePuzzleResult = model.handlePuzzleCommand("take item");
                    handlePuzzleResult(takePuzzleResult);
                }
                return;
            }

            displayResult(model.takeItem(command));
            checkPuzzleRoomTrigger();
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

    // FIXED: This is the only method that changed
    private boolean isNumberMoveCommand(String command) {
        if (command == null) return false;

        String trimmed = command.trim().toLowerCase();

        // Must start with "move "
        if (!trimmed.startsWith("move ")) {
            return false;
        }

        // Get the part after "move "
        String numberPart = trimmed.substring(5).trim();

        // Check if it's empty
        if (numberPart.isEmpty()) {
            return false;
        }

        // Try to parse as integer
        try {
            Integer.parseInt(numberPart);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void checkPuzzleRoomTrigger() {
        if (!model.hasActivePuzzle()) {
            return;
        }

        Puzzle activePuzzle = model.getActivePuzzle();
        String currentRoom = model.getPlayer().getCurrentRoomId();

        // Check for Puzzle4Sacrifice - when entering SC-03 (end of bridge)
        if (activePuzzle instanceof Puzzle4Sacrifice && currentRoom.equals("SC-03")) {
            // Call handleCommand with special trigger
            GameResult result = model.handlePuzzleCommand("check_room_trigger");
            if (result.isCombatStarted()) {
                view.displayMessage(result.getMessage());
                runCombat(result.getMonster());
                if (model.getActivePuzzle() != null && model.getPlayer().isAlive()) {
                    if (activePuzzle.isFinished()) {
                        handlePuzzleFinish(activePuzzle);
                    }
                }
            } else if (result.isPuzzleFinished()) {
                view.displayMessage(result.getMessage());
                handlePuzzleFinish(activePuzzle);
            }
        }
    }

    private void handlePuzzleResult(GameResult result) {
        if (result.isCombatStarted()) {
            view.displayMessage(result.getMessage());
            runCombat(result.getMonster());

            if (model.getActivePuzzle() == null || !model.getPlayer().isAlive()) {
                return;
            }
        } else {
            displayResult(result);
        }

        if (result.isPuzzleFinished()) {
            handlePuzzleFinish(result.getPuzzle());
        }
    }

    private void handlePuzzleFinish(Puzzle finishedPuzzle) {
        if (finishedPuzzle == null) return;

        boolean completedTrial = finishedPuzzle != null && finishedPuzzle.isTrialComplete();

        if (completedTrial) {
            model.markTrialCompletedForPuzzle(finishedPuzzle);
        }

        model.clearActivePuzzle();

        if (!model.getPlayer().isAlive()) {
            return;
        }

        if (completedTrial) {
            view.displayMessage("");
            view.displayMessage("Puzzle complete.");
            view.displayMessage(model.showStatus().getMessage());
            view.displayMessage("");
            view.displayMessage(model.lookRoom().getMessage());
        }

        autoStartPuzzleAfterMove();
    }

    private void autoStartPuzzleAfterMove() {
        if (!model.getPlayer().isAlive()) {
            return;
        }

        GameResult puzzleResult = model.autoStartPuzzleIfPresent();

        if (puzzleResult != null && puzzleResult.isPuzzleStarted()) {
            view.displayMessage("");
            view.displayMessage(puzzleResult.getMessage());
            printPuzzleHelp(puzzleResult.getPuzzle());
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

            if (activePuzzleBeforeCombat instanceof Puzzle2Restraint) {
                model.markTrialCompletedForPuzzle(activePuzzleBeforeCombat);
                model.getPlayer().setCurrentRoomId("EZ-01");
                model.getRoomManager().setRoom("EZ-01");
                model.clearActivePuzzle();

                view.displayMessage("You have completed the Trial of Restraint. (No Reward)");
                view.displayMessage(model.showStatus().getMessage());
                view.displayMessage("");
                view.displayMessage(model.lookRoom().getMessage());
                return;
            }

            if (activePuzzleBeforeCombat instanceof Puzzle3Trust) {
                model.markTrialCompletedForPuzzle(activePuzzleBeforeCombat);
                model.getPlayer().setCurrentRoomId("EZ-01");
                model.getRoomManager().setRoom("EZ-01");
                model.clearActivePuzzle();

                view.displayMessage("You have completed the Trial of Trust. (No Reward)");
                view.displayMessage(model.showStatus().getMessage());
                view.displayMessage("");
                view.displayMessage(model.lookRoom().getMessage());
                return;
            }

            if (activePuzzleBeforeCombat instanceof Puzzle4Sacrifice) {
                model.markTrialCompletedForPuzzle(activePuzzleBeforeCombat);
                model.getPlayer().setCurrentRoomId("EZ-01");
                model.getRoomManager().setRoom("EZ-01");
                model.clearActivePuzzle();

                view.displayMessage("You have completed the Trial of Sacrifice. (No Reward)");
                view.displayMessage(model.showStatus().getMessage());
                view.displayMessage("");
                view.displayMessage(model.lookRoom().getMessage());
                return;
            }

            if (activePuzzleBeforeCombat instanceof Puzzle5Commitment) {
                Puzzle5Commitment commitment = (Puzzle5Commitment) activePuzzleBeforeCombat;
                view.displayMessage(commitment.finishAfterPursuerDefeated(model.getPlayer()));

                if (model.getPlayer().isAlive()) {
                    model.markTrialCompletedForPuzzle(activePuzzleBeforeCombat);
                    model.getPlayer().setCurrentRoomId("EZ-01");
                    model.getRoomManager().setRoom("EZ-01");
                    model.clearActivePuzzle();

                    view.displayMessage(model.showStatus().getMessage());
                    view.displayMessage("");
                    view.displayMessage(model.lookRoom().getMessage());
                }
                return;
            }

            if (activePuzzleBeforeCombat instanceof Puzzle6FinalTrial) {
                Puzzle6FinalTrial finalTrial = (Puzzle6FinalTrial) activePuzzleBeforeCombat;
                finalTrial.onStalkerDefeated();

                view.displayMessage("The Stalker falls. The teleporter stabilizes.");
                view.displayMessage("Would you like to go through the teleporter? Yes or no");
                return;
            }

            if (activePuzzleBeforeCombat instanceof Puzzle7AwarenessTrap) {
                Puzzle7AwarenessTrap trap = (Puzzle7AwarenessTrap) activePuzzleBeforeCombat;
                view.displayMessage(trap.finishAfterWardenDefeated(model.getPlayer()));
                view.displayMessage("You have been teleported back to the Main Hall.");

                if (model.getPlayer().isAlive()) {
                    model.markTrialCompletedForPuzzle(activePuzzleBeforeCombat);
                    model.getPlayer().setCurrentRoomId("EZ-01");
                    model.getRoomManager().setRoom("EZ-01");
                    model.clearActivePuzzle();

                    view.displayMessage(model.showStatus().getMessage());
                    view.displayMessage("");
                    view.displayMessage(model.lookRoom().getMessage());
                }
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

    private void printMainHelp() {
        view.displayMainHelp();
    }

    private void printPuzzleHelp(Puzzle puzzle) {
        if (puzzle == null) {
            view.displayMessage("No active puzzle.");
            return;
        }

        view.displayMessage("=== Puzzle Commands ===");
        view.displayMessage("hint");
        view.displayMessage("status");
        view.displayMessage("inventory");
        view.displayMessage("take [item]");
        view.displayMessage("drop [item]");
        view.displayMessage("consume [item]");
        view.displayMessage("equip [item]");
        view.displayMessage("unequip [item]");
        view.displayMessage("move [number]");
        view.displayMessage("save");
        view.displayMessage("load");

        if (puzzle instanceof Puzzle1Awareness) {
            view.displayMessage("take glowing red gem");
            view.displayMessage("take rubble");
            view.displayMessage("throw glowing red gem");
            view.displayMessage("throw rubble");
            view.displayMessage("inspect teleporter");
            view.displayMessage("enter");
            return;
        }

        if (puzzle instanceof Puzzle2Restraint) {
            view.displayMessage("take coin");
            view.displayMessage("inspect coin");
            view.displayMessage("inspect chest");
            view.displayMessage("open chest");
            view.displayMessage("yes / no");
            return;
        }

        if (puzzle instanceof Puzzle3Trust) {
            view.displayMessage("attack guardian");
            view.displayMessage("inspect chest");
            view.displayMessage("destroy chest");
            view.displayMessage("open chest");
            view.displayMessage("yes / no");
            return;
        }

        if (puzzle instanceof Puzzle4Sacrifice) {
            view.displayMessage("take strong trial sword");
            view.displayMessage("move 1 (to bridge)");
            view.displayMessage("throw sword");
            view.displayMessage("move 1 (to end)");
            return;
        }

        if (puzzle instanceof Puzzle5Commitment) {
            view.displayMessage("move [number]");
            view.displayMessage("examine item");
            view.displayMessage("take [item]");
            view.displayMessage("kill pursuer");
            view.displayMessage("yes / no");
            return;
        }

        if (puzzle instanceof Puzzle6FinalTrial) {
            view.displayMessage("burn chest");
            view.displayMessage("extinguish fire");
            view.displayMessage("open chest");
            view.displayMessage("insert explosive device");
            view.displayMessage("place core fragment");
            view.displayMessage("step symbol");
            view.displayMessage("throw final jewel");
            view.displayMessage("enter unstable teleporter");
            view.displayMessage("yes / no");
            return;
        }

        if (puzzle instanceof Puzzle7AwarenessTrap) {
            view.displayMessage("inspect room");
            view.displayMessage("take rubble");
            view.displayMessage("take red gem");
            view.displayMessage("throw rubble");
            view.displayMessage("throw red gem");
            view.displayMessage("enter teleporter");
            view.displayMessage("yes / no");
        }
    }
}