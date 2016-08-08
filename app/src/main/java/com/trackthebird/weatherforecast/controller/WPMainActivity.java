package com.trackthebird.weatherforecast.controller;
/*	By Naveenu Perumunda	*/
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.trackthebird.weatherforecast.R;
import com.trackthebird.weatherforecast.model.Http;
import com.trackthebird.weatherforecast.model.WeatherItem;
import com.trackthebird.weatherforecast.view.WeatherForecastRecyclerViewAdapter;
/*
 * Created by Naveenu on 9/07/2016.
 */
public class WPMainActivity extends AppCompatActivity{

	private static final int REQUEST_ID_MULTIPLE_PERMISSIONS 	=   1;
	private Context                     context				    =	null;
    private RecyclerView 				mRecyclerView	        =	null;
    private RecyclerView.Adapter 		mAdapter		        =	null;
    private RecyclerView.LayoutManager	mLayoutManager	        =	null;
    private ProgressDialog              mShowProgressDlg	    =	null;
    private String                      mForecastData		    =	null;

    /*
     * Initialize the application variable and create object
     */ 
	private void init(){
		context				=	this;
		mRecyclerView 		=	(RecyclerView) findViewById(R.id.weather_forecast_recyclerview_item);
	    // Setting the adapter to RecyclerView
	    mLayoutManager 		=   new LinearLayoutManager(context);   // Creating a layout Manager
	    mRecyclerView.setLayoutManager(mLayoutManager);             // Setting the layout Manager
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wpmain);
		// Init application
		init();
        // Get Weather details.
        getWeatherDeatils(getResources().getString(R.string.action_settings_currently));
	}

    @Override
    public void onStart(){
        super.onStart();
    }

	/*
 	 * 	Get Latitude and Longitude. Checks whether the device is Android 6.0 to check Permissions.
 	 */
	private Bundle getLatLang(){
		if(Build.VERSION.SDK_INT	>= 23){
			// Get GPS access permission
			if(getGpsAccessPermission()){
				// Get location details if app has access to GPS
				return	getLocationDetails();
			}
		}
		else{
			// Get location detail if app has access to GPS
			return	getLocationDetails();
		}
		return null;
	}

    /*
     * Display Weather Info Async Task
     */
	private class DisplayWeatherDetailsAsyncTask extends AsyncTask<Bundle, Void, List<WeatherItem>>{
        String title        =   null;
        @Override
        protected List<WeatherItem> doInBackground(Bundle... bundles) {
            Bundle params                       =   bundles[0];
            List <WeatherItem> rowListItem 		= 	new ArrayList <WeatherItem>();;
            WeatherItem		weatherItem			=	null;
            this.title                          =   params.getString("weather_type");
            try {
                if(params.getString("weather_type").equalsIgnoreCase(getResources().getString(R.string.action_settings_currently))){
                    weatherItem		            =	new WeatherItem(context, new JSONObject(params.getString("response")).getJSONObject("currently"));
                    rowListItem.add(weatherItem);
                }
                else{
                    JSONArray	jsonArray		=	new JSONArray(new JSONObject(params.getString("response")).getJSONObject(params.getString("weather_type").toLowerCase()).getString("data"));
                    for(int i=0; i<jsonArray.length(); i++){
                        weatherItem		        =	new WeatherItem(context, jsonArray.getJSONObject(i));
                        rowListItem.add(weatherItem);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return rowListItem;
        }

        @Override
        protected void onPostExecute(List<WeatherItem> item){
            // Calls Recycler view adapter
            if(item.size() > 0){
                // Set Dynamic title based on the weather forecast
                ActionBar actionBar         =   getSupportActionBar();
                if (actionBar != null) {
                    actionBar.setTitle(this.title);
                }
                // Set Recycler View Adapter
                mAdapter			        =	new WeatherForecastRecyclerViewAdapter(context, item);
                mRecyclerView.setAdapter(mAdapter);
            }
            else{
                //Show Error message
                Toast.makeText(context, getResources().getString(R.string.unknow_error), Toast.LENGTH_SHORT).show();
            }
            DismissPopUp();
        }
    }

    /*
     * 	Get ApiKey from Manifest files
     */
	private String getApikey(String type) {
		String keys = null;
		try {
			ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			keys 					= (String)appInfo.metaData.get(type);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return keys;
	}

    /*
     * Gets the weather details from server based on Hourly or currently settings. Default is Currently.
     */
	private void getWeatherDeatils(String weatherType){
		// Get fresh weather data only if user action is "Refresh" or on launch time
		if((mForecastData != null) && (!weatherType.equalsIgnoreCase(getResources().getString(R.string.action_settings_refresh)))){
			Bundle params	=	new Bundle();
		    params.putString("response",        mForecastData);
		    params.putString("weather_type",    weatherType);
            //Show Popup
            ShowPopUp();
            // Display Weather details
            new DisplayWeatherDetailsAsyncTask().execute(params);
		}
		else if(isNetworkAvailable()){
            // Get Lat and Lang
            Bundle params	=	getLatLang();
            if(params != null){
                params.putString("weather_type",weatherType);
                //Show Popup
                ShowPopUp();
                //Call Asynchronously task and download weather details
                new GetWeatherForecastAsyncTask().execute(params);
            }
            else{
                Toast.makeText(context, getResources().getString(R.string.gps_location_error), Toast.LENGTH_SHORT).show();
            }
		}
		else{
			Toast.makeText(context, getResources().getString(R.string.internet_not_available), Toast.LENGTH_SHORT).show();
		}
	}
	
    /*
     * Async task class to get the Weather data from forecast server.
     */
	private class GetWeatherForecastAsyncTask extends AsyncTask<Bundle, Void, Bundle>{
		@Override
		protected Bundle doInBackground(Bundle... paramType) {
			Bundle params			=	new Bundle();
            String apiKey           =   getApikey("API_KEY");
			StringBuilder forecastWeatherUrl = new StringBuilder(getResources().getString(R.string.weather_fore_cast_url));
				forecastWeatherUrl.append(apiKey+ "/");
				forecastWeatherUrl.append(Double.toString(paramType[0].getDouble("latitude"))+ ",");
				forecastWeatherUrl.append(Double.toString(paramType[0].getDouble("longitude")));
				// Calls HTTP class
		        Http http 				= 	new Http();
		        try {
		        	mForecastData		=	http.read(forecastWeatherUrl.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
		    params.putString("response",mForecastData);
		    params.putString("weather_type", paramType[0].getString("weather_type"));
		    return params;
		}

		@Override
		protected void onPostExecute(Bundle params){ // This is in the UI thread
			if(params.size() > 1){
                new DisplayWeatherDetailsAsyncTask().execute(params);
			}
			else{
				Toast.makeText(context, getResources().getString(R.string.unknow_error), Toast.LENGTH_SHORT).show();
                DismissPopUp();
            }
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wpmain, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle App compact action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_current_weather) {
			getWeatherDeatils(getResources().getString(R.string.action_settings_currently));
			return true;
		}
		else if(id == R.id.action_hourly_weathers){
			getWeatherDeatils(getResources().getString(R.string.action_settings_hourly));
			return true;
		}
		else if (id == R.id.action_daily_weathers) {
			getWeatherDeatils(getResources().getString(R.string.action_settings_daily));
			return true;
		}
		else if (id == R.id.action_refresh_weathers) {
            ActionBar actionBar         =   getSupportActionBar();
            if (actionBar != null) {
                getWeatherDeatils(actionBar.getTitle().toString());
            }
            else{
                getWeatherDeatils(getResources().getString(R.string.action_settings_currently));
            }
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

    /*
     * Check whether Network is available or not before connecting network.
     */
	public boolean isNetworkAvailable(){
		try{
			ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		    // if no network is available networkInfo will be null
		    // otherwise check if we are connected
		    if (networkInfo != null && networkInfo.isConnected()) {
		        return true;
		    }
		} catch (Exception e){
			e.printStackTrace();
		}
		 return false;
	}

	/*
     * 	Gets GPS access permission
     */
	private boolean getGpsAccessPermission(){
		int permissionCoarseLocation 	    = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
		int permissionFineLocation 		    = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
		List<String> listPermissionsNeeded  = new ArrayList<>();
		if (permissionCoarseLocation != PackageManager.PERMISSION_GRANTED) {
			listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
		}
		if (permissionFineLocation != PackageManager.PERMISSION_GRANTED) {
			listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
		}
		if (!listPermissionsNeeded.isEmpty()) {
			ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
			return false;
		}
		return true;
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		switch (requestCode) {
			case REQUEST_ID_MULTIPLE_PERMISSIONS:
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// All good and Get Weather details
                    getWeatherDeatils(getResources().getString(R.string.action_settings_currently));
				}
                else {
					Toast.makeText(this, getResources().getString(R.string.gps_loation_is_not_available), Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
		}
	}

    /*
     * 	Gets location lat and long
     */
    private Bundle getLocationDetails(){
        Bundle	params	=	new Bundle();
        if(Build.VERSION.SDK_INT	>= 23){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)  != PackageManager.PERMISSION_GRANTED)
                return null;
        }
        LocationManager locManager 	= 	(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria 			= 	new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        String bestProvider 		= 	locManager.getBestProvider(criteria, true);
        Location location 			= 	locManager.getLastKnownLocation(bestProvider);
        if(location == null){
            location				=	locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if(location != null){
            params.putDouble("latitude",location.getLatitude());
            params.putDouble("longitude",location.getLongitude());
        }
        return params;
    }

    /*
     * Show popup message while downloading search items
     */
	protected void ShowPopUp(){
        this.runOnUiThread(new Runnable() {
            public void run() {
            	mShowProgressDlg = new ProgressDialog(context);
            	mShowProgressDlg.setTitle(getResources().getString(R.string.forecast_weather_popup_title));
            	mShowProgressDlg.setMessage(getResources().getString(R.string.please_wait));
        		mShowProgressDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        		mShowProgressDlg.setIcon(R.drawable.ic_launcher);
        		mShowProgressDlg.setCancelable(true);
        		mShowProgressDlg.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.close), new DialogInterface.OnClickListener() {
        		    @Override
        		    public void onClick(DialogInterface dialog, int which) {
        		    	try{
        			        dialog.cancel();
        			        dialog= null;
        			        mShowProgressDlg.dismiss();
        		    	}
        		    	catch(Exception e){
        		    	}
        		    }
        		});
        		mShowProgressDlg.show();
            }
        });
	}

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    /*
     * 	Dismiss popup
     */
	protected void DismissPopUp(){
		if(mShowProgressDlg != null && mShowProgressDlg.isShowing()){
			mShowProgressDlg.dismiss();
		}
	}
	
	@Override
	public void onStop(){
		super.onStop();
		//Make sure that Popup dialog is closed before closing the app.
		if(mShowProgressDlg != null){
			DismissPopUp();
		}
	}

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
