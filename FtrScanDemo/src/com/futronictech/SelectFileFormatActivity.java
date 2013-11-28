// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) ansi 

package com.futronictech;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.view.*;
import android.widget.*;
import java.io.File;

public class SelectFileFormatActivity extends Activity
{

    public SelectFileFormatActivity()
    {
        mFileFormat = "BITMAP";
    }

    private void CheckFileName()
    {
        if((new File(mDir, mFileName)).exists())
        {
            (new android.app.AlertDialog.Builder(this)).setTitle("File name").setMessage("File already exists. Do you want replace it?").setPositiveButton("Yes", new android.content.DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialoginterface, int i)
                {
                    SetFileName();
                }

                final SelectFileFormatActivity this$0;

            
            {
                this$0 = SelectFileFormatActivity.this;
                super();
            }
            }
).setNegativeButton("No", new android.content.DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialoginterface, int i)
                {
                    mMessage.setText("Cancel");
                }

                final SelectFileFormatActivity this$0;

            
            {
                this$0 = SelectFileFormatActivity.this;
                super();
            }
            }
).setCancelable(false).show();
            return;
        } else
        {
            SetFileName();
            return;
        }
    }

    private void SetFileName()
    {
        String as[] = new String[2];
        as[0] = mFileFormat;
        as[1] = (new StringBuilder(String.valueOf(mDir.getAbsolutePath()))).append("/").append(mFileName).toString();
        Intent intent = new Intent();
        intent.putExtra(EXTRA_FILE_FORMAT, as);
        setResult(-1, intent);
        finish();
    }

    private void ShowAlertDialog()
    {
        (new android.app.AlertDialog.Builder(this)).setTitle("File name").setMessage("File name can not be empty!").setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialoginterface, int i)
            {
            }

            final SelectFileFormatActivity this$0;

            
            {
                this$0 = SelectFileFormatActivity.this;
                super();
            }
        }
).setCancelable(false).show();
    }

    public boolean isImageFolder()
    {
        mDir = new File(Environment.getExternalStorageDirectory(), "Android//FtrScanDemo");
        if(mDir.exists())
        {
            if(!mDir.isDirectory())
            {
                mMessage.setText((new StringBuilder("Can not create image folder ")).append(mDir.getAbsolutePath()).append(". File with the same name already exist.").toString());
                return false;
            }
        } else
        {
            try
            {
                mDir.mkdirs();
            }
            catch(SecurityException securityexception)
            {
                mMessage.setText((new StringBuilder("Can not create image folder ")).append(mDir.getAbsolutePath()).append(". Access denied.").toString());
                return false;
            }
        }
        return true;
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(0x7f030000);
        mButtonOK = (Button)findViewById(0x7f080007);
        mRadioGroup = (RadioGroup)findViewById(0x7f080001);
        mRadioBitmap = (RadioButton)findViewById(0x7f080002);
        mRadioWSQ = (RadioButton)findViewById(0x7f080003);
        mEditFileName = (EditText)findViewById(0x7f080006);
        mMessage = (TextView)findViewById(0x7f080008);
        setResult(0);
        mRadioGroup.setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup radiogroup, int i)
            {
                if(i == mRadioBitmap.getId())
                    mFileFormat = "BITMAP";
                else
                if(i == mRadioWSQ.getId())
                {
                    mFileFormat = "WSQ";
                    return;
                }
            }

            final SelectFileFormatActivity this$0;

            
            {
                this$0 = SelectFileFormatActivity.this;
                super();
            }
        }
);
        mButtonOK.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                mFileName = mEditFileName.getText().toString();
                if(mFileName.trim().isEmpty())
                    ShowAlertDialog();
                else
                if(isImageFolder())
                {
                    if(mFileFormat.compareTo("BITMAP") == 0)
                    {
                        SelectFileFormatActivity selectfileformatactivity1 = SelectFileFormatActivity.this;
                        selectfileformatactivity1.mFileName = (new StringBuilder(String.valueOf(selectfileformatactivity1.mFileName))).append(".bmp").toString();
                    } else
                    {
                        SelectFileFormatActivity selectfileformatactivity = SelectFileFormatActivity.this;
                        selectfileformatactivity.mFileName = (new StringBuilder(String.valueOf(selectfileformatactivity.mFileName))).append(".wsq").toString();
                    }
                    CheckFileName();
                    return;
                }
            }

            final SelectFileFormatActivity this$0;

            
            {
                this$0 = SelectFileFormatActivity.this;
                super();
            }
        }
);
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(0x7f070000, menu);
        return true;
    }

    public static String EXTRA_FILE_FORMAT = "file_format";
    private static File mDir;
    private Button mButtonOK;
    private EditText mEditFileName;
    private String mFileFormat;
    private String mFileName;
    private TextView mMessage;
    private RadioButton mRadioBitmap;
    private RadioGroup mRadioGroup;
    private RadioButton mRadioWSQ;












}
