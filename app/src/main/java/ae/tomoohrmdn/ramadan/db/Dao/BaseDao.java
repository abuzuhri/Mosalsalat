package ae.tomoohrmdn.ramadan.db.Dao;

import com.activeandroid.Model;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by tareq on 06/11/2015.
 */
public class BaseDao
{
    public static <T extends Model> T getById(Class<T> type, long id) {
        return T.load(type, id);
    }

    public static <T extends Model> List<T> getAll(Class<T> type) {
        return new Select().from(type).execute();
    }

}
