package Searcher;

import Application.ViewModel;
import Parse.Parser;
import Ranker.Rank;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * this class will get a query and return list of ranked documents match to the query.
 */

public class Searcher extends Thread  {

    private String query;
    private boolean stemming;

    public Searcher(String query, boolean stemming) {
        this.query = query;
        this.stemming = stemming;
        this.parse();
    }

    private void parse()  {
        try {
            Parser parser = new Parser(0,query, ViewModel.getPathToData(),stemming,true);
            parser.parse();
            ArrayList<String> words = parser.getQueryWord();
            HashMap<String,ArrayList<Integer>> entities =Parser.getEntities();
            words.addAll(entities.keySet());
            System.out.println(words.stream().collect(Collectors.joining(" ")));
            //Rank rank = new Rank(words,stemming);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
