//Danny Class
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Player {
    private String currentRoomID;
    private int maxHP;
    private int currentHP;
    private ArrayList<Item> inventory;
    private int attackPower;
    private int trialTokens;
    private HashSet<String> completedTrials;

    public Player(String startingRoomID) {
        this.currentRoomID = startingRoomID;
        this.maxHP = 5;
        this.currentHP = 5;
        this.inventory = new ArrayList<>();
        this.attackPower = 1;
        this.trialTokens = 0;
        this.completedTrials = new HashSet<>();
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

    public Set<String> getCompletedTrials() {
        return completedTrials;
    }

    public void setCurrentRoomID(String currentRoomID) {
        this.currentRoomID = currentRoomID;
        setCurrentRoomID(currentRoomID);
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }

    public void setMaxHP(int maxHP){
        this.maxHP = maxHP;
    }

    public void setCurrentHP(int currentHP) {
        this.currentHP = currentHP;
    }

    public void moveToRoom(String newRoomID) {
        this.currentRoomID = newRoomID;
    }

    //Inventory methods -Mai
    public void takeItem(Item item, Room currentRoomObj) {
        item.moveToInventory();
        inventory.add(item);
    }

    public void removeItem(Item item) {
        if (item == null) return;
        inventory.remove(item);
    }

    public boolean dropItem(Item item, Room room) {
        if (item == null || room == null || !inventory.contains(item)) {
            return false;
        }
        inventory.remove(item);
        item.moveToRoom(room.getRoomID());
        return true;
    }
    public Item findItemByName(String itemName) {
        if (itemName == null) return null;

        for (Item item : inventory) {
            if (item.getItemName() != null &&
                    item.getItemName().equalsIgnoreCase(itemName)) {
                return item;
            }
        }
        return null;
    }

    //Using methods -Mai
    public boolean useItem(String itemName) {
        Item item = findItemByName(itemName);
        if (item == null) return false;

        item.use(this);
        return true;
    }

    public int heal(int amount) {
        int before = currentHP;
        currentHP += amount;

        if (currentHP > maxHP) {
            currentHP = maxHP;
        }

        return currentHP - before;
    }

    public boolean hasSword() {
        for (Item item : inventory) {
            if (item.getItemName() != null &&
                    item.getItemName().toLowerCase().contains("sword")) {
                return true;
            }
        }
        return false;
    }

    public boolean unequipWeapon(String itemName) {
        Item item = findItemByName(itemName);
        if (item instanceof Sword) {
            ((Sword) item).unequip(this);
            return true;
        }
        return false;
    }

    public void takeDamage(int damage) {
        currentHP -= damage;
        if (currentHP < 0) {
            currentHP = 0;
        }
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

    public boolean hasCompletedTrial(String trialKey) {
        return completedTrials.contains(trialKey);
    }

    public void markTrialCompleted(String trialKey) {
        completedTrials.add(trialKey);
    }
}