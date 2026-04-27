//mai
public class Potion extends Item {
    private final int healAmount;

    public Potion(String itemId, String itemName, String description, String roomID, Boolean stackable, int healAmount) {
        super(itemId, itemName, description, roomID, stackable);
        this.healAmount = healAmount;
    }

    @Override
    public void use(Player player) {
        // Heal to full current Max HP.
        // Player.heal() already caps HP at maxHP.
        player.heal(player.getMaxHP());
    }
}