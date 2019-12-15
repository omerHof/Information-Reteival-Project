package Parse;

import java.io.IOException;

/**
 * this class is for test the stemmer.
 */

public class testStemmer {

    public static void main(String[] args) throws IOException {
        String[] words=new String[1];
        words[0]="placement";
        char[] charAray=words[0].toCharArray();
        Stemmer stemmer=new Stemmer();
        stemmer.add(charAray,words[0].length());
        stemmer.stem();
        System.out.println(stemmer.toString());
        stemmer.execute(words);
    }
}

