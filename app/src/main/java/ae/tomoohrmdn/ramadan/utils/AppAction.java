package ae.tomoohrmdn.ramadan.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import ae.tomoohrmdn.ramadan.Service.DataLoading;
import ae.tomoohrmdn.ramadan.application.OverridePendingUtil;
import ae.tomoohrmdn.ramadan.db.Dao.EpisodeDao;
import ae.tomoohrmdn.ramadan.db.Entity.Episode;
import ae.tomoohrmdn.ramadan.db.Entity.Show;
import ae.tomoohrmdn.ramadan.ui.activity.OneFragmentActivity;
import ae.tomoohrmdn.ramadan.view.Event.IDataLodingCollectionListener;

/**
 * Created by tareq on 06/11/2015.
 */
public class AppAction {

    public static class EXTRA {
        public static final String IDEXTRA = "IDEXTRA";
        public static final String SEARCHEXTRA = "SEARCHEXTRA";
        public static final String FRAGMENTEXTRA = "FRAGMENT";
        public static final String POSITION = "POSITION";
        public static final String MENUID = "MENUID";


    }

    public  static void OpenActivityIntent(Activity context,Intent intent){
        context.startActivity(intent);
        OverridePendingUtil.in(context);

    }

    public static void OpenActivity(Activity context,Class<?> cls){
        Intent intent = new Intent(context, cls);
        OpenActivityIntent(context,intent);
    }
    public static void OpenActivityWithID(Activity context,Class<?> cls, Long ID){
        Intent intent = new Intent(context, cls);
        if(ID!=null)
            intent.putExtra(EXTRA.IDEXTRA, ID);
        OpenActivityIntent(context,intent);
    }
    public static void OpenActivityWithFRAGMENT(Activity context,Class<?> cls, String name){
        Intent intent = new Intent(context, cls);
        if(name!=null)
            intent.putExtra(EXTRA.FRAGMENTEXTRA, name);
        OpenActivityIntent(context, intent);
    }
    public static void OpenActivityWithFRAGMENTSearch(Activity context, String name){
        Intent intent = new Intent(context, OneFragmentActivity.class);
        if(name!=null)
            intent.putExtra(EXTRA.FRAGMENTEXTRA, name);
        intent.putExtra(EXTRA.SEARCHEXTRA, true);

        OpenActivityIntent(context,intent);
    }
    public static void OpenActivityWithFRAGMENT(Activity context, String name, Long ID){
        Intent intent = new Intent(context, OneFragmentActivity.class);
        if(name!=null)
            intent.putExtra(EXTRA.FRAGMENTEXTRA, name);
        if(ID!=null)
            intent.putExtra(EXTRA.IDEXTRA, ID);

        OpenActivityIntent(context, intent);
    }


    public static void OpenVedio(long ID,Activity context)
    {
        EpisodeDao episodeDao=new EpisodeDao();
        Episode episode =episodeDao.getById(ID);
        episodeDao.setSeen(true,ID,episode.ServerId);

        if(episode.VdUrl.contains("youtube.com")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(episode.VdUrl));
            context.startActivity(intent);
            PackageManager pm = context.getPackageManager();
            AppAction.OpenActivityIntent(context, intent);
        }
    }

    public static void  getNewoffer(Context context){
        AppLog.i(" DataLoading.getNewOffer ===");

        DataLoading.getNewOffer(context, new IDataLodingCollectionListener<Show>() {
            @Override
            public void onSeccess(boolean isChanged) {
                AppLog.i("isChanged >" + isChanged);
            }

            @Override
            public void onFail(String error) {

            }
        });
    }


}
