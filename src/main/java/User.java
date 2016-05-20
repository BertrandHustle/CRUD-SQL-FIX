
/**
 * User class
 */
public class User {

    //properties
    private String name;
    //String password;
    private int id;

    public User(String name) {
        this.name = name;
        //this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
