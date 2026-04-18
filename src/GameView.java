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

    //Display game introduction
    public static void displayIntro() {
        System.out.println("     /\\      /\\      /\\      /\\      /\\      /\\      /\\");
        System.out.println("    /  \\    /  \\    /  \\    /  \\    /  \\    /  \\    /  \\");
        System.out.println("    \\  /    \\  /    \\  /    \\  /    \\  /    \\  /    \\  /");
        System.out.println("     \\/      \\/      \\/      \\/      \\/      \\/      \\/");

        System.out.println("==============WELCOME TO THE DUNGEON OF TRIALS==============");
        System.out.println("The Dungeon of Trials was constructed by a king to find a worthy successor.");
        System.out.println("The dungeon tests character across five themed trials.");
        System.out.println("A detached Teleport Trap Room serves as a penalty zone for incorrect puzzle actions.");
        System.out.println("Good Luck!");
    }
//    // Display inventory
//    public void displayInventory(Player player) {
//        System.out.println("=== Inventory ===");
//        if (player.getInventory().isEmpty()) {
//            System.out.println("Inventory is empty.");
//        } else {
//            player.getInventory().forEach(item ->
//                    System.out.println("- " + item.getName())
//            );
//        }
//    }

    // Display combat messages
    public void displayCombat(String message) {
        System.out.println("[COMBAT] " + message);
    }

    // Display errors
    public void displayError(String message) {
        System.out.println("[ERROR] " + message);
    }

    // Display player status
    public void displayStatus() {
        System.out.println("Status updated.");
    }

    // Optional shop display
    public void displayShop() {
        System.out.println("=== Shop ===");
        System.out.println("Shop feature not implemented yet.");
    }
}