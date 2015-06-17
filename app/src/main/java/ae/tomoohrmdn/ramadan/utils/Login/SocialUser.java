package ae.tomoohrmdn.ramadan.utils.Login;

/**
 * Created by tareq on 06/11/2015.
 */
public class SocialUser {
    public String email;
    public String name;
    public String id;
    public int network;
    public  String avatarURL;

    public static class NetworkType
    {
        public static final int GOOGLEPLUS=1;
        public static final int FACEBOOK=2;
    }
}