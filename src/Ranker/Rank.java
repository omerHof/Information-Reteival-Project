package Ranker;


import Application.ViewModel;
import Query.initPartB;
import invertedIndex.Dictionary;
import javafx.application.Application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Rank {
    ArrayList<String> words;
    ArrayList<String> additionalWords;
    ArrayList<String> description;
    HashMap<String, Double> scores;
    ArrayList<Integer> result;
    boolean stemming;
    HashMap<String, String> dictionary;
    //HashMap<String, Integer> wordsInDoc;
    HashMap<Integer,Integer> docsLength;
    HashMap<Integer,Integer> popularwWord;

    /**
     * Parameters
     */
    final double b = 0.1;
    final double k1 = 1.5;
    double avgDl;
    ViewModel viewModel=new ViewModel();


    /**
     * constructor
     *
     * @param words
     * @param stemming
     */
    public Rank(ArrayList<String> words,ArrayList<String> words2,ArrayList<String> words3, boolean stemming) {
        this.words = words;
        this.additionalWords=words2;
        this.description=words3;
        this.scores = new HashMap<>();
        this.stemming = stemming;
        this.dictionary= initPartB.getDictionary();
        //this.wordsInDoc=new HashMap<>();
        this.popularwWord=initPartB.getPopularwWord();
        this.docsLength=initPartB.getTotalWordsInDoc();
        this.result=new ArrayList<>();
        this.avgDl=caculateAvarageLength(docsLength);
    }

    /**
     * caculate Avarage Length of documents
     * @param docsLength
     * @return
     */
    private double caculateAvarageLength(HashMap<Integer, Integer> docsLength) {
        int sum=0;
        int mone=0;
        Iterator it = docsLength.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            sum+=(int)pair.getValue();
            mone++;
        }
        if(mone>0){
            return sum/mone;
        }
        return 0;
    }

    /**
     * wrap function for iterate all words in query and sort
     * @return
     */
    public ArrayList<Integer> rankQuery(){
        int constant=1;
        for(String word:words){
            merge(rankWord(word,constant));
        }
        for(String word:additionalWords){
            merge(rankWord(word,constant/3));
        }
        for(String word:description){
            merge(rankWord(word,constant/3));
        }
        scores = sortByValue();
        result=prepareBestResult(scores);
        return result;
    }

    /**
     * take the best 50 result to arrayList
     * @param scores
     * @return
     */
    private ArrayList<Integer> prepareBestResult(HashMap<String, Double> scores) {
        ArrayList<Integer> result=new ArrayList<>();
        int counter=0;
        Iterator it = scores.entrySet().iterator();
        while (it.hasNext()&&counter<50) {
        //while (it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            int doc=Integer.parseInt((String)pair.getKey());
            result.add(doc);
            counter++;
        }
        return result;
    }

    /**
     * rank documents by a word
     * @param word
     * @return
     */
    public HashMap<String, Double> rankWord(String word,double constant) {
        double score;
        HashMap<String, Double> docScore=new HashMap<>();
        String tempValue=dictionary.get(word);
        if(tempValue==null){
            return null;
        }
        String[] values = tempValue.split("-");
        int numOfDocs = Integer.parseInt(values[0]);
        String postingFileName = values[1];
        int postingLine = Integer.parseInt(values[2]);
        ArrayList<String> posting=readPostingFile(ViewModel.getPathToOutput(),postingFileName,numOfDocs,postingLine);
        for(int i=0; i<posting.size(); i++) {
            String[] line = posting.get(i).split(" ");
            int numberInDoc = (line[line.length - 1].split(",")).length;//number of times a word in specific doc
            int firstLocation = Integer.parseInt(line[line.length - 1].split(",")[0]);
            String doc = calculateDoc(line);
            int popularDoc = popularwWord.get(Integer.parseInt(doc));
            int numWordsInDoc = docsLength.get(Integer.parseInt(doc));
            score = calculateScore(numOfDocs, numberInDoc, popularDoc, numWordsInDoc, firstLocation, b, k1, avgDl, constant);
            docScore.put(doc, score);
        }
        return docScore;
    }

    /**
     * help to rankWord to rank the docs
     * @param text
     * @param postingFileName
     * @return
     */
    private ArrayList<String> readPostingFile(String text,String postingFileName, int numOfDocs,int postingLine) {
        if (stemming) {
            String path = text + "/postingStemming/posting/"+postingFileName+".txt";
            if (viewModel.validFile(path)){
                File file = new File(path);
                return readFile(file,numOfDocs,postingLine);
            }
        } else {
            String path =text + "/postingWithoutStemming/posting/"+postingFileName+".txt";
            if (viewModel.validFile(path)){
                File file = new File(path);
                return readFile(file,numOfDocs,postingLine);
            }
        }
        return null;
    }
    /**
     * this function get a file and read the data to the memory
     * @param file
     */
    public ArrayList readFile (File file, int numOfDocs,int postingLine) {
        ArrayList<String> posting = new ArrayList<>();
        String line=null;
        int counter = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (counter < postingLine + numOfDocs) {
                while (counter < postingLine) {
                    line = reader.readLine();
                    counter++;
                }
                if (line == null) {
                    break;
                }
                line = reader.readLine();
                if (line == null) {
                    break;
                }
                posting.add(line);
                counter++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return posting;
    }

    /**
     * the equation of BM25
     * @param n
     * @param numberInDoc
     * @param numWordsInDoc
     * @param b
     * @param k1
     * @param avgDl
     * @return
     */
    private double calculateScore(int n, int numberInDoc,int popular, int numWordsInDoc,int firstLocation, double b, double k1, double avgDl,double constant) {
        double idf=(docsLength.size()-n+0.5)/(n+0.5);
        idf=Math.log(idf);
        return constant*idf*((numberInDoc*(k1+1)/*/popular*/)/((numberInDoc/*/popular*/)+k1*(1-b+b*(numWordsInDoc/avgDl))));
    }

    /**
     * this function merge the score of the new doc to previous docs
     * @param score
     */
    private void merge(HashMap<String, Double> score){
        if(score==null){
            return;
        }
        if(scores.isEmpty()){
            scores=score;
        }
        else{
            Iterator it = score.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                if(scores.containsKey(pair.getKey())){
                    scores.put((String)pair.getKey(),scores.get(pair.getKey())+(double)pair.getValue());
                }
                else{
                    scores.put((String)pair.getKey(),(double)pair.getValue());
                }
            }
        }
    }

    /**
     * sort the scores by the grades
     * @return
     */
    public HashMap<String, Double> sortByValue()
    {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Double> > list =
                new LinkedList<Map.Entry<String, Double> >(scores.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Double> >() {
            public int compare(Map.Entry<String, Double> o1,
                               Map.Entry<String, Double> o2)
            {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<String, Double> temp = new LinkedHashMap<String, Double>();
        for (Map.Entry<String, Double> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    /**
     *  this function take a line and find the doc name
     * @param line
     * @return
     */
    public String calculateDoc(String[] line) {
        if (line.length > 3) {
            return line[line.length - 2];
        }
        return line[1];
    }

    /**
     * this function get a split line and find the word inside
     *
     * @param line
     * @return the word
     */
    public String calculateWord(String[] line) {
        if (line.length > 3) {
            String ans = line[0];
            for (int i = 1; i < line.length - 2; i++) {
                ans = ans + " " + line[i];
            }
            return ans;
        } else {
            return line[0];
        }
    }
}
