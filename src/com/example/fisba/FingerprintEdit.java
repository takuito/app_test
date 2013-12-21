package com.example.fisba;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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

public class FingerprintEdit extends Activity {
	public final static int ID_IMAGE_GALLERY = 1;
	public final static int ID_IMAGE_CROP  = 2;
	private static final int REQUEST_GALLERY = 0;
	
	private static TextView mScannerInfo2;
	
    /** Called when the activity is first created. */
	private static Button mButtonCancel;
	private static Button mButtonDelete;
	private static Button mButtonDeleteStart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fingerprint_edit);
        
        mButtonDelete = (Button) findViewById(R.id.btnDelete);
        mButtonDeleteStart = (Button) findViewById(R.id.btnDeleteStart);
    	mButtonCancel = (Button) findViewById(R.id.btnCancel);
    	

    	mScannerInfo2 = (TextView) findViewById(R.id.tvScannerInfo2);

    	
    	ListView listView = (ListView)findViewById(R.id.ListView);  
    	  
        // アダプタの作成  
        listView.setAdapter(new ArrayAdapter<String>(  
            this,  
            android.R.layout.simple_list_item_multiple_choice,  
            DAYS)  
        );  
      
        // フォーカスが当たらないよう設定  
        listView.setItemsCanFocus(false);  
      
        // 選択の方式の設定  
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);  
          
        for (int i = 1; i < 6; i++) {  
          // 指定したアイテムがチェックされているかを設定  
          listView.setItemChecked(i, true);  
        }  
          
        // アイテムがクリックされたときに呼び出されるコールバックを登録  
        listView.setOnItemClickListener(new OnItemClickListener() {  
          @Override  
          public void onItemClick(AdapterView<?> parent,  
                  View view, int position, long id) {  
              // クリックされた時の処理  
          }  
        });  
          
        // 現在チェックされているアイテムを取得  
        // チェックされてないアイテムは含まれない模様  
        SparseBooleanArray checked = listView.getCheckedItemPositions();  
        for (int i = 0; i < checked.size(); i++) {  
          // チェックされているアイテムの key の取得  
          int key = checked.keyAt(i);  
          //Log.v(getClass().getSimpleName(), "values: " + DAYS[key]);  
        }  
      }  
        
      // ListView に表示させる文字列  
      private static final String[] DAYS = new String[] {  
        "Sunday", "Monday", "Tuesday", "Wednesday",  
        "Thursday", "Friday", "Saturday"  
      };
}