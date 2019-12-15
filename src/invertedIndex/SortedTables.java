package invertedIndex;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * this class create sorted tables of words and write them to the disk
 * input: words after parse
 * output: tables
 */
public class SortedTables {

    private static int tableNum = 0;
    private static ArrayList<String> table = new ArrayList<>();
    private static SortedTables sortedTables=new SortedTables();

    /**
     * get words from parse, insert to tables and writes to the disk
     *
     * @param word
     * @param docNum
     * @param position
     * @throws IOException
     */

    public void addToTable(String word, int docNum, int position) throws IOException {
        if (word == null || word.equals("")) {
            return;
        }
        //each table contains words from x docs
        if (docNum / 10000 == tableNum) {
            table.add(word + " " + docNum + " " + position);
        } else {
            writeToFile();
        }
    }

    public void addEntityToTable(String word, int docNum, int position){
        if (word == null || word.equals("")) {
            return;
        }
        table.add(word + " " + docNum + " " + position);
    }

    public static void addLastTable() throws IOException {
        sortedTables.writeToFile();
    }

    public void writeToFile() throws IOException {
        Collections.sort(table);
        tableNum++;
        FileWriter writer = new FileWriter(new File("prePosting//" + tableNum + ".txt"));
        for (String str : table) {
            writer.write(str + System.lineSeparator());
        }
        writer.close();
        table.clear();
    }
}