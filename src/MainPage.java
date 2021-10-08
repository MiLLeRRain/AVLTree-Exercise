import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * A tabPane, main interface for user,
 * Should contain function buttons: Load(load from file), CleanPane,
 * Choose index pattern(either by first name or surname),
 * Edit the PersonNode(MenuButton) and the Person(MenuItem), Add new Person, Remove old Person,
 * Search for Person(1. search the PersonNode, by name or surname, 2. search the person with id),
 *
 * TODO Animation of the tree re-balancing?
 */
public class MainPage extends Pane {

    /**
     * Main structure to hold all data
     */
    private AVLTreeV1 pTree;

    private Stage primaryStage;

    private VBox mainContainer;

    private AnchorPane graphicsContainer;

    private HBox menu;

    private int uid = 80000;

    private final int BUTTON_WIDTH = 80;
    /**
     * A toggle to switch index pattern, by first name or surname
     */
    private boolean surname;
    private HBox loadingPattern;
    /**
     * Button to destroy and clean
     */
    private Button clean;
    /**
     * Button to reload
     */
    private Button reload;


    public MainPage(Stage stage) {
        pTree = new AVLTreeV1();
        primaryStage = stage;
        initUI();
    }



    private void initUI() {
        mainContainer = new VBox();
        mainContainer.prefWidthProperty().bind(this.widthProperty());
        mainContainer.prefHeightProperty().bind(this.heightProperty());

        graphicsContainer = new AnchorPane();
        graphicsContainer.prefHeightProperty().bind(mainContainer.heightProperty());
        graphicsContainer.prefWidthProperty().bind(mainContainer.widthProperty());
        graphicsContainer.setStyle("-fx-background-color: #FFCCBC");

        menu = setMenu();

        mainContainer.getChildren().addAll(graphicsContainer, menu);

        this.getChildren().add(mainContainer);
    }

    private HBox setMenu() {
        HBox toReturn = new HBox();
        toReturn.setPadding(new Insets(10));
// TODO style change later      toReturn.setPrefHeight(100);
        toReturn.prefWidthProperty().bind(mainContainer.widthProperty());
        toReturn.setStyle("-fx-background-color: #E0F7FA");


        loadingPattern = loadingZone();

        ButtonBar bb = new ButtonBar();

        bb.getButtons().addAll();

        toReturn.getChildren().addAll(loadingPattern, bb);

        return toReturn;
    }

    private HBox loadingZone() {
        HBox toReturn = new HBox();
        surname = true;
        toReturn.setAlignment(Pos.CENTER);
        toReturn.setSpacing(10);
        Label modeLb = new Label("Index by:");
        RadioButton fnRB = new RadioButton("First Name");
        RadioButton snRB = new RadioButton("Surname");
        snRB.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                surname = true;
            }
        });
        fnRB.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                surname = false;
            }
        });
        ToggleGroup tg = new ToggleGroup();
        snRB.setToggleGroup(tg); fnRB.setToggleGroup(tg);
        tg.selectToggle(snRB);

        Button loadbtn = new Button("Load");
        loadbtn.setPrefWidth(BUTTON_WIDTH);
        loadbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    loadUpFile();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        toReturn.getChildren().addAll(modeLb, snRB, fnRB, loadbtn);

        return toReturn;
    }

    private void loadUpFile() throws FileNotFoundException {
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose a file to open");
        File file = fc.showOpenDialog(primaryStage);
//        File file = new File("mswdev.csv");
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            String fn = sc.next();
            String sn = sc.next();
            sc.nextLine();
            Person toInsert = new Person(fn, sn, ++uid);
            if (surname) pTree.insert(sn, toInsert);
            else pTree.insert(fn, toInsert);
        }
        pTree.inOrder();
        sc.close();
    }

    private void loadSurnameTree(File file) {

    }

    private void loadNameTree(File file) {

    }


}
