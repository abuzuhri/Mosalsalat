package ae.tomoohrmdn.ramadan.db.Dao;

import com.activeandroid.query.Select;
import com.activeandroid.query.Update;

import java.util.List;

import ae.tomoohrmdn.ramadan.db.Entity.SeenItem;
import ae.tomoohrmdn.ramadan.db.Entity.Show;
import ae.tomoohrmdn.ramadan.utils.AppConstant;
import ae.tomoohrmdn.ramadan.utils.AppLog;

/**
 * Created by tareq on 06/11/2015.
 */
public class ShowDao extends BaseDao
{
    public Show getById(long Id){
        return getById(Show.class, Id);
    }

    public Show getByServerId(long ServerId){

        List<Show> list= new Select()
                .from(Show.class)
                .where("ServerId = ?",ServerId)
                .execute();
        if(list==null || list.size()==0)
            return null;
        else return list.get(0);
    }

    public List<Show> getAll(){
        return getAll(Show.class);
    }

    public List<Show> getAll(String search){
        search="%"+search+"%";
        AppLog.i("search==> " + search);

        return new Select()
                .from(Show.class)
                .where("Name like ?", search).or("Description like  ?",search)
                .execute();
    }

    public List<Show> getAllByMenu(int menuId){

        if(menuId== AppConstant.AppDrawer.Home.id){
            return getAll();
        }else if(menuId== AppConstant.AppDrawer.Series.id){
            return new Select()
                    .from(Show.class)
                    .where("Category = ?", Show.CategoryType.Series.id)
                    .execute();
        }else if(menuId== AppConstant.AppDrawer.Program.id){
            return new Select()
                    .from(Show.class)
                    .where("Category = ?", Show.CategoryType.Program.id)
                    .execute();
        }else if(menuId== AppConstant.AppDrawer.TopShows.id){
            return new Select()
                    .from(Show.class)
                    .where("is_top_show = ?", 1)
                    .execute();
        }else if(menuId== AppConstant.AppDrawer.Favorites.id){
            return new Select()
                    .from(Show.class)
                    .where("is_favorite = ?", 1)
                    .execute();
        }
        return null;
    }


    public void Save(Show shop) {
        shop.save();
    }

    public void setFavorite(boolean isFav,long ID,long ServerId){

        if(isFav) {
            new Update(Show.class)
                    .set("is_favorite = ?", isFav ? 1 : 0)
                    .where("_id = ?", ID)
                    .execute();

            new SeenItemDao().Add(ServerId, SeenItem.ViewType.Show.id, SeenItem.ActionType.Favorite.id);
        }else {
            new SeenItemDao().Remove(ServerId, SeenItem.ViewType.Show.id, SeenItem.ActionType.Favorite.id);
        }


    }

    public void setSeen(boolean isSeen,long ID,long ServerId){

        if(isSeen) {
            new Update(Show.class)
                    .set("is_seen = ?", isSeen ? 1 : 0)
                    .where("_id = ?", ID)
                    .execute();

            new SeenItemDao().Add(ServerId, SeenItem.ViewType.Show.id, SeenItem.ActionType.Seen.id);
        }else{
            new SeenItemDao().Remove(ServerId, SeenItem.ViewType.Show.id, SeenItem.ActionType.Seen.id);
        }
    }

}
