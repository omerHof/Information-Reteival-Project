package Application;

import Query.DominantEntity;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * this class represent the buttons in the menu
 */
public class LandingController implements Initializable {

    @FXML
    private TextField textFieldCorpus;

    @FXML
    private TextField textFieldPosting;

    @FXML
    private TextField textFieldQuery;

    @FXML
    private CheckBox checkBoxSemantic;

    @FXML
    private CheckBox checkBoxStemming;


    final TableView<Pair<String, String>> table = new TableView<>();

    private boolean stemming;
    private boolean semantic;
    private ViewModel viewModel;

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        checkBoxStemming = new CheckBox();
        checkBoxStemming.setSelected(false);
        checkBoxSemantic = new CheckBox();
        checkBoxSemantic.setSelected(false);
        viewModel = new ViewModel();
    }

    /**
     * this function change the stemming value by the user choise
     * @param actionEvent
     * @throws IOException
     */
    public void setStemming(ActionEvent actionEvent) throws IOException {
        if (checkBoxStemming.isSelected()){
            checkBoxStemming.setSelected(false);
            stemming = false;
        }else{
            checkBoxStemming.setSelected(true);
            stemming = true;
        }
    }

    /**
     * this function change the semantic value by the user choise
     * @param actionEvent
     * @throws IOException
     */
    public void setSemantic(ActionEvent actionEvent) throws IOException {
        if (checkBoxSemantic.isSelected()){
            checkBoxSemantic.setSelected(false);
            semantic = false;
        }else{
            checkBoxSemantic.setSelected(true);
            semantic = true;
        }
    }

    /**
     * this function help to search the data folder
     * @param actionEvent
     * @throws IOException
     */
    public void browseCorpusFolderPressed(ActionEvent actionEvent) throws IOException {
        final DirectoryChooser chooser = new DirectoryChooser();
        File file = chooser.showDialog(Main.getStage());

        if (file!=null){
            textFieldCorpus.setText(file.getAbsolutePath());
        }
    }

    /**
     * this function help to search the folder for the posting files
     * @param actionEvent
     * @throws IOException
     */
    public void browsePostingFolderPressed(ActionEvent actionEvent) throws IOException {
        final DirectoryChooser chooser = new DirectoryChooser();
        File file = chooser.showDialog(Main.getStage());

        if (file!=null){
            textFieldPosting.setText(file.getAbsolutePath());
        }
    }

    /**
     * this function help to search the query file
     * @param actionEvent
     * @throws IOException
     */
    public void browseQueryFilePressed(ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(Main.getStage());

        if (selectedFile!=null && !viewModel.validFolder(selectedFile.getAbsolutePath())){
            textFieldQuery.setText(selectedFile.getAbsolutePath());
        }
    }



    /**
     * this function execute the engine and create all the files
     * @param actionEvent
     * @throws IOException
     * @throws InterruptedException
     */
    public void excuteButtonPressed(ActionEvent actionEvent) throws IOException, InterruptedException {
        if (!checkLocation()){
            return;
        }
        if (checkBoxStemming.isSelected()){
            stemming = true;
        }
        else{
            stemming = false;
        }
        long startTime = System.nanoTime();
        //call to viewModel.execute
        viewModel.excute(textFieldCorpus.getText(),stemming, textFieldPosting.getText());
        long endTime = System.nanoTime();
        long timeElapsed = (endTime - startTime)/1000000000;
        int numberOfTerms = viewModel.getUserDictionary().size();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Dictionary created successfully!");
        alert.setContentText("Total indexed documents: "+viewModel.numberOfDocsThatIndexed(textFieldPosting.getText(),stemming)+" documents \r\n"+
        "Total terms in corpus: "+numberOfTerms+ " terms. \r\n"+
        "Total Total processing time: "+timeElapsed+ " seconds.");
        alert.showAndWait();
    }

    /**
     * this function execute the engine and create all the files
     * @param actionEvent
     * @throws IOException
     * @throws InterruptedException
     */
    public void runButtonPressed(ActionEvent actionEvent) throws IOException, InterruptedException {
        if (!checkLocation()){
            return;
        }
        if (checkBoxStemming.isSelected()){
            stemming = true;
        }
        else{
            stemming = false;
        }

        if(checkBoxSemantic.isSelected()){
            semantic = true;
        } else{
            semantic = false;
        }

        viewModel.run(textFieldCorpus.getText(),textFieldPosting.getText(),stemming,textFieldQuery.getText(),semantic);

        ArrayList <Pair<String,String>> results = viewModel.getResults();

        //if there isn't a Dictionary to show -> write a massage

        resultTableShow1(results);


    }

    private void resultTableShow1(ArrayList<Pair<String, String>> results){

        Text txtHeader = new Text("Result set of the query");
        txtHeader.setFont(Font.font(20));
        txtHeader.setFill(Color.GREEN);


        ObservableList<Pair<String, String>> data = FXCollections.observableArrayList(results);


        table.getItems().setAll(data);
        table.setPrefHeight(275);

        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // table definition
        TableColumn<Pair<String, String>, String> nameColumn = new TableColumn<>("Query number");
        nameColumn.setPrefWidth(250);
        TableColumn<Pair<String, String>, String> valueColumn = new TableColumn<>("Document name");
        valueColumn.setSortable(false);
        valueColumn.setPrefWidth(250);
        table.setMaxSize(500, 600);
        table.setMinSize(250, 300);

        nameColumn.setCellValueFactory(new PairKeyFactory());
        valueColumn.setCellValueFactory(new PairValueFactory());
        table.getColumns().setAll(nameColumn, valueColumn);
        valueColumn.setCellFactory(new Callback<TableColumn<Pair<String, String>, String>, TableCell<Pair<String, String>, String>>() {
            @Override
            public TableCell<Pair<String, String>, String> call(TableColumn<Pair<String, String>, String> column) {
                return new PairValueCell();
            }
        });

        // layout the scene.
        final StackPane layout = new StackPane();
        layout.getChildren().setAll(table);




        Button btnAdd = new Button("Export to eval file");
        btnAdd.setMinWidth(85);


        //Add Information to TableView
        btnAdd.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {

                final DirectoryChooser chooser = new DirectoryChooser();
                File file = chooser.showDialog(Main.getStage());
                String fileLocation = "";
                if (file!=null){
                    fileLocation =file.getAbsolutePath();
                }
                try {
                    viewModel.writeResultFile(fileLocation);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("File created successfully!");

                alert.showAndWait();
            }
        });

        Text txtName = new Text("Dominant Entity - please enter document name to get its 5 dominant entity");


        TextField fldName = new TextField();
        Button dominantEntity = new Button("Find Dominant!");
        dominantEntity.setMinWidth(85);

        //Add Information to TableView
        dominantEntity.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {

                ArrayList<String> dominantEntities = viewModel.getEntities(fldName.getText());
                if(dominantEntities ==null){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setHeaderText("An Error Dialog");
                    alert.setContentText("Ooops, we didn't found any doc with the given name");
                    alert.showAndWait();
                }else{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText("We found 5 Dominant Entities for '"+ fldName.getText()+ "' doc: ");
                    alert.setContentText("First entity: "+dominantEntities.get(0) +System.lineSeparator()+
                                        "Second entity: "+ dominantEntities.get(1) +System.lineSeparator()+
                                        "Third entity: "+ dominantEntities.get(2) +System.lineSeparator()+
                                        "Firth entity: "+ dominantEntities.get(3) +System.lineSeparator()+
                                        "Fifth entity: "+ dominantEntities.get(4) +System.lineSeparator()
                    );
                    alert.showAndWait();
                }
            }
        });

        GridPane center = new GridPane();
        center.setHgap(10);
        center.setVgap(10);
        center.setPadding(new Insets(50));

        center.add(txtHeader, 0, 0, 5, 1);
        GridPane.setHalignment(txtHeader, HPos.CENTER);
        center.add(table, 0, 1, 5, 5);

        center.add(txtName, 0, 7);
        center.add(fldName, 0, 8);
        center.add(dominantEntity,0,9);

        VBox right = new VBox(15);
        right.setAlignment(Pos.CENTER);
        right.setPadding(new Insets(5, 100,5,5));

        right.getChildren().addAll(btnAdd);

        BorderPane root = new BorderPane();

        root.setCenter(center);
        root.setRight(right);

        Scene scene = new Scene(root, 800, 600);

        Stage stage = new Stage();
        stage.setTitle("RESULTS");
        stage.setScene(scene);
        Main.setStage(stage);
        stage.show();
    }

    private Pair<String, Object> pair(String name, Object value) {
        return new Pair<>(name, value);
    }

    private void printRow(String item) {
        System.out.println(item);
    }






    /**
     * check if the location is valid and write a massage
     * @return true/false
     */
    private boolean checkLocation() {
        //if the data folder path is invalid
        if (!viewModel.validFolder(textFieldCorpus.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Look, an Error Dialog");
            alert.setContentText("Ooops, there was an error! You didn't enter data folder location");
            alert.showAndWait();
            return false;
        }
        //if the posting folder path is invalid
        if (!viewModel.validFolder(textFieldPosting.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Look, an Error Dialog");
            alert.setContentText("Ooops, there was an error! You didn't enter posting folder location");
            alert.showAndWait();
            return false;
        }
        //the paths are valid
        return true;
    }

    /**
     * this function reset the match folder (Stemming/without Stemming)
     * @param actionEvent
     * @throws IOException
     */
    public void resetButtonPressed(ActionEvent actionEvent) throws IOException {
        if (!checkLocation()){
            return;
        }
        if (viewModel.reset(textFieldPosting.getText(),stemming)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("reset completed successfully!");
            alert.showAndWait();
        } else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Look, an Error Dialog");
            alert.setContentText("Ooops, there is no folder to delete!");
            alert.showAndWait();
        }
    }

    /**
     * this function present the Dictionary by using dictionaryTableShow
     * @param actionEvent
     * @throws IOException
     */
    public void showDictionaryButtonPressed(ActionEvent actionEvent) throws IOException {

        Map <String, Integer> userDictionary;
        userDictionary = viewModel.getUserDictionary();
        //if there isn't a Dictionary to show -> write a massage
        if (userDictionary==null|| userDictionary.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Look, an Error Dialog");
            alert.setContentText("Ooops, there is no dictionary!");
            alert.showAndWait();
        }else{
            dictionaryTableShow(userDictionary);
        }
    }

    /**
     * the actual function who present the Dictionary by using the class DictionaryToShow
     * @param userDictionary
     * @throws IOException
     */
    private void dictionaryTableShow(Map<String, Integer> userDictionary) throws IOException {

        TableColumn<Map.Entry<String,Integer>,String> termColumn = new TableColumn<>("Term");
        TableColumn<Map.Entry<String,Integer>,String> tfCol = new TableColumn<>("Total terms in corpus");

        termColumn.setCellValueFactory(p->new SimpleStringProperty(p.getValue().getKey()));
        tfCol.setCellValueFactory(p->new SimpleStringProperty(String.valueOf(p.getValue().getValue())));

        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("/Application/Dictionary.fxml").openStream());
        TableView<Map.Entry<String,Integer>> entryTableView = ((DictionaryToShow)fxmlLoader.getController()).getTableView();
        entryTableView.setItems(FXCollections.observableArrayList(userDictionary.entrySet()));
        entryTableView.getColumns().setAll(termColumn,tfCol);

        Stage stage = new Stage();
        stage.setTitle("Dictionary");
        stage.setScene(new Scene(root,700,500));
        Main.setStage(stage);
        stage.show();
    }

    /**
     * this function load the Dictionary from the disk to the user
     * @param actionEvent
     * @throws IOException
     */
    public void LoadDicFromDiscButtonPressed(ActionEvent actionEvent) throws IOException {
        if (!checkLocation()){
            return;
        }
        //if the load successful
        if (viewModel.load(textFieldPosting.getText(),stemming)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("load completed successfully!");
            alert.showAndWait();
            //if the load don't successful
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Look, an Error Dialog");
            alert.setContentText("Ooops, there is no dictionary!");
            alert.showAndWait();
        }
    }
}

    class PairKeyFactory implements Callback<TableColumn.CellDataFeatures<Pair<String, String>, String>, ObservableValue<String>> {
        @Override
        public ObservableValue<String> call(TableColumn.CellDataFeatures<Pair<String, String>, String> data) {
            return new ReadOnlyObjectWrapper<>(data.getValue().getKey());
        }
    }

    class PairValueFactory implements Callback<TableColumn.CellDataFeatures<Pair<String, String>, String>, ObservableValue<String>> {
        @SuppressWarnings("unchecked")
        @Override
        public ObservableValue<String> call(TableColumn.CellDataFeatures<Pair<String, String>, String> data) {
            Object value = data.getValue().getValue();
            return (value instanceof ObservableValue)
                    ? (ObservableValue) value
                    : new ReadOnlyObjectWrapper<String>((String) value);
        }
    }

    class PairValueCell extends TableCell<Pair<String, String>, String> {
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (item != null) {
                if (item instanceof String) {
                    setText((String) item);
                    setGraphic(null);
            } else {
                setText(null);
                setGraphic(null);
            }
        }
    }
}