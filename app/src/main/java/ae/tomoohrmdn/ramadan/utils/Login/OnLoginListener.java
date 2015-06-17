package ae.tomoohrmdn.ramadan.utils.Login;

/**
 * Created by tareq on 06/11/2015.
 */
public interface OnLoginListener{
    void onSuccess(SocialUser user);
    void onFail();
}