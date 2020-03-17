package BluetoothConnectivity;


import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;


public class SerialDevice implements IDevice {

    InputStream mInStream = null;
    OutputStream mOutStream = null;
	String mAddress = null;
	
	SerialPort mSerialPort;
	
	DspPwrHalService dspPwrHalService = new DspPwrHalService();
	
    public SerialDevice(String address){
    	this.mAddress = address;
    }

	@Override
	public void Connect() throws Exception {
		if(this.mSerialPort != null){
			return ;
		}
		mSerialPort = new SerialPort(new File("/dev/ttyS3"), 19200, 0);
		mOutStream = mSerialPort.getOutputStream();
		mInStream = mSerialPort.getInputStream();		
	}

	@Override
	public void Disconnect() throws Exception {
    	//this.mSerialPort.close();
    	this.mSerialPort = null;
		if(this.mInStream != null){
        	this.mInStream.close();
        	this.mInStream = null;
    	}
    	if(this.mOutStream != null){
    		this.mOutStream.close();
    		this.mOutStream = null;
    	}
	}

	@Override
	public InputStream GetInputStream() throws Exception {
		// TODO Auto-generated method stub
		return this.mInStream;
	}

	@Override
	public OutputStream GetOutputStream() throws Exception {
		// TODO Auto-generated method stub
		return this.mOutStream;
	}
	@Override
	public void DspPowerOn() {
		dspPwrHalService.set_power_on(50, 0);
        dspPwrHalService.set_power_on(33, 0);
        dspPwrHalService.set_power_on(19, 5);
        dspPwrHalService.set_power_on(10, 0);
        try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	@Override
	public void DspPowerOff() {
		dspPwrHalService.set_power_off(10, 200);
        dspPwrHalService.set_power_off(33, 0);
        dspPwrHalService.set_power_off(50, 0);
        dspPwrHalService.set_power_off(19, 2);
        try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}        
	}
}
