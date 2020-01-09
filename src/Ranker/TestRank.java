package Ranker;

import Application.ViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class TestRank {
    public static void main(String[] args) throws IOException {
        ViewModel.setPathToOutput("C:\\Users\\yszok\\Desktop\\temp\\posting folder");
        HashMap<String, Double> result=new HashMap<>();
        ArrayList<String> test1= new ArrayList<>();
        test1.add("bad");
        test1.add("peoplet");
        test1.add("world");
        Rank rank=new Rank(test1,true);
        result=rank.rankQuery();
        System.out.println();
    }
}
