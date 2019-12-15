package Application;

import ReadFile.ReadFileJsoupThreads;
import invertedIndex.Dictionary;
import invertedIndex.MergeSorter;
import invertedIndex.SortedTablesThreads;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeMap;

public class ViewModel {

    boolean stemming;
    TreeMap<String, Integer> userDictionary;
    ArrayList<String> userDictionaryInArray;

    public ViewModel() {
        this.userDictionary = new TreeMap<>();
        this.userDictionaryInArray = new ArrayList<>();
    }

    public void excute(String input, boolean stemming, String output) throws IOException, InterruptedException {
        this.stemming = stemming;
        ReadFileJsoupThreads readFileJsoupThreads = new ReadFileJsoupThreads(input, stemming, output);
        String pathForPosting = readFileJsoupThreads.splitToDocs();
        createIndex(pathForPosting);
    }

    public void reset(String postingPath) throws IOException {
        userDictionary.clear();
        Path path = Paths.get(postingPath);
        Files.walk(path)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);

    }


    public void createIndex(String pathForPosting) throws IOException {
        String pathForPrePosting = "";
        if (stemming) {
            pathForPrePosting = "stemming";
        } else {
            pathForPrePosting = "withoutStemming";
        }
        SortedTablesThreads sortedTablesThreads = new SortedTablesThreads(stemming);
        MergeSorter merge = new MergeSorter(1, pathForPrePosting);

        System.out.println("finish parser!----------------");
        sortedTablesThreads.entityToSortedTable();

        System.out.println("finish entity!----------------");
        sortedTablesThreads.addLastTable();

        System.out.println("finish last table!----------------");


        File folder = new File(pathForPrePosting);
        File[] listOfFiles = folder.listFiles();

        merge.startMergingfiles(listOfFiles.length);
        System.out.println("finish merge!----------------");
        listOfFiles = folder.listFiles();
        String pathForDicPosting = pathForPosting + "\\posting";
        String pathForDicMetadata = pathForPosting + "\\Dictionary Metadata";
        Dictionary dictionary = new Dictionary(listOfFiles[0], pathForDicPosting, pathForDicMetadata);
        dictionary.create();
        this.userDictionary = dictionary.saveInformation();
    }

    public TreeMap<String, Integer> getUserDictionary() {
        return userDictionary;
    }

    public void load(String text, boolean stemming) {
        if (stemming) {
            File file = new File(text + "/postingStemming/Dictionary Metadata/termsInDic.txt");
            readFile(file);
        } else {
            File file = new File(text + "/postingWithoutStemming/Dictionary Metadata/termsInDic.txt");
            readFile(file);
        }
    }

    public ArrayList<String> getUserDictionaryInArray () {
        return userDictionaryInArray;
    }

    public void readFile (File file){
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                this.userDictionaryInArray.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

