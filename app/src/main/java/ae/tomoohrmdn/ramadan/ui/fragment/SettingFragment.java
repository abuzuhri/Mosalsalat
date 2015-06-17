package ae.tomoohrmdn.ramadan.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.SwitchPreference;

import java.util.Set;
import com.github.machinarius.preferencefragment.PreferenceFragment;

import ae.tomoohrmdn.ramadan.R;
import ae.tomoohrmdn.ramadan.db.Dao.AppSettingDao;
import ae.tomoohrmdn.ramadan.db.Entity.AppSetting;
import ae.tomoohrmdn.ramadan.ui.activity.SplashActivity;
import ae.tomoohrmdn.ramadan.utils.AppAction;
import ae.tomoohrmdn.ramadan.utils.AppLog;
import ae.tomoohrmdn.ramadan.utils.DialogUtils;
import ae.tomoohrmdn.ramadan.utils.Login.FacebookLogin;
import ae.tomoohrmdn.ramadan.utils.Login.GooglPlusLogin;

/**
 * Created by tareq on 06/11/2015.
 */
public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {


    private SwitchPreference notification;
    AppSettingDao settingNotification=new AppSettingDao();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);

        Set<String> preferenceNames = getPreferenceManager().getSharedPreferences().getAll().keySet();

        findPreference("logout").setOnPreferenceClickListener(this);
        findPreference("rateapp").setOnPreferenceClickListener(this);

        AppSetting appSetting=settingNotification.getAppSetting();

        AppLog.i("appSetting.Notification ==>" + appSetting.Notification);

        notification=(SwitchPreference)findPreference("notification");
        notification.setOnPreferenceClickListener(this);
        notification.setChecked(appSetting.Notification);


        for (String prefName : preferenceNames) {
            AppLog.i("prefName"+prefName);
            Preference preference = findPreference(prefName);
            if(preference!=null)
                preference.setOnPreferenceClickListener(this);
        }

    }


    private  void LogOut(){

        DialogUtils.OkDialog(getActivity(), getString(R.string.user_logout_clear_data), getString(R.string.user_logout_clear_data_summary), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {


                FacebookLogin fbNetwork = new FacebookLogin(getActivity());
                fbNetwork.Logout();

                GooglPlusLogin gpNetwork = new GooglPlusLogin(getActivity());
                gpNetwork.Logout();

                AppAction.OpenActivity(getActivity(), SplashActivity.class);
                getActivity().finish();


            }
        });
    }


    private  void  RateApp(){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("market://details?id=" + getActivity().getPackageName()));
        startActivity(i);
    }

    private void NotificationApp(){
        AppLog.i("NotificationApp.isChecked() => " + notification.isChecked());
        settingNotification.setNotification(notification.isChecked());

    }

    @Override
    public boolean onPreferenceClick(Preference preference) {

        String key = preference.getKey();
        //Toast.makeText(getActivity(),"sss",Toast.LENGTH_LONG).show();

        if (key.equals("logout")) {
            LogOut();
        } else if (key.equals("rateapp")) {
            RateApp();
        } else if (key.equals("notification")) {
            NotificationApp();
            return true;
        }
        return true;
    }


}
