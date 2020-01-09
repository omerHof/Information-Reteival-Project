package Searcher;

import Application.ViewModel;
import Parse.Parser;
import Query.Semantic;
import Ranker.Rank;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
            ArrayList<String> additionalWords=new ArrayList<>();
            if(ViewModel.isSemantic()){
                additionalWords=getSemanticWords(words);
                for(String s:additionalWords){//todo remove
                    System.out.println("additional word: "+s);
                }
            }
            Rank rank = new Rank(words,additionalWords,stemming);
            ArrayList<Integer> docs = rank.rankQuery();

            //test
            int i=1;
            for(Integer doc:docs){
                System.out.println("number"+i+": "+doc);
                i++;
            }



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return
     */
    private ArrayList<String> getSemanticWords(ArrayList<String> words) {
        ArrayList<String> result= new ArrayList<>();
        try {
            for (String word : words) {
                String[] semanticWords = Semantic.getSemanticWords(word);
                ArrayList<String> wordList = new ArrayList(Arrays.asList(semanticWords));
                wordList.remove(0);
                for(String s:wordList)
                result.add(s);
            }

        }
        catch (Exception e){

        }
        return result;
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
