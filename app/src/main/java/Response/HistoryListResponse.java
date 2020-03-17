package Response;

public class HistoryListResponse {
    private String date;
    private String time;
    private float result;
    private boolean diet;
    private boolean medicine;


    public HistoryListResponse( String date, String time, float result, boolean diet, boolean medicine) {
        this.date = date;
        this.time = time;
        this.result = result;
        this.diet = diet;
        this.medicine = medicine;
    }

    public HistoryListResponse(String date, String time, String s, String s1, String s2) {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public float getResult() {
        return result;
    }

    public void setResult(float result) {
        this.result = result;
    }

    public boolean getDiet() {
        return diet;
    }

    public void setDiet(boolean diet) {
        this.diet = diet;
    }

    public boolean isMedicine() {
        return medicine;
    }

    public void setMedicine(boolean medicine) {
        this.medicine = medicine;
    }
}
