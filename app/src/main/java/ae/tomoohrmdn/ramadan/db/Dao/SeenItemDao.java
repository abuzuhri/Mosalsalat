package ae.tomoohrmdn.ramadan.db.Dao;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.List;

import ae.tomoohrmdn.ramadan.db.Entity.Episode;
import ae.tomoohrmdn.ramadan.db.Entity.SeenItem;
import ae.tomoohrmdn.ramadan.utils.AppLog;

/**
 * Created by tareq on 06/15/2015.
 */
public class SeenItemDao extends BaseDao
{
    public SeenItem getByServerId(long ServerId,int ViewTypeId,int ActionTypeId){
        AppLog.i("getByServerId))) ServerId=> "+ServerId+" ViewTypeId=> "+ViewTypeId+" ActionTypeId=> "+ActionTypeId);
        List<SeenItem> list= new Select()
                .from(SeenItem.class)
                .where("ServerId = ? AND ViewTypeId = ? AND ActionTypeId = ?",ServerId,ViewTypeId,ActionTypeId)
                .execute();
        if(list==null || list.size()==0)
            return null;
        else return list.get(0);
    }

    public boolean IsHave(long ServerId,int ViewTypeId,int ActionTypeId){
        AppLog.i("IsHave))) ServerId=> "+ServerId+" ViewTypeId=> "+ViewTypeId+" ActionTypeId=> "+ActionTypeId);
        SeenItem seenItem =getByServerId(ServerId,ViewTypeId,ActionTypeId);
        if(seenItem!=null)
            return true;
        else return  false;
    }

    public void Add(long ServerId,int ViewTypeId,int ActionTypeId) {
        AppLog.i("Add))) ServerId=> "+ServerId+" ViewTypeId=> "+ViewTypeId+" ActionTypeId=> "+ActionTypeId);
        SeenItem itm = new SeenItem();
        itm.ActionTypeId = ActionTypeId;
        itm.ViewTypeId = ViewTypeId;
        itm.ServerId = ServerId;
        itm.save();
    }

    public void Remove(long ServerId,int ViewTypeId,int ActionTypeId) {
        AppLog.i("Remove))) ServerId=> "+ServerId+" ViewTypeId=> "+ViewTypeId+" ActionTypeId=> "+ActionTypeId);

        new Delete().from(SeenItem.class).where("ServerId = ? AND ViewTypeId = ? AND ActionTypeId = ?",ServerId,ViewTypeId,ActionTypeId).execute();

    }

}
