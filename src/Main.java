public class Main {

    public static void main(String[] args) {

        // Create core components
        Player player = new Player("EZ-01");
        RoomManager roomManager = new RoomManager();
        GameView view = new GameView();
        FileManager fileManager = new FileManager();

        // Create controller
        GameControllerNA controller =
                new GameControllerNA(player, roomManager, view, fileManager);

        // Start game
        controller.startGame();
    }
    public class Main {
        public static void main(String[] args) {

            System.out.println("Starting test...");

            RoomManager rm = new RoomManager();

            rm.showRoom();   // should print first room

        }
    }

}