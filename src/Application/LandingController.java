package Application;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

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