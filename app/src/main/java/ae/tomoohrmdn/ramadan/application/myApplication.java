package ae.tomoohrmdn.ramadan.application;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.crashlytics.android.Crashlytics;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import ae.tomoohrmdn.ramadan.db.Dao.AppSettingDao;
import ae.tomoohrmdn.ramadan.db.Entity.AppSetting;
import io.fabric.sdk.android.Fabric;

/**
 * Created by tareq on 06/11/2015.
 */
public class myApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        initializeDB();
        InitImageLoader();
    }


    protected void initializeDB() {
        Configuration.Builder configurationBuilder = new Configuration.Builder(this);
        //configurationBuilder.addModelClasses(Test.class);

        ActiveAndroid.initialize(configurationBuilder.create());


        //Setup setting first time
        AppSettingDao settingNotification=new AppSettingDao();
        AppSetting appSetting=settingNotification.getAppSetting();
        if(appSetting==null){
            AppSetting setting=new AppSetting();
            setting.Notification=true;
            setting.save();
        }



        forceLanguage();
    }

    public void forceLanguage(){
        Resources res = getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale("ar");
        res.updateConfiguration(conf, dm);
    }

    private void InitImageLoader(){
        //SAMPLE using [PICASSO](https://github.com/square/picasso)
        //initialize and create the image loader logic
        DrawerImageLoader.init(new DrawerImageLoader.IDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Picasso.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Picasso.with(imageView.getContext()).cancelRequest(imageView);
            }

            @Override
            public Drawable placeholder(Context ctx) {
                return null;
            }
        });
    }


}
