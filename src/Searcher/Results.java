package Searcher;

import Application.ViewModel;
import Query.DominantEntity;
import Query.InitQuery;
import javax.management.Query;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Results {

    private static Results results;
    private static boolean lock;
    private static ArrayList<String> resultsArray;
    private static BlockingQueue<String> queue;
    private static String queryNumber;
    private static int queriesCounter;

    private Results() {
        queue = new ArrayBlockingQueue(10000);
        lock = true;

    }

    public static Results getResultsInstance(){
        if(results == null){
            synchronized (Results.class) {
                if(results == null){
                    results = new Results();
                }
            }
        }
        return results;
    }

    public void insertToResultList(ArrayList<String> resultsArray, String queryNumber) throws IOException {
        if (resultsArray == null || queryNumber== null) {
            return;
        }
        if(lock){
            lock=false;
            for(String docName: resultsArray) {
                queue.add(queryNumber+ " 0"+ docName+ " 0");
            }
            lock=true;
        }
        if(InitQuery.getNumberOfQueries()==queriesCounter){
            writeToFile(queue);
        }
    }

    private void writeToFile(BlockingQueue<String> queue) throws IOException {

        String[] arr = queue.toArray(new String[queue.size()]);
        queue.clear();
        //Arrays.sort(arr);
        FileWriter writer = new FileWriter(new File(ViewModel.getPathToOutput() +"//results.txt"));
        for (String str : arr) {
            writer.write(str + System.lineSeparator());
        }
        writer.close();
    }
}
