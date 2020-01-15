package Query;
import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import com.medallia.word2vec.Searcher;
import com.medallia.word2vec.Word2VecModel;
import org.json.*;
public class Semantic {

    public Semantic() {
        try {
            InputStream in = Semantic.class.getResourceAsStream("word2vec.txt");
            BufferedReader bf = new BufferedReader(new InputStreamReader(in));
            File file = new File(System.getProperty("user.dir") + "\\word2vec.txt");
            OutputStream outputStream = new FileOutputStream(file);
            String line = "";


            if (file.exists()) {
                file.delete();
            }

            while ((line = bf.readLine()) != null) {
                outputStream.write((line + "\n").getBytes());
            }
            in.close();
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    /**
     * use open source- give a word and get K similar words
     * @param word
     * @return
     */
    public static String[] getSemanticWords(String word){
        int k=5;
        String[] result= new String[k];
        try{
            File file2 = new File(System.getProperty("user.dir")+"\\word2vec.txt");

            Word2VecModel model = Word2VecModel.fromTextFile(file2);

            com.medallia.word2vec.Searcher semanticSearcher = model.forSearch();
            int numOfResultInList = k;

            List<com.medallia.word2vec.Searcher.Match> matches = semanticSearcher.getMatches(word, numOfResultInList);
            int i=0;
            for(com.medallia.word2vec.Searcher.Match match : matches){
                result[i]=match.match();
                i++;

            }
            return result;
        }catch (Searcher.UnknownWordException | IOException e){

        }
        return result;
    }

    /**
     * use open source- give a word and get K similar words
     * @param word
     * @return
     */
    /*
    public static String[] getSemanticWords(String word) {
    String stringWord = "";
    String[] result;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(word);
    String[] splitWord = word.split(" ");
    for (int i = 0; i < splitWord.length; i++) {
        if (i == splitWord.length - 1) {
            stringWord = splitWord[i];
        } else {
            stringWord = splitWord[i] + "+";
        }
    }

    try {
        URL adress = new URL("https://api.datamuse.com/words?ml="+stringWord);
        Scanner scanner = new Scanner(adress.openStream());
        StringBuilder sBuilder = new StringBuilder(("{\"result\":"));
        while (scanner.hasNext()) {
            sBuilder.append(scanner.nextLine());
        }
        scanner.close();
        sBuilder.append("}");
        JSONObject jsonObject = new JSONObject(sBuilder.toString());
        JSONArray objects = jsonObject.getJSONArray("result");
        int i = 0;
        for (Object o : objects) {
            if (i == 3) {
                break;
            }
            JSONObject data = (JSONObject) o;
            stringBuilder.append(" ").append(data.getString("word"));
            i++;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    result=stringBuilder.toString().split(" ");
    return result;
}
*/

}