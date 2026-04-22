public class Sword extends Item {
    private int damageBonus;
    private int durability;
    private int maxDurability;

    public Sword(String id, String name, String description, boolean stackable, int damageBonus, int durability) {
        super(id, name, description, stackable, "sword");
        this.damageBonus = damageBonus;
        this.durability = durability;
        this.maxDurability = durability;
    }

    @Override
    public void use(Player player) {
        player.equipSword(this);
    }

    public void unequip(Player player) {
        if (player.getEquippedSword() == this) {
            player.unequipSword();
        }
    }

    public int getDamageBonus() {
        return damageBonus;
    }

    public int getDurability() {
        return durability;
    }

    public void useDurability() {
        durability--;
        if (durability <= 0) {
            System.out.println("Your " + name + " has broken!");
        }
    }

    public boolean isBroken() {
        return durability <= 0;
    }
}