package Searcher;

import Query.DominantEntity;

import java.util.ArrayList;
import java.util.HashMap;

public class Results {

    private static Results results;
    private static ArrayList<String> resultsArray;
    private static String queryNumber;

    private Results(ArrayList<String> results, String queryNumber) {
        this.resultsArray = results;
        this.queryNumber = queryNumber;
    }

    public static Results getResultsInstance(ArrayList<String> resultsArray, String queryNumber){
        if(results == null){
            synchronized (Results.class) {
                if(results == null){
                    results = new Results(resultsArray,queryNumber);
                }
            }
        }
        return results;
    }



}
