import java.util.Scanner;

public class GameControllerNA {
    private Scanner scanner;
    private Player player;
    private RoomManager roomManager;
    private GameView gameView;
    private FileManager fileManager;
    private boolean isRunning;

    public GameControllerNA(Player player, RoomManager roomManager,
                            GameView gameView, FileManager fileManager) {
        this.scanner = new Scanner(System.in);
        this.player = player;
        this.roomManager = roomManager;
        this.gameView = gameView;
        this.fileManager = fileManager;
        this.isRunning = true;
    }

    public void startGame() {
        displayWelcome();

        boolean menuActive = true;

        while (menuActive) {
            System.out.println("\n========== MAIN MENU ==========");
            System.out.println("[1] Start New Game");
            System.out.println("[2] About");
            System.out.println("[3] Quit");
            System.out.println("================================");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim().toLowerCase();

            switch (choice) {
                case "1":
                case "start":
                    System.out.println("\nStarting new game...\n");
                    menuActive = false;
                    play();
                    break;
                case "2":
                case "about":
                    displayAbout();
                    break;
                case "3":
                case "quit":
                    System.out.println("Goodbye! Thanks for playing!");
                    menuActive = false;
                    isRunning = false;
                    break;
                default:
                    System.out.println("[ERROR] Invalid option. Please enter 1, 2, or 3.");
            }
        }
    }

    private void displayWelcome() {
        System.out.println("     /\\      /\\      /\\      /\\      /\\      /\\      /\\");
        System.out.println("    /  \\    /  \\    /  \\    /  \\    /  \\    /  \\    /  \\");
        System.out.println("    \\  /    \\  /    \\  /    \\  /    \\  /    \\  /    \\  /");
        System.out.println("     \\/      \\/      \\/      \\/      \\/      \\/      \\/");
        System.out.println();
        System.out.println("============== WELCOME TO THE DUNGEON OF TRIALS ==============");
        System.out.println("The Dungeon of Trials was constructed by a king to find a worthy successor.");
        System.out.println("The dungeon tests character across five themed trials.");
        System.out.println("Complete all five trials to reach the Final Trial and claim the Catalyst!");
        System.out.println("=================================================================\n");
    }

    private void displayAbout() {
        System.out.println("\n========================================================");
        System.out.println("                   DUNGEON OF TRIALS                      ");
        System.out.println("========================================================");
        System.out.println("A text-based adventure game where you must complete      ");
        System.out.println("five trials to prove yourself worthy.                    ");
        System.out.println("                                                          ");
        System.out.println("Each trial tests a different virtue:                     ");
        System.out.println("  - Awareness  - Observe carefully                       ");
        System.out.println("  - Restraint  - Control your greed                      ");
        System.out.println("  - Trust      - Have faith in your instincts            ");
        System.out.println("  - Sacrifice  - Give up power for greater good          ");
        System.out.println("  - Commitment - Stay focused on your goal               ");
        System.out.println("                                                          ");
        System.out.println("Type 'help' at any time to see available commands.       ");
        System.out.println("========================================================\n");
    }

    public void play() {
        gameView.displayHelp();
        roomManager.addPuzzleItemsToRooms();

        while (isRunning) {
            if (roomManager.getCurrentRoom() != null) {
                gameView.displayRoom(roomManager.getCurrentRoom());
            }

            Monster currentMonster = roomManager.getCurrentRoom().getMonster();
            if (currentMonster != null && currentMonster.isAlive()) {
                System.out.println("\nA " + currentMonster.getName() + " blocks your path!");
                startCombat();
                if (!player.isAlive()) {
                    gameView.displayGameOver();
                    break;
                }
                continue;
            }

            System.out.print("\nWhat would you like to do? (type 'help' for commands)\n> ");
            String command = scanner.nextLine().trim();
            processCommand(command);
            updateState();
        }
        scanner.close();
    }

    public void processCommand(String command) {
        if (command.isEmpty()) {
            System.out.println("[ERROR] Please enter a command. Type 'help' for available commands.");
            return;
        }

        String[] parts = command.split(" ", 2);
        String action = parts[0].toLowerCase();
        String arg = parts.length > 1 ? parts[1] : "";

        switch (action) {
            case "help":
                gameView.displayHelp();
                break;
            case "look":
            case "room":
                gameView.displayRoom(roomManager.getCurrentRoom());
                break;
            case "about":
                displayAbout();
                break;
            case "inspect":
                handleInspect(arg);
                break;
            case "take":
                handleTake(arg);
                break;
            case "drop":
                handleDrop(arg);
                break;
            case "inventory":
                gameView.displayInventory(player);
                break;
            case "equip":
                handleEquip(arg);
                break;
            case "unequip":
                handleUnequip();
                break;
            case "explore":
                handleExplore(arg);
                break;
            case "solve":
                handleSolve(arg);
                break;
            case "hint":
                handleHint(arg);
                break;
            case "status":
                gameView.displayStatus(player);
                break;
            case "attack":
            case "fight":
                handleAttack();
                break;
            case "consume":
                handleConsume(arg);
                break;
            case "move":
                handleMove(arg);
                break;
            case "save":
                handleSave();
                break;
            case "load":
                handleLoad();
                break;
            case "puzzle1":
            case "awareness":
                handlePuzzle1();
                break;
            case "puzzle2":
            case "restraint":
                handlePuzzle2();
                break;
            case "puzzle3":
            case "trust":
                handlePuzzle3();
                break;
            case "puzzle4":
            case "sacrifice":
                handlePuzzle4();
                break;
            case "puzzle5":
            case "commitment":
                handlePuzzle5();
                break;
            case "puzzle6":
            case "final":
            case "finaltrial":
                handlePuzzle6();
                break;
            case "puzzle7":
            case "trap":
                handlePuzzle7();
                break;
            case "exit":
                System.out.println("\nThanks for playing! Goodbye!\n");
                isRunning = false;
                break;
            default:
                System.out.println("[ERROR] Unknown command: '" + action + "'");
                System.out.println("Type 'help' to see all available commands.");
        }
    }

    private void handleInspect(String arg) {
        if (arg.isEmpty()) {
            System.out.println("[ERROR] Inspect what? Example: inspect room, inspect potion");
            return;
        }

        if (arg.equalsIgnoreCase("room")) {
            System.out.println(roomManager.getCurrentRoom().getDescription());
            return;
        }

        Item roomItem = roomManager.getCurrentRoom().findItem(arg);
        if (roomItem != null) {
            System.out.println(roomItem.getDescription());
            return;
        }

        Item invItem = player.findItem(arg);
        if (invItem != null) {
            System.out.println(invItem.getDescription());
            return;
        }

        System.out.println("[ERROR] No such item: " + arg);
    }

    private void handleTake(String arg) {
        if (arg.isEmpty()) {
            System.out.println("[ERROR] Take what? Example: take potion");
            return;
        }

        Item item = roomManager.getCurrentRoom().findItem(arg);
        if (item != null) {
            roomManager.getCurrentRoom().removeItem(item);
            player.addItem(item);
            System.out.println("You took: " + item.getName());
        } else {
            System.out.println("[ERROR] That item is not here.");
        }
    }

    private void handleDrop(String arg) {
        if (arg.isEmpty()) {
            System.out.println("[ERROR] Drop what? Example: drop potion");
            return;
        }

        Item item = player.findItem(arg);
        if (item != null) {
            player.removeItem(item);
            roomManager.getCurrentRoom().addItem(item);
            System.out.println("You dropped: " + item.getName());
        } else {
            System.out.println("[ERROR] You don't have that item.");
        }
    }

    private void handleEquip(String arg) {
        if (arg.isEmpty()) {
            System.out.println("[ERROR] Equip what? Example: equip sword");
            return;
        }

        Item item = player.findItem(arg);
        if (item instanceof Sword) {
            item.use(player);
            System.out.println("Equipped: " + item.getName());
        } else if (item == null) {
            System.out.println("[ERROR] You don't have that item.");
        } else {
            System.out.println("[ERROR] That's not a sword.");
        }
    }

    private void handleUnequip() {
        if (player.getEquippedSword() != null) {
            String swordName = player.getEquippedSword().getName();
            player.unequipSword();
            System.out.println("Unequipped: " + swordName);
        } else {
            System.out.println("[ERROR] No sword equipped.");
        }
    }

    private void handleExplore(String arg) {
        if (arg.isEmpty()) {
            System.out.println("[ERROR] Explore which trial? Example: explore awareness");
            return;
        }

        Puzzle puzzle = roomManager.getCurrentRoom().getPuzzle();
        if (puzzle != null && puzzle.getTrialName().equalsIgnoreCase(arg)) {
            gameView.displayPuzzle(puzzle);
        } else {
            System.out.println("[ERROR] No puzzle named '" + arg + "' here.");
        }
    }

    private void handleSolve(String arg) {
        if (arg.isEmpty()) {
            System.out.println("[ERROR] Solve which trial? Example: solve awareness");
            return;
        }

        Puzzle puzzle = roomManager.getCurrentRoom().getPuzzle();
        if (puzzle != null && puzzle.getTrialName().equalsIgnoreCase(arg)) {
            if (puzzle.isSolved()) {
                System.out.println("This puzzle is already solved!");
                return;
            }

            System.out.print("Enter your solution:\n> ");
            String solution = scanner.nextLine().trim();

            if (puzzle.checkSolution(solution)) {
                puzzle.setSolved(true);
                player.setMaxHP(player.getMaxHP() + 1);
                player.fullHeal();
                System.out.println("\nCORRECT! You completed the " + puzzle.getTrialName() + " Trial!");
                System.out.println("Max HP increased to " + player.getMaxHP() + " and fully healed!");
                System.out.println("You received a Trial Token!");
            } else {
                System.out.println("\nINCORRECT! The puzzle remains unsolved.");
                gameView.displayHint(puzzle.getHint());
            }
        } else {
            System.out.println("[ERROR] No puzzle named '" + arg + "' here.");
        }
    }

    private void handleHint(String arg) {
        if (arg.isEmpty()) {
            System.out.println("[ERROR] Hint for which trial? Example: hint awareness");
            return;
        }

        Puzzle puzzle = roomManager.getCurrentRoom().getPuzzle();
        if (puzzle != null && puzzle.getTrialName().equalsIgnoreCase(arg)) {
            gameView.displayHint(puzzle.getHint());
        } else {
            System.out.println("[ERROR] No puzzle named '" + arg + "' here.");
        }
    }

    private void handleAttack() {
        Monster monster = roomManager.getCurrentRoom().getMonster();
        if (monster != null && monster.isAlive()) {
            startCombat();
        } else {
            System.out.println("[ERROR] No monster to attack here!");
        }
    }

    private void handleConsume(String arg) {
        if (arg.isEmpty()) {
            System.out.println("[ERROR] Consume what? Example: consume potion");
            return;
        }

        Item item = player.findItem(arg);
        if (item instanceof Potion && ((Potion) item).getHealAmount() > 0) {
            int oldHP = player.getCurrentHP();
            item.use(player);
            player.removeItem(item);
            System.out.println("You consumed " + item.getName() + "!");
            System.out.println("HP: " + oldHP + " -> " + player.getCurrentHP() + "/" + player.getMaxHP());
        } else if (item == null) {
            System.out.println("[ERROR] You don't have that item.");
        } else {
            System.out.println("[ERROR] You can only consume potions.");
        }
    }

    private void handleMove(String arg) {
        if (arg.isEmpty()) {
            System.out.println("[ERROR] Move where? Examples:");
            System.out.println("  - move 0 (move by number)");
            System.out.println("  - move 'Trial Room' (move by name)");
            return;
        }

        try {
            int index = Integer.parseInt(arg);
            roomManager.move(index);
            player.setCurrentRoomID(roomManager.getRoomId());
            System.out.println("\nYou move to: " + roomManager.getCurrentRoom().getName() + "\n");
        } catch (NumberFormatException e) {
            roomManager.moveToRoom(arg);
            player.setCurrentRoomID(roomManager.getRoomId());
        }
    }

    private void handleSave() {
        gameView.displaySaveSuccess();
    }

    private void handleLoad() {
        gameView.displayLoadSuccess();
    }

    private void startCombat() {
        Room currentRoom = roomManager.getCurrentRoom();
        Monster monster = currentRoom.getMonster();

        if (monster == null || !monster.isAlive()) {
            System.out.println("No monster here.");
            return;
        }

        Combat combat = new Combat(player, monster);
        combat.startBattle(gameView, scanner, currentRoom);
    }

    public void updateState() {
        if (!player.isAlive()) {
            gameView.displayGameOver();
            isRunning = false;
        }
    }

    // ==================== PUZZLE 1: AWARENESS ====================
    private void handlePuzzle1() {
        if (!roomManager.isPuzzle1Active()) {
            System.out.println("This trial is already complete.");
            return;
        }

        Puzzle1_Awareness puzzle = roomManager.getPuzzle1();
        String currentRoomId = roomManager.getCurrentRoom().getId();

        if (!currentRoomId.equals("AW-02")) {
            System.out.println("You are not in the Awareness Trial room.");
            System.out.println("Go to the Awareness Chamber first.");
            return;
        }

        puzzle.displayDescription(gameView);

        boolean inPuzzle = true;
        while (inPuzzle && !puzzle.isCompleted() && isRunning) {
            System.out.print("\n[Trial of Awareness] What would you like to do?\n> ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("exit")) {
                System.out.println("You leave the trial room.");
                inPuzzle = false;
            } else if (input.equals("hint")) {
                puzzle.displayHint(gameView);
            } else if (input.startsWith("take ")) {
                String itemName = input.substring(5);
                if (itemName.equals("gem") || itemName.equals("glowing red gem")) {
                    if (!puzzle.isGemTaken()) {
                        player.addItem(puzzle.getGlowingRedGem());
                        puzzle.setGemTaken(true);
                        System.out.println("You took the Glowing Red Gem.");
                    } else {
                        System.out.println("The gem is already taken.");
                    }
                } else if (itemName.equals("rubble")) {
                    if (!puzzle.isRubbleTaken()) {
                        player.addItem(puzzle.getRubble());
                        puzzle.setRubbleTaken(true);
                        System.out.println("You took the rubble.");
                    } else {
                        System.out.println("The rubble is already taken.");
                    }
                } else {
                    System.out.println("You can't take that.");
                }
            } else if (input.startsWith("throw ")) {
                String itemName = input.substring(6).replace(" to teleporter", "").trim();
                int result = puzzle.processThrow(itemName, gameView, player, scanner);

                if (result == 1) {
                    puzzle.completePuzzle(gameView, player, roomManager);
                    roomManager.setPuzzle1Active(false);
                    inPuzzle = false;
                } else if (result == -1) {
                    if (player.isAlive()) {
                        puzzle.handlePenalty(gameView, player, roomManager);
                        roomManager.setPuzzle1Active(false);
                        inPuzzle = false;
                    } else {
                        gameView.displayGameOver();
                        isRunning = false;
                        inPuzzle = false;
                    }
                }
            } else {
                System.out.println("Invalid command. Available: take gem, take rubble, throw <item>, hint, exit");
            }
        }
    }

    // ==================== PUZZLE 2: RESTRAINT ====================
    private void handlePuzzle2() {
        if (!roomManager.isPuzzle2Active()) {
            System.out.println("This trial is already complete.");
            return;
        }

        Puzzle2_Restraint puzzle = roomManager.getPuzzle2();
        String currentRoomId = roomManager.getCurrentRoom().getId();

        if (!currentRoomId.equals("RS-02")) {
            System.out.println("You are not in the Restraint Trial room.");
            System.out.println("Go to the Restraint Chamber first.");
            return;
        }

        puzzle.displayDescription(gameView);

        boolean inPuzzle = true;
        while (inPuzzle && !puzzle.isCompleted() && isRunning) {
            System.out.print("\n[Trial of Restraint] What would you like to do?\n> ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("exit")) {
                System.out.println("You leave the trial room.");
                inPuzzle = false;
            } else if (input.equals("hint")) {
                puzzle.displayHint(gameView);
            } else if (input.equals("take coin")) {
                if (!puzzle.isCoinTaken()) {
                    player.addItem(puzzle.getBaitCoin());
                    puzzle.setCoinTaken(true);
                    System.out.println("You took the Bait Coin.");
                } else {
                    System.out.println("The coin is already taken.");
                }
            } else if (input.equals("examine chest")) {
                boolean mimicTriggered = puzzle.examineChest(gameView, player);
                if (mimicTriggered) {
                    roomManager.spawnMimicInRestraintRoom();
                    Monster mimic = roomManager.getCurrentRoom().getMonster();
                    if (mimic != null) {
                        Combat combat = new Combat(player, mimic);
                        combat.startBattle(gameView, scanner, roomManager.getCurrentRoom());
                    }

                    if (!player.isAlive()) {
                        gameView.displayGameOver();
                        isRunning = false;
                        return;
                    }

                    puzzle.onMimicDefeated(gameView, player, roomManager);
                    roomManager.setPuzzle2Active(false);
                    inPuzzle = false;
                }
            } else if (input.equals("open chest")) {
                puzzle.openChest(gameView, player);
            } else if (input.equals("place coin")) {
                puzzle.placeCoin(gameView);
            } else if (input.equals("leave") || input.equals("complete")) {
                puzzle.completePuzzle(gameView, player, roomManager, true);
                if (puzzle.isCompleted()) {
                    roomManager.setPuzzle2Active(false);
                    inPuzzle = false;
                }
            } else {
                System.out.println("Invalid command. Available: take coin, examine chest, open chest, place coin, leave, hint, exit");
            }
        }
    }

    // ==================== PUZZLE 3: TRUST ====================
    private void handlePuzzle3() {
        if (!roomManager.isPuzzle3Active()) {
            System.out.println("This trial is already complete.");
            return;
        }

        String currentRoomId = roomManager.getCurrentRoom().getId();
        if (!currentRoomId.equals("TR-02")) {
            System.out.println("You are not in the Trust Trial room.");
            System.out.println("Go to the Trust Chamber first.");
            return;
        }

        System.out.println("\n==== Welcome to the Trial of Trust ====");
        System.out.println("You stand before a guardian statue... it watches your every move.");
        System.out.println("Hint: Not everything should be taken at face value.");
        System.out.println("Sometimes trust must be placed in action, not reward.");

        boolean guardianBroken = false;
        boolean chestAppeared = false;
        boolean trialFinished = false;

        while (!trialFinished && isRunning) {
            System.out.print("\n[Trial of Trust] What would you like to do?\n> ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("exit")) {
                System.out.println("You leave the trial room.");
                trialFinished = true;
            } else if (input.equals("help")) {
                System.out.println("attack guardian - Break the guardian statue");
                System.out.println("inspect chest - Examine the chest");
                System.out.println("destroy chest - Destroy the chest (WIN)");
                System.out.println("open chest - Open the chest (LOSE - combat)");
                System.out.println("exit - Leave the trial");
            } else if (input.equals("attack guardian") || input.equals("attack guardian statue")) {
                if (guardianBroken) {
                    System.out.println("The guardian statue is already broken.");
                } else {
                    guardianBroken = true;
                    chestAppeared = true;
                    System.out.println("The guardian breaks.");
                    System.out.println("A chest appears.");
                }
            } else if (input.equals("inspect chest") || input.equals("examine chest")) {
                if (!chestAppeared) {
                    System.out.println("There is no chest here yet.");
                } else {
                    System.out.println("The chest looks tempting, but something feels wrong about it.");
                }
            } else if (input.equals("destroy chest")) {
                if (!guardianBroken) {
                    System.out.println("You need to break the guardian first.");
                } else {
                    // WIN PATH
                    player.modifyMaxHP(1);
                    player.healToFull();
                    player.addTrialToken();
                    player.setCurrentRoomID("EZ-01");
                    roomManager.setPuzzle3Active(false);
                    trialFinished = true;
                    System.out.println("\nYou saw through the illusion and made the right choice.");
                    System.out.println("You have completed the Trial of Trust and have been teleported to the entrance zone!");
                    System.out.println("You get +1 Max HP, Trial Token, full HP restore.");
                }
            } else if (input.equals("open chest")) {
                if (!chestAppeared) {
                    System.out.println("There is no chest here to open.");
                } else {
                    // LOSE PATH - combat
                    player.takeDamage(1);
                    System.out.println("\nThe guardian reforms and attacks you!");
                    System.out.println("You lose 1 HP.");
                    System.out.println("Combat begins!");

                    // Spawn Guardian monster
                    Monster guardian = new Monster("M-02", "Guardian", 2, 1, false);
                    guardian.addReward(new Potion("I-13", "Silver Sigil", "A valuable silver sigil.", false, 0));
                    roomManager.getCurrentRoom().setMonster(guardian);

                    // Start combat
                    startCombat();

                    if (!player.isAlive()) {
                        gameView.displayGameOver();
                        isRunning = false;
                        return;
                    }

                    System.out.println("\nYou have completed Trial of Trust (No Reward)");
                    player.setCurrentRoomID("EZ-01");
                    roomManager.setPuzzle3Active(false);
                    trialFinished = true;
                }
            } else {
                System.out.println("Invalid command. Type 'help' for available commands.");
            }
        }
    }

    // ==================== PUZZLE 4: SACRIFICE ====================
    private void handlePuzzle4() {
        if (!roomManager.isPuzzle4Active()) {
            System.out.println("This trial is already complete.");
            return;
        }

        String currentRoomId = roomManager.getCurrentRoom().getId();

        // Check which room we're in
        if (!currentRoomId.equals("SC-01") && !currentRoomId.equals("SC-02") && !currentRoomId.equals("SC-03")) {
            System.out.println("You are not in the Sacrifice Trial.");
            System.out.println("Go to the Sacrifice Chamber first.");
            return;
        }

        // State tracking for this puzzle session
        boolean swordTaken = false;
        boolean reachedBridge = false;
        boolean swordThrown = false;
        boolean trialFinished = false;

        System.out.println("\n=== Welcome to the Trial of Sacrifice ===");
        System.out.println("A powerful sword rests before you... but not everything is meant to be kept.");
        System.out.println("A bridge lies ahead... something waits at the end.");
        System.out.println("Hint: Not all strength should be carried forward.");

        while (!trialFinished && isRunning) {
            System.out.print("\n[Trial of Sacrifice] What would you like to do?\n> ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("exit")) {
                System.out.println("You leave the trial room.");
                trialFinished = true;
            } else if (input.equals("help")) {
                System.out.println("take sword - Take the sword");
                System.out.println("move bridge - Move onto the bridge");
                System.out.println("throw sword - Throw the sword away");
                System.out.println("inspect bridge - Examine the bridge");
                System.out.println("move forward - Move to the end");
                System.out.println("exit - Leave the trial");
            } else if (input.equals("take sword")) {
                if (swordTaken) {
                    System.out.println("You already took the sword.");
                } else {
                    swordTaken = true;
                    Sword trialSword = new Sword("TRIAL-SWORD", "Trial Sword",
                            "A powerful sword used in the Trial of Sacrifice.", false, 3, 3);
                    player.addItem(trialSword);
                    System.out.println("You took the sword.");
                }
            } else if (input.equals("move bridge")) {
                if (!swordTaken) {
                    System.out.println("You need to take the sword first.");
                } else {
                    reachedBridge = true;
                    System.out.println("You move onto the bridge.");
                    // Move player to SC-02
                    Room sc02 = roomManager.findRoomByName("Long Bridge");
                    if (sc02 != null) {
                        player.setCurrentRoomID("SC-02");
                    }
                }
            } else if (input.equals("throw sword")) {
                if (!reachedBridge) {
                    System.out.println("You need to reach the bridge first.");
                } else if (swordThrown) {
                    System.out.println("You already threw the sword.");
                } else {
                    Item sword = player.findItemByName("Trial Sword");
                    if (sword != null) {
                        player.removeItem(sword);
                        if (player.getEquippedSword() == sword) {
                            player.unequipSword();
                        }
                    }
                    swordThrown = true;
                    System.out.println("You throw the sword away before reaching the end.");
                }
            } else if (input.equals("inspect bridge") || input.equals("examine bridge")) {
                if (!reachedBridge) {
                    System.out.println("You are not at the bridge yet.");
                } else if (!swordThrown) {
                    System.out.println("The bridge feels unsafe while you still carry the sword.");
                } else {
                    System.out.println("The bridge seems calm now.");
                }
            } else if (input.equals("move forward")) {
                if (!swordTaken) {
                    System.out.println("You need to take the sword first.");
                } else if (!reachedBridge) {
                    System.out.println("You need to move to the bridge first.");
                } else if (swordThrown) {
                    // WIN PATH
                    player.modifyMaxHP(1);
                    player.healToFull();
                    player.addTrialToken();
                    player.setCurrentRoomID("EZ-01");
                    roomManager.setPuzzle4Active(false);
                    trialFinished = true;
                    System.out.println("\nYou chose to let go of power and were spared.");
                    System.out.println("You have completed the Trial of Sacrifice and have been teleported to the entrance zone!");
                    System.out.println("You get +1 Max HP, Trial Token, full HP restore.");
                } else {
                    // LOSE PATH - keep sword, combat with Wraith
                    Item sword = player.findItemByName("Trial Sword");
                    if (sword != null) {
                        player.removeItem(sword);
                        if (player.getEquippedSword() == sword) {
                            player.unequipSword();
                        }
                    }

                    System.out.println("\nThe power you held has betrayed you.");
                    System.out.println("You are attacked by the Wraith!");
                    System.out.println("Combat begins!");

                    Monster wraith = new Monster("M-SAC", "Wraith", 2, 1, false);
                    roomManager.getCurrentRoom().setMonster(wraith);
                    startCombat();

                    if (!player.isAlive()) {
                        gameView.displayGameOver();
                        isRunning = false;
                        return;
                    }

                    System.out.println("\nYou have completed Trial of Sacrifice (No Reward)");
                    player.setCurrentRoomID("EZ-01");
                    roomManager.setPuzzle4Active(false);
                    trialFinished = true;
                }
            } else {
                System.out.println("Invalid command. Type 'help' for available commands.");
            }
        }
    }

    // ==================== PUZZLE 5: COMMITMENT ====================
    private void handlePuzzle5() {
        if (!roomManager.isPuzzle5Active()) {
            System.out.println("This trial is already complete.");
            return;
        }

        String currentRoomId = roomManager.getCurrentRoom().getId();
        if (!currentRoomId.equals("CM-01")) {
            System.out.println("You are not in the Commitment Trial.");
            System.out.println("Go to Commitment Hall first.");
            return;
        }

        System.out.println("\n===== Trial of Commitment =====");
        System.out.println("You hear heavy footsteps echoing from the entrance... Commit to your path. Do not linger.");
        System.out.println("Hint: The Pursuer is two rooms behind you. Every time you stop to examine or take an item, it gets closer.");

        int forwardMoves = 0;
        int takeCount = 0;
        boolean trialFinished = false;

        while (!trialFinished && isRunning) {
            System.out.print("\n[Trial of Commitment] What would you like to do?\n> ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("exit")) {
                System.out.println("You leave the trial room.");
                trialFinished = true;
            } else if (input.equals("help")) {
                System.out.println("move forward - Move forward through the corridor");
                System.out.println("take item - Take an item (Pursuer gets closer)");
                System.out.println("examine item - Examine an item (Pursuer gets closer)");
                System.out.println("exit - Leave the trial");
            } else if (input.equals("move forward")) {
                forwardMoves++;
                if (forwardMoves >= 6) {
                    // WIN PATH
                    player.modifyMaxHP(1);
                    player.healToFull();
                    player.addTrialToken();
                    player.setCurrentRoomID("EZ-01");
                    roomManager.setPuzzle5Active(false);
                    trialFinished = true;
                    System.out.println("\nYou stayed the course!");
                    System.out.println("Trial of Commitment Complete!");
                    System.out.println("You have been teleported to the Entrance Zone.");
                    System.out.println("You get +1 Max HP, Trial Token, full HP restore.");
                } else {
                    System.out.println("You move forward. Progress: " + forwardMoves + "/6");
                    // Move player to next CM room
                    if (forwardMoves == 1) {
                        Room cm02 = roomManager.findRoomByName("Dagger Room");
                        if (cm02 != null) player.setCurrentRoomID("CM-02");
                    } else if (forwardMoves == 2) {
                        Room cm03 = roomManager.findRoomByName("Chalice Room");
                        if (cm03 != null) player.setCurrentRoomID("CM-03");
                    } else if (forwardMoves == 3) {
                        Room cm04 = roomManager.findRoomByName("Lens Room");
                        if (cm04 != null) player.setCurrentRoomID("CM-04");
                    } else if (forwardMoves == 4) {
                        Room cm05 = roomManager.findRoomByName("Idol Room");
                        if (cm05 != null) player.setCurrentRoomID("CM-05");
                    } else if (forwardMoves == 5) {
                        Room cm06 = roomManager.findRoomByName("Map Room");
                        if (cm06 != null) player.setCurrentRoomID("CM-06");
                    }
                }
            } else if (input.equals("examine item") || input.equals("inspect item")) {
                // LOSE - caught by Pursuer
                player.takeDamage(1);
                player.setCurrentRoomID("EZ-01");
                roomManager.setPuzzle5Active(false);
                trialFinished = true;
                System.out.println("\nToo slow! The Pursuer caught you! You lose 1 HP.");
                System.out.println("You have failed the Trial of Commitment. You must try again.");
            } else if (input.equals("take item")) {
                takeCount++;
                if (takeCount >= 2) {
                    // LOSE - caught by Pursuer
                    player.takeDamage(1);
                    player.setCurrentRoomID("EZ-01");
                    roomManager.setPuzzle5Active(false);
                    trialFinished = true;
                    System.out.println("\nToo slow! The Pursuer caught you! You lose 1 HP.");
                    System.out.println("You have failed the Trial of Commitment. You must try again.");
                } else {
                    System.out.println("You take an item. The Pursuer gets closer!");
                }
            } else {
                System.out.println("Invalid command. Type 'help' for available commands.");
            }
        }
    }

    // ==================== PUZZLE 6: FINAL TRIAL ====================
    private void handlePuzzle6() {
        if (!roomManager.isPuzzle6Active()) {
            System.out.println("The Final Trial is already complete.");
            return;
        }

        String currentRoomId = roomManager.getCurrentRoom().getId();
        if (!currentRoomId.equals("FN-02")) {
            System.out.println("You are not in the Final Trial room.");
            System.out.println("Go to The Last Mechanism first.");
            return;
        }

        int tokenCount = player.getTrialTokens();
        boolean hasFiveTokens = (tokenCount >= 5);

        System.out.println("\n===== Welcome to the Final Trial =====");
        System.out.println("Everything you have learned will now be tested.");
        System.out.println("A chest burns before you... a statue looms nearby... the floor beneath you feels unstable.");
        System.out.println("Hint: Do not extinguish the fire. Timing and order matter.");
        System.out.println("Some actions cannot be undone.");

        if (hasFiveTokens) {
            System.out.println("\nYou have 5 Trial Tokens! The teleporter is already stabilized.");
            System.out.println("Enter the teleporter to claim your victory!");
        }

        // Step tracking
        boolean chestBurned = false;
        boolean floorCracked = false;
        boolean exploderUsed = false;
        boolean corePlaced = false;
        boolean jewelRevealed = false;
        boolean jewelThrown = false;
        int waitCounter = 0;
        boolean trialFinished = false;

        while (!trialFinished && isRunning) {
            System.out.print("\n[Final Trial] What would you like to do?\n> ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("exit")) {
                System.out.println("You leave the trial room.");
                trialFinished = true;
            } else if (input.equals("help")) {
                System.out.println("burn chest - Burn the chest");
                System.out.println("wait - Wait (for cracked floor)");
                System.out.println("use explosive - Use explosive device on statue");
                System.out.println("place core - Place core fragment in pillar");
                System.out.println("step floor - Step on cracked floor");
                System.out.println("throw jewel - Throw jewel on teleporter");
                System.out.println("enter teleporter - Enter the teleporter");
                System.out.println("exit - Leave the trial");
            } else if (hasFiveTokens && input.equals("enter teleporter")) {
                System.out.println("\nYou enter the stabilized teleporter.");
                player.setCurrentRoomID("END-01");
                System.out.println("\nYou arrive at the Ascension Chamber.");
                System.out.println("You grab the Catalyst... YOU WIN!");
                roomManager.setPuzzle6Active(false);
                trialFinished = true;
                isRunning = false;
            } else if (input.equals("burn chest")) {
                if (chestBurned) {
                    System.out.println("The chest is already burning.");
                } else {
                    chestBurned = true;
                    System.out.println("You burn the chest. The fire crackles and grows.");
                }
            } else if (input.equals("wait")) {
                if (!chestBurned) {
                    System.out.println("Nothing happens. The chest is not burning.");
                } else if (floorCracked) {
                    System.out.println("The floor symbol is already visible.");
                } else {
                    waitCounter++;
                    if (waitCounter >= 3) {
                        floorCracked = true;
                        System.out.println("After waiting, a cracked floor symbol appears.");
                    } else {
                        System.out.println("You wait. Nothing happens yet. (" + waitCounter + "/3)");
                    }
                }
            } else if (input.equals("use explosive")) {
                if (!floorCracked) {
                    System.out.println("You need to see the floor symbol first.");
                } else if (exploderUsed) {
                    System.out.println("You already used the explosive device.");
                } else {
                    Item explosive = player.findItem("Explosive Device");
                    if (explosive == null) {
                        System.out.println("You don't have the Explosive Device. Find it in the hidden room first.");
                    } else {
                        player.removeItem(explosive);
                        exploderUsed = true;
                        System.out.println("You insert the explosive device into the statue. The statue shatters!");
                        System.out.println("A Core Fragment drops.");
                        // Add Core Fragment to inventory
                        Item coreFragment = new Potion("I-05", "Core Fragment",
                                "A glowing shard from the shattered monster statue.", false, 0);
                        player.addItem(coreFragment);
                    }
                }
            } else if (input.equals("place core")) {
                if (!exploderUsed) {
                    System.out.println("You need to shatter the statue first.");
                } else if (corePlaced) {
                    System.out.println("Core already placed.");
                } else {
                    Item core = player.findItem("Core Fragment");
                    if (core == null) {
                        System.out.println("You don't have the Core Fragment.");
                    } else {
                        corePlaced = true;
                        System.out.println("You place the Core Fragment into the broken pillar.");
                        System.out.println("The teleporter activates (unstable).");
                    }
                }
            } else if (input.equals("step floor")) {
                if (!corePlaced) {
                    System.out.println("You need to place the core first.");
                } else if (jewelRevealed) {
                    System.out.println("The jewel is already revealed.");
                } else {
                    jewelRevealed = true;
                    System.out.println("You step onto the cracked floor symbol. The floor collapses!");
                    System.out.println("A Final Jewel appears.");
                }
            } else if (input.equals("throw jewel")) {
                if (!jewelRevealed) {
                    System.out.println("The Final Jewel has not appeared yet.");
                } else if (jewelThrown) {
                    System.out.println("You already threw the jewel.");
                } else {
                    jewelThrown = true;
                    System.out.println("You throw the Final Jewel onto the teleporter. It stabilizes!");
                    System.out.println("You enter the teleporter and arrive at the Ascension Chamber.");
                    System.out.println("You grab the Catalyst... YOU WIN!");
                    player.modifyMaxHP(1);
                    player.healToFull();
                    player.addTrialToken();
                    player.setCurrentRoomID("END-01");
                    roomManager.setPuzzle6Active(false);
                    trialFinished = true;
                    isRunning = false;
                }
            } else if (input.equals("enter teleporter") && !jewelThrown) {
                System.out.println("The unstable teleporter pulls you into the Trap Room!");
                player.setCurrentRoomID("TP-TRAP-01");
                roomManager.setPuzzle6Active(false);
                trialFinished = true;
            } else {
                System.out.println("Invalid command. Type 'help' for available commands.");
            }
        }
    }

    // ==================== PUZZLE 7: TRAP ROOM ====================
    private void handlePuzzle7() {
        if (!roomManager.isPuzzle7Active()) {
            System.out.println("This trial is already complete.");
            return;
        }

        Puzzle7_Trap puzzle = roomManager.getPuzzle7();
        String currentRoomId = roomManager.getCurrentRoom().getId();

        if (!currentRoomId.equals("TP-TRAP-01")) {
            System.out.println("You are not in the Trap room.");
            System.out.println("Go to the Warden Arena first.");
            return;
        }

        puzzle.displayDescription(gameView);

        boolean inPuzzle = true;
        while (inPuzzle && !puzzle.isCompleted() && isRunning) {
            System.out.print("\n[Trap] What would you like to do?\n> ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("exit")) {
                System.out.println("You leave the trap room.");
                inPuzzle = false;
            } else if (input.equals("hint")) {
                puzzle.displayHint(gameView);
            } else if (input.startsWith("take ")) {
                String itemName = input.substring(5);
                if (itemName.equals("gem") || itemName.equals("glowing red gem")) {
                    if (!puzzle.isGemTaken()) {
                        player.addItem(puzzle.getGlowingRedGem());
                        puzzle.setGemTaken(true);
                        System.out.println("You took the Glowing Red Gem.");
                    } else {
                        System.out.println("The gem is already taken.");
                    }
                } else if (itemName.equals("rubble")) {
                    if (!puzzle.isRubbleTaken()) {
                        player.addItem(puzzle.getRubble());
                        puzzle.setRubbleTaken(true);
                        System.out.println("You took the rubble.");
                    } else {
                        System.out.println("The rubble is already taken.");
                    }
                } else {
                    System.out.println("You can't take that.");
                }
            } else if (input.startsWith("throw ")) {
                String itemName = input.substring(6).replace(" to teleporter", "").trim();
                int result = puzzle.processThrow(itemName, gameView, player, scanner);

                if (result == 1) {
                    puzzle.completePuzzle(gameView, player, roomManager);
                    roomManager.setPuzzle7Active(false);
                    inPuzzle = false;
                } else if (result == -1) {
                    if (player.isAlive()) {
                        puzzle.completePuzzle(gameView, player, roomManager);
                        roomManager.setPuzzle7Active(false);
                        inPuzzle = false;
                    } else {
                        gameView.displayGameOver();
                        isRunning = false;
                        inPuzzle = false;
                    }
                }
            } else {
                System.out.println("Invalid command. Available: take gem, take rubble, throw <item>, hint, exit");
            }
        }
    }
}