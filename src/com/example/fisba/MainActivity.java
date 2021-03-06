package com.example.fisba;

//import com.futronictech.SelectFileFormatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;

public class MainActivity extends Activity {
	/** Called when the activity is first created. */
	private static Button mButtonScan;
	private static Button mButtonDataAdministration;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mButtonScan = (Button) findViewById(R.id.btnScan);
		mButtonDataAdministration = (Button) findViewById(R.id.btnDataAdministration);

		mButtonScan.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent serverIntent = new Intent(getApplication(),
						FingerScan.class);
				startActivityForResult(serverIntent, 1);
			}
		});

		mButtonDataAdministration.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent serverIntent = new Intent(getApplication(),
						DataAdministration.class);
				startActivityForResult(serverIntent, 1);
			}
		});
	}

}