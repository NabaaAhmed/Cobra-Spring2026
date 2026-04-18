public class Main {
    public static void main(String[] args) {
        // Create game instance
        GameControllerMaiTesting game = new GameControllerMaiTesting();

        // Load all game data from external files
        // game.loadRooms("rooms.txt");
        game.loadItems("item.txt");
        // game.loadPuzzles("puzzle.txt");

        // Start the game
        game.run();
    }
}
