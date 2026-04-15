import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class FileManager {

    public static String load (String filename){
        String data = "";

        try {
            File file = new File(filename);
            Scanner reader = new Scanner(file);

            while (reader.hasNextLine()) {
                data += reader.nextLine() + "\n";
            }

            reader.close();
        } catch (IOException e) {
            System.out.println("Error loading file.");
        }
        return data;
        }
}
