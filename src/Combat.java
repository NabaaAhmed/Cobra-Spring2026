//nabaa
import java.util.Scanner;

public class Combat {
    private final Player player;
    private final Monster enemy;
    private int turnCount;

    public Combat(Player player, Monster enemy) {
        this.player = player;
        this.enemy = enemy;
        this.turnCount = 1;
    }

    public boolean isBattleOver() {
        return !player.isAlive() || !enemy.isAlive();
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
        } else if (command.equalsIgnoreCase("use potion")) {
            Item potion = player.findItemByName("Potion");
            if (potion == null) {
                return "You do not have a potion.\n";
            }

            potion.use(player);
            player.removeItem(potion);
            result += "You used a potion.\n";
        } else {
            return "Invalid combat command. Use 'attack' or 'use potion'.\n";
        }

        result += "Player HP: " + player.getCurrentHP() + "/" + player.getMaxHP() + "\n";
        result += enemy.getName() + " HP: " + enemy.getHp() + "\n";

        turnCount++;
        return result;
    }

    public void startBattle(GameView view, Scanner input) {
        enemy.onEncounter(player);
        view.displayCombat("A " + enemy.getName() + " appears!");

        while (!isBattleOver()) {
            view.displayCombat("Turn " + turnCount);
            view.displayCombat("Type: attack or use potion");

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