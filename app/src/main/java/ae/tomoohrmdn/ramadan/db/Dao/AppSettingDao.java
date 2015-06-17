package ae.tomoohrmdn.ramadan.db.Dao;

import com.activeandroid.query.Update;

import ae.tomoohrmdn.ramadan.db.Entity.AppSetting;

/**
 * Created by tareq on 06/11/2015.
 */
public class AppSettingDao extends BaseDao{

    public AppSetting getAppSetting(){
        return AppSetting.load(AppSetting.class, 1);
    }

    public void setNotification(boolean isEnable){

        new Update(AppSetting.class)
                .set("Notification = ?",isEnable ? 1 : 0)
                .where("_id = ?", 1)
                .execute();
    }


}
