package com.sarkariresult.android;

import android.app.AlertDialog.*;
import android.content.*;
import android.os.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.widget.*;

import android.support.v7.widget.Toolbar;


public class DebugActivity extends AppCompatActivity {

	String[] errMessage = new String[]{"Invalid string operation\u006E", "Invalid list operation\u006E", "Invalid arithmetical operation\u006E", "Invalid toNumber block operation\u006E", "Invalid intent operation"};
    String[] exceptionType = new String[]{"StringIndexOutOfBoundsException", "IndexOutOfBoundsException", "ArithmeticException", "NumberFormatException", "ActivityNotFoundException"};
	
	private Toolbar toolbar;
	private TextView text;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.debug_activity);
		initialize();
        Intent intent = getIntent();
        String errMsg = "";
        String madeErrMsg = "";
        if (intent != null) {
            errMsg = intent.getStringExtra("error");
            String[] spilt = errMsg.split("\u006E");
            int j = 0;
            while (j < this.exceptionType.length) {
                try {
                    if (spilt[0].contains(this.exceptionType[j])) {
                        madeErrMsg = new StringBuilder(String.valueOf(this.errMessage[j])).append(spilt[0].substring(spilt[0].indexOf(this.exceptionType[j]) + this.exceptionType[j].length(), spilt[0].length())).toString();
                        break;
                    }
                    j++;
                } catch (Exception e) {
                }
            }
            if (madeErrMsg.isEmpty()) {
                madeErrMsg = errMsg;
            }
        }
        text.setText(madeErrMsg);
    }
	
	private void initialize(){
		text = (TextView) findViewById(R.id.text);
		toolbar = (Toolbar) findViewById(R.id.main_toolbar);
		setSupportActionBar(toolbar);
		
	}
}
