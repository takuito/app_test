package com.example.fisba;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.MediaColumns;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.widget.CheckedTextView;

public class FingerprintEdit extends Activity {
	public final static int ID_IMAGE_GALLERY = 1;

	private int click_num;
	private String setFilename;

	private static ListView listView;

	/** Called when the activity is first created. */
	private static Button mButtonCancel;
	private static Button mButtonEdit;
	private static Button mButtonEditStart;

	private static boolean mExistEdit = false;
	private static String datasavefilename = "test.txt";
	private static String dataeditfilename = "hensyu.txt";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fingerprint_edit);

		mButtonEdit = (Button) findViewById(R.id.btnEdit);
		mButtonCancel = (Button) findViewById(R.id.btnCancel);

		mButtonEdit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// デバッグ用
				BufferedReader in = null;
				try {
					FileInputStream fileRead = openFileInput(datasavefilename);
					in = new BufferedReader(new InputStreamReader(fileRead));
					String str = in.readLine();
					String[] str_Name = str.split(",", 0);
					if ((str_Name.length % 3) == 0) {
						listView = (ListView) findViewById(R.id.ListView);

						ArrayList<String> test_data = new ArrayList<String>();
						for (int i = 0; i < str_Name.length / 3; i++) {
							test_data.add(str_Name[i * 3 + 1]);
						}
						// アダプタの作成
						listView.setAdapter(new ArrayAdapter<String>(
								FingerprintEdit.this,
								android.R.layout.simple_list_item_1, test_data));

						// フォーカスが当たらないよう設定
						listView.setItemsCanFocus(false);

						// アイテムがクリックされたときに呼び出されるコールバックを登録
						listView.setOnItemClickListener(new OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								// クリックされた時の処理
								Intent intent = new Intent();
								intent.setType("image/*");
								intent.setAction(Intent.ACTION_PICK);
								intent = Intent.createChooser(intent,
										"Select Gallery App");
								startActivityForResult(intent, ID_IMAGE_GALLERY);

								click_num = position;
							}
						});
					} else {

					}
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
					Toast.makeText(FingerprintEdit.this, "保存用データファイル読み込み失敗",
							Toast.LENGTH_SHORT).show();
				} finally {
					
				}
			}
		});

		mButtonCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ID_IMAGE_GALLERY:
			if (resultCode == Activity.RESULT_OK) {
				Uri uri = data.getData();
				BufferedReader in = null;
				try {
					FileInputStream fileRead = openFileInput(datasavefilename);
					in = new BufferedReader(new InputStreamReader(fileRead));
					String str = in.readLine();
					String[] str_Name = str.split(",", 0);
					if (((str_Name.length % 3) == 0) && (str_Name.length != 0)) {
						setFilename = str_Name[click_num * 3 + 1];
					}
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
					Toast.makeText(FingerprintEdit.this, "保存用データファイル読み込み失敗",
							Toast.LENGTH_SHORT).show();
				} finally {
					
				}

				BufferedReader in_edit = null;
				try {
					FileInputStream fileRead = openFileInput(dataeditfilename);
					in_edit = new BufferedReader(
							new InputStreamReader(fileRead));
					String str = in_edit.readLine();
					String[] str_Name = null;
					if (null != str) {
						str_Name = str.split(",", 0);
						if (((str_Name.length % 2) == 0)) {
							for (int i = 0; i < str_Name.length / 2; i++) {
								if (str_Name[i * 2].equals(setFilename)) {
									try {
										FileOutputStream outStream = openFileOutput(
												dataeditfilename, MODE_PRIVATE);
										OutputStreamWriter writer = new OutputStreamWriter(
												outStream);
										str = str.replace(str_Name[i * 2 + 0]
												+ "," + str_Name[i * 2 + 1]
												+ ",", "");
										// ファイル更新
										writer.write(str);
										writer.flush();
										writer.close();
									} catch (IOException e) {
										e.printStackTrace();
										Toast.makeText(FingerprintEdit.this,
												"編集用データファイル書き込み失敗",
												Toast.LENGTH_SHORT).show();
									} finally {
										
									}
								}
							}
						}
					}
					in_edit.close();
				} catch (IOException e) {
					e.printStackTrace();
					Toast.makeText(FingerprintEdit.this,
							"編集用データファイル読み込み失敗",
							Toast.LENGTH_SHORT).show();
				} finally {
					
				}

				try {
					// ストリームを開く
					FileOutputStream outStream = openFileOutput(
							dataeditfilename, MODE_APPEND);
					OutputStreamWriter writer = new OutputStreamWriter(
							outStream);
					writer.write(setFilename + "," + uri + ",");
					writer.flush();
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
					Toast.makeText(FingerprintEdit.this, "編集失敗",
							Toast.LENGTH_SHORT).show();
				} finally {
					
				}
			} else {

			}
			break;
		}
	}
}