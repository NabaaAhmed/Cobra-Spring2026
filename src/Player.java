//Danny Class
import java.util.ArrayList;

public class Player {
    private String currentRoomID;
    private int maxHP;
    private int currentHP;
    private ArrayList<Item> inventory;
    private int attackPower;
    private int trialTokens;

    public Player(String startingRoomID) {
        this.currentRoomID = startingRoomID;
        this.maxHP = 5;
        this.currentHP = 5;
        this.inventory = new ArrayList<>();
        this.attackPower = 1;
        this.trialTokens = 0;
    }

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

    public int getTrialTokens() {
        return trialTokens;
    }

    public void setCurrentRoomID(String currentRoomID) {
        this.currentRoomID = currentRoomID;
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }

    public void setMaxHP(int MaxHP){
        this.maxHP = maxHP;
    }

    public void setCurrentHP(int currentHP) {
        this.currentHP = currentHP;
    }

    public void moveToRoom(String roomID) {
        this.currentRoomID = roomID;
    }

    public void addItem(Item item) {
        if (item == null) return;
        inventory.add(item);
        item.moveToInventory();
    }

    public void removeItem(Item item) {
        if (item == null) return;
        inventory.remove(item);
    }

    public Item findItemByName(String itemName) {
        if (itemName == null) return null;

        for (Item item : inventory) {
            if (item.getitemName() != null &&
                    item.getitemName().equalsIgnoreCase(itemName)) {
                return item;
            }
        }
        return null;
    }

    public boolean useItem(String itemName) {
        Item item = findItemByName(itemName);
        if (item == null) return false;

        item.use(this);
        return true;
    }

    public boolean hasSword() {
        for (Item item : inventory) {
            if (item.getitemName() != null &&
                    item.getitemName().toLowerCase().contains("sword")) {
                return true;
            }
        }
        return false;
    }

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

    public void healToFull() {
        currentHP = maxHP;
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

    public void addTrialToken() {
        trialTokens++;
    }

    public void removeTrialToken() {
        if (trialTokens > 0) {
            trialTokens--;
        }
    }

    public String waitTurn() {
        return "You wait for a turn.";
    }

    public void attack(Monster monster) {
        if (monster == null) return;
        monster.takeDamage(attackPower);
    }
}