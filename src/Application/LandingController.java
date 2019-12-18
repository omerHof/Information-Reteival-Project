package Application;

import ReadFile.ReadFileJsoupThreads;
import invertedIndex.Dictionary;
import invertedIndex.MergeSorter;
import invertedIndex.SortedTablesThreads;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;


public class LandingController extends Controller implements Initializable {


    @FXML
    private TextField textFieldCorpus;

    @FXML
    private TextField textFieldPosting;

    @FXML
    private CheckBox checkBoxStemming;

    @FXML
    private  TableView<Map.Entry<String, Integer>> DictionaryTable;

    private boolean stemming;
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
        viewModel = new ViewModel();
        DictionaryTable = new TableView<>();
    }
    public void setStemming(ActionEvent actionEvent) throws IOException {
        if (checkBoxStemming.isSelected()){
            checkBoxStemming.setSelected(false);
            stemming = false;
        }else{
            checkBoxStemming.setSelected(true);
            stemming = true;
        }
    }


    public void browseCorpusFolderPressed(ActionEvent actionEvent) throws IOException {
        final DirectoryChooser chooser = new DirectoryChooser();
        File file = chooser.showDialog(Main.getStage());

        if (file!=null){
            textFieldCorpus.setText(file.getAbsolutePath());
        }


    }

    public void browsePostingFolderPressed(ActionEvent actionEvent) throws IOException {
        final DirectoryChooser chooser = new DirectoryChooser();
        File file = chooser.showDialog(Main.getStage());

        if (file!=null){
            textFieldPosting.setText(file.getAbsolutePath());
        }

    }

    public void excuteButtonPressed(ActionEvent actionEvent) throws IOException, InterruptedException {

        if (!checkLocation()){
            return;
        }




        if (checkBoxStemming.isSelected()){
            stemming = true;
        }else{
            stemming = false;
        }
        long startTime = System.nanoTime();
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

    private boolean checkLocation() {
        if (!validFolder(textFieldCorpus.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Look, an Error Dialog");
            alert.setContentText("Ooops, there was an error! You didn't enter data folder location");
            alert.showAndWait();
            return false;
        }
        if (!validFolder(textFieldPosting.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Look, an Error Dialog");
            alert.setContentText("Ooops, there was an error! You didn't enter data folder location");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    private boolean validFolder(String folderLocation) {
        File f = new File(folderLocation);
        if (f.exists() && f.isDirectory()) {
            return true;
        }
        return false;
    }

    public void resetButtonPressed(ActionEvent actionEvent) throws IOException {
        if (!checkLocation()){
            return;
        }
        viewModel.reset(textFieldPosting.getText());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("reset completed successfully!");
        alert.showAndWait();


    }

    public void showDictionaryButtonPressed(ActionEvent actionEvent) throws IOException {

        Map <String, Integer> userDictionary;

        userDictionary = viewModel.getUserDictionary();
        if (userDictionary==null|| userDictionary.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Look, an Error Dialog");
            alert.setContentText("Ooops, there is no dictionary!");
            alert.showAndWait();
        }else{
            showDictionary(userDictionary);
        }
    }

    private void showDictionary(Map<String, Integer> userDictionary) throws IOException {
        TableColumn<Map.Entry<String,Integer>,String> termColumn = new TableColumn<>("Term");
        termColumn.setCellValueFactory(p->new SimpleStringProperty(p.getValue().getKey()));

        TableColumn<Map.Entry<String,Integer>,String> tfCol = new TableColumn<>("Total terms in corpus");
        tfCol.setCellValueFactory(p->new SimpleStringProperty(String.valueOf(p.getValue().getValue())));

        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("/Application/Dictionary.fxml").openStream());
        TableView<Map.Entry<String,Integer>> tableView = ((DictionaryTable)fxmlLoader.getController()).getTableView();
        tableView.setItems(FXCollections.observableArrayList(userDictionary.entrySet()));
        tableView.getColumns().setAll(termColumn,tfCol);

        Stage stage = new Stage();
        stage.setTitle("Dictionary");
        stage.setScene(new Scene(root,700,500));
        Main.setStage(stage);
        stage.show();
    }


    public void LoadDicFromDiscButtonPressed(ActionEvent actionEvent) throws IOException {
        if (!checkLocation()){
            return;
        }

        if (viewModel.load(textFieldPosting.getText(),stemming)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("load completed successfully!");
            alert.showAndWait();
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Look, an Error Dialog");
            alert.setContentText("Ooops, there is no dictionary!");
            alert.showAndWait();
        }

    }

    protected void closeProgram() {
        Stage s = Main.getStage();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wan't to exit the program?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("Exit the program");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            s.close();
        }
    }
}
