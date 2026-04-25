import java.util.HashMap;

public class GameModel {
    private Player player;
    private RoomManager roomManager;
    private Puzzle activePuzzle;

    private HashMap<String, Monster> monsterTemplates;
    private HashMap<String, String> puzzleRoomMap;

    public GameModel(Player player, RoomManager roomManager) {
        this.player = player;
        this.roomManager = roomManager;
        this.activePuzzle = null;

        this.monsterTemplates = new HashMap<>();
        this.puzzleRoomMap = new HashMap<>();

        loadMonsters("monster.txt");
        loadPuzzles("puzzle.txt");
    }

    public Player getPlayer() {
        return player;
    }

    public RoomManager getRoomManager() {
        return roomManager;
    }

    public Puzzle getActivePuzzle() {
        return activePuzzle;
    }

    public boolean hasActivePuzzle() {
        return activePuzzle != null && !activePuzzle.isFinished();
    }

    public void clearActivePuzzle() {
        activePuzzle = null;
    }

    public GameResult lookRoom() {
        Room room = roomManager.getCurrentRoom();
        if (room == null) {
            return new GameResult("No room loaded.");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(room.getRoomName()).append(" ===\n");
        sb.append(room.getRoomDesc());

        if (!room.getItems().isEmpty()) {
            sb.append("\n\nItems in room:");
            for (Item item : room.getItems()) {
                sb.append("\n- ").append(item.getItemName());
            }
        }

        if (!room.getConnections().isEmpty()) {
            sb.append("\n\nConnections:");
            for (int i = 0; i < room.getConnections().size(); i++) {
                sb.append("\n").append(i).append(": ")
                        .append(room.getConnections().get(i).getRoomName());
            }
        }

        return new GameResult(sb.toString());
    }

    public GameResult move(String command) {
        if (command == null || command.isEmpty()) {
            GameResult result = new GameResult("Move where?");
            result.setSuccess(false);
            return result;
        }

        String[] parts = command.trim().split(" ");
        if (parts.length < 2) {
            GameResult result = new GameResult("Use: move [number]");
            result.setSuccess(false);
            return result;
        }

        try {
            int index = Integer.parseInt(parts[1]);
            Room current = roomManager.getCurrentRoom();

            if (current == null) {
                GameResult result = new GameResult("No current room loaded.");
                result.setSuccess(false);
                return result;
            }

            if (index < 0 || index >= current.getConnections().size()) {
                GameResult result = new GameResult("Invalid room connection.");
                result.setSuccess(false);
                return result;
            }

            Room destination = current.getConnections().get(index);
            String destinationId = destination.getRoomId();

            if ((destinationId.equals("FN-01") || destinationId.equals("FN-02") || destinationId.equals("EZ-02"))
                    && player.getTrialTokens() < 5) {
                GameResult result = new GameResult("That area is locked. Complete all 5 trials first.");
                result.setSuccess(false);
                return result;
            }

            String destinationTrial = getTrialKeyForRoom(destinationId);

            if (destinationTrial != null
                    && player.hasCompletedTrial(destinationTrial)
                    && !destinationId.equals("TP-TRAP-01")
                    && !destinationId.equals("END-01")
                    && !destinationId.equals("FN-01")
                    && !destinationId.equals("FN-02")) {
                GameResult result = new GameResult("That trial has already been completed.");
                result.setSuccess(false);
                return result;
            }

            roomManager.move(index);
            player.setCurrentRoomId(roomManager.getRoomId());

            return new GameResult("You moved to " + roomManager.getCurrentRoom().getRoomName());
        } catch (NumberFormatException e) {
            GameResult result = new GameResult("Use a number. Example: move 0");
            result.setSuccess(false);
            return result;
        }
    }

    public GameResult takeItem(String command) {
        if (command == null || command.length() <= 5) {
            GameResult result = new GameResult("Take what?");
            result.setSuccess(false);
            return result;
        }

        String itemName = command.substring(5).trim();
        Room room = roomManager.getCurrentRoom();

        if (room == null) {
            GameResult result = new GameResult("No room loaded.");
            result.setSuccess(false);
            return result;
        }

        Item item = room.takeItemByName(itemName);

        if (item == null) {
            GameResult result = new GameResult("That item is not in this room.");
            result.setSuccess(false);
            return result;
        }

        player.addItem(item);
        return new GameResult("You picked up: " + item.getItemName());
    }

    public GameResult dropItem(String command) {
        if (command == null || command.length() <= 5) {
            GameResult result = new GameResult("Drop what?");
            result.setSuccess(false);
            return result;
        }

        String itemName = command.substring(5).trim();
        Item item = player.findItemByName(itemName);

        if (item == null) {
            GameResult result = new GameResult("You do not have that item.");
            result.setSuccess(false);
            return result;
        }

        player.removeItem(item);
        roomManager.getCurrentRoom().addItem(item);
        return new GameResult("You dropped: " + item.getItemName());
    }

    public GameResult useItem(String command) {
        if (command == null || command.trim().isEmpty()) {
            GameResult result = new GameResult("Use what?");
            result.setSuccess(false);
            return result;
        }

        String trimmed = command.trim();
        String lower = trimmed.toLowerCase();

        if (lower.startsWith("unequip ")) {
            String itemName = trimmed.substring(8).trim();
            Item item = player.findItemByName(itemName);

            if (item == null) {
                GameResult result = new GameResult("You do not have that item.");
                result.setSuccess(false);
                return result;
            }

            if (item instanceof Sword) {
                ((Sword) item).unequip(player);
                return new GameResult("You unequipped " + item.getItemName() + ".");
            }

            GameResult result = new GameResult("That item cannot be unequipped.");
            result.setSuccess(false);
            return result;
        }

        String itemName;
        if (lower.startsWith("use ")) {
            itemName = trimmed.substring(4).trim();
        } else if (lower.startsWith("consume ")) {
            itemName = trimmed.substring(8).trim();
        } else if (lower.startsWith("equip ")) {
            itemName = trimmed.substring(6).trim();
        } else {
            GameResult result = new GameResult("Invalid item command.");
            result.setSuccess(false);
            return result;
        }

        Item item = player.findItemByName(itemName);

        if (item == null) {
            GameResult result = new GameResult("You do not have that item.");
            result.setSuccess(false);
            return result;
        }

        item.use(player);

        if (item instanceof Potion) {
            player.removeItem(item);
            return new GameResult("You used a potion.");
        }

        if (lower.startsWith("equip ")) {
            return new GameResult("You equipped " + item.getItemName() + ".");
        }

        return new GameResult("You used " + item.getItemName() + ".");
    }

    public GameResult showInventory() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Inventory ===");

        if (player.getInventory().isEmpty()) {
            sb.append("\n- empty");
        } else {
            for (Item item : player.getInventory()) {
                sb.append("\n- ").append(item.getItemName());
            }
        }

        return new GameResult(sb.toString());
    }

    public GameResult showStatus() {
        StringBuilder sb = new StringBuilder();
        sb.append("Current Room: ").append(player.getCurrentRoomId());
        sb.append("\nCurrent HP: ").append(player.getCurrentHP());
        sb.append("\nMax HP: ").append(player.getMaxHP());
        sb.append("\nAttack Power: ").append(player.getAttackPower());
        sb.append("\nTrial Tokens: ").append(player.getTrialTokens());

        return new GameResult(sb.toString());
    }

    public GameResult saveGame() {
        if (hasActivePuzzle()) {
            GameResult result = new GameResult("You cannot save in the middle of a puzzle. Finish or fail the puzzle first.");
            result.setSuccess(false);
            return result;
        }

        FileManager.savePlayer("save.txt", player);
        return new GameResult("Game saved.");
    }

    public GameResult loadGame() {
        Player loaded = FileManager.loadPlayer("save.txt");

        if (loaded == null) {
            GameResult result = new GameResult("Could not load save file.");
            result.setSuccess(false);
            return result;
        }

        this.player = loaded;
        roomManager.setRoom(player.getCurrentRoomId());
        activePuzzle = null;

        return new GameResult("Game loaded.");
    }

    public GameResult autoStartPuzzleIfPresent() {
        String roomId = player.getCurrentRoomId();
        String trialKey = getTrialKeyForRoom(roomId);

        if (trialKey != null && player.hasCompletedTrial(trialKey)) {
            return null;
        }

        if (!puzzleRoomMap.containsKey(roomId)) {
            return null;
        }

        String puzzleID = puzzleRoomMap.get(roomId);
        Puzzle puzzle = createPuzzleById(puzzleID);

        if (puzzle == null) {
            return null;
        }

        activePuzzle = puzzle;

        GameResult result = new GameResult(puzzle.startPuzzle());
        result.setPuzzleStarted(true);
        result.setPuzzle(puzzle);
        return result;
    }

    public GameResult handlePuzzleCommand(String command) {
        if (activePuzzle == null) {
            GameResult result = new GameResult("There is no active puzzle.");
            result.setSuccess(false);
            return result;
        }

        GameResult result = new GameResult(activePuzzle.handleCommand(player, command));
        result.setPuzzle(activePuzzle);

        Monster monster = extractFailureMonster(activePuzzle);
        if (monster != null) {
            result.setCombatStarted(true);
            result.setMonster(monster);
        }

        if (activePuzzle.isFinished()) {
            result.setPuzzleFinished(true);
        }

        roomManager.setRoom(player.getCurrentRoomId());
        return result;
    }

    public GameResult startCombatForCurrentRoom() {
        Monster monster = getMonsterForCurrentRoom();

        if (monster == null) {
            GameResult result = new GameResult("There is no monster here.");
            result.setSuccess(false);
            return result;
        }

        GameResult result = new GameResult("Combat begins!");
        result.setCombatStarted(true);
        result.setMonster(monster);
        return result;
    }

    public void markTrialCompletedForPuzzle(Puzzle puzzle) {
        if (puzzle == null) {
            return;
        }

        String key = getTrialKeyForPuzzle(puzzle);
        if (key == null) {
            return;
        }

        if (puzzle instanceof Puzzle1Awareness && player.getCurrentRoomId().equals("TP-TRAP-01")) {
            return;
        }

        player.markTrialCompleted(key);
    }

    private String getTrialKeyForRoom(String roomId) {
        switch (roomId) {
            case "AW-02":
                return "AWARENESS";
            case "RS-02":
                return "RESTRAINT";
            case "TR-02":
                return "TRUST";
            case "SC-01":
                return "SACRIFICE";
            case "CM-01":
                return "COMMITMENT";
            case "TP-TRAP-01":
                return "TRAP";
            case "FN-02":
                return "FINAL";
            default:
                return null;
        }
    }

    private String getTrialKeyForPuzzle(Puzzle puzzle) {
        if (puzzle instanceof Puzzle1Awareness) {
            return "AWARENESS";
        }
        if (puzzle instanceof Puzzle2Restraint) {
            return "RESTRAINT";
        }
        if (puzzle instanceof Puzzle3Trust) {
            return "TRUST";
        }
        if (puzzle instanceof Puzzle4Sacrifice) {
            return "SACRIFICE";
        }
        if (puzzle instanceof Puzzle5Commitment) {
            return "COMMITMENT";
        }
        if (puzzle instanceof Puzzle7AwarenessTrap) {
            return "TRAP";
        }
        if (puzzle instanceof Puzzle6FinalTrial) {
            return "FINAL";
        }
        return null;
    }

    private void loadMonsters(String filename) {
        String fileData = FileManager.load(filename);
        String[] lines = fileData.split("\n");

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) {
                continue;
            }

            String[] parts = line.split(",");
            if (parts.length < 5) {
                continue;
            }

            String monsterID = parts[0].trim();
            String name = parts[1].trim();
            int hp = Integer.parseInt(parts[2].trim());
            int atkValue = Integer.parseInt(parts[3].trim());
            String reward = parts[4].trim();

            monsterTemplates.put(monsterID, new Monster(monsterID, name, hp, atkValue, reward));
        }
    }

    private void loadPuzzles(String filename) {
        String fileData = FileManager.load(filename);
        String[] lines = fileData.split("\n");

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) {
                continue;
            }

            String[] parts = line.split(",");
            if (parts.length < 4) {
                continue;
            }

            String puzzleID = parts[0].trim();
            String roomId = parts[2].trim();

            puzzleRoomMap.put(roomId, puzzleID);
        }
    }

    private Monster getMonsterForCurrentRoom() {
        if ("TP-TRAP-01".equals(player.getCurrentRoomId())) {
            return copyMonster("M-07");
        }
        return null;
    }

    private Monster copyMonster(String monsterID) {
        Monster template = monsterTemplates.get(monsterID);
        if (template == null) {
            return null;
        }

        Item reward = template.dropReward();
        String rewardName = reward == null ? "null" : reward.getItemName();

        return new Monster(
                template.getMonsterID(),
                template.getName(),
                template.getHp(),
                template.getAttackValue(),
                rewardName
        );
    }

    private Puzzle createPuzzleById(String puzzleID) {
        switch (puzzleID) {
            case "PZ-01":
                return new Puzzle1Awareness();
            case "PZ-02":
                return new Puzzle2Restraint();
            case "PZ-03":
                return new Puzzle3Trust();
            case "PZ-04":
                return new Puzzle4Sacrifice();
            case "PZ-05":
                return new Puzzle5Commitment();
            case "PZ-06":
                return new Puzzle6FinalTrial();
            case "PZ-07":
                return new Puzzle7AwarenessTrap();
            default:
                return null;
        }
    }

    private Monster extractFailureMonster(Puzzle puzzle) {
        if (puzzle instanceof Puzzle2Restraint) {
            Puzzle2Restraint p = (Puzzle2Restraint) puzzle;
            if (p.isCombatTriggered()) {
                return p.getFailureMonster();
            }
        }

        if (puzzle instanceof Puzzle3Trust) {
            Puzzle3Trust p = (Puzzle3Trust) puzzle;
            if (p.isCombatTriggered()) {
                return p.getFailureMonster();
            }
        }

        if (puzzle instanceof Puzzle4Sacrifice) {
            Puzzle4Sacrifice p = (Puzzle4Sacrifice) puzzle;
            if (p.isCombatTriggered()) {
                return p.getFailureMonster();
            }
        }

        if (puzzle instanceof Puzzle6FinalTrial) {
            Puzzle6FinalTrial p = (Puzzle6FinalTrial) puzzle;
            if (p.isCombatTriggered()) {
                Monster monster = p.getFailureMonster();
                p.clearCombatTrigger();
                return monster;
            }
        }

        return null;
    }
}