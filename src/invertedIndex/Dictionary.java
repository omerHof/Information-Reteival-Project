package invertedIndex;

import Parse.Parser;
import Query.DominantEntity;

import java.io.*;
import java.util.*;

/**
 * this class create a dictionary and split the file to posting files
 */
public class Dictionary {

    File file;
    private HashMap<String, String> dictionary;
    private HashMap<String, Integer> userDictionary;
    private HashMap<String, Integer> wordsInDoc;
    private HashMap<String, Integer> popularWordInDoc;
    String pathForFile;
    String pathForPosting;
    String pathForDicMetadata;

    /**
     * constructor
     *
     * @param file
     */
    public Dictionary(File file, String pathForPosting, String pathForDicMetadata) {
        this.file = file;
        dictionary = new HashMap<>();
        userDictionary = new HashMap<>();
        wordsInDoc = new HashMap<>();
        popularWordInDoc = new HashMap<>();
        this.pathForPosting = pathForPosting;
        this.pathForDicMetadata = pathForDicMetadata;
        //init();
        Parser.getBigWordList();

    }

    public HashMap<String, Integer> getWordsInDoc() {
        return wordsInDoc;
    }

    /**
     * this function reads the file, split to x posting files and creates the dictionary
     *
     * @throws IOException
     */
    public void create() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));

        String[] line;
        int index = 0;
        int counterFile = 0;
        int counterLine = 0;
        int pointerLine = 1;
        int Ndocs = 0;
        int shows = 0;
        int showsInCorpus = 0;
        String previousWord = "";
        String currentWord = "";
        String ln = reader.readLine();
        String doc = "";
        // while the line we read isn't null
        while (ln != null) {
            // if the line is a space, we skip
            while (ln.equals("") || ln.charAt(0) == ' ') {
                ln = reader.readLine();
            }
            //create the posting file
            counterFile++;
            FileWriter pw = new FileWriter(pathForPosting+"/" + counterFile + ".txt", false);
            counterLine = 0;

            // each x lines is one posting file
            while (ln != null && (counterLine < 20000000)) {
                line = ln.split(" ");
                if (line.length < 3) {
                    continue;
                }
                currentWord = calculateWord(line);
                doc = calculateDoc(line);
                if (wordsInDoc.containsKey(doc)) {
                    wordsInDoc.put(doc, wordsInDoc.get(doc) + 1);
                } else {
                    wordsInDoc.put(doc, 1);
                }
                shows = calculateShows(line);
                if (popularWordInDoc.containsKey(doc)) {
                    if (shows > popularWordInDoc.get(doc)) {
                        popularWordInDoc.put(doc, shows);
                    }
                } else {
                    popularWordInDoc.put(doc, shows);
                }

                if (currentWord.equals(previousWord)) {
                    Ndocs++;
                    showsInCorpus += shows;
                } else {
                    //add to dictionary with: number of docs, file name, number of line in the doc

                    if (Parser.getBigWordList().containsKey(previousWord) && Parser.getBigWordList().get(previousWord)== showsInCorpus) {

                        dictionary.put(previousWord.toUpperCase(), Ndocs + "-" + counterFile + "-" + pointerLine);
                        userDictionary.put(previousWord.toUpperCase(), showsInCorpus);
                    } else {
                        dictionary.put(previousWord, Ndocs + "-" + counterFile + "-" + pointerLine);
                        userDictionary.put(previousWord, showsInCorpus);
                    }
                    pointerLine = counterLine;
                    previousWord = currentWord;
                    Ndocs = 1;
                    showsInCorpus = shows;

                }
                //write to the posting file
                pw.write(ln + "\r\n");
                counterLine++;
                ln = reader.readLine();
            }
            pw.close();
            ln = reader.readLine();
            index++;
        }
    }

    /**
     * this function take a line and calculated the number of shows in this doc
     * @param line
     * @return
     */
    private int calculateShows(String[] line) {
        String list = line[line.length - 1];
        String[] locations = list.split(",");
        return locations.length;
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

    /**
     * this function write to the disk information about rhe posting files, terms and docs.
     * @return
     * @throws IOException
     * @param docNum
     * @param totalWordsInDoc
     */
    public TreeMap<String, Integer> saveInformation(HashMap<Integer, String> docNum, HashMap<Integer, Integer> totalWordsInDoc) throws IOException {
        TreeMap<String, Integer> sorted = new TreeMap<>(userDictionary);
        Set<Map.Entry<String, Integer>> mappings = sorted.entrySet();
        TreeMap<String, String> sorted2 = new TreeMap<>(dictionary);
        Set<Map.Entry<String, String>> mappings2 = sorted2.entrySet();

        //create the files of the dictionary for posting and the dictionary of terms-shows in corpus
        FileWriter pw = new FileWriter(pathForDicMetadata+"/dicMetaData.txt", false);
        FileWriter pw1 = new FileWriter(pathForDicMetadata+"/termsInDic.txt", false);
        Iterator it = mappings2.iterator();
        Iterator it1 = mappings.iterator();

        //write the files of the dictionary for posting and the dictionary of terms-shows in corpus
        while (it.hasNext() && it1.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            pw.write(pair.getKey() + " " + pair.getValue() + "\r\n");
            Map.Entry pair1 = (Map.Entry) it1.next();
            pw1.write(pair1.getKey() + "/" + pair1.getValue() + "\r\n");
        }
        pw.close();
        pw1.close();

        TreeMap<String, Integer> sorted3 = new TreeMap<>(popularWordInDoc);
        Set<Map.Entry<String, Integer>> mappings3 = sorted3.entrySet();
        TreeMap<String, Integer> sorted4 = new TreeMap<>(wordsInDoc);
        Set<Map.Entry<String, Integer>> mappings4 = sorted4.entrySet();

        //create the files of the doc-amonunt of the most popular word doc and doc-amount of unique words
        pw = new FileWriter(pathForDicMetadata+"/amountOfPopularInDoc.txt", false);
        pw1 = new FileWriter(pathForDicMetadata+"/termsInDoc.txt", false);
        it = mappings3.iterator();
        it1 = mappings4.iterator();

        FileWriter pw2 = new FileWriter(pathForDicMetadata+"/entities.txt", false);
        HashMap<String, ArrayList<Integer>> entitiesTemp = DominantEntity.getEntities();

        HashMap<String, String> entities = new HashMap<>();
        Set<Map.Entry<String, ArrayList<Integer>>> entitiesToIterateTemp = entitiesTemp.entrySet();
        Iterator it2 = entitiesToIterateTemp.iterator();
        while (it2.hasNext()){
            Map.Entry pair2 = (Map.Entry) it2.next();
            if ( ((ArrayList<Integer>) pair2.getValue()).size()>1){
                ArrayList<Integer> arr = (ArrayList<Integer>) pair2.getValue();
                if(arr!=null && !arr.isEmpty()){
                    entities.put((String) pair2.getKey(),arrayAsString(arr));
                }
            }
        }

        //write the files of the doc-amonunt of the most popular word doc and doc-amount of unique words
        while(it.hasNext() && it1.hasNext()){
            Map.Entry pair = (Map.Entry) it.next();
            pw.write(pair.getKey() + " " + pair.getValue() + "\r\n");
            Map.Entry pair1 = (Map.Entry) it1.next();
            pw1.write(pair1.getKey() + " " + pair1.getValue() + "\r\n");

        }

        pw.close();
        pw1.close();


        Set<Map.Entry<String, String>> entitiesToIterate = entities.entrySet();
        it2 = entitiesToIterate.iterator();

        while(it2.hasNext()){
            Map.Entry entity = (Map.Entry) it2.next();
            pw2.write(entity.getKey()+ "|" +  entity.getValue()+"\r\n");
        }

        pw2.close();

        pw = new FileWriter(pathForDicMetadata+"/docNum.txt", false);
        pw1 = new FileWriter(pathForDicMetadata+"/TotalWordsInDoc.txt", false);
        Set<Map.Entry<Integer,String>> DocNumToIterate = docNum.entrySet();
        Set<Map.Entry<Integer,Integer>> totalWordsInDocIterate = totalWordsInDoc.entrySet();

        it = DocNumToIterate.iterator();
        it1 = totalWordsInDocIterate.iterator();

        while(it.hasNext() && it1.hasNext()){
            Map.Entry pair = (Map.Entry) it.next();
            pw.write(pair.getKey() + " " + pair.getValue() + "\r\n");
            Map.Entry pair1 = (Map.Entry) it1.next();
            pw1.write(pair1.getKey() + " " + pair1.getValue() + "\r\n");

        }

        pw.close();
        pw1.close();


        return sorted;
    }

    private String arrayAsString(ArrayList<Integer> docList) {
        String result = "";
        if(docList==null|| docList.isEmpty()){
            return result;
        }
        for(int i=0;i<docList.size();i++){
            result+=docList.get(i)+",";
        }

        return result.substring(0,result.length()-1);
    }

    public HashMap<String, String> getDictionary() {
        return dictionary;
    }
}