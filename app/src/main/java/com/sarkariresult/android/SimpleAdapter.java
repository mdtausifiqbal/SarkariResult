package com.sarkariresult.android;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import android.view.View.*;

public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.MyViewHolder>
{

	private ArrayList<Data> data;
	public ListFragment fragment;
	
	public SimpleAdapter(ListFragment fragment)
	{
		this.data = fragment.dataSet.getData();
		this.fragment = fragment;
	}
	
	@Override
	public SimpleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int p2)
	{
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_item,parent,false);
		return new MyViewHolder(v);
	}

	@Override
	public void onBindViewHolder(SimpleAdapter.MyViewHolder holder, int position)
	{
		holder.tv.setText(data.get(position).getName());
		holder.parent.setTag(R.string.url_key,data.get(position).getUrl());
		holder.parent.setTag(R.string.position_key,position);
		holder.parent.setOnClickListener(listner);
	}

	@Override
	public int getItemCount()
	{
		// TODO: Implement this method
		return data.size();
	}
	
	public class MyViewHolder extends RecyclerView.ViewHolder{
		public TextView tv;
		public View parent;
		public MyViewHolder(View v){
			super(v);
			parent = v;
			tv = (TextView) v.findViewById(android.R.id.text1);
		}
		
	}

	public View.OnClickListener listner = new View.OnClickListener(){

		@Override
		public void onClick(View p1)
		{
			fragment.position = (int)p1.getTag(R.string.position_key);
			fragment.handler.handle(p1.getTag(R.string.url_key).toString());
		}
		
		
	};
}
