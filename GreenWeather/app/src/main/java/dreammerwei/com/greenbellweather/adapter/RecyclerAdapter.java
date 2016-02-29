package dreammerwei.com.greenbellweather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.List;

import dreammerwei.com.greenbellweather.MainActivity;
import dreammerwei.com.greenbellweather.R;

/**
 * Created by weixianbin on 16/2/26.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private List<String> mData;
    private Context mContext;

    public RecyclerAdapter(List<String> data,Context context){
        mData = data;
        mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_home,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv.setText(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
         TextView tv;
         public  MyViewHolder (View view){
             super(view);
             tv = (TextView) view.findViewById(R.id.id_num);
         }
     }

}
