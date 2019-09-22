package com.sarkariresult.android;

import android.content.*;
import android.net.*;
import android.support.customtabs.*;
import android.support.v4.content.*;
import com.sarkariresult.android.*;

public class UrlHandler
{
	private Context context;
	private CustomTabsIntent.Builder builder;
	private CustomTabsIntent customTabsIntent;
	public UrlHandler(Context c){
		this.context = c;
		builder = new CustomTabsIntent.Builder();
		builder.setShowTitle(true);
		builder.setToolbarColor(ContextCompat.getColor(context,R.color.colorPrimary));
		builder.setExitAnimations(context, R.anim.right_to_left_end, R.anim.left_to_right_end);
        builder.setStartAnimations(context, R.anim.left_to_right_start, R.anim.right_to_left_start);
		customTabsIntent = builder.build();
		
	}
	
	public void handle(String url){
		customTabsIntent.launchUrl((MainActivity)this.context, Uri.parse(url));
	}
}
