public class Main {

//    public static void main(String[] args) {
//        GameView.displayIntro();
//        GameController playGame = new GameController();
//    }
public static void main(String[] args) {
    Player player = new Player("EZ-01");
    RoomManager roomManager = new RoomManager();
    GameView view = new GameView();

    GameControllerNA controller =
            new GameControllerNA(player, roomManager, view);

    controller.startGame();
}

    public static void main(String[] args) {
        Player player = new Player();
        RoomManager roomManager = new RoomManager();
        GameView view = new GameView();
        FileManager fileManager = new FileManager();

        GameControllerNA game = new GameControllerNA(player, roomManager, view, fileManager);
        game.startGame();
    }
}