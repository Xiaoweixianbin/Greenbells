package dreammerwei.com.greenbellweather;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import dreammerwei.com.greenbellweather.adapter.PredictAdapter;
import dreammerwei.com.greenbellweather.base.BaseActivity;
import dreammerwei.com.greenbellweather.model.HeWeatherDataService30;

/**
 * Created by weixianbin on 16/3/8.
 */
public class PredictActivity extends BaseActivity{
    private static final String HEAD = "weather";
    private RecyclerView recyclerView;
    private HeWeatherDataService30 data;
    private PredictAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predict);
        data= (HeWeatherDataService30) getIntent().getSerializableExtra(HEAD);
        initView();
    }

    private void initView(){
        recyclerView = (RecyclerView) findViewById(R.id.re_predict);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PredictAdapter(data,this);
        recyclerView.setAdapter(adapter);
    }
}
