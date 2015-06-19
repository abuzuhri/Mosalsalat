package ae.tomoohrmdn.ramadan.view.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amulyakhare.textdrawable.TextDrawable;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import ae.tomoohrmdn.ramadan.R;
import ae.tomoohrmdn.ramadan.db.Entity.Episode;
import ae.tomoohrmdn.ramadan.db.Entity.Show;
import ae.tomoohrmdn.ramadan.utils.AppLog;
import ae.tomoohrmdn.ramadan.view.Event.IClickCardView;
import ae.tomoohrmdn.ramadan.view.Holder.ItemViewHolder;

/**
 * Created by tareq on 06/15/2015.
 */
public class ItemDetailsAdapter extends RecyclerView.Adapter<ItemViewHolder>
{

    public List<Episode> mDataset;
    private Context context;
    private IClickCardView mListener;
    private static  int targetWidth=0;
    private boolean isMenu;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ItemDetailsAdapter(List<Episode> myDataset, Context context,boolean isMenu, IClickCardView mListener) {
        AppLog.i("myDataset = "+myDataset.size());
        mDataset = myDataset;
        this.context=context;
        this.mListener=mListener;
        this.isMenu=isMenu;
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_object,parent, false);

        // create ViewHolder
        ItemViewHolder viewHolder = new ItemViewHolder(itemLayoutView, new IClickCardView() {
            @Override
            public void onClick(View v,long ID) {
                mListener.onClick(v, ID);
            }
        });
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ItemViewHolder viewHolder, int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData
        Episode obj = mDataset.get(position);

        viewHolder.setID(obj.getId());
        String plusName = "";
        if (isMenu)
            plusName = obj.Show.Name + " > ";

        viewHolder.ChannelName.setText(plusName + context.getString(R.string.item_details_episode_num) + " " + obj.SeqNum);
        viewHolder.ShowName.setVisibility(View.GONE);
        viewHolder.ShowDescription.setVisibility(View.GONE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(obj.PostDate);

        TextDrawable drawable1 = TextDrawable.builder().beginConfig().bold().endConfig().buildRound(obj.SeqNum + "", randomColor());
        //viewHolder.ChannelLogoUrl.setVisibility(View.GONE);
        viewHolder.ChannelLogoUrl.setImageDrawable(drawable1);


        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(calendar.getTimeInMillis(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        viewHolder.timestamp.setText(timeAgo);

        if (isMenu) {
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                    //whatever algorithm here to compute size
                    if (targetWidth == 0)
                        targetWidth = viewHolder.ShowLogoUrl.getWidth();
                    AppLog.i("viewHolder.ShowLogoUrl.getWidth()" + targetWidth + " >>" + viewHolder.ShowLogoUrl.getWidth());

                    float ratio = (float) bitmap.getHeight() / (float) bitmap.getWidth();
                    float heightFloat = ((float) targetWidth) * ratio;

                    final android.view.ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) viewHolder.ShowLogoUrl.getLayoutParams();

                    layoutParams.height = (int) heightFloat;
                    layoutParams.width = (int) targetWidth;
                    viewHolder.ShowLogoUrl.setLayoutParams(layoutParams);
                    viewHolder.ShowLogoUrl.setImageBitmap(bitmap);

                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            Picasso.with(context).load(obj.Show.ImgUrl).into(target);
        }else{
            viewHolder.ShowLogoUrl.setVisibility(View.GONE);
        }
    }

    private int randomColor() {
        Random random = new Random();
        String[] colorsArr = context.getResources().getStringArray(R.array.colors);
        return Color.parseColor(colorsArr[random.nextInt(colorsArr.length)]);
    }


    @Override
    public int getItemCount() {
        if(mDataset != null) {
            AppLog.i("mDataset ==> " + mDataset.size());
            return mDataset.size();
        }else{
            AppLog.i("mDataset ==> " + 0);
            return 0;
        }
    }
}
