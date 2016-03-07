package dreammerwei.com.greenbellweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import dreammerwei.com.greenbellweather.base.BaseApplication;

/**
 * Created by weixianbin on 16/3/7.
 */
public class Util {

    public static final String CHANGE_ICONS = "change_icons";//切换图标
    public static final String CLEAR_CACHE = "clear_cache";//清空缓存
    public static final String AUTO_UPDATE = "change_update_time"; //自动更新时长
    public static final String CITY_NAME = "城市";//选择城市
    public static final String HOUR = "小时";//当前小时
    public static final String HOUR_SELECT = "hour_select"; //设置更新频率的联动-需要改进

    public static final String API_TOKEN = "****";//fir.im api_token
    public static final String KEY = "049dc522c8014490b1f3993944c44088";// 和风天气 key

    public static int ONE_HOUR = 3600;



    private SharedPreferences mPreferences;
    private static Util sInstance;


    public static Util getInstance(){
        if (sInstance ==null){
            sInstance = new Util(BaseApplication.appContent);
        }
        return sInstance;
    }

    private Util(Context context){
        mPreferences = context.getSharedPreferences("setting",Context.MODE_PRIVATE);
    }

    public Util putBoolean(String key,boolean value){
        mPreferences.edit().putBoolean(key,value);
        return this;
    }

    public boolean getBoolean(String key,boolean def){
        return mPreferences.getBoolean(key,def);
    }

    public Util putInt(String key, int value){
        mPreferences.edit().putInt(key,value).apply();
        return this;
    }

    public int getInt(String key, int defValue){
        return mPreferences.getInt(key, defValue);
    }

    public Util putString(String key,String value){
        mPreferences.edit().putString(key,value).apply();
        return this;
    }
    public String getString(String key, String value){
        return mPreferences.getString(key, value);
    }




    public static boolean isNetWorkConneted(Context context){
        if (context !=null){
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info != null){
                return info.isAvailable();
            }
        }
        return false;
    }
}
