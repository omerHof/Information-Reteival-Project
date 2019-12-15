package ReadFile;

import Parse.Parser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class magicThreads extends Thread {
    File file;
    int indexDoc;
    String path;
    boolean stemming;

    public magicThreads(File file, int indexDoc,String path,boolean stemming){
        this.file = file;
        this.indexDoc = indexDoc;
        this.path = path;
        this.stemming = stemming;
    }

    public void run(){
        if (file.isDirectory()) {
            String doc = null;
            try {
                doc = new String(Files.readAllBytes(Paths.get(file.getPath() + "\\" + file.getName())));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Document html = Jsoup.parse(doc);
            Elements elements=  html.getElementsByTag("DOC");



            for (Element element : elements) {
                if(element.getElementsByTag("TEXT").text().equals("")){
                    indexDoc++;
                    System.out.println(indexDoc);
                }else{
                    Parser parser = null;
                    try {
                        parser = new Parser(indexDoc,element.getElementsByTag("TEXT").text(),path,stemming);
                        parser.parse();
                        System.out.println(indexDoc);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    indexDoc++;
                }
            }
        }
    }
}
