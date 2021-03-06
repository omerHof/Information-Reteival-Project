package Query;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class initPartB {

    boolean stemming;
    private static HashMap<String,String> dictionary = new HashMap<>();

    private static HashMap<Integer,String> docNum = new HashMap<>();

    private static HashMap<Integer,Integer> totalWordsInDoc = new HashMap<>();

    private static HashMap<Integer,Integer> popularwWord = new HashMap<>();

    private static HashMap<String, ArrayList<Integer>> entities = new HashMap<>();


    //load dictionary from memory

    //load

    public initPartB(String pathToData, String pathToOutput, boolean stemming){
        this.stemming = stemming;
        initInformation(pathToOutput);
        //getDocNumInformation(pathToData);
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
            String pathDictionary =pathToOutput + "\\postingStemming\\Dictionary Metadata\\dicMetaData.txt";
            //String pathWordInDoc =pathToOutput + "/postingStemming/Dictionary Metadata/termsInDoc.txt";
            String pathPopular =pathToOutput + "\\postingStemming\\Dictionary Metadata\\amountOfPopularInDoc.txt";
            String pathEntites =pathToOutput + "\\postingStemming\\Dictionary Metadata\\entities.txt";
            String pathDocNum = pathToOutput+ "\\postingStemming\\Dictionary Metadata\\docNum.txt";
            String pathTotalWords = pathToOutput+"\\postingStemming\\Dictionary Metadata\\TotalWordsInDoc.txt";

            if (validFile(pathDictionary)&& validFile(pathPopular)){
                File file = new File(pathDictionary);
                readFileDictionary(file);
                File file2 = new File(pathPopular);
                readFilePopular(file2);
                File file3 = new File(pathEntites);
                readFileEntities(file3);
                File file4 = new File(pathDocNum);
                readFileDucNum(file4);
                File file5 = new File(pathTotalWords);
                readFileTotalWords(file5);
            }
        } else {
            String pathDictionary =pathToOutput + "/postingWithoutStemming/Dictionary Metadata/dicMetaData.txt";
            //String pathWordInDoc =pathToOutput + "/postingWithoutStemming/Dictionary Metadata/termsInDoc.txt";
            String pathPopular =pathToOutput + "/postingWithoutStemming/Dictionary Metadata/amountOfPopularInDoc.txt";
            String pathEntites =pathToOutput + "/postingWithoutStemming/Dictionary Metadata/entities.txt";
            String pathDocNum = pathToOutput+ "/postingWithoutStemming/Dictionary Metadata/docNum.txt";
            String pathTotalWords = pathToOutput+"/postingWithoutStemming/Dictionary Metadata/TotalWordsInDoc.txt";
            if (validFile(pathDictionary)&& validFile(pathPopular)){
                File file = new File(pathDictionary);
                readFileDictionary(file);
                File file2 = new File(pathPopular);
                readFilePopular(file2);
                File file3 = new File(pathEntites);
                readFileEntities(file3);
                File file4 = new File(pathDocNum);
                readFileDucNum(file4);
                File file5 = new File(pathTotalWords);
                readFileTotalWords(file5);


            }
        }

    }



    /**
     * read the file word in doc to HashMap
     * @param file2
     */
    private void readFilePopular(File file2) {
        String[] term;
        int lineNumber = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file2))) {
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                term = line.split(" ");
                lineNumber++;


                if(term[0].equals("null")|| term[1].equals("null")){
                    break;
                }
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



    private void readFileEntities(File file3) {
        String[] term;

        try (BufferedReader reader = new BufferedReader(new FileReader(file3))) {
            while (true) {
                ArrayList<Integer> entitiesInDoc = new ArrayList<>();
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                term = line.split("\\|");

                String[] docEntities = term[1].split(",");
                for(String str: docEntities){
                    if(!str.equals("null")){
                        entitiesInDoc.add(Integer.parseInt(str));
                    }

                }
                this.entities.put(term[0],entitiesInDoc);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * read the file word in doc to HashMap
     * @param file4
     */
    private void readFileDucNum(File file4) {
        String[] term;
        int lineNumber = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file4))) {
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                term = line.split(" ");
                lineNumber++;


                if (term[0].equals("null") || term[1].equals("null")) {
                    break;
                }
                this.docNum.put(Integer.parseInt(term[0]), term[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * read the file word in doc to HashMap
     * @param file5
     */
    private void readFileTotalWords(File file5) {
        String[] term;
        int lineNumber = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file5))) {
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                term = line.split(" ");
                lineNumber++;


                if(term[0].equals("null")|| term[1].equals("null")){
                    break;
                }
                this.totalWordsInDoc.put(Integer.parseInt(term[0]),Integer.parseInt(term[1]));
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

    public static int getKeyByValue(String value) {
        for (Map.Entry<java.lang.Integer, java.lang.String> entry : docNum.entrySet()) {
            if (value.equals(entry.getValue())) {
                return (Integer) entry.getKey();
            }
        }
        return 0;
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

    public static HashMap<String, ArrayList<Integer>> getEntities() {
        return entities;
    }
}
