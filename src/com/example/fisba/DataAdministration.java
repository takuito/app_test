package com.example.fisba;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;

public class DataAdministration extends Activity {
	/** Called when the activity is first created. */
	private static Button mButtonRegistrationentry;
	private static Button mButtonDelete;
	private static Button mButtonEdit;
	private static Button mButtonCancel;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_menu);

		mButtonRegistrationentry = (Button) findViewById(R.id.btnRegistrationentry);
		mButtonDelete = (Button) findViewById(R.id.btnDelete);
		mButtonEdit = (Button) findViewById(R.id.btnEdit);
		mButtonCancel = (Button) findViewById(R.id.btnCancel);

		mButtonRegistrationentry.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent serverIntent = new Intent(DataAdministration.this,
						FingerprintRegist.class);
				startActivityForResult(serverIntent, 1);
			}
		});

		mButtonDelete.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent serverIntent = new Intent(DataAdministration.this,
						FingerprintDelete.class);
				startActivityForResult(serverIntent, 1);
			}
		});

		mButtonEdit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent serverIntent = new Intent(DataAdministration.this,
						FingerprintEdit.class);
				startActivityForResult(serverIntent, 1);
			}
		});

		mButtonCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}

}