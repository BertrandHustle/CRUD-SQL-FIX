/**
 * Creates forms
 */
public class Form {

    String title;
    String genre;
    String system;

    public Form(String title, String genre, String system) {
        this.title = title;
        this.genre = genre;
        this.system = system;
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

    public String toString() {
        return(title + " " + genre + " " + system);
    }
}
