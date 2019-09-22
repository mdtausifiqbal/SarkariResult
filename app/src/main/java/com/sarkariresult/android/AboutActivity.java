package com.sarkariresult.android;
import android.support.v7.app.*;
import android.os.*;
import android.support.v7.widget.*;
import android.view.*;

public class AboutActivity extends AppCompatActivity
{
	private Toolbar toolbar;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_activity);
		toolbar = (Toolbar) findViewById(R.id.main_toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				onBackPressed();
				break;
		}
		return true;
	}
	
	
}
