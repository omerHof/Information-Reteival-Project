package Searcher;

import Application.ViewModel;
import Parse.Parser;
import Query.Semantic;
import Query.docNum;
import Query.initPartB;
import Ranker.Rank;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static Searcher.Results.getResultsInstance;

/**
 * this class will get a query and return list of ranked documents match to the query.
 */

public class Searcher extends Thread  {

    private String query;
    private boolean stemming;
    private String queryNumber;
    private String description;

    public Searcher(String query, String queryNumber, boolean stemming, String description) {
        this.query = query;
        this.stemming = stemming;
        this.queryNumber = queryNumber;
        this.description = description;
    }

    /**
     * this method parse the query
     */
    public void run()  {

        try {
            ArrayList<String> queryWords = new ArrayList<>();
            ArrayList<String> descriptionWords = new ArrayList<>();
            if(query!=null){
                ArrayList<String> queryEntities = getEntities(query);
                queryWords = prepareInput(query);
                queryWords.addAll(queryEntities);
            }

/*
            if(description!=null){
                ArrayList<String> descriptionEntities = getEntities(description);
                descriptionWords = prepareInput(description);
                descriptionWords.addAll(descriptionEntities);
            }

 */





            System.out.println("FINISH PARSE query number:"+queryNumber );
            ArrayList<String> queryAdditionalWords=new ArrayList<>();
            ArrayList<String> descriptionAdditionalWords=new ArrayList<>();

            if(ViewModel.isSemantic()){
                queryAdditionalWords=getSemanticWords(queryWords);
                if(description!=null){
                    //descriptionAdditionalWords = getSemanticWords(descriptionWords);
                }
            }

            Rank rank = new Rank(queryWords,queryAdditionalWords,descriptionWords,descriptionAdditionalWords,stemming);//todo change null to description
            ArrayList<Integer> docs = rank.rankQuery();
            System.out.println("FINISH RANK query number:" + queryNumber);
            ArrayList<String> stringDocs = new ArrayList<>();

            HashMap<Integer, String> convert = initPartB.getDocNum();
            for (Integer doc : docs) {
                stringDocs.add(convert.get(doc));
            }
            Results results = Results.getResultsInstance();
            results.insertToResultList(stringDocs, queryNumber);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * this function get a query, send it to parser, and return arrayList of words
     *
     * @param query
     * @return
     * @throws IOException
     */
    private ArrayList<String> prepareInput(String query) throws IOException {

        query = removeEntities(query);
        Parser parser = new Parser(0,query, ViewModel.getPathToOutput(),stemming,true);
        parser.parse();
        return parser.getQueryWord();
    }

    /**
     * this function get List of query's words, and returns the semantic words
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

    /**
     * get a string amd remove the entities
     *
     * @param query
     * @return
     */
    private String removeEntities(String query) {
        String[] temp = query.split(" ");
        ArrayList<String> res = new ArrayList<>();
        for(String term: temp){
            if(term.charAt(0)>91 && term.charAt(0)<123){
                res.add(term);
            } else {
                res.add(term.toLowerCase());
                res.add(term.toUpperCase());

            }
        }
        return res.stream().collect(Collectors.joining(" "));
    }

    /**
     * get a string (query) and return a list of entities
     *
     * @param query
     * @return
     */
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

    /**
     * get list of words and reduce word who are not entity
     *
     * @param words
     */
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
