// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) ansi 

package com.futronictech;

import android.app.Activity;
import android.content.Intent;
import android.graphics.*;
import android.os.*;
import android.view.View;
import android.widget.*;
import java.io.File;
import java.io.FileOutputStream;

// Referenced classes of package com.futronictech:
//            SelectFileFormatActivity, Scanner, ftrWsqAndroidHelper, MyBitmapFile, 
//            FPScan, UsbDeviceDataExchangeImpl

public class FtrScanDemoUsbHostActivity extends Activity
{

    public FtrScanDemoUsbHostActivity()
    {
        mFPScan = null;
        usb_host_ctx = null;
    }

    private void SaveImage()
    {
        startActivityForResult(new Intent(this, com/futronictech/SelectFileFormatActivity), 1);
    }

    private void SaveImageByFileFormat(String s, String s1)
    {
        if(s.compareTo("WSQ") == 0)
        {
            Scanner scanner = new Scanner();
            boolean flag;
            if(mUsbHostMode)
                flag = scanner.OpenDeviceOnInterfaceUsbHost(usb_host_ctx);
            else
                flag = scanner.OpenDevice();
            if(!flag)
            {
                mMessage.setText(scanner.GetErrorMessage());
                return;
            }
            byte abyte0[] = new byte[mImageWidth * mImageHeight];
            long l = scanner.GetDeviceHandle();
            ftrWsqAndroidHelper ftrwsqandroidhelper = new ftrWsqAndroidHelper();
            if(ftrwsqandroidhelper.ConvertRawToWsq(l, mImageWidth, mImageHeight, 2.25F, mImageFP, abyte0))
            {
                File file = new File(s1);
                try
                {
                    FileOutputStream fileoutputstream = new FileOutputStream(file);
                    fileoutputstream.write(abyte0, 0, ftrwsqandroidhelper.mWSQ_size);
                    fileoutputstream.close();
                    mMessage.setText((new StringBuilder("Image is saved as ")).append(s1).toString());
                }
                catch(Exception exception)
                {
                    mMessage.setText("Exception in saving file");
                }
            } else
            {
                mMessage.setText("Failed to convert the image!");
            }
            if(mUsbHostMode)
            {
                scanner.CloseDeviceUsbHost();
                return;
            } else
            {
                scanner.CloseDevice();
                return;
            }
        }
        File file1 = new File(s1);
        try
        {
            FileOutputStream fileoutputstream1 = new FileOutputStream(file1);
            fileoutputstream1.write((new MyBitmapFile(mImageWidth, mImageHeight, mImageFP)).toBytes());
            fileoutputstream1.close();
            mMessage.setText((new StringBuilder("Image is saved as ")).append(s1).toString());
            return;
        }
        catch(Exception exception1)
        {
            mMessage.setText("Exception in saving file");
        }
    }

    private static void ShowBitmap()
    {
        int ai[] = new int[mImageWidth * mImageHeight];
        int i = 0;
        do
        {
            if(i >= mImageWidth * mImageHeight)
            {
                Bitmap bitmap = Bitmap.createBitmap(ai, mImageWidth, mImageHeight, android.graphics.Bitmap.Config.RGB_565);
                int j = bitmap.getHeight();
                mBitmapFP = Bitmap.createBitmap(bitmap.getWidth(), j, android.graphics.Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(mBitmapFP);
                Paint paint = new Paint();
                ColorMatrix colormatrix = new ColorMatrix();
                colormatrix.setSaturation(0.0F);
                paint.setColorFilter(new ColorMatrixColorFilter(colormatrix));
                canvas.drawBitmap(bitmap, 0.0F, 0.0F, paint);
                mFingerImage.setImageBitmap(mBitmapFP);
                return;
            }
            ai[i] = mImageFP[i];
            i++;
        } while(true);
    }

    private boolean StartScan()
    {
        mFPScan = new FPScan(usb_host_ctx, mHandler);
        mFPScan.start();
        return true;
    }

    public void onActivityResult(int i, int j, Intent intent)
    {
        switch(i)
        {
        default:
            return;

        case 1: // '\001'
            break;
        }
        if(j == -1)
        {
            String as[] = intent.getExtras().getStringArray(SelectFileFormatActivity.EXTRA_FILE_FORMAT);
            SaveImageByFileFormat(as[0], as[1]);
            return;
        } else
        {
            mMessage.setText("Cancelled!");
            return;
        }
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(0x7f030001);
        mFrame = true;
        mUsbHostMode = true;
        mInvertImage = false;
        mLFD = false;
        mButtonScan = (Button)findViewById(0x7f08000e);
        mButtonStop = (Button)findViewById(0x7f08000f);
        mButtonSave = (Button)findViewById(0x7f080010);
        mMessage = (TextView)findViewById(0x7f080011);
        mScannerInfo = (TextView)findViewById(0x7f080009);
        mFingerImage = (ImageView)findViewById(0x7f080012);
        mCheckFrame = (CheckBox)findViewById(0x7f08000a);
        mCheckLFD = (CheckBox)findViewById(0x7f08000b);
        mCheckInvertImage = (CheckBox)findViewById(0x7f08000c);
        mCheckUsbHostMode = (CheckBox)findViewById(0x7f080013);
        usb_host_ctx = new UsbDeviceDataExchangeImpl(this, mHandler);
        mButtonScan.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                if(mFPScan != null)
                {
                    FtrScanDemoUsbHostActivity.mStop = true;
                    mFPScan.stop();
                }
                FtrScanDemoUsbHostActivity.mStop = false;
                if(!FtrScanDemoUsbHostActivity.mUsbHostMode) goto _L2; else goto _L1
_L1:
                usb_host_ctx.CloseDevice();
                if(!usb_host_ctx.OpenDevice(0, true)) goto _L4; else goto _L3
_L3:
                if(StartScan())
                {
                    FtrScanDemoUsbHostActivity.mButtonScan.setEnabled(false);
                    mButtonSave.setEnabled(false);
                    mCheckUsbHostMode.setEnabled(false);
                    FtrScanDemoUsbHostActivity.mButtonStop.setEnabled(true);
                }
_L6:
                return;
_L4:
                if(!usb_host_ctx.IsPendingOpen())
                {
                    FtrScanDemoUsbHostActivity.mMessage.setText("Can not start scan operation.\nCan't open scanner device");
                    return;
                }
                continue; /* Loop/switch isn't completed */
_L2:
                if(StartScan())
                {
                    FtrScanDemoUsbHostActivity.mButtonScan.setEnabled(false);
                    mButtonSave.setEnabled(false);
                    mCheckUsbHostMode.setEnabled(false);
                    FtrScanDemoUsbHostActivity.mButtonStop.setEnabled(true);
                    return;
                }
                if(true) goto _L6; else goto _L5
_L5:
            }

            final FtrScanDemoUsbHostActivity this$0;

            
            {
                this$0 = FtrScanDemoUsbHostActivity.this;
                super();
            }
        }
);
        mButtonStop.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                FtrScanDemoUsbHostActivity.mStop = true;
                if(mFPScan != null)
                {
                    mFPScan.stop();
                    mFPScan = null;
                }
                FtrScanDemoUsbHostActivity.mButtonScan.setEnabled(true);
                mButtonSave.setEnabled(true);
                mCheckUsbHostMode.setEnabled(true);
                FtrScanDemoUsbHostActivity.mButtonStop.setEnabled(false);
            }

            final FtrScanDemoUsbHostActivity this$0;

            
            {
                this$0 = FtrScanDemoUsbHostActivity.this;
                super();
            }
        }
);
        mButtonSave.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                if(FtrScanDemoUsbHostActivity.mImageFP != null)
                    SaveImage();
            }

            final FtrScanDemoUsbHostActivity this$0;

            
            {
                this$0 = FtrScanDemoUsbHostActivity.this;
                super();
            }
        }
);
        mCheckFrame.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton compoundbutton, boolean flag)
            {
                if(compoundbutton.isChecked())
                {
                    FtrScanDemoUsbHostActivity.mFrame = true;
                    return;
                } else
                {
                    FtrScanDemoUsbHostActivity.mFrame = false;
                    mCheckLFD.setChecked(false);
                    FtrScanDemoUsbHostActivity.mLFD = false;
                    return;
                }
            }

            final FtrScanDemoUsbHostActivity this$0;

            
            {
                this$0 = FtrScanDemoUsbHostActivity.this;
                super();
            }
        }
);
        mCheckLFD.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton compoundbutton, boolean flag)
            {
                if(compoundbutton.isChecked())
                {
                    FtrScanDemoUsbHostActivity.mLFD = true;
                    return;
                } else
                {
                    FtrScanDemoUsbHostActivity.mLFD = false;
                    return;
                }
            }

            final FtrScanDemoUsbHostActivity this$0;

            
            {
                this$0 = FtrScanDemoUsbHostActivity.this;
                super();
            }
        }
);
        mCheckInvertImage.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton compoundbutton, boolean flag)
            {
                if(compoundbutton.isChecked())
                {
                    FtrScanDemoUsbHostActivity.mInvertImage = true;
                    return;
                } else
                {
                    FtrScanDemoUsbHostActivity.mInvertImage = false;
                    return;
                }
            }

            final FtrScanDemoUsbHostActivity this$0;

            
            {
                this$0 = FtrScanDemoUsbHostActivity.this;
                super();
            }
        }
);
        mCheckUsbHostMode.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton compoundbutton, boolean flag)
            {
                if(compoundbutton.isChecked())
                {
                    FtrScanDemoUsbHostActivity.mUsbHostMode = true;
                    return;
                } else
                {
                    FtrScanDemoUsbHostActivity.mUsbHostMode = false;
                    return;
                }
            }

            final FtrScanDemoUsbHostActivity this$0;

            
            {
                this$0 = FtrScanDemoUsbHostActivity.this;
                super();
            }
        }
);
    }

    protected void onDestroy()
    {
        super.onDestroy();
        mStop = true;
        if(mFPScan != null)
        {
            mFPScan.stop();
            mFPScan = null;
        }
        usb_host_ctx.CloseDevice();
        usb_host_ctx.Destroy();
        usb_host_ctx = null;
    }

    public static final int MESSAGE_ERROR = 4;
    public static final int MESSAGE_SHOW_IMAGE = 3;
    public static final int MESSAGE_SHOW_MSG = 1;
    public static final int MESSAGE_SHOW_SCANNER_INFO = 2;
    public static final int MESSAGE_TRACE = 5;
    private static final int REQUEST_FILE_FORMAT = 1;
    private static Bitmap mBitmapFP = null;
    private static Button mButtonScan;
    private static Button mButtonStop;
    private static ImageView mFingerImage;
    public static boolean mFrame = true;
    public static byte mImageFP[] = null;
    public static int mImageHeight = 0;
    public static int mImageWidth = 0;
    public static boolean mInvertImage = false;
    public static boolean mLFD = false;
    private static TextView mMessage;
    private static TextView mScannerInfo;
    public static boolean mStop = false;
    public static boolean mUsbHostMode = true;
    private Button mButtonSave;
    private CheckBox mCheckFrame;
    private CheckBox mCheckInvertImage;
    private CheckBox mCheckLFD;
    private CheckBox mCheckUsbHostMode;
    private FPScan mFPScan;
    private final Handler mHandler = new Handler() {

        public void handleMessage(Message message)
        {
            message.what;
            JVM INSTR lookupswitch 6: default 64
        //                       1: 65
        //                       2: 81
        //                       3: 97
        //                       4: 101
        //                       255: 127
        //                       256: 196;
               goto _L1 _L2 _L3 _L4 _L5 _L6 _L7
_L1:
            return;
_L2:
            String s1 = (String)message.obj;
            FtrScanDemoUsbHostActivity.mMessage.setText(s1);
            return;
_L3:
            String s = (String)message.obj;
            FtrScanDemoUsbHostActivity.mScannerInfo.setText(s);
            return;
_L4:
            FtrScanDemoUsbHostActivity.ShowBitmap();
            return;
_L5:
            FtrScanDemoUsbHostActivity.mButtonScan.setEnabled(true);
            mCheckUsbHostMode.setEnabled(true);
            FtrScanDemoUsbHostActivity.mButtonStop.setEnabled(false);
            return;
_L6:
            if(usb_host_ctx.ValidateContext())
            {
                if(StartScan())
                {
                    FtrScanDemoUsbHostActivity.mButtonScan.setEnabled(false);
                    mButtonSave.setEnabled(false);
                    mCheckUsbHostMode.setEnabled(false);
                    FtrScanDemoUsbHostActivity.mButtonStop.setEnabled(true);
                    return;
                }
            } else
            {
                FtrScanDemoUsbHostActivity.mMessage.setText("Can't open scanner device");
                return;
            }
              goto _L1
_L7:
            FtrScanDemoUsbHostActivity.mMessage.setText("User deny scanner device");
            return;
        }

        final FtrScanDemoUsbHostActivity this$0;

            
            {
                this$0 = FtrScanDemoUsbHostActivity.this;
                super();
            }
    }
;
    private UsbDeviceDataExchangeImpl usb_host_ctx;














}
