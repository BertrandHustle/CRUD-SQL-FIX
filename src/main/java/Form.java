/**
 * Creates forms
 */
public class Form {

    String title;
    String genre;
    String year;
    String system;

    public Form(String title, String genre, String year, String system) {
        this.title = title;
        this.genre = genre;
        this.year = year;
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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }
}
