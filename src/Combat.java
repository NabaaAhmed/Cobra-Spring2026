public class Combat {

    private final Player player;
    private Monster enemy;
    private int turnCount;
    private boolean retreated;

    // Constructor
    public Combat(Player player, Monster enemy) {
        this.player = player;
        this.enemy = enemy;
        this.turnCount = 1;
        this.retreated = false;
    }

    // Check if battle is over
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

    // Main action system
    public String action(String command) {

        String result = "";

        if (command.equalsIgnoreCase("attack")) {

            // Sword logic
            if (player.hasSword()) {
                enemy.takeDamage(enemy.getHp());
                result += "You used the sword! Instant kill.\n";
            } else {
                // Clash system
                enemy.clash(player);
                result += "You and " + enemy.getName() + " both take 1 damage.\n";
            }
        }

        else if (command.equalsIgnoreCase("retreat")) {
            result += "You retreat from the battle.\n";
            retreated = true;
        }

        else if (command.equalsIgnoreCase("wait")) {
            result += player.waitTurn() + "\n";
        }

        else {
            result += "Invalid command. Try again.\n";
            turnCount--; // don't count bad input
        }

        // Apply turn effects
        applyTurnEffects();

        // Status output
        result += "Player HP: " + player.getHp() + "\n";
        result += enemy.getName() + " HP: " + enemy.getHp() + "\n";

        turnCount++;
        return result;
    }

    public void applyTurnEffects() {
        enemy.onTurn(player);
    }

    // Start battle loop
    public void startBattle(GameView view) {

        enemy.onEncounter(player);

        view.displayCombat("A " + enemy.getName() + " appears!");

        while (!isBattleOver()) {
            view.displayCombat("Turn " + turnCount);

            // For now auto-attack; replace with input later
            String result = action("attack");

            view.displayCombat(result);
        }

        if (retreated) {
            view.displayCombat("You fled the battle.");
        } else if (!player.isAlive()) {
            view.displayCombat("You died.");
        } else {
            view.displayCombat("Monster defeated!");

            // Loot
            for (Item item : enemy.dropLoot()) {
                player.addItem(item);
                view.displayCombat("You got: " + item.getitemName());
            }
        }
    }
}