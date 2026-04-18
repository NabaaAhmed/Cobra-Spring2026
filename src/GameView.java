public class GameView {

    // Display general messages
    public void displayMessage(String message) {
        System.out.println(message);
    }

    // Display current room
    public void displayRoom(Room room) {
        System.out.println("=== " + room.getRoomName() + " ===");
        System.out.println(room.getRoomDesc());
    }

    // Display inventory
    public void displayInventory(Player player) {
        System.out.println("=== Inventory ===");
        if (player.getInventory().isEmpty()) {
            System.out.println("Inventory is empty.");
        } else {
            player.getInventory().forEach(item ->
                    System.out.println("- " + item.getitemName())
            );
        }
    }
}