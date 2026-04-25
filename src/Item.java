public abstract class Item {
    private String itemId;
    private String itemName;
    private String description;
    private String roomID;
    private boolean stackable;
    private boolean inPlayerInventory;

    public Item(String itemId, String itemName, String description, String roomID, boolean stackable) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.description = description;
        this.roomID = roomID;
        this.stackable = stackable;
        this.inPlayerInventory = false;
    }

    public Item(String itemId, String itemName, String description, boolean stackable) {
        this(itemId, itemName, description, null, stackable);
    }

    public String getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getitemName() {
        return itemName;
    }

    public String getName() {
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
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public Boolean getStackable() {
        return stackable;
    }

    public boolean isStackable() {
        return stackable;
    }

    public boolean isInPlayerInventory() {
        return inPlayerInventory;
    }

    public void moveToInventory() {
        this.inPlayerInventory = true;
        this.roomID = null;
    }

    public void moveToRoom(String roomID) {
        this.inPlayerInventory = false;
        this.roomID = roomID;
    }

    public abstract void use(Player player);

    @Override
    public String toString() {
        return getItemName() + ": " + description;
    }
}