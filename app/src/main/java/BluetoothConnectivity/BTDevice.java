package BluetoothConnectivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BTDevice implements IDevice {

	final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	
	BluetoothSocket mSocket = null;
    InputStream mInStream = null;
    OutputStream mOutStream = null;
 	
    String mAddress = "";
    public BTDevice(String address){
    	this.mAddress = address;
    }
	@Override
	public void Connect() throws Exception {
		// TODO Auto-generated method stub
		BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
		btAdapter.cancelDiscovery();
		BluetoothDevice bluetoothDevice = btAdapter.getRemoteDevice(mAddress);
		mSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);		
		mSocket.connect();
		this.mInStream = mSocket.getInputStream();
		this.mOutStream = mSocket.getOutputStream();
	}

	@Override
	public void Disconnect() throws Exception {
    	if(this.mSocket != null){
    		this.mSocket.close();
    	}
		if(this.mInStream != null){
        	this.mInStream.close();    		
    	}
    	if(this.mOutStream != null){
    		this.mOutStream.close();
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
		// TODO Auto-generated method stub
		
	}
	@Override
	public void DspPowerOff() {
		// TODO Auto-generated method stub
		
	}

}
