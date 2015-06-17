package ae.tomoohrmdn.ramadan.db.Entity;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;


/**
 * Created by tareq on 06/11/2015.
 */
@Table(name = "Show", id = "_id")
public class Show extends BaseEntity
{
    @Column(name = "ServerId")
    public long  ServerId ;

    @Column(name = "Name")
    public String  Name ;

    @Column(name = "Description")
    public String Description ;

    @Column(name = "ImgUrl")
    public String ImgUrl ;

    @Column(name = "is_top_show")
    public Boolean isTopShow ;

    @Column(name = "is_seen")
    public Boolean isSeen ;

    @Column(name = "is_favorite")
    public Boolean isFavorite ;


    @Column(name = "Category")
    public int Category ;


    @Column(name = "Channel_Id", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public Channel Channel;

    public List<Episode> Episodes() {
        return getMany(Episode.class, "Show_Id");
    }

    public enum CategoryType{
        Series(1),
        Program(2);
        public int id;
        private CategoryType(int id) {
            this.id = id;
        }
    }

}
