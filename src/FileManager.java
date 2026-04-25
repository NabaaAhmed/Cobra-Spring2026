import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class FileManager {

    public static void savePlayer(String filename, Player player) {
        try {
            FileWriter writer = new FileWriter(filename);

            writer.write(player.getCurrentRoomId() + "\n");
            writer.write(player.getCurrentHP() + "\n");
            writer.write(player.getMaxHP() + "\n");
            writer.write(player.getAttackPower() + "\n");
            writer.write(player.getTrialTokens() + "\n");

            StringBuilder inventoryLine = new StringBuilder();
            for (Item item : player.getInventory()) {
                inventoryLine.append(item.getItemName()).append(";");
            }
            writer.write(inventoryLine.toString() + "\n");

            StringBuilder completedTrialsLine = new StringBuilder();
            for (String trial : player.getCompletedTrials()) {
                completedTrialsLine.append(trial).append(";");
            }
            writer.write(completedTrialsLine.toString() + "\n");

            writer.close();
        } catch (Exception e) {
            System.out.println("Error saving game progress file.");
        }
    }

    public static Player loadPlayer(String filename) {
        try {
            File file = new File(filename);
            Scanner reader = new Scanner(file);

            String roomID = reader.nextLine();
            int currentHP = Integer.parseInt(reader.nextLine());
            int maxHP = Integer.parseInt(reader.nextLine());
            int attackPower = Integer.parseInt(reader.nextLine());
            int trialTokens = Integer.parseInt(reader.nextLine());
            String inventoryLine = reader.nextLine();
            String completedTrialsLine = reader.hasNextLine() ? reader.nextLine() : "";

            Player player = new Player(roomID);

            if (maxHP != 5) {
                player.modifyMaxHP(maxHP - 5);
            }

            if (currentHP < player.getCurrentHP()) {
                player.takeDamage(player.getCurrentHP() - currentHP);
            } else if (currentHP > player.getCurrentHP()) {
                player.heal(currentHP - player.getCurrentHP());
            }

            player.setAttackPower(attackPower);

            for (int i = 0; i < trialTokens; i++) {
                player.addTrialToken();
            }

            if (!inventoryLine.isEmpty()) {
                String[] inventoryNames = inventoryLine.split(";");
                for (String name : inventoryNames) {
                    if (!name.trim().isEmpty()) {
                        Item item;

                        if (name.equalsIgnoreCase("Potion") || name.equalsIgnoreCase("Monster potion")) {
                            item = new Potion(
                                    "LOAD-POTION",
                                    name,
                                    "A small vial of restorative red liquid.",
                                    "0",
                                    true,
                                    2
                            );
                        } else if (name.toLowerCase().contains("sword")) {
                            item = new Sword(
                                    "LOAD-SWORD",
                                    name,
                                    name,
                                    "0",
                                    false,
                                    2
                            );
                        } else {
                            item = new QuestItems(
                                    "LOAD-" + name.toUpperCase().replace(" ", "_"),
                                    name,
                                    name,
                                    "0",
                                    false
                            );
                        }

                        player.addItem(item);
                    }
                }
            }

            if (!completedTrialsLine.isEmpty()) {
                String[] trials = completedTrialsLine.split(";");
                for (String trial : trials) {
                    if (!trial.trim().isEmpty()) {
                        player.markTrialCompleted(trial.trim());
                    }
                }
            }

            reader.close();
            return player;
        } catch (Exception e) {
            System.out.println("Error loading game.");
            return null;
        }
    }

    public static String load(String filename) {
        StringBuilder data = new StringBuilder();

        try {
            File file = new File(filename);
            Scanner reader = new Scanner(file);

            while (reader.hasNextLine()) {
                data.append(reader.nextLine()).append("\n");
            }

            reader.close();
        } catch (Exception e) {
            System.out.println("Error loading file: " + filename);
        }

        return data.toString();
    }
}