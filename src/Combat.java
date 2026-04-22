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

    public void startBattle(GameView view, Scanner input, Room currentRoom) {
        view.displayCombat("A " + enemy.getName() + " appears!");

        // AMBUSH PHASE
        int beforeHP = player.getCurrentHP();
        enemy.onEncounter(player);

        if (player.getCurrentHP() < beforeHP) {
            view.displayCombat(enemy.getName() + " ambushed you! (-" +
                    (beforeHP - player.getCurrentHP()) + " HP)");
        }

        if (!player.isAlive()) {
            view.displayCombat("You died before you could act.");
            return;
        }

        // COMBAT LOOP
        while (!isBattleOver()) {
            view.displayCombat("\n=== Turn " + turnCount + " ===");
            view.displayCombat("Player HP: " + player.getCurrentHP() + "/" + player.getMaxHP());
            view.displayCombat(enemy.getName() + " HP: " + enemy.getHp());
            view.displayCombat("Commands: attack / consume <potion>");

            String command = input.nextLine().trim().toLowerCase();

            // Handle consume command
            if (command.startsWith("consume")) {
                String[] parts = command.split(" ", 2);
                if (parts.length > 1) {
                    handleConsume(view, parts[1]);
                } else {
                    view.displayCombat("Consume what? Example: consume potion");
                }
                continue;
            }

            // Handle player action
            switch (command) {
                case "attack":
                    handleAttack(view);
                    break;

                default:
                    view.displayCombat("Invalid command. Use: attack or consume <potion>");
                    continue;
            }

            // Check if monster died
            if (!enemy.isAlive()) {
                break;
            }

            // Monster attacks back
            view.displayCombat(enemy.getName() + " attacks you!");
            player.takeDamage(enemy.getAttackValue());
            view.displayCombat("You take " + enemy.getAttackValue() + " damage!");

            if (!player.isAlive()) {
                view.displayCombat("You died.");
                break;
            }

            turnCount++;
        }

        // BATTLE RESOLUTION
        if (!enemy.isAlive()) {
            view.displayCombat("Monster defeated!");

            for (Item item : enemy.dropLoot()) {
                player.addItem(item);
                view.displayCombat("You got: " + item.getName());
            }

            // Remove monster from room
            currentRoom.setMonster(null);
        }
    }

    private void handleAttack(GameView view) {
        // Check if player has sword equipped
        if (player.hasSword()) {
            view.displayCombat("You strike with your sword!");
            enemy.takeDamage(player.getAttackPower());
            player.useSword();
            view.displayCombat("You deal " + player.getAttackPower() + " damage!");

            if (!player.hasSword()) {
                view.displayCombat("Your sword breaks from the strain!");
            }
        } else {
            view.displayCombat("You and " + enemy.getName() + " clash!");
            enemy.clash(player);
        }
    }

    private void handleConsume(GameView view, String itemName) {
        Item item = player.findItem(itemName);

        if (item == null) {
            view.displayCombat("You don't have that item.");
            return;
        }

        if (item instanceof Potion) {
            int oldHP = player.getCurrentHP();
            item.use(player);
            player.removeItem(item);
            view.displayCombat("You drink the potion!");
            view.displayCombat("HP: " + oldHP + " → " + player.getCurrentHP() + "/" + player.getMaxHP());
        } else {
            view.displayCombat("You can only consume potions in combat.");
        }
    }
}