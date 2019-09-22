package com.sarkariresult.android;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.net.*;
import android.support.design.widget.*;
import com.sarkariresult.android.*;
import android.support.v7.app.*;

public class NetworkReceiver extends BroadcastReceiver
{

	private ConnectivityManager manager;
	private OnNetworkChangeListener listener;
	private ContextWrapper context;
	
	private IntentFilter i = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
	public NetworkReceiver(ContextWrapper context,OnNetworkChangeListener listener){
		setNetworkChangeListener(listener);
		setContext(context);
	}

	public void setContext(ContextWrapper context)
	{
		this.context = context;
	}

	public ContextWrapper getContext()
	{
		return context;
	}

	
	public void register(){
		getContext().registerReceiver(this,i);
	}
	
	public void unregister(){
		getContext().unregisterReceiver(this);
	}
	@Override
	public void onReceive(Context context, Intent p2)
	{
		manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();
		if(listener != null){
			listener.onNetworkChange(isConnected);
		}
	}
	
	public void setNetworkChangeListener(OnNetworkChangeListener listener){
		this.listener = listener;
	}
	
	public interface OnNetworkChangeListener{
		public void onNetworkChange(boolean isConnected);
	}
}
