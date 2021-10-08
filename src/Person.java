import javafx.scene.control.MenuItem;

class Person extends MenuItem {

    String fullName;
    String name;
    String surname;
    int ID;

    public Person(String fn, String sn, int uid) {
        this.name = fn;
        this.surname = sn;
        this.fullName = fn + " " + sn;
        this.ID = uid;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
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

    @Override
    public boolean equals(Object o) {
        return o instanceof Person
                && (this.fullName + this.ID).equals(((Person) o).fullName + ((Person) o).fullName);
    }

    @Override
    public int hashCode() {
        return (this.fullName + this.ID).hashCode();
    }

}
