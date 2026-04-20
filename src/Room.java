import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room
{
    private String roomID;
    private String roomName;
    private String roomDesc;
    private Map<String, String> exits;
    private List<Item> items;
    private Puzzle puzzle;
    private Monster monster;

    public Room(String roomID, String roomName, String roomDesc)
    {
        this.roomID = roomID;
        this.roomName = roomName;
        this.roomDesc = roomDesc;
        this.exits = new HashMap<>();
        this.items = new ArrayList<>();
        this.puzzle = null;
        this.monster = null;
    }

    public String getRoomID()
    {
        return roomID;
    }

    public void setRoomID(String roomID)
    {
        this.roomID = roomID;
    }

    public String getRoomName()
    {
        return roomName;
    }

    public void setRoomName(String roomName)
    {
        this.roomName = roomName;
    }

    public String getRoomDesc()
    {
        return roomDesc;
    }

    public void setRoomDesc(String roomDesc)
    {
        this.roomDesc = roomDesc;
    }

    public Map<String, String> getExits()
    {
        return Collections.unmodifiableMap(exits);
    }

    public void setExits(Map<String, String> exits)
    {
        this.exits.clear();

        if (exits != null)
        {
            for (Map.Entry<String, String> entry : exits.entrySet())
            {
                addExit(entry.getKey(), entry.getValue());
            }
        }
    }

    public void addExit(String direction, String destinationRoomID)
    {
        if (direction == null || direction.trim().isEmpty())
        {
            throw new IllegalArgumentException("Direction cannot be null or empty.");
        }

        if (destinationRoomID == null || destinationRoomID.trim().isEmpty())
        {
            throw new IllegalArgumentException("Destination room ID cannot be null or empty.");
        }

        exits.put(direction.trim().toUpperCase(), destinationRoomID.trim());
    }

    public boolean hasExit(String direction)
    {
        if (direction == null)
        {
            return false;
        }

        return exits.containsKey(direction.trim().toUpperCase());
    }

    public String getExit(String direction)
    {
        if (direction == null)
        {
            return null;
        }

        return exits.get(direction.trim().toUpperCase());
    }

    public List<Item> getItems()
    {
        return Collections.unmodifiableList(items);
    }

    public void setItems(List<Item> items)
    {
        this.items.clear();

        if (items != null)
        {
            this.items.addAll(items);
        }
    }

    public void addItem(Item item)
    {
        if (item == null)
        {
            throw new IllegalArgumentException("Item cannot be null.");
        }

        items.add(item);
    }

    public boolean removeItem(Item item)
    {
        if (item == null)
        {
            return false;
        }

        return items.remove(item);
    }

    public Item removeItemByName(String itemName)
    {
        Item item = getItemByName(itemName);

        if (item != null)
        {
            items.remove(item);
        }

        return item;
    }

    public Item getItemByName(String itemName)
    {
        if (itemName == null)
        {
            return null;
        }

        for (Item item : items)
        {
            if (item.getItemName().equalsIgnoreCase(itemName.trim()))
            {
                return item;
            }
        }

        return null;
    }

    public boolean hasItem(String itemName)
    {
        return getItemByName(itemName) != null;
    }

    public Puzzle getPuzzle()
    {
        return puzzle;
    }

    public void setPuzzle(Puzzle puzzle)
    {
        this.puzzle = puzzle;
    }

    public boolean hasPuzzle()
    {
        return puzzle != null;
    }

    public Monster getMonster()
    {
        return monster;
    }

    public void setMonster(Monster monster)
    {
        this.monster = monster;
    }

    public boolean hasMonster()
    {
        return monster != null;
    }

    public void removeMonster()
    {
        monster = null;
    }

    public void enter()
    {
    }

    public String describe()
    {
        StringBuilder sb = new StringBuilder();

        sb.append(roomName).append("\n");
        sb.append(roomDesc).append("\n");

        if (!items.isEmpty())
        {
            sb.append("Items: ");
            for (int i = 0; i < items.size(); i++)
            {
                sb.append(items.get(i).getItemName());

                if (i < items.size() - 1)
                {
                    sb.append(", ");
                }
            }
            sb.append("\n");
        }

        if (monster != null)
        {
            sb.append("Monster: ").append(monster.getName()).append("\n");
        }

        if (puzzle != null && !puzzle.isSolved())
        {
            sb.append("Puzzle: ").append(puzzle.getTrialName()).append("\n");
        }

        if (!exits.isEmpty())
        {
            sb.append("Exits: ");

            int count = 0;
            for (String direction : exits.keySet())
            {
                sb.append(direction);
                count++;

                if (count < exits.size())
                {
                    sb.append(", ");
                }
            }
        }

        return sb.toString().trim();
    }

    @Override
    public String toString()
    {
        return roomID + " - " + roomName;
    }
}
