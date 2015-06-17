package ae.tomoohrmdn.ramadan.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import ae.tomoohrmdn.ramadan.R;
import ae.tomoohrmdn.ramadan.ui.fragment.FragmentLoginPager;
import ae.tomoohrmdn.ramadan.utils.AppAction;
import ae.tomoohrmdn.ramadan.utils.AppLog;
import ae.tomoohrmdn.ramadan.utils.Login.FacebookLogin;
import ae.tomoohrmdn.ramadan.utils.Login.GooglPlusLogin;
import ae.tomoohrmdn.ramadan.utils.Login.OnLoginListener;
import ae.tomoohrmdn.ramadan.utils.Login.SocialNetwork;
import ae.tomoohrmdn.ramadan.utils.Login.SocialUser;
import ae.tomoohrmdn.ramadan.view.Pager.CirclePageIndicator;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by tareq on 06/11/2015.
 */
public class LoginActivity extends BaseActivity {

    ViewPager viewPager;
    MyPageAdapter pageAdapter;
    SocialNetwork socialNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        try {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }catch(Exception ex){}

        setContentView(R.layout.activity_login);

        try {


            List<Fragment> fragments = getFragments();
            pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);

            viewPager = (ViewPager) findViewById(R.id.view_pager);
            CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);


            viewPager.setAdapter(pageAdapter);
            // just set viewPager
            indicator.setViewPager(viewPager);

            Button facebook_button=(Button) findViewById(R.id.facebook_button);
            facebook_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoginSocialNetwork(SocialUser.NetworkType.FACEBOOK);
                }
            });


            Button google_button=(Button) findViewById(R.id.google_plus_button);
            google_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoginSocialNetwork(SocialUser.NetworkType.GOOGLEPLUS);
                }
            });

        }catch (Exception ex){
            ex.printStackTrace();
        }



    }


    private void LoginSocialNetwork(int NetworkType)
    {
        AppLog.i("NetworkType== >"+NetworkType);
        if(NetworkType==SocialUser.NetworkType.GOOGLEPLUS)
            socialNetwork = new GooglPlusLogin(this);
        else if(NetworkType==SocialUser.NetworkType.FACEBOOK)
            socialNetwork=new FacebookLogin(this);

        socialNetwork.Login(new OnLoginListener() {
            @Override
            public void onSuccess(SocialUser user) {
                String msg="Hi, " + user.name + "  " + user.email +" "+user.avatarURL;
                Toast.makeText(LoginActivity.this,msg , Toast.LENGTH_LONG).show();
                AppLog.i(msg);

                AppAction.OpenActivity(LoginActivity.this, MainActivity.class);
                LoginActivity.this.finish();
            }

            @Override
            public void onFail() {
                Toast.makeText(LoginActivity.this, "onFail", Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(socialNetwork!=null)
            socialNetwork.onActivityResult(requestCode, resultCode, data);
    }


    private List<Fragment> getFragments(){

        List<Fragment> fList = new ArrayList<Fragment>();



        fList.add(FragmentLoginPager.newInstance("1"));
        fList.add(FragmentLoginPager.newInstance("2"));
        fList.add(FragmentLoginPager.newInstance("3"));
        fList.add(FragmentLoginPager.newInstance("4"));


        return fList;

    }


    class MyPageAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments;


        public MyPageAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }
    }



    public void  app(){

        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted( JSONObject object, GraphResponse response) {
                        // Application code
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link");
        request.setParameters(parameters);
        request.executeAsync();
    }
}