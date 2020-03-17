package BluetoothConnectivity;


public class DspPwrHalService
{

    public DspPwrHalService()
    {
        init();
    }

    private native boolean _init();

    private native boolean _set_off(int i, int j);

    private native boolean _set_on(int i, int j);

    public boolean init()
    {
        return _init();
    }

    public boolean set_power_off(int i, int j)
    {
        return _set_off(i, j);
    }

    public boolean set_power_on(int i, int j)
    {
        return _set_on(i, j);
    }

    static 
    {
        System.load("libdsp_power_hal_jni.so");
    }
}
