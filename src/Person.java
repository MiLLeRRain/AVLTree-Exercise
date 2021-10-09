import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;

class Person extends MenuItem {

    private String fullName;
    private String name;
    private String surname;
    private String key;
    private final int ID;

    public Person(String fn, String sn, int uid, String key) {
        this.name = fn;
        this.surname = sn;
        this.fullName = fn + " " + sn;
        this.ID = uid;
        this.key = key;
        this.setText(this.toString());
        setupAction();
    }

    private void setupAction() {
        this.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                MainPage.personDialog(key, ID);
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
