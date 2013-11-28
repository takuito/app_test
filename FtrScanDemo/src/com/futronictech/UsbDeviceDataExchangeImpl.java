// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) ansi 

package com.futronictech;

import android.app.PendingIntent;
import android.content.*;
import android.hardware.usb.*;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.util.*;

public class UsbDeviceDataExchangeImpl
{
    public class FTR_USB_DEVICE_INTERNAL
    {

        public UsbDeviceConnection mDevConnetion_;
        public UsbDevice mDev_;
        public boolean mHandleClaimed_;
        public UsbInterface mIntf_;
        public UsbEndpoint mReadPoint_;
        public UsbEndpoint mWritePoint_;
        final UsbDeviceDataExchangeImpl this$0;

        public FTR_USB_DEVICE_INTERNAL(UsbDevice usbdevice, UsbInterface usbinterface, UsbEndpoint usbendpoint, UsbEndpoint usbendpoint1, UsbDeviceConnection usbdeviceconnection)
        {
            this$0 = UsbDeviceDataExchangeImpl.this;
            super();
            mDev_ = usbdevice;
            mIntf_ = usbinterface;
            mReadPoint_ = usbendpoint;
            mWritePoint_ = usbendpoint1;
            mDevConnetion_ = usbdeviceconnection;
            mHandleClaimed_ = false;
        }
    }


    public UsbDeviceDataExchangeImpl(Context context1, Handler handler1)
    {
        usb_ctx = null;
        context = null;
        handler = null;
        pending_open = false;
        mPermissionIntent = null;
        max_transfer_buffer = new byte[0x10000];
        context = context1;
        handler = handler1;
        mDevManager = (UsbManager)context1.getSystemService("usb");
        mPermissionIntent = PendingIntent.getBroadcast(context1, 0, new Intent("com.futronictech.FtrScanDemoActivity.USB_PERMISSION"), 0);
        IntentFilter intentfilter = new IntentFilter("com.futronictech.FtrScanDemoActivity.USB_PERMISSION");
        context.registerReceiver(mUsbReceiver, intentfilter);
    }

    public static void GetInterfaces(Context context1, byte abyte0[])
    {
        Iterator iterator;
        int i;
        iterator = ((UsbManager)context1.getSystemService("usb")).getDeviceList().values().iterator();
        i = 0;
_L3:
        if(i < 128) goto _L2; else goto _L1
_L1:
        if(!iterator.hasNext())
            return;
        break MISSING_BLOCK_LABEL_50;
_L2:
        abyte0[i] = 1;
        i++;
          goto _L3
        UsbDevice usbdevice = (UsbDevice)iterator.next();
        if(IsFutronicDevice(usbdevice.getVendorId(), usbdevice.getProductId()))
            abyte0[0] = 0;
          goto _L1
    }

    public static boolean IsFutronicDevice(int i, int j)
    {
        boolean flag;
label0:
        {
            if((i != 2100 || j != 32) && (i != 2392 || j != 775) && (i != 5265 || j != 32 && j != 37 && j != 136 && j != 144 && j != 80 && j != 96 && j != 152 && j != 32920 && j != 39008))
            {
                flag = false;
                if(i != 8122)
                    break label0;
                if(j != 19)
                {
                    flag = false;
                    if(j != 18)
                        break label0;
                }
            }
            flag = true;
        }
        return flag;
    }

    private FTR_USB_DEVICE_INTERNAL OpenDevice(UsbDevice usbdevice)
    {
        UsbInterface usbinterface = usbdevice.getInterface(0);
        if(usbinterface != null)
        {
            UsbEndpoint usbendpoint = null;
            UsbEndpoint usbendpoint1 = null;
            int i = 0;
            do
            {
                if(i >= usbinterface.getEndpointCount())
                    if(usbendpoint != null && usbendpoint1 != null)
                    {
                        UsbDeviceConnection usbdeviceconnection = mDevManager.openDevice(usbdevice);
                        if(usbdeviceconnection != null)
                        {
                            Log.i("FUTRONICFTR_J", (new StringBuilder("Open device: ")).append(usbdevice).toString());
                            return new FTR_USB_DEVICE_INTERNAL(usbdevice, usbinterface, usbendpoint, usbendpoint1, usbdeviceconnection);
                        } else
                        {
                            Log.e("FUTRONICFTR_J", (new StringBuilder("open device failed: ")).append(usbdevice).toString());
                            return null;
                        }
                    } else
                    {
                        Log.e("FUTRONICFTR_J", (new StringBuilder("End points not found in device: ")).append(usbdevice).toString());
                        return null;
                    }
                if(usbinterface.getEndpoint(i).getDirection() == 0)
                    usbendpoint1 = usbinterface.getEndpoint(i);
                if(usbinterface.getEndpoint(i).getDirection() == 128)
                    usbendpoint = usbinterface.getEndpoint(i);
                i++;
            } while(true);
        } else
        {
            Log.e("FUTRONICFTR_J", (new StringBuilder("Get interface failed failed in device: ")).append(usbdevice).toString());
            return null;
        }
    }

    public void CloseDevice()
    {
        this;
        JVM INSTR monitorenter ;
        if(usb_ctx != null && usb_ctx.mDevConnetion_ != null)
            usb_ctx.mDevConnetion_.close();
        usb_ctx = null;
        this;
        JVM INSTR monitorexit ;
        return;
        Exception exception;
        exception;
        this;
        JVM INSTR monitorexit ;
        throw exception;
    }

    public boolean DataExchange(byte abyte0[], byte abyte1[], int i, int j, boolean flag, boolean flag1, int k)
    {
        this;
        JVM INSTR monitorenter ;
        FTR_USB_DEVICE_INTERNAL ftr_usb_device_internal = usb_ctx;
        boolean flag3 = false;
        if(ftr_usb_device_internal == null) goto _L2; else goto _L1
_L1:
        if(usb_ctx.mIntf_ == null || usb_ctx.mDevConnetion_ == null || usb_ctx.mReadPoint_ == null) goto _L4; else goto _L3
_L3:
        UsbEndpoint usbendpoint1 = usb_ctx.mWritePoint_;
        if(usbendpoint1 == null) goto _L4; else goto _L5
_L5:
        flag3 = true;
_L2:
        if(flag3)
            break; /* Loop/switch isn't completed */
        this;
        JVM INSTR monitorexit ;
        return false;
_L4:
        flag3 = false;
        if(true) goto _L2; else goto _L6
_L6:
        if(!usb_ctx.mHandleClaimed_)
        {
            usb_ctx.mDevConnetion_.claimInterface(usb_ctx.mIntf_, false);
            usb_ctx.mHandleClaimed_ = true;
        }
        if(abyte0.length <= 0 || usb_ctx.mDevConnetion_.bulkTransfer(usb_ctx.mWritePoint_, abyte0, abyte0.length, j) != -1)
            break MISSING_BLOCK_LABEL_178;
        Object aobj9[] = new Object[1];
        aobj9[0] = Integer.valueOf(abyte0.length);
        Log.e("FUTRONICFTR_J", String.format("Send %d bytes failed", aobj9));
        this;
        JVM INSTR monitorexit ;
        return false;
        int l = abyte1.length;
        int i1 = 0;
_L19:
        if(l >= getTransferBuffer().length) goto _L8; else goto _L7
_L7:
        if(l >= 16384) goto _L10; else goto _L9
_L9:
        int l1;
        if(l <= usb_ctx.mReadPoint_.getMaxPacketSize())
            break MISSING_BLOCK_LABEL_688;
        l1 = l - l % usb_ctx.mReadPoint_.getMaxPacketSize();
        if(l1 <= 0)
            break MISSING_BLOCK_LABEL_688;
        int i2 = usb_ctx.mDevConnetion_.bulkTransfer(usb_ctx.mReadPoint_, getTransferBuffer(), l1, i);
        if(i2 != -1)
            break; /* Loop/switch isn't completed */
        Object aobj3[] = new Object[1];
        aobj3[0] = Integer.valueOf(l1);
        Log.e("FUTRONICFTR_J", String.format("Receive(1) %d bytes failed", aobj3));
        this;
        JVM INSTR monitorexit ;
        return false;
_L8:
        int l2 = usb_ctx.mDevConnetion_.bulkTransfer(usb_ctx.mReadPoint_, getTransferBuffer(), getTransferBuffer().length, i);
        if(l2 != -1)
            break MISSING_BLOCK_LABEL_377;
        Object aobj7[] = new Object[1];
        aobj7[0] = Integer.valueOf(getTransferBuffer().length);
        Log.e("FUTRONICFTR_J", String.format("Receive %d bytes failed", aobj7));
        this;
        JVM INSTR monitorexit ;
        return false;
        int i3 = i1 + l2;
        if(i3 <= abyte1.length)
            break MISSING_BLOCK_LABEL_430;
        Object aobj8[] = new Object[1];
        aobj8[0] = Integer.valueOf((i1 + l2) - abyte1.length);
        Log.e("FUTRONICFTR_J", String.format("Small receive buffer. Need %d bytes", aobj8));
        this;
        JVM INSTR monitorexit ;
        return false;
        System.arraycopy(getTransferBuffer(), 0, abyte1, i1, l2);
        l -= l2;
        i1 += l2;
        continue; /* Loop/switch isn't completed */
_L10:
        int j2 = usb_ctx.mDevConnetion_.bulkTransfer(usb_ctx.mReadPoint_, getTransferBuffer(), 16384, i);
        if(j2 != -1)
            break MISSING_BLOCK_LABEL_529;
        Object aobj5[] = new Object[1];
        aobj5[0] = Integer.valueOf(getTransferBuffer().length);
        Log.e("FUTRONICFTR_J", String.format("Receive %d bytes failed", aobj5));
        this;
        JVM INSTR monitorexit ;
        return false;
        int k2 = i1 + j2;
        if(k2 <= abyte1.length)
            break MISSING_BLOCK_LABEL_582;
        Object aobj6[] = new Object[1];
        aobj6[0] = Integer.valueOf((i1 + j2) - abyte1.length);
        Log.e("FUTRONICFTR_J", String.format("Small receive buffer. Need %d bytes", aobj6));
        this;
        JVM INSTR monitorexit ;
        return false;
        System.arraycopy(getTransferBuffer(), 0, abyte1, i1, j2);
        l -= j2;
        i1 += j2;
        if(true) goto _L7; else goto _L11
_L11:
        if(i1 + i2 <= abyte1.length)
            break MISSING_BLOCK_LABEL_661;
        Object aobj4[] = new Object[1];
        aobj4[0] = Integer.valueOf((i1 + i2) - abyte1.length);
        Log.e("FUTRONICFTR_J", String.format("Small receive buffer. Need %d bytes", aobj4));
        this;
        JVM INSTR monitorexit ;
        return false;
        System.arraycopy(getTransferBuffer(), 0, abyte1, i1, i2);
        l -= i2;
        i1 += i2;
_L16:
        if(l > 0) goto _L13; else goto _L12
_L12:
        if(flag)
            break MISSING_BLOCK_LABEL_724;
        usb_ctx.mDevConnetion_.releaseInterface(usb_ctx.mIntf_);
        usb_ctx.mHandleClaimed_ = false;
        boolean flag2 = true;
_L17:
        this;
        JVM INSTR monitorexit ;
        return flag2;
_L13:
        UsbDeviceConnection usbdeviceconnection;
        UsbEndpoint usbendpoint;
        byte abyte2[];
        usbdeviceconnection = usb_ctx.mDevConnetion_;
        usbendpoint = usb_ctx.mReadPoint_;
        abyte2 = getTransferBuffer();
        if(!flag1)
            break MISSING_BLOCK_LABEL_822;
        int j1 = usb_ctx.mReadPoint_.getMaxPacketSize();
_L14:
        if(usbdeviceconnection.bulkTransfer(usbendpoint, abyte2, j1, i) != -1)
            break MISSING_BLOCK_LABEL_829;
        Object aobj2[] = new Object[1];
        aobj2[0] = Integer.valueOf(l);
        Log.e("FUTRONICFTR_J", String.format("Receive(1) %d bytes failed", aobj2));
        this;
        JVM INSTR monitorexit ;
        return false;
        j1 = l;
          goto _L14
        int k1;
        if(l <= usb_ctx.mReadPoint_.getMaxPacketSize())
            break MISSING_BLOCK_LABEL_905;
        k1 = usb_ctx.mReadPoint_.getMaxPacketSize();
_L15:
        if(i1 + k1 <= abyte1.length)
            break MISSING_BLOCK_LABEL_912;
        Object aobj1[] = new Object[1];
        aobj1[0] = Integer.valueOf((i1 + k1) - abyte1.length);
        Log.e("FUTRONICFTR_J", String.format("Small receive buffer. Need %d bytes", aobj1));
        this;
        JVM INSTR monitorexit ;
        return false;
        k1 = l;
          goto _L15
        System.arraycopy(getTransferBuffer(), 0, abyte1, i1, k1);
        l -= k1;
        i1 += k1;
          goto _L16
        Exception exception1;
        exception1;
        Object aobj[] = new Object[1];
        aobj[0] = exception1.toString();
        Log.e("FUTRONICFTR_J", String.format("Data exchange fail %s", aobj));
        flag2 = false;
          goto _L17
        Exception exception;
        exception;
        this;
        JVM INSTR monitorexit ;
        throw exception;
        if(true) goto _L19; else goto _L18
_L18:
    }

    public void DataExchangeEnd()
    {
        this;
        JVM INSTR monitorenter ;
        if(usb_ctx != null && usb_ctx.mHandleClaimed_)
        {
            usb_ctx.mDevConnetion_.releaseInterface(usb_ctx.mIntf_);
            usb_ctx.mHandleClaimed_ = false;
        }
        this;
        JVM INSTR monitorexit ;
        return;
        Exception exception;
        exception;
        this;
        JVM INSTR monitorexit ;
        throw exception;
    }

    public void Destroy()
    {
        context.unregisterReceiver(mUsbReceiver);
    }

    public boolean GetDeviceInfo(byte abyte0[])
    {
        this;
        JVM INSTR monitorenter ;
        FTR_USB_DEVICE_INTERNAL ftr_usb_device_internal = usb_ctx;
        boolean flag = false;
        if(ftr_usb_device_internal == null) goto _L2; else goto _L1
_L1:
        int i = usb_ctx.mDev_.getVendorId();
        int j;
        byte byte0;
        j = 0 + 1;
        byte0 = (byte)i;
        abyte0[0] = byte0;
        int k;
        byte byte1;
        k = j + 1;
        byte1 = (byte)(i >> 8);
        abyte0[j] = byte1;
        byte byte2;
        j = k + 1;
        byte2 = (byte)(i >> 16);
        abyte0[k] = byte2;
        int l;
        byte byte3;
        l = j + 1;
        byte3 = (byte)(i >> 24);
        int i1;
        abyte0[j] = byte3;
        i1 = usb_ctx.mDev_.getProductId();
        byte byte4;
        j = l + 1;
        byte4 = (byte)i1;
        abyte0[l] = byte4;
        int j1;
        byte byte5;
        j1 = j + 1;
        byte5 = (byte)(i1 >> 8);
        abyte0[j] = byte5;
        byte byte6;
        j = j1 + 1;
        byte6 = (byte)(i1 >> 16);
        abyte0[j1] = byte6;
        int k1;
        byte byte7;
        k1 = j + 1;
        byte7 = (byte)(i1 >> 24);
        String s;
        abyte0[j] = byte7;
        s = usb_ctx.mDevConnetion_.getSerial();
        if(s == null) goto _L4; else goto _L3
_L3:
        j = k1 + 1;
        byte abyte1[];
        int l1;
        abyte0[k1] = 1;
        abyte1 = s.getBytes();
        l1 = abyte1.length;
        int i2;
        byte byte8;
        i2 = j + 1;
        byte8 = (byte)l1;
        abyte0[j] = byte8;
        byte byte9;
        j = i2 + 1;
        byte9 = (byte)(l1 >> 8);
        abyte0[i2] = byte9;
        int j2;
        byte byte10;
        j2 = j + 1;
        byte10 = (byte)(l1 >> 16);
        abyte0[j] = byte10;
        byte byte11;
        j = j2 + 1;
        byte11 = (byte)(l1 >> 24);
        int k2;
        abyte0[j2] = byte11;
        System.arraycopy(abyte1, 0, abyte0, j, abyte1.length);
        k2 = abyte1.length;
        k2 + 13;
_L5:
        flag = true;
_L2:
        this;
        JVM INSTR monitorexit ;
        return flag;
_L4:
        j = k1 + 1;
        abyte0[k1] = 0;
        j;
          goto _L5
        Exception exception1;
        exception1;
_L6:
        Log.e("FUTRONICFTR_J", (new StringBuilder("Get device info failed: ")).append(exception1.toString()).toString());
        flag = false;
          goto _L2
        Exception exception;
        exception;
        this;
        JVM INSTR monitorexit ;
        throw exception;
        exception1;
        j;
          goto _L6
    }

    public boolean IsPendingOpen()
    {
        return pending_open;
    }

    public boolean OpenDevice(int i, boolean flag)
    {
        this;
        JVM INSTR monitorenter ;
        if(usb_ctx != null) goto _L2; else goto _L1
_L1:
        Iterator iterator;
        pending_open = false;
        iterator = mDevManager.getDeviceList().values().iterator();
        int j = 0;
_L4:
        if(iterator.hasNext()) goto _L3; else goto _L2
_L2:
        FTR_USB_DEVICE_INTERNAL ftr_usb_device_internal = usb_ctx;
        boolean flag1;
        flag1 = false;
        if(ftr_usb_device_internal != null)
            flag1 = true;
        this;
        JVM INSTR monitorexit ;
        return flag1;
_L3:
        UsbDevice usbdevice = (UsbDevice)iterator.next();
        if(IsFutronicDevice(usbdevice.getVendorId(), usbdevice.getProductId()))
        {
label0:
            {
                if(j >= i)
                    break label0;
                j++;
            }
        }
          goto _L4
        if(mDevManager.hasPermission(usbdevice))
            break MISSING_BLOCK_LABEL_158;
        mDevManager.requestPermission(usbdevice, mPermissionIntent);
        if(flag)
            break MISSING_BLOCK_LABEL_207;
        pending_open = false;
        PendingIntent pendingintent = mPermissionIntent;
        pendingintent;
        JVM INSTR monitorenter ;
        mPermissionIntent.wait();
_L5:
        if(!mDevManager.hasPermission(usbdevice))
            break MISSING_BLOCK_LABEL_216;
        usb_ctx = OpenDevice(usbdevice);
          goto _L4
        Exception exception;
        exception;
        this;
        JVM INSTR monitorexit ;
        throw exception;
        InterruptedException interruptedexception;
        interruptedexception;
        interruptedexception.printStackTrace();
          goto _L5
        Exception exception1;
        exception1;
        pendingintent;
        JVM INSTR monitorexit ;
        throw exception1;
        pending_open = true;
        this;
        JVM INSTR monitorexit ;
        return false;
        Log.e("FUTRONICFTR_J", "device not allow");
          goto _L4
    }

    public boolean ValidateContext()
    {
        this;
        JVM INSTR monitorenter ;
        FTR_USB_DEVICE_INTERNAL ftr_usb_device_internal = usb_ctx;
        boolean flag;
        flag = false;
        if(ftr_usb_device_internal == null)
            break MISSING_BLOCK_LABEL_55;
        Exception exception;
        if(usb_ctx.mIntf_ != null && usb_ctx.mDevConnetion_ != null && usb_ctx.mReadPoint_ != null && usb_ctx.mWritePoint_ != null)
            flag = true;
        else
            flag = false;
        this;
        JVM INSTR monitorexit ;
        return flag;
        exception;
        this;
        JVM INSTR monitorexit ;
        throw exception;
    }

    public byte[] getTransferBuffer()
    {
        return max_transfer_buffer;
    }

    public void setTransferBuffer(byte abyte0[])
    {
        max_transfer_buffer = abyte0;
    }

    static final String ACTION_USB_PERMISSION = "com.futronictech.FtrScanDemoActivity.USB_PERMISSION";
    public static final int MESSAGE_ALLOW_DEVICE = 255;
    public static final int MESSAGE_DENY_DEVICE = 256;
    static final String log_tag = "FUTRONICFTR_J";
    static final int transfer_buffer_size = 0x10000;
    static final int transfer_buffer_size_2 = 16384;
    private Context context;
    private Handler handler;
    private UsbManager mDevManager;
    private PendingIntent mPermissionIntent;
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

        public void onReceive(Context context2, Intent intent)
        {
            if(!"com.futronictech.FtrScanDemoActivity.USB_PERMISSION".equals(intent.getAction()))
                break MISSING_BLOCK_LABEL_109;
            PendingIntent pendingintent = mPermissionIntent;
            pendingintent;
            JVM INSTR monitorenter ;
            UsbDevice usbdevice;
            usbdevice = (UsbDevice)intent.getParcelableExtra("device");
            if(!intent.getBooleanExtra("permission", false))
                break MISSING_BLOCK_LABEL_83;
            if(usbdevice == null)
                break MISSING_BLOCK_LABEL_64;
            usb_ctx = OpenDevice(usbdevice);
            handler.obtainMessage(255).sendToTarget();
_L2:
            pendingintent;
            JVM INSTR monitorexit ;
            return;
            handler.obtainMessage(256).sendToTarget();
            if(true) goto _L2; else goto _L1
_L1:
            Exception exception;
            exception;
            pendingintent;
            JVM INSTR monitorexit ;
            throw exception;
        }

        final UsbDeviceDataExchangeImpl this$0;

            
            {
                this$0 = UsbDeviceDataExchangeImpl.this;
                super();
            }
    }
;
    private byte max_transfer_buffer[];
    private boolean pending_open;
    private FTR_USB_DEVICE_INTERNAL usb_ctx;




}
