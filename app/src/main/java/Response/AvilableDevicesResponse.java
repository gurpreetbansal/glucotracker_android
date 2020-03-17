package Response;

public class AvilableDevicesResponse {
    private String deviceName;

    public AvilableDevicesResponse(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
