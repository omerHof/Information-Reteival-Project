package Searcher;

import Application.ViewModel;
import Parse.Parser;
import Ranker.Rank;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * this class will get a query and return list of ranked documents match to the query.
 */

public class Searcher extends Thread  {

    private String query;
    private boolean stemming;
    private String queryNumber;

    public Searcher(String query, String queryNumber, boolean stemming) {
        this.query = query;
        this.stemming = stemming;
        this.queryNumber = queryNumber;
        this.parse();
    }

    /**
     * this method parse the query
     */
    private void parse()  {

        try {
            ArrayList<String> entities = getEntities(query);
            query = removeEntities(query);
            Parser parser = new Parser(0,query, ViewModel.getPathToData(),stemming,true);
            parser.parse();
            ArrayList<String> words = parser.getQueryWord();
            words.addAll(entities);
            System.out.println(words.stream().collect(Collectors.joining(" ")));

            Rank rank = new Rank(words,stemming);
            ArrayList<Integer> docs = rank.rankQuery();




        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String removeEntities(String query) {
        String[] temp = query.split(" ");
        ArrayList<String> res = new ArrayList<>();
        for(String term: temp){
            if(term.charAt(0)>91 && term.charAt(0)<123){
                res.add(term);
            }
        }
        return res.stream().collect(Collectors.joining(" "));
    }

    private ArrayList<String> getEntities(String query) {
        ArrayList <String> ans = new ArrayList<>();
        String[] temp = query.split(" ");
        String entity="";
        boolean add = false;
        int i=0;
        while (i<temp.length){
            if(temp[i].charAt(0)>64 && temp[i].charAt(0)<91){
                ans.add(temp[i]);
                entity +=temp[i]+ " ";
                i++;
                add = true;
                while(i<temp.length &&temp[i].charAt(0)>64 && temp[i].charAt(0)<91 ){
                    ans.add(temp[i]);
                    entity +=temp[i]+ " ";
                    i++;
                    add = true;
                }
            }else{
                i++;
            }
            if (add){
                ans.add(entity.substring(0,entity.length()-1));
            }
            add=false;
            entity="";
        }
        return ans;
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
