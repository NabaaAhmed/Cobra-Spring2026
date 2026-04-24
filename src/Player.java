import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Player {
    private String currentRoomId;
    private int maxHP;
    private int currentHP;
    private ArrayList<Item> inventory;
    private int attackPower;
    private int trialTokens;
    private HashSet<String> completedTrials;

    public Player(String startingRoomId) {
        this.currentRoomId = startingRoomId;
        this.maxHP = 5;
        this.currentHP = 5;
        this.inventory = new ArrayList<>();
        this.attackPower = 1;
        this.trialTokens = 0;
        this.completedTrials = new HashSet<>();
    }

    public String getCurrentRoomId() {
        return currentRoomId;
    }

    public String getCurrentRoomID() {
        return getCurrentRoomId();
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

    public void setCurrentRoomId(String currentRoomId) {
        this.currentRoomId = currentRoomId;
    }

    public void setCurrentRoomID(String currentRoomId) {
        setCurrentRoomId(currentRoomId);
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }

    public void moveToRoom(String roomId) {
        this.currentRoomId = roomId;
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
            if (item.getItemName() != null &&
                    item.getItemName().equalsIgnoreCase(itemName)) {
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
            if (item.getItemName() != null &&
                    item.getItemName().toLowerCase().contains("sword")) {
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

    public void modifyMaxHP(int amount) {
        maxHP += amount;

        if (maxHP < 1) {
            maxHP = 1;
        }

        if (maxHP > 10) {
            maxHP = 10;
        }

        if (currentHP > maxHP) {
            currentHP = maxHP;
        }
    }

    public boolean isAlive() {
        return currentHP > 0;
    }

    public void addTrialToken() {
        if (trialTokens < 5) {
            trialTokens++;
        }
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