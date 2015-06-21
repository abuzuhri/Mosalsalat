package ae.tomoohrmdn.ramadan.Service;

import android.content.Context;
import android.os.StrictMode;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import ae.tomoohrmdn.ramadan.db.Dao.EpisodeDao;
import ae.tomoohrmdn.ramadan.db.Dao.SeenItemDao;
import ae.tomoohrmdn.ramadan.db.Dao.ShowDao;
import ae.tomoohrmdn.ramadan.db.Dao.ChannelDao;
import ae.tomoohrmdn.ramadan.db.Entity.Channel;
import ae.tomoohrmdn.ramadan.db.Entity.Episode;
import ae.tomoohrmdn.ramadan.db.Entity.SeenItem;
import ae.tomoohrmdn.ramadan.db.Entity.Show;
import ae.tomoohrmdn.ramadan.utils.AppConstant;
import ae.tomoohrmdn.ramadan.utils.AppLog;
import ae.tomoohrmdn.ramadan.view.Event.IDataLodingCollectionListener;
import ae.tomoohrmdn.ramadan.view.Event.IDataLodingListener;

/**
 * Created by tareq on 06/12/2015.
 */
public class DataLoading extends BaseService
{

    public static void getNewOffer(Context context,final IDataLodingCollectionListener event)
    {
        AppLog.i("getNewOffer ===");
        Hashtable table=new Hashtable();
        table.put("PackageName", context.getPackageName());

        PostData(table, new IDataLodingListener() {
            @Override
            public void onSeccess(String html) {
                ChannelDao shopDao=new ChannelDao();
                ShowDao offerDao=new ShowDao();
                EpisodeDao episodeDao=new EpisodeDao();
                SeenItemDao seenItemDao=new SeenItemDao();
                //Delete All
                shopDao.DeleteAll();;
                //AppLog.i("shopDao.DeleteAll(); Done");

                Gson gson = new Gson();
                int newOffers=0;

                Type collectionType = new TypeToken<Collection<FeedData>>(){}.getType();
                Collection<FeedData> list = gson.fromJson(html, collectionType);

                AppLog.i("App List ==>"+list.size());

                Iterator itr1 = list.iterator();
                while (itr1.hasNext()) {
                    FeedData feedData = (FeedData) itr1.next();

                    Channel channel= shopDao.getByServerId(feedData.ChannelId);
                    if(channel==null){
                        channel =new Channel();
                        channel.Name=feedData.ChannelName;
                        channel.OrderSeq=feedData.OrderSeq;
                        channel.LogoUrl=feedData.ChannelImgUrl;
                        channel.ServerId=feedData.ChannelId;
                        shopDao.Save(channel);
                        //AppLog.i("Save Channel > " + channel.Name);
                    }


                    Show show= offerDao.getByServerId(feedData.ShowId);
                    if(show==null){
                        show=new Show();
                        show.ServerId=feedData.ShowId;
                        show.Name=feedData.ShowName;
                        show.Category=feedData.ShowCategoryType;
                        show.isTopShow=feedData.isTopShow;
                        AppLog.i("isTopShow > "+ show.isTopShow);
                        show.Description=feedData.ShowDescription;
                        show.ImgUrl=feedData.ShowImgUrl;
                        show.Channel =channel;
                        show.isSeen=seenItemDao.IsHave(show.ServerId, SeenItem.ViewType.Show.id, SeenItem.ActionType.Seen.id);
                        show.isFavorite=seenItemDao.IsHave(show.ServerId, SeenItem.ViewType.Show.id, SeenItem.ActionType.Favorite.id);
                        offerDao.Save(show);
                        //AppLog.i("Save Show > " + show.Name);
                        newOffers++;
                    }

                    if(feedData.Episodes!= null && feedData.Episodes.size() >0) {
                        for (EpisodeFeedData item : feedData.Episodes) {
                            Episode episode = episodeDao.getByServerId(item.EpisodeId);
                            if (episode == null) {
                                episode = new Episode();
                                episode.Show=show;
                                episode.PostDate = new Date(item.EpisodePostDate);
                                episode.SeqNum = item.EpisodeSeqNum;
                                episode.ServerId = item.EpisodeId;
                                episode.VdUrl = item.EpisodeVdUrl;
                                episode.isSeen=seenItemDao.IsHave(episode.ServerId, SeenItem.ViewType.Episode.id, SeenItem.ActionType.Seen.id);
                                episode.isFavorite=seenItemDao.IsHave(episode.ServerId, SeenItem.ViewType.Episode.id, SeenItem.ActionType.Favorite.id);
                                episodeDao.Save(episode);
                                //AppLog.i("Save Episode > " + episode.VdUrl);
                            }
                        }
                    }
                }

                boolean isHaveNewData=false;
                if(newOffers>0)
                    isHaveNewData=true;

                event.onSeccess(isHaveNewData);
            }

            @Override
            public void onFail(String error) {
                event.onFail(error);
            }
        });
    }

    private static void PostData(Hashtable table,IDataLodingListener event){
        AppLog.i("PostData ===");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final OkHttpClient client = new OkHttpClient();
        FormEncodingBuilder formParms=new FormEncodingBuilder();

        Enumeration e = table.keys();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            System.out.println(key + " : " + table.get(key));
            formParms.add(key, table.get(key).toString());
            AppLog.i( key + "==>" + table.get(key).toString());
        }
        RequestBody formBody = formParms.build();

        Request request = new Request.Builder().url(AppConstant.ServerUrlNewFeed).post(formBody).build();
        AppLog.i("AppConstant.ServerUrlNewFeed >" + AppConstant.ServerUrlNewFeed);
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String html=response.body().string();
                event.onSeccess(html);

                System.out.println(html);
                AppLog.i("Appp ==>"+ html);

            }
        }catch (IOException ex){
            ex.printStackTrace();
            event.onFail(ex.getMessage());
        }catch (Exception ex){
            ex.printStackTrace();
            event.onFail(ex.getMessage());
        }
    }
}
