package ReadFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * this class read files from a directory, split the words, give the words a document number and send them to parse
 */
public class ReadFile implements ReadFileMethods{
    /**
     * variables
     */
    private File[] folders;
    private int indexDoc = 1;
    private String line;
    private String path;
    private final String textTag = "<TEXT>";
    private final String closingTextTag = "</TEXT>";

    /**
     * constructor
     * @param path
     */
    public ReadFile(String path) {
        this.folders = new File(path+"\\corpus").listFiles();
        this.path=path;
        File f = new File("//posting");
        f.mkdir();
    }

    /**
     * this function takes the files from the folder and split them to lines and send each line to the split lines function
     * @throws IOException
     */
    public String splitToDocs() throws IOException {
        for (File file : folders) {
            if (file.isDirectory()) {
                String doc = new String(Files.readAllBytes(Paths.get(file.getPath() + "\\" + file.getName())));
                BufferedReader bufReader = new BufferedReader(new StringReader(doc));
                while ((line = bufReader.readLine()) != null) {
                    while (streamNotReachTag(textTag)) {
                        line = bufReader.readLine();
                    }
                    if (line != null && line.equals(textTag)) {
                        line = bufReader.readLine();
                    }
                    String text = "";
                    while (streamNotReachTag(closingTextTag)) {
                        while (line.equals("")) {
                            line = bufReader.readLine();
                        }

                        if (streamNotReachTag(closingTextTag)) {
                            text += line;
                            line = bufReader.readLine();
                        }
                    }
                    if (line != null) {
                        indexDoc++;
                        System.out.println(indexDoc);
                    }
                    //file ends or arrive at </text>
                }
            }
        }
        return "";
    }

    private boolean streamNotReachTag(String tag){
        if (line != null && !line.equals(tag)){
            return true;
        }
        return false;
    }
}