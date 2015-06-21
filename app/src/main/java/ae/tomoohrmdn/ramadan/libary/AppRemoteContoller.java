package ae.tomoohrmdn.ramadan.libary;

import android.app.Activity;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Created by tareq on 06/08/2015.
 */
public class AppRemoteContoller {
    static Context _context;

    private static String ServerUrl="http://mobiledow.com/";
    public static String ServerPagePost=ServerUrl+"Mobile/ajax/UserInterface.php";


    public  static  void  initialize(Context context){
        _context=context;
        run();
    }

    static void run()  {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final OkHttpClient client = new OkHttpClient();

        FormEncodingBuilder formParms=new FormEncodingBuilder();
        AppInfo appInfo=new AppInfo(_context);
        Hashtable table=appInfo.getPostParm();

        Enumeration e = table.keys();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            System.out.println(key + " : " + table.get(key));
            formParms.add(key, table.get(key).toString());
            Log.i("tg",key+"==>"+table.get(key).toString());
        }

        RequestBody formBody = formParms.build();

        Request request = new Request.Builder().url(ServerPagePost).post(formBody).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                System.out.println(response.body().string());
                Log.i("tg","Appp ==>"+response.body().string());
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }


    public static void checker(Activity activity){

    }




}