public abstract class Item {
    private String itemId;
    private String itemName;
    private String description;
    private String roomID;
    private Boolean stackable;
    private boolean inPlayerInventory;

    // Item constructor
    public Item(String itemId, String itemName, String description, String roomID, Boolean stackable) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.description = description;
        this.roomID = roomID;
        this.stackable = stackable;
        this.inPlayerInventory = false;
    }

    // Getter for itemId
    public String getItemId() {
        return itemId;
    }

    // Getter and setter for itemName
    public String getitemName() {
        return itemName;
    }

    public void setitemName(String name) {
        this.itemName = name;
    }

    // Getter and setter for description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Getter and setter for roomID
    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    // Getter and setter for stackable
    public Boolean getStackable() {
        return stackable;
    }

    public void setStackable(Boolean stackable) {
        this.stackable = stackable;
    }

    public boolean isInPlayerInventory() {
        return inPlayerInventory;
    }

    // Move item to inventory
    public void moveToInventory() {
        this.inPlayerInventory = true;
        this.roomID = null;
    }

    // Move item back to a room
    public void moveToRoom(String roomID) {
        this.inPlayerInventory = false;
        this.roomID = roomID;
    }

    // Abstract method to be implemented by subclasses
    public abstract void use(Player player);

    @Override
    public String toString() {
        return itemName + ": " + description;
    }
}