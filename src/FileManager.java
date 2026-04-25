import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.io.*;

public class FileManager {

    public static void saveGame(String filename, String data) {
        try {
            FileWriter writer = new FileWriter(filename);
            writer.write(data);
            writer.close();
            System.out.println("Your game progress has been saved!");
        } catch (Exception e) {
            System.out.println("Error saving game progress file.");
        }
    }

    public static String loadGame(String filename) {
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

    public void saveGame(String filename, Player player, RoomManager roomManager,
                         boolean p1, boolean p2, boolean p3, boolean p4, boolean p5) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            GameSaveData data = new GameSaveData();
            data.currentRoomID = player.getCurrentRoomID();
            data.maxHP = player.getMaxHP();
            data.currentHP = player.getCurrentHP();
            data.attackPower = player.getAttackPower();
            data.trialTokens = player.getTrialTokens();
            data.puzzle1Active = p1;
            data.puzzle2Active = p2;
            data.puzzle3Active = p3;
            data.puzzle4Active = p4;
            data.puzzle5Active = p5;

            for (Item item : player.getInventory()) {
                if (item.getItemId() != null) {
                    data.inventoryItemIds.add(item.getItemId());
                }
            }
            oos.writeObject(data);
            System.out.println("Game saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving game: " + e.getMessage());
        }
    }

    public GameSaveData loadSavedGame(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (GameSaveData) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No save file found.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading game: " + e.getMessage());
        }
        return null;
    }
}