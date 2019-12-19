package Application;


import javafx.scene.control.TableView;

import java.util.Map;

/**
 * this class help to present the Dictionary
 */
public class DictionaryToShow {
    public TableView<Map.Entry<String,Integer>> viewTableOfDictionary;

    public TableView<Map.Entry<String, Integer>> getTableView() {
        return viewTableOfDictionary;
    }
}
