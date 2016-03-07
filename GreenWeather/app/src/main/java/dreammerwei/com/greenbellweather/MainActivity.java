package dreammerwei.com.greenbellweather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.nfc.Tag;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;

import dreammerwei.com.greenbellweather.adapter.RecyclerAdapter;
import dreammerwei.com.greenbellweather.model.HeWeatherDataService30;
import dreammerwei.com.greenbellweather.model.WeatherBean;
import dreammerwei.com.greenbellweather.util.ACache;
import dreammerwei.com.greenbellweather.util.GetData;
import dreammerwei.com.greenbellweather.util.RetrofitUtil;
import dreammerwei.com.greenbellweather.util.Util;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observers.Observers;
import rx.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener =new  MyLocationListener();
    private Util mUtil = Util.getInstance();
    private ACache aCache = ACache.get(MainActivity.this);

    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerAdapter mAdapter;
    private Observer<HeWeatherDataService30> observer;
    private boolean isLocaiton = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myListener);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        fetchData();
        if (Util.isNetWorkConneted(this)){
            initLocation();
            if (isLocaiton){
                onRefresh();
            }
        }

    }

    private void initView(){
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.VISIBLE);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mRefreshLayout.setOnRefreshListener(this);

        mRecyclerView= (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }

    private void fetchData(){
        observer = new Observer<HeWeatherDataService30>() {
            @Override
            public void onCompleted() {
                new RefreshHanler().sendEmptyMessage(2);
            }

            @Override
            public void onError(Throwable e) {
                RetrofitUtil.disposeFailure(e,MainActivity.this);
            }

            @Override
            public void onNext(HeWeatherDataService30 heWeatherDataService30) {
                mProgressBar.setVisibility(View.INVISIBLE);
                new RefreshHanler().sendEmptyMessage(2);
                mAdapter = new RecyclerAdapter(heWeatherDataService30,MainActivity.this);
                mRecyclerView.setAdapter(mAdapter);

            }
        };
        fetchDataByCache(observer);
    }



    private void fetchDataByCache(Observer<HeWeatherDataService30> observer){
        HeWeatherDataService30 weather = null;
        try{
            weather = (HeWeatherDataService30) aCache.getAsObject("weatherData");
        } catch (Exception e){
            Log.e(TAG,e.toString());
        }

        if (weather != null){
            Observable.just(weather).distinct().subscribe(observer);
        }else {
            fetchDataByNetWork(observer);
        }
    }

    private void fetchDataByNetWork(Observer<HeWeatherDataService30> observer){
        String cityname = mUtil.getString(Util.CITY_NAME,"南京");
        if (cityname !=null){
            cityname = cityname.replace("市", "")
                    .replace("省", "")
                    .replace("自治区", "")
                    .replace("特别行政区", "")
                    .replace("地区", "")
                    .replace("盟", "");
        }
        RetrofitUtil.getApiService(this)
                .mWeather(cityname,Util.KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Func1<WeatherBean, Boolean>() {
                    @Override
                    public Boolean call(WeatherBean weatherBean) {
                        return weatherBean.getHeWeatherDataService30s().get(0).getStatus().equals("ok");
                    }
                })
                .map(new Func1<WeatherBean, HeWeatherDataService30>() {
                    @Override
                    public HeWeatherDataService30 call(WeatherBean weatherBean) {
                        return weatherBean.getHeWeatherDataService30s().get(0);
                    }
                })
                .doOnNext(new Action1<HeWeatherDataService30>() {
                    @Override
                    public void call(HeWeatherDataService30 heWeatherDataService30) {

                        aCache.put("weatherData", heWeatherDataService30,
                                (mUtil.getInt(Util.AUTO_UPDATE, 0) + 1) * Util.ONE_HOUR);
                    }
                })
                .subscribe(observer);
    }









    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        //定位模式高精度
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        int span = 100000;
        option.setScanSpan(span);
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    @Override
    public void onRefresh() {
        fetchDataByNetWork(observer);
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            if (location!=null){
                mUtil.putString(Util.CITY_NAME,location.getCity());
            }else {
                Log.i("BaiduLocationApiDem", "error");
            }


        }
    }


    @SuppressLint("HandlerLeak")
    class RefreshHanler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    mRefreshLayout.setRefreshing(true);
                    break;
                case 2:
                    if (mRefreshLayout.isRefreshing()){
                        mRefreshLayout.setRefreshing(false);
                        if (Util.isNetWorkConneted(MainActivity.this)){
                            Toast.makeText(MainActivity.this,"加载完毕",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(MainActivity.this,"网络有问题",Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
            }
        }
    }
}
