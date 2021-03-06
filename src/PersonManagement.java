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
    private static Pane root = new Pane();
    private TabPane tabRoot;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Personal Info Hub");
        PersonManagement.primaryStage = stage;
        PersonManagement.primaryStage.setResizable(true); // TODO for testing

        root = new MainPage(primaryStage);

        root.prefWidthProperty().bind(primaryStage.widthProperty());
        root.prefHeightProperty().bind(primaryStage.heightProperty());

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setWidth(1900);
        primaryStage.setHeight(600);
        primaryStage.setAlwaysOnTop(false);
        primaryStage.show();

    }

    public static void main(String[] args) {launch(args);}
}
