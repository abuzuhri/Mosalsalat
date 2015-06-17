package ae.tomoohrmdn.ramadan.utils.Login;

import android.content.Intent;

/**
 * Created by tareq on 06/11/2015.
 */
public interface SocialNetwork
{
    boolean isLoggedIn();
    void Logout();
    void Login(OnLoginListener lsnr);
    void onActivityResult(int requestCode, int resultCode, Intent data);
}