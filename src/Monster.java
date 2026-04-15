import java.util.ArrayList;
import java.util.List;

public class Monster {

    private String name;
    private int hp;
    private int turnCounter;

    // constructor
    public Monster(String name, int hp) {
        this.name = name;
        this.hp = hp;
        this.turnCounter = 0;
    }

    // getters
    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    // damage system
    public void takeDamage(int amount) {
        hp -= amount;
        if (hp < 0) hp = 0;
    }

    // 🔥 MAIN CLASH SYSTEM (CORE)
    public void clash(Player player) {
        player.takeDamage(1);
        this.takeDamage(1);
    }

    // runs when combat starts (can be empty/simple)
    public void onEncounter(Player player) {
        // optional: ambush later
        // for now keep simple
    }

    // runs each turn (optional behavior)
    public void onTurn(Player player) {
        turnCounter++;

        // keep SIMPLE for now (or remove logic)
        // example:
        // if(turnCounter % 2 == 0){
        //     player.takeDamage(1);
        // }
    }

    // attack value (fixed system)
    public int attack() {
        return 1;
    }

    // trigger special ability (placeholder for later)
    public void trigger() {
        // optional future logic
    }

    // loot system
    public List<Item> dropLoot() {
        return new ArrayList<>(); // empty for now
    }
}