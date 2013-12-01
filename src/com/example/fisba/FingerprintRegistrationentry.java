package com.example.fisba;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;

public class FingerprintRegistrationentry extends Activity {
    /** Called when the activity is first created. */
	private static Button mButtonCancel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fingerprint_registrationentry);
        
    	mButtonCancel = (Button) findViewById(R.id.btnCancel);

        mButtonCancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	finish();
            }
        });
    }
    
}