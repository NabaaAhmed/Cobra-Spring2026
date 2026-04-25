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
                return;

            case "take":
                displayResult(model.takeItem(command));
                return;

            case "drop":
                displayResult(model.dropItem(command));
                return;

            case "use":
            case "consume":
            case "equip":
            case "unequip":
                displayResult(model.useItem(command));
                return;

            case "inventory":
                view.displayMessage(model.showInventory().getMessage());
                return;

            case "status":
                view.displayMessage(model.showStatus().getMessage());
                return;

            case "fight":
            case "startcombat":
                result = model.startCombatForCurrentRoom();
                displayResult(result);

                if (result.isCombatStarted()) {
                    runCombat(result.getMonster());
                }
                return;

            case "hint":
            case "givehint":
                view.displayError("There is no active puzzle.");
                return;

            case "save":
                view.displayError("You cannot save in the middle of a puzzle. Finish or fail the puzzle first.");                return;

            case "load":
                model.clearActivePuzzle();
                displayResult(model.loadGame());
                view.displayMessage("");
                view.displayMessage(model.lookRoom().getMessage());
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

        // Allow normal item commands even while a puzzle is active.
        // This lets the player pick up potions, consume potions, equip swords, etc.
        if (action.equals("take")) {
            displayResult(model.takeItem(command));
            return;
        }

        if (action.equals("drop")) {
            displayResult(model.dropItem(command));
            return;
        }

        if (action.equals("use") || action.equals("consume") || action.equals("equip") || action.equals("unequip")) {
            displayResult(model.useItem(command));
            return;
        }

        if (command.equalsIgnoreCase("save")) {
            view.displayError("You cannot save in the middle of a puzzle. Finish or fail the puzzle first.");
            return;
        }

        if (command.equalsIgnoreCase("load")) {
            model.clearActivePuzzle();
            displayResult(model.loadGame());
            view.displayMessage("");
            view.displayMessage(model.lookRoom().getMessage());
            return;
        }

        if (command.equalsIgnoreCase("fight") || command.equalsIgnoreCase("startcombat")) {
            GameResult fightResult = model.startCombatForCurrentRoom();
            displayResult(fightResult);

            if (fightResult.isCombatStarted()) {
                runCombat(fightResult.getMonster());
            }
            return;
        }

        if (command.equalsIgnoreCase("exit")) {
            view.displayMessage("Exiting game.");
            isRunning = false;
            return;
        }

        GameResult result = model.handlePuzzleCommand(command);
        displayResult(result);

        if (result.isCombatStarted()) {
            runCombat(result.getMonster());

            if (model.getActivePuzzle() == null || !model.getPlayer().isAlive()) {
                return;
            }
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

            view.displayMessage("");

            if (completedTrial) {
                view.displayMessage("Puzzle complete.");
            } else {
                view.displayMessage("Puzzle ended.");
            }

            view.displayMessage(model.showStatus().getMessage());
            view.displayMessage("");
            view.displayMessage(model.lookRoom().getMessage());

            GameResult nextPuzzle = model.autoStartPuzzleIfPresent();
            if (nextPuzzle != null && nextPuzzle.isPuzzleStarted()) {
                view.displayMessage("");
                view.displayMessage(nextPuzzle.getMessage());
                printPuzzleHelp(nextPuzzle.getPuzzle());
            }
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

                view.displayMessage("You have completed the Trial of Restraint and returned to the entrance zone. (No Reward)");
                view.displayMessage("");
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
                view.displayMessage("");
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
                view.displayMessage("");
                view.displayMessage(model.showStatus().getMessage());
                view.displayMessage("");
                view.displayMessage(model.lookRoom().getMessage());
                return;
            }

            if (activePuzzleBeforeCombat instanceof Puzzle6FinalTrial) {
                Puzzle6FinalTrial finalTrial = (Puzzle6FinalTrial) activePuzzleBeforeCombat;
                finalTrial.onStalkerDefeated();
                view.displayMessage("The Stalker falls. Its corpse could stabilize the teleporter.");
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
        view.displayMessage("save");
        view.displayMessage("load");

        if (puzzle instanceof Puzzle1Awareness) {
            view.displayMessage("take red gem");
            view.displayMessage("take rubble");
            view.displayMessage("throw red gem");
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
            return;
        }

        if (puzzle instanceof Puzzle4Sacrifice) {
            view.displayMessage("take sword");
            view.displayMessage("move bridge");
            view.displayMessage("inspect bridge");
            view.displayMessage("throw sword");
            view.displayMessage("move forward");
            return;
        }

        if (puzzle instanceof Puzzle5Commitment) {
            view.displayMessage("move forward");
            view.displayMessage("examine item");
            view.displayMessage("take item");
            view.displayMessage("kill pursuer");
            view.displayMessage("yes / no");
            return;
        }

        if (puzzle instanceof Puzzle6FinalTrial) {
            view.displayMessage("fight");
            view.displayMessage("burn chest");
            view.displayMessage("extinguish fire");
            view.displayMessage("open chest");
            view.displayMessage("insert explosive device");
            view.displayMessage("place core fragment");
            view.displayMessage("step symbol");
            view.displayMessage("throw final jewel");
            view.displayMessage("throw stalker corpse");
            view.displayMessage("enter unstable teleporter");
            view.displayMessage("yes / no");
            return;
        }

        if (puzzle instanceof Puzzle7AwarenessTrap) {
            view.displayMessage("fight");
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