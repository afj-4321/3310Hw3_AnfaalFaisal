package edu.wmich.cs3310.hw3.main;

/**
 * Data holder
 */

public class Item {

    private String name;
    private int minimumStrenght;
    private int maximumStrenght;
    private String rarity;
    private int currentStrenght;

    /**
     * Private constructor for cloning
     */
    private Item() {
    }

    /**
     * Typical constructor
     *
     * @param line with basic data
     */
    public Item(String line) {
        String[] data = line.split(",");
        name = data[0];
        minimumStrenght = Integer.parseInt(data[1]);
        maximumStrenght = Integer.parseInt(data[2]);
        rarity = data[3];
    }

    /*Getters and setters*/
    public String getName() {
        return name;
    }

    public int getMinimumStrenght() {
        return minimumStrenght;
    }

    public int getMaximumStrenght() {
        return maximumStrenght;
    }

    public String getRarity() {
        return rarity;
    }

    public int getCurrentStrenght() {
        return currentStrenght;
    }

    public void setCurrentStrenght(int currentStrenght) {
        this.currentStrenght = currentStrenght;
    }

    /**
     * Cloning item
     * @return exact clone of object
     * @throws CloneNotSupportedException ignored
     */
    public Item clone() throws CloneNotSupportedException {
        Item newItem = new Item();
        newItem.rarity = rarity;
        newItem.name = name;
        newItem.minimumStrenght = minimumStrenght;
        newItem.maximumStrenght = maximumStrenght;
        newItem.currentStrenght = currentStrenght;
        return newItem;
    }

    /**
     *
     * @return string representation
     */
    @Override
    public String toString() {
        return rarity + " " + name + ", " + currentStrenght;
    }

    /**
     * compare object by both parts of name
     * @param item another object
     * @return result
     */
    public boolean equalsName(Item item) {
        if (name.equals(item.name)) {
            return rarity.equals(item.rarity);
        } else {
            return false;
        }
    }
}
