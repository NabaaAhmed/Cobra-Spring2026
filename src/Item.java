//mai
public abstract class Item {
    private String itemId;
    private String itemName;
    private String description;
    private String roomId;
    private Boolean stackable;
    private boolean inPlayerInventory;

    public Item(String itemId, String itemName, String description, String roomId, Boolean stackable) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.description = description;
        this.roomId = roomId;
        this.stackable = stackable;
        this.inPlayerInventory = false;
    }

    public String getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String name) {
        this.itemName = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRoomID() {
        return roomId;
    }

    public void setRoomID(String roomID) {
        this.roomId = roomID;
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

    public void moveToInventory() {
        this.inPlayerInventory = true;
        this.roomId = null;
    }

    public void moveToRoom(String roomID) {
        this.inPlayerInventory = false;
        this.roomId = roomID;
    }

    public abstract void use(Player player);

    @Override
    public String toString() {
        return itemName + ": " + description;
    }
}