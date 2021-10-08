import javafx.scene.control.MenuItem;

class Person extends MenuItem {

    String fullName;
    String name;
    String surname;
    int ID;

    public Person(String n, int uid) {
        this.fullName = n;
        String[] fName = fullName.split("\\s+");
        this.name = fName[0];
        this.surname = fName[1];
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
        return this.name + " " + this.surname + " | ID: " + this.ID;
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
