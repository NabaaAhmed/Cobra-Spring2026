//nabaa
public class Monster {
    private String monsterID;
    private String name;
    private int hp;
    private int attackValue;
    private String rewardItemName;

    public Monster(String monsterID, String name, int hp, int attackValue) {
        this.monsterID = monsterID;
        this.name = name;
        this.hp = hp;
        this.attackValue = attackValue;
    }

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

    public boolean isAlive() {
        return hp > 0;
    }

    public void takeDamage(int dmg) {
        hp -= dmg;
        if (hp < 0) hp = 0;

    }

    public void setRewardItemName(String rewardItemName) {
        this.rewardItemName = rewardItemName;
    }

    public void setRewardItemName(String rewardItemName) {
        this.rewardItemName = rewardItemName;
    }

    public void attack(Player player) {
        player.takeDamage(attackValue);
    }

    public void clash(Player player) {
        player.takeDamage(attackValue);
        this.takeDamage(1);
    }

    public void onEncounter(Player player) {
        // kept empty on purpose
    }

    public Item dropReward() {
        if (rewardItemName == null || rewardItemName.equalsIgnoreCase("null")) {
            return null;
        }

        if (rewardItemName.equalsIgnoreCase("Potion")) {
            return new Potion("DROP-POTION", "Potion",
                    "A small vial of restorative red liquid.", "0", true, 2);
        }

        if (rewardItemName.toLowerCase().contains("sword")) {
            return new Sword("DROP-SWORD", rewardItemName,
                    rewardItemName, "0", false, 2);
        }

        return new QuestItems(
                "DROP-" + rewardItemName.toUpperCase().replace(" ", "_"),
                rewardItemName,
                rewardItemName,
                "0",
                false
        );
    }
}