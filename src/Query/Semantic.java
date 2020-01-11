package Query;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import com.medallia.word2vec.Word2VecModel;
import org.json.*;
public class Semantic {

    /**
     * use open source- give a word and get K similar words
     * @param word
     * @return
     */
    public static String[] getSemanticWords(String word){
        int k=5;
        String[] result= new String[k];
        try{
            Word2VecModel model = Word2VecModel.fromTextFile(new File("word2vec.c.output.model.txt"));
            com.medallia.word2vec.Searcher semanticSearcher = model.forSearch();
            int numOfResultInList = k;

            List<com.medallia.word2vec.Searcher.Match> matches = semanticSearcher.getMatches(word, numOfResultInList);
            int i=0;
            for(com.medallia.word2vec.Searcher.Match match : matches){
                result[i]=match.match();
                i++;

            }



        }catch (IOException e){
            e.printStackTrace();
        }catch (com.medallia.word2vec.Searcher.UnknownWordException e){

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