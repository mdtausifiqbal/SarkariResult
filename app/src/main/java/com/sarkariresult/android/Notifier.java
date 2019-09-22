package com.sarkariresult.android;

import android.app.*;
import android.content.*;
import android.support.v4.app.*;
import java.util.*;

public class Notifier
{
	private Context context;
	private NotificationManagerCompat manager;
	private NotificationCompat.Builder builder;
	public static int id = 2;
	public Notifier(Context context)
	{
		id = 2;
		this.context = context;
		manager = NotificationManagerCompat.from(context);
		builder = new NotificationCompat.Builder(context);
	}
	
	public void sendNotice(String title,ArrayList<Data> alldata){
		Intent intent = new Intent(context,MainActivity.class);
		intent.putExtra(Intent.EXTRA_TITLE,title);
		builder.setContentTitle(title);
		builder.setContentText(alldata.size() + " new updates");
		NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
		for(Data data:alldata){
			style.addLine(data.getName());
		}
		style.setSummaryText(context.getString(R.string.website));
		builder.setStyle(style);
		builder.setSmallIcon(android.R.drawable.ic_dialog_info);
		builder.setContentIntent(PendingIntent.getActivity(context,0,intent,0));
		builder.setAutoCancel(true);
		manager.notify(id,builder.build());
		id++;
	}
}
