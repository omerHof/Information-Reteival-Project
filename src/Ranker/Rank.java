package Ranker;


import Application.ViewModel;
import invertedIndex.Dictionary;
import javafx.application.Application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Rank {
    ArrayList<String> words;
    HashMap<String, Double> scores;
    boolean stemming;
    HashMap<String, String> dictionary;
    HashMap<String, Integer> wordsInDoc;

    final double b = 0.75;
    final double k1 = 1.5;
    final double avgDl = 100;//todo need to calculate
    ViewModel viewModel=new ViewModel();


    /**
     * constructor
     *
     * @param words
     * @param stemming
     */
    public Rank(ArrayList<String> words, boolean stemming) {
        this.words = words;
        this.scores = new HashMap<>();
        this.stemming = stemming;
        this.dictionary=new HashMap<>();
        this.wordsInDoc=new HashMap<>();
        initInformation(ViewModel.getPathToOutput());
    }

    private void initInformation(String pathToOutput) {
        if (stemming) {
            String pathDictionary =pathToOutput + "/postingStemming/Dictionary Metadata/dicMetaData.txt";
            String pathWordInDoc =pathToOutput + "/postingStemming/Dictionary Metadata/termsInDoc.txt";
            if (viewModel.validFile(pathDictionary)&&viewModel.validFile(pathWordInDoc)){
                File file = new File(pathDictionary);
                readFileDictionary(file);
                File file2 = new File(pathWordInDoc);
                readFileWordInDoc(file2);
            }
        } else {
            String pathDictionary =pathToOutput + "/postingWithoutStemming/Dictionary Metadata/dicMetaData.txt";
            String pathWordInDoc =pathToOutput + "/postingWithoutStemming/Dictionary Metadata/termsInDoc.txt";
            if (viewModel.validFile(pathDictionary)&&viewModel.validFile(pathWordInDoc)){
                File file = new File(pathDictionary);
                readFileDictionary(file);
                File file2 = new File(pathWordInDoc);
                readFileWordInDoc(file2);
            }
        }
    }

    private void readFileWordInDoc(File file2) {
        String[] term;
        try (BufferedReader reader = new BufferedReader(new FileReader(file2))) {
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                term = line.split(" ");
                this.wordsInDoc.put(term[0],Integer.parseInt(term[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readFileDictionary(File file) {
        String[] term;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                term = line.split(" ");
                String []data = term[1].split("-");
                if(data.length>2) {
                    this.dictionary.put(term[0], data[0] + "-" + data[1] + "-" + data[2]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * wrap function for iterate all words in query
     * @return
     */
    public HashMap<String, Double> rankQuery(){
        for(String word:words){
            mergeAndSort(rankWord(word));
        }
        scores = sortByValue();
        return scores;
    }

    public HashMap<String, Double> rankWord(String word) {
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
        ArrayList<String> posting=readPostingFile(ViewModel.getPathToOutput(),postingFileName);
        String[] line = posting.get(postingLine).split(" ");

        while (postingLine < posting.size()-1) {//iterate all lines with the word
            if(line!=null && calculateWord(line).equals(word)){
                int numberInDoc = (line[line.length - 1].split(",")).length;//number of times a word in specific doc
                String doc = calculateDoc(line);
                int numWordsInDoc = wordsInDoc.get(doc);
                score = calculateScore(numOfDocs, numberInDoc, numWordsInDoc, b, k1, avgDl);
                docScore.put(doc, score);
            }
            postingLine++;
            line = posting.get(postingLine).split(" ");
        }
        return docScore;
    }

    private ArrayList<String> readPostingFile(String text,String postingFileName) {
        if (stemming) {
            String path = text + "/postingStemming/posting/"+postingFileName+".txt";
            if (viewModel.validFile(path)){
                File file = new File(path);
                return readFile(file);
            }
        } else {
            String path =text + "/postingWithoutStemming/posting/"+postingFileName+".txt";
            if (viewModel.validFile(path)){
                File file = new File(path);
                return readFile(file);
            }
        }
        return null;
    }
    /**
     * this function get a file and read the data to the memory
     * @param file
     */
    public ArrayList readFile (File file){
        ArrayList<String>posting=new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                posting.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return posting;
    }

    private double calculateScore(int n, int numberInDoc, int numWordsInDoc, double b, double k1, double avgDl) {
        double idf=(wordsInDoc.size()-n+0.5)/(n+0.5);
        idf=Math.log(idf);
        return idf*((numberInDoc*(k1+1))/numberInDoc+k1*(1-b+b*(numWordsInDoc/avgDl)));
    }

    private void mergeAndSort(HashMap<String, Double> score){
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
