package ae.tomoohrmdn.ramadan.view.Holder;

import android.view.View;
import android.widget.*;

import ae.tomoohrmdn.ramadan.R;
import ae.tomoohrmdn.ramadan.view.Event.IClickCardView;

/**
 * Created by tareq on 06/11/2015.
 */
public class ItemViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener{

    public ImageView ChannelLogoUrl;
    public TextView ChannelName;
    public TextView timestamp;
    public TextView ShowDescription;
    public ImageView ShowLogoUrl;
    public TextView ShowName;


    public IClickCardView mListener;

    public ItemViewHolder(View itemLayoutView, IClickCardView listener) {
        super(itemLayoutView);
        mListener=listener;


        ChannelLogoUrl =(ImageView) itemLayoutView.findViewById(R.id.profilePic);
        ChannelName =(TextView) itemLayoutView.findViewById(R.id.name);
        timestamp=(TextView) itemLayoutView.findViewById(R.id.timestamp);
        ShowDescription =(TextView) itemLayoutView.findViewById(R.id.txtStatusMsg);
        ShowLogoUrl =(ImageView) itemLayoutView.findViewById(R.id.feedImage1);
        ShowName =(TextView) itemLayoutView.findViewById(R.id.txtShowName);


        itemLayoutView.setOnClickListener(this);


    }



    @Override
    public void onClick(View v) {
        mListener.onClick(v,getID());
    }


}