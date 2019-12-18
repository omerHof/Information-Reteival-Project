package Application;


import javafx.scene.control.TableView;

import java.util.Map;

public class DictionaryTable {
    public TableView<Map.Entry<String,Integer>> tableView;

    public TableView<Map.Entry<String, Integer>> getTableView() {
        return tableView;
    }
}
