package Application;

import ReadFile.ReadFileJsoupThreads;
import invertedIndex.Dictionary;
import invertedIndex.MergeSorter;
import invertedIndex.SortedTablesThreads;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

        viewModel.excute(textFieldCorpus.getText(),stemming, textFieldPosting.getText());

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

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Dictionary");
        alert.setHeaderText("List of Terms and appearances in the corpus");


// Create expandable Exception.
        StringWriter sw = new StringWriter();
        ArrayList<String> dictionary = viewModel.getUserDictionaryInArray();
        String dictionaryOutput = "";

        /*
        for(Map.Entry<String,Integer> entry : dictionary.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();

            dictionaryOutput += (key + " => " + value) + System.lineSeparator();
        }

         */
        for(String line:dictionary){
            dictionaryOutput += line + System.lineSeparator();
        }

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(dictionaryOutput);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

// Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();


    }


    public void LoadDicFromDiscButtonPressed(ActionEvent actionEvent) throws IOException {
        if (!checkLocation()){
            return;
        }
        viewModel.load(textFieldPosting.getText(),stemming);
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
