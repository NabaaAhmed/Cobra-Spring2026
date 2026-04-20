import java.util.HashMap;
import java.util.Map;

public class RoomManager {

    private Map<String, Room> rooms = new HashMap<>();
    private Map<String, Item> items = new HashMap<>();
    private Map<String, Monster> monsters = new HashMap<>();
    private Map<String, Puzzle> puzzles = new HashMap<>();

    // -------- ROOM METHODS --------
    public void addRoom(Room room) {
        rooms.put(room.getRoomID(), room);
    }

    public Room getRoom(String id) {
        return rooms.get(id);
    }

    // -------- ITEM METHODS --------
    public void addItem(Item item) {
        items.put(item.getitemName(), item);
    }

    public Item getItem(String name) {
        return items.get(name);
    }

    // -------- MONSTER METHODS --------
    public void addMonster(Monster monster) {
        monsters.put(monster.getName(), monster);
    }

    public Monster getMonster(String name) {
        return monsters.get(name);
    }

    // -------- PUZZLE METHODS --------
    public void addPuzzle(Puzzle puzzle) {
        puzzles.put(puzzle.getPuzzleID(), puzzle);
    }

    public Puzzle getPuzzle(String name) {
        return puzzles.get(name);
    }
}
