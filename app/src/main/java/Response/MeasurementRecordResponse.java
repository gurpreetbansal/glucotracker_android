package Response;


public class MeasurementRecordResponse {
    String date;
    String recordData;
    String recordResult;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRecordData() {
        return recordData;
    }

    public void setRecordData(String recordData) {
        this.recordData = recordData;
    }

    public String getRecordResult() {
        return recordResult;
    }

    public void setRecordResult(String recordResult) {
        this.recordResult = recordResult;
    }

    public MeasurementRecordResponse(String date, String recordData, String recordResult) {
        this.date = date;
        this.recordData = recordData;
        this.recordResult = recordResult;
    }
}
