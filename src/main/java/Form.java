/**
 * Creates forms
 */
public class Form {

    String title;
    String genre;
    String system;
    int ID;

    public Form(String title, String genre, String system, int ID) {
        this.title = title;
        this.genre = genre;
        this.system = system;
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String toString() {
        return(title + " " + genre + " " + system + " " + ID);
    }
}
