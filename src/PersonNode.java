import javafx.scene.control.MenuButton;

import java.util.HashMap;

/**
 * It is a node, also a VBox holding an arraylist of persons.
 * The key can only be the first name or surname, it will be decided in main class.
 * Two separate indexing Trees will be created according to the first name or surname.
 */
class PersonNode extends MenuButton implements Comparable<PersonNode> {

    /**
     * A box to hold all person with same key
     */
    HashMap<Integer, Person> box;
    int depth;
    String key;

    PersonNode left;
    PersonNode right;

    public PersonNode(String key, PersonNode l, PersonNode r) {
        this.key = key;
        this.box = new HashMap<>();
        this.setText(key); // Set the Menu Text, shows the key
        this.left = l;
        this.right = r;
        this.depth = 0; // Empty Root node height = 0
    }

    public Person insertPerson(Person p) {
        return box.put(p.ID, p);
    }

    public Person removePerson(Person p) {
        return box.remove(p.ID);
    }

    public boolean isEmpty() {
        return this.box.size() == 0;
    }

    public HashMap<Integer, Person> getBox() {
        return box;
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

    @Override
    public int compareTo(PersonNode o) {
        return this.key.compareTo(o.key);
    }

    @Override
    public String toString() {
        return this.key + " | Size: " + box.size() + " | Depth: " + this.depth;
    }

    public void updateText() {
        this.setText(key);
    }
}
