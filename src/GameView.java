public class GameView {

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public void displayRoom(Room room) {
        if (room == null) {
            System.out.println("No room is currently loaded.");
            return;
        }

        System.out.println("=== " + room.getRoomName() + " ===");
        System.out.println(room.getRoomDesc());
    }

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

    public void displayCombat(String message) {
        System.out.println("[COMBAT] " + message);
    }

    public void displayError(String message) {
        System.out.println("[ERROR] " + message);
    }

    public void displayStatus(Player player) {
        System.out.println("=== Player Status ===");
        System.out.println("Current Room: " + player.getCurrentRoomID());
        System.out.println("HP: " + player.getCurrentHP() + "/" + player.getMaxHP());
        System.out.println("Attack Power: " + player.getAttackPower());
     //   System.out.println("Trial Tokens: " + player.getTrialTokens());
    }

    public void displayHelp() {
        System.out.println("=== Commands ===");
        System.out.println("help");
        System.out.println("room");
        System.out.println("move [number]");
        System.out.println("inventory");
        System.out.println("status");
        System.out.println("wait");
        System.out.println("fight");
        System.out.println("exit");
    }

    public void displayShop() {
        System.out.println("=== Shop ===");
        System.out.println("Shop feature not implemented yet.");
    }
}