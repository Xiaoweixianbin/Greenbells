package dreammerwei.com.greenbellweather.util;

import dreammerwei.com.greenbellweather.model.WeatherBean;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by weixianbin on 16/3/5.
 */
public interface GetData {
    @GET("weather?cityid=CN101190101&key=049dc522c8014490b1f3993944c44088")
    Call<WeatherBean> getData();
}
