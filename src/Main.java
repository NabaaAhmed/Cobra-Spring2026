public class Main {

    public static void main(String[] args) {

        // Create core components
        Player player = new Player();
        RoomManager roomManager = new RoomManager();
        GameView view = new GameView();
        FileManager fileManager = new FileManager();

        // Create controller
        GameController controller =
                new GameController(player, roomManager, view, fileManager);

        // Start game
        controller.startGame();
    }
}
