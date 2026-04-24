public class GameView {

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public void displayRoom(Room room) {
        if (room == null) {
            System.out.println("No room loaded.");
            return;
        }

        System.out.println("\n=== " + room.getRoomName() + " ===");
        System.out.println(room.getRoomDesc());

        if (!room.getItems().isEmpty()) {
            System.out.println("\nItems in room:");
            for (Item item : room.getItems()) {
                System.out.println("- " + item.getitemName());
            }
        }

        if (!room.getConnections().isEmpty()) {
            System.out.println("\nConnections:");
            for (int i = 0; i < room.getConnections().size(); i++) {
                System.out.println(i + ": " + room.getConnections().get(i).getRoomName());
            }
        }
    }

    public void displayInventory(Player player) {
        System.out.println("=== Inventory ===");
        if (player.getInventory().isEmpty()) {
            System.out.println("- empty");
        } else {
            for (Item item : player.getInventory()) {
                System.out.println("- " + item.getitemName());
            }
        }
    }

    public void displayStatus(Player player) {
        System.out.println("=== Player Status ===");
        System.out.println("Current Room: " + player.getCurrentRoomID());
        System.out.println("HP: " + player.getCurrentHP() + "/" + player.getMaxHP());
        System.out.println("Attack Power: " + player.getAttackPower());
        System.out.println("Trial Tokens: " + player.getTrialTokens());
    }

    public void displayCombat(String message) {
        System.out.println("[COMBAT] " + message);
    }

    public void displayError(String message) {
        System.out.println("[ERROR] " + message);
    }
}