package com.example.dialog;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	

	public void onClick(View view){
		final String[] items = {"A型","B型","AB型","O型"};
		
		final int[] selected = {0};
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("血液型")
		.setIcon(R.drawable.ic_launcher)
		.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog,int which){
				selected[0] = which;
			}
		})
		
		.setPositiveButton("OK", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog,int which){
				Toast.makeText(MainActivity.this, String.format("「%s」が選択されました。", items[selected[0]]),Toast.LENGTH_SHORT).show();
			}
		})
		.show();
	}
		
		
		/*
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("ダイアログの基本")
		.setMessage("AndroidはJavaで開発できますか？")
		.setIcon(R.drawable.ic_launcher)
		
		.setPositiveButton("はい",new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which){
				Toast.makeText(MainActivity.this, "正解です！", Toast.LENGTH_SHORT).show();
			}
		})
	
		.setNegativeButton("いいえ",new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which){
				Toast.makeText(MainActivity.this, "ミス！", Toast.LENGTH_SHORT).show();
			}
		})
	
		.setNeutralButton("キャンセル",new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which){}
		})
		.show();
	}*/
}
