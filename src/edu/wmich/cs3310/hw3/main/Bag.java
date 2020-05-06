package edu.wmich.cs3310.hw3.main;

import java.util.LinkedList;
import java.util.Random;

/**
 * Hash holder for item data
 */
public class Bag {
    private static int sizeHashtable = 199;
    private static int addingSize = 125;
    private Item[] itemsArray;
    private LinkedList<Item>[] itemsList;
    private int numberBag;
    private int hashingType;
    private int probingType;
    private boolean isOpenHashing;
    private int[] randomShuffle;

    /**
     * Complicated constructor
     *
     * @param items         for filling bag
     * @param isOpenHashing if open hasning used for this
     * @param hashingType   which type of hash will be used
     * @param probingType   which type of probing will be used
     * @param numberBag     for output
     */
    public Bag(Item[] items, boolean isOpenHashing, int hashingType, int probingType, int numberBag) {
        this.numberBag = numberBag;
        this.hashingType = hashingType;
        this.probingType = probingType;
        this.isOpenHashing = isOpenHashing;
        Random rd = new Random();
        int k;
        if (isOpenHashing) {
            itemsList = new LinkedList[sizeHashtable];
            for (int i = 0; i < addingSize; i++) {
                Item currentItem = items[rd.nextInt(items.length)];
                k = currentItem.getMaximumStrenght() - currentItem.getMinimumStrenght();
                int y = rd.nextInt(k);
                currentItem.setCurrentStrenght(1 + currentItem.getMinimumStrenght() + y);
                int number = getHash(currentItem, hashingType) % sizeHashtable;
                if (itemsList[number] == null) {
                    itemsList[number] = new LinkedList<>();
                }
                itemsList[number].add(currentItem);
            }
        } else {
            itemsArray = new Item[sizeHashtable];
            if (probingType > 1) {
                randomShuffle = new int[sizeHashtable];
                for (int i = 0; i < sizeHashtable; i++) {
                    randomShuffle[i] = i;
                }
                int temp;
                int randomIndexToSwap;
                for (int i = 0; i < randomShuffle.length; i++) {
                    randomIndexToSwap = rd.nextInt(randomShuffle.length);
                    temp = randomShuffle[randomIndexToSwap];
                    randomShuffle[randomIndexToSwap] = randomShuffle[i];
                    randomShuffle[i] = temp;
                }
            }
            for (int i = 0; i < addingSize; i++) {
                Item currentItem = items[rd.nextInt(items.length)];
                k = currentItem.getMaximumStrenght() - currentItem.getMinimumStrenght();
                currentItem.setCurrentStrenght(1 + currentItem.getMinimumStrenght() + rd.nextInt(k));
                int number = getHash(currentItem, hashingType);
                switch (probingType) {
                    case 0://linear probing
                        number = number % sizeHashtable;
                        while (itemsArray[number] != null) {
                            number++;
                            if (number == sizeHashtable) {
                                number = 0;
                            }
                        }
                        itemsArray[number] = currentItem;
                        break;
                    case 1://double hashing
                        // H2(K) = 1 + ( (K/M) mod (M-1) )
                        int hash1 = number;
                        int hash2 = 1 + ((hash1 / 19) % 198);
                        number = number % sizeHashtable;
                        while (itemsArray[number] != null) {
                            number = number + hash2;
                            number = number % sizeHashtable;
                        }
                        itemsArray[number] = currentItem;
                        break;
                    default://pseudo random probing
                        number = number % sizeHashtable;
                        int cc = 0;
                        while (itemsArray[number] != null) {
                            number = number + randomShuffle[cc];
                            cc++;
                            number = number % sizeHashtable;
                        }
                        itemsArray[number] = currentItem;
                        break;
                }
            }
        }
    }

    /**
     * Return selected type of item hash
     *
     * @param item        object for hashing
     * @param hashingType ne of types
     * @return hash
     */
    private int getHash(Item item, int hashingType) {
        int hash = 0;
        String uniqueID = item.getName() + item.getRarity();
        switch (hashingType) {
            case 0:
                for (byte v : uniqueID.getBytes()) {
                    hash = 31 * hash + (v & 0xff);
                }
                break;
            case 2:
                hash = uniqueID.hashCode();
                break;
            default:
                char[] chars = uniqueID.toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    hash = hash + ((i + 1) * chars[i]);
                }
        }
        if (hash >= 0) {
            return hash;
        } else {
            return Integer.MAX_VALUE - 1 + hash;
        }

    }

    /**
     * return string representation of bag
     *
     * @param fullType required parameter
     * @return string view
     */
    public String print(boolean fullType) {
        StringBuilder res = new StringBuilder("Bag " + numberBag + " using ");
        if (isOpenHashing) {
            res.append("Open ");
        } else {
            switch (probingType) {
                case 0:
                    res.append("Linear-Probing Closed ");
                    break;
                case 1:
                    res.append("Pseudo-Random-Probing Closed ");
                    break;
                default:
                    res.append("Double-Hashing-Probing Closed ");
                    break;
            }
        }
        res.append("Hashing with my Hashing Function").append(hashingType + 1).append(": \n");
        if (fullType) {
            if (isOpenHashing) {
                for (LinkedList<Item> items : itemsList) {
                    if (items != null) {
                        if (items.size() == 1) {
                            res.append("\t").append(items.get(0)).append("\n");
                        } else {
                            res.append("\t(").append(items.get(0)).append(");");
                            for (int u = 1; u < items.size(); u++) {
                                res.append(" (").append(items.get(u)).append(");");
                            }
                            res.append("\n");
                        }
                    }

                }
            } else {
                for (Item item : itemsArray) {
                    if (item != null) {
                        res.append("\t").append(item).append("\n");
                    }
                }
            }
        } else {
            int rows = 0;
            if (isOpenHashing) {
                for (int i = 0, itemsListLength = itemsList.length; i < itemsListLength; i++) {
                    LinkedList<Item> items = itemsList[i];
                    if (items != null) {
                        rows++;
                        if (items.size() == 1) {
                            res.append("\t").append(items.get(0)).append("\n");
                        } else {
                            res.append("\t(").append(items.get(0)).append(");");
                            for (int u = 1; u < items.size(); u++) {
                                res.append(" (").append(items.get(u)).append(");");
                            }
                            res.append("\n");
                        }
                    }
                    if (rows > 4) {
                        i = itemsListLength + 1;
                    }
                }
            } else {
                for (int i = 0; i < itemsArray.length; i++) {
                    Item item = itemsArray[i];
                    if (item != null) {
                        rows++;
                        res.append("\t").append(item).append("\n");
                    }
                    if (rows > 4) {
                        i = itemsArray.length + 1;
                    }
                }
            }
        }
        return res.toString();
    }

    /**
     * @return number of bug
     */
    public int getNumber() {
        return numberBag;
    }

    /**
     * Search item in bag, using hash
     *
     * @param item searched object
     * @return list with indexes
     */
    public LinkedList<Integer> find(Item item) {
        LinkedList<Integer> res = new LinkedList<>();
        if (isOpenHashing) {
            int number = getHash(item, hashingType) % sizeHashtable;
            if (itemsList[number] != null) {
                for (int i = 0; i < itemsList[number].size(); i++) {
                    if (itemsList[number].get(i).equalsName(item)) {
                        res.add(number);
                    }
                }
            }
        } else {
            int number = getHash(item, hashingType);
            switch (probingType) {
                case 0://linear probing
                    number = number % sizeHashtable;
                    while (itemsArray[number] != null) {
                        if (itemsArray[number].equalsName(item)) {
                            res.add(number);
                        }
                        number++;
                        if (number == sizeHashtable) {
                            number = 0;
                        }
                    }
                    break;
                case 1://double hashing
                    // H2(K) = 1 + ( (K/M) mod (M-1) )
                    int hash1 = number;
                    int hash2 = 1 + ((hash1 / 19) % 199);
                    number = number % sizeHashtable;
                    while (itemsArray[number] != null) {
                        if (itemsArray[number].equalsName(item)) {
                            res.add(number);
                        }
                        number = number + hash2;
                        number = number % sizeHashtable;
                    }
                    break;
                default://pseudo random probing
                    number = number % sizeHashtable;
                    int cc = 0;
                    while (itemsArray[number] != null) {
                        if (itemsArray[number].equalsName(item)) {
                            res.add(number);
                        }
                        number = number + randomShuffle[cc];
                        cc++;
                        number = number % sizeHashtable;
                    }
                    break;
            }
        }
        return res;
    }

    /**
     * Return strenghts of selected items
     *
     * @param res  list with indexes
     * @param item for comparison
     * @return list of strenghts
     */
    public LinkedList<Integer> getStrengths(LinkedList<Integer> res, Item item) {
        LinkedList<Integer> str = new LinkedList<>();
        if (isOpenHashing) {
            try {
                for (Integer ii : res) {
                    for (Item ls : itemsList[ii]) {
                        if (item.equalsName(ls)) {
                            str.add(ls.getCurrentStrenght());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            for (Integer ii : res) {
                if (item.equalsName(itemsArray[ii])) {
                    str.add(itemsArray[ii].getCurrentStrenght());
                }
            }
        }
        return str;
    }
}
