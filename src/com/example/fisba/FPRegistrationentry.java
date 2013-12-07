package com.example.fisba;

import com.futronictech.Scanner;
import com.futronictech.UsbDeviceDataExchangeImpl;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

public class FPRegistrationentry {
    private final Handler mHandler;
    private ScanThread mScanThread;
    private UsbDeviceDataExchangeImpl ctx = null;
    
    public FPRegistrationentry(UsbDeviceDataExchangeImpl context, Handler handler ) {
        mHandler = handler;
        ctx = context;
    }

    public synchronized void start() {
        if (mScanThread == null) {
        	mScanThread = new ScanThread();
        	mScanThread.start();
        }
    }
    
    public synchronized void stop() {
        if (mScanThread != null) {mScanThread.cancel(); mScanThread = null;}
    }
    
    private class ScanThread extends Thread {
    	private boolean bGetInfo;
    	private Scanner devScan = null;
    	private String strInfo;
    	private int mask, flag;
    	private int errCode;
    	private boolean bRet;
    	
        public ScanThread() {
        	bGetInfo=false;
        	devScan = new Scanner();
        	/******************************************
        	// By default, a directory of "/mnt/sdcard/Android/" is necessary for libftrScanAPI.so to work properly
        	// in case you want to change it, you can set it by calling the below function
        	String SyncDir =  "/mnt/sdcard/test/";  // YOUR DIRECTORY
        	if( !devScan.SetGlobalSyncDir(SyncDir) )
        	{
                mHandler.obtainMessage(FtrScanDemoActivity.MESSAGE_SHOW_MSG, -1, -1, devScan.GetErrorMessage()).sendToTarget();
                mHandler.obtainMessage(FtrScanDemoActivity.MESSAGE_ERROR).sendToTarget();
           	    devScan = null;
	            return;
        	}
        	*******************************************/
        }

        public void run() {
            while (!FingerprintRegistrationentry.mStop) 
            {
            	if(!bGetInfo)
            	{
            		Log.i("FUTRONIC", "Run fp scan");
            		boolean bRet;
         	        if( FingerprintRegistrationentry.mUsbHostMode )
         	        	bRet = devScan.OpenDeviceOnInterfaceUsbHost(ctx);
         	        else
         	        	bRet = devScan.OpenDevice();
                    if( !bRet )
                    {
                        //Toast.makeText(this, strInfo, Toast.LENGTH_LONG).show();
                        mHandler.obtainMessage(FingerprintRegistrationentry.MESSAGE_SHOW_MSG, -1, -1, devScan.GetErrorMessage()).sendToTarget();
                        mHandler.obtainMessage(FingerprintRegistrationentry.MESSAGE_ERROR).sendToTarget();
                        return;
                    }
            		
            		if( !devScan.GetImageSize() )
	    	        {
	    	        	mHandler.obtainMessage(FingerprintRegistrationentry.MESSAGE_SHOW_MSG, -1, -1, devScan.GetErrorMessage()).sendToTarget();
	    	        	if( FingerprintRegistrationentry.mUsbHostMode )
	    	        		devScan.CloseDeviceUsbHost();
	    	        	else
	    	        		devScan.CloseDevice();
                        mHandler.obtainMessage(FingerprintRegistrationentry.MESSAGE_ERROR).sendToTarget();
	    	            return;
	    	        }
	    	        FingerprintRegistrationentry.mImageWidth = devScan.GetImageWidth();
	    	        FingerprintRegistrationentry.mImageHeight = devScan.GetImaegHeight();
	                //allocate the buffer before calling GetFrame
	                FingerprintRegistrationentry.mImageFP = new byte[FingerprintRegistrationentry.mImageWidth*FingerprintRegistrationentry.mImageHeight];
	    	        strInfo = devScan.GetVersionInfo();
    	        	mHandler.obtainMessage(FingerprintRegistrationentry.MESSAGE_SHOW_SCANNER_INFO, -1, -1, strInfo).sendToTarget();
	    	        bGetInfo = true;            	
             	}
                //set options
                flag = 0;
                mask = devScan.FTR_OPTIONS_DETECT_FAKE_FINGER | devScan.FTR_OPTIONS_INVERT_IMAGE;             
                if( !devScan.SetOptions(mask, flag) )
    	        	mHandler.obtainMessage(FingerprintRegistrationentry.MESSAGE_SHOW_MSG, -1, -1, devScan.GetErrorMessage()).sendToTarget();
                // get frame / image2
                long lT1 = SystemClock.uptimeMillis();
                if( FingerprintRegistrationentry.mFrame )
                	bRet = devScan.GetFrame(FingerprintRegistrationentry.mImageFP);
                else
                	bRet = devScan.GetImage2(4,FingerprintRegistrationentry.mImageFP);
                if( !bRet )
                {
                	mHandler.obtainMessage(FingerprintRegistrationentry.MESSAGE_SHOW_MSG, -1, -1, devScan.GetErrorMessage()).sendToTarget();
                	errCode = devScan.GetErrorCode();
                	if( errCode != devScan.FTR_ERROR_EMPTY_FRAME && errCode != devScan.FTR_ERROR_MOVABLE_FINGER &&  errCode != devScan.FTR_ERROR_NO_FRAME )
                	{
	    	        	if( FingerprintRegistrationentry.mUsbHostMode )
	    	        		devScan.CloseDeviceUsbHost();
	    	        	else
	    	        		devScan.CloseDevice();
                        mHandler.obtainMessage(FingerprintRegistrationentry.MESSAGE_ERROR).sendToTarget();
	    	            return;                		
                	}    	        	
                }
                else
                {
                	if( FingerprintRegistrationentry.mFrame ) 
                		strInfo = String.format("OK. GetFrame time is %d(ms)",  SystemClock.uptimeMillis()-lT1);
                	else
                		strInfo = String.format("OK. GetImage2 time is %d(ms)",  SystemClock.uptimeMillis()-lT1);
                	mHandler.obtainMessage(FingerprintRegistrationentry.MESSAGE_SHOW_MSG, -1, -1, strInfo ).sendToTarget();
                }
                //show image
            	mHandler.obtainMessage(FingerprintRegistrationentry.MESSAGE_SHOW_IMAGE).sendToTarget();
                try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            //close device
        	if( FingerprintRegistrationentry.mUsbHostMode )
        		devScan.CloseDeviceUsbHost();
        	else
        		devScan.CloseDevice();          
        }

        public void cancel() {
        	FingerprintRegistrationentry.mStop = true;
        	        	       	        	
        	try {
				this.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
       	      	           	
        }
    }    
}
