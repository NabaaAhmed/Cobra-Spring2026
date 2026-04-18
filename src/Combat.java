public class Combat {

    private final Player player;
    private Monster enemy;
    private int turns;
    private boolean retreated;

    public Combat(Player player) {
        this.player = player;
        this.turns = 1;
        this.retreated = false;
    }

    public void resetEngine(Monster enemy) {
        this.enemy = enemy;
        this.turns = 1;
        this.retreated = false;

        enemy.onEncounter(player);
    }

    public boolean isBattleOver() {
        return !player.isAlive() || !enemy.isAlive() || retreated;
    }

    public int getTurns() {
        return turns;
    }

    public boolean getMonsterAlive() {
        return enemy.isAlive();
    }

    public String action(String command) {

        String result = "";
        boolean defending = (turns % 3 == 0);

        if (command.equalsIgnoreCase("attack")) {

            if (player.hasSword()) {
                enemy.takeDamage(enemy.getHp());
                player.useSword();

                result += "⚔️ Sword instant kill!\n";
            } else {
                enemy.clash(player);
                result += "Clash! Both take 1 damage.\n";
            }
        }

        else if (command.equalsIgnoreCase("retreat")) {
            result += "You retreated.\n";
            retreated = true;
        }

        else {
            result += "Invalid command.\n";
            turns--;
        }

        if (enemy.isAlive()) {
            enemy.onTurn(player);
        }

        result += "Player HP: " + player.getCurrentHP() + "\n";
        result += enemy.getName() + " HP: " + enemy.getHp() + "\n";

        turns++;
        return result;
    }
}