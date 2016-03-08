package dreammerwei.com.greenbellweather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import dreammerwei.com.greenbellweather.R;
import dreammerwei.com.greenbellweather.model.HeWeatherDataService30;
import dreammerwei.com.greenbellweather.util.Util;

/**
 * Created by weixianbin on 16/2/26.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private HeWeatherDataService30 mData;
    private Context mContext;
    private Util mUtil;

    public RecyclerAdapter(HeWeatherDataService30 data, Context context) {
        this.mData = data;
        this.mContext = context;
        mUtil = Util.getInstance();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_info, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        switch (position) {
            case 0:
                holder.tv.setText(mData.getNow().getCond().getTxt());
                Glide.with(mContext)
                        .load(mUtil.getInt(mData.getNow().getCond().getTxt(), R.mipmap.none))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.img);
                break;
            case 1:
                holder.tv.setText("当前温度");
                holder.img.setVisibility(View.GONE);
                holder.info.setVisibility(View.VISIBLE);
                holder.info.setText(mData.getNow().getTmp() + "℃");
                break;
            case 2:
                holder.tv.setText("当前湿度");
                holder.img.setVisibility(View.GONE);
                holder.info.setVisibility(View.VISIBLE);
                holder.info.setText(mData.getNow().getHum() + "%");
                break;
            case 3:
                holder.tv.setText("日出/日落");
                holder.img.setVisibility(View.GONE);
                holder.info.setVisibility(View.VISIBLE);
                holder.info.setText(mData.getDailyForecast().get(0).getAstro().getSr() + "/"
                        + mData.getDailyForecast().get(0).getAstro().getSs());
                break;
            case 4:
                holder.tv.setText("最高温/最低温");
                holder.img.setVisibility(View.GONE);
                holder.info.setVisibility(View.VISIBLE);
                holder.info.setText(mData.getDailyForecast().get(0).getTmp().getMax() + "/"
                        + mData.getDailyForecast().get(0).getTmp().getMin());
                break;
            case 5:
                holder.tv.setText("风速");
                holder.img.setVisibility(View.GONE);
                holder.info.setVisibility(View.VISIBLE);
                holder.info.setText(mData.getDailyForecast().get(0).getWind().getSpd() + "km/h");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        ImageView img;
        TextView info;

        public MyViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.tv);
            img = (ImageView) view.findViewById(R.id.img);
            info = (TextView) view.findViewById(R.id.info);
        }
    }

}

