import java.util.ArrayList;
import java.util.List;

public class Monster {

    private String monsterID;
    private String name;
    private int hp;
    private int atkValue;
    private ArrayList<Item> inventory;
    private int turnCounter;

    public Monster(String name, int hp) {
        this.name = name;
        this.hp = hp;
        this.turnCounter = 0;
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
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
        // optional special effect later
    }

    public void onTurn(Player player) {
        turnCounter++;
    }

    public int getTurnCounter() {
        return turnCounter;
    }

    public int attack() {
        return 1;
    }

    public void trigger() {
        // optional special trigger later
    }

    public List<Item> dropLoot() {
        return new ArrayList<>();
    }
}