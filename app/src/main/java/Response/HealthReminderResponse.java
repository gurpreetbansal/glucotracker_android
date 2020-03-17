package Response;

public class HealthReminderResponse {
    String reminderText;
    String timeSet;
    String reminderType;
    String switchButtonState;
    int hours;
    int min;

    public String getReminderType() {
        return reminderType;
    }

    public void setReminderType(String reminderType) {
        this.reminderType = reminderType;
    }

    public String getTimeSet() {
        return timeSet;
    }

    public void setTimeSet(String timeSet) {
        this.timeSet = timeSet;
    }

    public String getReminderText() {
        return reminderText;
    }

    public void setReminderText(String reminderText) {
        this.reminderText = reminderText;
    }

    public HealthReminderResponse(int min, String timeSet, String reminderType, String switchButtonState, int hours, String reminderText) {
        this.min = min;
        this.timeSet = timeSet;
        this.reminderType = reminderType;
        this.switchButtonState = switchButtonState;
        this.hours = hours;
        this.reminderText = reminderText;
    }

    public String getSwitchButtonState() {
        return switchButtonState;
    }

    public void setSwitchButtonState(String switchButtonState) {
        this.switchButtonState = switchButtonState;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }
}
