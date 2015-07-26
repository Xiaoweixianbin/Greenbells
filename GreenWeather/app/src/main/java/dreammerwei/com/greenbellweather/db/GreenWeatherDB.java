package dreammerwei.com.greenbellweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import dreammerwei.com.greenbellweather.bean.City;
import dreammerwei.com.greenbellweather.bean.Country;
import dreammerwei.com.greenbellweather.bean.Province;

/**
 * Created by Administrator on 2015/7/26.
 */
public class GreenWeatherDB {
    /*
    * 数据库名
    * */
    public static final String DB_NAME = "Cool_weather";
    /*
    * 数据库版本
    * */
    public static final int VERSION = 1;
    private static GreenWeatherDB greenWeatherDB;
    private SQLiteDatabase db;

    /*
    * 将构造方法私有化
    * */
    private GreenWeatherDB(Context context) {
        GreenWeatherOpenHelper dbHelper = new GreenWeatherOpenHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    /*
    * 获取GreenWeatherDB的实例
    * */
    public synchronized static GreenWeatherDB getInstance(Context context) {
        if (greenWeatherDB == null) {
            greenWeatherDB = new GreenWeatherDB(context);
        }
        return greenWeatherDB;
    }

    /*
    *  将Province实例存储到数据库
    *  */
    public void saveProvince(Province province) {
        if (province != null) {
            ContentValues values = new ContentValues();
            values.put("province_name", province.getProvinceName());
            values.put("province_code", province.getProvinceCode());
            db.insert("Province", null, values);
        }
    }

    /*
    * 从数据库中读取全国的所有的省份信息
    * */
    public List<Province> loadProvinces() {
        List<Province> list = new ArrayList<Province>();
        Cursor cursor = db.query("Province", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                list.add(province);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }


    /*
   *  将City实例存储到数据库
   *  */
    public void saveCity(City city) {
        if (city != null) {
            ContentValues values = new ContentValues();
            values.put("city_name", city.getCityName());
            values.put("city_code", city.getCityCode());
            values.put("province_id", city.getProvinceId());
            db.insert("City", null, values);
        }
    }

    /*
    * 从数据库中读取全国的所有的省份信息
    * */
    public List<City> loadCities(int provinceId) {
        List<City> list = new ArrayList<City>();
        Cursor cursor = db.query("City", null, "province_id = ?", new String[]{String.valueOf(provinceId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceId(provinceId);
                list.add(city);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }


    /*
   *  将Country实例存储到数据库
   *  */
    public void saveCountry(Country country) {
        if (country != null) {
            ContentValues values = new ContentValues();
            values.put("country_name", country.getConuntryName());
            values.put("country_code", country.getConuntryCode());
            values.put("city_id", country.getCityId());
            db.insert("Country", null, values);
        }
    }

    /*
    * 从数据库中读取全国的所有的省份信息
    * */
    public List<Country> loadCountries(int cityId) {
        List<Country> list = new ArrayList<Country>();
        Cursor cursor = db.query("Country", null, "city_id = ?", new String[]{String.valueOf(cityId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Country country = new Country();
                country.setId(cursor.getInt(cursor.getColumnIndex("id")));
                country.setConuntryName(cursor.getString(cursor.getColumnIndex("country_name")));
                country.setConuntryCode(cursor.getString(cursor.getColumnIndex("country_code")));
                country.setCityId(cityId);
                list.add(country);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }
}
