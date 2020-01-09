package Query;

import java.io.IOException;

public class SemanticTest {
    public static void main(String[] args) throws IOException {
        String s = "world";
        Semantic semantic = new Semantic();
        String []result=semantic.getSemanticWords(s);
        for(int i=0; i<result.length; i++){
            System.out.println(result[i]);
        }
    }
}
