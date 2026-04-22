import java.util.ArrayList;
import java.util.List;

public class Monster {
    private String id;
    private String name;
    private int hp;
    private int attackValue;
    private boolean canAmbush;
    private List<Item> rewards;
    private boolean isAlive;

    public Monster(String name, int hp, int attackValue, boolean canAmbush) {
        this.id = id;
        this.name = name;
        this.hp = hp;
        this.attackValue = attackValue;
        this.canAmbush = canAmbush;
        this.rewards = new ArrayList<>();
        this.isAlive = true;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getAttackValue() { return attackValue; }
    public boolean isAlive() { return isAlive && hp > 0; }

    public void takeDamage(int amount) {
        hp -= amount;
        if (hp <= 0) {
            isAlive = false;
            hp = 0;
        }
    }

    /**
     * Clash combat - both player and monster take damage
     */
    public void clash(Player player) {
        // Monster damages player
        player.takeDamage(attackValue);
        // Player damages monster
        this.takeDamage(player.getAttackPower());
    }

    /**
     * Pre-emptive ambush attack before combat begins when player encounters monster
     */
    public void onEncounter(Player player) {
        if (canAmbush) {
            player.takeDamage(attackValue);
        }
    }

    public void addReward(Item item) {
        if (item != null) {
            rewards.add(item);
        }
    }

    public void addRewards(List<Item> items) {
        if (items != null) {
            rewards.addAll(items);
        }
    }

    public List<Item> dropLoot() {
        List<Item> dropped = new ArrayList<>(rewards);
        rewards.clear();
        return dropped;
    }

    public void setRewards(List<Item> rewards) {
        this.rewards = rewards != null ? rewards : new ArrayList<>();
    }

    public void reset() {
        isAlive = true;
        // Note: hp reset would need original HP stored
    }
}