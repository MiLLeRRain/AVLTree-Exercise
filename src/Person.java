import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;

/**
 * The Person Class
 * It contains the name, the key, unique ID and the MainPage which is holding this object.
 */
class Person extends MenuItem {

    private String fullName;
    private String name;
    private String surname;

    private String key;
    private final int ID;
    private MainPage mp;

    /**
     * Constructor
     * @param fn the first name
     * @param sn the surname
     * @param uid the unique ID
     * @param key the key allocated by the system
     * @param mp the MainPage UI
     */
    public Person(String fn, String sn, int uid, String key, MainPage mp) {
        this.name = fn;
        this.surname = sn;
        this.fullName = fn + " " + sn;
        this.ID = uid;
        this.key = key;
        this.setText(this.toString());
        this.mp = mp;
        setupAction();
    }

    /**
     * The action while the Dialog of Person needs to be activated.
     */
    private void setupAction() {
        this.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                mp.personDialog(key, ID);
            }
        });
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public int getID() {
        return ID;
    }

    public String getKey() {
        return key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public String toString() {
        return this.fullName + " | ID: " + this.ID;
    }

}
