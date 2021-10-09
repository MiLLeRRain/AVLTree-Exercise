import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * A tabPane, main interface for user,
 * Should contain function buttons: Load(load from file), CleanPane,
 * Choose index pattern(either by first name or surname),
 * Edit the PersonNode(MenuButton) and the Person(MenuItem), Add new Person, Remove old Person,
 * Search for Person(1. search the PersonNode, by name or surname, 2. search the person with id),
 * <p>
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

    private static int uid = 80000;

    private final int BUTTON_WIDTH = 80;

    private static final double COLUMN_OFFSET = 45;

    private static final double ROW_OFFSET = 17;

    private static final double ROW_GAP = 60;

    private static double midX = 900;

    private HBox loadingPattern;
    /**
     * Booleans
     */
    private boolean surname;
    private boolean loaded;

    /**
     * Buttons
     */
    private Button loadBtn;
    private Button displayBtn;
    private Button insertBtn;
    private Button cleanBtn;

    /**
     * Button to reload
     */
    private Button reload;



    public MainPage(Stage stage) {
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
        graphicsContainer.setStyle("-fx-background-color: #E3F2FD");

        menu = setMenu();

        mainContainer.getChildren().addAll(graphicsContainer, menu);

        this.getChildren().add(mainContainer);
    }

    private HBox setMenu() {
        HBox toReturn = new HBox();
        toReturn.setPadding(new Insets(10));
// TODO style change later      toReturn.setPrefHeight(100);
        toReturn.prefWidthProperty().bind(mainContainer.widthProperty());
        toReturn.setStyle("-fx-background-color: #FFFDE7");


        loadingPattern = loadingZone();

        ButtonBar bb = buttonsLoad();

        toReturn.getChildren().addAll(loadingPattern, bb);

        return toReturn;
    }

    /**
     * Place to hold buttons with useful features
     *
     * @return
     */
    private ButtonBar buttonsLoad() {
        ButtonBar toReturn = new ButtonBar();

        setupLoadBtn();
        setupDisplayBtn();
        setupInsertBtn();
        setupCleanBtn();

        toReturn.getButtons().addAll(loadBtn, displayBtn, insertBtn, cleanBtn);
        return toReturn;
    }

    private void setupLoadBtn() {
        loadBtn = new Button("Load");
        loadBtn.setPrefWidth(BUTTON_WIDTH);
        loadBtn.setTooltip(new Tooltip("Load from file"));
        loadBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    loaded = true;
                    loadUpFile();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setupDisplayBtn() {
        displayBtn = new Button("Display");
        displayBtn.setPrefWidth(BUTTON_WIDTH);
        displayBtn.setTooltip(new Tooltip("Display sample plot"));
        displayBtn.setOnAction(a -> {
            if (loaded) displayTree();
        });
    }

    private void setupInsertBtn() {
        insertBtn = new Button("Insert");
        insertBtn.setPrefWidth(BUTTON_WIDTH);
        insertBtn.setTooltip(new Tooltip("Insert new Person"));
        insertBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (loaded) insertDialog();
            }
        });
    }

    private void setupCleanBtn() {
        cleanBtn = new Button("Clean");
        cleanBtn.setPrefWidth(BUTTON_WIDTH);
        cleanBtn.setTooltip(new Tooltip("Clean the plot"));
        cleanBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (loaded) {
                    pTree.destroy();
                    graphicsContainer.getChildren().clear();
                }
            }
        });
    }

    private void insertDialog() {
        TextInputDialog in = new TextInputDialog();
        in.getEditor().setPromptText("Enter the Name");
        in.getEditor().setAlignment(Pos.CENTER_LEFT);
        in.getEditor().setFocusTraversable(true);
        in.setTitle("Person Insert Editor");
        in.setHeaderText("Insert new Person's name:");
        Button ok = (Button) in.getDialogPane().lookupButton(ButtonType.OK);

        ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String fullName = in.getEditor().getText();
                insertPerson(fullName);
            }
        });
        in.show();
    }

    public void displayTree() {
        graphicsContainer.getChildren().clear();
        PersonNode root = pTree.getRoot();
        displayHelper(root, 1, midX);
    }

    private void displayHelper(PersonNode root, int indent, double posX) {
        if (root == null || indent > 6) return; // indent > 5 to stop over-sized plot

        String col = "#F57F17";
        String col2 = "#039BE5";
        int alpha = (int) Math.round(1.0 / indent * 255);
        String hex = Integer.toHexString(alpha).toUpperCase();
        if (hex.length() == 1) hex = "0" + hex;
        col = col + hex;
        col2 = col2 + hex;
        root.getGraphic().setStyle("-fx-background-color: " + col);

        if (root.left != null) makeLine(posX, indent, -1, col2);
        if (root.right != null) makeLine(posX, indent, 1, col2);

        graphicsContainer.getChildren().add(root);
        graphicsContainer.setTopAnchor(root, ROW_GAP * indent);
        graphicsContainer.setLeftAnchor(root, posX);

        displayHelper(root.left, indent + 1, posX - midX / (Math.pow(2, indent)));
        displayHelper(root.right, indent + 1, posX + midX / (Math.pow(2, indent)));
    }

    private void makeLine(double posX, int indent, int direction, String col) {
        posX = posX + COLUMN_OFFSET;
        double l1Y = ROW_GAP * indent + ROW_OFFSET*2;
        double lXOffset = midX / (Math.pow(2, indent)) * direction;
        double l2Y = ROW_GAP * indent + ROW_GAP / 2 + ROW_OFFSET;
        double l3Y = ROW_GAP * (indent + 1);

        Line l1 = new Line(posX, l1Y, posX, l2Y);
        Line l2 = new Line(posX, l2Y, posX + lXOffset, l2Y);
        Line l3 = new Line(posX + lXOffset, l2Y, posX + lXOffset, l3Y);

        Line[] lines = new Line[]{l1, l2, l3};
        Arrays.stream(lines).forEach(l -> {
            l.setStroke(Color.valueOf(col));
            l.setStrokeWidth(5);
            l.setStrokeLineCap(StrokeLineCap.ROUND);
        });
        graphicsContainer.getChildren().addAll(l1, l2, l3);
    }

    private HBox loadingZone() {
        HBox toReturn = new HBox();
        surname = false;
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
        snRB.setToggleGroup(tg);
        fnRB.setToggleGroup(tg);
        tg.selectToggle(fnRB);

        toReturn.getChildren().addAll(modeLb, snRB, fnRB);

        return toReturn;
    }

    private void loadUpFile() throws FileNotFoundException {
        pTree = new AVLTreeV1(this);
// TODO for test       FileChooser fc = new FileChooser();
//        fc.setTitle("Choose a file to open");
//        File file = fc.showOpenDialog(primaryStage);
        File file = new File("mswdev.csv");
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            String fn = sc.next();
            String sn = sc.next();
            sc.nextLine();
            // Use different key, toggle by radio button
            if (surname) pTree.insert(sn, new Person(fn, sn, ++uid, sn, this));
            else pTree.insert(fn, new Person(fn, sn, ++uid, fn, this));
        }
        pTree.preOrder();
        sc.close();
        displayTree();
    }

    public void remove(String key, Person p) {
        pTree.remove(key, p);
        displayTree();
    }

    public void removeNode(PersonNode pn) {
        pTree.removeNode(pn);
        displayTree();
    }


    public void nodeDialog(String key) {
        Dialog<ButtonType> nodeEditor = new Dialog<>();

        PersonNode showing = pTree.search(key);

        nodeEditor.setTitle("PersonNode '%s'".formatted(key.toUpperCase()));
//        nodeEditor.getDialogPane().setPrefSize(500, 400);
        nodeEditor.initModality(Modality.APPLICATION_MODAL);
        nodeEditor.setResizable(false);

        nodeEditor.setHeaderText("Persons in this Node:");

        StringBuilder content = new StringBuilder();
        for (MenuItem p : showing.getItems()) {
            content.append(p + "\n\n");
        }
        nodeEditor.setContentText("" + content);

        ButtonType delete = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);
        nodeEditor.getDialogPane().getButtonTypes().add(delete);
        boolean disabled = false;
        nodeEditor.getDialogPane().lookupButton(delete).setDisable(disabled);

        nodeEditor.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Button cancel = (Button) nodeEditor.getDialogPane().lookupButton(ButtonType.CANCEL);
        cancel.setText("Cancel");
        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                nodeEditor.close();
            }
        });

        Optional<ButtonType> opt = nodeEditor.showAndWait();
        opt.ifPresent(new Consumer<ButtonType>() {
            @Override
            public void accept(ButtonType buttonType) {
                if (buttonType == delete) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText("Remove entire Node?");

                    Button ok = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
                    ok.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            System.out.println("Remove this Node");
                            removeNode(showing);
                            nodeEditor.close();
                        }
                    });
                    alert.show();
                }
                else if (buttonType == ButtonType.CANCEL) {
                    nodeEditor.close();
                }
            }
        });
    }

    public void personDialog(String key, int ID) {
        Person p = pTree.search(key).getPerson(ID);
        if (p == null) return;

        TextInputDialog pd = new TextInputDialog();
        pd.getEditor().setPromptText("Modify the Name");
        pd.getEditor().setAlignment(Pos.CENTER_LEFT);
        pd.getEditor().setFocusTraversable(false);
        pd.setTitle("Person Info");
        pd.setHeaderText("Person Info Panel:");
        pd.setContentText(p + "\n");

        Button modify = (Button) pd.getDialogPane().lookupButton(ButtonType.OK);
        modify.setText("Modify");
        modify.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                remove(key, p);
                String fullName = pd.getEditor().getText();
                insertPerson(fullName);
            }
        });

        pd.getDialogPane().getButtonTypes().add(ButtonType.APPLY);
        Button remove = (Button) pd.getDialogPane().lookupButton(ButtonType.APPLY);
        remove.setText("Remove");
        remove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                remove(key, p);
            }
        });

        pd.show();
    }

    private void insertPerson(String fullName) {
        String[] s = fullName.split("\\s+");
        String fnKey = s[0];
        String snKey = s[1];
        if (surname) pTree.insert(snKey, new Person(fnKey, snKey, ++uid, snKey, this));
        else pTree.insert(fnKey, new Person(fnKey, snKey, ++uid, fnKey, this));
        displayTree();
    }

}
