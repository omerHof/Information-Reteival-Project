package Query;
import java.net.URL;
import java.util.Scanner;
import org.json.*;
public class Semantic {

    /**
     * use open source- give a word and get K similar words
     * @param word
     * @return
     */
    public String[] getSemanticWords(String word) {
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
        StringBuilder sBuilder = new StringBuilder(("{\"result\":"));
        Scanner scanner = new Scanner(adress.openStream());
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

}