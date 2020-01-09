package Searcher;

import Application.ViewModel;
import Parse.Parser;
import Ranker.Rank;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

    /**
     * this method parse the query
     */
    private void parse()  {
        try {
            Parser parser = new Parser(0,query, ViewModel.getPathToData(),stemming,true);
            parser.parse();
            ArrayList<String> words = parser.getQueryWord();
            words.addAll(parser.getQueryEntity());
            reduceNotEntity(words);
            System.out.println(words.stream().collect(Collectors.joining(" ")));
            Rank rank = new Rank(words,stemming);
            ArrayList<String> docs = new ArrayList<>();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void reduceNotEntity(ArrayList<String> words) {
        for (int i=0;i<words.size();i++){
            if(words.get(i).contains(" ")){
                String[] temp = words.get(i).split(" ");
                words.remove(words.get(i));
                words.addAll(Arrays.asList(temp));
            }
        }

        for(int i=0;i<words.size()-1;i++){
            if(words.get(i).charAt(0)>91){
                for( int j=i+1;j<words.size();j++ ){
                    if (words.get(i).equals(words.get(j).toLowerCase())){
                        words.remove(i);
                        i--;
                        break;
                    }
                }
            }
        }
    }


}
