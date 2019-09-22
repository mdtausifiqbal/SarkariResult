package com.sarkariresult.android;

import android.content.*;
import java.util.*;

public class UpdateChecker
{
	private ArrayList<Data> newUpdateList = new ArrayList<Data>();
	
	public void checkForUpdate(ArrayList<Data> oldList,ArrayList<Data> newList){
		newUpdateList.clear();
		for(Data data:newList){
			if(!oldList.contains(data)){
				newUpdateList.add(data);
			}
		}
	}
	
	public boolean isUpdateAvailable(){
		return (newUpdateList.size() != 0);
	}
	
	public ArrayList<Data> getAllNotice(){
		return newUpdateList;
	}
}
