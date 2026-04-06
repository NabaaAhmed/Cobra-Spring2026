public class Item {
    private String itemId;
    private String name;
    private String description;
    private boolean inPlayerInventory;        // True if the item is currently carried by the player.
    private int currentRoomNumber;      // Current room number when not in inventory. Use -1 while in inventory.

    // Item constructor
    public Item(String itemId, String name, String description, int startingRoomNumber) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.inPlayerInventory = false;
        this.currentRoomNumber = startingRoomNumber;
    }

    // Getter for itemId
    public String getItemId() {
        return itemId;
    }

    // Getter and setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and setter for description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Getter and setter for inInventory
    public boolean isInInventory() {
        return inPlayerInventory;
    }

    public void setInInventory(boolean inPlayerInventory) {
        this.inPlayerInventory = inPlayerInventory;
    }

    // Getter and setter for currentRoomNumber
    public int getCurrentRoomNumber() {
        return currentRoomNumber;
    }

    public void setCurrentRoomNumber(int currentRoomNumber) {
        this.currentRoomNumber = currentRoomNumber;
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
