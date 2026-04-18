import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class GameControllerMaiTesting {
    private final Scanner scanner;
    private final Map<String, Item> items;
    private final Map<String, Room> rooms;
    private Player player;

    public GameControllerMaiTesting() {
        this.scanner = new Scanner(System.in);
        this.items = new HashMap<>();
        this.rooms = new HashMap<>();
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

        player = new Player("R1", 5, 5, 1);
    }

    private Room getCurrentRoom() {
        return rooms.get(player.getCurrentRoom());
    }

    private Room findRoom(String roomInput) {
        if (roomInput == null) {
            return null;
        }

        String target = roomInput.trim();
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

        System.out.println("\n=== " + room.getRoomName() + " ===");
        System.out.println(room.getRoomDesc());
        System.out.println("Connections: " + room.getConnections());
        System.out.println("Items in room:");

        boolean foundAny = false;
        for (Item item : items.values()) {
            if (room.getRoomId().equals(item.getRoomID()) && !item.isInPlayerInventory()) {
                System.out.println("  - " + item.getitemName());
                foundAny = true;
            }
        }

        if (!foundAny) {
            System.out.println("  - None");
        }
    }

    public void takeItem(String itemName, Room currentRoomObj) {
        if (itemName == null || currentRoomObj == null) {
            System.out.println("Invalid pickup request.");
            return;
        }

        Item itemToPick = null;
        for (Item item : items.values()) {
            boolean sameName = item.getitemName() != null && item.getitemName().equalsIgnoreCase(itemName.trim());
            boolean sameRoom = currentRoomObj.getRoomId().equals(item.getRoomID());
            if (sameName && sameRoom && !item.isInPlayerInventory()) {
                itemToPick = item;
                break;
            }
        }

        if (itemToPick == null) {
            System.out.println("This item is not available in the current room.");
            return;
        }

        player.addItem(itemToPick);
        System.out.println(itemToPick.getitemName() + " was picked up and added to inventory.");
    }

    public void dropItem(String itemName, Room currentRoomObj) {
        if (itemName == null || currentRoomObj == null) {
            System.out.println("Invalid drop request.");
            return;
        }

        Item item = getItemFromInventory(itemName);
        if (item == null) {
            System.out.println("You do not have this item in your inventory.");
            return;
        }

        player.removeItem(item);
        item.moveToRoom(currentRoomObj.getRoomId());
        System.out.println(item.getitemName() + " has been dropped in " + currentRoomObj.getRoomName() + ".");
    }

    public void displayInventory() {
        if (player.getInventory().isEmpty()) {
            System.out.println("You have not picked up any items yet.");
            return;
        }

        System.out.println("Your Inventory:");
        for (Item item : player.getInventory()) {
            System.out.println("  - " + item.getitemName());
        }
    }

    public void inspectItem(String itemName) {
        Item item = getItemFromInventory(itemName);
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

        if (currentRoom != null && !currentRoom.hasConnection(destination.getRoomId())) {
            System.out.println("You can't go there from here.");
            return;
        }

        player.moveToRoom(destination.getRoomId());
        System.out.println("You moved to " + destination.getRoomName() + ".");
        System.out.println(destination.getRoomDesc());
    }

    private Item getItemFromInventory(String itemName) {
        if (itemName == null) {
            return null;
        }

        String target = itemName.trim();
        for (Item item : player.getInventory()) {
            if (item.getitemName() != null && item.getitemName().equalsIgnoreCase(target)) {
                return item;
            }
        }
        return null;
    }

    public void run() {
        play();
    }

    public void play() {
        System.out.println("     /\\      /\\      /\\      /\\      /\\      /\\      /\\");
        System.out.println("    /  \\    /  \\    /  \\    /  \\    /  \\    /  \\    /  \\");
        System.out.println("    \\  /    \\  /    \\  /    \\  /    \\  /    \\  /    \\  /");
        System.out.println("     \\/      \\/      \\/      \\/      \\/      \\/      \\/");

        System.out.println("==============WELCOME TO THE DUNGEON OF TRIALS==============");
        System.out.println("The Dungeon of Trials was constructed by a king to find a worthy successor.");
        System.out.println("The dungeon tests character across five themed trials.");
        System.out.println("A detached Teleport Trap Room serves as a penalty zone for incorrect puzzle actions.");
        System.out.println("Good Luck!");

        Room currentRoom = getCurrentRoom();
        if (currentRoom != null) {
            System.out.println("\nYou start in: " + currentRoom.getRoomName());
            System.out.println(currentRoom.getRoomDesc());
        }

        while (true) {
            currentRoom = getCurrentRoom();

            System.out.println("\nAvailable Commands:");
            System.out.println("  EXPLORE - See items and exits in the room");
            System.out.println("  PICKUP <item-name> - Pick up an item");
            System.out.println("  INSPECT <item-name> - Examine an item in inventory");
            System.out.println("  DROP <item-name> - Drop an item from inventory");
            System.out.println("  INVENTORY - View your inventory");
            System.out.println("  CONSUME - Consume a potion from inventory to restore health");
            System.out.println("  MOVE TO <room name> - Move to a connected room");
            System.out.println("  USE <item-name> - Use an item from inventory");
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
            } else if (upperInput.equals("STATUS")){
                System.out.println("current HP:" + player.getCurrentHP()+
                        "\nmax HP:" + player.getMaxHP()+
                        "\nattack power:" + player.getAttackPower());
            }
            else {
                System.out.println("Invalid Command. Try Again.");
            }
        }

        System.out.println("Thanks for playing!");
        scanner.close();
    }
}
