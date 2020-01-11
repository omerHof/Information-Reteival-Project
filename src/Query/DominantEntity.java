package Query;
import Application.ViewModel;
import Parse.Parser;

import java.io.IOException;
import java.util.*;

/**
 * this class is help class to get 5 dominant entity from a given document.
 */

public class DominantEntity {
        private static DominantEntity instance;
        private static HashMap<String, ArrayList<Integer>> entities;

        private DominantEntity(HashMap<String, ArrayList<Integer>> entities) {
            this.entities = entities;
        }

    /**
     * the list of entity is single instance
     * @param entities
     * @return
     */
    public static DominantEntity getEntityListInstance(HashMap<String, ArrayList<Integer>> entities){
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

    /**
     * main method - get document and return 5 dominant entity from it.
     * @param doc
     * @param stemming
     * @return
     */
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

    /**
     * help function to main method, sort the list of entities by number of occurrences.
     * @param hm
     * @return
     */

        public static String[] sortByValue(HashMap<String, Integer> hm)
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
            return (String[]) temp.keySet().toArray();
        }

    /**
     * main method - get document and return 5 dominant entity from it.

     * @return
     */
    public static ArrayList<String> getDominantEntities(int docIndex){
        ArrayList<String> dominateWordsToReturn = new ArrayList<>();
        HashMap<String,Integer> tempEntities = new HashMap<>();

        //sort tempMap
        Iterator it = entities.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            int val = ((ArrayList<String>) pair.getValue()).size();
            String key = (String) pair.getKey();
            tempEntities.put(key,val);
        }

       String[] sortedEntities = sortByValue(tempEntities);
        //itrate over the
        for()
    }

}
