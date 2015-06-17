package ae.tomoohrmdn.ramadan.db.Entity;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by tareq on 06/11/2015.
 */

@Table(name = "SeenItems", id = "_id")
public class SeenItem extends BaseEntity
{

    @Column(name = "ServerId")
    public long ServerId;

    @Column(name = "ViewTypeId")
    public int ViewTypeId;

    @Column(name = "ActionTypeId")
    public int ActionTypeId;

    public enum ViewType{
        Show(1),
        Episode(2);
        public int id;
        private ViewType(int id) {
            this.id = id;
        }
    }

    public enum ActionType{
        Seen(1),
        Favorite(2);
        public int id;
        private ActionType(int id) {
            this.id = id;
        }
    }


}
