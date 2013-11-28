// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) ansi 

package com.futronictech;


public class ftrWsqAndroidHelper
{

    public ftrWsqAndroidHelper()
    {
        mHeight = 0;
        mWidth = 0;
        mWSQ_size = 0;
        mBMP_size = 0;
        mRAW_size = 0;
        mDPI = 0;
        mBitrate = 0.75F;
    }

    private native boolean JNIGetImageParameters(byte abyte0[]);

    private native boolean JNIRawToWsqImage(long l, int i, int j, float f, byte abyte0[], byte abyte1[]);

    private native boolean JNIWsqToRawImage(byte abyte0[], byte abyte1[]);

    public boolean ConvertRawToWsq(long l, int i, int j, float f, byte abyte0[], byte abyte1[])
    {
        while(abyte0.length != i * j || abyte1.length != i * j || (double)f > 2.25D || (double)f < 0.75D) 
            return false;
        return JNIRawToWsqImage(l, i, j, f, abyte0, abyte1);
    }

    public boolean ConvertWsqToRaw(byte abyte0[], byte abyte1[])
    {
        if(abyte1.length < mWidth * mHeight)
            return false;
        else
            return JNIWsqToRawImage(abyte0, abyte1);
    }

    public int GetWsqImageRawSize(byte abyte0[])
    {
        if(!JNIGetImageParameters(abyte0))
            return 0;
        else
            return mWidth * mHeight;
    }

    public int mBMP_size;
    public float mBitrate;
    public int mDPI;
    public int mHeight;
    public int mRAW_size;
    public int mWSQ_size;
    public int mWidth;

    static 
    {
        System.loadLibrary("usb-1.0");
        System.loadLibrary("ftrScanAPI");
        System.loadLibrary("ftrWSQAndroid");
        System.loadLibrary("ftrWSQAndroidJni");
    }
}
