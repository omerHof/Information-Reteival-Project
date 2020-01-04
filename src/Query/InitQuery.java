package Query;

import ReadFile.ReadFileJsoup;
import Searcher.Searcher;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.util.concurrent.Executors.newFixedThreadPool;

/**
 * this class will get query string or query file and sent it to searcher.
 */

public class InitQuery {

    private String query;
    private boolean stemming;

    public InitQuery(String query, boolean stemming) {
        this.query = query;
        this.stemming = stemming;
    }

    /**
     * this method check if the user send a file or a single query, after the check sent to the relevant method.
     */
    public void initSearcher(){
        File queries = new File(query);
        if (queries.exists()){
            //query is a file
            separateToQueries(queries);
        }else{
            search(query);
        }
    }



    private void separateToQueries(File query)  {
        //open the file and separate the queries
        String queryWords= "";
        try{
            queryWords = new String(Files.readAllBytes(Paths.get(query.getPath())));
        }  catch (IOException e) {
            e.printStackTrace();
        }

        Document html = Jsoup.parse(queryWords);
        Elements elements = html.getElementsByTag("num");

        ExecutorService threadPool = newFixedThreadPool(6);
        List<Callable<Object>> todo = new ArrayList<Callable<Object>>();


        for (Element element : elements) {
            String queryToSend = element.getElementsByTag("title").text();
            todo.add(Executors.callable(new Searcher(queryToSend, stemming)));
            //System.out.println(element.text());
        }
        try {
            List<Future<Object>> answers = threadPool.invokeAll(todo);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        threadPool.shutdown();
    }

    private void search(String query) {
        Searcher searcher = new Searcher(query,stemming);
        //excute parser and stuff...

    }
}
