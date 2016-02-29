package dreammerwei.com.greenbellweather;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dreammerwei.com.greenbellweather.adapter.RecyclerAdapter;
import dreammerwei.com.greenbellweather.bean.Basic;
import dreammerwei.com.greenbellweather.bean.Bean;
import dreammerwei.com.greenbellweather.bean.OtherDetail;


public class MainActivity extends ActionBarActivity {
    private RecyclerView recyclerView;

    private RecyclerAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();


    private String txt;
    private String provider;
    private TextView position;
    private TextView condTxt;
    private TextView update;
    private static final int SHOW_LOCATION = 0;
    private static final int UPDATE_UI =1;
    private static final int UPDATE_TIME =2 ;
    private LocationManager locationManager;
    private String url = "http://api.map.baidu.com/geocoder/v2/?ak=qkytwEqBiWD2M72nm1TzywEX&callback=renderReverse&location=32.116283,118.924583&output=json&pois=1";
    String url1 = "http://api.map.baidu.com/geocoder/v2/?ak=qkytwEqBiWD2M72nm1TzywEX&location=";
    String url2 = "&output=json&pois=1";
    String latitude = "32.116283";
    String longitude = "118.924583";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getData();
        getPosition();

        /*swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.id_recyclerview);
        recyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        adapter = new RecyclerAdapter(datas,MainActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));*/
        final Request request = new Request.Builder()
                .url("https://api.heweather.com/x3/weather?cityid=CN101190101&key=049dc522c8014490b1f3993944c44088")
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String test = response.body().string();

                try {
                    JSONObject jsonObject = new JSONObject(test);
                    JSONArray jsonArray = jsonObject.getJSONArray("HeWeather data service 3.0");
                    JSONObject object = jsonArray.getJSONObject(0);
                    Bean bean = gson.fromJson(object.toString(), new TypeToken<Bean>() {
                    }.getType());
                    Log.e("+++++++", bean.getNow().getTmp());
                    Message message = new Message();
                    message.what = UPDATE_UI;
                    message.obj = bean.getNow().getCond().getTxt();
                    txt = bean.getNow().getCond().getTxt();
                    handler.sendMessage(message);
/*
                    Message time = new Message();
                    message.what = UPDATE_TIME;
                    message.obj = bean.getBasic().getUpdate().getLoc();
                    handler.sendMessage(time);*/

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }


    private void getData(){
        position = (TextView) findViewById(R.id.id_position);
        condTxt = (TextView) findViewById(R.id.id_condTxt);
        update = (TextView) findViewById(R.id.id_update);
    }

    /**
     * 通过定位获得城市,并且更新UI
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void getPosition() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        List<String> providerList = locationManager.getProviders(true);
        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            Toast.makeText(this, "No location provider to use", Toast.LENGTH_SHORT).show();
            return;
        }
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            showLocation(location);
        }
        locationManager.requestLocationUpdates(provider, 5000, 1, locationListener);
    }

    private void showLocation(Location location) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                {
                    OkHttpClient mOkHttpClient = new OkHttpClient();
                    final Request request = new Request.Builder()
                            .url(url1 + latitude + "," + longitude + url2)
                            .build();
                    Call call = mOkHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Request request, IOException e) {

                        }

                        @Override
                        public void onResponse(Response response) throws IOException {
                            String jieguo = response.body().string();
                            try {
                                JSONObject jsonObject = new JSONObject(jieguo);
                                JSONObject sub1 = jsonObject.getJSONObject("result");
                                JSONObject sub2 = sub1.getJSONObject("addressComponent");
                                String city = sub2.getString("city");
                                Message message = new Message();
                                message.what = SHOW_LOCATION;
                                message.obj = city;
                                handler.sendMessage(message);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
            }
        }).start();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SHOW_LOCATION:
                    String currentPosition = (String) msg.obj;
                    position.setText(currentPosition);
                    break;
                case UPDATE_UI:
                    String txt = (String) msg.obj;
                    condTxt.setText(txt);
                    break;
                case UPDATE_TIME:
                    String time = (String) msg.obj;
                    update.setText(time);
                    break;
                default:
                    break;
            }
        }
    };

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
}
