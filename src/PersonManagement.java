import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

public class PersonManagement extends Application {

    /**
     * Use this to manipulate the Stage title
     */
    private static Stage primaryStage;
    /**
     * Containers for contents holding
     */
    private Pane root = new Pane();
    private TabPane tabRoot;


    @Override
    public void start(Stage stage) throws Exception {
        PersonManagement.primaryStage = stage;

        root = new MainPage(primaryStage);

        root.prefWidthProperty().bind(primaryStage.widthProperty());
        root.prefHeightProperty().bind(primaryStage.heightProperty());

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setWidth(1900);
        primaryStage.setHeight(1000);
        primaryStage.setAlwaysOnTop(false);
        primaryStage.show();

    }

    public static void main(String[] args) {launch(args);}
}
