package com.sarkariresult.android;

import android.content.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.support.design.widget.*;
import android.support.v4.app.*;
import android.support.v4.widget.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;

import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import com.sarkariresult.android.NetworkReceiver.*;
import android.content.res.*;
import java.util.*;
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,OnNetworkChangeListener
{
	private static final String FACEBOOK = "https://m.facebook.com/pages/sarkariresultcom/499890650057530";
	private static final String TWITTER = "https://twitter.com/sarkari_result";
	private static final String WEBSITE = "https://www.sarkariresult.com";
	private static final String TAG = "MainActivity";
	
	private DrawerLayout drawerLayout;
	private Toolbar toolbar;
	private ActionBarDrawerToggle toggle;
	private NavigationView navView;
	
	private UrlHandler urlHandler;
	private FragmentTransaction ft;
	private FrameLayout frame;
	private ListFragment fragment;

	private NetworkReceiver netReceiver;
	private boolean firstLaunch;
	private boolean isGroupVisible = false;
	public boolean isInternetAvailable;
	
	private Intent launchIntent;
	private boolean doubleBackToExitPressedOnce = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		launchIntent = getIntent();
		initialize();
		firstLaunch = true;
		netReceiver = new NetworkReceiver(this,this);
		netReceiver.register();
		
	}

	
	@Override
	protected void onResume()
	{
		super.onResume();
		toggle.syncState();
		
	}

	@Override
	protected void onPause()
	{
		// TODO: Implement this method
		super.onPause();
		toggle.syncState();
		
	}

	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
		netReceiver.unregister();
	}

	private void initialize()
	{
		//Initializing views
		urlHandler = new UrlHandler(this);
		drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
		toolbar = (Toolbar) findViewById(R.id.main_toolbar);
		navView = (NavigationView) findViewById(R.id.main_nav_view);
		frame = (FrameLayout) findViewById(R.id.frame_content);
		
		//Setting up
		navView.setNavigationItemSelectedListener(this);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close);
		drawerLayout.setDrawerListener(toggle);
		
		fragment = new ListFragment(this,getFragmentName());
		ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.frame_content,fragment);
		ft.commit();
		getSupportActionBar().setSubtitle(getFragmentName());
		navView.getMenu().findItem(Utils.getIdFromTitle(this,getFragmentName())).setChecked(true);
		
	}

	@Override
	public boolean onNavigationItemSelected(MenuItem item)
	{

		
		switch(item.getItemId()){
			case R.id.facebook:
				urlHandler.handle(FACEBOOK);
				drawerLayout.closeDrawers();
				return true;
			case R.id.twitter:
				urlHandler.handle(TWITTER);
				drawerLayout.closeDrawers();
				return true;
			case R.id.more:
				isGroupVisible = !isGroupVisible;
				navView.getMenu().setGroupVisible(R.id.moreGroup,isGroupVisible);
				int icon = isGroupVisible?R.drawable.expand_less:R.drawable.expand_more;
				navView.getMenu().findItem(R.id.more).setIcon(icon);
				return false;
			default:
				drawerLayout.closeDrawers();
				changeFragment(item.getTitle().toString());
				return false;
		}
	}

	public void changeFragment(String title){
		getSupportActionBar().setSubtitle(title);
		fragment.updateFragment(title);
		navView.getMenu().findItem(Utils.getIdFromTitle(this,title)).setChecked(true);
	}
	
	private String getFragmentName(){
		
		if(launchIntent.hasExtra(Intent.EXTRA_TITLE)){
			return launchIntent.getStringExtra(Intent.EXTRA_TITLE);
		}else{
			return "Home";
		}
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main_option,menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// TODO: Implement this method
		switch(item.getItemId()){
			case R.id.website:
				urlHandler.handle(WEBSITE);
				return true;
			case R.id.feedback:
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse("mailto:mdtausifiqbalnwd@gmail.com?subject=Feedback for Sarkari Result"));
				startActivity(i);
				return true;	
			case R.id.aboutus:
				startActivity(new Intent(this,AboutActivity.class));
				return true;
			case R.id.refreshContent:
				fragment.refreshData();
				return true;
		}
		return false;
	}
	
	@Override
	public void onNetworkChange(boolean isConnected)
	{
		isInternetAvailable = isConnected;
		if (!firstLaunch)
		{
			if (isConnected)
			{
				Message msg = new Message(findViewById(R.id.main_content), "Connected to Internet", Message.TYPE_SUCCESS);
				msg.create().show();
				if(fragment.dataSet.isHome()){
					if(!fragment.isAllTopFilesExists())fragment.refreshData();
				}else{
					if(!fragment.dataSet.isDataFileExists())fragment.refreshData();
				}
			}
			else
			{
				Message msg = new Message(findViewById(R.id.main_content), "Not connected to Internet", Message.TYPE_ERROR);
				msg.create().show();
			}
		}
		else
		{
			firstLaunch = false;
		}

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		// TODO: Implement this method
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onBackPressed()
	{
		// TODO: Implement this method
		if(fragment.dataSet.isHome()){
			if (doubleBackToExitPressedOnce) {
				super.onBackPressed();
				return;
			}

			this.doubleBackToExitPressedOnce = true;
			Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

			new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						doubleBackToExitPressedOnce=false;                       
					}
				}, 900);
		}else{
			changeFragment("Home");
		}
		
	}
	
}
