package BluetoothConnectivity;

import java.io.OutputStream;

public class OutRunnable implements Runnable {
	String cmd = null;
	OutputStream mOutputStream = null;

	public OutRunnable(String cmd, OutputStream outStream){
		this.cmd = cmd;
		this.mOutputStream = outStream;
	}
	@Override
	public void run() {
		try {			
			this.mOutputStream.write(cmd.getBytes("UTF-8"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}