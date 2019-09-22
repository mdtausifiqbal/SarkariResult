package com.sarkariresult.android;
import android.app.job.*;
import android.content.*;
import android.os.*;
import android.util.*;

public class RefreshJobService extends JobService implements OnUpdateComplete
{
	private Intent intent;
	private boolean isServiceBound;
	private ServiceConnection mServiceConnection;
	private RefreshService mService;
	private Intent serviceIntent;
	private Context context;
	private JobParameters params;
	
	private static final String TAG = "RefreshJobService";

	@Override
	public void onCreate()
	{
		// TODO: Implement this method
		super.onCreate();
		Log.d(TAG,"onCreate");
		context = this;
		serviceIntent = new Intent(this,RefreshService.class);
	}
	
	@Override
	public boolean onStartJob(JobParameters p1)
	{
		Log.d(TAG,"onStartJob");
		params = p1;
		bindService();
		intent = new Intent(this,RefreshService.class);
		intent.putExtra(RefreshService.KEY_ALL_TITLE,p1.getExtras().getStringArray(RefreshService.KEY_ALL_TITLE));
		startService(intent);
		return true;
	}

	@Override
	public boolean onStopJob(JobParameters p1)
	{
		Log.d(TAG,"onStopJob");
		unbindService();
		stopService(intent);
		return true;
	}
	
	@Override
	public void onStarted()
	{
		Log.d(TAG,"onStarted");
	}

	@Override
	public void onFinished()
	{
		Log.d(TAG,"onFinished");
		unbindService();
		jobFinished(params,true);
	}
	
	private void bindService(){
        if(mServiceConnection==null){
            mServiceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    Log.d(TAG,"onServiceConnected");
					RefreshService.DataBinder myServiceBinder=(RefreshService.DataBinder)iBinder;
                    mService=myServiceBinder.getService();
					mService.setUpdateListener(RefreshJobService.this);
                    isServiceBound=true;
                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                    Log.d(TAG,"onServiceDisconnected");
					mService.setUpdateListener(null);
					isServiceBound=false;
                }
            };
        }

        if(!isServiceBound)context.bindService(serviceIntent,mServiceConnection,Context.BIND_AUTO_CREATE);

    }

    private void unbindService(){
        if(isServiceBound){
            context.unbindService(mServiceConnection);
            isServiceBound=false;
        }
    }
}
