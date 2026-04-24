import java.util.ArrayList;

public class Room {
    private String id;
    private String name;
    private String description;
    private ArrayList<Room> connections;
    private ArrayList<Item> items;

    public Room(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.connections = new ArrayList<>();
        this.items = new ArrayList<>();
    }

    public void connection(Room room) {
        if (room != null && !connections.contains(room)) {
            connections.add(room);
        }
    }

    public void addItem(Item item) {
        if (item != null) {
            items.add(item);
            item.moveToRoom(id);
        }
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public Item findItemByName(String itemName) {
        for (Item item : items) {
            if (item.getitemName() != null &&
                    item.getitemName().equalsIgnoreCase(itemName)) {
                return item;
            }
        }
        return null;
    }

    public Item takeItemByName(String itemName) {
        Item item = findItemByName(itemName);
        if (item != null) {
            items.remove(item);
        }
        return item;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public String getRoomId() {
        return id;
    }

    public String getRoomName() {
        return name;
    }

    public String getRoomDesc() {
        return description;
    }

    public ArrayList<Room> getConnections() {
        return connections;
    }
}