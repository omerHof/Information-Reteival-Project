package ReadFile;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.util.concurrent.Executors.newFixedThreadPool;

/**
 * this class creates folders for posting and creates the threads to read the files
 */
public class InitProgram extends Thread implements ReadFileMethods {

    /**
     * variables
     */
    private File[] folders;
    private int indexDoc = 1;
    private String pathForData;
    private String pathForPosting;
    private String pathForPrePosting;
    private String pathForDicMetadata;
    private String pathToReturn;

    private ArrayList<Integer> docIndexer;
    ArrayList<ReadFileJsoup> threadList = new ArrayList<>();
    boolean stemming;

    /**
     * constructor
     *
     * @param pathForData
     */
    public InitProgram(String pathForData, boolean stemming, String pathForPosting) throws IOException {
        this.folders = new File(pathForData + "\\test3").listFiles();
        this.pathForData = pathForData;
        this.pathForPosting = pathForPosting;
        this.stemming = stemming;
        deleteFolders();
        createFolders();
        this.docIndexer = new ArrayList<>();
        this.threadList = new ArrayList<>();
    }

    /**
     * this function delete the folders that located in the location of the new folders using deleteFolderHelper
     * @throws IOException
     */
    private void deleteFolders() throws IOException {
        if (stemming) {
            deleteFolderHelper("Stemming");
            deleteFolderHelper(this.pathForPosting + "\\postingStemming");
        } else {
            deleteFolderHelper("withoutStemming");
            deleteFolderHelper(this.pathForPosting + "\\postingwithoutStemming");
        }
    }

    /**
     * this function get a path of folder and delete
     * @param pathToDelete
     * @throws IOException
     */
    private void deleteFolderHelper(String pathToDelete) throws IOException {
        if (validFolder(pathToDelete)){
            Path path = Paths.get(pathToDelete);
            Files.walk(path)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }

    /**
     * this function create the folder for posting by the user's choice.
     * @throws IOException
     */
    private void createFolders() throws IOException {
        File f = new File(pathForData);
        f.mkdir();
        if (stemming){
            File f1 = new File("stemming");
            f1.mkdir();
            pathForPrePosting = "Stemming";
            createFolderHelper();
        } else {
            File f1 = new File("withoutStemming");
            f1.mkdir();
            pathForPrePosting = "withoutStemming";
            createFolderHelper();
        }
    }

    /**
     * this function help to create the folders
     */
    private void createFolderHelper(){
        File f2 = new File(pathForPosting+"\\posting"+pathForPrePosting);
        pathForPosting+="\\posting"+pathForPrePosting;
        f2.mkdir();
        File f3 = new File(pathForPosting+"\\Dictionary Metadata");
        f3.mkdir();
        this.pathForDicMetadata = pathForPosting+"\\Dictionary Metadata";
        File f4 = new File(pathForPosting+"\\posting");
        this.pathToReturn =pathForPosting;
        pathForPosting = pathForPosting+"\\posting";

        f4.mkdir();
    }

    /**
     * this function takes the files from the folder and split them docs, then send only the text part to parse class
     *
     * @throws IOException
     */
    public String splitToDocs() throws IOException, InterruptedException {
        docIndexer.add(1);
        for (File file : folders) {

            String doc = null;
            doc = new String(Files.readAllBytes(Paths.get(file.getPath() + "\\" + file.getName())));
            Document html = Jsoup.parse(doc);
            Elements elements = html.getElementsByTag("DOC");
            int docNumber = docIndexer.get(docIndexer.size() - 1) + elements.size();
            docIndexer.add(docNumber);
        }
        ExecutorService threadPool = newFixedThreadPool(8);
        List<Callable<Object>> todo = new ArrayList<Callable<Object>>();

        int currentIndexDoc = 0;
        for (File file : folders) {
            todo.add(Executors.callable(new ReadFileJsoup(file, docIndexer.get(currentIndexDoc), pathForData, stemming)));
            currentIndexDoc++;
        }
        List<Future<Object>> answers = threadPool.invokeAll(todo);
        threadPool.shutdown();
        return pathToReturn;
    }

    /**
     * this function check if a folder exist by a string path
     * @param folderLocation
     * @return true/false
     */
    private boolean validFolder(String folderLocation) {
        File f = new File(folderLocation);
        if (f.exists() && f.isDirectory()) {
            return true;
        }
        return false;
    }
}
