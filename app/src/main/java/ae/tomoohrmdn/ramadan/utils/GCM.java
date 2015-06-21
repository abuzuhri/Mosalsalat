package ae.tomoohrmdn.ramadan.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Patterns;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import ae.tomoohrmdn.ramadan.R;
import ae.tomoohrmdn.ramadan.application.myApplication;
import ae.tomoohrmdn.ramadan.libary.AppRemoteContoller;

/**
 * Created by tareq on 06/18/2015.
 */
public class GCM {

    private static final String EXTRA_MESSAGE = "message";
    private static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";

    public static final int NOTIFICATION_ID = 1;

    private Activity activity;
    private GoogleCloudMessaging gcm;
    private  String regid;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private  String userName;

    public  GCM(Activity activity,String userName){
        this.activity=activity;
        this.userName=userName;
    }
    public void  connect(){

        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(activity);
            regid = getRegistrationId();

            if (regid.isEmpty()) {
                registerInBackground();
            }
            else
            {
                sendRegistrationIdToBackendInBackground();
            }
        }
    }

    private void sendRegistrationIdToBackendInBackground(){
        AsyncTask asyncTask=   new AsyncTask(){
            @Override
            protected Object doInBackground(Object[] params) {
                // You should send the registration ID to your server over HTTP,
                // so it can use GCM/HTTP or CCS to send messages to your app.
                // The request to your server should be authenticated if your app
                // is using accounts.

                sendRegistrationIdToBackend(regid);
                AppLog.i("AppRemoteContoller.initialize(activity);");

                AppRemoteContoller.initialize(activity);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
            }
        };

        asyncTask.execute();
    }

    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
     * or CCS to send messages to your app. Not needed for this demo since the
     * device sends upstream messages to a server that echoes back the message
     * using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend(String registrationId) {
        // Your implementation here.
        String serverUrl = AppConstant. SERVER_URL_REGISTER_GCM_USER;
        Map<String, String> params = new HashMap<String, String>();
        params.put("RegId", registrationId);
        params.put("Name", userName);
        params.put("Email", getEmail());
        params.put("MacAddress", getMacAddress());
        params.put("AppVersion", AppAction.getAppVersion(activity)+"");
        params.put("PackageName", activity.getPackageName());
        String postResponse= post(serverUrl, params);


    }

    private String  getMacAddress(){
        try {
            WifiManager wm = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
            return wm.getConnectionInfo().getMacAddress();
        }catch (Exception ex){
            ex.printStackTrace();
            return "";
        }
    }

    private void registerInBackground() {
        AsyncTask asyncTask=   new AsyncTask(){
            @Override
            protected Object doInBackground(Object[] params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(activity);
                    }
                    String SENDER_ID = activity.getString(R.string.gcm_project_number);
                    String regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // For this demo: we don't need to send it because the device
                    // will send upstream messages to a server that echo back the
                    // message using the 'from' address in the message.

                    // Persist the registration ID - no need to register again.

                    storeRegistrationId(activity, regid);

                    // You should send the registration ID to your server over HTTP,
                    // so it can use GCM/HTTP or CCS to send messages to your app.
                    // The request to your server should be authenticated if your app
                    // is using accounts.

                    sendRegistrationIdToBackend(regid);


                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(Object o) {
            }
        };

        asyncTask.execute();


    }
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, activity, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                AppLog.i("This device is not supported.");
            }
            return false;
        }
        return true;
    }

    /**
     * Gets the current registration ID for application on GCM service.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    private String getRegistrationId() {
        final SharedPreferences prefs = getGCMPreferences();
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            AppLog.i( "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing registration ID is not guaranteed to work with
        // the new app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion =AppAction. getAppVersion(activity);
        if (registeredVersion != currentVersion) {
            AppLog.i("App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences();
        int appVersion =AppAction. getAppVersion(context);
        AppLog.i("Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    private static String post(String endpoint, Map<String, String> params){

        if(isNetworkAvailable()) {
            AppLog.i("endpoint== >" + endpoint);
            OkHttpClient client = new OkHttpClient();

            FormEncodingBuilder formBodyBuilder = new FormEncodingBuilder();
            Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
            // constructs the POST body using the parameters
            while (iterator.hasNext()) {
                Map.Entry<String, String> param = iterator.next();
                formBodyBuilder.add(param.getKey(), param.getValue());
            }

            RequestBody formBody = formBodyBuilder.build();

            Request request = new Request.Builder()
                    .url(endpoint)
                    .post(formBody)
                    .build();


            try {
                Response response = client.newCall(request).execute();
                String str = response.body().string();
                AppLog.i("response.body().string(); == >" + str);

                return str;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }else{

            return "";
        }

    }


    private static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager  = (ConnectivityManager) myApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private String getEmail(){
        String email="";
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(activity).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                String possibleEmail = account.name;
                return  possibleEmail;
            }
        }
        return "";
    }
    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGCMPreferences() {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the registration ID in your app is up to you.
        return activity. getSharedPreferences(activity.getPackageName(), Context.MODE_PRIVATE);
    }

    public static void  OnReciveMessageFromService(Context context, Intent intent){
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        String messageType = gcm.getMessageType(intent);
        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging. MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                AppLog.i("message  "+extras.toString());

                String message = extras.getString("message");
                AppLog.i("message =" + message);
                String sessionId = extras.getString("sessionId");
                AppLog.i("sessionId ="+sessionId);
                String name=extras.getString("name");
                AppLog.i("name ="+name);

                ReceiveIntentMsg(context, intent);

            }
        }
    }

    public static void ReceiveIntentMsg(Context context, Intent intent){

        Bundle extras = intent.getExtras();

        String message = extras.getString("message");
        AppLog.i("message 2222" + message);


       AppAction. sendNotification(context, message);
    }

}
