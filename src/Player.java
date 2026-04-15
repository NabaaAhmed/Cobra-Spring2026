import java.util.ArrayList;

public class Player {
    private String currentRoomID;
    private int maxHP;
    private int currentHP;
    private ArrayList<Item> inventory;
    private int attackPower;

    // Constructor
    public Player(String startingRoomID) {
        this.currentRoomID = startingRoomID;
        this.maxHP = 5;
        this.currentHP = 5;
        this.inventory = new ArrayList<>();
        this.attackPower = 1;
    }

    // Getters
    public String getCurrentRoomID() {
        return currentRoomID;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public int getHp() {
        return currentHP;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public ArrayList<Item> getInventory() {
        return inventory;
    }

    public int getAttackPower() {
        return attackPower;
    }

    // Setters
    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }

    public void setCurrentRoomID(String currentRoomID) {
        this.currentRoomID = currentRoomID;
    }

    // Movement
    public void moveToRoom(String roomID) {
        this.currentRoomID = roomID;
    }

    // Inventory methods
    public void addItem(Item item) {
        if (item == null) {
            return;
        }
        inventory.add(item);
        item.moveToInventory();
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

    // Health methods
    public void takeDamage(int damage) {
        currentHP -= damage;
        if (currentHP < 0) {
            currentHP = 0;
        }
    }

    public int heal(int amount) {
        int before = currentHP;
        currentHP += amount;
        if (currentHP > maxHP) {
            currentHP = maxHP;
        }
        return currentHP - before;
    }

    public void modifyMaxHP(int amount) {
        maxHP += amount;

        if (maxHP < 1) {
            maxHP = 1;
        }

        if (currentHP > maxHP) {
            currentHP = maxHP;
        }
    }

    public boolean isAlive() {
        return currentHP > 0;
    }

    // Combat methods
    public void attack(Monster monster) {
        if (monster == null) {
            return;
        }
        monster.takeDamage(attackPower);
    }
}