public class Potion extends Item {
    private int healAmount;

    public Potion(String id, String name, String description, boolean stackable, int healAmount) {
        super(id, name, description, stackable, "potion");
        this.healAmount = healAmount;
    }

    @Override
    public void use(Player player) {
        player.heal(healAmount);
    }

    public int getHealAmount() {
        return healAmount;
    }
}