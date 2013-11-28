package com.example.test;

import java.util.Date;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
	
	Button currentTimeGet;
	//TextView currentTimeSet;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        currentTimeGet = (Button)findViewById(R.id.current_time_get);
        
        currentTimeGet.setOnClickListener(
        		new View.OnClickListener() {
					public void onClick(View view) {
						TextView currentTimeSet;
						currentTimeSet = (TextView)findViewById(R.id.current_time_set);
						currentTimeSet.setText(new Date().toString());
						
						Toast toast = Toast.makeText(MainActivity.this, new Date().toString(), Toast.LENGTH_LONG);
						toast.show();
						Log.d("CurrentTime",new Date().toString());
					}
        		}
        		);
        		*/
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    /*
    //画面が破棄される前に状態を保存
    @Override
    protected void onSaveInstanceState(Bundle outState){
    	super.onSaveInstanceState(outState);
    	TextView txtResult = (TextView)findViewById(R.id.current_time_set);
    	outState.putString("txtResult", txtResult.getText().toString());
    }
    
    //画面が復元される前に状態を取り出し
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
    	super.onRestoreInstanceState(savedInstanceState);
    	TextView txtResult = (TextView)findViewById(R.id.current_time_set);
    	txtResult.setText(savedInstanceState.getString("txtResult"));
    }
    */
}
