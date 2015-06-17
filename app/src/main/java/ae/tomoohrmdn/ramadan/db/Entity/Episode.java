package ae.tomoohrmdn.ramadan.db.Entity;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

/**
 * Created by tareq on 06/13/2015.
 */

@Table(name = "episode", id = "_id")
public class Episode extends BaseEntity
{

    @Column(name = "ServerId")
    public long  ServerId ;

    @Column(name = "SeqNum")
    public int SeqNum;

    @Column(name = "PostDate")
    public Date PostDate;

    @Column(name = "VdUrl")
    public String VdUrl;

    @Column(name = "is_seen")
    public Boolean isSeen ;

    @Column(name = "is_favorite")
    public Boolean isFavorite ;


    @Column(name = "Show_Id", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public Show Show;
}
