package dreammerwei.com.greenbellweather.util;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by weixianbin on 16/3/6.
 */
public class RetrofitUtil {
    private static Api api = null;
    private static Retrofit retrofit = null;
    private static OkHttpClient okHttpClient = null;
    private static Context context;
    public static void init(final Context context) {
        Gson gson = new GsonBuilder().create();
        retrofit = new Retrofit.Builder().baseUrl(Api.HOST)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .callbackExecutor(Executors.newCachedThreadPool())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        api = retrofit.create(Api.class);
    }

    public static Api getApiService(Context context){
        if (api != null){
            return api;
        }
        init(context);
        return getApiService(context);
    }

    public static Retrofit getRetrofit(Context context){
        if (retrofit != null){
            return  retrofit;
        }
        init(context);
        return getRetrofit(context);
    }
    public static OkHttpClient getOkHtoopClient(Context context){
        if (okHttpClient != null){
            return okHttpClient;
        }
        init(context);
        return getOkHtoopClient(context);
    }
    public static void disposeFailure(Throwable e, Context context){
        if (e.toString().contains("GaiException") || e.toString().contains("SocketTimeoutException")||e.toString().contains("UnknownHostException")){
            Toast.makeText(context,"网络连接出错", Toast.LENGTH_LONG).show();
        }
    }
}
