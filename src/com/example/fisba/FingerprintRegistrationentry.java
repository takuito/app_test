package com.example.fisba;

//import com.futronictech.FPScan;
import com.futronictech.Scanner;
import com.futronictech.UsbDeviceDataExchangeImpl;
import com.futronictech.ftrWsqAndroidHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class FingerprintRegistrationentry extends Activity {
    /** Called when the activity is first created. */
	private static Button mButtonCancel;
	private static Button mButtonScanStart;
	private static Button mButtonSave;
	
	private static TextView mMessage;
	private static TextView mScannerInfo;
	private static TextView mScannerInfo2;
	private static TextView mScannerInfo3;
	
	private Scanner devScan = null;


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
    
	private FPRegistrationentry mFPScan = null;
	
	// Intent request codes
    private static final int REQUEST_FILE_FORMAT = 1;
	private UsbDeviceDataExchangeImpl usb_host_ctx = null;
	
	
	public static boolean mStop = true;
	public static boolean mUsbHostMode = true;
	public static boolean mFrame = true;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.fingerprint_registrationentry);
        
    	mButtonCancel = (Button) findViewById(R.id.btnCancel);
    	mFingerImage = (ImageView) findViewById(R.id.imageFinger);
    	mMessage = (TextView) findViewById(R.id.tvMessage);
    	mButtonScanStart = (Button) findViewById(R.id.btnScanStart);
    	mButtonSave = (Button) findViewById(R.id.btnSave);
    	mScannerInfo = (TextView) findViewById(R.id.tvScannerInfo);
    	mScannerInfo2 = (TextView) findViewById(R.id.tvScannerInfo2);
    	mScannerInfo3 = (TextView) findViewById(R.id.tvScannerInfo3);
    	
    	usb_host_ctx = new UsbDeviceDataExchangeImpl(FingerprintRegistrationentry.this, mHandler);

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
	        			Toast.makeText(FingerprintRegistrationentry.this, "true",Toast.LENGTH_LONG).show();
	        			if( StartScan() )
		        		{
	        				mButtonScanStart.setEnabled(true);
	    	            	//mCheckUsbHostMode.setEnabled(true);
	    	            	mButtonCancel.setEnabled(false);
		        		}	
	                }
	            	else
	            	{
	            		if(!usb_host_ctx.IsPendingOpen())
	            		{
	            			Toast.makeText(FingerprintRegistrationentry.this, "false",Toast.LENGTH_LONG).show();
	            			mMessage.setText("Can not start scan operation.\nCan't open scanner device");
	            		}
	            	}    
        		}
        		else
        		{
        			if( StartScan() )
	        		{
        				mButtonScanStart.setEnabled(true);
    	            	//mCheckUsbHostMode.setEnabled(true);
    	            	mButtonCancel.setEnabled(false);
	        		}	
        		}
            }
        });
    
        mButtonSave.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {   
            	if( mImageFP != null)
            		SaveImage();
            	}
        });
    
        mButtonCancel.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		mStop = true;	       
        		if( mFPScan != null )
        		{
        			mFPScan.stop();
        			mFPScan = null;
   			
        		}	        		
        		mButtonScanStart.setEnabled(true);
        		mButtonSave.setEnabled(true);
        		//mCheckUsbHostMode.setEnabled(true);
        		mButtonCancel.setEnabled(false);
        		finish();
        		}
        	});
        }
    
	private boolean StartScan()
    {
		mFPScan = new FPRegistrationentry(usb_host_ctx, mHandler);
		mFPScan.start();
		return true;
    }
	
    private void SaveImage()
    {
    	Intent serverIntent = new Intent(FingerprintRegistrationentry.this, SelectFileFormatActivity.class);
	    startActivityForResult(serverIntent, 1);
    }
    
    private void SaveImageByFileFormat(String fileFormat, String fileName)
    {
 	   	if( fileFormat.compareTo("WSQ") == 0 )	//save wsq file
    	{

    		Scanner devScan = new Scanner();
    		boolean bRet;
    		if( mUsbHostMode )
    			bRet = devScan.OpenDeviceOnInterfaceUsbHost(usb_host_ctx);
    		else
    			bRet = devScan.OpenDevice();
    		if( !bRet )
    		{
                mMessage.setText(devScan.GetErrorMessage());
                return;    			
    		}
    		byte[] wsqImg = new byte[mImageWidth*mImageHeight];
    		long hDevice = devScan.GetDeviceHandle();
    		ftrWsqAndroidHelper wsqHelper = new ftrWsqAndroidHelper();
    		if( wsqHelper.ConvertRawToWsq(hDevice, mImageWidth, mImageHeight, 2.25f, mImageFP, wsqImg) )
    		{  			
    	        File file = new File(fileName);                
    	        try {
    	            FileOutputStream out = new FileOutputStream(file);                    
    	            out.write(wsqImg, 0, wsqHelper.mWSQ_size);	// save the wsq_size bytes data to file
    	            out.close();
    	            mMessage.setText("Image is saved as " + fileName);
    	            
    	         } catch (Exception e) { 
    	        	 mMessage.setText("Exception in saving file"); 
    	         }
    		}
    		else
    			mMessage.setText("Failed to convert the image!");
    		if( mUsbHostMode )
    			devScan.CloseDeviceUsbHost();
    		else
    			devScan.CloseDevice();
    		return;
    	}
    	// 0 - save bitmap file 
        File file = new File(fileName);                
        try {
            FileOutputStream out = new FileOutputStream(file);
            //mBitmapFP.compress(Bitmap.CompressFormat.PNG, 90, out);
            MyBitmapFile fileBMP = new MyBitmapFile(mImageWidth, mImageHeight, mImageFP);
            out.write(fileBMP.toBytes());
            out.close();
            mScannerInfo2.setText("Image is saved as " + fileName);
            
            /*
            FileOutputStream outStream = openFileOutput(fileName, MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(outStream);
            writer.write(fileNum);
            writer.flush();
            writer.close();
            
            FileInputStream file_read = openFileInput(fileName);
            in = new BufferedReader(new InputStreamReader(file_read));
            mMessage.setText(in.readLine());
            mScannerInfo2.setText("test"+in.readLine());
            Toast.makeText(FingerScan.this, String.format("%s", in.readLine()),Toast.LENGTH_LONG).show();
            in.close();
            */
         } catch (Exception e) { 
        	 mMessage.setText("Exception in saving file"); 
         } 
        
        
        /*
        try {
            // ストリームを開く
        	FileOutputStream outStream = openFileOutput("test.txt", MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(outStream);
            writer.write(fileName);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        try {
            // ストリームを開く
        	FileOutputStream outStream = openFileOutput("test.txt", MODE_APPEND);
            OutputStreamWriter writer = new OutputStreamWriter(outStream);
            writer.write(fileName + ",");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        //デバッグ用
        BufferedReader in = null;
        try {
          FileInputStream fileRead = openFileInput("test.txt");
          in = new BufferedReader(new InputStreamReader(fileRead));
          mScannerInfo3.setText("test:"+in.readLine());
          in.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
        
        
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
	                mMessage.setText(showMsg);
	                break;
	            case MESSAGE_SHOW_SCANNER_INFO:            	
	            	String showInfo = (String) msg.obj;
	                mScannerInfo.setText(showInfo);
	                break;
	            case MESSAGE_SHOW_IMAGE:
	            	ShowBitmap();
	                break;
	            case MESSAGE_ERROR:
	           		////mFPScan = null;
	            	mButtonScanStart.setEnabled(true);
	            	//mCheckUsbHostMode.setEnabled(true);
	            	mButtonCancel.setEnabled(false);
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
	            		mMessage.setText("Can't open scanner device");
	            	break;           
		        case UsbDeviceDataExchangeImpl.MESSAGE_DENY_DEVICE:
	            	mMessage.setText("User deny scanner device");
	            	break;
	            }
	        }
	    };
	    
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
	    

	    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	        switch (requestCode) {
	         case REQUEST_FILE_FORMAT:
				 if (resultCode == Activity.RESULT_OK) {
				     // Get the file format
					 String[] extraString = data.getExtras().getStringArray(SelectFileFormatActivity.EXTRA_FILE_FORMAT);
					 String fileFormat = extraString[0];
					 String fileName = extraString[1];
					 SaveImageByFileFormat(fileFormat, fileName);
	             }
				 else
					 mMessage.setText("Cancelled!");
	             break;            
	        }
	    }

}