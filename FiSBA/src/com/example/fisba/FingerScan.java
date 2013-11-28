package com.example.fisba;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;

public class FingerScan extends Activity {
    /** Called when the activity is first created. */
	private static Button mButtonCancel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finger_scan);
        
    	mButtonCancel = (Button) findViewById(R.id.btnCancel);

        mButtonCancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	setContentView(R.layout.activity_main);
            }
        });
    }
    
}