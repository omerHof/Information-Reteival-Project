package Parse;

import invertedIndex.Dictionary;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * this class is for test the parser.
 */


public class testsParser {

    public static void main(String[] args) throws IOException {
        int docIndex = 1;
        String pathForStopWords = "C:\\Users\\ohoff\\Documents\\information retrieval\\corpus";
        String sentence1 = "Lorem Ipsum is simply dummy text of the Printing and typesetting industry";
        String sentence2 = "The Mongolian Government and people gave wholehearted support to the " +
                "Vietnamese people in their struggle for national liberation in " +
                "the past as well as in national Vietnamese construction and defence at present";
        String sentence3 = " We also provide-static-- electricity protection for filling and draining devices";
        Parser parser = new Parser(docIndex,sentence1,pathForStopWords,true);
        numberTest(parser);
        entityTest(parser);
        betweenTest(parser);
        dateTest(parser);
        dateChnge(parser);
    }

    private static void dateChnge(Parser parser) {
        String date = "October 15 March 7 8 March May 1994 ";
        date = "15 March";
        date = "May 1994";


        ArrayList<String> myList = new ArrayList<String>(Arrays.asList(date.split(" ")));
        myList = parser.dateChange(myList,0);
        System.out.println((myList.stream().collect(Collectors.joining(" "))));


    }

    private static void dateTest(Parser parser) {
        String month = "([Jj][Aa][Nn](?:[Uu][Aa][Rr][Yy])?|[Ff][Ee][Bb](?:[Rr][Uu][Aa][Rr][Yy])?|[Mm][Aa][Rr](?:[Cc][Hh])?|[Aa][Pp][Rr](?:[Ii][Ll])?|[Mm][Aa][Yy]?|[Jj][Uu][Nn](?:[Ee])?|[Jj][Uu][Ll](?:[Yy])?|[Aa][Uu][Gg](?:[Uu][Ss][Tt])?|[Ss][Ee][Pp](?:[Tt][Ee][Mm][Bb][Ee][Rr])?|[Oo][Cc][Tt](?:[Oo][Bb][Ee][Rr])?|[Nn][Oo][Vv](?:[Ee][Mm][Bb][Ee][Rr])?|[Dd][Ee][Cc](?:[Ee][Mm][Bb][Ee][Rr])?)";
        Pattern p = Pattern.compile("^"+month+"$");
        String months = "January,JANUARY,Jan,February,FEBRUARY,Feb,March,MARCH,Mar,April,APRIL,Apr,May,MAY,June,JUNE,Jun,July,JULY,Jul,August,AUGUST,Aug,September,SEPTEMBER,Sep,October,OCTOBER,Oct,November,NOVEMBER,Nov,December,DECEMBER,Dec" ;
        String[] inputstring = months.split(",");
        for (int i=0;i<35;i++){
            Matcher m = p.matcher(inputstring[i]);
            if(m.find()){
                date date = new date();
            }
        }

    }


    private static void entityTest(Parser parser) throws IOException {
        String sentence1 = "Lorem Ipsum is simply dummy text of the Printing and typesetting industry";
        String sentence2 = "The Mongolian Government and people gave wholehearted support to the " +
                            "Vietnamese people in their struggle for national liberation in " +
                                "the past as well as in national Vietnamese construction and defence at present";
        String sentence3 = " We also provide-static-- electricity protection for filling and draining devices";



    }

    private static void numberTest(Parser parser) throws IOException {

    }

    private static void betweenTest(Parser parser) throws IOException {
        String sentence1 = "between 1 10 not Between 1 0 or between omer and israel simply dummy text of the printing and typesetting industry";
        ArrayList<String> myList = new ArrayList<String>(Arrays.asList(sentence1.split(" ")));
        //parser.wordIsBetween(myList);

    }

    private static void generalTest(File input, Dictionary dic) throws IOException {
        Document html = Jsoup.parse(input,"UTF-8","");
        Elements elements=  html.getElementsByTag("TEXT");
        String pathForStopWords = "C:\\Users\\ohoff\\Documents\\information retrieval\\corpus";
        int counter = 1;
        for (Element element : elements) {
            System.out.println(counter);
            Parser parser1 = new Parser(counter,element.text(),pathForStopWords,true);
            parser1.parse();
            counter++;
        }


    }
}
