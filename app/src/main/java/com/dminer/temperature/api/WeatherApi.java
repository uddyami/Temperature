package com.dminer.temperature.api;

import com.dminer.temperature.model.Weather;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
/**
 * Created by uddyamiagrawal on 3/30/16.
 */
public interface WeatherApi {
    //weather.gov
    //http://forecast.weather.gov/MapClick.php?lat=41.885575&lon=-87.644408&FcstType=json
    @GET("/MapClick.php")
    Observable<Weather> weather(@Query("lat")double lat, @Query("lon") double lon, @Query("FcstType") String type);
}
