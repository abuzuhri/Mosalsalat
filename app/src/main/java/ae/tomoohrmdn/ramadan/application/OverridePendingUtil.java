package ae.tomoohrmdn.ramadan.application;

import android.app.Activity;

import ae.tomoohrmdn.ramadan.R;

/**
 * Created by tareq on 06/11/2015.
 */
public class OverridePendingUtil {
    public static void in(final Activity activity) {
        new Object() {
            public void overridePendingTransition(Activity c) {
                c.overridePendingTransition(R.anim.fb_slide_in_from_right, R.anim.fb_forward);
            }
        }.overridePendingTransition(activity);
    }

    public static void out(final Activity activity) {
        new Object() {
            public void overridePendingTransition(Activity c) {
                c.overridePendingTransition(R.anim.fb_back, R.anim.fb_slide_out_from_right);
            }
        }.overridePendingTransition(activity);
    }
}