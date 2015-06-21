package ae.tomoohrmdn.ramadan.libary;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Hashtable;
import java.util.Locale;

/**
 * Created by tareq on 06/08/2015.
 */
public class AppInfo {
    Context context;
    SharedPreferences localSharedPreferences;
    public  AppInfo(Context context){
        this.context=context;
        localSharedPreferences = context.getSharedPreferences(context.getPackageName(), 0);
    }
    public  AppInfo(Context context,String jsonResult){
        this.context=context;
        localSharedPreferences = context.getSharedPreferences(context.getPackageName(), 0);
        fillData(jsonResult);

    }
    public String AdsID;
    public String LastNotificationID;
    public String Email;
    public String AppID;
    public String AppKey;
    public String NetworkType;
    public String Lang;
    public String CountryCode;
    public String Json;

    public String AdsBanner;
    public String AdsInterstitial;
    static String fileName="json.json";


    void fillData(String jsonResult) {
        try {
            JSONObject json = new JSONObject(jsonResult);
            String NetworkType = json.getString("NetworkType");
            String AdMob = json.getString("AdMob");
            String AdMobIn = json.getString("AdMobIn");
            String RedirectAppID = json.getString("RedirectAppID");
            String AdsAppID = json.getString("AdsAppID");
            //String AdsInAppID= json.getString("AdsInAppID");
            String AdsDescription = json.getString("AdsDescription");
            String AdsAppPackage = json.getString("AdsAppPackage");
            String NotificationID = json.getString("NotificationID");
            String NotificationDectiption = json.getString("NotificationDectiption");
            String Json = json.getString("Json");
            int isJsonChanged = json.getInt("isJsonChanged");//.getString("NotificationDectiption");

            Log.i("tag","NetworkType==>"+NetworkType);
            Log.i("tag","AdMob==>"+AdMob);
            Log.i("tag","AdMobIn==>"+AdMobIn);
            Log.i("tag","RedirectAppID==>"+RedirectAppID);

            Log.i("tag","AdsAppID==>"+AdsAppID);
            Log.i("tag","AdsAppPackage==>"+AdsAppPackage);
            Log.i("tag","AdsDescription==>"+AdsDescription);
            Log.i("tag","NotificationID==>"+NotificationID);
            Log.i("tag","NotificationDectiption==>"+NotificationDectiption);
            Log.i("tag","getAdsAppID()=="+getAdsAppID());

            Json=Json.replace("\\\"","\"");
            Log.i("tag","Json==>"+Json);
            Log.i("tag","isJsonChanged==>"+isJsonChanged);

            if(NotificationID==null || NotificationID=="")
                NotificationID="0";
            if(AdsAppID==null || AdsAppID=="")
                AdsAppID="0";

            boolean OldisJsonChanged= localSharedPreferences.getBoolean("isJsonChanged", false);

            if(isJsonChanged==0)
                localSharedPreferences.edit().putBoolean("isJsonChanged", false).commit();
            else localSharedPreferences.edit().putBoolean("isJsonChanged", true).commit();


            if(!AdMob.equals(getAdsBanner()) && !AdMob.equals("")){
                localSharedPreferences.edit().putString("AdmobAdsId", AdMob).commit();
            }
            if(!AdMobIn.equals(getAdsInterstitial()) && !AdMobIn.equals("")){
                localSharedPreferences.edit().putString("AdmobAdsInId", AdMobIn).commit();
            }
            if(!NetworkType.equals(getNetworkType()) && !NetworkType.equals("")){
                localSharedPreferences.edit().putString("NetworkType", NetworkType).commit();
            }
/*
            if(isJsonChanged==1)
            {
                Log.i("tag","readFromFile()==>"+readFromFile());
                Log.i("tag","Json==>"+Json);
                if(!readFromFile().equals(Json)){
                    Log.i("tagi","Restart=====>");
                    writeToFile(Json);
                    Intent i=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(i);
                    finish();
                    return;
                }else{
                    Log.i("tagi","Same file");
                }
            }else{
                if(OldisJsonChanged){
                    Intent i=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(i);
                    finish();
                    return;
                }
            }
            if(!RedirectAppID.trim().equals("")){
                startApplication(RedirectAppID);
            }else{
                if(!AdsAppID.equals(getAdsAppID()) && !AdsAppID.equals("")){
                    localSharedPreferences.edit().putString("AdsAppID", AdsAppID).commit();
                    dialogAdsNewPackage(AdsAppPackage,AdsDescription);
                }
                else showRateDialog();
                if(!NotificationID.equals(getNotificationID()) && !NotificationID.equals("")){
                    localSharedPreferences.edit().putString("NotificationID", NotificationID).commit();
                    localSharedPreferences.edit().putString("NotificationDectiption", NotificationDectiption).commit();
                }
            }
            */
            Log.i("tag","Go one result"+jsonResult);


        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }
    public Hashtable getPostParm(){
        AdsID=getAdsAppID();
        LastNotificationID=getNotificationID();
        Email=getUserEmails();
        AppID=context.getPackageName();
        AppKey="";
        NetworkType=getNetworkType();
        AdsBanner=getAdsBanner();
        AdsInterstitial=getAdsInterstitial();
        Lang= Locale.getDefault().getLanguage();
        CountryCode=getCountry();
        Json =getJsonFile();


        Hashtable table = new Hashtable();
        table.put("AdsID", AdsID);
        table.put("NotificationID",LastNotificationID);
        table.put("email", Email);
        table.put("AppID", AppID);
        table.put("AppKey", AppKey);
        table.put("NetworkType", NetworkType);
        table.put("admob", AdsBanner);
        table.put("admobIn",AdsInterstitial);
        table.put("Lang", Lang);
        table.put("Country", CountryCode);
        table.put("Json", Json);

        return  table;
    }

    public String getAdsAppID(){
        String AdsAppID = localSharedPreferences.getString("AdsAppID", "0");
        return AdsAppID;
    }

    public String getNotificationID(){
        String NotificationID = localSharedPreferences.getString("NotificationID", "0");
        return NotificationID;
    }
    public String getNetworkType(){
        String AdmobAdsId = localSharedPreferences.getString("NetworkType", getMetaData("AppContollerNetworkType"));
        return AdmobAdsId;
    }
    public String getAdsBanner(){
        String AdmobAdsId = localSharedPreferences.getString("AdsBanner", getMetaData("AppContollerAdsBanner"));
        return AdmobAdsId;
    }
    public String getAdsInterstitial(){
        String AdsInterstitial = localSharedPreferences.getString("AdsInterstitial", getMetaData("AppContollerAdsInterstitial"));
        return AdsInterstitial;
    }
    private String getCountry() {
        String country="";
        TelephonyManager manager = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        if(manager!=null)
            country = manager.getNetworkCountryIso();
        if(country==null){
            country = manager.getSimCountryIso();
        }

        if(country==null)
            return "";
        else return  country;


    }

    private String getUserEmails(){
        try {
            AccountManager accountManager = AccountManager.get(context);
            Account account = getAccount(accountManager);

            if (account == null) {
                return "";
            } else {
                return account.name;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return "";
    }

    private static Account getAccount(AccountManager accountManager) {

        Account[] accounts = accountManager.getAccountsByType("com.google");
        Account account;
        if (accounts.length > 0) {
            account = accounts[0];
        } else {
            account = null;
        }
        return account;
    }

    private void writeToFile(String data) {
        File file = context. getApplicationContext().getFileStreamPath(fileName);
        try {
            if (!file.exists())
                file.createNewFile();

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.getApplicationContext().openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    private String getJsonFile(){
        try {
            boolean isJsonChanged = localSharedPreferences.getBoolean("isJsonChanged", false);
            if (!isJsonChanged) {
                StringBuilder buf = new StringBuilder();
                InputStream json;

                try {
                    String str = "";
                    json = context.getAssets().open(fileName);
                    BufferedReader in = new BufferedReader(new InputStreamReader(json));

                    while ((str = in.readLine()) != null) {
                        buf.append(str);
                    }
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return buf.toString();
            } else {
                return readFromFile();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
    private String readFromFile() {

        String ret = "";

        try {
            InputStream inputStream =context. getApplicationContext().openFileInput(fileName);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }
    private String  getMetaData(String Metakey){
        try {
            ApplicationInfo ai =context. getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            if(bundle!=null) {
                String keyValue = bundle.getString(Metakey);

                for (String key : bundle.keySet()) {
                    Log.d("TEST", key + " is a key in the bundle");
                }

                return keyValue;
            }else return "undefined";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return "";
    }
}