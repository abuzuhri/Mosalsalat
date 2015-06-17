package ae.tomoohrmdn.ramadan.utils;

import android.util.Log;

/**
 * Created by tareq on 06/11/2015.
 */
public class AppLog {

    public  static final  String  TAG="MyApp";
    public static void i(String msg){
        Log.i(TAG, msg);
        //Crashlytics.log(msg);
    }

}
