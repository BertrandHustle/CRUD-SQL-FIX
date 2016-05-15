/**
 * Creates forms
 */
public class Form {

    String title;
    String genre;
    String system;
    int userId;

    public Form(String title, String genre, String system, int userId) {
        this.title = title;
        this.genre = genre;
        this.system = system;
        this.userId = userId;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String toString() {
        return("Title: " +  title + " Genre: " + genre + " System: " + system);
    }
}
