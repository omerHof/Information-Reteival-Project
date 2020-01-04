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

/**
 * this class read files from a directory, split the words, give the words a document number and send them to parse
 */
public class ReadFileJsoup extends Thread {
    private File file;
    private int indexDoc;
    private String path;
    private boolean stemming;

    /**
     * constructor
     * @param file
     * @param indexDoc
     * @param path
     * @param stemming
     */
    public ReadFileJsoup(File file, int indexDoc, String path, boolean stemming){
        this.file = file;
        this.indexDoc = indexDoc;
        this.path = path;
        this.stemming = stemming;
    }

    /**
     * this function read files and send them to parse by threads
     */
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
                        parser = new Parser(indexDoc,element.getElementsByTag("TEXT").text(),path,stemming,false);
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