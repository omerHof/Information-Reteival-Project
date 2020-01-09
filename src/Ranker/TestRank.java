package Ranker;

import Application.ViewModel;
import Query.initPartB;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class TestRank {
    public static void main(String[] args) throws IOException {
        ViewModel.setPathToOutput("C:\\Users\\yszok\\Desktop\\temp\\posting folder");
        ArrayList<Integer> result=new ArrayList<>();
        HashMap<Integer, Integer> length=new HashMap<>();
        ArrayList<String> test1= new ArrayList<>();
        ViewModel viewModel=new ViewModel();
        viewModel.functionsPartB("C:\\Users\\yszok\\Desktop\\temp\\data\\corpus","C:\\Users\\yszok\\Desktop\\temp\\posting folder",false);
        length=initPartB.getTotalWordsInDoc();
        test1.add("politicians");
        //test1.add("world");
        //for(int i=0; i<1000000;i++){
        //    length.put(i,i);
        //}
        Rank rank=new Rank(test1,false,length);
        result=rank.rankQuery();
        System.out.println("finish ranking");
        for (int i = 0; i < result.size(); i++)
            System.out.println("place "+i+" is:"+result.get(i));
    }
}
