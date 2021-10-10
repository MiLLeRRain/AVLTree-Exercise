import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;

import java.util.HashMap;

/**
 * It is a node, also a VBox holding an arraylist of persons.
 * The key can only be the first name or surname, it will be decided in main class.
 * Two separate indexing Trees will be created according to the first name or surname.
 */
class PersonNode extends Button implements Comparable<PersonNode> {

//    /**
//     * A box to hold all person with same key
//     */
    HashMap<Integer, Person> box;
    MainPage mp;
    int weight;
    String key;
//    Button edit;

    final int BUTTON_WIDTH = 90;

    PersonNode left;
    PersonNode right;

    public PersonNode(String key, PersonNode l, PersonNode r, MainPage mp) {
        this.key = key;
        this.box = new HashMap<>();

        this.mp = mp;
        this.left = l;
        this.right = r;
        this.weight = 0; // Empty Root node height = 0
//        this.setPrefWidth(BUTTON_WIDTH);
        this.setAlignment(Pos.CENTER);
//        this.edit = buttonSet();
//        this.setGraphic(edit); // Put the button into MenuButton panel
        buttonSetup();
        this.setStyle("-fx-background-color: rgba(255, 255, 255, 0);" + "-fx-selection-bar: #ffffff00;");
    }

    private Button buttonSet() {
        Button toReturn = new Button(key);
        toReturn.setPrefWidth(BUTTON_WIDTH-20);
        toReturn.setTooltip(new Tooltip(key));
        toReturn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                popAlert();
            }
        });

        return toReturn;
    }

    private void buttonSetup() {
        this.setAlignment(Pos.CENTER);
        this.setText(key); // Set the Menu Text, shows the key
        this.setPrefWidth(BUTTON_WIDTH-20);
        this.setTooltip(new Tooltip(key));
        this.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                popAlert();
            }
        });
    }


    private void popAlert() {
        mp.nodeDialog(key);
    }

    public void insertPerson(Person p) {
//        return this.getItems().add(p);
        box.put(p.getID(), p);
    }

    public void removePerson(Person p) {
//        return this.getItems().remove(p);
        box.remove(p.getID());
    }

    public boolean isEmpty() {
//        return this.getItems().isEmpty();
        return box.isEmpty();
    }

    public String getKey() {
        return key;
    }

    public PersonNode getLeft() {
        return left;
    }

    public PersonNode getRight() {
        return right;
    }

    public Person getPerson(int ID) {
        Person p = null;
        for (MenuItem mi : box.values()) {
            if (((Person) mi).getID() == ID) p = (Person) mi;
        }
        return p;
    }

    @Override
    public int compareTo(PersonNode o) {
        return this.key.compareTo(o.key);
    }

    @Override
    public String toString() {
//        return this.key + " | Size: " + this.getItems().size();
        return this.key + " | Size: " + box.size();
    }

    public void updateText() {
        this.setText(key);
    }
}
