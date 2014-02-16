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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View.OnClickListener;

public class FingerprintDelete extends Activity {
	private static ListView listView;

	/** Called when the activity is first created. */
	private static Button mButtonCancel;
	private static Button mButtonDelete;
	private static Button mButtonDeleteStart;

	ArrayAdapter<String> adapter;

	private static String datasavefilename = "test.txt";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fingerprint_delete);

		mButtonDelete = (Button) findViewById(R.id.btnDelete);
		mButtonDeleteStart = (Button) findViewById(R.id.btnDeleteStart);
		mButtonCancel = (Button) findViewById(R.id.btnCancel);
		listView = (ListView) findViewById(R.id.ListView);
		mButtonDelete.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// デバッグ用
				BufferedReader in = null;
				try {
					FileInputStream fileRead = openFileInput(datasavefilename);
					in = new BufferedReader(new InputStreamReader(fileRead));
					String str = in.readLine();
					if (str != null) {
						String[] str_Name = str.split(",", 0);
						if (((str_Name.length % 3) == 0)
								&& (str_Name.length != 0)) {
							ArrayList<String> test_data = new ArrayList<String>();
							for (int i = 0; i < str_Name.length / 3; i++) {
								test_data.add(str_Name[i * 3 + 1]);
							}

							// アダプタの作成
							listView.setAdapter(adapter = new ArrayAdapter<String>(
									FingerprintDelete.this,
									android.R.layout.simple_list_item_multiple_choice,
									test_data));

							// フォーカスが当たらないよう設定
							listView.setItemsCanFocus(false);

							// 選択の方式の設定
							listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

							// アイテムがクリックされたときに呼び出されるコールバックを登録
							listView.setOnItemClickListener(new OnItemClickListener() {
								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id) {
									// クリックされた時の処理
								}
							});
						}
						// 現在チェックされているアイテムを取得
						// チェックされてないアイテムは含まれない模様
						SparseBooleanArray checked = listView
								.getCheckedItemPositions();
						for (int i = 0; i < checked.size(); i++) {
							// チェックされているアイテムの key の取得
							int key = checked.keyAt(i);
						}
					} else {

					}
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
					Toast.makeText(FingerprintDelete.this, "保存用データファイル読み込み失敗",
							Toast.LENGTH_LONG).show();
				} finally {

				}
			}
		});

		mButtonDeleteStart.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// デバッグ用
				BufferedReader in = null;
				try {
					FileInputStream fileRead = openFileInput(datasavefilename);
					in = new BufferedReader(new InputStreamReader(fileRead));
					String str = in.readLine();
					String[] str_Name = str.split(",", 0);
					// setData data = new
					// setData(（Integer)str_Name[0],str_Name[1],str_Name[2]);

					in.close();

					for (int i = 0; i < listView.getChildCount(); i++) {
						CheckedTextView check = (CheckedTextView) listView
								.getChildAt(i);
						if (check.isChecked()) {
							try {
								// ストリームを開く
								FileOutputStream outStream = openFileOutput(
										datasavefilename, MODE_PRIVATE);
								OutputStreamWriter writer = new OutputStreamWriter(
										outStream);

								// ファイル削除
								File file = new File(str_Name[i * 3 + 2]);
								file.delete();

								str = str.replaceAll(str_Name[i * 3 + 0] + ","
										+ str_Name[i * 3 + 1] + ","
										+ str_Name[i * 3 + 2] + ",", "");

								// リストビューから削除
								adapter.remove(str_Name[i * 3 + 1]);

								// ファイル更新
								writer.write(str);
								writer.flush();
								// deleteFile("test.txt");
								writer.close();
							} catch (IOException e) {
								e.printStackTrace();
								Toast.makeText(FingerprintDelete.this, "削除失敗",
										Toast.LENGTH_LONG).show();
							} finally {

							}
						}
					}
					// in.close();
				} catch (IOException e) {
					e.printStackTrace();
					Toast.makeText(FingerprintDelete.this, "削除失敗",
							Toast.LENGTH_LONG).show();
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
}