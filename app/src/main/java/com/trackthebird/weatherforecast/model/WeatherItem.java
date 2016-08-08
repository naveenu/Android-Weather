package com.trackthebird.weatherforecast.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.trackthebird.weatherforecast.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by naveenu on 9/07/2016.
 */
public class WeatherItem {

    public static String DATE_FORMAT		= 	"MMM dd, yyyy hh:mm:ss a";

    // Weather related strings
    private String time;
    private String summary;
    private int icon;
    private Bitmap bitmapIcon;
    private String precipIntensity;
    private String precipProbability;
    private String precipType;
    private String temperature;
    private String apparentTemperature;
    private String minimumTemperature;
    private String maximumTemperature;

    private String dewPoint;
    private String humidity;
    private String windSpeed;
    private String windBearing;
    private String visibility;
    private String cloudCover;
    private String pressure;
    private String ozone;
    private String nearestStormDistance;

    //  Constructor
    public WeatherItem( Context context, JSONObject jsonObject) {
        try{
            this.time                   =   jsonObject.has("time")                  ?
                    getDateAndTime(jsonObject.getString("time"))
                    : null;

            this.summary                =   jsonObject.has("summary")               ?
                    jsonObject.getString("summary")
                    : null;

            this.icon                   =   jsonObject.has("icon")                  ?
                    getWeatherBackgroundGradient(jsonObject.getString("icon"))
                    : 0;

            this.bitmapIcon             =   jsonObject.has("icon")                  ?
                    getWeatherIcon(context, jsonObject.getString("icon"))
                    : null;

            this.precipIntensity        =   jsonObject.has("precipIntensity")       ?
                    context.getResources().getString(R.string.precipIntensity)
                            .concat(" : ")
                            .concat(jsonObject.getString("precipIntensity"))
                            .concat(" mm/hr")
                    : null;

            this.precipProbability      =   jsonObject.has("precipProbability")     ?
                    context.getResources().getString(R.string.precipProbability)
                            .concat(" : ")
                            .concat(jsonObject.getString("precipProbability"))
                    : null;

            this.precipType             =   jsonObject.has("precipType")            ?
                    context.getResources().getString(R.string.precipType)
                            .concat(" : ")
                            .concat(jsonObject.getString("precipType"))
                    : null;

            this.temperature            =   jsonObject.has("temperature")           ?
                    context.getResources().getString(R.string.temperature)
                            .concat(" : ")
                            .concat(convertFahrenheitToCelcius(jsonObject.getString("temperature")))
                            .concat(" \u2103")
                    :  null;

            this.apparentTemperature    =   jsonObject.has("apparentTemperature")   ?
                    context.getResources().getString(R.string.apparentTemperature)
                            .concat(" : ")
                            .concat(convertFahrenheitToCelcius(jsonObject.getString("apparentTemperature")))
                            .concat(" \u2103")
                    : null;

            this.minimumTemperature     =   jsonObject.has("temperatureMin")        ?
                    context.getResources().getString(R.string.temperatureMin)
                            .concat(" : ")
                            .concat(convertFahrenheitToCelcius(jsonObject.getString("temperatureMin")))
                            .concat(" \u2103")
                    : null;

            this.maximumTemperature     =   jsonObject.has("temperatureMax")        ?
                    context.getResources().getString(R.string.temperatureMax)
                            .concat(" : ")
                            .concat(convertFahrenheitToCelcius(jsonObject.getString("temperatureMax")))
                            .concat(" \u2103")
                    : null;

            this.dewPoint               =   jsonObject.has("dewPoint")              ?
                    context.getResources().getString(R.string.dewPoint)
                            .concat(" : ")
                            .concat(jsonObject.getString("dewPoint"))
                            .concat(" \u2109")
                    : null;

            this.humidity               =   jsonObject.has("humidity")              ?
                    context.getResources().getString(R.string.humidity)
                            .concat(" : ")
                            .concat(getHumidityInpercent(jsonObject.getString("humidity")))
                            .concat("%")
                    : null;

            this.windSpeed              =   jsonObject.has("windSpeed")             ?
                    context.getResources().getString(R.string.windSpeed)
                            .concat(" : ")
                            .concat(getSpeedInKilloMeter(jsonObject.getString("windSpeed")))
                            .concat(" km")
                    : null;

            this.windBearing            =   jsonObject.has("windBearing")           ?
                    context.getResources().getString(R.string.windBearing)
                            .concat(" : ")
                            .concat(jsonObject.getString("windBearing"))
                            .concat(" \u00B0")
                    : null;

            this.visibility             =   jsonObject.has("visibility")            ?
                    context.getResources().getString(R.string.visibility)
                            .concat(" : ")
                            .concat(jsonObject.getString("visibility"))
                            .concat(" Miles")
                    : null;

            this.cloudCover             =   jsonObject.has("cloudCover")            ?
                    context.getResources().getString(R.string.cloudCover)
                            .concat(" : ")
                            .concat(jsonObject.getString("cloudCover"))
                    : null;

            this.pressure               =   jsonObject.has("pressure")              ?
                    context.getResources().getString(R.string.pressure)
                            .concat(" : ")
                            .concat(jsonObject.getString("pressure"))
                            .concat(" M bars")
                    : null;

            this.ozone                  =   jsonObject.has("ozone")                 ?
                    context.getResources().getString(R.string.ozone)
                            .concat(" : ")
                            .concat(jsonObject.getString("ozone"))
                            .concat(" Dobson")
                    : null;

            this.nearestStormDistance   =   jsonObject.has("nearestStormDistance")  ?
                    context.getResources().getString(R.string.nearestStormDistance)
                            .concat(" : ")
                            .concat(jsonObject.getString("nearestStormDistance"))
                    : null;
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Get Time
    public String getTime() {
        return time;
    }

    // Get Summary
    public String getSummary() {
        return summary;
    }

    // get Icon name
    public int getIcon() {
        return icon;
    }

    // Get Bitmap Icon name
    public Bitmap getBitmapIcon() {
        return bitmapIcon;
    }

    // Get Intensity
    public String getPrecipIntensity() {
        return precipIntensity;
    }

    // Get Probability
    public String getPrecipProbability() {
        return precipProbability;
    }

    // Get Type
    public String getPrecipType() {
        return precipType;
    }

    // Get Temperature
    public String getTemperature() {
        return temperature;
    }

    // Get Minimum Temperature
    public String getMinimumTemperature() {
        return minimumTemperature;
    }

    // Get Maximum Temperature
    public String getMaximumTemperature() {
        return maximumTemperature;
    }

    // Get Apparent Temperature
    public String getApparentTemperature() {
        return apparentTemperature;
    }

    // Get Dew Point
    public String getDewPoint() {
        return dewPoint;
    }

    // Het Humidity
    public String getHumidity() {
        return humidity;
    }

    // Het Wind Speed
    public String getWindSpeed() {
        return windSpeed;
    }

    // Get Wind Bearing
    public String getWindBearing() {
        return windBearing;
    }

    // Get Visibility
    public String getVisibility() {
        return visibility;
    }

    // Het Cloud Cover
    public String getCloudCover() {
        return cloudCover;
    }

    // Get Pressure
    public String getPressure() {
        return pressure;
    }

    // Get Ozone
    public String getOzone() {
        return ozone;
    }

    // Get Near Storm Distance
    public String getNearestStormDistance() {
        return nearestStormDistance;
    }

    /*
     * Converts to Celcius
     */
    private String convertFahrenheitToCelcius(String fahrenheit) {
        return String.format("%.2f", (Float.parseFloat(fahrenheit) - 32) * 5 / 9);
    }

    /*
     * 	Get weather background color
     */
    private int getWeatherBackgroundGradient(String iconType){
        if(iconType.equalsIgnoreCase("clear-day")){
            return R.drawable.gradient_background_clear_day;
        }
        else if(iconType.equalsIgnoreCase("clear-night")){
            return R.drawable.gradient_background_clear_night;
        }
        else if(iconType.equalsIgnoreCase("rain")){
            return R.drawable.gradient_background_cloudy;
        }
        else if(iconType.equalsIgnoreCase("snow")){
            return R.drawable.gradient_background_snow;
        }
        else if(iconType.equalsIgnoreCase("sleet")){
            return R.drawable.gradient_background_sleet;
        }
        else if(iconType.equalsIgnoreCase("wind")){
            return R.drawable.gradient_background_default;
        }
        else if(iconType.equalsIgnoreCase("fog")){
            return R.drawable.gradient_background_fog;
        }
        else if(iconType.equalsIgnoreCase("cloudy")){
            return R.drawable.gradient_background_cloudy;
        }
        else if(iconType.equalsIgnoreCase("partly-cloudy-day")){
            return R.drawable.gradient_background_cloudy;
        }
        else if(iconType.equalsIgnoreCase("partly-cloudy-night")){
            return R.drawable.gradient_background_cloudy_night;
        }
        return R.drawable.gradient_background_default;
    }

    /*
     * 	Get Wheather Icos
     */
    private Bitmap getWeatherIcon(Context context, String iconType){
        if(iconType.equalsIgnoreCase("clear-day")){
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_clear_day);
        }
        else if(iconType.equalsIgnoreCase("clear-night")){
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_clear_night);
        }
        else if(iconType.equalsIgnoreCase("rain")){
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_rain);
        }
        else if(iconType.equalsIgnoreCase("snow")){
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_snow);
        }
        else if(iconType.equalsIgnoreCase("sleet")){
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_sleet);
        }
        else if(iconType.equalsIgnoreCase("wind")){
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_wind);
        }
        else if(iconType.equalsIgnoreCase("fog")){
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_fog);
        }
        else if(iconType.equalsIgnoreCase("cloudy")){
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_cloudy);
        }
        else if(iconType.equalsIgnoreCase("partly-cloudy-day")){
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_partly_cloudy);
        }
        else if(iconType.equalsIgnoreCase("partly-cloudy-night")){
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_partly_cloudy_night);
        }
        return null;
    }

    /*
     * Get String Date and Time
     */
    private String getDateAndTime(String timeInSeconds){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(timeInSeconds)*1000L);
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        return formatter.format(calendar.getTime());
    }

    /*
     * Convert speed in Miles to Km
     */
    private String getSpeedInKilloMeter(String speedInMiles){
        return String.format("%.2f", (Float.parseFloat(speedInMiles) *1.6));
    }

    /*
     * Get Humidity in percent
     */
    private String getHumidityInpercent(String humidity){
        return Float.toString(Float.parseFloat(humidity) *100);
    }
}
