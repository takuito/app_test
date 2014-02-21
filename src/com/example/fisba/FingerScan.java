package com.example.fisba;

import com.futronictech.Scanner;
import com.futronictech.UsbDeviceDataExchangeImpl;
import com.futronictech.ftrWsqAndroidHelper;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


//import jp.ito.fingerprintreader.MainActivity.myThread;
import sourceafis.simple.AfisEngine;
import sourceafis.simple.Fingerprint;
import sourceafis.simple.Person;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class FingerScan extends Activity {
	/** Called when the activity is first created. */
	private static Button mButtonCancel;
	private static Button mButtonScanStart;
	private static Button mButtonSave;

	private static TextView mMessage;

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

	private FPScan mFPScan = null;

	private static int fileNum = 0;
	// Intent request codes
	private static final int REQUEST_FILE_FORMAT = 1;
	private UsbDeviceDataExchangeImpl usb_host_ctx = null;

	// Afis
	private static AfisEngine Afis;

	public static boolean mStop = true;
	public static boolean mUsbHostMode = true;
	public static boolean mFrame = true;
	public static boolean mScan = false;

	private static String mFilePath = null;
	private static boolean mGetFilePath = false;

	private static String[] str_Name;

	private static String datasavefilename = "test.txt";
	private static String dataeditfilename = "hensyu.txt";

	// デバッグ用
	private static boolean mDebug = false;

	private AfisEngine mEngine = null;
	private ProgressDialog mProgressDialog = null;

	// private myThread mThread = null;

	/*
	 * public interface CLibrary extends Library { CLibrary INSTANCE =
	 * (CLibrary) Native.loadLibrary(Platform.isWindows() ? "msvcrt" : "libc",
	 * CLibrary.class); void printf(String format, Object... args); }
	 */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finger_scan);

		mButtonCancel = (Button) findViewById(R.id.btnCancel);
		mFingerImage = (ImageView) findViewById(R.id.imageFinger);
		mMessage = (TextView) findViewById(R.id.tvMessage);
		mButtonScanStart = (Button) findViewById(R.id.btnScanStart);
		mButtonSave = (Button) findViewById(R.id.btnSave);

		usb_host_ctx = new UsbDeviceDataExchangeImpl(FingerScan.this, mHandler);

		mEngine = new AfisEngine();
		mEngine.setDpi(200);
		mEngine.setMinMatches(100);
		mEngine.setThreshold(10.0f);

		mButtonScanStart.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (mFPScan != null) {
					mStop = true;
					mFPScan.stop();
				}
				mStop = false;
				
				/*
				mDebug = true;
				if (true == mDebug) {
					FingerScanSuccess("a.bmp");
				}
				*/

				if (mUsbHostMode) {
					usb_host_ctx.CloseDevice();
					if (usb_host_ctx.OpenDevice(0, true)) {
						Toast.makeText(FingerScan.this, "true",
								Toast.LENGTH_LONG).show();
						if (StartScan()) {
							mButtonScanStart.setEnabled(true);
							mButtonCancel.setEnabled(true);
						}
					} else {
						if (!usb_host_ctx.IsPendingOpen()) {
							if (true == mDebug) {
								//FingerScanSuccess("a.bmp");
								FingerScanSuccess("test.bmp");
							}
							mMessage.setText("Can not start scan operation.\nCan't open scanner device");
						}
					}
				}
			}
		});

		mButtonSave.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Random random_num = new Random();
				int n = random_num.nextInt()%3;
				if(0 != n){
					sleep(8000);
					/*
					BufferedReader in = null;
					try {
						FileInputStream fileRead = openFileInput(datasavefilename);
						in = new BufferedReader(new InputStreamReader(fileRead));
						String str = in.readLine();
						String[] str_Name = str.split(",", 0);
						*/
					
					FingerScanSuccess("test.bmp");
					//FingerScanSuccess("a.bmp");
				}
				
				
				/*
				BufferedReader in = null;

				try {
					FileInputStream fileRead = openFileInput(datasavefilename);
					in = new BufferedReader(new InputStreamReader(fileRead));
					String str = in.readLine();
					str_Name = str.split(",", 0);
					if (str_Name.length % 3 == 0) {
						mScan = true;
					} else {
						mScan = false;
					}
					in.close();
				} catch (IOException e) {
					mScan = false;
					e.printStackTrace();
					Toast.makeText(FingerScan.this, "保存用データファイル読み込み失敗",
							Toast.LENGTH_LONG).show();
				} finally {

				}

				FingerScanSuccess("a.bmp");
				if (true == mScan) {
					// 入力画像
					// Bitmap src =
					// BitmapFactory.decodeResource(getResources(),R.id.view1);

					// Bitmap src = BitmapFactory.decodeFile("str_Name[2]");
				}
				
				*/
			}
		});

		mButtonCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mStop = true;
				if (mFPScan != null) {
					mFPScan.stop();
					mFPScan = null;

				}
				mButtonScanStart.setEnabled(true);
				mButtonSave.setEnabled(true);
				mButtonCancel.setEnabled(false);
				finish();
			}
		});
	}

	private boolean StartScan() {
		mFPScan = new FPScan(usb_host_ctx, mHandler);
		mFPScan.start();
		return true;
	}

	/* 認証成功後に画像表示 */
	private void FingerScanSuccess(String fileName) {
		String[] str_Name = null;
		BufferedReader in = null;
		try {
			FileInputStream fileRead = openFileInput(dataeditfilename);
			in = new BufferedReader(new InputStreamReader(fileRead));
			String str = in.readLine();
			str_Name = str.split(",", 0);
			if (((str_Name.length % 2) == 0) && (str_Name.length != 0)) {
				for (int i = 0; i < str_Name.length / 2; i++) {
					if (str_Name[i * 2].equals(fileName)) {
						mGetFilePath = true;
						mFilePath = str_Name[i * 2 + 1];
					}
				}
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(FingerScan.this, "編集用データファイル読み込み失敗",
					Toast.LENGTH_LONG).show();
		} finally {

		}

		if (true == mGetFilePath) {
			/* 画像を表示 */
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(mFilePath));
			startActivity(intent);
		}
		mGetFilePath = false;
	}

	private static void ShowBitmap() {
		int[] pixels = new int[mImageWidth * mImageHeight];
		for (int i = 0; i < mImageWidth * mImageHeight; i++)
			pixels[i] = mImageFP[i];
		Bitmap emptyBmp = Bitmap.createBitmap(pixels, mImageWidth,
				mImageHeight, Config.RGB_565);

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
				break;
			case MESSAGE_SHOW_IMAGE:
				ShowBitmap();
				break;
			case MESSAGE_ERROR:
				mButtonScanStart.setEnabled(true);
				mButtonCancel.setEnabled(false);
				break;
			case UsbDeviceDataExchangeImpl.MESSAGE_ALLOW_DEVICE:
				if (usb_host_ctx.ValidateContext()) {
					if (StartScan()) {

					}
				} else {
					mMessage.setText("Can't open scanner device");
				}
				break;
			case UsbDeviceDataExchangeImpl.MESSAGE_DENY_DEVICE:
				mMessage.setText("User deny scanner device");
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mStop = true;
		if (mFPScan != null) {
			mFPScan.stop();
			mFPScan = null;
		}
		usb_host_ctx.CloseDevice();
		usb_host_ctx.Destroy();
		usb_host_ctx = null;
	}

	@SuppressWarnings("serial")
	public static class MyFingerprint extends Fingerprint {
		public String Filename;
	}

	// Inherit from Person in order to add Name field
	@SuppressWarnings("serial")
	public static class MyPerson extends Person {
		public String Name;
	}

	// Take fingerprint image file and create Person object from the image
	public static MyPerson Enroll(String filename, String name) {
		// //Console.WriteLine("Enrolling {0}...", name);

		// Initialize empty fingerprint object and set properties
		MyFingerprint fp = new MyFingerprint();
		fp.Filename = filename;
		Bitmap image = BitmapFactory.decodeFile(str_Name[2]);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		image.compress(CompressFormat.JPEG, 100, bos);
		// bos.toByteArray() で byte[] が取れる

		fp.setTemplate(bos.toByteArray());

		// Load image from the file
		// //Console.WriteLine(" Loading image from {0}...", filename);
		// BitmapImage image = new BitmapImage(new Uri(filename,
		// UriKind.RelativeOrAbsolute));
		// fp.AsBitmapSource = image;
		// Above update of fp.AsBitmapSource initialized also raw image in
		// fp.Image
		// Check raw image dimensions, Y axis is first, X axis is second
		// //Console.WriteLine(" Image size = {0} x {1} (width x height)",
		// fp.Image.GetLength(1), fp.Image.GetLength(0));

		// Initialize empty person object and set its properties
		MyPerson person = new MyPerson();
		person.Name = name;
		// Add fingerprint to the person
		// person.Fingerprints(fp);

		ArrayList<Fingerprint> list_f = new ArrayList<Fingerprint>();
		list_f.add(fp);
		person.setFingerprints(list_f);

		// Execute extraction in order to initialize fp.Template
		// //Console.WriteLine(" Extracting template...");
		// Afis.extract(person);
		// Check template size
		// //Console.WriteLine(" Template size = {0} bytes",
		// fp.Template.Length);

		// Fingerprint fp1 = new Fingerprint();
		// fp1.AsBitmapSource = null;
		// Fingerprint fp2 = new Fingerprint();
		// Fingerprint fp3 = new Fingerprint();

		return person;
	}

	static Person getPerson(int id, byte[][] template) throws IOException {
		Fingerprint arrFp[] = new Fingerprint[template.length];
		for (int x = 0; x < template.length; x++) {
			arrFp[x] = new Fingerprint();
			arrFp[x].setIsoTemplate(template[x]);
		}
		Person p = new Person(arrFp);
		p.setId(id);
		return p;
	}

	// 指定ミリ秒実行を止めるメソッド
	public synchronized void sleep(long msec) {
		try {
			wait(msec);
		} catch (InterruptedException e) {
			
		}finally{
			
		}
	}

}