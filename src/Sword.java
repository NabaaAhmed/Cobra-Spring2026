public class Sword extends Item {
    private int damageBonus;
    private boolean equipped;

    public Sword(String itemId, String itemName, String description, String roomID, Boolean stackable, int damageBonus) {
        super(itemId, itemName, description, roomID, stackable);
        this.damageBonus = damageBonus;
        this.equipped = false;
    }

    @Override
    public void use(Player player) {
        if (!equipped) {
            player.setAttackPower(player.getAttackPower() + damageBonus);
            equipped = true;
            System.out.println("You ready the sword and gain " + damageBonus + " attack power.");
        } else {
            System.out.println("The sword is already equipped.");
        }
    }

    public void unequip(Player player) {
        if (equipped) {
            player.setAttackPower(player.getAttackPower() - damageBonus);
            equipped = false;
            System.out.println("You unequip the sword.");
        } else {
            System.out.println("The sword is not equipped.");
        }
    }

    public int getDamageBonus() {
        return damageBonus;
    }

    public boolean isEquipped() {
        return equipped;
    }
}