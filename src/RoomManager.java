import java.io.IOException;
import java.util.*;

public class RoomManager {

    private Map<String, Room> rooms = new HashMap<>();
    private Map<String, Item> items = new HashMap<>();
    private Map<String, Monster> monsters = new HashMap<>();
    private Map<String, Puzzle> puzzles = new HashMap<>();

    private Player player;
    private Room currentRoom;
    private Set<String> visitedRooms = new HashSet<>();

    private FileManager fileManager;

    public RoomManager(String dataPath) {

        this.fileManager = new FileManager(dataPath);

        this.player = new Player();

        try {
            fileManager.loadAllData(this);
            assignPuzzlesToRooms();
            setStartingRoom();
        }
        catch (IOException e) {
            System.out.println("Error loading game: " + e.getMessage());
        }
    }
    private void setStartingRoom() {

        currentRoom = rooms.get("START");

        if (currentRoom == null && !rooms.isEmpty()) {
            currentRoom = rooms.values().iterator().next();
        }

        if (currentRoom != null) {
            visitedRooms.add(currentRoom.getId());
        }
    }

    private void assignPuzzlesToRooms() {

        for (Puzzle p : puzzles.values()) {
            Room r = rooms.get(p.getRoomID());
            if (r != null) r.setPuzzle(p);
        }
    }
    // -------- ROOM --------

    public void showRoom() {

        if (currentRoom == null) {
            System.out.println("No room loaded.");
            return;
        }

        System.out.println("\n" + currentRoom.getRoomName());
        System.out.println(currentRoom.getRoomDesc());

        visitedRooms.add(currentRoom.getId());

        if (!currentRoom.getItems().isEmpty()) {
            System.out.println("\nItems:");
            for (Item i : currentRoom.getItems()) {
                System.out.println("- " + i.getitemName());
            }
        }

        if (currentRoom.hasMonster()) {
            System.out.println("\nMonster: " + currentRoom.getMonster().getName());
        }

        if (currentRoom.hasPuzzle()) {
            System.out.println("\nPuzzle: " + currentRoom.getPuzzle().getTrialName());
        }

        System.out.println("\nConnections:");

        List<String> exits = new ArrayList<>(currentRoom.getExits().keySet());

        for (int i = 0; i < exits.size(); i++) {

            String key = exits.get(i);
            String target = currentRoom.getExits().get(key);

            Room r = rooms.get(target);

            System.out.println(i + ": " + (r != null ? r.getRoomName() : target));
        }
    }

    public void move(int index) {

        List<String> exits = new ArrayList<>(currentRoom.getExits().keySet());

        if (index < 0 || index >= exits.size()) {
            System.out.println("Invalid move.");
            return;
        }

        String target = currentRoom.getExits().get(exits.get(index));

        Room next = rooms.get(target);

        if (next == null) {
            System.out.println("You cannot go there.");
            return;
        }

        currentRoom = next;
        showRoom();
    }
    public void addRoom(Room r) { rooms.put(r.getId(), r); }
    public void addItem(Item i) { items.put(i.getId(), i); }
    public void addMonster(Monster m) { monsters.put(m.getId(), m); }
    public void addPuzzle(Puzzle p) { puzzles.put(p.getId(), p); }

    public Room getCurrentRoom() { return currentRoom; }
    public Player getPlayer() { return player; }

    public Map<String, Room> getRooms() { return rooms; }
    public Map<String, Item> getItems() { return items; }
    public Map<String, Monster> getMonsters() { return monsters; }
    public Map<String, Puzzle> getPuzzles() { return puzzles; }

    public Set<String> getVisitedRooms() { return visitedRooms; }

    public Item getItem(String id) { return items.get(id); }
    public Monster getMonster(String id) { return monsters.get(id); }
    public Puzzle getPuzzle(String id) { return puzzles.get(id); }
}