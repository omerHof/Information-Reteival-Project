package invertedIndex;

import Parse.Parser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * this class create sorted tables of words and write them to the disk
 * input: words after parse
 * output: tables
 */
public class SortedTablesThreads {
    private static int tableNum = 0;
    private static ArrayList<String> table = new ArrayList<>();
    private static BlockingQueue <String> queue = new ArrayBlockingQueue(10000000);
    private static boolean lock= true;
    private boolean stemming;
    private String wordFolder;

    /**
     * constructor
     * @param stemming
     */
    public SortedTablesThreads(boolean stemming) {
        this.stemming = stemming;
        if (stemming){
            this.wordFolder = "stemming";
        }else{
            this.wordFolder = "withoutStemming";
        }
    }

    /**
     * get words from parse, insert to tables and writes to the disk
     * @param word
     * @param docNum
     * @param position
     * @throws IOException
     */
    public void addToTable(String word, int docNum, int position) throws IOException {
        if (word == null || word.equals("")) {
            return;
        }
        queue.add(word + " " + docNum + " " + position);

        if (queue.size() > 1500000 && lock) {
            lock = false;
            writeToFile();
            lock = true;
        }
    }

    public void addLastTable() throws IOException {
        writeToFile();
    }

    /**
     * this function write the tables to files
     * @throws IOException
     */
    private void writeToFile() throws IOException {

        tableNum++;
        String[] arr = queue.toArray(new String[queue.size()]);
        queue.clear();
        Arrays.sort(arr);
        FileWriter writer = new FileWriter(new File(wordFolder+"//" + tableNum + ".txt"));
        for (String str : arr) {
            writer.write(str + System.lineSeparator());
        }
        writer.close();
    }

    /**
     * this function write the entities to files by another table
     * @throws IOException
     */
    public void entityToSortedTable() throws IOException {
        HashMap<String,ArrayList<Integer>> entities = Parser.getEntities();
        TreeMap<String, ArrayList<Integer>> sorted = new TreeMap<>();
        sorted.putAll(entities);
        Iterator it = sorted.entrySet().iterator();
        tableNum++;
        FileWriter writer = new FileWriter(new File(wordFolder+"//" + tableNum + ".txt"));
        while (it.hasNext()) {
            Map.Entry entity = (Map.Entry)it.next();
            if (  ((ArrayList<Integer>) entity.getValue()).size()>1){
                ArrayList<Integer> arr = (ArrayList<Integer>) entity.getValue();
                for(Integer docNum: arr){
                    writer.write((String)entity.getKey() + " "+ docNum + " 0"+ System.lineSeparator());
                }
            }
        }
        writer.close();
    }

    public static void setTableNum(int tableNum) {
        SortedTablesThreads.tableNum = tableNum;
    }
}
