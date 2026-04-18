import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileManager {

    public static void saveGame(String filename, String data) {
        try {
            FileWriter writer = new FileWriter(filename);
            writer.write(data);
            writer.close();
            System.out.println("You're game progress has been saved!");
        } catch (Exception e) {
            System.out.println("Error saving game progress file.");
        }
    }

    public static String load (String filename){
        String data = "";

        try {
            File file = new File(filename);
            Scanner reader = new Scanner(file);

            while (reader.hasNextLine()) {
                data += reader.nextLine() + "\n";
            }

            reader.close();
        } catch (Exception e) {
            System.out.println("Error loading file.");
        }
        return data;
        }

    public Player loadGame() {
        String data = load("savegame.txt").trim();

        if (data.isEmpty()) {
            return new Player("EZ-01");
        }

        String[] parts = data.split(",");
        Player player = new Player(parts.length > 0 ? parts[0] : "EZ-01");

        try {
            if (parts.length > 1) {
                player.setCurrentHP(Integer.parseInt(parts[1]));
            }
            if (parts.length > 2) {
                player.setMaxHP(Integer.parseInt(parts[2]));
            }
            if (parts.length > 3) {
                player.setAttackPower(Integer.parseInt(parts[3]));
            }
        } catch (NumberFormatException ignored) {
            // Fall back to the default player state.
        }

        return player;
    }

    public static void saveGame(Player player) {
        if (player == null) {
            return;
        }

        String data = String.join(",",
                player.getCurrentRoomID(),
                String.valueOf(player.getCurrentHP()),
                String.valueOf(player.getMaxHP()),
                String.valueOf(player.getAttackPower()));
        saveGame("savegame.txt", data);
    }
}
