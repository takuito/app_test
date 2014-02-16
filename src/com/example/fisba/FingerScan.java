package com.example.fisba;

import com.futronictech.Scanner;
import com.futronictech.UsbDeviceDataExchangeImpl;
import com.futronictech.ftrWsqAndroidHelper;

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

import sourceafis.simple.AfisEngine;
import sourceafis.simple.Fingerprint;
import sourceafis.simple.Person;
import android.app.Activity;
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

		mButtonScanStart.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (mFPScan != null) {
					mStop = true;
					mFPScan.stop();
				}
				mStop = false;

				mDebug = true;
				if (true == mDebug) {
					FingerScanSuccess("a.bmp");
				}

				if (mUsbHostMode) {
					usb_host_ctx.CloseDevice();
					if (usb_host_ctx.OpenDevice(0, true)) {
						Toast.makeText(FingerScan.this, "true",
								Toast.LENGTH_LONG).show();
						if (StartScan()) {
							mButtonScanStart.setEnabled(true);
							mButtonCancel.setEnabled(false);
						}
					} else {
						if (!usb_host_ctx.IsPendingOpen()) {
							if (true == mDebug) {
								FingerScanSuccess("a.bmp");
							}
							mMessage.setText("Can not start scan operation.\nCan't open scanner device");
						}
					}
				} else {
					if (StartScan()) {
						mButtonScanStart.setEnabled(true);
						mButtonCancel.setEnabled(false);
					}
				}
			}
		});

		mButtonSave.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
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

					/*
					 * Bitmap src = BitmapFactory.decodeFile(
					 * "/mnt/sdcard/Android/FtrScanDemo/r.bmp");
					 * 
					 * //Bitmap src1 = src.copy(src.getConfig(), true); Bitmap
					 * src1 = src.copy(Bitmap.Config.ARGB_8888, true); Mat image
					 * = android.BitmapToMat(src1); //テンプレート画像 //src =
					 * BitmapFactory.decodeResource(getResources(),R.id.view2);
					 * src = BitmapFactory.decodeFile(
					 * "/mnt/sdcard/Android/FtrScanDemo/r.bmp"); Bitmap src2 =
					 * src.copy(Bitmap.Config.ARGB_8888, true); Mat templ =
					 * android.BitmapToMat(src2); //テンプレートマッチング Mat result = new
					 * Mat(); Imgproc.matchTemplate(image, templ, result,
					 * Imgproc.TM_CCOEFF_NORMED); Core.MinMaxLocResult maxr =
					 * Core.minMaxLoc(result); //マッチング結果の表示
					 * org.opencv.core.Point maxp = maxr.maxLoc;
					 * org.opencv.core.Point pt2 = new Point((int)(maxp.x +
					 * templ.width()), (int)(maxp.y + templ.height())); Mat dst
					 * = image.clone(); Core.rectangle(dst, maxp, pt2, new
					 * Scalar(255,0,0), 2);
					 * 
					 * Toast.makeText(FingerScan.this, String.format("%s",
					 * result),Toast.LENGTH_LONG).show();
					 * Toast.makeText(FingerScan.this, String.format("%s",
					 * maxr),Toast.LENGTH_LONG).show();
					 * Toast.makeText(FingerScan.this, String.format("%s",
					 * maxp),Toast.LENGTH_LONG).show();
					 * Toast.makeText(FingerScan.this, String.format("%s",
					 * pt2),Toast.LENGTH_LONG).show();
					 * //Toast.makeText(MainActivity.this, String.format("%s",
					 * result),Toast.LENGTH_SHORT).show();
					 * 
					 * view1.setImageBitmap(src1); view2.setImageBitmap(src2);
					 * 
					 * FingerScanSuccess("a.bmp");
					 */

					// List<MyPerson> database = new List<MyPerson>();
					// 閾値10と設定
					// Afis.setThreshold(10);
					// Afis.Identify(probe, database);

					// Afis.extract(null);

					// MyPerson fp = new MyPerson();
					// ArrayList<MyPerson> database = new ArrayList<MyPerson>();
					// List<MyPerson> database3 = new ArrayList<MyPerson>();
					// MyFingerprint fp2 = new MyFingerprint();
					// List<MyFingerprint> database2 = new
					// List<MyFingerprint>();

					// Compute similarity score
					// float score = Afis.verify(fp, fp);

					// MyPerson match = Afis.identify(fp,
					// database).FirstOrDefault();

					// MyFingerprint fp3 = new MyFingerprint();
					// ArrayList<Fingerprint> list_f = new
					// ArrayList<Fingerprint>();
					// list_f.add(fp3);

					// MyPerson match2 = Afis.identify(fp,
					// list_f).FirstOrDefault();

					MyPerson person1 = new MyPerson();
					MyPerson person2 = new MyPerson();

					MyFingerprint fingerprint1 = new MyFingerprint();
					// fingerprint1.Filename = filename;
					Bitmap image1 = BitmapFactory
							.decodeFile("/mnt/sdcard/mnt/sdcard/Android/FtrScanDemo/r.bmp");// str_Name[2]
					ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
					image1.compress(CompressFormat.JPEG, 90, bos1);
					// bos.toByteArray() で byte[] が取れる
					// fingerprint1.setImage(bos1.toByteArray());

					MyFingerprint fingerprint2 = new MyFingerprint();
					// fingerprint1.Filename = filename;
					Bitmap image2 = BitmapFactory
							.decodeFile("/mnt/sdcard/mnt/sdcard/Android/FtrScanDemo/r.bmp");// str_Name[2]
					ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
					image2.compress(CompressFormat.JPEG, 90, bos2);
					// bos.toByteArray() で byte[] が取れる
					// fingerprint2.setTemplate(bos2.toByteArray());

					ArrayList<Fingerprint> list_fingerprint1 = new ArrayList<Fingerprint>();
					list_fingerprint1.add(fingerprint1);
					person1.setFingerprints(list_fingerprint1);

					ArrayList<Fingerprint> list_fingerprint2 = new ArrayList<Fingerprint>();
					list_fingerprint2.add(fingerprint2);
					person2.setFingerprints(list_fingerprint2);

					// ArrayList<MyPerson> database = new ArrayList<MyPerson>();
					// List<MyPerson> database3 = new ArrayList<MyPerson>();
					// MyFingerprint fp2 = new MyFingerprint();

					// float score2 = Afis.verify(person1, person2);

					// 対象の文字列
					// String org = "ほげほげ";

					// 文字列のエンコード
					// String encode = Base64.encodeToString(org.getBytes(),
					// Base64.DEFAULT);

					// 文字列のデコード
					// String decode = new
					// String(Base64.decode(encode,Base64.DEFAULT));

					String t1 = "Rk1SACAyMAAAAAE4AAABYAIgAMUAxQEAAAAAL0CaATHcAECPAUPJAEC9AWm1AECTAPP/AECSAXQ1AEDJAYCpAEBmAPYVAEC/AM/yAEBNAVDEAEEiAU7VAEBCAWhBAECfALABAEE2AQzlAECvAcgOAECtAHqGAEC0AGf6AEDKATrSAECbAQ3yAEByAT4bAEDZAWm/AEBjAT7DAECwAYwuAED6AXLLAEB3AYQGAEEeATPbAEA+AVXGAEEsAUVbAIDOAbchAIDFAKZ8AICSAdZ6AID3AIZvAICIAGcJAIDMAR/cAICtAWGyAIDWAPzoAID1AUrVAICYAXyGAIDqAXi9AIDiANxwAIBhAPKhAIC1AaIkAID0AZa/AIA2ARGmAIDpAa8VAICMAKEJAICcAeUHAIBwAH2VAAAA";
					String t2 = "Rk1SACAyMAAAAAFKAAABYAIgAMUAxQEAAAAAMkC/ASbVAECgAUq1AECBATCzAECRAPz1AEDeAVvAAEBlAS4cAECJAN4AAEBtAW0BAEDpAXy1AEDaAMh1AEDDAZwhAEDfAZUsAECRAKABAEEDAaBwAECDAcV/AEDqAHhwAECjAF3/AEDBAQveAEDGAUzEAECMAUifAEDrATXVAEB4AVROAECiAXUuAECBAXF6AECoAYkhAEEUAT7YAEBaAOoVAEElAS9hAEEoAQHmAEEeAYBSAIB9AJgLAICaAdILAICdAGqJAIB3AGELAICOAR/eAICwAVSyAICPAQP1AIDCAWqmAIDOAOXoAIDvAVjLAIBVATLBAIEUASDfAIC0AMDzAIA+AUPBAIBIAO2kAICjAa8OAIC2AJiAAIEZAaNeAICEAdkDAIBcAHObAAAA";
					String t3 = "Rk1SACAyMAAAAAE+AAABYAIgAMUAxQEAAAAAMEDDASPSAEDJAQPeAEC3AU2yAEBtASQaAEDkAVTAAECTANYAAED6AVDLAEBkAOEVAEDvAXiyAEEgAR7YAEDjAZAoAEA3AUHHAEEdAYBNAEEIAZxzAECIAdQBAEBuAGmYAECWARfcAECJASqyAEDSAUTBAEDzAS/VAECpAW4sAEBeASnAAEByAWX/AEDkAMVyAEBHATq/AEEeATrUAEBAAVREAECpAasQAIDGAJB/AICHAb9/AID7AHJvAIC0AFT9AICnAUOzAICUAT+kAICbAPTzAIDIAWWlAIDaAN7mAICIAWp9AICvAYIhAIDBALjwAIBUAOKiAIDHAZghAIEsATBYAICgAJb/AICOAI4JAICfAccLAICuAGGJAICHAFcKAAAA";
					String t4 = "Rk1SACAyMAAAAAE+AAABYAIgAMUAxQEAAAAAMEDDASrSAECjAU21AEC0AVewAEDvATnVAEBoAS8aAECPAOL/AEBZATPBAECrAYwhAEDsAYC/AEBBAUTBAEDFAaEhAECXAKQBAEC+AJx/AEEFAaR1AECGAdcEAEBpAHmVAECSASPcAECDAS6uAECPAUymAEDQAO/pAEDhAWC/AECEAXR5AEBuAW//AEDbANFyAEEaASjaAIBPAO2hAIDhAZkaAIEyAQPrAIEfAYRQAICDAcl8AIDxAH1wAICqAGD8AIDEARDfAICUAQf1AIDKAVLBAIC/AWyoAIClAXgsAIDzAV/LAIC6AMr1AIBeAO0UAIEYAUPXAIEmATlaAIAtAU3JAICkAbMOAICDAJwLAICcAdALAICjAG2JAIB+AGQLAAAA";

					String probeTemplate = "Rk1SACAyMAAAAAFQAAABYAIgAMUAxQEAAAAAM0DHASnSAECQASyuAECXAUacAEDYAU6/AEDMAWmmAECsAXQsAECKAW96AECyAYghAEDFALzwAEEiASLaAEDJAZ0eAEA7AUbDAEAvAQWpAEDKAJR/AEEMAaNuAECKAdgAAEByAHCXAEDLARtzAECyAUqwAEC7AVSvAEByASkaAEDoAVu/AECXANr/AED6AVrKAEBpAOQSAEDyAX2/AEBZAOWhAEDmAZYXAEA+AVlAAECqAa8OAEElAYNOAECIAcN9AICwAGqHAICOAF8LAICbARzcAIDOAQncAICeAPn1AID3ATXUAIDfAOPmAIBiAS3AAIB1AWr8AIDoAMlyAIBKAT7BAIEkATrUAIEvATVYAIE2AQDpAICiAJoAAICPAJIKAIChAcsKAID+AHRwAIC3AFb6AAAA";

					// byte[] p1=Base64.decodeBase64(probeTemplate);
					byte[][] c = new byte[4][];
					// c[0]=Base64.decodeBase64(t1);
					// c[1]=Base64.decodeBase64(t2);
					// c[2]=Base64.decodeBase64(t3);
					// c[3]=Base64.decodeBase64(t4);

					// c[3]=bos1.toByteArray();

					byte[] p1 = bos1.toByteArray();
					byte[][] check_fingerprint = new byte[1][];

					// check_fingerprint[0] = bos2.toByteArray();
					// c[0] = bos2.toByteArray();
					c[0] = Base64.encode(bos1.toByteArray(), 1);
					c[1] = Base64.encode(bos2.toByteArray(), 1);

					c[0] = Base64.decode(t2, 1);

					byte[] _bArray = bos2.toByteArray();
					String image64 = Base64.encodeToString(_bArray,
							Base64.DEFAULT);
					// c[1]=Base64.decode(image64,1);
					// c[1]=Base64.encode(bos2.toByteArray(),1);
					c[1] = Base64.decode(bos2.toByteArray(), 1);
					c[2] = Base64.decode(t2, 1);

					/*
					 * Create AFIS Engine and set the Threshold
					 */
					AfisEngine afis = new AfisEngine();
					afis.setThreshold(12);

					/*
					 * Creating database. More persons can be added to database.
					 * Only one person is added to database in this example
					 */

					ArrayList<Person> database = new ArrayList<Person>();
					try {
						database.add(getPerson(1, c));
					} catch (IOException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
						Toast.makeText(FingerScan.this, "データベース登録失敗",
								Toast.LENGTH_LONG).show();
					} finally {
						
					}

					/* giving dummy id -1 for probe */
					Person probe = null;
					try {
						probe = getPerson(-1, new byte[][] { p1 });
					} catch (IOException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
						Toast.makeText(FingerScan.this, "データベース読み込み失敗",
								Toast.LENGTH_LONG).show();
					} finally {
						
					}
					Iterable<Person> matches = afis.identify(probe, database);

					for (Person match : matches) {
						System.out.println("Matched::" + match.getId());
					}

				}
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

}