package dreammerwei.com.greenbellweather.util;

import dreammerwei.com.greenbellweather.model.WeatherBean;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by weixianbin on 16/3/6.
 */
public interface Api {
    String HOST = "https://api.heweather.com/x3/";
    @GET("weather")
    Observable<WeatherBean> mWeather(@Query("city") String city, @Query("key") String key);
}
