package com.example.widget_enter;

import java.util.ArrayList;
import java.util.Calendar;
import java.text.SimpleDateFormat;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ArrayAdapter;

public class MainActivity extends Activity {
	WebView wv;


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		wv=(WebView)this.findViewById(R.id.wv);
		wv.loadUrl("http://www.google.co.jp");
		/*
		RadioGroup rgroup = (RadioGroup)this.findViewById(R.id.rgroup);
		rgroup.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			public void onCheckedChanged(RadioGroup group, int checkedId){
				RadioButton radio =(RadioButton)group.findViewById(checkedId);
				Toast.makeText(MainActivity.this, String.format("「%s」が選択されました。",radio.getText() ), Toast.LENGTH_SHORT).show();
			}
		});
		
		
		SeekBar seek = (SeekBar)findViewById(R.id.seek);
		seek.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
				Toast.makeText(MainActivity.this, String.format("現在値:%d", progress), Toast.LENGTH_SHORT).show();
			}
			public void onStartTrackingTouch(SeekBar seekBar){}
			public void onStopTrackingTouch(SeekBar seekBar){}
		});
		
		Spinner sp = (Spinner)this.findViewById(R.id.spnOs);
		sp.setOnItemSelectedListener(new OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> parent,View view,int position,long id){
				Spinner sp =(Spinner)parent;
				Toast.makeText(MainActivity.this, String.format("選択項目：%s",sp.getSelectedItem()),Toast.LENGTH_SHORT).show();
			}
			public void onNothingSelected(AdapterView<?> parent){}
		});
		
		
		createSpinner();
		*/
	}

	public void btn_onclick(View view){
		switch(view.getId()){
		case R.id.btnHome :
			wv.loadUrl("http://www.wings.msn.to/");
			break;
		case R.id.btnBbs :
			wv.loadUrl("http://keijiban.msn.co.to/top.jsp?id=gr7638");
			break;
		case R.id.btnHelp :
			wv.loadUrl("http://www.wings.msn.to/index.php/-/A-08");
			break;
		default :
			break;
		}
	}
	/*
	private void createSpinner(){
		ArrayList<String> list = new ArrayList<String>();
		SimpleDateFormat format =new SimpleDateFormat("yyyy/MM/dd");
		Calendar cal = Calendar.getInstance();
		for(int i =1; i<11 ;i++){
			cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)+1);
			list.add(format.format(cal.getTime()));
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,list);
		Spinner spn =(Spinner)this.findViewById(R.id.spnArch);
		spn.setAdapter(adapter);
		
		spn.setOnItemSelectedListener(new OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> parent,View view,int position,long id){
				Spinner sp =(Spinner)parent;
				Toast.makeText(MainActivity.this, String.format("選択項目：%s",sp.getSelectedItem()),Toast.LENGTH_SHORT).show();
			}
			public void onNothingSelected(AdapterView<?> parent){}
	});
	}*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
