import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Player player = new Player();
        Monster monster = new Monster("Goblin", 2);

        // give sword item
        Sword sword = new Sword("I1", "Trial Sword", "Powerful sword", null, false, 1);
        sword.use(player); // equip sword

        Combat combat = new Combat(player, monster);
        Scanner input = new Scanner(System.in);

        System.out.println("=== COMBAT START ===");

        while (!combat.isBattleOver()) {

            System.out.println("\nTurn: " + combat.getTurnCount());
            System.out.println("Enter command (attack, defend, dodge, retreat):");

            String cmd = input.nextLine();

            String result = combat.action(cmd);
            System.out.println(result);
        }

        if (!player.isAlive()) {
            System.out.println("You died.");
        } else if (!combat.isMonsterAlive()) {
            System.out.println("Monster defeated!");
        } else {
            System.out.println("You fled.");
        }
    }
}