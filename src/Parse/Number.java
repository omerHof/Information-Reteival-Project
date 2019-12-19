package Parse;

import java.util.ArrayList;

/**
 * Number class represent the words that are numbers: check and change
 */
public class Number {
    private ArrayList<String> newWords;

    /**
     * this function check if the word is a number
     * @param word
     * @return true/false
     */
    public boolean check(String word) {
        char firstChar = word.charAt(0);
        word = word.replaceAll(",","");
        if (Character.isDigit(firstChar) && word.matches("[0-9]+|[0-9]++\\b\\.\\b+[0-9]+")) {
            return true;
        }
        return false;
    }

    /**
     * this function change the word to a valid number
     * @param textWords
     * @param index
     * @return string number
     */
    public String change(ArrayList<String> textWords, int index) {
        double doubleNum;
        String word=textWords.get(index);
        word = word.replaceAll(",","");
        doubleNum = Double.valueOf(word);

        if (doubleNum >= 1000000000) {
            doubleNum = doubleNum / 1000000000;
            int intNum = (int)(doubleNum*1000);
            doubleNum=(double) intNum/1000;
            if ((doubleNum % 1) == 0) {
                word = "" + (int) doubleNum + "B";
            } else {
                word = "" + doubleNum + "B";
            }
        } else if (doubleNum >= 1000000) {
            doubleNum = doubleNum / 1000000;
            int intNum = (int)(doubleNum*1000);
            doubleNum=(double) intNum/1000;
            if ((doubleNum % 1) == 0) {
                word = "" + (int) doubleNum + "M";
            } else {
                word = "" + doubleNum + "M";
            }

        } else if (doubleNum >= 1000) {
            doubleNum = doubleNum / 1000;
            int intNum = (int)(doubleNum*1000);
            doubleNum=(double) intNum/1000;
            if ((doubleNum % 1) == 0) {
                word = "" + (int) doubleNum + "K";
            } else {
                word = "" + doubleNum + "K";
            }

        } else {
            double scale = Math.pow(10, 3);
            doubleNum = Math.round(doubleNum * scale) / scale;
            if ((doubleNum % 1) == 0) {
                word = "" + (int) doubleNum;
            } else {
                word = "" + doubleNum;
            }
        }
        return word;
    }
}

