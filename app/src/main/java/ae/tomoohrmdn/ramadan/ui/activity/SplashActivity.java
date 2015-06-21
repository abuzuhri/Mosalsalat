package ae.tomoohrmdn.ramadan.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import ae.tomoohrmdn.ramadan.utils.AppAction;
import ae.tomoohrmdn.ramadan.utils.Login.FacebookLogin;
import ae.tomoohrmdn.ramadan.utils.Login.GooglPlusLogin;
import ae.tomoohrmdn.ramadan.utils.Login.SocialNetwork;

/**
 * Created by tareq on 06/11/2015.
 */
public class SplashActivity extends BaseActivity{

    private static int SPLASH_TIME_OUT=1000;


    SocialNetwork facebookNetwork;
    SocialNetwork googleNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        facebookNetwork=new FacebookLogin(this);
        googleNetwork=new GooglPlusLogin(this);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=null;
                if(facebookNetwork.isLoggedIn() || googleNetwork.isLoggedIn()){
                    AppAction.OpenActivity(SplashActivity.this, MainActivity.class);
                }else{
                    AppAction.OpenActivity(SplashActivity.this,LoginActivity.class);
                }
                finish();
            }
        }, SPLASH_TIME_OUT);


    }
}