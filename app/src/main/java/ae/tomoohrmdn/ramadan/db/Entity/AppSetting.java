package ae.tomoohrmdn.ramadan.db.Entity;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by tareq on 06/11/2015.
 */
@Table(name = "app_setting", id = "_id")
public class AppSetting  extends BaseEntity {

    @Column(name = "Notification")
    public Boolean Notification ;



}