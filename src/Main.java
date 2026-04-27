//team
public class Main {
    public static void main(String[] args) {
        Player player = new Player("EZ-01");
        RoomManager roomManager = new RoomManager();
        GameView view = new GameView();

        GameModel model = new GameModel(player, roomManager);
        GameController controller = new GameController(model, view);

        controller.startGame();
    }
}

