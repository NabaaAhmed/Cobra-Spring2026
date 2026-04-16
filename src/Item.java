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

    // Getter and setter for name
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

    // getters and setters for roomID
    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public Boolean getStackable() {
        return stackable;
    }

    public void setStackable(Boolean stackable) {
        this.stackable = stackable;
    }

    public boolean isInPlayerInventory() {
        return inPlayerInventory;
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
         return itemName + ": " + description;
     }

    public java.lang.String getitemName() {
    }
}
