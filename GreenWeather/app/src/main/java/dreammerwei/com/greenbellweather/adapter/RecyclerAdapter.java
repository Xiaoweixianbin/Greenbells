package dreammerwei.com.greenbellweather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import dreammerwei.com.greenbellweather.MainActivity;
import dreammerwei.com.greenbellweather.R;
import dreammerwei.com.greenbellweather.model.HeWeatherDataService30;

/**
 * Created by weixianbin on 16/2/26.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private HeWeatherDataService30 mData;
    private Context mContext;

    public RecyclerAdapter(HeWeatherDataService30 data, Context context) {
        this.mData = data;
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_home, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv.setText(mData.getNow().getCond().getTxt());
        holder.img.setImageResource(R.mipmap.icon);
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        ImageView img;
        public MyViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.tv);
            img = (ImageView) view.findViewById(R.id.img);
        }
    }

}

