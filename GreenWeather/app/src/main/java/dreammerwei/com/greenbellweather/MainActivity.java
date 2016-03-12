package dreammerwei.com.greenbellweather;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.util.Date;

import dreammerwei.com.greenbellweather.adapter.HeaderViewRecyclerAdapter;
import dreammerwei.com.greenbellweather.adapter.RecyclerAdapter;
import dreammerwei.com.greenbellweather.base.BaseActivity;
import dreammerwei.com.greenbellweather.model.HeWeatherDataService30;
import dreammerwei.com.greenbellweather.model.WeatherBean;
import dreammerwei.com.greenbellweather.util.ACache;
import dreammerwei.com.greenbellweather.util.EndlessRecyclerOnScrollListener;
import dreammerwei.com.greenbellweather.util.RetrofitUtil;
import dreammerwei.com.greenbellweather.util.Util;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final String HEAD = "weather";
    private static final String TAG = MainActivity.class.getSimpleName();
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener =new  MyLocationListener();

    private HeaderViewRecyclerAdapter stringAdapter;
    private GridLayoutManager gridLayoutManager;
    private ProgressBar onLoadMore;



    private TextView tvCity;
    private TextView tvCond;
    private TextView tvUpdateTime;
    private TextView tvTime;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerAdapter mAdapter;
    private Observer<HeWeatherDataService30> observer;
    private boolean isLocaiton = false;
    private long exitTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myListener);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initIcon();
        fetchData();
        if (Util.isNetWorkConneted(this)){
            initLocation();
            if (isLocaiton){
                onRefresh();
            }
        }

    }

    private void initView(){
        tvCity = (TextView) findViewById(R.id.id_city);
        tvCond = (TextView) findViewById(R.id.id_condTxt);
        tvTime = (TextView) findViewById(R.id.id_time);
        tvUpdateTime = (TextView) findViewById(R.id.id_updatetime);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.VISIBLE);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mRefreshLayout.setOnRefreshListener(this);


        mRecyclerView= (RecyclerView) findViewById(R.id.recyclerview);
        gridLayoutManager = new GridLayoutManager(this,2);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return ((stringAdapter.getItemCount() - 1) == position) ? gridLayoutManager.getSpanCount() : 1;
            }
        });

        tvCity.setText(mUtil.getString(Util.CITY_NAME, "南京市"));
        tvTime.setText(Util.getYMD(new Date()));
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
            public void onNext(final HeWeatherDataService30 heWeatherDataService30) {
                mProgressBar.setVisibility(View.INVISIBLE);
                new RefreshHanler().sendEmptyMessage(2);
                mAdapter = new RecyclerAdapter(heWeatherDataService30,MainActivity.this);
                stringAdapter = new HeaderViewRecyclerAdapter(mAdapter);
                createLoadMoreView();
                mRecyclerView.setAdapter(stringAdapter);
                tvCond.setText(heWeatherDataService30.getAqi().getCity().getQlty());
                tvUpdateTime.setText(heWeatherDataService30.getBasic().getUpdate().getLoc());
                mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(gridLayoutManager) {
                    @Override
                    public void onLoadMore(int currentPage) {
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this,PredictActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(HEAD,heWeatherDataService30);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        onLoadMore.setVisibility(View.GONE);
                    }
                });
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




    private void initIcon(){
        mUtil.putInt("未知", R.mipmap.none);
        mUtil.putInt("晴", R.mipmap.sunny);
        mUtil.putInt("阴", R.mipmap.overcast);
        mUtil.putInt("多云", R.mipmap.cloudy);
        mUtil.putInt("少云", R.mipmap.fewcloudy);
        mUtil.putInt("晴间多云", R.mipmap.partlycloudy);
        mUtil.putInt("小雨", R.mipmap.lightrain);
        mUtil.putInt("中雨", R.mipmap.moderaterain);
        mUtil.putInt("大雨", R.mipmap.heavyrain);
        mUtil.putInt("阵雨", R.mipmap.showerrain);
        mUtil.putInt("雷阵雨", R.mipmap.thundershower);
        mUtil.putInt("霾", R.mipmap.haze);
        mUtil.putInt("雾", R.mipmap.foggy);
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

    private void createLoadMoreView() {
        View loadMoreView = LayoutInflater
                .from(MainActivity.this)
                .inflate(R.layout.view_load_more, mRecyclerView, false);
        onLoadMore = (ProgressBar) loadMoreView.findViewById(R.id.load_progress_bar);
        stringAdapter.addFooterView(loadMoreView);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if ((System.currentTimeMillis() - exitTime) > 2000){
            Toast.makeText(this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        }else {
            finish();
        }
    }

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
