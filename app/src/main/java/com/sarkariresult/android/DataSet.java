package com.sarkariresult.android;

import android.content.*;
import android.util.*;
import java.io.*;
import java.util.*;

public class DataSet extends Object implements Serializable
{
	
	private File dataFile;
	private String dataFileName;
	private String dataFilePath;
	private String dataTitle;
	private String dataUrl;
	private ArrayList<Data> data;
	private boolean isDataFileExists;
	private String dateText;
	
	
	public static DataSet newInstance(Context context,String title)
	{
		DataSet temp = new DataSet();
		temp.setDataTitle(title);
		temp.setDataUrl(Utils.getUrlFromTitle(context,title));
		temp.setDataFileName(Utils.getDataFileNameFromTitle(title));
		temp.setDataFilePath(Utils.getDataFilePathFromTitle(title));
		temp.setDataFile(new File(temp.getDataFilePath()));
		temp.setIsDataFileExists(temp.getDataFile().exists());
		temp.refreshData();
		return temp;
	}
	
	public void refreshData(){
		if (isDataFileExists())
		{
			setDateText(Utils.getDateText(getDataFile().lastModified()));
			try
			{
				if (isHome())
				{
					setData(Utils.parseTopLinks(getDataFile()));
				}
				else
				{
					setData(Utils.parseList(getDataFile()));
				}
			}
			catch (IOException e)
			{
				Log.e("DataSet",e.getMessage());
			}
		}
		else
		{
			setData(new ArrayList<Data>());
		}
	}
	
	public void setDataTitle(String dataTitle)
	{
		this.dataTitle = dataTitle;
	}

	public String getDataTitle()
	{
		return dataTitle;
	}
	
	public void setDataFilePath(String dataFilePath)
	{
		this.dataFilePath = dataFilePath;
	}

	public String getDataFilePath()
	{
		return dataFilePath;
	}
	
	public void setDateText(String dateText)
	{
		this.dateText = dateText;
	}

	public String getDateText()
	{
		return dateText;
	}
	
	public void setIsDataFileExists(boolean isDataFileExists)
	{
		this.isDataFileExists = isDataFileExists;
	}

	public boolean isDataFileExists()
	{
		return isDataFileExists;
	}
	public void setDataFile(File dataFile)
	{
		this.dataFile = dataFile;
	}

	public File getDataFile()
	{
		return dataFile;
	}

	public void setDataFileName(String dataFileName)
	{
		this.dataFileName = dataFileName;
	}

	public String getDataFileName()
	{
		return dataFileName;
	}

	public void setDataUrl(String dataUrl)
	{
		this.dataUrl = dataUrl;
	}

	public String getDataUrl()
	{
		return dataUrl;
	}

	public void setData(ArrayList<Data> data)
	{
		this.data = data;
	}

	public ArrayList<Data> getData()
	{
		return data;
	}
	
	public boolean isHome(){
		return getDataFileName().equals(Utils.HOME_FILE_NAME);
	}
	public ArrayList<Data> getData(int count){
		ArrayList<Data> temp = new ArrayList<Data>();
		if(count<=getData().size()){
			for(int i = 0; i<count; i++){
				temp.add(this.getData().get(i));
			}
			
		}
		return temp;
	}
}
