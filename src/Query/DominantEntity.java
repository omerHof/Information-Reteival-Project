package Query;
import Application.ViewModel;
import Parse.Parser;

import java.io.IOException;
import java.util.*;


public class DominantEntity {
        private static DominantEntity instance;
        private static HashMap<String, ArrayList<Integer>> entities;
        private DominantEntity(HashMap<String, ArrayList<Integer>> entities) {
            this.entities = entities;
        }


    public static DominantEntity getInstanceUsingDoubleLocking(HashMap<String, ArrayList<Integer>> entities){
        if(instance == null){
            synchronized (DominantEntity.class) {
                if(instance == null){
                    instance = new DominantEntity(entities);
                }
            }
        }
        return instance;
    }

    public static HashMap<String, ArrayList<Integer>> getEntities() {
        return entities;
    }

    public static ArrayList<String> getDominantEntities(String doc, boolean stemming){
        ArrayList<String> dominateWordsToReturn = new ArrayList<>();
        try{
            Parser parser = new Parser(0,doc, ViewModel.getPathToData(),stemming,true);
            parser.parse();
            ArrayList<String> queryEntity = parser.getQueryEntity();
            HashMap<String,Integer> tempMap = new HashMap<>();
            for(String entity: queryEntity){
                if (entities.containsKey(entity)){
                    int size = entities.get(entity).size();
                    tempMap.put(entity,size);
                }
            }

            //sort tempMap
            HashMap<String, Integer> sorted = sortByValue(tempMap);
            ArrayList<String> dominateWords = (ArrayList<String>) sorted.keySet();

            for(int i=0;i<5;i++){
                if(dominateWords.get(i)!=null){
                    dominateWordsToReturn.add(dominateWords.get(i));
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return dominateWordsToReturn;
    }

        public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm)
        {
            // Create a list from elements of HashMap
            List<Map.Entry<String, Integer> > list =
                    new LinkedList<Map.Entry<String, Integer> >(hm.entrySet());

            // Sort the list
            Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
                public int compare(Map.Entry<String, Integer> o1,
                                   Map.Entry<String, Integer> o2)
                {
                    return (o1.getValue()).compareTo(o2.getValue());
                }
            });

            // put data from sorted list to hashmap
            HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
            for (Map.Entry<String, Integer> aa : list) {
                temp.put(aa.getKey(), aa.getValue());
            }
            return temp;
        }
}
