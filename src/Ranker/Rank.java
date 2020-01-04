package Ranker;


import Application.ViewModel;
import invertedIndex.Dictionary;
import javafx.application.Application;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Rank {
    ArrayList<String> words;
    HashMap<Integer,Integer> scores;
    boolean stemming;
    Dictionary oldDictionary;
    final int b=0.75;

    /**
     * constructor
     * @param words
     * @param scores
     * @param stemming
     */
    public Rank(ArrayList<String> words, HashMap<String, Integer> scores, boolean stemming) {
        this.words = words;
        this.scores = scores;
        this.stemming = stemming;
    }

    public HashMap<String,Integer> rankWord(String word){
        HashMap<String,String> dictionary= oldDictionary.getDictionary();//todo get the dictionary
        String[]values=dictionary.get(word).split("-");
        int numOfDocs= Integer.parseInt(values[0]);
        String postingFileName=values[1];
        int postingLine=Integer.parseInt(values[2]);
        ArrayList<String> posting;//todo read the posting
        String[]line=posting.get(postingLine).split(" ");

        while(oldDictionary.calculateWord(line).equals(word)&& postingLine<posting.size()) {//iterate all lines with the word
            int numberInDoc = (line[line.length - 1].split(",")).length;//number of times a word in specific doc
            String doc = oldDictionary.calculateDoc(line);
            int wordsInDoc= oldDictionary.getWordsInDoc().get(doc);
            calculateScore(numOfDocs,numberInDoc,wordsInDoc,)
            scores.put(doc,);
        }

}
