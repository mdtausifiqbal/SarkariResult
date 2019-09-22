package com.sarkariresult.android;
import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.text.*;
import java.util.*;
import android.support.v4.widget.*;
import android.support.v7.widget.*;
import android.app.AlertDialog;

public class ListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,OnUpdateComplete
{

	
	private View root;
	private RecyclerView listview;
	private TextView syncText;
	private LinearLayout error;
	private SwipeRefreshLayout refresh;
	
	public String[] all_titles;
	public String[] top_titles;
	public DataSet dataSet;
	public String dataTitle;
	public HashMap<String,DataSet> dataSets;
	public MainActivity context;
	public UrlHandler handler;
	
	private final String TAG = "List Fragment";
	public int position = -1;
	
	private SharedPreferences pref;
	private static final String USER = "user";
	private static final String KEY_IS_NEW_USER = "isNewUser";
	private boolean isNewUser;
	
	private boolean isServiceBound;
	private ServiceConnection mServiceConnection;
	private RefreshService mService;
	private TaskSchedular schedular;
	private Intent serviceIntent;
	
	public ListFragment(MainActivity context, String dataTitle){
		this.dataTitle = dataTitle;
		this.context = context;
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		all_titles = Utils.getAllTitles(this.context);
		top_titles = Utils.getTopTitles(this.context);
		updateData();
		Log.d(TAG,"onCreate");
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		Log.d(TAG,"onCreateView");
		root = inflater.inflate(R.layout.list_fragment, container, false);
		return root;
	}


	@Override
	public void onStart()
	{
		
		super.onStart();
		Log.d(TAG,"onStart");
		initialize();
		updateFragment(this.dataTitle);
		bindService();
		//if(!isNewUser)schedular.cancelTask();
		if(isNewUser)schedular.scheduleTask();
		if(!dataSet.isDataFileExists())refreshData();
	}

	@Override
	public void onStop()
	{
		// TODO: Implement this method
		super.onStop();
		unbindService();
		//schedular.scheduleTask();
		
	}
	
	@Override
	public void onStarted()
	{
		context.runOnUiThread(new Runnable(){

				@Override
				public void run()
				{
					if(!refresh.isRefreshing())refresh.setRefreshing(true);
				}
				
		});
		
	}

	@Override
	public void onFinished()
	{
		updateData();
		context.runOnUiThread(new Runnable(){

				@Override
				public void run()
				{
					Log.d(TAG,"onFinished");
					updateListView();
					if (refresh.isRefreshing())refresh.setRefreshing(false);
					
				}

			});
	}
	
	@Override
	public void onRefresh()
	{
		Log.d(TAG,"onRefresh");
		refreshData();
	}

	private void initialize()
	{
		//Initializing all Views in Layout
		listview = (RecyclerView) root.findViewById(R.id.dataView);
		syncText = (TextView) root.findViewById(R.id.synctime);
		error = (LinearLayout) root.findViewById(R.id.error);
		refresh = (SwipeRefreshLayout) root.findViewById(R.id.refresh);
		
		//Initializing and setting other objects
		handler = new UrlHandler(context);
		serviceIntent = new Intent(context,RefreshService.class);
		pref = context.getSharedPreferences(USER,Context.MODE_PRIVATE);
		isNewUser = pref.getBoolean(KEY_IS_NEW_USER,true);
		schedular = new TaskSchedular(context);
		if(isNewUser){
			pref.edit().putBoolean(KEY_IS_NEW_USER,false).commit();
		}
		refresh.setOnRefreshListener(this);
		listview.setHasFixedSize(true);
		listview.setLayoutManager(new LinearLayoutManager(context));
		
	}
	
	public void updateData(){
		dataSets = new HashMap<String,DataSet>();
		for(String title:all_titles){
			dataSets.put(title,DataSet.newInstance(this.context,title));
		}
		dataSet = dataSets.get(dataTitle);
	}
	
	public void updateFragment(String dataTitle){
		this.dataTitle = dataTitle;
		dataSet = dataSets.get(dataTitle);
		boolean check = dataSet.isHome()?isAllTopFilesExists():dataSet.isDataFileExists();
		if(check){
			updateListView();
		}else{
			showList(false);
		}
	}
	
	
	public void refreshData()
	{
		if (context.isInternetAvailable)
		{ 
			if(dataSet.isHome())serviceIntent.putExtra(RefreshService.KEY_ALL_TITLE,all_titles);
			else serviceIntent.putExtra(RefreshService.KEY_ALL_TITLE,new String[]{dataSet.getDataTitle()});
			bindService();
			context.startService(serviceIntent);
			
		}
		else
		{
			if (refresh.isRefreshing())refresh.setRefreshing(false);
		}
	}

	private void updateListView()
	{
		showList(true);
		setMessage("Last synced : "+dataSet.getDateText()+" | Total : "+dataSet.getData().size());
		
		if(!dataSet.isHome())listview.setAdapter(new SimpleAdapter(this));
		else listview.setAdapter(new AdvanceAdapter(this));
	}
	

	private void showList(boolean show)
	{
		if (show)
		{
			refresh.setVisibility(View.VISIBLE);
			listview.setVisibility(View.VISIBLE);
			error.setVisibility(View.GONE);
			syncText.setVisibility(View.VISIBLE);
		}
		else
		{
			refresh.setVisibility(View.GONE);
			listview.setVisibility(View.GONE);
			error.setVisibility(View.VISIBLE);
			syncText.setVisibility(View.GONE);
		}
	}

	private void setMessage(String message){
		this.syncText.setText(message);
	}
	
	
	public boolean isAllTopFilesExists(){
		for(String title:top_titles){
			if(!dataSets.get(title).isDataFileExists()){
				return false;
			}
		}
		return true;
	}
	
	private void bindService(){
        if(mServiceConnection==null){
            mServiceConnection=new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    Log.d(TAG,"onServiceConnected");
					RefreshService.DataBinder myServiceBinder=(RefreshService.DataBinder)iBinder;
                    mService=myServiceBinder.getService();
					mService.setUpdateListener(ListFragment.this);
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
