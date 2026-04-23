import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileManager {
    //loads game by reading data
    public static String loadGame (String filename){
        String data = "";

        try {
            File file = new File(filename);
            Scanner reader = new Scanner(file);

            //while there is still text left in the  file
            while (reader.hasNextLine()) {
                data += reader.nextLine() + "\n";
            }

            reader.close();
        } catch (Exception e) {
            System.out.println("[Error] Could not find the file named " + filename);
        }
        return data;
    }

    //saves game by writing data on file
    public static void saveGame(String filename, String data) {
        try {
            FileWriter writer = new FileWriter(filename);
            writer.write(data);
            writer.close();
            System.out.println("You're game progress has been saved!");
        } catch (Exception e) {
            System.out.println("[Error] Could not save to " + filename);
        }
    }


}
