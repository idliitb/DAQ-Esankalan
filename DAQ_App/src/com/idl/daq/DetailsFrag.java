package com.idl.daq;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TabHost;

import com.daq.formula.Formula;
import com.daq.sensors.Sensor;
import com.daq.tabsswipe.adapter.TabsPagerAdapter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;

public class DetailsFrag extends Fragment implements ActionBar.TabListener, LoaderCallbacks<Void>{

	private View rootView;
	private TabHost tabHost;
	private ViewPager viewPager;
	//to take the tabs out of the action bar
	private PagerTabStrip strip;
	private ActionBar actionBar;
	private TabsPagerAdapter mAdapter;
	private String[] tabs = {"Graphical", "Tabular" };
	//recieves sensor object from SensorListActivity in the form of the tabId 
	private Sensor mySensor;
	
	private Context c;
	private GlobalState gS;
	
	//needed for display
	private ArrayList<String> data,time,info;
	private ArrayAdapter<String> infoAdapter;
	
	int count;
	//why
	static int countFrag = 0;
	
	ArrayList<JSONObject> t;
	
	//a new series is being built for each DetailsFrag
	public GraphViewSeries series;
	private double TIME = 0;
	
	Callbacks sensorInfoCallbacks;
	
	public interface Callbacks{
		
		public ArrayAdapter<String> getArrayAdapter();
		
		public ArrayList<String> getArrayList();
		
		public GraphViewSeries getSeries();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
		
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}
		setRetainInstance(true);
		sensorInfoCallbacks = (Callbacks) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		TIME = 0;
		L.d("on create view called");
		countFrag++;
		rootView = inflater.inflate(R.layout.fragment_sensor_detail, container,
				false);
		viewPager = (ViewPager) rootView.findViewById(R.id.pager);
		strip = (PagerTabStrip) rootView.findViewById(R.id.pager_tabs);
		
		actionBar = getActivity().getActionBar();
		L.d("action bar selected");
		mAdapter = new TabsPagerAdapter(getChildFragmentManager());
		viewPager.setAdapter(mAdapter);
		L.d("view pager set");
//		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//		setupTabs();
		L.d("setup tabs");
		
		
		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
//				actionBar.setSelectedNavigationItem(position);
				L.d("action bar position: "+position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		data = new ArrayList<String>();
		time = new ArrayList<String>();
		count = 0;
		//load the asyc task
		getLoaderManager().initLoader(0, null, this);
		return rootView;
	}

	private void setupTabs() {
		// TODO Auto-generated method stub
		for(String tab : tabs){
			actionBar.addTab(actionBar.newTab().setText(tab)
					.setTabListener(this));
		}
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		viewPager.setCurrentItem(tab.getPosition());
		L.d("tab position: "+tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
//		viewPager.setCurrentItem(tab.getPosition());
//		L.d("tab position: "+tab.getPosition());
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	public void setSensor(Sensor sensor) {
		// TODO Auto-generated method stub
		mySensor = sensor;
	}

	public void setContext(Context c) {
		// TODO Auto-generated method stub
		this.c = c;
		gS = (GlobalState) c;
	}

//	@Override
//	public Context getContext() {
//		// TODO Auto-generated method stub
//		return c;
//	}
//
//	@Override
//	public Sensor getSensor() {
//		// TODO Auto-generated method stub
//		return mySensor;
//	}

	@Override
	public Loader<Void> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		AsyncTaskLoader<Void> loader = new AsyncTaskLoader<Void>(getActivity()) {

			@Override
			public Void loadInBackground() {
				try {
					// simulate some time consuming operation going on in the
					// background
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				return null;
			}
		};
		// somehow the AsyncTaskLoader doesn't want to start its job without
		// calling this method
		loader.forceLoad();
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Void> arg0, Void arg1) {
		// TODO Auto-generated method stub
		loadData();
		getLoaderManager().restartLoader(0, null, this);
	}

	private void loadData() {
		// TODO Auto-generated method stub
		//it recieves the new data from SensorListActivity
		info = sensorInfoCallbacks.getArrayList();
		infoAdapter = sensorInfoCallbacks.getArrayAdapter();
		L.d("received adapters");
		L.d("Loading data");
		series = mAdapter.graph.series;
		L.d("Received series");
//		ArrayList<String> data = new ArrayList<String>();
//		ArrayAdapter<String> a = new ArrayAdapter<String>(c,android.R.layout.simple_list_item_1,data);
//		lv.setAdapter(a);
//		getLoaderManager().initLoader(0, null, this);
		t = gS.getTemp();
		for(int i=data.size();i<t.size();++i){
			L.d("value of i " + i);
			try {
				if(t.get(i).get("sensor_code").equals(mySensor.getSensorName())){
					for(Map.Entry<String, Formula> e : mySensor.getFormula().getFc().entrySet()){
						L.d(e.getKey()+" "+e.getValue());
					}
					Formula f = mySensor.getFormula().getFc().get(mySensor.getQuantity());
					String value = t.get(i).getString("data");
					String date = t.get(i).getString("date");
					//String[] r = info.split(":");
					//L.d(r.length);
					L.d(value+" "+date);
					final double val = Double.parseDouble(value);
					if(f==null){
						L.d("Problemo");
					}
					f.setValue(val);
					//f.getAllVariables().get(0).setValue(t.get(i).getDouble("data"));
//					try {
//						f.setValue(t.get(i).getDouble("data"));
//					} catch (Exception e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
					String s = "";
//					if(!mySensor.getFormula().toString().equals("")){
						mySensor.getFormula().evaluate();
						for(Map.Entry<String, Formula> e : mySensor.getFormula().getFc().entrySet()){
							s=e.getValue().getValue()+"";
						}
//					}
					//if there is no formula then the data needs to be directly displayed
//					L.d(s);
					//add the respective values to the series
					data.add(s);
					time.add(date);
					L.d(s+" "+date);
					if(info==null && infoAdapter==null){
						L.d("info or infoAdapter null hain");
					}
					if(series == null){
						L.d("Series null");
					}
					//notify the SensorList activity that a value has been recieved
					info.add(mySensor.getId()+":"+s+" Time:"+date);
					infoAdapter.notifyDataSetChanged();
					getActivity().runOnUiThread(new Runnable() {
			              @Override
			              public void run() {
			            	  //keep appending the particular list
			            	  JSONObject obj = mySensor.getJSON();
			                  try {
								series.appendData(new GraphView.GraphViewData(TIME + (float)(obj.getInt("rate")), val), true);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								series.appendData(new GraphView.GraphViewData(TIME ++, val), true);
								e.printStackTrace();
							}
			                  //Here I try few different things
			                  
			              }
			          });
					L.d("for fragment: " + countFrag + " data: "+ s);
					//data.add(t.get(i).getDouble("data")+"");
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//		a.notifyDataSetChanged();		
//		for(int i=0;i<100;++i){
//			try {
//				data.add(i + ": "+(new JSONObject(json).getString("sensorName")));
//			} catch (JSONException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
//		}
	}

	@Override
	public void onLoaderReset(Loader<Void> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		L.d("on Destroy of Deatilsfarag caleedf");
		mAdapter = null;
		super.onDestroy();
	}
	
	

//	@Override
//	public void sendArrayWithAdapter(ArrayAdapter<String> a,ArrayList<String> data) {
//		// TODO Auto-generated method stub
//		detailAdapter = a;
//		info = data;
//	}

}
