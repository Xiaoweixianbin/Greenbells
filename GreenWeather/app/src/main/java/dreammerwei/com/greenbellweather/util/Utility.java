package dreammerwei.com.greenbellweather.util;

import android.text.TextUtils;

import dreammerwei.com.greenbellweather.bean.City;
import dreammerwei.com.greenbellweather.bean.County;
import dreammerwei.com.greenbellweather.bean.Province;
import dreammerwei.com.greenbellweather.db.GreenWeatherDB;

/**
 * Created by Administrator on 2015/7/27.
 */
public class Utility {

    public synchronized static boolean handleProvincesResponse(GreenWeatherDB greenWeatherDB, String response){
        if (!TextUtils.isEmpty(response)){
            String[] allProvinces = response.split(",");
            if(allProvinces != null && allProvinces.length >0){
                for (String p:allProvinces){
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceName(array[1]);
                    province.setProvinceCode(array[0]);
                    greenWeatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    public static boolean handleCitiesResponse(GreenWeatherDB greenWeatherDB, String response, int provinceId){
        if (!TextUtils.isEmpty(response)){
            String[] allCities = response.split(",");
            if (allCities != null && allCities.length>0){
                for(String c : allCities){
                    String[] array = c.split("\\|");
                    City city = new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);

                    greenWeatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;

    }

    public static boolean handleCountiesResponse(GreenWeatherDB greenWeatherDB, String response, int cityId){
        if (!TextUtils.isEmpty(response)){
            String[] allCounties = response.split(",");
            if (allCounties!= null && allCounties.length>0){
                for(String c : allCounties) {
                    String[] array = c.split("\\|");
                    County county = new County();
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    county.setCityId(cityId);
                    greenWeatherDB.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }
}
