import java.util.ArrayList;
import java.util.List;

public class Player {

    private String currentRoomID;
    private int maxHP;
    private int currentHP;
    private int attackPower;
    private final List<Item> inventory;

    // sword system
    private boolean hasSword;
    private int swordDurability;
    private Sword equippedSword;

    public Player() {
        this("EZ-01", 5);  // Changed from 10 to 5
    }

    public Player(String currentRoomID) {
        this(currentRoomID, 5);  // Changed from 10 to 5
    }

    public Player(int hp) {
        this("EZ-01", hp);
    }

    public Player(String currentRoomID, int hp) {
        this.currentRoomID = currentRoomID;
        this.maxHP = Math.max(1, hp);
        this.currentHP = this.maxHP;
        this.attackPower = 1;
        this.inventory = new ArrayList<>();
        this.hasSword = false;
        this.swordDurability = 0;
        this.equippedSword = null;
    }

    public String getCurrentRoomID() {
        return currentRoomID;
    }

    public void setCurrentRoomID(String currentRoomID) {
        this.currentRoomID = currentRoomID;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public void setCurrentHP(int currentHP) {
        this.currentHP = Math.max(0, Math.min(currentHP, maxHP));
    }

    public int getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = Math.max(1, maxHP);
        if (currentHP > this.maxHP) {
            currentHP = this.maxHP;
        }
    }

    public int getAttackPower() {
        return attackPower;
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = Math.max(0, attackPower);
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public void addItem(Item item) {
        if (item != null) {
            inventory.add(item);
        }
    }

    public void removeItem(Item item) {
        inventory.remove(item);
    }

    public int getHp() {
        return currentHP;
    }

    public boolean isAlive() {
        return currentHP > 0;
    }

    public void takeDamage(int dmg) {
        currentHP -= Math.max(0, dmg);
        if (currentHP < 0) currentHP = 0;
    }

    public void heal(int amount) {
        currentHP += Math.max(0, amount);
        if (currentHP > maxHP) currentHP = maxHP;
    }

    public void fullHeal() {
        currentHP = maxHP;
    }

    public void attack(Monster monster) {
        if (monster != null) {
            monster.takeDamage(attackPower);
        }
    }

    // ===== SWORD =====

    public void equipSword(Sword sword) {
        this.equippedSword = sword;
        this.hasSword = true;
        this.swordDurability = sword.getDurability();
        this.attackPower += sword.getDamageBonus();
    }

    public void equipSword(int durability) {
        hasSword = durability > 0;
        swordDurability = Math.max(0, durability);
        if (hasSword) {
            attackPower += 3;
        }
    }

    public boolean hasSword() {
        return hasSword && equippedSword != null;
    }

    public void useSword() {
        if (!hasSword()) return;

        swordDurability--;
        if (equippedSword != null) {
            equippedSword.useDurability();
        }

        if (swordDurability <= 0) {
            hasSword = false;
            swordDurability = 0;
            if (equippedSword != null) {
                attackPower -= equippedSword.getDamageBonus();
                equippedSword = null;
            }
        }
    }

    public int getSwordDurability() {
        return swordDurability;
    }

    public Sword getEquippedSword() {
        return equippedSword;
    }

    public void unequipSword() {
        if (equippedSword != null) {
            attackPower -= equippedSword.getDamageBonus();
            equippedSword = null;
            hasSword = false;
            swordDurability = 0;
        }
    }

    public Item findItem(String name) {
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }
}