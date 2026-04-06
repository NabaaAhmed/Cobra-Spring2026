public class Combat {

    public static void fight(Player player, Monster monster) {

        monster.onEncounter(player); // pre-attack

        while (player.isAlive() && monster.isAlive()) {

            monster.clash(player);   // main damage

            monster.onTurn(player);  // special behavior

            System.out.println("Player HP: " + player.getHp());
            System.out.println(monster.getName() + " HP: " + monster.getHp());
        }

        if (!player.isAlive()) {
            System.out.println("Player died");
        } else {
            System.out.println("Monster defeated");
        }
    }
}