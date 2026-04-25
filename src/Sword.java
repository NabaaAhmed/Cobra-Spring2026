public class Sword extends Item {
    private int damageBonus;

    public Sword(String itemId, String itemName, String description, String roomID, boolean stackable, int damageBonus) {
        super(itemId, itemName, description, roomID, stackable);
        this.damageBonus = damageBonus;
    }

    public Sword(String itemId, String itemName, String description, boolean stackable, int damageBonus) {
        super(itemId, itemName, description, stackable);
        this.damageBonus = damageBonus;
    }

    @Override
    public void use(Player player) {
        player.setAttackPower(player.getAttackPower() + damageBonus);
        System.out.println("You ready the sword and gain " + damageBonus + " attack power.");
    }

    public void unequip(Player player) {
        player.setAttackPower(player.getAttackPower() - damageBonus);
    }

    public int getDamageBonus() {
        return damageBonus;
    }
}