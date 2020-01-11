package Query;

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
    private static int numberOfQueries;


    public InitQuery(String query, boolean stemming) {
        this.query = query;
        this.stemming = stemming;

    }

    /**
     * this method check if the user send a file or a single query, after the check sent to the relevant method.
     */
    public void initSearcher() {
        File queries = new File(query);
        if (queries.exists()) {
            //query is a file
            separateToQueries(queries);
        } else {
            search(query);
        }
    }


    private void separateToQueries(File query) {
        //open the file and separate the queries
        String queryWords = "";
        try {
            queryWords = new String(Files.readAllBytes(Paths.get(query.getPath())));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Document html = Jsoup.parse(queryWords);
        Elements elements = html.getElementsByTag("num");
        this.numberOfQueries = elements.size();

        ExecutorService threadPool = newFixedThreadPool(6);
        List<Callable<Object>> todo = new ArrayList<Callable<Object>>();


        for (Element element : elements) {
            String queryToSend = element.getElementsByTag("title").text();
            String queryNumber = element.childNode(0).toString();
            queryNumber = getNumbersFromQuery(queryNumber);
            todo.add(Executors.callable(new Searcher(queryToSend, queryNumber, stemming)));
            System.out.println(queryNumber);
        }
        try {
            List<Future<Object>> answers = threadPool.invokeAll(todo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        threadPool.shutdown();
    }

    private void search(String query) {
        this.numberOfQueries =1;
        Searcher searcher = new Searcher(query, "111", stemming);
        //excute parser and stuff...

    }

    private String getNumbersFromQuery(String queryNumber) {
        String ans = "";
        for (int i = 0; i < queryNumber.length(); i++) {
            if (queryNumber.charAt(i) > 47 && queryNumber.charAt(i) < 58) {
                ans += queryNumber.charAt(i);
            }
        }
        return ans;
    }

    public static int getNumberOfQueries() {
        return numberOfQueries;
    }
}
