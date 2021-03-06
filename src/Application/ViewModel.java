package Application;

import Parse.Parser;
import Query.DominantEntity;
import Query.InitQuery;
import Query.Semantic;
import Query.initPartB;
import ReadFile.InitProgram;
import Searcher.Results;
import invertedIndex.Dictionary;
import invertedIndex.MergeSorter;
import invertedIndex.SortedTablesThreads;
import javafx.scene.control.Alert;
import javafx.util.Pair;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * this class connect between LandingController and the engine
 */
public class ViewModel {

    private boolean stemming;
    private static boolean semantic;

    private TreeMap<String, Integer> userDictionary;
    private static String pathToData;
    private static String pathToOutput;
    private static String query;
    private static ArrayList <Pair<String,String>> results;


    public ViewModel() {
        this.userDictionary = new TreeMap<>();
    }

    /**
     * this function starting the engine by calling to initProgram and create the index
     * @param input
     * @param stemming
     * @param output
     * @throws IOException
     * @throws InterruptedException
     */
    public void excute(String input, boolean stemming, String output) throws IOException, InterruptedException {
        this.stemming = stemming;
        this.pathToData = input;
        this.pathToOutput = output;
        InitProgram initProgram = new InitProgram(input, stemming, output);
        String pathForPosting = initProgram.splitToDocs();
        createIndex(pathForPosting,initProgram);
    }

    /**
     * this function reset the mach folder by the user's choice
     * @param postingPath
     * @param stemming
     * @return
     * @throws IOException
     */
    public boolean reset(String postingPath,boolean stemming) throws IOException {
        this.stemming = stemming;
        userDictionary.clear();
        Path path;
        String location;
        if (stemming){
            path = Paths.get(postingPath+"/postingStemming");
            location = postingPath+"/postingStemming";
        }else {
            path = Paths.get(postingPath+"/postingwithoutStemming");
            location = postingPath+"/postingwithoutStemming";
        }
        if (validFolder(location)){
            Files.walk(path)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
            return true;
        }else{
            return false;
        }
    }

    /**
     * this function help to execute to create the index
     * @param pathForPosting
     * @param initProgram
     * @throws IOException
     */
    public void createIndex(String pathForPosting, InitProgram initProgram) throws IOException {
        String pathForPrePosting = "";
        if (stemming) {
            pathForPrePosting = "stemming";
        } else {
            pathForPrePosting = "withoutStemming";
        }
        SortedTablesThreads sortedTablesThreads = new SortedTablesThreads(stemming);
        MergeSorter merge = new MergeSorter(1, pathForPrePosting);

        System.out.println("finish parser!----------------");
        DominantEntity.getEntityListInstance(Parser.getEntities());

        sortedTablesThreads.entityToSortedTable();

        System.out.println("finish entity!----------------");
        sortedTablesThreads.addLastTable();

        System.out.println("finish last table!----------------");

        File folder = new File(pathForPrePosting);
        File[] listOfFiles = folder.listFiles();
        System.out.println("start merge!----------------");
        merge.startMergingfiles(listOfFiles.length);
        System.out.println("finish merge!----------------");

        //create the folders of posting and information
        listOfFiles = folder.listFiles();
        String pathForDicPosting = pathForPosting + "\\posting";
        String pathForDicMetadata = pathForPosting + "\\Dictionary Metadata";
        Dictionary dictionary = new Dictionary(listOfFiles[0], pathForDicPosting, pathForDicMetadata);
        dictionary.create();
        this.userDictionary = dictionary.saveInformation(initProgram.getDocNum(),initProgram.getTotalWordsInDoc());
        SortedTablesThreads.setTableNum(0);
        String query = "people";
        //functionsPartB(pathToData,pathForDicMetadata,stemming,query);
    }

    public void run(String pathToOutput, boolean stemming, String queryString, boolean semantic){
        System.out.println("Start initialize part B");;
        this.semantic = semantic;
        this.stemming = stemming;
        this.pathToOutput = pathToOutput;
        this.query = queryString;

        setPathToOutput(pathToOutput);
        String pathToData1 = pathToData+"\\corpus";
        initPartB init = new initPartB(pathToData1,pathToOutput,stemming);
        DominantEntity dominantEntity= DominantEntity.getEntityListInstance(initPartB.getEntities());
        if(semantic){
            Semantic semantic1 = new Semantic();
        }
        InitQuery query = new InitQuery(queryString,stemming);
        query.initSearcher();

        Results results1 = Results.getResultsInstance();
        results =  results1.getResultHashMap();





    }

    public TreeMap<String, Integer> getUserDictionary() {
        return userDictionary;
    }

    /**
     * load the Dictionary from the disk to the memory
     * @param text
     * @param stemming
     * @return
     */
    public boolean load(String text, boolean stemming) {
        this.stemming = stemming;
        if (stemming) {
            String path = text + "/postingStemming/Dictionary Metadata/termsInDic.txt";
            if (validFile(path)){
                File file = new File(path);
                readFile(file);
            }
        } else {
            String path =text + "/postingWithoutStemming/Dictionary Metadata/termsInDic.txt";
            if (validFile(path)){
                File file = new File(path);
                readFile(file);
            }
        }
        return (!this.userDictionary.isEmpty());
    }

    /**
     * this function calculated the number of docs that indexed
     * @param text
     * @param stemming
     * @return
     */
    public int numberOfDocsThatIndexed(String text, boolean stemming){
        if (stemming) {
            File file = new File(text + "/postingStemming/Dictionary Metadata/termsInDoc.txt");
            return checkDocIndexed(file);
        } else {
            File file = new File(text + "/postingWithoutStemming/Dictionary Metadata/termsInDoc.txt");
            return checkDocIndexed(file);
        }
    }

    /**
     * this function count the lines in a file
     * @param file
     * @return
     */
    private int checkDocIndexed(File file) {
        int numOfdocs = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                numOfdocs++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return numOfdocs;
    }

    /**
     * this function get a file and read the data to the memory
     * @param file
     */
    public void readFile (File file){
        String[] term = new String[2];
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                term = line.split("\\/");
                int appears = Integer.parseInt(term[1]);
                this.userDictionary.put(term[0],appears);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * this function check if a string of path of folder is exist and valid
     * @param folderLocation
     * @return
     */
    public boolean validFolder(String folderLocation) {

        File f = new File(folderLocation);
        if (f.exists() && f.isDirectory()) {
            return true;
        }
        return false;
    }

    /**
     * this function check if a string of path of folder is exist and valid
     * @param folderLocation
     * @return
     */
    public boolean validFolderForQueryOutput(String folderLocation, boolean stemming) {
        this.stemming = stemming;
        if(stemming){
            folderLocation = folderLocation+"\\postingStemming";
        }else{
            folderLocation = folderLocation+"\\postingwithoutStemming";
        }
        File f = new File(folderLocation);
        if (f.exists() && f.isDirectory()) {
            return true;
        }
        return false;
    }

    /**
     * this function check if a string of path of file is exist and valid
     * @param folderLocation
     * @return
     */
    public boolean validFile(String folderLocation) {
        File f = new File(folderLocation);
        if (f.exists()) {
            return true;
        }
        return false;
    }

    public static String getPathToData() {
        return pathToData;
    }

    public static void setPathToData(String pathToData) {
        ViewModel.pathToData = pathToData;
    }

    public static String getPathToOutput() {
        return pathToOutput;
    }

    public static void setPathToOutput(String pathToOutput) {
        ViewModel.pathToOutput = pathToOutput;
    }

    public static boolean isSemantic() {
        return semantic;
    }

    public static ArrayList <Pair<String,String>> getResults() {
        return results;
    }


    public void writeResultFile(String fileLocation) throws IOException {

        FileWriter writer = new FileWriter(new File(fileLocation +"//results.txt"));
        ArrayList<String> writeResult = new ArrayList<>();
        float flo = 42;
        for (Pair<String,String> pair : results) {
            writeResult.add(pair.getKey() + " 0 "+ pair.getValue()+ " 0 "+ flo+ " mt" );
        }

        for (String str : writeResult) {
            writer.write(str + System.lineSeparator());
        }
        writer.close();
    }

    public ArrayList<String> getEntities(String text) {
        if (text==null){
            return null;
        }
        int docIndex = initPartB.getKeyByValue(text);
        if(docIndex!=0){
            return DominantEntity.getDominantEntities(docIndex);

        }else{

        }
        return null;
    }
}