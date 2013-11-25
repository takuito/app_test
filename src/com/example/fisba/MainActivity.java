package com.example.fisba;

//import com.futronictech.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Button;

public class MainActivity extends Activity {
	private static Button mButtonScan;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mButtonScan = (Button) findViewById(R.id.scan_button);
		mButtonScan.setOnClickListener(new OnClickListener(){
			if( mFPScan != null )
    		{
    			mStop = true;
    			mFPScan.stop();
    			
    		}
			mStop = false;
    		if(mUsbHostMode)
    		{
        		usb_host_ctx.CloseDevice();
        		if(usb_host_ctx.OpenDevice(0, true))
                {
        			if( StartScan() )
	        		{
	        			mButtonScan.setEnabled(false);
	        	        mButtonSave.setEnabled(false);
	        	        mCheckUsbHostMode.setEnabled(false);
	        	        mButtonStop.setEnabled(true);
	        		}	
                }
            	else
            	{
            		if(!usb_host_ctx.IsPendingOpen())
            		{
            			mMessage.setText("Can not start scan operation.\nCan't open scanner device");
            		}
            	}    
    		}
    		else
    		{
    			if( StartScan() )
        		{
        			mButtonScan.setEnabled(false);
        	        mButtonSave.setEnabled(false);
        	        mCheckUsbHostMode.setEnabled(false);
        	        mButtonStop.setEnabled(true);
        		}	
    		}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
