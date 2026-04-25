import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class GameControllerMaiTesting {
    private final Scanner scanner;
    private final Map<String, Item> items;
    private final Map<String, Room> rooms;
    private Player player;
    private GameView gameView;

    public GameControllerMaiTesting() {
        this.scanner = new Scanner(System.in);
        this.items = new HashMap<>();
        this.rooms = new HashMap<>();
        this.gameView = new GameView();
        this.player = new Player("R1", 7, 5, 1);
        initializeHardcodedData();
    }

    // Keeps Main.java compatible while using hardcoded data for now.
    public void loadItems(String filename) {
        initializeHardcodedData();
        if (filename != null && !filename.isBlank()) {
            System.out.println("Loaded hardcoded data instead of file: " + filename);
        }
    }

    private void initializeHardcodedData() {
        rooms.clear();
        items.clear();

        Room r1 = new Room("R1", "Entrance", "A small stone room with a wooden door.");
        Room r2 = new Room("R2", "Hallway", "A narrow hall lit by flickering torches.");
        Room r3 = new Room("R3", "Treasure Room", "A quiet room with an old chest in the center.");

        r1.addConnection("R2");
        r2.addConnection("R1");
        r2.addConnection("R3");
        r3.addConnection("R2");

        rooms.put(r1.getRoomId(), r1);
        rooms.put(r2.getRoomId(), r2);
        rooms.put(r3.getRoomId(), r3);

        Item sword = new Sword("I1", "Bronze Sword", "A simple sword with a dull edge.", "R2", false, 2);
        Item potion = new Potion("I2", "Small Potion", "Heals a little health.", "R1", true, 9);
        Item coin = new Item("I3", "Old Coin", "An old coin with strange markings.", "R3", true) {
            @Override
            public void use(Player player) {
                System.out.println("The coin feels warm in your hand, but nothing happens.");
            }
        };

        items.put(sword.getItemId(), sword);
        items.put(potion.getItemId(), potion);
        items.put(coin.getItemId(), coin);
    }
    private Room getCurrentRoom() {
        return rooms.get(player.getCurrentRoom());
    }

    private Room findRoom(String roomInput) {
        if (roomInput == null) {
            return null;
        }

        String target = roomInput.trim().replaceAll("\\s+", " ");
        for (Room room : rooms.values()) {
            if (room.getRoomName().equalsIgnoreCase(target)
                    || room.getRoomId().equalsIgnoreCase(target)) {
                return room;
            }
        }
        return null;
    }

    private void exploreRoom(Room room) {
        if (room == null) {
            System.out.println("There is no room here.");
            return;
        }

        System.out.print(room.getExploreText(items.values()));
    }

    public void takeItem(String itemName, Room currentRoomObj) {
        if (itemName == null || currentRoomObj == null) {
            System.out.println("Invalid pickup request.");
            return;
        }

        Item itemToPick = currentRoomObj.findItemInRoom(items.values(), itemName);

        if (itemToPick == null) {
            System.out.println("This item is not available in the current room.");
            return;
        }

        player.addItem(itemToPick);
        System.out.println(itemToPick.getItemName() + " was picked up and added to inventory.");
    }

    public void dropItem(String itemName, Room currentRoomObj) {
        if (itemName == null || currentRoomObj == null) {
            System.out.println("Invalid drop request.");
            return;
        }

        Item item = player.findItemByName(itemName);
        if (item == null) {
            System.out.println("You do not have this item in your inventory.");
            return;
        }

        if (player.dropItem(item, currentRoomObj)) {
            System.out.println(item.getItemName() + " has been dropped in " + currentRoomObj.getRoomName() + ".");
        } else {
            System.out.println("You could not drop that item.");
        }
    }

    public void displayInventory() {
        System.out.print(player.getInventoryText());
    }

    public void inspectItem(String itemName) {
        Item item = player.findItemByName(itemName);
        if (item == null) {
            System.out.println("You do not have that item.");
            return;
        }

        System.out.println(item);
    }

    public void moveToRoom(String roomName) {
        Room currentRoom = getCurrentRoom();
        Room destination = findRoom(roomName);

        if (destination == null) {
            System.out.println("That room does not exist.");
            return;
        }

        if (currentRoom != null && !currentRoom.isConnectedTo(destination.getRoomId())) {
            System.out.println("You can't go there from here.");
            return;
        }

        player.moveToRoom(destination.getRoomId());
        System.out.println("You moved to " + destination.getRoomName() + ".");
        System.out.println(destination.getRoomDesc());
    }

    public void run() {
        play();
    }

    public void play() {
        GameView.displayIntro();

        Room currentRoom = getCurrentRoom();
        if (currentRoom != null) {
            System.out.println("\nYou start in: " + currentRoom.getRoomName());
            System.out.println(currentRoom.getRoomDesc());
        }

        while (true) {
            currentRoom = getCurrentRoom();

            System.out.println("\nAvailable Commands:");
            System.out.println("  EXPLORE - See items and exits in the room");
            System.out.println("  TAKE <item-name>");
            System.out.println("  INSPECT <item-name> - Examine an item in inventory");
            System.out.println("  DROP <item-name> - Drop an item from inventory");
            System.out.println("  INVENTORY - View your inventory");
            System.out.println("  CONSUME - Consume a potion from inventory to restore health");
            System.out.println("  MOVE TO <room name> - Move to a connected room");
            System.out.println("  USE <item-name> - Use an item from inventory");
            System.out.println("  UNEQUIP <item-name> - Unequip a weapon to inventory");
            System.out.println("  STATUS - display player stats");
            System.out.println("  QUIT - Exit game");

            System.out.print("\nEnter command: ");
            String input = scanner.nextLine().trim();
            String upperInput = input.toUpperCase();

            if (upperInput.equals("QUIT")) {
                break;
            } else if (upperInput.equals("EXPLORE")) {
                exploreRoom(currentRoom);
            } else if (upperInput.startsWith("TAKE ")) {
                takeItem(input.substring(5).trim(), currentRoom);
            } else if (upperInput.startsWith("INSPECT ")) {
                inspectItem(input.substring(8).trim());
            } else if (upperInput.startsWith("DROP ")) {
                dropItem(input.substring(5).trim(), currentRoom);
            } else if (upperInput.equals("INVENTORY")) {
                displayInventory();
            } else if (upperInput.equals("CONSUME")) {
                System.out.print("Enter the name of the potion to consume: ");
                String potionName = scanner.nextLine().trim();
                boolean success = player.useItem(potionName);
                if (success) {
                    System.out.println("You consumed " + potionName + " and restored health.");
                } else {
                    System.out.println("You don't have that potion or it can't be consumed.");
                }
            } else if (upperInput.startsWith("MOVE TO ")) {
                moveToRoom(input.substring(8).trim());
            } else if (upperInput.startsWith("USE ")) {
                String itemName = input.substring(4).trim();
                boolean success = player.useItem(itemName);
                if (success) {
                    System.out.println("You used " + itemName + ".");
                } else {
                    System.out.println("You don't have that item or it can't be used.");
                }
            } else if (upperInput.startsWith("UNEQUIP ")) {
                String itemName = input.substring(8).trim();
                boolean success = player.unequipWeapon(itemName);
                if (success) {
                    System.out.println("You unequipped " + itemName + " and moved it to your inventory.");
                } else {
                    System.out.println("You don't have that weapon equipped or it can't be unequipped.");
                }
            } else if (upperInput.equals("STATUS")){
                System.out.println(player.getStatusText());
            }
            else {
                System.out.println("Invalid Command. Try Again.");
            }
        }

        System.out.println("Thanks for playing!");
        scanner.close();
    }
}
