// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) ansi 

package com.futronictech;

import android.content.Context;

// Referenced classes of package com.futronictech:
//            UsbDeviceDataExchangeImpl

public class Scanner
{

    public Scanner()
    {
        m_hDevice = 0L;
        m_ImageHeight = 0;
        m_ImageWidth = 0;
    }

    private native boolean OpenDeviceCtx(Object obj);

    public native boolean CloseDevice();

    public void CloseDeviceUsbHost()
    {
        this;
        JVM INSTR monitorenter ;
        CloseDevice();
        this;
        JVM INSTR monitorexit ;
        return;
        Exception exception;
        exception;
        this;
        JVM INSTR monitorexit ;
        throw exception;
    }

    public long GetDeviceHandle()
    {
        return m_hDevice;
    }

    public native boolean GetDiodesStatus(byte abyte0[]);

    public int GetErrorCode()
    {
        return m_ErrorCode;
    }

    public String GetErrorMessage()
    {
        switch(m_ErrorCode)
        {
        default:
            Object aobj[] = new Object[1];
            aobj[0] = Integer.valueOf(m_ErrorCode);
            return String.format("Error code is %d", aobj);

        case 0: // '\0'
            return "OK";

        case 4306: 
            return "Empty Frame";

        case 536870913: 
            return "Moveable Finger";

        case 536870914: 
            return "Fake Finger";

        case 536870916: 
            return "Hardware Incompatible";

        case 536870917: 
            return "Firmware Incompatible";

        case 536870918: 
            return "Invalid Authorization Code";

        case 19: // '\023'
            return "Write Protect";

        case 536870929: 
            return "System libusb error!";
        }
    }

    public native boolean GetFrame(byte abyte0[]);

    public int GetImaegHeight()
    {
        return m_ImageHeight;
    }

    public native boolean GetImage(int i, byte abyte0[]);

    public native boolean GetImage2(int i, byte abyte0[]);

    public native boolean GetImageByVariableDose(int i, byte abyte0[]);

    public native boolean GetImageSize();

    public int GetImageWidth()
    {
        return m_ImageWidth;
    }

    public native boolean GetInterfaces(byte abyte0[]);

    public boolean GetInterfacesUsbHost(Context context, byte abyte0[])
    {
        if(abyte0.length < 128)
        {
            m_ErrorCode = 8;
            return false;
        } else
        {
            UsbDeviceDataExchangeImpl.GetInterfaces(context, abyte0);
            return true;
        }
    }

    public native String GetVersionInfo();

    public native boolean IsFingerPresent();

    public native boolean OpenDevice();

    public native boolean OpenDeviceOnInterface(int i);

    public boolean OpenDeviceOnInterfaceUsbHost(UsbDeviceDataExchangeImpl usbdevicedataexchangeimpl)
    {
        this;
        JVM INSTR monitorenter ;
        boolean flag = OpenDeviceCtx(usbdevicedataexchangeimpl);
        this;
        JVM INSTR monitorexit ;
        return flag;
        Exception exception;
        exception;
        this;
        JVM INSTR monitorexit ;
        throw exception;
    }

    public native boolean Restore7Bytes(byte abyte0[]);

    public native boolean RestoreSecret7Bytes(byte abyte0[], byte abyte1[]);

    public native boolean Save7Bytes(byte abyte0[]);

    public native boolean SaveSecret7Bytes(byte abyte0[], byte abyte1[]);

    public native boolean SetDiodesStatus(int i, int j);

    public native boolean SetGlobalSyncDir(String s);

    public native boolean SetNewAuthorizationCode(byte abyte0[]);

    public native boolean SetOptions(int i, int j);

    public static final byte FTRSCAN_INTERFACE_STATUS_CONNECTED = 0;
    public static final byte FTRSCAN_INTERFACE_STATUS_DISCONNECTED = 1;
    public static final int FTR_MAX_INTERFACE_NUMBER = 128;
    public final int FTR_ERROR_EMPTY_FRAME = 4306;
    public final int FTR_ERROR_FIRMWARE_INCOMPATIBLE = 0x20000005;
    public final int FTR_ERROR_HARDWARE_INCOMPATIBLE = 0x20000004;
    public final int FTR_ERROR_INVALID_AUTHORIZATION_CODE = 0x20000006;
    public final int FTR_ERROR_LIBUSB_ERROR = 0x20000011;
    public final int FTR_ERROR_MOVABLE_FINGER = 0x20000001;
    public final int FTR_ERROR_NOT_ENOUGH_MEMORY = 8;
    public final int FTR_ERROR_NOT_READY = 21;
    public final int FTR_ERROR_NO_ERROR = 0;
    public final int FTR_ERROR_NO_FRAME = 0x20000002;
    public final int FTR_ERROR_WRITE_PROTECT = 19;
    public final int FTR_OPTIONS_CHECK_FAKE_REPLICA = 1;
    public final int FTR_OPTIONS_DETECT_FAKE_FINGER = 1;
    public final int FTR_OPTIONS_IMPROVE_IMAGE = 32;
    public final int FTR_OPTIONS_INVERT_IMAGE = 64;
    private final int kDefaultDeviceInstance = 0;
    private int m_ErrorCode;
    private int m_ImageHeight;
    private int m_ImageWidth;
    private long m_hDevice;

    static 
    {
        System.loadLibrary("usb-1.0");
        System.loadLibrary("ftrScanAPI");
        System.loadLibrary("ftrScanApiAndroidJni");
    }
}
