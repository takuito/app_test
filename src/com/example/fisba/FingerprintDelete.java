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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class FingerprintDelete extends Activity {
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
        setContentView(R.layout.fingerprint_delete);
        
        mButtonDelete = (Button) findViewById(R.id.btnDelete);
        mButtonDeleteStart = (Button) findViewById(R.id.btnDeleteStart);
    	mButtonCancel = (Button) findViewById(R.id.btnCancel);
    	

    	mScannerInfo2 = (TextView) findViewById(R.id.tvScannerInfo2);

    	
    	mButtonDelete.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	//デバッグ用
                BufferedReader in = null;
                try {
                  FileInputStream fileRead = openFileInput("test.txt");
                  in = new BufferedReader(new InputStreamReader(fileRead));
                  String str = in.readLine();
                  String[] str_Name = str.split(",", 0);
                  Toast.makeText(FingerprintDelete.this, String.format("%d", str_Name.length),Toast.LENGTH_SHORT).show();
                  if( (str_Name.length % 3) == 0 ){
                	  mScannerInfo2.setText(str_Name[0] + "," + str_Name[1] + "," + str_Name[2]);
                  } else {
                	  mScannerInfo2.setText("ファイルが存在せーへん。");
                  }
                  in.close();
                } catch (IOException e) {
                	mScannerInfo2.setText("ファイルが存在しません。");
                  e.printStackTrace();
                }
            	/*
            	Intent intent = new Intent();
            	intent.setType("image/*");
            	intent.setAction(Intent.ACTION_PICK);
            	intent = Intent.createChooser(intent,  "Select Gallery App");
            	startActivityForResult(intent, ID_IMAGE_GALLERY);
            	*/
            }
        });
    	
    	mButtonDeleteStart.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	//デバッグ用
                BufferedReader in = null;
                try {
                  FileInputStream fileRead = openFileInput("test.txt");
                  in = new BufferedReader(new InputStreamReader(fileRead));
                  String str = in.readLine();
                  String[] str_Name = str.split(",", 0);
                  //setData data = new setData(（Integer)str_Name[0],str_Name[1],str_Name[2]);
                  if( (str_Name.length % 3) == 0 ){
                	  File file = new File(str_Name[2]);
                	  file.delete();
                	  deleteFile("test.txt");
                  } else {
                	  mScannerInfo2.setText("ファイルが存在ないのです。");
                  }
                  in.close();
                } catch (IOException e) {
                	mScannerInfo2.setText("ファイルが存在しません。");
                  e.printStackTrace();
                }
            	/*
            	Intent intent = new Intent();
            	intent.setType("image/*");
            	intent.setAction(Intent.ACTION_PICK);
            	intent = Intent.createChooser(intent,  "Select Gallery App");
            	startActivityForResult(intent, ID_IMAGE_GALLERY);
            	*/
            }
        });
    	
        mButtonCancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	finish();
            }
        });
    }
        
    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
    	
    	BufferedReader in = null;
    	if(requestCode == ID_IMAGE_GALLERY && resultCode == RESULT_OK) {
    		try {
    		*/
    			/*
    			InputStream in = getContentResolver().openInputStream(data.getData());
    			Bitmap img = BitmapFactory.decodeStream(in);
    			in.close();
    			// 選択した画像を表示
    			//imgView.setImageBitmap(img);
    			mScannerInfo2.setText("test:"+in);
    			*/
    /*
    			Uri u = data.getData();
    			ContentResolver cr = getContentResolver();
    		    String[] columns = { MediaColumns.DATA,  MediaColumns.DISPLAY_NAME};
    		    Cursor c = cr.query(u, columns, null, null, null);
    		    if(c==null) return;
    		    c.moveToFirst();
    		    int i = c.getColumnIndex(MediaColumns.DISPLAY_NAME);
    		    String s = c.getString(i);
    		    mScannerInfo2.setText("test:"+s);
    		    c.close();
    		    
    		    FileInputStream fileRead = openFileInput("test.txt");
    	        in = new BufferedReader(new InputStreamReader(fileRead));
    	        //mScannerInfo2.setText("test2:"+in.readLine());
    	        
    	        String str = in.readLine();
    	        String[] str_Name = str.split(",", 0);
    	        
    	        if(s == str_Name[1]){
    	        	mScannerInfo2.setText("test2:"+in.readLine());
    	        }
    	        in.close();
    			} catch (Exception e) {
    				
    			}
    		}
    }
    */
    
    
    
    
    
    /*
    @Override  
    public void onWindowFocusChanged(boolean hasFocus) {  
     super.onWindowFocusChanged(hasFocus);  
     
     width = layout.getWidth();  
     height = layout.getHeight();  
    }  
     
    @Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
     super.onActivityResult(requestCode, resultCode, data);  
     
     if (resultCode == RESULT_OK) {  
      switch (requestCode) {  
       case ID_IMAGE_GALLERY:  
        Uri uri = data.getData();  
     
        Intent intent = new Intent("com.android.camera.action.CROP");  
        intent.setType("image/*");  
        intent.setData(uri);  
        intent.putExtra("outputX", width);  
        intent.putExtra("outputY", height);  
        intent.putExtra("aspectX", width);  
        intent.putExtra("aspectY", height);  
        intent.putExtra("scale", true);  
        intent.putExtra("setWallpaper", false);  
        intent.putExtra("noFaceDetection", false);  
        intent.putExtra(MediaStore.EXTRA_OUTPUT, "");  
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.name());  
        intent = Intent.createChooser(intent, "Select Crop App");  
     
        startActivityForResult(intent, ID_IMAGE_CROP);  
        break;  
       case ID_IMAGE_CROP:  
        try {  
         // 一時ファイル場所のUri取得処理  
         Uri mUri = data.getData();  
         if (mUri == null) {  
          String action = data.getAction();  
          if (action != null && action.indexOf("content://") > -1) {  
           mUri = Uri.parse(action);  
          }  
         }  
     
         // uriがしっかりと取れているようなら/files/の領域へコピーして一時保存用削除  
         if (mUri != null) {  
          ContentResolver cr = getContentResolver();  
          String[] columns = { MediaColumns.DATA };  
          Cursor c = cr.query(mUri, columns, null, null, null);  
          if (c != null && c.moveToFirst()) {  
           // 一時ファイル  
           File ifilepath = new File(c.getString(0));  
           // ローカル保存用ファイル  
           File ofilepath = new File(getFileStreamPath(bgimage).getPath());  
     
           FileChannel ifile = new FileInputStream(ifilepath).getChannel();  
           FileChannel ofile = new FileOutputStream(ofilepath).getChannel();  
     
           // ファイルコピー  
           ifile.transferTo(0, ifile.size(), ofile);  
     
           // クローズ処理  
           ifile.close();  
           ofile.close();  
     
           // 一時ファイルの削除  
           getContentResolver().delete(mUri, null, null);  
     
           if (layout != null) {  
            layout.setBackgroundDrawable(rntBGImage());  
           }  
     
           Toast.makeText(this, "Complete", Toast.LENGTH_SHORT).show();  
          } else {  
           Toast.makeText(this, "Miss", Toast.LENGTH_SHORT).show();  
          }  
         }  
        } catch (Exception e) {  
         Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();  
         e.printStackTrace();  
        }  
        break;  
      }  
     }  
    }
    */
}

class setData{
	  Integer num;
	  String name;
	  String path;
	  
	  void setData(Integer num, String name, String path){
		  this.num = num;
		  this.name = name;
		  this.path = path;
	  }
  }