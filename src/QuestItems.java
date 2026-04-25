public class QuestItems extends Item {

    public QuestItems(String itemId, String itemName, String description, String roomID, Boolean stackable) {
        super(itemId, itemName, description, roomID, stackable);
    }

    @Override
    public void use(Player player) {
        System.out.println("This item cannot be used directly.");
    }
}