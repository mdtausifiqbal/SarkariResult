package com.sarkariresult.android;
import android.app.*;
import android.content.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v4.app.*;
import android.util.*;
import com.sarkariresult.android.NetworkReceiver.*;
import java.util.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
public class RefreshService extends IntentService implements OnNetworkChangeListener
{
	private NotificationCompat.Builder builder;
	private DataBinder binder = new DataBinder();
	private OnUpdateComplete updateListener;

	public static final String KEY_ALL_TITLE = "alltitle";
	private static final String TAG = "RefreshService";
	private static final int UPDATE_NOTIFICATION_ID = 1;
	private String titles[];
	private Arrays arrays;
	private boolean isInternetAvailable;
	private NetworkReceiver netReceiver;
	private UpdateChecker mUpdateChecker;
	private Notifier notifier;

	private PowerManager.WakeLock wakeLock;
	
	public RefreshService(){
		super("RefreshService");
		setIntentRedelivery(true);
	}
	
	@Override
	protected void onHandleIntent(@Nullable Intent intent)
	{
		Log.d(TAG,"onHandleIntent");
		if(intent.hasExtra(KEY_ALL_TITLE)){
			titles = intent.getStringArrayExtra(KEY_ALL_TITLE);
			
		}
		if(updateListener != null)updateListener.onStarted();
		try{
			for(String title:titles){
				if(isInternetAvailable){
					DataSet dataSet = DataSet.newInstance(RefreshService.this,title);
					updateNotice(title,dataSet.getDataUrl());
					ArrayList<Data> oldData = null;
					ArrayList<Data> newData = null;
					Document doc = Jsoup.connect(dataSet.getDataUrl()).get();
					if(dataSet.isDataFileExists()){
						oldData = dataSet.getData();
					}
					if(doc != null){
						Utils.writeFile(Utils.getDataFilePathFromTitle(title),doc.toString());
						dataSet.refreshData();
						newData = dataSet.getData();
					}
					if(oldData != null && newData != null){
						mUpdateChecker.checkForUpdate(oldData,newData);
						if(mUpdateChecker.isUpdateAvailable()){
							notifier.sendNotice(title,mUpdateChecker.getAllNotice());
						}
					}
				}

			}
			Thread.sleep(1000);
		}catch(Exception e){
			Log.e(TAG,e.getMessage());
		}finally{
			if (updateListener != null)updateListener.onFinished();
			stopForeground(true);
		}	
	}
	
	@Override
	public void onNetworkChange(boolean isConnected)
	{
		isInternetAvailable = isConnected;
	}
	
	@Override
	public IBinder onBind(Intent p1)
	{
		Log.d(TAG,"onBind");
		return binder;
	}

	@Override
	public void onCreate()
	{
		// TODO: Implement this method
		super.onCreate();
		Log.d(TAG,"onCreate");
		initialize();
		netReceiver.register();
	}

	@Override
	public void onDestroy()
	{
		Log.d(TAG,"onDestroy");
		stopForeground(true);
		netReceiver.unregister();
		wakeLock.release();
		Log.d(TAG,"WAKE LOCK RELEASED");
		super.onDestroy();
		
	}
	
	private void initialize(){
		builder = new NotificationCompat.Builder(this);
		netReceiver = new NetworkReceiver(this,this);
		mUpdateChecker = new UpdateChecker();
		notifier = new Notifier(this);
		PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"WakeLock");
		wakeLock.acquire();
		Log.d(TAG,"WAKE LOCK ACQUIRED");
	}
	
	private void updateNotice(String title,String dataUrl){
		builder.setContentTitle("Updating...("+title+")");
		builder.setContentText(dataUrl.replace("https://",""));
		builder.setSmallIcon(R.drawable.download);
		builder.setProgress(0,0,true);
		builder.setPriority(Notification.PRIORITY_HIGH);
		startForeground(UPDATE_NOTIFICATION_ID,builder.build());
		
	}
	public void setUpdateListener(OnUpdateComplete updateListener)
	{
		this.updateListener = updateListener;
	}

	public OnUpdateComplete getUpdateListener()
	{
		return updateListener;
	}
	
	public class DataBinder extends Binder{
		public RefreshService getService(){
			return RefreshService.this;
		}
	}
	
}
