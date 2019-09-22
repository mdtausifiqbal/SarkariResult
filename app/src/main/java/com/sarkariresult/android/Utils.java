package com.sarkariresult.android;

import android.content.*;
import android.util.*;
import java.io.*;
import java.util.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import java.text.*;
public class Utils
{
	public static final String HOME_URL = "https://www.sarkariresult.com";
	public static final String APP_ROOT = "/data/data/com.sarkariresult.android/files";
	public static final String INTERNAL_PATH = "/sdcard/Sarkari Result";
	public static final String DATA_PATH = INTERNAL_PATH;
	public static final String HOME_FILE_NAME = "Home.php";
	private static String TAG = "Utils";
	
	public static ArrayList<Data> parseList(File file) throws IOException{
		Document doc = Jsoup.parse(file,"UTF-8");
		Data temp;
		ArrayList<Data> resultList = new ArrayList<Data>();
		Elements result = doc.select("div#post").first().select("li");
		for (Element e:result)
		{
			String link = e.select("a").last().attr("href");
			String name = e.text().replace("Last Date","\nLast Date");
			temp = new Data(name,link);
			resultList.add(temp);
		}
		
		return resultList;
	}
	
	public static ArrayList<Data> parseTopLinks(File file) throws IOException{
		Document doc = Jsoup.parse(file,"UTF-8");
		Data temp;
		ArrayList<Data> resultList = new ArrayList<Data>();
		Elements div = doc.select("div[id^='image']");
		for(Element e:div){
			Element a = e.select("a").last();
			String name = a.text();
			String link = a.attr("href");
			temp = new Data(name,link);
			resultList.add(temp);
		}
		
		return resultList;
	}
	
	
	public static String getUrlFromTitle(Context c, String title)
	{
		String key = title.toLowerCase().replace(" ", "_");
		int identifier = c.getResources().getIdentifier(key+"_url", "string", c.getPackageName());
		return getUrlFromStringId(c,identifier);
	}
	
	
	public static int getIdFromTitle(Context c,String title){
		String key = title.toLowerCase().replace(" ","_");
		return c.getResources().getIdentifier(key+"_id","id",c.getPackageName());
	
	}
	
	public static String getUrlFromStringId(Context c,int id){
		return c.getResources().getString(id);
	}
	
	public static String[] getAllUrls(Context c){
		return c.getResources().getStringArray(R.array.all_urls);
	}
	
	public static String[] getTopUrls(Context c){
		return c.getResources().getStringArray(R.array.top_urls);
	}
	
	public static String[] getAllTitles(Context c){
		return c.getResources().getStringArray(R.array.all_titles);
	}

	public static String[] getTopTitles(Context c){
		return c.getResources().getStringArray(R.array.top_titles);
	}
	
	public static String getDataFileNameFromTitle(String title){
		return title.concat(".php");
	}
	
	public static String getDataFilePathFromTitle(String title){
		return DATA_PATH+"/"+getDataFileNameFromTitle(title);
	}
	
	public static String getDateText(long time){
		Date date = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy hh:mm:ss a");
		return sdf.format(date);
	}
	
	public static boolean copyFile(String sourcePath,String destPath){
		try
		{
			File file = new File(sourcePath);
			String read = readDataFile(file);
			writeFile(destPath, read);
			file = null;
			read = null;
			return true;
		}
		catch (IOException e)
		{
			Log.e(TAG,e.getMessage());
			return false;
		}
	}
	public static void writeFile(String path, String text)
	{
		try
		{
			File file = new File(path);
			File parentFile = file.getParentFile();
			if(!parentFile.exists())parentFile.mkdir();
			FileWriter writer = new FileWriter(file);
			writer.write(text);
			writer.flush();
			writer.close();
		}
		catch (IOException e)
		{
			Log.e(TAG,e.getMessage());
		}
	}

	public static String readDataFile(File file) throws IOException{
		return Jsoup.parse(file,"UTF-8").toString();
		
	}
	

	
}
