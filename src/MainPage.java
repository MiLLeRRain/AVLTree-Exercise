import javafx.scene.control.Button;
import javafx.scene.control.TabPane;

import java.io.File;

/**
 * A tabPane, main interface for user,
 * Should contain function buttons: Load(load from file), CleanPane,
 * Choose index pattern(either by first name or surname),
 * Edit the PersonNode(MenuButton) and the Person(MenuItem), Add new Person, Remove old Person,
 * Search for Person(1. search the PersonNode, by name or surname, 2. search the person with id),
 *
 * TODO Animation of the tree re-balancing?
 */
public class MainPage extends TabPane {

    /**
     * Main structure to hold all data
     */
    private AVLTreeV1 pTree;
    /**
     * A toggle to switch index pattern, by first name or surname
     */
    private boolean surname;
    private Button loadingPattern;
    /**
     * Button to load file
     */
    private Button load;
    /**
     * Button to destroy and clean
     */
    private Button clean;


    public MainPage(boolean surname, File file) {
        pTree = new AVLTreeV1();
        if (surname) loadSurnameTree(file);
        else loadNameTree(file);
    }

    private void loadSurnameTree(File file) {

    }

    private void loadNameTree(File file) {

    }


}
