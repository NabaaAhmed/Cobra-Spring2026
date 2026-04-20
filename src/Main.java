public class Main {

    public static void main(String[] args) {
        Player player = new Player("EZ-01");
        RoomManager roomManager = new RoomManager();
        GameView view = new GameView();

        GameControllerNA controller =
                new GameControllerNA(player, roomManager, view);

        controller.startGame();
    }
}
