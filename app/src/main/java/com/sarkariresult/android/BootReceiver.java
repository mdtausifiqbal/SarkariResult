package com.sarkariresult.android;
import android.content.*;
import android.app.*;
import java.util.*;
import android.os.*;
import android.util.*;

public class BootReceiver extends BroadcastReceiver
{

	private Context context;
	private TaskSchedular taskSchedular;
	private static final String TAG = "BootReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent)
	{
		this.context = context;
		taskSchedular = new TaskSchedular(context);
		Log.i(TAG,"Called "+intent.getAction());
		if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
			taskSchedular.scheduleTask();
		}
	}
	
}
