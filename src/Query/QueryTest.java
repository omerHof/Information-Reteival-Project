package Query;

import Application.ViewModel;

public class QueryTest {

    public static void main(String[] args){

        //testForSingleQuery();
        //testForQueriesFile();
        //testForDocNum();
        testForEntity();

    }

    private static void testForEntity() {

        String pathToOutput = "C:\\Users\\ohoff\\Documents\\הנדסת מערכות מידע\\שנה ג'\\סמסטר א\\3-אחזור מידע\\3-עבודות\\מנוע\\part2Output";
        ViewModel viewModel = new ViewModel();
        String pathToData = "C:\\Users\\ohoff\\Documents\\information retrieval\\corpus";
        String query = "C:\\Users\\ohoff\\Documents\\information retrieval\\corpus\\03 queries.txt";
        viewModel.run(pathToData,pathToOutput,true,query,true);
    }

    private static void testForDocNum() {
        docNum docNum = new docNum();
        docNum.initDocNum("C:\\Users\\ohoff\\Documents\\information retrieval\\corpus\\corpus");

    }


    private static void testForSingleQuery() {
        ViewModel.setPathToData("C:\\Users\\ohoff\\Documents\\information retrieval\\corpus");
        String query = "are is the ENTITY yffdhhou workhhhing";
        boolean stemming = false;
        InitQuery test = new InitQuery(query,stemming);
        test.initSearcher();
    }

    private static void testForQueriesFile() {
        ViewModel.setPathToData("C:\\Users\\ohoff\\Documents\\information retrieval\\corpus");
        String query = "C:\\Users\\ohoff\\Documents\\information retrieval\\03 queries.txt";
        boolean stemming = false;
        InitQuery test = new InitQuery(query,stemming);
        test.initSearcher();
    }
}
