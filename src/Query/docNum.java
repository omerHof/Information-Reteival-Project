package Query;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class docNum {

    private HashMap<Integer,String> docNum;

    public HashMap<Integer, String> getDocNum() {
        return docNum;
    }

    public void initDocNum(String pathToData){
        docNum = new HashMap<>();
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
            Elements elements = html.getElementsByTag("DOCNO");
            for(Element element: elements){
                docNum.put(docIndexer,element.text());
                docIndexer++;
                System.out.println(docIndexer);
            }

        }
        System.out.println("done");

    }
}
