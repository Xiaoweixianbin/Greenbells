package dreammerwei.com.greenbellweather.util;

/**
 * Created by Administrator on 2015/7/26.
 */
public interface HttpCallbackListener {
    void onFinish(String response);

    void onError(Exception e);
}
