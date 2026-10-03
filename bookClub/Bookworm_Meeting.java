package bookClub;

public class Bookworm_Meeting {

    private String bookName;
    private String maxCapacity;
    private String startDate;
    private String endDate;

    public Bookworm_Meeting(
            String bookName,
            String maxCapacity,
            String startDate,
            String endDate) {

        this.bookName = bookName;
        this.maxCapacity = maxCapacity;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // -----------------------------
    // Getters
    // -----------------------------

    public String getBookName() {
        return bookName;
    }

    public String getMaxCapacity() {
        return maxCapacity;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    // -----------------------------
    // Setters
    // -----------------------------

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public void setMaxCapacity(String maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
