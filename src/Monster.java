import java.util.ArrayList;
import java.util.List;

public class Monster {

    private String monsterID;
    private String name;
    private int hp;
    private int atkValue;
    private ArrayList<Item> inventory;
    private int turnCounter;
    private boolean isBoss;

    public Monster(String name, int hp) {
        this.name = name;
        this.hp = hp;
        this.turnCounter = 0;
        this.atkValue = 1;
        this.inventory = new ArrayList<>();
    }

    public Monster(String monsterID, String name, int hp, int atkValue, boolean isBoss) {
        this.monsterID = monsterID;
        this.name = name;
        this.hp = hp;
        this.atkValue = atkValue;
        this.isBoss = isBoss;
        this.turnCounter = 0;
        this.inventory = new ArrayList<>();
    }

    public Monster(String id, String name, int hp, int atkValue) {
        this(id, name, hp, atkValue, false);
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public int getAttackValue() {
        return atkValue;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public void takeDamage(int amount) {
        hp -= amount;
        if (hp < 0) {
            hp = 0;
        }
    }

    public void clash(Player player) {
        player.takeDamage(1);
        this.takeDamage(1);
    }

    public void onEncounter(Player player) {
    }

    public void onTurn(Player player) {
        turnCounter++;
    }

    public int getTurnCounter() {
        return turnCounter;
    }

    public int attack() {
        return atkValue;
    }

    public void trigger() {
    }

    public List<Item> dropLoot() {
        return new ArrayList<>();
    }
}