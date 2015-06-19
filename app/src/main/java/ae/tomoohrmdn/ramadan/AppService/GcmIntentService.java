package ae.tomoohrmdn.ramadan.AppService;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import ae.tomoohrmdn.ramadan.utils.AppAction;
import ae.tomoohrmdn.ramadan.utils.GCM;

public class GcmIntentService extends IntentService {

    private String TAG="tg";


    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GCM.OnReciveMessageFromService(this, intent);

        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }
}