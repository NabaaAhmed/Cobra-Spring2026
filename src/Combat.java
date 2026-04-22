public class Combat {

    private final Player player;
    private Monster enemy;
    private int turnCount;
    private boolean retreated;

    public Combat(Player player, Monster enemy) {
        this.player = player;
        this.enemy = enemy;
        this.turnCount = 1;
        this.retreated = false;
    }

    public boolean isBattleOver() {
        return !player.isAlive() || !enemy.isAlive() || retreated;
    }

    public int getTurnCount() {
        return turnCount;
    }

    public String getMonsterHealth() {
        return String.valueOf(enemy.getHp());
    }

    public boolean isMonsterAlive() {
        return enemy.isAlive();
    }

    public String action(String command) {
        String result = "";

        if (command.equalsIgnoreCase("attack")) {
            if (player.hasSword()) {
                enemy.takeDamage(enemy.getHp());
                result += "You used a sword! Instant kill.\n";
            } else {
                enemy.clash(player);
                result += "You and " + enemy.getName() + " both take 1 damage.\n";
            }
        } else if (command.equalsIgnoreCase("retreat")) {
            retreated = true;
            result += "You retreat from the battle.\n";
        } else if (command.equalsIgnoreCase("wait")) {
            result += player.waitTurn() + "\n";
        } else {
            result += "Invalid combat command. Try attack, wait, or retreat.\n";
            return result;
        }

        applyTurnEffects();

        result += "Player HP: " + player.getCurrentHP() + "\n";
        result += enemy.getName() + " HP: " + enemy.getHp() + "\n";

        turnCount++;
        return result;
    }

    public void applyTurnEffects() {
        enemy.onTurn(player);
    }

    public void startBattle(GameView view, java.util.Scanner input) {
        enemy.onEncounter(player);
        view.displayCombat("A " + enemy.getName() + " appears!");

        while (!isBattleOver()) {
            view.displayCombat("Turn " + turnCount);
            view.displayCombat("Type: attack / wait / retreat");

            String command = input.nextLine();
            String result = action(command);
            view.displayCombat(result);
        }

        if (retreated) {
            view.displayCombat("You fled the battle.");
        } else if (!player.isAlive()) {
            view.displayCombat("You died.");
        } else {
            view.displayCombat("Monster defeated!");

            for (Item item : enemy.dropLoot()) {
                player.addItem(item);
                view.displayCombat("You got: " + item.getitemName());
            }
        }
    }
}