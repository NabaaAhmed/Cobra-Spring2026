public class Monster {

    private String name;
    private int hp;
    private boolean alive;
    private String type;     // monster type (golem, wraith, mimic, stalker)

    private int turnCounter;

    // constructor
    public Monster(String name, int hp, String type) {
        this.name = name;        // set name
        this.hp = hp;            // set hp
        this.type = type;        // set type
        this.alive = true;       // starts alive
        this.turnCounter = 0;    // start counter at 0
    }

    // return name
    public String getName() {
        return name;
    }

    // return hp
    public int getHp() {
        return hp;
    }

    // check if alive
    public boolean isAlive() {
        return alive;
    }

    // reduce hp
    public void takeDamage(int dmg) {
        hp -= dmg;               // subtract damage

        if (hp <= 0) {           // if dead
            hp = 0;
            alive = false;
        }
    }

    // main combat rule clash system
    public void clash(Player player) {
        player.takeDamage(1);    // player loses 1 hp
        this.takeDamage(1);      // monster loses 1 hp
    }

    // runs BEFORE fight which is ambush logic
    public void onEncounter(Player player) {

        if (type.equals("mimic")) {
            // mimic attacks first
            player.takeDamage(1);
            System.out.println("Mimic ambush!");
        }
    }

    // runs EACH TURN this is special attack
    public void onTurn(Player player) {

        // wraith will cause extra damage every turn
        if (type.equals("wraith")) {
            player.takeDamage(1);
            System.out.println("Wraith extra damage");
        }

        //  stalker will damage ever 2 turns
        if (type.equals("stalker")) {
            turnCounter++;   // count turns

            if (turnCounter % 2 == 0) {
                player.takeDamage(1);
                System.out.println("Stalker damage over time");
            }
        }
    }
}