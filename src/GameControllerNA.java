//import java.util.Scanner;
//
//public class GameControllerNA {
//    private Scanner scanner;
//    private Player player;
//    private RoomManager roomManager;
//    private GameView gameView;
//    private FileManager fileManager;
//    private boolean isRunning;
//
//    public GameControllerNA(Player player, RoomManager roomManager,
//                            GameView gameView, FileManager fileManager) {
//        this.scanner = new Scanner(System.in);
//        this.player = player;
//        this.roomManager = roomManager;
//        this.gameView = gameView;
//        this.fileManager = fileManager;
//        this.isRunning = true;
//    }
//
//    public void startGame() {
//        displayWelcome();
//
//        boolean menuActive = true;
//
//        while (menuActive) {
//            System.out.println("\n========== MAIN MENU ==========");
//            System.out.println("[1] Start New Game");
//            System.out.println("[2] About");
//            System.out.println("[3] Quit");
//            System.out.println("================================");
//            System.out.print("Enter your choice: ");
//
//            String choice = scanner.nextLine().trim().toLowerCase();
//
//            switch (choice) {
//                case "1":
//                case "start":
//                    System.out.println("\nStarting new game...\n");
//                    menuActive = false;
//                    play();
//                    break;
//
//                case "2":
//                case "about":
//                    displayAbout();
//                    break;
//
//                case "3":
//                case "quit":
//                    System.out.println("Goodbye! Thanks for playing!");
//                    menuActive = false;
//                    isRunning = false;
//                    break;
//
//                default:
//                    System.out.println("[ERROR] Invalid option. Please enter 1, 2, or 3.");
//            }
//        }
//    }
//
//    private void displayWelcome() {
//        System.out.println("     /\\      /\\      /\\      /\\      /\\      /\\      /\\");
//        System.out.println("    /  \\    /  \\    /  \\    /  \\    /  \\    /  \\    /  \\");
//        System.out.println("    \\  /    \\  /    \\  /    \\  /    \\  /    \\  /    \\  /");
//        System.out.println("     \\/      \\/      \\/      \\/      \\/      \\/      \\/");
//        System.out.println();
//        System.out.println("============== WELCOME TO THE DUNGEON OF TRIALS ==============");
//        System.out.println("The Dungeon of Trials was constructed by a king to find a worthy successor.");
//        System.out.println("The dungeon tests character across five themed trials.");
//        System.out.println("Complete all five trials to reach the Final Trial and claim the Catalyst!");
//        System.out.println("=================================================================\n");
//    }
//
//    private void displayAbout() {
//        System.out.println("\n========================================================");
//        System.out.println("                   DUNGEON OF TRIALS                      ");
//        System.out.println("========================================================");
//        System.out.println("A text-based adventure game where you must complete      ");
//        System.out.println("five trials to prove yourself worthy.                    ");
//        System.out.println("                                                          ");
//        System.out.println("Each trial tests a different virtue:                     ");
//        System.out.println("  - Awareness  - Observe carefully                         ");
//        System.out.println("  - Restraint  - Control your greed                        ");
//        System.out.println("  - Trust      - Have faith in your instincts              ");
//        System.out.println("  - Sacrifice  - Give up power for greater good            ");
//        System.out.println("  - Commitment - Stay focused on your goal                 ");
//        System.out.println("                                                          ");
//        System.out.println("Type 'help' at any time to see available commands.       ");
//        System.out.println("========================================================\n");
//    }
//
//    public void play() {
//        gameView.displayHelp();
//
//        // Add puzzle items and monsters to rooms after loading from file
//        roomManager.addPuzzleItemsToRooms();
//
//        while (isRunning) {
//            if (roomManager.getCurrentRoom() != null) {
//                gameView.displayRoom(roomManager.getCurrentRoom());
//            }
//
//            // AUTO COMBAT TRIGGER
//            Monster currentMonster = roomManager.getCurrentRoom().getMonster();
//            if (currentMonster != null && currentMonster.isAlive()) {
//                System.out.println("\nA " + currentMonster.getName() + " blocks your path!");
//                startCombat();
//                if (!player.isAlive()) {
//                    gameView.displayGameOver();
//                    break;
//                }
//                continue;
//            }
//
//            System.out.print("\nWhat would you like to do? (type 'help' for commands)\n> ");
//            String command = scanner.nextLine().trim();
//
//            processCommand(command);
//            updateState();
//        }
//
//        scanner.close();
//    }
//
//    public void processCommand(String command) {
//        if (command.isEmpty()) {
//            System.out.println("[ERROR] Please enter a command. Type 'help' for available commands.");
//            return;
//        }
//
//        String[] parts = command.split(" ", 2);
//        String action = parts[0].toLowerCase();
//        String arg = parts.length > 1 ? parts[1] : "";
//
//        switch (action) {
//            case "help":
//                gameView.displayHelp();
//                break;
//
//            case "look":
//            case "room":
//                gameView.displayRoom(roomManager.getCurrentRoom());
//                break;
//
//            case "about":
//                displayAbout();
//                break;
//
//            case "inspect":
//                handleInspect(arg);
//                break;
//
//            case "take":
//                handleTake(arg);
//                break;
//
//            case "drop":
//                handleDrop(arg);
//                break;
//
//            case "inventory":
//                gameView.displayInventory(player);
//                break;
//
//            case "equip":
//                handleEquip(arg);
//                break;
//
//            case "unequip":
//                handleUnequip();
//                break;
//
//            case "explore":
//                handleExplore(arg);
//                break;
//
//            case "solve":
//                handleSolve(arg);
//                break;
//
//            case "hint":
//                handleHint(arg);
//                break;
//
//            case "status":
//                gameView.displayStatus(player);
//                break;
//
//            case "attack":
//            case "fight":
//                handleAttack();
//                break;
//
//            case "consume":
//                handleConsume(arg);
//                break;
//
//            case "move":
//                handleMove(arg);
//                break;
//
//            case "save":
//                handleSave();
//                break;
//
//            case "load":
//                handleLoad();
//                break;
//
//            case "puzzle1":
//            case "awareness":
//                handlePuzzle1();
//                break;
//
//            case "puzzle2":
//            case "restraint":
//                handlePuzzle2();
//                break;
//
//            case "puzzle3":
//            case "trust":
//                handlePuzzle3();
//                break;
//
//            case "puzzle4":
//            case "sacrifice":
//                handlePuzzle4();
//                break;
//
//            case "exit":
//                System.out.println("\nThanks for playing! Goodbye!\n");
//                isRunning = false;
//                break;
//
//            default:
//                System.out.println("[ERROR] Unknown command: '" + action + "'");
//                System.out.println("Type 'help' to see all available commands.");
//        }
//    }
//
//    private void handleInspect(String arg) {
//        if (arg.isEmpty()) {
//            System.out.println("[ERROR] Inspect what? Example: inspect room, inspect potion");
//            return;
//        }
//
//        if (arg.equalsIgnoreCase("room")) {
//            System.out.println(roomManager.getCurrentRoom().getDescription());
//            return;
//        }
//
//        Item roomItem = roomManager.getCurrentRoom().findItem(arg);
//        if (roomItem != null) {
//            System.out.println(roomItem.getDescription());
//            return;
//        }
//
//        Item invItem = player.findItem(arg);
//        if (invItem != null) {
//            System.out.println(invItem.getDescription());
//            return;
//        }
//
//        System.out.println("[ERROR] No such item: " + arg);
//    }
//
//    private void handleTake(String arg) {
//        if (arg.isEmpty()) {
//            System.out.println("[ERROR] Take what? Example: take potion");
//            return;
//        }
//
//        Item item = roomManager.getCurrentRoom().findItem(arg);
//        if (item != null) {
//            roomManager.getCurrentRoom().removeItem(item);
//            player.addItem(item);
//            System.out.println("You took: " + item.getName());
//        } else {
//            System.out.println("[ERROR] That item is not here.");
//        }
//    }
//
//    private void handleDrop(String arg) {
//        if (arg.isEmpty()) {
//            System.out.println("[ERROR] Drop what? Example: drop potion");
//            return;
//        }
//
//        Item item = player.findItem(arg);
//        if (item != null) {
//            player.removeItem(item);
//            roomManager.getCurrentRoom().addItem(item);
//            System.out.println("You dropped: " + item.getName());
//        } else {
//            System.out.println("[ERROR] You don't have that item.");
//        }
//    }
//
//    private void handleEquip(String arg) {
//        if (arg.isEmpty()) {
//            System.out.println("[ERROR] Equip what? Example: equip sword");
//            return;
//        }
//
//        Item item = player.findItem(arg);
//        if (item instanceof Sword) {
//            item.use(player);
//            System.out.println("Equipped: " + item.getName());
//        } else if (item == null) {
//            System.out.println("[ERROR] You don't have that item.");
//        } else {
//            System.out.println("[ERROR] That's not a sword.");
//        }
//    }
//
//    private void handleUnequip() {
//        if (player.getEquippedSword() != null) {
//            String swordName = player.getEquippedSword().getName();
//            player.unequipSword();
//            System.out.println("Unequipped: " + swordName);
//        } else {
//            System.out.println("[ERROR] No sword equipped.");
//        }
//    }
//
//    private void handleExplore(String arg) {
//        if (arg.isEmpty()) {
//            System.out.println("[ERROR] Explore which trial? Example: explore awareness");
//            return;
//        }
//
//        Puzzle puzzle = roomManager.getCurrentRoom().getPuzzle();
//        if (puzzle != null && puzzle.getTrialName().equalsIgnoreCase(arg)) {
//            gameView.displayPuzzle(puzzle);
//        } else {
//            System.out.println("[ERROR] No puzzle named '" + arg + "' here.");
//        }
//    }
//
//    private void handleSolve(String arg) {
//        if (arg.isEmpty()) {
//            System.out.println("[ERROR] Solve which trial? Example: solve awareness");
//            return;
//        }
//
//        Puzzle puzzle = roomManager.getCurrentRoom().getPuzzle();
//        if (puzzle != null && puzzle.getTrialName().equalsIgnoreCase(arg)) {
//            if (puzzle.isSolved()) {
//                System.out.println("This puzzle is already solved!");
//                return;
//            }
//
//            System.out.print("Enter your solution:\n> ");
//            String solution = scanner.nextLine().trim();
//
//            if (puzzle.checkSolution(solution)) {
//                puzzle.setSolved(true);
//                player.setMaxHP(player.getMaxHP() + 1);
//                player.fullHeal();
//                System.out.println("\nCORRECT! You completed the " + puzzle.getTrialName() + " Trial!");
//                System.out.println("Max HP increased to " + player.getMaxHP() + " and fully healed!");
//                System.out.println("You received a Trial Token!");
//            } else {
//                System.out.println("\nINCORRECT! The puzzle remains unsolved.");
//                gameView.displayHint(puzzle.getHint());
//            }
//        } else {
//            System.out.println("[ERROR] No puzzle named '" + arg + "' here.");
//        }
//    }
//
//    private void handleHint(String arg) {
//        if (arg.isEmpty()) {
//            System.out.println("[ERROR] Hint for which trial? Example: hint awareness");
//            return;
//        }
//
//        Puzzle puzzle = roomManager.getCurrentRoom().getPuzzle();
//        if (puzzle != null && puzzle.getTrialName().equalsIgnoreCase(arg)) {
//            gameView.displayHint(puzzle.getHint());
//        } else {
//            System.out.println("[ERROR] No puzzle named '" + arg + "' here.");
//        }
//    }
//
//    private void handleAttack() {
//        Monster monster = roomManager.getCurrentRoom().getMonster();
//        if (monster != null && monster.isAlive()) {
//            startCombat();
//        } else {
//            System.out.println("[ERROR] No monster to attack here!");
//        }
//    }
//
//    private void handleConsume(String arg) {
//        if (arg.isEmpty()) {
//            System.out.println("[ERROR] Consume what? Example: consume potion");
//            return;
//        }
//
//        Item item = player.findItem(arg);
//        if (item instanceof Potion && ((Potion) item).getHealAmount() > 0) {
//            int oldHP = player.getCurrentHP();
//            item.use(player);
//            player.removeItem(item);
//            System.out.println("You consumed " + item.getName() + "!");
//            System.out.println("HP: " + oldHP + " -> " + player.getCurrentHP() + "/" + player.getMaxHP());
//        } else if (item == null) {
//            System.out.println("[ERROR] You don't have that item.");
//        } else {
//            System.out.println("[ERROR] You can only consume potions.");
//        }
//    }
//
//    private void handleMove(String arg) {
//        if (arg.isEmpty()) {
//            System.out.println("[ERROR] Move where? Examples:");
//            System.out.println("  - move 0 (move by number)");
//            System.out.println("  - move 'Trial Room' (move by name)");
//            return;
//        }
//
//        try {
//            int index = Integer.parseInt(arg);
//            roomManager.move(index);
//            player.setCurrentRoomID(roomManager.getRoomId());
//            System.out.println("\nYou move to: " + roomManager.getCurrentRoom().getName() + "\n");
//        } catch (NumberFormatException e) {
//            roomManager.moveToRoom(arg);
//            player.setCurrentRoomID(roomManager.getRoomId());
//        }
//    }
//
//    private void handleSave() {
//        gameView.displaySaveSuccess();
//    }
//
//    private void handleLoad() {
//        gameView.displayLoadSuccess();
//    }
//
//    private void startCombat() {
//        Room currentRoom = roomManager.getCurrentRoom();
//        Monster monster = currentRoom.getMonster();
//
//        if (monster == null || !monster.isAlive()) {
//            System.out.println("No monster here.");
//            return;
//        }
//
//        Combat combat = new Combat(player, monster);
//        combat.startBattle(gameView, scanner, currentRoom);
//    }
//
//    public void updateState() {
//        if (!player.isAlive()) {
//            gameView.displayGameOver();
//            isRunning = false;
//        }
//    }
//
//    private void handlePuzzle1() {
//        if (!roomManager.isPuzzle1Active()) {
//            System.out.println("This trial is already complete.");
//            return;
//        }
//
//        Puzzle1_Awareness puzzle = roomManager.getPuzzle1();
//        String currentRoomId = roomManager.getCurrentRoom().getId();
//
//        if (!currentRoomId.equals("AW-02")) {
//            System.out.println("You are not in the Awareness Trial room.");
//            System.out.println("Go to the Awareness Trial Room first.");
//            return;
//        }
//
//        puzzle.displayDescription(gameView);
//
//        boolean inPuzzle = true;
//        while (inPuzzle && !puzzle.isCompleted() && isRunning) {
//            System.out.print("\n[Trial of Awareness] What would you like to do?\n> ");
//            String input = scanner.nextLine().trim().toLowerCase();
//
//            if (input.equals("exit")) {
//                System.out.println("You leave the trial room.");
//                inPuzzle = false;
//            } else if (input.equals("hint")) {
//                puzzle.displayHint(gameView);
//            } else if (input.startsWith("take ")) {
//                String itemName = input.substring(5);
//                if (itemName.equals("gem") || itemName.equals("glowing red gem")) {
//                    if (!puzzle.isGemTaken()) {
//                        player.addItem(puzzle.getGlowingRedGem());
//                        puzzle.setGemTaken(true);
//                        System.out.println("You took the Glowing Red Gem.");
//                    } else {
//                        System.out.println("The gem is already taken.");
//                    }
//                } else if (itemName.equals("rubble")) {
//                    if (!puzzle.isRubbleTaken()) {
//                        player.addItem(puzzle.getRubble());
//                        puzzle.setRubbleTaken(true);
//                        System.out.println("You took the rubble.");
//                    } else {
//                        System.out.println("The rubble is already taken.");
//                    }
//                } else {
//                    System.out.println("You can't take that.");
//                }
//            } else if (input.startsWith("throw ")) {
//                String itemName = input.substring(6).replace(" to teleporter", "").trim();
//                int result = puzzle.processThrow(itemName, gameView, player, scanner);
//
//                if (result == 1) {
//                    puzzle.completePuzzle(gameView, player, roomManager);
//                    roomManager.setPuzzle1Active(false);
//                    inPuzzle = false;
//                } else if (result == -1) {
//                    if (player.isAlive()) {
//                        puzzle.handlePenalty(gameView, player, roomManager);
//                        roomManager.setPuzzle1Active(false);
//                        inPuzzle = false;
//                        roomManager.spawnWardenInTrapRoom();
//                    } else {
//                        gameView.displayGameOver();
//                        isRunning = false;
//                        inPuzzle = false;
//                    }
//                }
//            } else {
//                System.out.println("Invalid command. Available: take gem, take rubble, throw <item>, hint, exit");
//            }
//        }
//    }
//
//    private void handlePuzzle2() {
//        if (!roomManager.isPuzzle2Active()) {
//            System.out.println("This trial is already complete.");
//            return;
//        }
//
//        Puzzle2_Restraint puzzle = roomManager.getPuzzle2();
//        String currentRoomId = roomManager.getCurrentRoom().getId();
//
//        if (!currentRoomId.equals("RS-02")) {
//            System.out.println("You are not in the Restraint Trial room.");
//            System.out.println("Go to the Restraint Trial Room first.");
//            return;
//        }
//
//        puzzle.displayDescription(gameView);
//
//        boolean inPuzzle = true;
//        while (inPuzzle && !puzzle.isCompleted() && isRunning) {
//            System.out.print("\n[Trial of Restraint] What would you like to do?\n> ");
//            String input = scanner.nextLine().trim().toLowerCase();
//
//            if (input.equals("exit")) {
//                System.out.println("You leave the trial room.");
//                inPuzzle = false;
//            } else if (input.equals("hint")) {
//                puzzle.displayHint(gameView);
//            } else if (input.equals("take coin")) {
//                if (!puzzle.isCoinTaken()) {
//                    player.addItem(puzzle.getBaitCoin());
//                    puzzle.setCoinTaken(true);
//                    System.out.println("You took the Bait Coin.");
//                } else {
//                    System.out.println("The coin is already taken.");
//                }
//            } else if (input.equals("examine chest")) {
//                boolean mimicTriggered = puzzle.examineChest(gameView, player);
//                if (mimicTriggered) {
//                    roomManager.spawnMimicInRestraintRoom();
//                    Monster mimic = roomManager.getCurrentRoom().getMonster();
//                    if (mimic != null) {
//                        Combat combat = new Combat(player, mimic);
//                        combat.startBattle(gameView, scanner, roomManager.getCurrentRoom());
//                    }
//
//                    if (!player.isAlive()) {
//                        gameView.displayGameOver();
//                        isRunning = false;
//                        return;
//                    }
//
//                    puzzle.onMimicDefeated(gameView, player, roomManager);
//                    roomManager.setPuzzle2Active(false);
//                    inPuzzle = false;
//                }
//            } else if (input.equals("open chest")) {
//                puzzle.openChest(gameView, player);
//            } else if (input.equals("place coin")) {
//                puzzle.placeCoin(gameView);
//            } else if (input.equals("leave") || input.equals("complete")) {
//                puzzle.completePuzzle(gameView, player, roomManager, true);
//                if (puzzle.isCompleted()) {
//                    roomManager.setPuzzle2Active(false);
//                    inPuzzle = false;
//                }
//            } else {
//                System.out.println("Invalid command. Available: take coin, examine chest, open chest, place coin, leave, hint, exit");
//            }
//        }
//    }
//
//    private void handlePuzzle3() {
//        if (!roomManager.isPuzzle3Active()) {
//            System.out.println("This trial is already complete.");
//            return;
//        }
//
//        Puzzle3Trust puzzle = new Puzzle3Trust();
//        String currentRoomId = roomManager.getCurrentRoom().getId();
//
//        if (!currentRoomId.equals("TR-02")) {
//            System.out.println("You are not in the Trust Trial room.");
//            System.out.println("Go to the Guardian Chamber first.");
//            return;
//        }
//
//        System.out.println(puzzle.startPuzzle());
//
//        boolean inPuzzle = true;
//        while (inPuzzle && !puzzle.isFinished() && isRunning) {
//            System.out.print("\n[Trial of Trust] What would you like to do?\n> ");
//            String input = scanner.nextLine().trim().toLowerCase();
//
//            if (input.equals("exit")) {
//                System.out.println("You leave the trial room.");
//                inPuzzle = false;
//            } else if (input.equals("help")) {
//                System.out.println("attack guardian");
//                System.out.println("inspect chest");
//                System.out.println("destroy chest");
//                System.out.println("open chest");
//                System.out.println("exit");
//            } else {
//                String result = puzzle.handleCommand(player, input);
//                System.out.println(result);
//
//                if (puzzle.isFinished()) {
//                    if (puzzle.isCombatTriggered()) {
//                        Monster monster = puzzle.getFailureMonster();
//                        if (monster != null) {
//                            Room currentRoom = roomManager.getCurrentRoom();
//                            currentRoom.setMonster(monster);
//                            startCombat();
//                        }
//                        if (!player.isAlive()) {
//                            gameView.displayGameOver();
//                            isRunning = false;
//                            return;
//                        }
//                        System.out.println("You have completed Trial of Trust (No Reward).");
//                        player.setCurrentRoomID("EZ-01");
//                    }
//                    roomManager.setPuzzle3Active(false);
//                    inPuzzle = false;
//                }
//            }
//        }
//    }
//
//    private void handlePuzzle4() {
//        if (!roomManager.isPuzzle4Active()) {
//            System.out.println("This trial is already complete.");
//            return;
//        }
//
//        Puzzle4Sacrifice puzzle = new Puzzle4Sacrifice();
//        String currentRoomId = roomManager.getCurrentRoom().getId();
//
//        if (!currentRoomId.equals("SC-01")) {
//            System.out.println("You are not in the Sacrifice Trial room.");
//            System.out.println("Go to the Sacrifice Antechamber first.");
//            return;
//        }
//
//        System.out.println(puzzle.startPuzzle());
//
//        boolean inPuzzle = true;
//        while (inPuzzle && !puzzle.isFinished() && isRunning) {
//            System.out.print("\n[Trial of Sacrifice] What would you like to do?\n> ");
//            String input = scanner.nextLine().trim().toLowerCase();
//
//            if (input.equals("exit")) {
//                System.out.println("You leave the trial room.");
//                inPuzzle = false;
//            } else if (input.equals("help")) {
//                System.out.println("take sword");
//                System.out.println("move bridge");
//                System.out.println("throw sword");
//                System.out.println("inspect bridge");
//                System.out.println("move forward");
//                System.out.println("exit");
//            } else {
//                String result = puzzle.handleCommand(player, input);
//                System.out.println(result);
//
//                if (puzzle.isFinished()) {
//                    if (puzzle.isCombatTriggered()) {
//                        Monster monster = puzzle.getFailureMonster();
//                        if (monster != null) {
//                            Room currentRoom = roomManager.getCurrentRoom();
//                            currentRoom.setMonster(monster);
//                            startCombat();
//                        }
//                        if (!player.isAlive()) {
//                            gameView.displayGameOver();
//                            isRunning = false;
//                            return;
//                        }
//                        System.out.println("You have completed Trial of Sacrifice (No Reward).");
//                        player.setCurrentRoomID("EZ-01");
//                    }
//                    roomManager.setPuzzle4Active(false);
//                    inPuzzle = false;
//                }
//            }
//        }
//    }
//}