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

    public static String loadGame (String filename){
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
}
