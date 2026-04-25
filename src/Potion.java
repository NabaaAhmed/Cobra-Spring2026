public class Potion extends Item {
    private final int healAmount;

    public Potion(String itemId, String itemName, String description, String roomID, boolean stackable, int healAmount) {
        super(itemId, itemName, description, roomID, stackable);
        this.healAmount = healAmount;
    }

    public Potion(String itemId, String itemName, String description, boolean stackable, int healAmount) {
        super(itemId, itemName, description, stackable);
        this.healAmount = healAmount;
    }

    public int getHealAmount() {
        return healAmount;
    }

    @Override
    public void use(Player player) {
        int healed = player.heal(healAmount);
        System.out.println("You drink the potion and recover " + healed + " HP.");
    }
}