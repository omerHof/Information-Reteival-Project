package Parse;

import java.io.IOException;


/**
 * this class is for test the price class.
 */


public class testprice {


    public static void main(String[] args) throws IOException {
        String[] words=new String[10];
        int counter=0;
        price price=new price();

        String word1="26m";
        String word2="Dollars";
        String word3="22";
        String word4="3/4";
        String word5="Dollars";
        String word6="$100B";
        String word7="100bn";
        String word8="Dollars";
        String word9="100B";
        String word10="Dollars";

        words[0]=word1;
        words[1]=word2;
        words[2]=word3;
        words[3]=word4;
        words[4]=word5;
        words[5]=word6;
        words[6]=word7;
        words[7]=word8;
        words[8]=word9;
        words[9]=word10;

        test(words,counter);

    }

    public static void test(String[]words,int counter){
        String result="";
        while (counter<words.length){
            if ((words[counter] != null && words[counter].length() > 0) && (words[counter].charAt(0) == '$' || (counter < words.length - 1 && words[counter + 1].equals("Dollars")))) {
                if (counter < words.length - 1 && words[counter+1] != null &&  words[counter + 1].equals("Dollars")) {
                    //result = price.change(words, counter) + " Dollars";
                    counter++;
                } else {
                    //result = price.change(words, counter);

                }
            }
            else if (counter < words.length - 1 && words[counter+1] != null && words[counter + 1].contains("/")) {
                //result = price.change(words, counter) + " " + words[counter + 1];
                counter++;
                if (counter < words.length - 1 && words[counter + 1].equals("Dollars")) {
                    result = result + " Dollars";
                    counter++;
                }
            }
            System.out.println(result);
            counter++;
        }
    }
}
