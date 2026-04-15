import java.util.ArrayList;

public class Player {
    private int currentRoomNumber;
    private int maxHP;
    private int currentHP;
    private ArrayList<Item> inventory;
    private int attackPower;

    // Constructor
    public Player(int startingRoom) {
        this.currentRoomNumber = startingRoom;
        this.maxHP = 5;
        this.currentHP = 5;
        this.inventory = new ArrayList<>();
        this.attackPower = 1;
    }

    // Getters and setters for attack power
    public int getAttackPower() {
        return attackPower;
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }

    // Getters
    public int getCurrentRoomNumber() {
        return currentRoomNumber;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public ArrayList<Item> getInventory() {
        return inventory;
    }

    // Movement
    public void moveToRoom(int roomNumber) {
        this.currentRoomNumber = roomNumber;
    }

    // Inventory methods
    public void addItem(Item item) {
        if (item == null) {
            return;
        }
        inventory.add(item);
        item.moveToInventory(); // matches teammate's Item class
    }

    public void removeItem(Item item) {
        if (item == null) {
            return;
        }
        inventory.remove(item);
    }

    public Item findItemByName(String itemName) {
        if (itemName == null) {
            return null;
        }
        for (Item item : inventory) {
            if (item.getitemName() != null && item.getitemName().equalsIgnoreCase(itemName)) {
                return item;
            }
        }
        return null;
    }

    public boolean useItem(String itemName) {
        Item item = findItemByName(itemName);
        if (item == null) {
            return false;
        }
        item.use(this);
        return true;
    }

    // Health
    public void takeDamage(int damage) {
        currentHP -= damage;
        if (currentHP < 0) currentHP = 0;
    }

    public int heal(int amount) {
        int before = currentHP;
        currentHP += amount;
        if (currentHP > maxHP) currentHP = maxHP;
        return currentHP - before;
    }
}