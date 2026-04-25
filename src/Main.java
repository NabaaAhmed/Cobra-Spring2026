public class Main {
    public static void main(String[] args) {
        Player player = new Player("EZ-01");
        RoomManager roomManager = new RoomManager();
        GameView view = new GameView();
        FileManager fileManager = new FileManager();

        GameControllerNA game = new GameControllerNA(player, roomManager, view, fileManager);
        game.startGame();
    }
}