package dreammerwei.com.greenbellweather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import dreammerwei.com.greenbellweather.R;
import dreammerwei.com.greenbellweather.model.HeWeatherDataService30;
import dreammerwei.com.greenbellweather.util.Util;

/**
 * Created by weixianbin on 16/3/8.
 */
public class PredictAdapter extends RecyclerView.Adapter<PredictAdapter.ForecastViewHolder> {
    private static final String TAG = PredictAdapter.class.getSimpleName();
    private HeWeatherDataService30 mWeatherData;
    private Context mContext;
    private Util mUtil;

    public PredictAdapter(HeWeatherDataService30 data, Context context) {
        this.mWeatherData = data;
        this.mContext = context;
        mUtil = Util.getInstance();
    }

    @Override
    public ForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ForecastViewHolder holder = new ForecastViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_forecast, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ForecastViewHolder holder, int position) {
        //今日 明日
        holder.forecastDate[0].setText("今日");
        holder.forecastDate[1].setText("明日");
        for (int i = 0; i < mWeatherData.getDailyForecast().size(); i++) {
            if (i > 1) {
                try {
                    holder.forecastDate[i].setText(
                            dayForWeek(mWeatherData.getDailyForecast().get(i).getDate()));
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }

            Glide.with(mContext)
                    .load(mUtil.getInt(mWeatherData.getDailyForecast().get(i).getCond().getTxtD(), R.mipmap.none))
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.forecastIcon[i]);

            holder.forecastTemp[i].setText(
                    mWeatherData.getDailyForecast().get(i).getTmp().getMin() + "° " +
                            mWeatherData.getDailyForecast().get(i).getTmp().getMax() + "°");
            holder.forecastTxt[i].setText(
                    mWeatherData.getDailyForecast().get(i).getCond().getTxtD() + "。 最高" +
                            mWeatherData.getDailyForecast().get(i).getTmp().getMax() + "℃。 " +
                            mWeatherData.getDailyForecast().get(i).getWind().getSc() + " " +
                            mWeatherData.getDailyForecast().get(i).getWind().getDir() + " " +
                            mWeatherData.getDailyForecast().get(i).getWind().getSpd() + " km/h。 " +
                            "降水几率 " +
                            "" + mWeatherData.getDailyForecast().get(i).getPop() + "%。");
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    /**
     * 判断当前日期是星期几
     *
     * @param pTime 修要判断的时间
     * @return dayForWeek 判断结果
     * @Exception 发生异常
     */
    public static String dayForWeek(String pTime) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(format.parse(pTime));
        int dayForWeek = 0;
        String week = "";
        dayForWeek = c.get(Calendar.DAY_OF_WEEK);
        switch (dayForWeek) {
            case 1:
                week = "星期日";
                break;
            case 2:
                week = "星期一";
                break;
            case 3:
                week = "星期二";
                break;
            case 4:
                week = "星期三";
                break;
            case 5:
                week = "星期四";
                break;
            case 6:
                week = "星期五";
                break;
            case 7:
                week = "星期六";
                break;
        }
        return week;
    }

    class ForecastViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout forecastLinear;
        private TextView[] forecastDate = new TextView[mWeatherData.getDailyForecast().size()];
        private TextView[] forecastTemp = new TextView[mWeatherData.getDailyForecast().size()];
        private TextView[] forecastTxt = new TextView[mWeatherData.getDailyForecast().size()];
        private ImageView[] forecastIcon = new ImageView[mWeatherData.getDailyForecast().size()];


        public ForecastViewHolder(View itemView) {
            super(itemView);
            forecastLinear = (LinearLayout) itemView.findViewById(R.id.forecast_linear);
            for (int i = 0; i < mWeatherData.getDailyForecast().size(); i++) {
                View view = View.inflate(mContext, R.layout.item_forecast_line, null);
                forecastDate[i] = (TextView) view.findViewById(R.id.forecast_date);
                forecastTemp[i] = (TextView) view.findViewById(R.id.forecast_temp);
                forecastTxt[i] = (TextView) view.findViewById(R.id.forecast_txt);
                forecastIcon[i] = (ImageView) view.findViewById(R.id.forecast_icon);
                forecastLinear.addView(view);
            }
        }

    }
}
