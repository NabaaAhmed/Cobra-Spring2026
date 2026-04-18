public class Sword extends Item {
    private int damageBonus;

    public Sword(String itemId, String itemName, String description, String roomID, Boolean stackable, int damageBonus) {
        super(itemId, itemName, description, roomID, stackable);
        this.damageBonus = damageBonus;
    }

    @Override
     public void use(Player player) {
        player.setAttackPower(player.getAttackPower() + damageBonus);
    }

    public void unequip(Player player) {
        player.setAttackPower(player.getAttackPower() - damageBonus);
    }

    public int getDamageBonus() {
        return damageBonus;
    }
}
