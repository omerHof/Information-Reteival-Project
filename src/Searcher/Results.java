package Searcher;

import Application.ViewModel;
import Query.DominantEntity;
import Query.InitQuery;
import javax.management.Query;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class Results {

    private static Results results;
    private static boolean lock;
    private static ArrayList<String> resultsArray;
    private static ConcurrentHashMap<String,ArrayList<String>> resultHashMap;
    private static BlockingQueue<String> queue;
    private static String queryNumber;
    private static int queriesCounter;

    private Results() {
        //queue = new ArrayBlockingQueue(10000);
        resultHashMap = new ConcurrentHashMap<>();
        lock = true;
        queriesCounter=0;

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
            resultHashMap.put(queryNumber,resultsArray);
            queriesCounter++;
            lock=true;

        }
        if(InitQuery.getNumberOfQueries()<=queriesCounter){
            writeToFile();
        }
    }

    private void writeToFile() throws IOException {
        FileWriter writer = new FileWriter(new File(ViewModel.getPathToOutput() +"//results.txt"));
        SortedSet<String> keys = new TreeSet<>(resultHashMap.keySet());
        ArrayList<String> writeResult = new ArrayList<>();
        for (String key : keys) {
            ArrayList<String> value = resultHashMap.get(key);
            for(String result: value){
                writeResult.add(key + " 0 "+ result+ " 0");
            }

        }

        for (String str : writeResult) {
            writer.write(str + System.lineSeparator());
        }
        writer.close();
    }
}
