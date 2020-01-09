package Query;

import javafx.util.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * help class that get a doc and return it index or its length
 */

public class docNum {

    private HashMap<Integer,String> docNum;

    private HashMap<Integer,Integer> totalWordsInDoc;

    public HashMap<Integer, String> getDocNum() {
        return docNum;
    }

    public HashMap<Integer, Integer> getTotalWordsInDoc() {
        return totalWordsInDoc;
    }

    public void initDocNum(String pathToData){
        docNum = new HashMap<>();
        totalWordsInDoc = new HashMap<>();
        int docIndexer=1;
        File[] folders = new File(pathToData).listFiles();;
        for (File file : folders) {
            String doc = null;
            try{
                doc = new String(Files.readAllBytes(Paths.get(file.getPath() + "\\" + file.getName())));
            } catch (IOException e) {
                e.printStackTrace();
            }

            Document html = Jsoup.parse(doc);
            Elements elements = html.getElementsByTag("DOC");
            for(Element element:elements){
                Elements docNumber = element.getElementsByTag("DOCNO");
                Elements docText = element.getElementsByTag("TEXT");
                docNum.put(docIndexer,docNumber.first().text());
                if(docText.first()!=null){
                    totalWordsInDoc.put(docIndexer,getNumberWordsInDoc(docText.first().text()));
                }

                docIndexer++;
                System.out.println(docIndexer);
            }
        }
        System.out.println("done");

    }

    private int getNumberWordsInDoc(String doc){
        String[] allWords = doc.split(" ");
        return allWords.length;
    }
}
