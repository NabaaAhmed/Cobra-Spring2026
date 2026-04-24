//Mai
public abstract class Item {
    private String itemId;
    private String itemName;
    private String description;
    private String roomID;
    private String monsterID;
    private boolean stackable;
    private boolean inPlayerInventory;

    // Item constructor
    public Item(String itemId, String itemName, String description, String roomID, String monsterID, boolean stackable) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.description = description;
        this.roomID = roomID;
        this.monsterID = monsterID;
        this.stackable = stackable;
        this.inPlayerInventory = false;
    }

    // Getter for itemId
    public String getItemId() {
        return itemId;
    }

    // Getter and setter for name
    public String getItemName() {
        return itemName;
    }

    public boolean matchesName(String candidate) {
        if (candidate == null || itemName == null) {
            return false;
        }
        return itemName.equalsIgnoreCase(candidate);
    }

    // Getter for description
    public String getDescription() {
        return description;
    }


    public boolean isInRoom(String roomId) {
        if (roomId == null || roomID == null) {
            return false;
        }
        return !inPlayerInventory && roomID.equalsIgnoreCase(roomId);
    }

    // Method to move item to inventory
    public void moveToInventory() {
        this.inPlayerInventory = true;
        this.roomID = null;
    }

    // Method to move item to a room
    public void moveToRoom(String roomID) {
        this.inPlayerInventory = false;
        this.roomID = roomID;
    }

    public abstract void use(Player player); // Abstract method to be implemented by specific item types


    //toString method
    @Override
    public String toString() {
        return getItemName() + ": " + description;
    }
}