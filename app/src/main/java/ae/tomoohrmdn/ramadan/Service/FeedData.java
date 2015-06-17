package ae.tomoohrmdn.ramadan.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by tareq on 06/12/2015.
 */
public class FeedData {

    public long ShowId;
    public long ChannelId;
    public String ChannelImgUrl;
    public String ChannelName;
    public int OrderSeq;
    public String ShowName;
    public String ShowImgUrl;
    public String ShowDescription;
    public boolean isTopShow;
    public Integer ShowCategoryType;

    public List<EpisodeFeedData> Episodes;

}
