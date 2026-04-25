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
                result += "You grip the sword and strike with overwhelming force.\n";
                result += "The sword cuts through " + enemy.getName() + " instantly.\n";
            } else {
                enemy.clash(player);
                result += "You and " + enemy.getName() + " clash.\n";
                result += "Both of you take damage in the struggle.\n";
            }
        } else if (command.equalsIgnoreCase("use potion") || command.equalsIgnoreCase("consume potion")) {
            Item potion = player.findItemByName("Potion");
            if (potion == null) {
                return "You do not have a potion.\n";
            }

            int beforeHP = player.getCurrentHP();

            potion.use(player);
            player.removeItem(potion);

            int healedAmount = player.getCurrentHP() - beforeHP;

            if (healedAmount > 0) {
                result += "You drink the potion during combat.\n";
                result += "Your wounds begin to close as you recover " + healedAmount + " HP.\n";
            } else {
                result += "You drink the potion, but your HP was already full.\n";
                result += "The potion is used up.\n";
            }
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
            view.displayCombat("Type: attack, use potion, or consume potion");

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