package com.example.fisba;

import com.futronictech.Scanner;
import com.futronictech.UsbDeviceDataExchangeImpl;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

public class FPScan {
	private final Handler mHandler;
	private ScanThread mScanThread;
	private UsbDeviceDataExchangeImpl ctx = null;

	public FPScan(UsbDeviceDataExchangeImpl context, Handler handler) {
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
		if (mScanThread != null) {
			mScanThread.cancel();
			mScanThread = null;
		}
	}

	private class ScanThread extends Thread {
		private boolean bGetInfo;
		private Scanner devScan = null;
		private String strInfo;
		private int mask, flag;
		private int errCode;
		private boolean bRet;

		public ScanThread() {
			bGetInfo = false;
			devScan = new Scanner();
			/******************************************
			 * // By default, a directory of "/mnt/sdcard/Android/" is necessary
			 * for libftrScanAPI.so to work properly // in case you want to
			 * change it, you can set it by calling the below function String
			 * SyncDir = "/mnt/sdcard/test/"; // YOUR DIRECTORY if(
			 * !devScan.SetGlobalSyncDir(SyncDir) ) {
			 * mHandler.obtainMessage(FtrScanDemoActivity.MESSAGE_SHOW_MSG, -1,
			 * -1, devScan.GetErrorMessage()).sendToTarget();
			 * mHandler.obtainMessage
			 * (FtrScanDemoActivity.MESSAGE_ERROR).sendToTarget(); devScan =
			 * null; return; }
			 *******************************************/
		}

		public void run() {
			while (!FingerScan.mStop) {
				if (!bGetInfo) {
					Log.i("FUTRONIC", "Run fp scan");
					boolean bRet;
					if (FingerScan.mUsbHostMode)
						bRet = devScan.OpenDeviceOnInterfaceUsbHost(ctx);
					else
						bRet = devScan.OpenDevice();
					if (!bRet) {
						// Toast.makeText(this, strInfo,
						// Toast.LENGTH_LONG).show();
						mHandler.obtainMessage(FingerScan.MESSAGE_SHOW_MSG, -1,
								-1, devScan.GetErrorMessage()).sendToTarget();
						mHandler.obtainMessage(FingerScan.MESSAGE_ERROR)
								.sendToTarget();
						return;
					}

					if (!devScan.GetImageSize()) {
						mHandler.obtainMessage(FingerScan.MESSAGE_SHOW_MSG, -1,
								-1, devScan.GetErrorMessage()).sendToTarget();
						if (FingerScan.mUsbHostMode)
							devScan.CloseDeviceUsbHost();
						else
							devScan.CloseDevice();
						mHandler.obtainMessage(FingerScan.MESSAGE_ERROR)
								.sendToTarget();
						return;
					}
					FingerScan.mImageWidth = devScan.GetImageWidth();
					FingerScan.mImageHeight = devScan.GetImaegHeight();
					// allocate the buffer before calling GetFrame
					FingerScan.mImageFP = new byte[FingerScan.mImageWidth
							* FingerScan.mImageHeight];
					strInfo = devScan.GetVersionInfo();
					mHandler.obtainMessage(
							FingerScan.MESSAGE_SHOW_SCANNER_INFO, -1, -1,
							strInfo).sendToTarget();
					bGetInfo = true;
				}
				// set options
				flag = 0;
				mask = devScan.FTR_OPTIONS_DETECT_FAKE_FINGER
						| devScan.FTR_OPTIONS_INVERT_IMAGE;
				if (!devScan.SetOptions(mask, flag))
					mHandler.obtainMessage(FingerScan.MESSAGE_SHOW_MSG, -1, -1,
							devScan.GetErrorMessage()).sendToTarget();
				// get frame / image2
				long lT1 = SystemClock.uptimeMillis();
				if (FingerScan.mFrame)
					bRet = devScan.GetFrame(FingerScan.mImageFP);
				else
					bRet = devScan.GetImage2(4, FingerScan.mImageFP);
				if (!bRet) {
					mHandler.obtainMessage(FingerScan.MESSAGE_SHOW_MSG, -1, -1,
							devScan.GetErrorMessage()).sendToTarget();
					errCode = devScan.GetErrorCode();
					if (errCode != devScan.FTR_ERROR_EMPTY_FRAME
							&& errCode != devScan.FTR_ERROR_MOVABLE_FINGER
							&& errCode != devScan.FTR_ERROR_NO_FRAME) {
						if (FingerScan.mUsbHostMode)
							devScan.CloseDeviceUsbHost();
						else
							devScan.CloseDevice();
						mHandler.obtainMessage(FingerScan.MESSAGE_ERROR)
								.sendToTarget();
						return;
					}
				} else {
					if (FingerScan.mFrame)
						strInfo = String.format("OK. GetFrame time is %d(ms)",
								SystemClock.uptimeMillis() - lT1);
					else
						strInfo = String.format("OK. GetImage2 time is %d(ms)",
								SystemClock.uptimeMillis() - lT1);
					mHandler.obtainMessage(FingerScan.MESSAGE_SHOW_MSG, -1, -1,
							strInfo).sendToTarget();
				}
				// show image
				mHandler.obtainMessage(FingerScan.MESSAGE_SHOW_IMAGE)
						.sendToTarget();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// close device
			if (FingerScan.mUsbHostMode)
				devScan.CloseDeviceUsbHost();
			else
				devScan.CloseDevice();
		}

		public void cancel() {
			FingerScan.mStop = true;

			try {
				this.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
