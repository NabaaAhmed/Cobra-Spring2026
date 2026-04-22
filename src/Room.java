import java.util.ArrayList;
import java.util.List;

public class Room {
    private String id;
    private String name;
    private String description;
    private List<Room> connections;
    private List<Item> items;
    private Monster monster;
    private Puzzle puzzle;

    public Room(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.connections = new ArrayList<>();
        this.items = new ArrayList<>();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public List<Room> getConnections() { return connections; }
    public List<Item> getItems() { return items; }
    public Monster getMonster() { return monster; }
    public Puzzle getPuzzle() { return puzzle; }

    public void setMonster(Monster monster) { this.monster = monster; }
    public void setPuzzle(Puzzle puzzle) { this.puzzle = puzzle; }

    public void addItem(Item item) {
        if (item != null && !items.contains(item)) items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public Item findItem(String name) {
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    public void addConnection(Room room) {
        if (room != null && !connections.contains(room)) {
            connections.add(room);
        }
    }

    public List<String> getConnectionNames() {
        List<String> names = new ArrayList<>();
        for (Room room : connections) {
            names.add(room.getName());
        }
        return names;
    }
}