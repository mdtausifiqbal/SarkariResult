package com.sarkariresult.android;

import android.content.*;
import android.os.*;
import android.support.v7.app.*;
import android.view.*;
import android.widget.*;
import com.sarkariresult.android.NetworkReceiver.*;
import java.io.*;
import android.util.*;
public class SplashActivity extends AppCompatActivity
{

	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		new Handler().postDelayed(run,3000);
	}

	Runnable run = new Runnable(){

		@Override
		public void run()
		{

			Intent i = new Intent(SplashActivity.this, MainActivity.class);
			startActivity(i);
			finish();

		}


	};

	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
	}
}
