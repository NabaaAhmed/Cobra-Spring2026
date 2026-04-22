public abstract class Item {
    protected String id;
    protected String name;
    protected String description;
    protected boolean stackable;
    protected String type;

    public Item(String id, String name, String description, boolean stackable, String type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.stackable = stackable;
        this.type = type;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public boolean isStackable() { return stackable; }
    public String getType() { return type; }

    public abstract void use(Player player);
}