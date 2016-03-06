package dreammerwei.com.greenbellweather.base;

import android.app.Application;
import android.content.Context;

/**
 * Created by weixianbin on 16/3/6.
 */
public class BaseApplication extends Application {
    public static Context appContent = null;
    @Override
    public void onCreate() {
        super.onCreate();
        appContent = getApplicationContext();
        //初始化retrofit

    }
}
