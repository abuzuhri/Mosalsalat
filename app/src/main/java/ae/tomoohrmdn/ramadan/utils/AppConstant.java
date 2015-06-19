package ae.tomoohrmdn.ramadan.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by tareq on 06/11/2015.
 */
public class AppConstant {


    private static final String ServerUrl="http://pass2you.com/";
    public static final String ServerUrlNewFeed=ServerUrl+"OtherApi/RamadanApps/Index";//"test/shopoffer/file.aspx";
    public static final String SERVER_URL_REGISTER_GCM_USER = ServerUrl+"OtherApi/RamadanApps/RegisterGoogleCloudeUser";
    public enum AppDrawer {
        Home(10),
        Series(20),
        Program(30),
        TopShows(40),
        Favorites(50),
        NewEpisode(60),
        Settings(1000);
        public int id;

        private AppDrawer(int id) {
            this.id = id;
        }
    }


    public  static class SharedPreferenceNames{
        public static String SocialUser="SocialUser";
    }


    public static class DateConvertion{

        public static Date getCurrentDate() {
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date currenTimeZone = new Date((long) 1379487711 * 1000);
            return currenTimeZone;
        }

    }
}
