package ae.tomoohrmdn.ramadan.db.Entity;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by tareq on 06/11/2015.
 */
@Table(name = "Channel", id = "_id")
public class Channel extends BaseEntity
{

    @Column(name = "ServerId")
    public long ServerId ;

    @Column(name = "OrderSeq")
    public int OrderSeq ;

    @Column(name = "Name")
    public String Name ;

    @Column(name = "LogoUrl")
    public String LogoUrl ;

    @Column(name = "is_top_show")
    public boolean isTopShow ;




}
