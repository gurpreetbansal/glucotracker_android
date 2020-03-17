package Response;

public class PairedDevicesResponse {

    private String deviceName;

    public PairedDevicesResponse(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
