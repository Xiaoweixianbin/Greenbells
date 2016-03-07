package dreammerwei.com.greenbellweather.base;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import dreammerwei.com.greenbellweather.util.RetrofitUtil;

/**
 * Created by weixianbin on 16/3/6.
 */
public class BaseApplication extends Application {
    public static String cacheDir = "";
    public static Context appContent = null;
    @Override
    public void onCreate() {
        super.onCreate();
        appContent = getApplicationContext();
        //初始化retrofit
        RetrofitUtil.init(getApplicationContext());

        if (getApplicationContext().getExternalCacheDir() !=null && existSDCard()){
            cacheDir=getApplicationContext().getExternalCacheDir().toString();
        }else {
            cacheDir= getApplicationContext().getCacheDir().toString();
        }

    }



    private boolean existSDCard(){
        if (android.os.Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            return true;
        }else {
            return false;
        }
    }
}
