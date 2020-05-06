package edu.wmich.cs3310.hw3.main;

import java.io.*;
import java.util.LinkedList;
import java.util.Random;

public class App {

    Bag[] bags;
    int n;
    boolean fullOutput;
    String output;

    /**
     * Constructor
     *
     * @param itemsArray for filling intrnal stores
     * @param n number of bugs
     */
    public App(Item[] itemsArray, int n) {
        this.n = n;
        output = "";
        bags = new Bag[n * 12];
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 3; j++) {
                bags[count] = new Bag(itemsArray, true, j, 0, i + 1);
                bags[count + 1] = new Bag(itemsArray, false, j, 0, i + 1);
                bags[count + 2] = new Bag(itemsArray, false, j, 1, i + 1);
                bags[count + 3] = new Bag(itemsArray, false, j, 2, i + 1);
                count = count + 4;
            }
        }
        output = output + ("\nn=" + n) + "\n";
        if (n == 1) {
            for (Bag bag : bags) {
                output = output + (bag.print(true)) + "\n";
            }
        } else {
            if (n <= 10) {
                for (Bag bag : bags) {
                    output = output + (bag.print(false)) + "\n";
                    output = output + ("...") + "\n";
                }
            }
        }
        fullOutput = n < 11;
        output = output + "\n";
    }


    /**
     * Create basic array with items from file
     *
     * @return constructed array
     */
    private static Item[] readFile() {
        Item[] array = new Item[750];
        try (BufferedReader br = new BufferedReader(new FileReader("hw3input_items.txt"))) {
            String line;
            br.readLine();
            int i = 0;
            while ((line = br.readLine()) != null) {
                if (i < array.length) {
                    array[i] = new Item(line);
                    i++;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return array;
    }

    /**
     * Search random item from array
     *
     * @param itemsArray array with items
     */
    private void search(Item[] itemsArray) {
        Random rd = new Random();
        long[] timeArray = new long[bags.length];
        /**/
        Item item2 = itemsArray[rd.nextInt(itemsArray.length)];
        for (Bag bag : bags) {
            long time = System.nanoTime();
            LinkedList<Integer> res = bag.find(item2);
            if (res.size() != 0) {
                LinkedList<Integer> resStrength = bag.getStrengths(res, item2);
            }
        }
        /**/
        for (int i = 0; i < 5; i++) {
            Item item = itemsArray[rd.nextInt(itemsArray.length)];
            if (fullOutput) {
                output = output + "Searching for " + item.getRarity() + " " + item.getName() + "... \nFound in";
            }
            for (int i1 = 0; i1 < bags.length; i1++) {
                Bag bag = bags[i1];
                long time = System.nanoTime();
                LinkedList<Integer> res = bag.find(item);
                timeArray[i1] = timeArray[i1] + (System.nanoTime() - time);
                if (fullOutput) {
                    StringBuilder outp = new StringBuilder();
                    if (res.size() != 0) {
                        outp.append(" (bag ").append(bag.getNumber()).append(" slots");
                        for (int g = 0; g < res.size(); g++) {
                            if (g == (res.size() - 1)) {
                                outp.append(" ").append(res.get(g)).append(",");
                            } else {
                                outp.append(" ").append(res.get(g)).append(".");
                            }
                        }
                        outp.append("  Strengths:");
                        LinkedList<Integer> resStrength = bag.getStrengths(res, item);
                        for (int g = 0; g < resStrength.size(); g++) {
                            if (g == (resStrength.size() - 1)) {
                                outp.append(" ").append(resStrength.get(g)).append(")");
                            } else {
                                outp.append(" ").append(resStrength.get(g)).append(",");
                            }
                        }
                    }
                    output = output + outp.toString();
                    if (i1 == (bags.length - 1)) {
                        output = output + "\n";
                    }
                }
            }
            if (fullOutput) {
                output = output + "\n";
            }
        }
     for (int c = 0; c < timeArray.length; c++) {
            System.out.print(timeArray[c] / 5);
            if (c > 0 & (c % 12 == 0)) {
                System.out.println();
            } else {
                System.out.print(" ");
            }
        }    /*/for all the average to be output /*/
        output = output + "\nTotal average\n";
        long[] timeArrayHelper = new long[12];
        for (int i = 0; i < timeArray.length; i++) {
            timeArrayHelper[i % 12] = timeArrayHelper[i % 12] + timeArray[i];
        }
        for (int i = 0; i < 12; i++) {
            output = output + ((timeArrayHelper[i] * 12) / (timeArray.length * 5)) + " ";
        }
        output = output + "\n";

    }

    /**
     * Entry point
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        try {
            PrintStream out = new PrintStream(new FileOutputStream("output.txt", true), true);
            /* System.setOut(out);*///
            int[] array = new int[]{1, 10, 100, 1000, 10000};
            Item[] itemsArray = readFile();
            for (int value : array) {
                App app = new App(itemsArray, value);
                app.search(itemsArray);
                System.out.println(app.output);
                BufferedWriter writer = new BufferedWriter(new FileWriter("n="+value+".txt"));
                writer.write(app.output);
                writer.close();
                System.setOut(out);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
