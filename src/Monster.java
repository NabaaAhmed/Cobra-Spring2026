public class Monster {

    private String name;
    private int hp;

    public Monster(String name, int hp) {
        this.name = name;
        this.hp = hp;
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

    public void takeDamage(int dmg) {
        hp -= dmg;
        if (hp < 0) hp = 0;
    }

    public void attack(Player player) {
        player.takeDamage(1);
    }

    public void clash(Player player) {
        player.takeDamage(1);
        this.takeDamage(1);
    }

    public void onEncounter(Player player) {
        System.out.println(name + " appears!");
    }

    public void onTurn(Player player) {
        // future behavior
    }
}