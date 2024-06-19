package project.hansungcomputerdepartment;

public class Notice {
    private String category;
    private String title;
    private String author;
    private String date;

    public Notice() {
        // Default constructor required for calls to DataSnapshot.getValue(Notice.class)
    }

    public Notice(String category, String title, String author, String date) {
        this.category = category;
        this.title = title;
        this.author = author;
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
