import java.util.ArrayList;

public class Player {
    private int currentRoomNumber;
    private int maxHP;
    private int currentHP;
    private ArrayList<Item> inventory;

    // Constructor
    public Player(int startingRoom) {
        this.currentRoomNumber = startingRoom;
        this.maxHP = 10;
        this.currentHP = 10;
        this.inventory = new ArrayList<>();
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
        inventory.add(item);
        item.moveToInventory(); // matches teammate's Item class
    }

    public void removeItem(Item item) {
        inventory.remove(item);
    }

    // Use item (basic version for now)
    public void useItem(Item item) {
        System.out.println("Used: " + item.getItemName());
    }

    // Health
    public void takeDamage(int damage) {
        currentHP -= damage;
        if (currentHP < 0) currentHP = 0;
    }

    public void heal(int amount) {
        currentHP += amount;
        if (currentHP > maxHP) currentHP = maxHP;
    }
}