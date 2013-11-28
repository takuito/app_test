package com.example.edittext;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
	
	// ボタンクリック時にメッセージを表示
	public void btnSend_onClick(View view){
		EditText txtName = (EditText)findViewById(R.id.txtName);
		TextView txtResult = (TextView)findViewById(R.id.txtResult);
		txtResult.setText(String.format("こんにちは、%sさん", txtName.getText()));
	}

}
