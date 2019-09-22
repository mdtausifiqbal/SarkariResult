package com.sarkariresult.android;
import android.content.*;
import android.support.v7.widget.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import android.view.View.*;
import android.widget.AdapterView.*;

public class AdvanceAdapter extends RecyclerView.Adapter<AdvanceAdapter.MyViewHolder>
{
	
	private String[] titles;
	private ListFragment fragment;
	private MainActivity context;
	private HashMap<String,DataSet> dataSets;
	public AdvanceAdapter(ListFragment fragment)
	{
		this.context = fragment.context;
		this.dataSets = fragment.dataSets;
		this.titles = fragment.top_titles;
		this.fragment = fragment;
	}
	@Override
	public AdvanceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int p2)
	{
		View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.advance_item,parent,false);
		return new MyViewHolder(this.context,root);
	}

	@Override
	public void onBindViewHolder(AdvanceAdapter.MyViewHolder holder, int position)
	{
		String title = titles[position];
		DataSet dataSet = null;
		if(dataSets.containsKey(title)){
			dataSet = dataSets.get(title);
		}
		holder.getTitle().setText(title.replace("Home","Top Links"));
		holder.getSeeall().setTag(title);
		holder.getInnerList().setTag(R.string.data_key,dataSet.getData(8));
		holder.getInnerList().setTag(R.string.position_key,position);
		holder.getInnerList().setOnItemClickListener(item);
		holder.getSeeall().setOnClickListener(click);
		holder.getInnerList().setAdapter(new ArrayAdapter(this.context,android.R.layout.simple_list_item_1,dataSet.getData(8)));
	}

	@Override
	public int getItemCount()
	{
		// TODO: Implement this method
		return titles.length;
	}
	
	public class MyViewHolder extends RecyclerView.ViewHolder{
		private TextView title;
		private ListView innerList;
		private Button seeall;
		private MainActivity activity;
		public MyViewHolder(MainActivity activity,View v){
			super(v);
			this.activity = activity;
			title = (TextView) v.findViewById(R.id.title);
			innerList = (ListView) v.findViewById(R.id.innerList);
			seeall = (Button) v.findViewById(R.id.seeall);
			
		}

		public void setSeeall(Button seeall)
		{
			this.seeall = seeall;
		}

		public Button getSeeall()
		{
			return seeall;
		}

		public void setTitle(TextView title)
		{
			this.title = title;
		}

		public TextView getTitle()
		{
			return title;
		}

		public void setInnerList(ListView innerList)
		{
			this.innerList = innerList;
		}

		public ListView getInnerList()
		{
			return innerList;
		}
		
		
	}
	
	private View.OnClickListener click = new View.OnClickListener(){

		@Override
		public void onClick(View p1)
		{
			context.changeFragment(p1.getTag().toString());
		}
		
	};
	
	public ListView.OnItemClickListener item = new ListView.OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> p1, View p2, int position, long p4)
		{
			ArrayList<Data> data= (ArrayList<Data>) p1.getTag(R.string.data_key);
			fragment.position = (int)p1.getTag(R.string.position_key);
			fragment.handler.handle(data.get(position).getUrl());
		}
	};
}
