public class Item {
    private String itemID;
    private String name;
    private String description;
    boolean stackable;

    //item constrctor
    public Item(String itemID, String name, String description, boolean stackable) {
        this.itemID = itemID;
        this.name = name;
        this.description = description;
        this.stackable = stackable;
    }

    //getters
    public String getItemID() {
        return itemID;
    }
    public String getItemName() {
        return name;
    }

    public String getItemDescription() {
        return description;
    }

    public boolean isStackable() {
        return stackable;
    }

    //setters
    public void setItemID(String itemID) {
        this.itemID = itemID;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setStackable(boolean stackable) {
        this.stackable = stackable;
    }

    // Method to move item to inventory
    public void moveToInventory() {
        this.inPlayerInventory = true;
        this.currentRoomNumber = -1;
    }

    // Method to move item to a room
    public void moveToRoom(int roomNumber) {
        this.inPlayerInventory = false;
        this.currentRoomNumber = roomNumber;
    }


     //toString method
     @Override
     public String toString() {
         return name + ": " + description;
     }
}
