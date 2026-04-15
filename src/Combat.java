public class Combat {

    private final Player player;
    private Monster enemy;
    private int turnCount;
    private boolean retreated;

    // constructor
    public Combat(Player player, Monster enemy) {
        this.player = player;
        this.enemy = enemy;
        this.turnCount = 1;
        this.retreated = false;
    }

    // check if battle is over
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

    //  MAIN ACTION SYSTEM
    public String action(String command) {

        String result = "";

        if (command.equalsIgnoreCase("attack")) {

            // sword logic (optional but good)
            if (player.hasSword()) {
                enemy.takeDamage(enemy.getHp());
                result += "You used the sword! Instant kill.\n";
            } else {
                // CLASH SYSTEM
                enemy.clash(player);
                result += "You and " + enemy.getName() + " both take 1 damage.\n";
            }
        }

        else if (command.equalsIgnoreCase("retreat")) {
            result += "You retreat from the battle.\n";
            retreated = true;
        }

        else {
            result += "Invalid command. Try again.\n";
            turnCount--;   // don't count bad input
        }

        // apply turn effects
        applyTurnEffects();

        // status output
        result += "Player HP: " + player.getHp() + "\n";
        result += enemy.getName() + " HP: " + enemy.getHp() + "\n";

        turnCount++;
        return result;
    }


    public void applyTurnEffects() {
        enemy.onTurn(player);
    }

    // start battle loop
    public void startBattle(GameView view) {

        enemy.onEncounter(player);

        view.displayCombat("A " + enemy.getName() + " appears!");

        while (!isBattleOver()) {

            view.displayCombat("Turn " + turnCount);

            // for now auto-attack replace with input later
            String result = action("attack");

            view.displayCombat(result);
        }

        if (retreated) {
            view.displayCombat("You fled the battle.");
        } else if (!player.isAlive()) {
            view.displayCombat("You died.");
        } else {
            view.displayCombat("Monster defeated!");

            // loot
            for (Item item : enemy.dropLoot()) {
                player.addItem(item);
                view.displayCombat("You got: " + item.getName());
            }
        }
    }
}