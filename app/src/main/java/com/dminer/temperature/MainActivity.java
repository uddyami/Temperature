package com.dminer.temperature;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dminer.temperature.api.WeatherApi;
import com.dminer.temperature.model.Weather;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static final String TAG = "Temperature";
    private static final int REQUEST_LOCATION = 13;

    @Bind(R.id.tvResponse) TextView response;
    @Bind(R.id.llWeatherViewHolder) LinearLayout weatherViews;
    private WeatherApi apiService;
    private GoogleApiClient googleApiClient;
    private Location location;
    private double[] coordinates;
    private final double[] defaultCoordinates = {41.885575, -87.644408};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        apiService = getWeatherService();
        setupGoogleApiClient();
    }

    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    private void setupGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this,this,this)
                .addApi(LocationServices.API)
                .build();
    }

    private WeatherApi getWeatherService() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://forecast.weather.gov")
                .build();


        return retrofit.create(WeatherApi.class);
    }

    @OnClick(R.id.buttonGetData)
    public void ongetDataClicked() {
        Log.i(TAG, "button clicked");
        if(coordinates!=null) {
            fetchData();
        }
    }

    private void fetchData() {
        response.setText(R.string.loading);
        apiService.weather(coordinates[0], coordinates[1], "json")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Weather>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "on completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "error " + e.getMessage());
                        response.setText("Error \n"+e.getMessage());
                    }

                    @Override
                    public void onNext(Weather weather) {
                        Log.i(TAG, "we have response" + weather);
                        addViews(getDateArrayMap(weather));

                        response.setText("");

                    }
                });
    }

    private void addViews(ArrayMap<Date, String[]> dateArrayMap) {
        weatherViews.removeAllViews();
        SimpleDateFormat formatter=new SimpleDateFormat("EE");
        for(int i=0 ;i<6;i++){
           View view= getLayoutInflater().inflate(R.layout.single_day_weather,null);
            Date keyDate=dateArrayMap.keyAt(i);
            ((TextView)view.findViewById(R.id.tvDay)).setText(DateUtils.isToday(keyDate.getTime())?"TODAY": formatter.format(keyDate));
            ((TextView)view.findViewById(R.id.tvWeather1)).setText(dateArrayMap.get(keyDate)[0]);
            ((TextView)view.findViewById(R.id.tvWeather2)).setText(dateArrayMap.get(keyDate)[1]);
            weatherViews.addView(view);
        }

    }

    @NonNull
    private ArrayMap<Date, String[]> getDateArrayMap(Weather weather) {
        ArrayMap<Date,String[]> dateHashMap= new ArrayMap<Date, String[]>();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        for (int i=0;i<weather.getTime().getStartValidTime().size();i++){
            String s = weather.getTime().getStartValidTime().get(i);
            try {
                Date date = formatter.parse(s.split("T")[0]);
                if(!dateHashMap.containsKey(date)){
                   String[] value= new String[2];
                    value[0]="T: "+weather.getData().getTemperature().get(i)+" \n"+weather.getData().getWeather().get(i);
                 dateHashMap.put(date,value);
                }else {
                 dateHashMap.get(date)[1]="T: "+weather.getData().getTemperature().get(i)+" \n"+weather.getData().getWeather().get(i);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return dateHashMap;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Location permission has NOT been granted. Requesting permission.");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);


            } else {
                Log.i(TAG, "Requesting permission.");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
            }


            return;
        } else {
            location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            getDataWithLocation();
        }
    }

    private void getDataWithLocation() {
        if (location != null) {
            coordinates = new double[2];
            coordinates[0] = location.getLatitude();
            coordinates[1] = location.getLongitude();
            fetchData();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "got permission.");
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    getDataWithLocation();

                } else {
                    // Handle permission denied
                    coordinates=defaultCoordinates;
                    Toast.makeText(this, "setting your location to 60661", Toast.LENGTH_SHORT).show();
                    fetchData();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Can't connect to Google Play Services!");

    }
}
