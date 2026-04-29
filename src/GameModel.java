//team
import java.util.HashMap;

public class GameModel {
    private Player player;
    private RoomManager roomManager;
    private Puzzle activePuzzle;

    private HashMap<String, Monster> monsterTemplates;
    private HashMap<String, String> puzzleRoomMap;
    private HashMap<String, String> puzzleHintMap;

    public GameModel(Player player, RoomManager roomManager) {
        this.player = player;
        this.roomManager = roomManager;
        this.activePuzzle = null;

        this.monsterTemplates = roomManager.getMonsterTemplates();
        this.puzzleRoomMap = roomManager.getPuzzleRoomMap();
        this.puzzleHintMap = roomManager.getPuzzleHintMap();
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

    public GameResult exploreRoom() {
        Room room = roomManager.getCurrentRoom();

        if (room == null) {
            return new GameResult("No room loaded.");
        }

        String roomId = room.getRoomId();
        String description = room.getRoomDesc();

        if (roomId.equals("EZ-01") && allMainTrialsCompleted()) {
            description = "The Main Hall has changed. The Wall Jewel can now be removed from the wall. The hidden bomb room is sealed until the jewel is taken, and a final teleporter waits ahead.";
        } else if (player.hasCompletedTrial("AWARENESS") && roomId.equals("AW-01")) {
            description = "The Awareness entry room is quiet. Broken teleporter parts remain scattered across the floor.";
        } else if (player.hasCompletedTrial("AWARENESS") && roomId.equals("AW-02")) {
            description = "The damaged teleporter is quiet now. Scattered debris remains where the Awareness trial was decided.";
        } else if (player.hasCompletedTrial("RESTRAINT") && roomId.equals("RS-01")) {
            description = "The Restraint entry chamber is still and dark. The chamber ahead has already tested your self-control.";
        } else if (player.hasCompletedTrial("RESTRAINT") && roomId.equals("RS-02")) {
            description = "The chest sits silent in the circular chamber. The test of temptation has already been decided.";
        } else if (player.hasCompletedTrial("RESTRAINT") && roomId.equals("RS-03")) {
            description = "The Restraint Exit Corridor is quiet. The path back to the Main Hall remains open.";
        } else if (player.hasCompletedTrial("TRUST") && roomId.equals("TR-01")) {
            description = "The entry chamber is quiet now. The floor inscription still reads: 'Trust the unguarded.'";
        } else if (player.hasCompletedTrial("TRUST") && roomId.equals("TR-02")) {
            description = "The guardian statue lies shattered across the chamber. "
                    + "The false chest has been destroyed, and the pedestal stands silent.";
        } else if (player.hasCompletedTrial("TRUST") && roomId.equals("TR-03")) {
            description = "The Trust Exit Hall is quiet. The trial behind you has already been decided.";
        } else if (player.hasCompletedTrial("SACRIFICE") && roomId.equals("SC-01")) {
            description = "The sword pedestal stands empty. The bridge ahead still feels heavy with the memory of the trial.";
        } else if (player.hasCompletedTrial("SACRIFICE") && roomId.equals("SC-02")) {
            description = "The long bridge stretches across the darkness. The air is calmer now that the sacrifice has been made.";
        } else if (player.hasCompletedTrial("SACRIFICE") && roomId.equals("SC-03")) {
            description = "The end of the bridge is quiet. No Wraith blocks the path now.";
        } else if (player.hasCompletedTrial("COMMITMENT") && roomId.equals("CM-01")) {
            description = "The Commitment Entry is quiet now. The path ahead no longer echoes with pursuit.";
        } else if (player.hasCompletedTrial("COMMITMENT") && roomId.equals("CM-02")) {
            description = "The Dagger Chamber is still. The pressure to keep moving has passed.";
        } else if (player.hasCompletedTrial("COMMITMENT") && roomId.equals("CM-03")) {
            description = "The Chalice Hall is quiet now. The footsteps behind you are gone.";
        } else if (player.hasCompletedTrial("COMMITMENT") && roomId.equals("CM-04")) {
            description = "The Lens Storage room is silent. The Commitment trial has already been decided.";
        } else if (player.hasCompletedTrial("COMMITMENT") && roomId.equals("CM-05")) {
            description = "The Idol Room is still. The Pursuer no longer follows this path.";
        } else if (player.hasCompletedTrial("COMMITMENT") && roomId.equals("CM-06")) {
            description = "The Map Corridor is quiet. The exit path remains open.";
        } else if (player.hasCompletedTrial("COMMITMENT") && roomId.equals("CM-07")) {
            description = "The Commitment Exit is safe and quiet. The path back to the Main Hall remains open.";
        } else if (player.hasCompletedTrial("TRAP") && roomId.equals("TP-TRAP-01")) {
            description = "The trap chamber is quiet for now. Broken stone and scorched marks remain around the unstable teleporter.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(room.getRoomName())
                .append(" (").append(room.getRoomId()).append(") ===\n");
        sb.append(description);

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

    public GameResult inspectItem(String command) {
        if (command == null || command.trim().isEmpty()) {
            GameResult result = new GameResult("Inspect what?");
            result.setSuccess(false);
            return result;
        }

        String trimmed = command.trim();
        String lower = trimmed.toLowerCase();
        String itemName;

        if (lower.equals("inspect item")) {
            GameResult result = new GameResult("Inspect what? Use: inspect [item name]");
            result.setSuccess(false);
            return result;
        }

        if (lower.startsWith("inspect item ")) {
            itemName = trimmed.substring(13).trim();
        } else if (lower.startsWith("inspect ")) {
            itemName = trimmed.substring(8).trim();
        } else {
            GameResult result = new GameResult("Use: inspect [item name]");
            result.setSuccess(false);
            return result;
        }

        if (itemName.isEmpty() || itemName.equalsIgnoreCase("room")) {
            GameResult result = new GameResult("Use 'inspect room' to check room items, or 'inspect [item name]' to inspect an item.");
            result.setSuccess(false);
            return result;
        }

        Item item = null;
        Room room = roomManager.getCurrentRoom();

        if (room != null) {
            item = room.findItemByName(itemName);
        }

        if (item == null) {
            item = player.findItemByName(itemName);
        }

        if (item == null && itemName.equalsIgnoreCase("sword")) {
            item = findFirstSwordInInventory();

            if (item == null && room != null) {
                for (Item roomItem : room.getItems()) {
                    if (roomItem instanceof Sword ||
                            roomItem.getItemName().toLowerCase().contains("sword")) {
                        item = roomItem;
                        break;
                    }
                }
            }
        }

        if (item == null) {
            GameResult result = new GameResult("You do not see that item here or in your inventory.");
            result.setSuccess(false);
            return result;
        }

        String description = item.getDescription();

        if (description == null || description.trim().isEmpty()) {
            description = "No description available.";
        }

        return new GameResult(item.getItemName() + ": " + description);
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

            if (destinationId.equals("EZ-02") && player.findItemByName("Wall Jewel") == null) {
                GameResult result = new GameResult("The Hidden Bomb Room is still sealed. Take the Wall Jewel from the Main Hall first.");
                result.setSuccess(false);
                return result;
            }

            if (destinationId.equals("TP-TRAP-01")) {
                GameResult result = new GameResult("That area is locked. You only go there through a trap or failed trial action.");
                result.setSuccess(false);
                return result;
            }

            String currentTrial = getTrialKeyForRoom(current.getRoomId());
            String destinationTrial = getTrialKeyForRoom(destinationId);

            if (destinationTrial != null
                    && player.hasCompletedTrial(destinationTrial)
                    && !destinationId.equals("TP-TRAP-01")
                    && !destinationId.equals("END-01")) {

                boolean movingInsideSameTrial = currentTrial != null && currentTrial.equals(destinationTrial);

                if (!movingInsideSameTrial) {
                    GameResult result = new GameResult("That trial has already been completed.");
                    result.setSuccess(false);
                    return result;
                }
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

        if (itemName.equalsIgnoreCase("Wall Jewel")) {
            if (!room.getRoomId().equals("EZ-01")) {
                GameResult result = new GameResult("The Wall Jewel is not here.");
                result.setSuccess(false);
                return result;
            }

            if (!allMainTrialsCompleted()) {
                GameResult result = new GameResult("The Wall Jewel is still locked in place. Complete all 5 trials first.");
                result.setSuccess(false);
                return result;
            }
        }

        Item item = room.takeItemByName(itemName);

        if (item == null) {
            GameResult result = new GameResult("That item is not in this room.");
            result.setSuccess(false);
            return result;
        }

        player.addItem(item);

        if (item.getItemName().equalsIgnoreCase("Wall Jewel")) {
            return new GameResult("You pry the Wall Jewel from the Main Hall wall.\n"
                    + "A hidden passage opens nearby, revealing the Hidden Bomb Room.");
        }

        if (item.getItemName().equalsIgnoreCase("Explosive Device")) {
            return new GameResult("You picked up the Explosive Device.\n"
                    + "It looks like it belongs in the Final Trial mechanism.");
        }

        return new GameResult("You picked up: " + item.getItemName());
    }

    public GameResult dropItem(String command) {
        if (command == null || command.length() <= 5) {
            GameResult result = new GameResult("Drop what?");
            result.setSuccess(false);
            return result;
        }

        String itemName = command.substring(5).trim();

        if (itemName.equalsIgnoreCase("Wall Jewel") ||
                itemName.equalsIgnoreCase("Explosive Device")) {
            GameResult result = new GameResult("You should not drop that item. It is needed for final progression.");
            result.setSuccess(false);
            return result;
        }

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
            Item item;

            if (itemName.equalsIgnoreCase("sword")) {
                item = findFirstSwordInInventory();
            } else {
                item = player.findItemByName(itemName);
            }

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

        Item item;

        if (lower.startsWith("equip ") && itemName.equalsIgnoreCase("sword")) {
            item = findFirstSwordInInventory();
        } else {
            item = player.findItemByName(itemName);
        }

        if (item == null) {
            GameResult result = new GameResult("You do not have that item.");
            result.setSuccess(false);
            return result;
        }

        if (lower.startsWith("equip ") && !(item instanceof Sword)) {
            GameResult result = new GameResult("That item cannot be equipped.");
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

    private Item findFirstSwordInInventory() {
        for (Item item : player.getInventory()) {
            if (item instanceof Sword ||
                    item.getItemName().toLowerCase().contains("sword")) {
                return item;
            }
        }

        return null;
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

    private Monster getMonsterForCurrentRoom() {
        if ("TP-TRAP-01".equals(player.getCurrentRoomId())) {
            return copyMonster("M-06");
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