package com.sarkariresult.android;

import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import com.sarkariresult.android.*;
import android.app.job.*;
import android.widget.*;

public class TaskSchedular
{
	private AlarmManager manager;
	private Intent intent;
	private PendingIntent pendingIntent;
	
	private JobScheduler schedular;
	private ComponentName component;
	private JobInfo jobInfo;
	private Context context;
	private static final String TAG = "TaskSchedular";
	private static final int REQUEST_CODE = 1;
	private long interval;
	
	private int version;
	public TaskSchedular(Context context){
		version = Build.VERSION.SDK_INT;
		this.context = context;
		setInterval(AlarmManager.INTERVAL_FIFTEEN_MINUTES);
		if(version >= Build.VERSION_CODES.LOLLIPOP){
			PersistableBundle bundle = new PersistableBundle();
			bundle.putStringArray(RefreshService.KEY_ALL_TITLE,Utils.getAllTitles(context));
			component = new ComponentName(context,RefreshJobService.class);
			schedular = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
			jobInfo = new JobInfo.Builder(REQUEST_CODE,component )
				.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
				.setPeriodic(getInterval())
				.setPersisted(true)
				.setExtras(bundle)
				.build();
		}else{
			/* manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			intent = new Intent(context,RefreshService.class);
			intent.putExtra(RefreshService.KEY_ALL_TITLE,Utils.getAllTitles(context));
			pendingIntent = PendingIntent.getService(context,REQUEST_CODE,intent,PendingIntent.FLAG_UPDATE_CURRENT);**/
			
		}
		
	}

	public void setInterval(long interval)
	{
		this.interval = interval;
	}

	public long getInterval()
	{
		return interval;
	}
	
	public void scheduleTask(){
		if(version >= Build.VERSION_CODES.LOLLIPOP){
			schedular.schedule(jobInfo);
			Log.d(TAG,"Scheduled JobService");
			Toast.makeText(context,"Scheduled Job Service",Toast.LENGTH_SHORT).show();
		}else{
			/*manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,SystemClock.elapsedRealtime()+10000,getInterval(),pendingIntent);
			Log.d(TAG,"Scheduled AlarmService");*/
		}
		
	}
	
	public void cancelTask(){
		if(version >= 21){
			schedular.cancel(REQUEST_CODE);
		}else{
			//manager.cancel(pendingIntent);
		}
		
	}
}
