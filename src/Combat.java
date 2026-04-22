import java.util.Scanner;

public class Combat {

    private final Player player;
    private Monster enemy;
    private int turnCount;

    public Combat(Player player, Monster enemy) {
        this.player = player;
        this.enemy = enemy;
        this.turnCount = 1;
    }

    public boolean isBattleOver() {
        return !player.isAlive() || !enemy.isAlive();
    }

    public int getTurnCount() {
        return turnCount;
    }

    public boolean isMonsterAlive() {
        return enemy.isAlive();
    }

    public Monster getEnemy() {
        return enemy;
    }

    public String action(String command) {
        String result = "";

        if (command.equalsIgnoreCase("attack")) {

            if (player.hasSword()) {
                enemy.takeDamage(enemy.getHp());
                result += "You used the sword! Instant kill.\n";
            } else {
                enemy.clash(player);
                result += "You and " + enemy.getName() + " clash.\n";
            }

        } else {
            return "Invalid combat command. Only 'attack' is allowed.\n";
        }

        result += "Player HP: " + player.getCurrentHP() + "\n";
        result += enemy.getName() + " HP: " + enemy.getHp() + "\n";

        turnCount++;
        return result;
    }

    public void startBattle(GameView view, Scanner input) {
        enemy.onEncounter(player);
        view.displayCombat("A " + enemy.getName() + " appears!");

        while (!isBattleOver()) {
            view.displayCombat("Turn " + turnCount);
            view.displayCombat("Type: attack");

            String command = input.nextLine().trim();
            String result = action(command);
            view.displayCombat(result);
        }

        if (!player.isAlive()) {
            view.displayCombat("You died.");
        } else {
            view.displayCombat("Monster defeated!");
        }
    }
}