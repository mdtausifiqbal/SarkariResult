package com.sarkariresult.android;
import java.io.*;

public class Data implements Serializable
{
	private String name;
	private String url;

	public Data(String name, String url)
	{
		this.name = name;
		this.url = url;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getUrl()
	{
		return url;
	}

	@Override
	public String toString()
	{
		return this.name;
	}

	@Override
	public boolean equals(Object o)
	{
		Data a = (Data) o;
		return (this.getName().equals(a.getName()) && this.getUrl().equals(a.getUrl()));
	}
}
