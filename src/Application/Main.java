package Application;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private static Stage primaryStage;



    public static void setStage(Stage stage) {
        Main.primaryStage = stage;
    }

    public static Stage getStage() {
        return primaryStage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        setStage(primaryStage);
        FXMLLoader fxmlLoader = new FXMLLoader();


        primaryStage.setTitle("INFORMATION RETRIEVAL PROJECT 2019");
        Parent root = fxmlLoader.load(getClass().getResource("/Application/Landing.fxml").openStream());
        Scene scene = new Scene(root, 856, 532);
        primaryStage.setScene(scene);
        LandingController lc = fxmlLoader.getController();

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
