package ae.tomoohrmdn.ramadan.db.Dao;

import com.activeandroid.query.Select;
import com.activeandroid.query.Update;

import java.util.List;

import ae.tomoohrmdn.ramadan.db.Entity.Episode;
import ae.tomoohrmdn.ramadan.db.Entity.SeenItem;
import ae.tomoohrmdn.ramadan.db.Entity.Show;

/**
 * Created by tareq on 06/15/2015.
 */
public class EpisodeDao extends BaseDao
{

    public Episode getById(long Id){
        return getById(Episode.class, Id);
    }

    public Episode getByServerId(long ServerId){

        List<Episode> list= new Select()
                .from(Episode.class)
                .where("ServerId = ?",ServerId)
                .execute();
        if(list==null || list.size()==0)
            return null;
        else return list.get(0);
    }

    public List<Episode> getAll(){
        List<Episode> list= new Select()
                .from(Episode.class)
                .orderBy("is_seen,PostDate desc")
                .execute();
        if(list==null || list.size()==0)
            return null;
        else return list;
    }

    public void Save(Episode episode) {
        episode.save();
    }


    public void setFavorite(boolean isFav,long ID,long ServerId){

        if(isFav) {
            new Update(Episode.class)
                    .set("is_favorite = ?", isFav ? 1 : 0)
                    .where("_id = ?", ID)
                    .execute();

            new SeenItemDao().Add(ServerId, SeenItem.ViewType.Episode.id, SeenItem.ActionType.Favorite.id);
        }else {
            new SeenItemDao().Remove(ServerId, SeenItem.ViewType.Episode.id, SeenItem.ActionType.Favorite.id);
        }


    }

    public void setSeen(boolean isSeen,long ID,long ServerId){

        if(isSeen) {
            new Update(Show.class)
                    .set("is_seen = ?", isSeen ? 1 : 0)
                    .where("_id = ?", ID)
                    .execute();

            new SeenItemDao().Add(ServerId, SeenItem.ViewType.Episode.id, SeenItem.ActionType.Seen.id);
        }else{
            new SeenItemDao().Remove(ServerId, SeenItem.ViewType.Episode.id, SeenItem.ActionType.Seen.id);
        }
    }
}
