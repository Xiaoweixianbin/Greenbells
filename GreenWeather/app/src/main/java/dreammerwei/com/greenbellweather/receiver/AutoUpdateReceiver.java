package dreammerwei.com.greenbellweather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import dreammerwei.com.greenbellweather.service.AutoUpdateService;

/**
 * Created by Administrator on 2015/7/29.
 */
public class AutoUpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        Intent i = new Intent(context, AutoUpdateService.class);
        context.startService(i);
    }
}
