// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) ansi 

package com.futronictech;

import android.os.*;
import android.util.Log;

// Referenced classes of package com.futronictech:
//            UsbDeviceDataExchangeImpl, Scanner, FtrScanDemoUsbHostActivity

public class FPScan
{
    private class ScanThread extends Thread
    {

        public void cancel()
        {
            FtrScanDemoUsbHostActivity.mStop = true;
            try
            {
                join();
                return;
            }
            catch(InterruptedException interruptedexception)
            {
                interruptedexception.printStackTrace();
            }
        }

        public void run()
        {
            do
            {
                long l;
                if(FtrScanDemoUsbHostActivity.mStop)
                    if(FtrScanDemoUsbHostActivity.mUsbHostMode)
                    {
                        devScan.CloseDeviceUsbHost();
                        return;
                    } else
                    {
                        devScan.CloseDevice();
                        return;
                    }
                if(!bGetInfo)
                {
                    Log.i("FUTRONIC", "Run fp scan");
                    boolean flag1;
                    if(FtrScanDemoUsbHostActivity.mUsbHostMode)
                        flag1 = devScan.OpenDeviceOnInterfaceUsbHost(ctx);
                    else
                        flag1 = devScan.OpenDevice();
                    if(!flag1)
                    {
                        mHandler.obtainMessage(1, -1, -1, devScan.GetErrorMessage()).sendToTarget();
                        mHandler.obtainMessage(4).sendToTarget();
                        return;
                    }
                    if(!devScan.GetImageSize())
                    {
                        mHandler.obtainMessage(1, -1, -1, devScan.GetErrorMessage()).sendToTarget();
                        if(FtrScanDemoUsbHostActivity.mUsbHostMode)
                            devScan.CloseDeviceUsbHost();
                        else
                            devScan.CloseDevice();
                        mHandler.obtainMessage(4).sendToTarget();
                        return;
                    }
                    FtrScanDemoUsbHostActivity.mImageWidth = devScan.GetImageWidth();
                    FtrScanDemoUsbHostActivity.mImageHeight = devScan.GetImaegHeight();
                    FtrScanDemoUsbHostActivity.mImageFP = new byte[FtrScanDemoUsbHostActivity.mImageWidth * FtrScanDemoUsbHostActivity.mImageHeight];
                    strInfo = devScan.GetVersionInfo();
                    mHandler.obtainMessage(2, -1, -1, strInfo).sendToTarget();
                    bGetInfo = true;
                }
                flag = 0;
                devScan.getClass();
                devScan.getClass();
                mask = 65;
                if(FtrScanDemoUsbHostActivity.mLFD)
                {
                    int j1 = flag;
                    devScan.getClass();
                    flag = j1 | 1;
                }
                if(FtrScanDemoUsbHostActivity.mInvertImage)
                {
                    int i1 = flag;
                    devScan.getClass();
                    flag = i1 | 0x40;
                }
                if(!devScan.SetOptions(mask, flag))
                    mHandler.obtainMessage(1, -1, -1, devScan.GetErrorMessage()).sendToTarget();
                l = SystemClock.uptimeMillis();
                if(FtrScanDemoUsbHostActivity.mFrame)
                    bRet = devScan.GetFrame(FtrScanDemoUsbHostActivity.mImageFP);
                else
                    bRet = devScan.GetImage2(4, FtrScanDemoUsbHostActivity.mImageFP);
                if(!bRet)
                {
                    mHandler.obtainMessage(1, -1, -1, devScan.GetErrorMessage()).sendToTarget();
                    errCode = devScan.GetErrorCode();
                    int i = errCode;
                    devScan.getClass();
                    if(i != 4306)
                    {
                        int j = errCode;
                        devScan.getClass();
                        if(j != 0x20000001)
                        {
                            int k = errCode;
                            devScan.getClass();
                            if(k != 0x20000002)
                            {
                                if(FtrScanDemoUsbHostActivity.mUsbHostMode)
                                    devScan.CloseDeviceUsbHost();
                                else
                                    devScan.CloseDevice();
                                mHandler.obtainMessage(4).sendToTarget();
                                return;
                            }
                        }
                    }
                } else
                {
                    InterruptedException interruptedexception;
                    if(FtrScanDemoUsbHostActivity.mFrame)
                    {
                        Object aobj1[] = new Object[1];
                        aobj1[0] = Long.valueOf(SystemClock.uptimeMillis() - l);
                        strInfo = String.format("OK. GetFrame time is %d(ms)", aobj1);
                    } else
                    {
                        Object aobj[] = new Object[1];
                        aobj[0] = Long.valueOf(SystemClock.uptimeMillis() - l);
                        strInfo = String.format("OK. GetImage2 time is %d(ms)", aobj);
                    }
                    mHandler.obtainMessage(1, -1, -1, strInfo).sendToTarget();
                }
                mHandler.obtainMessage(3).sendToTarget();
                try
                {
                    Thread.sleep(100L);
                }
                // Misplaced declaration of an exception variable
                catch(InterruptedException interruptedexception)
                {
                    interruptedexception.printStackTrace();
                }
            } while(true);
        }

        private boolean bGetInfo;
        private boolean bRet;
        private Scanner devScan;
        private int errCode;
        private int flag;
        private int mask;
        private String strInfo;
        final FPScan this$0;

        public ScanThread()
        {
            this$0 = FPScan.this;
            super();
            devScan = null;
            bGetInfo = false;
            devScan = new Scanner();
        }
    }


    public FPScan(UsbDeviceDataExchangeImpl usbdevicedataexchangeimpl, Handler handler)
    {
        ctx = null;
        mHandler = handler;
        ctx = usbdevicedataexchangeimpl;
    }

    public void start()
    {
        this;
        JVM INSTR monitorenter ;
        if(mScanThread == null)
        {
            mScanThread = new ScanThread();
            mScanThread.start();
        }
        this;
        JVM INSTR monitorexit ;
        return;
        Exception exception;
        exception;
        throw exception;
    }

    public void stop()
    {
        this;
        JVM INSTR monitorenter ;
        if(mScanThread != null)
        {
            mScanThread.cancel();
            mScanThread = null;
        }
        this;
        JVM INSTR monitorexit ;
        return;
        Exception exception;
        exception;
        throw exception;
    }

    private UsbDeviceDataExchangeImpl ctx;
    private final Handler mHandler;
    private ScanThread mScanThread;


}
