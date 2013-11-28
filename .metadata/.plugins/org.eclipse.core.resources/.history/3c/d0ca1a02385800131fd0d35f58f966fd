package com.example.fisba;

//import com.futronictech.R;

import com.example.fisba.Scanner;
import com.example.fisba.UsbDeviceDataExchangeImpl;
import com.example.fisba.FPScan;

import android.os.Bundle;
import android.os.Message;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.os.Handler;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
	
	private static Button mButtonScan;
	private static Button mButtonStop;
	private Button mButtonSave;
	private static TextView mMessage;
	private static TextView mText;
	
	private CheckBox mCheckFrame;
	private CheckBox mCheckLFD;
	private CheckBox mCheckInvertImage;
	private CheckBox mCheckUsbHostMode;
	
    public static boolean mStop = false;
	public static boolean mFrame = true;
	public static boolean mLFD = false;
	public static boolean mInvertImage = false;
    
    public static final int MESSAGE_SHOW_MSG = 1;
    public static final int MESSAGE_SHOW_SCANNER_INFO = 2;
    public static final int MESSAGE_SHOW_IMAGE = 3;
    public static final int MESSAGE_ERROR = 4;
    public static final int MESSAGE_TRACE = 5;

    public static byte[] mImageFP = null;    
    public static int mImageWidth = 0;
    public static int mImageHeight = 0;

    private FPScan mFPScan = null;   
    //
    public static boolean mUsbHostMode = true;

    // Intent request codes
    private UsbDeviceDataExchangeImpl usb_host_ctx = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFrame = true;
        mUsbHostMode = true;
    	mLFD = mInvertImage = false;    	
    	mButtonScan = (Button) findViewById(R.id.btnScan);
        mButtonStop = (Button) findViewById(R.id.btnStop);
        mButtonSave = (Button) findViewById(R.id.btnSave);
       // mText =(TextView) findViewById(R.id.text);

        usb_host_ctx = new UsbDeviceDataExchangeImpl(this, mHandler);

        mButtonScan.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {	        		
	        		if( mFPScan != null )
	        		{
	        			mStop = true;
	        			mFPScan.stop();
	        			mText.setText("Stop");
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
			        	        //mCheckUsbHostMode.setEnabled(false);
			        	        mButtonStop.setEnabled(true);
			        	        mText.setText("Start");
			        		}	
		                }
		            	else
		            	{
		            		if(!usb_host_ctx.IsPendingOpen())
		            		{
		            			//mText.setText("Can not start scan operation.\nCan't open scanner device");
		            		}
		            	}    
	        		}
	        		else
	        		{
	        			if( StartScan() )
		        		{
		        			mButtonScan.setEnabled(false);
		        	        mButtonSave.setEnabled(false);
		        	        //mCheckUsbHostMode.setEnabled(false);
		        	        mButtonStop.setEnabled(true);
		        		}	
	        		}
        		}
        });
        
        mButtonStop.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
	        		mStop = true;	       
	        		if( mFPScan != null )
	        		{
	        			mFPScan.stop();
	        			mFPScan = null;
       			
	        		}	        		
	        		mButtonScan.setEnabled(true);
	        		mButtonSave.setEnabled(true);
	        		//mCheckUsbHostMode.setEnabled(true);
	        		mButtonStop.setEnabled(false);	        		
        		}
        });
    }
    @Override
	protected void onDestroy() {
		super.onDestroy();
		mStop = true;	       
		if( mFPScan != null )
		{
			mFPScan.stop();
			mFPScan = null;
		}
		usb_host_ctx.CloseDevice();
		usb_host_ctx.Destroy();
		usb_host_ctx = null;
    }

	private boolean StartScan()
    {
		mFPScan = new FPScan(usb_host_ctx, mHandler);
		mFPScan.start();		
		return true;
    }
	
	private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_SHOW_MSG:            	
            	String showMsg = (String) msg.obj;
                mMessage.setText(showMsg);
                break;
                /*
            case MESSAGE_SHOW_SCANNER_INFO:            	
            	String showInfo = (String) msg.obj;
                mScannerInfo.setText(showInfo);
                break;
            case MESSAGE_SHOW_IMAGE:
            	ShowBitmap();
                break; 
                */
            case MESSAGE_ERROR:
           		//mFPScan = null;
            	mButtonScan.setEnabled(true);
            	//mCheckUsbHostMode.setEnabled(true);
            	mButtonStop.setEnabled(false);
            	break;
            case UsbDeviceDataExchangeImpl.MESSAGE_ALLOW_DEVICE:
            	if(usb_host_ctx.ValidateContext())
            	{
            		if( StartScan() )
	        		{
	        			mButtonScan.setEnabled(false);
	        	        mButtonSave.setEnabled(false);
	        	        //mCheckUsbHostMode.setEnabled(false);
	        	        mButtonStop.setEnabled(true);
	        		}	
            	}
            	else
            		mMessage.setText("Can't open scanner device");
            	break;           
	        case UsbDeviceDataExchangeImpl.MESSAGE_DENY_DEVICE:
            	mMessage.setText("User deny scanner device");
            	break;
            }
        }
    };
    
}