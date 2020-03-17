package BluetoothConnectivity;

import java.io.InputStream;
import java.io.OutputStream;

public interface IDevice {
	void Connect()throws Exception;
	void Disconnect()throws Exception;
	InputStream GetInputStream()throws Exception;
	OutputStream GetOutputStream()throws Exception;
	
	void DspPowerOn();
	void DspPowerOff();
}
