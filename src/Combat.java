public class Combat {

    private Player player;
    private Monster monster;
    private int turnCount;
    private boolean retreated;

    // constructor
    public Combat(Player player, Monster monster) {
        this.player = player;
        this.monster = monster;
        this.turnCount = 1;
        this.retreated = false;
    }

    public void startBattle() {

        System.out.println("Battle started with " + monster.getName());

        while (player.isAlive() && monster.isAlive() && !retreated) {

            System.out.println("Turn " + turnCount);

            // 🔥 CLASH SYSTEM (CORE)
            if (player.hasSword()) {
                monster.takeDamage(monster.getHp()); // insta kill
                System.out.println("You used the sword! Instant kill.");
            } else {
                player.takeDamage(1);
                monster.takeDamage(1);
                System.out.println("You and " + monster.getName() + " both take 1 damage.");
            }

            // print status
            System.out.println("Player HP: " + player.getHp());
            System.out.println(monster.getName() + " HP: " + monster.getHp());

            turnCount++;
        }

        if (retreated) {
            System.out.println("You fled the battle.");
        } else if (!player.isAlive()) {
            System.out.println("You died.");
        } else {
            System.out.println("Monster defeated!");
        }
    }

    // optional command (if you want input system later)
    public void retreat() {
        this.retreated = true;
    }
}