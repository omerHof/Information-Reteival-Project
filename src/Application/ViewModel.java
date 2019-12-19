package Application;

import ReadFile.ReadFileJsoupThreads;
import invertedIndex.Dictionary;
import invertedIndex.MergeSorter;
import invertedIndex.SortedTables;
import invertedIndex.SortedTablesThreads;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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

    public boolean validFile(String folderLocation) {
        File f = new File(folderLocation);
        if (f.exists()) {
            return true;
        }
        return false;
    }

    public void sortByValue() throws IOException {

        List<Map.Entry<String, Integer> > list =
                new LinkedList<Map.Entry<String, Integer> >(userDictionary.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2)
            {
                return (o2.getValue()).compareTo(o1.getValue());
            }
    });
        FileWriter pw = new FileWriter("wordsAllWords.txt", false);
        Iterator it = list.iterator();
        while (it.hasNext()){
            Map.Entry pair = (Map.Entry) it.next();
            pw.write( pair.getKey() +"-"+pair.getValue() + "\r\n");

        }
        pw.close();
        System.out.println("finish!!");

}
}

