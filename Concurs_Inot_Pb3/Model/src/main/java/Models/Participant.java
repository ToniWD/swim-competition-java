package Models;

public class Participant extends Entity<Long> {
    private String firstName;
    private String lastName;
    private int age;
    private int nrEvents = -1;

    public Participant(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getNrEvents() {
        return nrEvents;
    }
    public void setNrEvents(int nrEvents) {
        this.nrEvents = nrEvents;
    }
}
