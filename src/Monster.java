//nabaa
public class Monster {
 //this takes monster stats
    private String monsterID;
    private String name;
    private int hp;
    private int attackValue;
    private String rewardItemName;
//constructer which will create monster stat
    public Monster(String monsterID, String name, int hp, int attackValue) {
        this.monsterID = monsterID;
        this.name = name;
        this.hp = hp;
        this.attackValue = attackValue;
    }
//getter for monster stats
    public String getMonsterID() {
        return monsterID;
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public int getAttackValue() {
        return attackValue;
    }
//Checks if monster is alive - returns true if HP is above 0
    public boolean isAlive() {
        return hp > 0;
    }
//When monster takes damage, subtract from HP
    public void takeDamage(int dmg) {
        hp -= dmg;
        if (hp < 0) hp = 0;

    }
 // Sets what item the monster drops when killed
    public void setRewardItemName(String rewardItemName) {
        this.rewardItemName = rewardItemName; // if attackValue is 3, player loses 3 health
    }

    public void attack(Player player) {
        player.takeDamage(attackValue);
    }

    //clash which will be caused when player fights without swords so both player and monster get damaged
    public void clash(Player player) {
        player.takeDamage(attackValue);
        this.takeDamage(1);
    }

    public void onEncounter(Player player) {
        // kept empty on purpose
    }

    // when monster dies this method runs to give player a item
    public Item dropReward() {
        //check if reward exisits
        if (rewardItemName == null || rewardItemName.equalsIgnoreCase("null")) {
            return null;  //no reward returns nothing
        }
//this will check the name of the reward and create the item
        if (rewardItemName.equalsIgnoreCase("Potion")) {
            return new Potion("DROP-POTION", "Potion",
                    "A small vial of restorative red liquid.", "0", true, 2);
        }

        //if the sword is the sword, this line will run to create sword item
        if (rewardItemName.toLowerCase().contains("sword")) {
            return new Sword("DROP-SWORD", rewardItemName,
                    rewardItemName, "0", false, 2);
        }

// If the monster drops something that is NOT a potion and NOT a sword, make a regular item.
        return new QuestItems(
                "DROP-" + rewardItemName.toUpperCase().replace(" ", "_"), //start ID
                rewardItemName,
                rewardItemName,
                "0",
                false
        );
    }
}