
package dreammerwei.com.greenbellweather.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeatherBean implements Serializable {
    @SerializedName("HeWeather data service 3.0") @Expose
    private List<HeWeatherDataService30> heWeatherDataService30s
            = new ArrayList<>();

    public List<HeWeatherDataService30> getHeWeatherDataService30s() {
        return heWeatherDataService30s;
    }

    public void setHeWeatherDataService30s(List<HeWeatherDataService30> heWeatherDataService30s) {
        this.heWeatherDataService30s = heWeatherDataService30s;
    }
}
