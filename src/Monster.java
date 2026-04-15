import java.util.ArrayList;
import java.util.List;

public class Monster {

    private String name;
    private int hp;
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
        if (hp < 0) hp = 0;
    }

    // core clash
    public void clash(Player player) {
        player.takeDamage(1);
        this.takeDamage(1);
    }

    public void onEncounter(Player player) {
        // optional
    }

    public void onTurn(Player player) {
        turnCounter++;
    }

    public int attack() {
        return 1;
    }

    public void trigger() {
        // optional
    }

    public List<Item> dropLoot() {
        // Base monsters drop no guaranteed loot by default.
        return new ArrayList<>();
    }
}
