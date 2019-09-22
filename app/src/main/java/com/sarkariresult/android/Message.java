package com.sarkariresult.android;
import android.graphics.*;
import android.support.design.widget.*;
import android.view.*;
import android.widget.*;

public class Message
{
	public static final int TYPE_ERROR = 0;
	public static final int TYPE_SUCCESS = 1;
	private View container;
	private String msg;
	private int type;
	private Snackbar snackbar;
	
	public Message(View container,String msg,int type){
		this.container = container;
		this.msg = msg;
		this.type = type;
	}
	
	public Message create(){
		int duration = 0;
		int backgroundColor = 0;
		if(this.type == TYPE_ERROR){
			duration = Snackbar.LENGTH_INDEFINITE;
			backgroundColor = Color.parseColor("#dd0000");
		}else if(this.type == TYPE_SUCCESS){
			duration = Snackbar.LENGTH_SHORT;
			backgroundColor = Color.parseColor("#009d55");
		}
		snackbar = Snackbar.make(this.container,this.msg,duration);
		snackbar.getView().setBackgroundColor(backgroundColor);
		return this;
	}
	
	public void show(){
		if(snackbar != null)this.snackbar.show();
	}
}
