package dreammerwei.com.greenbellweather.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import dreammerwei.com.greenbellweather.util.ACache;
import dreammerwei.com.greenbellweather.util.Util;

/**
 * Created by weixianbin on 16/3/8.
 */
public class BaseActivity extends AppCompatActivity {
    public ACache aCache;
    public Util mUtil= null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aCache = ACache.get(BaseActivity.this);
        mUtil = Util.getInstance();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }
}
