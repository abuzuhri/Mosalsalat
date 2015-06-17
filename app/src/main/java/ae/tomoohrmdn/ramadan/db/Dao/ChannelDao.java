package ae.tomoohrmdn.ramadan.db.Dao;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.List;

import ae.tomoohrmdn.ramadan.db.Entity.Channel;

/**
 * Created by tareq on 06/13/2015.
 */
public class ChannelDao extends BaseDao
{
    public Channel getById(long Id){
        return getById(Channel.class, Id);
    }

    public Channel getByServerId(long ServerId){

        List<Channel> list= new Select()
                .from(Channel.class)
                .where("ServerId = ?",ServerId)
                .execute();
        if(list==null || list.size()==0)
            return null;
        else return list.get(0);
    }

    public List<Channel> getAll(){
        return getAll(Channel.class);
    }

    public void Save(Channel shop) {
        shop.save();
    }

    public void DeleteAll(){
        new Delete().from(Channel.class).execute();
    }
}
