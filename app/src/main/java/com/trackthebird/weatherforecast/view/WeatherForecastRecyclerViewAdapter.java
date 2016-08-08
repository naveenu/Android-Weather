package com.trackthebird.weatherforecast.view;
/*	By Naveenu Perumunda	*/
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.trackthebird.weatherforecast.R;
import com.trackthebird.weatherforecast.model.WeatherItem;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/*
 * Created by Naveenu on 9/07/2016.
 */
public class WeatherForecastRecyclerViewAdapter extends RecyclerView.Adapter<WeatherForecastRecyclerViewAdapter.ViewHolder>{
	private static Context	context;

	private List<WeatherItem>	weatherInfo	=	null;
	private LayoutInflater	 inflater		=   null;
	
    /*
     * Creating a ViewHolder which extends the RecyclerView View Holder
     * ViewHolder are used to to store the inflated views in order to recycle them
     */
	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
		private LinearLayout	lLWeatherBackground	=	null;
		
		private TextView 	tvLastUpdatedTime		=	null;
        private TextView 	tvWeatherSummary		=	null;

        private ImageView 	ivWeatherStatusIcon		=	null;

        private TextView    tvPrecipIntensity       =   null;
        private TextView    tvPrecipProbability     =   null;
        private TextView    tvPrecipType            =   null;

		private TextView 	tvTemparature			=	null;

        private TextView 	tvTemperatureMin	    =	null;
        private TextView 	tvTemperatureMax		=	null;
        private TextView 	tvApparentTemperature	=	null;



		private TextView 	tvHumidity				=	null;
		private TextView 	tvDewPoint				=	null; 
		private TextView 	tvPressure				=	null; 
		
		
		private TextView 	tvWindSpeed				=	null;

        private TextView 	tvWindBearing		    =	null;
        private TextView 	tvCloudCover		    =	null;

		private TextView	tvVisibility			=	null;
		private TextView	tvOzone					=	null;
		
		private TextView tvNearstStormDistance		=	null;
		
        private int mDispayViewOriginalHeight		= 	0;
        private boolean mIsViewExpanded 			= 	false;
        
		public ViewHolder(View itemView,int ViewType) {                 // Creating a ViewHolder Constructor with View and viewType As a parameter
			super(itemView);
			itemView.setClickable(true);
            lLWeatherBackground     	    = (LinearLayout) itemView.findViewById(R.id.linearLayoutWeatherForecast);
			// Set the appropriate view in accordance with the the view type as passed when the holder object is created
			tvLastUpdatedTime				= (TextView) itemView.findViewById(R.id.tvLastUpdatedTime);
			
			ivWeatherStatusIcon				= (ImageView) itemView.findViewById(R.id.ivWeatherStatusIcon); 	
			tvWeatherSummary 				= (TextView) itemView.findViewById(R.id.tvWeatherSummary);
			tvTemparature 					= (TextView) itemView.findViewById(R.id.tvTemparature);

            tvPrecipIntensity               = (TextView) itemView.findViewById(R.id.tvPrecipIntensity);
            tvPrecipProbability             = (TextView) itemView.findViewById(R.id.tvPrecipProbability);
            tvPrecipType                    = (TextView) itemView.findViewById(R.id.tvPrecipType);

            tvTemperatureMin                = (TextView) itemView.findViewById(R.id.tvTemperatureMin);
            tvTemperatureMax                = (TextView) itemView.findViewById(R.id.tvTemperatureMax);
            tvApparentTemperature           = (TextView) itemView.findViewById(R.id.tvApparentTemperature);

			tvHumidity						= (TextView) itemView.findViewById(R.id.tvHumidity);
			tvDewPoint	 					= (TextView) itemView.findViewById(R.id.tvDewPoint);
			tvPressure		 				= (TextView) itemView.findViewById(R.id.tvPressure);

            tvWindBearing		 		    = (TextView) itemView.findViewById(R.id.tvWindBearing);
            tvCloudCover		 		    = (TextView) itemView.findViewById(R.id.tvCloudCover);

			tvWindSpeed						= (TextView) itemView.findViewById(R.id.tvWindSpeed);
			tvVisibility					= (TextView) itemView.findViewById(R.id.tvVisibility);
			tvOzone	 						= (TextView) itemView.findViewById(R.id.tvOzone);

			tvNearstStormDistance	 		= (TextView) itemView.findViewById(R.id.tvNearstStormDistance);
			
			// Set listener
			itemView.setOnClickListener(this);
			itemView.setLongClickable(true);
		}

		@SuppressLint("NewApi")
		@Override
		public void onClick(final View view) {
		    float subViewHeightFactor	=	0.88F;
		    LinearLayout layout			= 	(LinearLayout) view.findViewById(R.id.sub_list_item);
		    layout.setVisibility(View.VISIBLE);
		    notifyItemChanged(getAdapterPosition()-1);
        	if (mDispayViewOriginalHeight == 0) {
        		mDispayViewOriginalHeight = view.getHeight();
            }
           	ValueAnimator valueAnimator;
            if (!mIsViewExpanded) {
                mIsViewExpanded = true;
                valueAnimator = ValueAnimator.ofInt(mDispayViewOriginalHeight, mDispayViewOriginalHeight + (int) (mDispayViewOriginalHeight * subViewHeightFactor));
            } else {
                mIsViewExpanded = false;
                valueAnimator = ValueAnimator.ofInt(mDispayViewOriginalHeight + (int) (mDispayViewOriginalHeight *  subViewHeightFactor), mDispayViewOriginalHeight);
            }
            valueAnimator.setDuration(300);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    Integer value = (Integer) animation.getAnimatedValue();
                    view.getLayoutParams().height = value.intValue();
                    view.requestLayout();
                }
            });
            valueAnimator.start();
        }
	}
    /*
     * 	Constructor
     */
	public WeatherForecastRecyclerViewAdapter(Context context, List<WeatherItem> info){
		this.context				=	context;
		this.weatherInfo			=	info;
		inflater					=	LayoutInflater.from(context);
	}

	/*
	 * Below first we override the method onCreateViewHolder which is called when the ViewHolder is(non-Javadoc)
	 * @see android.support.v7.widget.RecyclerView.Adapter#onCreateViewHolder(android.view.ViewGroup, int)
	 * Created, In this method we inflate the weather_forecast_recyclerview_item.xml layout
	 * and pass it to the view holder
	 */

	@Override
	public WeatherForecastRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view =	inflater.inflate(R.layout.weather_forecast_recyclerview_item,parent,false); //Inflating the layout
		ViewHolder vhItem = new ViewHolder(view,viewType); //Creating ViewHolder and passing the object of type view
		return vhItem; // Returning the created object
	}
	
    /*
     * Next we override a method which is called when the item in a row is needed to be displayed, here the int position
     * Tells us item at which position is being constructed to be displayed and the holder id of the holder object tell us
     * which view type is being created 1 for item row
     */
	@Override
	public void onBindViewHolder(final WeatherForecastRecyclerViewAdapter.ViewHolder holder, int position) {
		if(weatherInfo.size() > 0){
			WeatherItem item		=	weatherInfo.get(position);
            if(item != null){
                //Sets Last updated time
                holder.tvLastUpdatedTime.setText(item.getTime());

                //Sets Weather status icon and set the background status
                holder.lLWeatherBackground.setBackgroundResource(item.getIcon());
                holder.ivWeatherStatusIcon.setImageBitmap(item.getBitmapIcon());

                //Set Summary
                holder.tvWeatherSummary.setText(item.getSummary());

                //Sets Temperature
                holder.tvTemparature.setText(item.getTemperature());

                //Sets Maximim Temperature
                holder.tvTemperatureMax.setText(item.getMaximumTemperature());

                //Sets Minimum Temperature
                holder.tvTemperatureMin.setText(item.getMinimumTemperature());

                //Sets Apparent Temperature
                holder.tvApparentTemperature.setText(item.getApparentTemperature());

                //Sets Precipitation Humidity
                holder.tvPrecipIntensity.setText(item.getPrecipIntensity());

                //Sets Precipitation Probability
                holder.tvPrecipProbability.setText(item.getPrecipProbability());

                //Sets Precipitation Type
                holder.tvPrecipType.setText(item.getPrecipType());

                //Sets Last Humidity
                holder.tvHumidity.setText(item.getHumidity());

                //Sets Dew Point
                holder.tvDewPoint.setText(item.getDewPoint());

                //Sets Pressure
                holder.tvPressure.setText(item.getPressure());

                //Sets Wind Speed
                holder.tvWindSpeed.setText(item.getWindSpeed());

                //Sets Wind Bearing
                holder.tvWindBearing.setText(item.getWindBearing());

                //Sets Cloud Cover
                holder.tvCloudCover.setText(item.getCloudCover());

                //Sets Visibility
                holder.tvVisibility.setText(item.getVisibility());

                //Sets Ozone
                holder.tvOzone.setText(item.getOzone());

                //Sets Nearest storm distance
                holder.tvNearstStormDistance.setText(item.getNearestStormDistance());
            }
		}
	}

    /*
     * 	This method returns the number of items present in the list
     */
	@Override
	public int getItemCount() {
		return weatherInfo.size();
	}

	@Override
	public int getItemViewType(int position) { 
		return 0;
	}
}