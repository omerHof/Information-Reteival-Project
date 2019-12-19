package Application;


import javafx.scene.control.TableView;

import java.util.Map;

public class DictionaryToShow {
    public TableView<Map.Entry<String,Integer>> viewTableOfDictionary;

    public TableView<Map.Entry<String, Integer>> getTableView() {
        return viewTableOfDictionary;
    }
}
