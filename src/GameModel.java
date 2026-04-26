import java.util.ArrayList;
import java.util.HashMap;

public class GameModel {
    private Player player;
    private RoomManager roomManager;
    private Puzzle activePuzzle;

    private HashMap<String, Monster> monsterTemplates;
    private HashMap<String, String> puzzleRoomMap;
    private HashMap<String, String> puzzleHintMap;
    private static HashMap<String, Item> pendingRewards = new HashMap<>();

    public GameModel(Player player, RoomManager roomManager) {
        this.player = player;
        this.roomManager = roomManager;
        this.activePuzzle = null;

        this.monsterTemplates = new HashMap<>();
        this.puzzleRoomMap = new HashMap<>();
        this.puzzleHintMap = new HashMap<>();

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

    /*
     * Automatically shown when entering a room.
     * Normal rooms: only room name + room ID.
     * Main Hall: room name + room ID + visible trial connections.
     */
    public GameResult roomHeader() {
        Room room = roomManager.getCurrentRoom();

        if (room == null) {
            return new GameResult("No room loaded.");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(room.getRoomName())
                .append(" (").append(room.getRoomId()).append(") ===");

        if (room.getRoomId().equals("EZ-01")) {
            sb.append("\n\nConnections:");

            boolean anyVisibleConnection = false;

            if (!room.getConnections().isEmpty()) {
                for (int i = 0; i < room.getConnections().size(); i++) {
                    Room connectedRoom = room.getConnections().get(i);
                    String connectedId = connectedRoom.getRoomId();

                    if (shouldHideConnection(room.getRoomId(), connectedId)) {
                        continue;
                    }

                    anyVisibleConnection = true;

                    sb.append("\n").append(i).append(": ")
                            .append(connectedRoom.getRoomName())
                            .append(" (")
                            .append(connectedRoom.getRoomId())
                            .append(")");
                }
            }

            if (!anyVisibleConnection) {
                sb.append("\n- No exits from this room.");
            }
        }

        return new GameResult(sb.toString());
    }

    /*
     * explore room = room description + exits/connections.
     * Does not show items.
     */
    public GameResult exploreRoom() {
        Room room = roomManager.getCurrentRoom();

        if (room == null) {
            return new GameResult("No room loaded.");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(room.getRoomName())
                .append(" (").append(room.getRoomId()).append(") ===\n");
        sb.append(room.getRoomDesc());

        sb.append("\n\nConnections:");

        boolean anyVisibleConnection = false;

        if (!room.getConnections().isEmpty()) {
            for (int i = 0; i < room.getConnections().size(); i++) {
                Room connectedRoom = room.getConnections().get(i);
                String connectedId = connectedRoom.getRoomId();

                if (shouldHideConnection(room.getRoomId(), connectedId)) {
                    continue;
                }

                anyVisibleConnection = true;

                sb.append("\n").append(i).append(": ")
                        .append(connectedRoom.getRoomName())
                        .append(" (")
                        .append(connectedRoom.getRoomId())
                        .append(")");
            }
        }

        if (!anyVisibleConnection) {
            sb.append("\n- No exits from this room.");
        }

        return new GameResult(sb.toString());
    }

    /*
     * inspect room = visible items only.
     * Does not show description or exits.
     */
    public GameResult lookRoom() {
        Room room = roomManager.getCurrentRoom();

        if (room == null) {
            return new GameResult("No room loaded.");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== Items in ")
                .append(room.getRoomName())
                .append(" (")
                .append(room.getRoomId())
                .append(") ===");

        if (!room.getItems().isEmpty()) {
            for (Item item : room.getItems()) {
                sb.append("\n- ").append(item.getItemName());
            }
        } else {
            sb.append("\nThere are no items in this room.");
        }

        return new GameResult(sb.toString());
    }

    public GameResult showMap() {
        ArrayList<Room> rooms = roomManager.getAllRooms();

        rooms.sort((room1, room2) -> room1.getRoomId().compareTo(room2.getRoomId()));

        StringBuilder sb = new StringBuilder();
        sb.append("=== Full Dungeon Map ===\n");
        sb.append("Total rooms loaded: ").append(roomManager.getRoomCount()).append("\n");

        int count = 1;
        for (Room room : rooms) {
            sb.append("\n").append(count).append(". ")
                    .append(room.getRoomId())
                    .append(" - ")
                    .append(room.getRoomName());

            if (!room.getConnections().isEmpty()) {
                sb.append("\n   Exits: ");
                for (int i = 0; i < room.getConnections().size(); i++) {
                    if (i > 0) {
                        sb.append(", ");
                    }
                    sb.append(room.getConnections().get(i).getRoomId());
                }
            } else {
                sb.append("\n   Exits: none");
            }

            count++;
        }

        sb.append("\n\nNormal movement: use move [number] from the current room.");
        sb.append("\nDemo/testing movement: use goto [roomID]. Example: goto CM-07");

        return new GameResult(sb.toString());
    }

    public GameResult goToRoomById(String command) {
        if (command == null || command.trim().length() <= 5) {
            GameResult result = new GameResult("Use: goto [roomID]. Example: goto AW-02");
            result.setSuccess(false);
            return result;
        }

        String roomId = command.substring(5).trim().toUpperCase();

        if (!roomManager.hasRoom(roomId)) {
            GameResult result = new GameResult("Room not found: " + roomId);
            result.setSuccess(false);
            return result;
        }

        roomManager.setRoom(roomId);
        player.setCurrentRoomId(roomId);
        activePuzzle = null;

        return new GameResult("You moved to " + roomManager.getCurrentRoom().getRoomName() + " (" + roomId + ")");
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
                GameResult result = new GameResult("Invalid room connection. Use one of the numbers shown under Connections.");
                result.setSuccess(false);
                return result;
            }

            Room destination = current.getConnections().get(index);
            String destinationId = destination.getRoomId();

            if ((destinationId.equals("FN-01") || destinationId.equals("FN-02") || destinationId.equals("EZ-02"))
                    && !allMainTrialsCompleted()) {
                GameResult result = new GameResult("That area is locked. Complete all 5 trials first.");
                result.setSuccess(false);
                return result;
            }

            if (destinationId.equals("TP-TRAP-01")) {
                GameResult result = new GameResult("That area is locked. You only go there through a trap or failed trial action.");
                result.setSuccess(false);
                return result;
            }

            String destinationTrial = getTrialKeyForRoom(destinationId);

            if (destinationTrial != null
                    && player.hasCompletedTrial(destinationTrial)
                    && !destinationId.equals("TP-TRAP-01")
                    && !destinationId.equals("END-01")) {
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

        if (item instanceof Potion) {
            int beforeHP = player.getCurrentHP();

            item.use(player);
            player.removeItem(item);

            int healedAmount = player.getCurrentHP() - beforeHP;

            if (healedAmount > 0) {
                return new GameResult("You drink the potion. Warmth rushes through your body.\n"
                        + "Your HP increased by " + healedAmount + " point(s).\n"
                        + "Current HP: " + player.getCurrentHP() + "/" + player.getMaxHP());
            } else {
                return new GameResult("You drink the potion, but your HP was already full.\n"
                        + "The potion is used up.\n"
                        + "Current HP: " + player.getCurrentHP() + "/" + player.getMaxHP());
            }
        }

        item.use(player);

        if (lower.startsWith("equip ")) {
            return new GameResult("You equipped " + item.getItemName() + ".");
        }

        return new GameResult("You used " + item.getItemName() + ".");
    }

    public GameResult saveGame() {
        if (hasActivePuzzle()) {
            GameResult result = new GameResult("You cannot save in the middle of a puzzle. Finish or fail the puzzle first.");
            result.setSuccess(false);
            return result;
        }

        FileManager.savePlayer("save.txt", player);
        return new GameResult("Game progress has been saved!");
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

    public String getActivePuzzleHint() {
        if (activePuzzle == null) {
            return "There is no active puzzle.";
        }

        String puzzleId = getPuzzleIdForPuzzle(activePuzzle);

        if (puzzleId != null && puzzleHintMap.containsKey(puzzleId)) {
            return puzzleHintMap.get(puzzleId);
        }

        return activePuzzle.getHint();
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

        player.markTrialCompleted(key);
    }

    private boolean allMainTrialsCompleted() {
        return player.hasCompletedTrial("AWARENESS")
                && player.hasCompletedTrial("RESTRAINT")
                && player.hasCompletedTrial("TRUST")
                && player.hasCompletedTrial("SACRIFICE")
                && player.hasCompletedTrial("COMMITMENT");
    }

    private boolean shouldHideConnection(String currentRoomId, String destinationId) {
        if (currentRoomId == null || destinationId == null) {
            return false;
        }

        if (!currentRoomId.equals("EZ-01")) {
            return false;
        }

        if (destinationId.equals("EZ-02") && !allMainTrialsCompleted()) {
            return true;
        }

        if ((destinationId.equals("FN-01") || destinationId.equals("FN-02")) && !allMainTrialsCompleted()) {
            return true;
        }

        if (destinationId.equals("TP-TRAP-01")) {
            return true;
        }

        return false;
    }

    private String getTrialKeyForRoom(String roomId) {
        if (roomId == null) {
            return null;
        }

        if (roomId.startsWith("AW-")) {
            return "AWARENESS";
        }

        if (roomId.startsWith("RS-")) {
            return "RESTRAINT";
        }

        if (roomId.startsWith("TR-")) {
            return "TRUST";
        }

        if (roomId.startsWith("SC-")) {
            return "SACRIFICE";
        }

        if (roomId.startsWith("CM-")) {
            return "COMMITMENT";
        }

        if (roomId.equals("TP-TRAP-01")) {
            return "TRAP";
        }

        if (roomId.startsWith("FN-")) {
            return "FINAL";
        }

        return null;
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

    private String getPuzzleIdForPuzzle(Puzzle puzzle) {
        if (puzzle instanceof Puzzle1Awareness) {
            return "PZ-01";
        }

        if (puzzle instanceof Puzzle2Restraint) {
            return "PZ-02";
        }

        if (puzzle instanceof Puzzle3Trust) {
            return "PZ-03";
        }

        if (puzzle instanceof Puzzle4Sacrifice) {
            return "PZ-04";
        }

        if (puzzle instanceof Puzzle5Commitment) {
            return "PZ-05";
        }

        if (puzzle instanceof Puzzle6FinalTrial) {
            return "PZ-06";
        }

        if (puzzle instanceof Puzzle7AwarenessTrap) {
            return "PZ-07";
        }

        return null;
    }

    public static void registerMonsterReward(String monsterId, Item item) {
        pendingRewards.put(monsterId, item);
    }

    private void loadMonsters(String filename) {
        String fileData = FileManager.load(filename);
        String[] lines = fileData.split("\n");

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();

            if (line.isEmpty() || line.startsWith("//") || line.toLowerCase().startsWith("monsterid")) {
                continue;
            }

            String[] parts = line.split(",");
            if (parts.length < 4) {
                continue;
            }

            String monsterID = parts[0].trim();
            String name = parts[1].trim();
            int hp = Integer.parseInt(parts[2].trim());
            int atkValue = Integer.parseInt(parts[3].trim());

            Monster monster = new Monster(monsterID, name, hp, atkValue);

            if (pendingRewards.containsKey(monsterID)) {
                Item reward = pendingRewards.get(monsterID);
                monster.setRewardItemName(reward.getItemName());
            }

            monsterTemplates.put(monsterID, monster);
        }
    }

    private void loadPuzzles(String filename) {
        String fileData = FileManager.load(filename);
        String[] lines = fileData.split("\n");

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();

            if (line.isEmpty() || line.startsWith("//") || line.toLowerCase().startsWith("puzzleid")) {
                continue;
            }

            String[] parts = line.split(",", 5);

            if (parts.length < 3) {
                continue;
            }

            String puzzleID = parts[0].trim();
            String roomId = parts[2].trim();

            puzzleRoomMap.put(roomId, puzzleID);

            if (parts.length >= 5) {
                String hint = parts[4].trim();

                if (!hint.isEmpty()) {
                    puzzleHintMap.put(puzzleID, hint);
                }
            }
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

        Monster copy = new Monster(
                template.getMonsterID(),
                template.getName(),
                template.getHp(),
                template.getAttackValue()
        );

        Item reward = template.dropReward();
        if (reward != null) {
            copy.setRewardItemName(reward.getItemName());
        }

        return copy;
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

        if (puzzle instanceof Puzzle5Commitment) {
            Puzzle5Commitment p = (Puzzle5Commitment) puzzle;
            if (p.isCombatTriggered()) {
                Monster monster = p.getPursuerMonster();
                p.clearCombatTrigger();
                return monster;
            }
        }

        if (puzzle instanceof Puzzle7AwarenessTrap) {
            Puzzle7AwarenessTrap p = (Puzzle7AwarenessTrap) puzzle;
            if (p.isCombatTriggered()) {
                Monster monster = p.getFailureMonster();
                p.clearCombatTrigger();
                return monster;
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