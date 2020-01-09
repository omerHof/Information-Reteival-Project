package Query;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class initPartB {

    boolean stemming;
    private static HashMap<String,String> dictionary;

    private static HashMap<Integer,String> docNum;

    private static HashMap<Integer,Integer> totalWordsInDoc;

    private static HashMap<Integer,Integer> popularwWord;


    //load dictionary from memory

    //load

    public initPartB(String pathToData, String pathToOutput, boolean stemming){
        this.stemming = stemming;
        initInformation(pathToOutput);
        getDocNumInformation(pathToData);
    }


    public void getDocNumInformation(String pathToData){
        docNum docNum = new docNum();
        docNum.initDocNum(pathToData);
        this.docNum = docNum.getDocNum();
        this.totalWordsInDoc = docNum.getTotalWordsInDoc();
    }

    /**
     * read the meta data from the disk (stemming/without) using readFileDictionary and readFileWordInDoc
     * @param pathToOutput
     */
    private void initInformation(String pathToOutput) {
        if (stemming) {
            String pathDictionary =pathToOutput + "/postingStemming/Dictionary Metadata/dicMetaData.txt";
            //String pathWordInDoc =pathToOutput + "/postingStemming/Dictionary Metadata/termsInDoc.txt";
            String pathPopular =pathToOutput + "/postingStemming/Dictionary Metadata/amountOfPopularInDoc.txt";
            if (validFile(pathDictionary)&& validFile(pathPopular)){
                File file = new File(pathDictionary);
                readFileDictionary(file);
                File file2 = new File(pathPopular);
                readFilePopular(file2);
            }
        } else {
            String pathDictionary =pathToOutput + "/postingWithoutStemming/Dictionary Metadata/dicMetaData.txt";
            //String pathWordInDoc =pathToOutput + "/postingWithoutStemming/Dictionary Metadata/termsInDoc.txt";
            String pathPopular =pathToOutput + "/postingStemming/Dictionary Metadata/amountOfPopularInDoc.txt";
            if (validFile(pathDictionary)&& validFile(pathPopular)){
                File file = new File(pathDictionary);
                readFileDictionary(file);
                File file2 = new File(pathPopular);
                readFilePopular(file2);
            }
        }
    }

    /**
     * read the file word in doc to HashMap
     * @param file2
     */
    private void readFilePopular(File file2) {
        String[] term;
        try (BufferedReader reader = new BufferedReader(new FileReader(file2))) {
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                term = line.split(" ");
                this.popularwWord.put(Integer.parseInt(term[0]),Integer.parseInt(term[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * read the file dictionary to HashMap
     * @param file
     */
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

    public static HashMap<String, String> getDictionary() {
        return dictionary;
    }

    public static HashMap<Integer, String> getDocNum() {
        return docNum;
    }

    public static HashMap<Integer, Integer> getTotalWordsInDoc() {
        return totalWordsInDoc;
    }

    public static HashMap<Integer, Integer> getPopularwWord() {
        return popularwWord;
    }
}
