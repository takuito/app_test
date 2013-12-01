package com.example.fisba;

import com.futronictech.FPScan;
import com.futronictech.Scanner;
import com.futronictech.UsbDeviceDataExchangeImpl;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class FingerScan extends Activity {
    /** Called when the activity is first created. */
	private static Button mButtonCancel;
	private static Button mButtonScanStart;
	
	private static TextView mMessage;
	
	private Scanner devScan = null;
	private boolean bGetInfo;
	private String strInfo;
	private int mask, flag;
	private int errCode;
	private boolean bRet;

	public static byte[] mImageFP = null;    
    public static int mImageWidth = 0;
    public static int mImageHeight = 0;
    private static Bitmap mBitmapFP = null;
    
    private static ImageView mFingerImage;
    
    public static final int MESSAGE_SHOW_MSG = 1;
    public static final int MESSAGE_SHOW_SCANNER_INFO = 2;
    public static final int MESSAGE_SHOW_IMAGE = 3;
    public static final int MESSAGE_ERROR = 4;
    public static final int MESSAGE_TRACE = 5;
    
	private FPScan mFPScan = null;
	private UsbDeviceDataExchangeImpl usb_host_ctx = null;
	
	
	public static boolean mStop = true;
	public static boolean mUsbHostMode = true;
	public static boolean mFrame = true;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finger_scan);
        
    	mButtonCancel = (Button) findViewById(R.id.btnCancel);
    	mFingerImage = (ImageView) findViewById(R.id.imageFinger);
    	mMessage = (TextView) findViewById(R.id.tvMessage);
    	mButtonScanStart = (Button) findViewById(R.id.btnScanStart);
    	
    	usb_host_ctx = new UsbDeviceDataExchangeImpl(this, mHandler);

    	mButtonScanStart.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
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
	        			
	        		}	
        		}
            }
        });
    
    
    mButtonCancel.setOnClickListener(new OnClickListener() {
        public void onClick(View v) {
        	finish();
        }
    });

    }
	private boolean StartScan()
    {
        		if( !devScan.GetImageSize() )
    	        {/*
    	        	mHandler.obtainMessage(MESSAGE_SHOW_MSG, -1, -1, devScan.GetErrorMessage()).sendToTarget();
    	        	if( mUsbHostMode )
    	        		devScan.CloseDeviceUsbHost();
    	        	else
    	        		devScan.CloseDevice();
                    mHandler.obtainMessage(MESSAGE_ERROR).sendToTarget();
    	            return false;*/
    	        }/*
    	        mImageWidth = devScan.GetImageWidth();
    	        mImageHeight = devScan.GetImaegHeight();
                //allocate the buffer before calling GetFrame
                mImageFP = new byte[mImageWidth*mImageHeight];
    	        strInfo = devScan.GetVersionInfo();
	        	mHandler.obtainMessage(MESSAGE_SHOW_SCANNER_INFO, -1, -1, strInfo).sendToTarget();
    	        //bGetInfo = true;            	
         	//}
            //set options
            flag = 0;
            mask = devScan.FTR_OPTIONS_DETECT_FAKE_FINGER | devScan.FTR_OPTIONS_INVERT_IMAGE;
            
            // get frame / image2
            long lT1 = SystemClock.uptimeMillis();
            if( mFrame )
            	bRet = devScan.GetFrame(mImageFP);
            else
            	bRet = devScan.GetImage2(4,mImageFP);
            if( !bRet )
            {
            	mHandler.obtainMessage(MESSAGE_SHOW_MSG, -1, -1, devScan.GetErrorMessage()).sendToTarget();
            	errCode = devScan.GetErrorCode();
            	if( errCode != devScan.FTR_ERROR_EMPTY_FRAME && errCode != devScan.FTR_ERROR_MOVABLE_FINGER &&  errCode != devScan.FTR_ERROR_NO_FRAME )
            	{
    	        	if( mUsbHostMode )
    	        		devScan.CloseDeviceUsbHost();
    	        	else
    	        		devScan.CloseDevice();
                    mHandler.obtainMessage(MESSAGE_ERROR).sendToTarget();
    	            return false;                		
            	}    	        	
            }
            else
            {
            	if( mFrame ) 
            		strInfo = String.format("OK. GetFrame time is %d(ms)",  SystemClock.uptimeMillis()-lT1);
            	else
            		strInfo = String.format("OK. GetImage2 time is %d(ms)",  SystemClock.uptimeMillis()-lT1);
            	//mHandler.obtainMessage(MESSAGE_SHOW_MSG, -1, -1, strInfo ).sendToTarget();
            }*/
            return true;
		
		/*
		mFPScan = new FPScan(usb_host_ctx, mHandler);
		mFPScan.start();
		return true;
		*/
    }
	
    private static void ShowBitmap()
    {
    	int[] pixels = new int[mImageWidth * mImageHeight];
    	for( int i=0; i<mImageWidth * mImageHeight; i++)
    		pixels[i] = mImageFP[i];
    	Bitmap emptyBmp = Bitmap.createBitmap(pixels, mImageWidth, mImageHeight, Config.RGB_565);

        int width, height; 
        height = emptyBmp.getHeight(); 
        width = emptyBmp.getWidth();     
     
        mBitmapFP = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565); 
        Canvas c = new Canvas(mBitmapFP); 
        Paint paint = new Paint(); 
        ColorMatrix cm = new ColorMatrix(); 
        cm.setSaturation(0); 
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm); 
        paint.setColorFilter(f); 
        c.drawBitmap(emptyBmp, 0, 0, paint); 
        
        mFingerImage.setImageBitmap(mBitmapFP);
    }
    
	// The Handler that gets information back from the FPScan
		private final Handler mHandler = new Handler() {
	        @Override
	        public void handleMessage(Message msg) {
	            switch (msg.what) {
	            case MESSAGE_SHOW_MSG:            	
	            	String showMsg = (String) msg.obj;
	                //mMessage.setText(showMsg);
	                break;
	            case MESSAGE_SHOW_SCANNER_INFO:            	
	            	String showInfo = (String) msg.obj;
	                //mScannerInfo.setText(showInfo);
	                break;
	            case MESSAGE_SHOW_IMAGE:
	            	ShowBitmap();
	                break;
	            case MESSAGE_ERROR:
	           		////mFPScan = null;
	            	//mButtonScan.setEnabled(true);
	            	//mCheckUsbHostMode.setEnabled(true);
	            	//mButtonStop.setEnabled(false);
	            	break;
	            case UsbDeviceDataExchangeImpl.MESSAGE_ALLOW_DEVICE:
	            	if(usb_host_ctx.ValidateContext())
	            	{
	            		if( StartScan() )
		        		{
		        			//mButtonScan.setEnabled(false);
		        	        //mButtonSave.setEnabled(false);
		        	        //mCheckUsbHostMode.setEnabled(false);
		        	        //mButtonStop.setEnabled(true);
		        		}	
	            	}
	            	else
	            		//mMessage.setText("Can't open scanner device");
	            	break;           
		        case UsbDeviceDataExchangeImpl.MESSAGE_DENY_DEVICE:
	            	//mMessage.setText("User deny scanner device");
	            	break;
	            }
	        }
	    };
}