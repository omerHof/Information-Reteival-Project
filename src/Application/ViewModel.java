package Application;

import ReadFile.ReadFileJsoupThreads;
import invertedIndex.Dictionary;
import invertedIndex.MergeSorter;
import invertedIndex.SortedTables;
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

    public ViewModel() {
        this.userDictionary = new TreeMap<>();
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
        SortedTablesThreads.setTableNum(0);
    }

    public TreeMap<String, Integer> getUserDictionary() {
        return userDictionary;
    }

    public boolean load(String text, boolean stemming) {
        if (stemming) {
            String path = text + "/postingStemming/Dictionary Metadata/termsInDic.txt";
            if (validFolder(path)){
                File file = new File(path);
                readFile(file);
            }
        } else {
            String path =text + "/postingWithoutStemming/Dictionary Metadata/termsInDic.txt";
            if (validFolder(path)){
                File file = new File(path);
                readFile(file);
            }
        }
        return (!this.userDictionary.isEmpty());
    }

    public int numberOfDocsThatIndexed(String text, boolean stemming){
        if (stemming) {
            File file = new File(text + "/postingStemming/Dictionary Metadata/termsInDoc.txt");
            return checkDocIndexed(file);
        } else {
            File file = new File(text + "/postingWithoutStemming/Dictionary Metadata/termsInDoc.txt");
            return checkDocIndexed(file);
        }
    }

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
                //this.userDictionaryInArray.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean validFolder(String folderLocation) {
        File f = new File(folderLocation);
        if (f.exists() && f.isDirectory()) {
            return true;
        }
        return false;
    }
}

