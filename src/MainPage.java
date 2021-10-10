import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
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

    private static AnchorPane graphicsContainer;

    private HBox menu;

    private static int uid = 80000;

    private static final int BUTTON_WIDTH = 80;

    private static final double COLUMN_OFFSET = 36;

    private static final double ROW_OFFSET = 20;

    private static final double ROW_GAP = 60;

    private static double midX;

    private int indentLimit = 6;

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
    private MenuButton print;

    private Label status;



    public MainPage(Stage stage) {
        pTree = new AVLTreeV1(this);
        primaryStage = stage;
        initUI();
    }

    private void initUI() {
        ScrollPane sp = new ScrollPane();
        sp.prefWidthProperty().bind(this.widthProperty());
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        mainContainer = new VBox();
        mainContainer.prefWidthProperty().bind(this.widthProperty());
        mainContainer.prefHeightProperty().bind(this.heightProperty());



        graphicsContainer = new AnchorPane();
        setupGraphicContainer();

        menu = setMenu();

        sp.setContent(graphicsContainer);

        mainContainer.getChildren().addAll(sp, menu);

        this.getChildren().add(mainContainer);
    }

    private void setupGraphicContainer() {
        status = new Label("Press Load Button to load.");
        status.setStyle("-fx-background-color: #ffffff00");
        graphicsContainer.prefHeightProperty().bind(mainContainer.heightProperty());
        graphicsContainer.prefWidthProperty().bind(mainContainer.widthProperty());
        graphicsContainer.setStyle("-fx-background-color: #E3F2FD");

        graphicsContainer.getChildren().add(status);
        graphicsContainer.setBottomAnchor(status, 70.0);
        graphicsContainer.setLeftAnchor(status, 10.0);
    }

    private HBox setMenu() {
        HBox toReturn = new HBox();
        toReturn.setEffect(new InnerShadow());
        toReturn.setPadding(new Insets(10));
// TODO style change later      toReturn.setPrefHeight(100);
        toReturn.prefWidthProperty().bind(mainContainer.widthProperty());
        toReturn.setStyle("-fx-background-color: #039BE5;" + "-fx-alignment: CENTER_LEFT");

        loadingPattern = loadingZone();

        ButtonBar bb = buttonsLoad();

        HBox lbl = new HBox();
        Label cc = new Label("AVLTree Person Data Management System");
        cc.setStyle("-fx-background-color: #FFFFFF00");
        lbl.getChildren().add(cc);
        lbl.prefWidthProperty().bind(toReturn.widthProperty());
        lbl.setAlignment(Pos.CENTER_RIGHT);

        toReturn.getChildren().addAll(loadingPattern, bb, lbl);

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
//        setupDisplayBtn();
        setupInsertBtn();
        setupCleanBtn();
        setupPrintBtn();

        toReturn.getButtons().addAll(loadBtn, insertBtn, cleanBtn, print);
        return toReturn;
    }

    private void setupPrintBtn() {
        print = new MenuButton("Print");
        print.setTooltip(new Tooltip("Print in Console"));
        print.setAlignment(Pos.CENTER);
        print.setPrefWidth(BUTTON_WIDTH);
        MenuItem preOrder = new MenuItem("Pre-Order Print");
        MenuItem inOrder = new MenuItem("In-Order Print");
        MenuItem postOrder = new MenuItem("Post-Order Print");

        preOrder.setOnAction(a -> {if (pTree.getRoot() != null) pTree.preOrder();});
        inOrder.setOnAction(a -> {if (pTree.getRoot() != null) pTree.inOrder();});
        postOrder.setOnAction(a -> {if (pTree.getRoot() != null) pTree.postOrder();});

        print.getItems().addAll(preOrder, inOrder, postOrder);
    }

    private void setupLoadBtn() {
        loadBtn = new Button("Load");
        loadBtn.setPrefWidth(BUTTON_WIDTH);
        loadBtn.setTooltip(new Tooltip("Load from file"));
        loadBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                    loaded = true;
                try {
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
                    status.setText("Press Load Button to load.");
                    graphicsContainer.getChildren().add(status);
                }
            }
        });
    }

    public void displayTree() {
        graphicsContainer.getChildren().clear();
        graphicsContainer.getChildren().add(status);
        PersonNode root = pTree.getRoot();
        if (root == null) return;
        midX = root.weight * 200 - 50;
        displayHelper(root, 1, midX);
    }

    private void displayHelper(PersonNode root, int indent, double posX) {
        if (root == null || indent > indentLimit) return; // indent > 5 to stop over-sized plot

        String col = "#F57F17";
        String col2 = "#039BE5";
        int alpha = (int) Math.round(1.0 / indent * 255);
        String hex = Integer.toHexString(alpha).toUpperCase();
        if (hex.length() == 1) hex = "0" + hex;
        col = col + hex;
        col2 = col2 + hex;
//        root.getGraphic().setStyle("-fx-background-color: " + col);
        root.setStyle("-fx-background-color: " + col);

        if (root.left != null) makeLine(posX, indent, -1, col2);
        if (root.right != null) makeLine(posX, indent, 1, col2);

        graphicsContainer.getChildren().add(root);
        graphicsContainer.setTopAnchor(root, ROW_GAP * indent + ROW_OFFSET / 4);
        graphicsContainer.setLeftAnchor(root, posX);

        displayHelper(root.left, indent + 1, posX - midX / (Math.pow(2, indent)));
        displayHelper(root.right, indent + 1, posX + midX / (Math.pow(2, indent)));
    }

    private void makeLine(double posX, int indent, int direction, String col) {
        if (indent > indentLimit - 1) return;
        double lineOffset = 2;
        posX = posX + COLUMN_OFFSET;
        double l1Y = ROW_GAP * indent + ROW_OFFSET * 2 - lineOffset;
        double lXOffset = midX / (Math.pow(2, indent)) * direction;
        double l2Y = ROW_GAP * indent + ROW_GAP / 2 + ROW_OFFSET - lineOffset;
        double l3Y = ROW_GAP * (indent + 1) - lineOffset;

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

        toReturn.setPrefWidth(960);
        toReturn.setAlignment(Pos.CENTER_LEFT);

        return toReturn;
    }

    private void loadUpFile() throws FileNotFoundException {
        pTree = new AVLTreeV1(this);

        FileChooser fc = new FileChooser();
        fc.setTitle("Choose a file to open");
        File file = fc.showOpenDialog(primaryStage);
//        File file = new File("mswdev.csv");
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            String fn = sc.next();
            String sn = sc.next();
            sc.nextLine();
            // Use different key, toggle by radio button
            if (surname) pTree.insert(sn, new Person(fn, sn, ++uid, sn, this));
            else pTree.insert(fn, new Person(fn, sn, ++uid, fn, this));
        }
        sc.close();
        status.setText("Loading Complete.");
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
        nodeEditor.getDialogPane().setStyle("-fx-background-color: #E8EAF6;" +
                "-fx-effect: innershadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0.5, 0.0, 0.0)");

        nodeEditor.setTitle("PersonNode '%s'".formatted(key.toUpperCase()));
//        nodeEditor.getDialogPane().setPrefSize(500, 400);
        nodeEditor.initModality(Modality.APPLICATION_MODAL);
        nodeEditor.setResizable(false);

        Label head = new Label(showing.box.size()+" Person in this Node:");
        head.setAlignment(Pos.CENTER_LEFT);
        head.setStyle("-fx-font-size: 16;" +
                "-fx-background-color: #C5CAE9;" +
                "-fx-padding: 20;" + "-fx-spacing: 20");
        nodeEditor.getDialogPane().setHeader(head);

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

        ChoiceBox<MenuItem> cb = new ChoiceBox();
        cb.setStyle("-fx-background-color: rgba(255, 255, 255, 0)");
        for (MenuItem p : showing.box.values()) {
            cb.getItems().add(p);
        }
        cb.setValue(cb.getItems().get(0));

        nodeEditor.getDialogPane().setContent(cb);

        ButtonType choose = new ButtonType("Choose", ButtonBar.ButtonData.NEXT_FORWARD);
        nodeEditor.getDialogPane().getButtonTypes().add(choose);

        Optional<ButtonType> opt = nodeEditor.showAndWait();
        opt.ifPresent(new Consumer<ButtonType>() {
            @Override
            public void accept(ButtonType buttonType) {
                if (buttonType == delete) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText("Remove entire Node?");
                    setupAlert(alert);

                    Button ok = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
                    ok.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            removeNode(showing);
                            nodeEditor.close();
                        }
                    });
                    alert.show();
                }
                else if (buttonType == ButtonType.CANCEL) {
                    nodeEditor.close();
                }
                else if (buttonType == choose) {
                    Person p = (Person)cb.getValue();
                    personDialog(p.getKey(), p.getID());
                }
            }
        });
    }

    private void setupAlert(Alert alert) {
        alert.getDialogPane().setStyle("-fx-background-color: #FF9800;" +
                "-fx-effect: innershadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0.5, 0.0, 0.0)");
    }

    public void personDialog(String key, int ID) {
        Person p = pTree.search(key).getPerson(ID);
        if (p == null) return;

        TextInputDialog pd = new TextInputDialog();
        pd.getDialogPane().setStyle("-fx-background-color: #E8F5E9;" +
                "-fx-effect: innershadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0.5, 0.0, 0.0)");
        pd.initModality(Modality.APPLICATION_MODAL);
        pd.getEditor().setPromptText("Enter New Name");
        pd.getEditor().setAlignment(Pos.CENTER_LEFT);
        pd.setTitle("Person Info");

        final BooleanProperty firstTime = new SimpleBooleanProperty(true);
        pd.getEditor().focusedProperty().addListener((observable,  oldValue,  newValue) -> {
            if(newValue && firstTime.get()){
                pd.getDialogPane().requestFocus(); // Delegate the focus to container
                firstTime.setValue(false); // Variable value changed for future references
            }
        });

        Label head = new Label("Person Info:\n" + "Remove / Edit");
        head.setAlignment(Pos.CENTER_LEFT);
        head.setStyle("-fx-font-size: 16;" +
                "-fx-background-color: #C8E6C9;" +
                "-fx-padding: 20;" + "-fx-spacing: 20");
        pd.getDialogPane().setHeader(head);

        pd.setContentText(p + "\n");

        Button modify = (Button) pd.getDialogPane().lookupButton(ButtonType.OK);
        modify.setText("Edit");
        modify.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String fullName = pd.getEditor().getText();
                if (fullName == null || !fullName.contains(" ")) return;

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.getDialogPane().setStyle("-fx-background-color: #9CCC65;" +
                        "-fx-effect: innershadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0.5, 0.0, 0.0)");
                alert.setHeaderText("Edit Entry: " + p + "\n to: " + fullName);

                Button ok = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
                ok.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        remove(key, p);
                        String fullName = pd.getEditor().getText();
                        insertPerson(fullName);
                    }
                });
                alert.show();
            }
        });

        pd.getDialogPane().getButtonTypes().add(ButtonType.APPLY);
        Button remove = (Button) pd.getDialogPane().lookupButton(ButtonType.APPLY);
        remove.setText("Remove");
        remove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("Remove Entry: " + p);
                setupAlert(alert);

                Button ok = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
                ok.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        remove(key, p);
                    }
                });
                alert.show();
            }
        });

        pd.show();
    }

    private void insertDialog() {
        TextInputDialog in = new TextInputDialog();
        in.getDialogPane().setStyle("-fx-background-color: #E8F5E9;" +
                "-fx-effect: innershadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0.5, 0.0, 0.0)");
        in.getEditor().setPromptText("Enter the Name");
        in.getEditor().setAlignment(Pos.CENTER_LEFT);
        BooleanProperty firstTime = new SimpleBooleanProperty(true);
        in.getEditor().focusedProperty().addListener((observable,  oldValue,  newValue) -> {
            if(newValue && firstTime.get()){
                in.getDialogPane().requestFocus(); // Delegate the focus to container
                firstTime.setValue(false); // Variable value changed for future references
            }
        });

        in.setTitle("Person Insert Editor");

        Label head = new Label("Insert new Person:");
        head.setAlignment(Pos.CENTER_LEFT);
        head.setStyle("-fx-font-size: 16;" +
                "-fx-background-color: #C8E6C9;" +
                "-fx-padding: 20;" + "-fx-spacing: 20");
        in.getDialogPane().setHeader(head);

        Button ok = (Button) in.getDialogPane().lookupButton(ButtonType.OK);

        ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String fullName = in.getEditor().getText();
                if (fullName == null || !fullName.contains(" ")) return;
                insertPerson(fullName);
            }
        });
        in.show();
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
