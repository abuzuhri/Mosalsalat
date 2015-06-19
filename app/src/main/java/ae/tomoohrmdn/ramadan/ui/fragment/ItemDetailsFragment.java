package ae.tomoohrmdn.ramadan.ui.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import ae.tomoohrmdn.ramadan.R;
import ae.tomoohrmdn.ramadan.db.Dao.EpisodeDao;
import ae.tomoohrmdn.ramadan.db.Dao.ShowDao;
import ae.tomoohrmdn.ramadan.db.Entity.Episode;
import ae.tomoohrmdn.ramadan.db.Entity.Show;
import ae.tomoohrmdn.ramadan.ui.activity.OneFragmentActivity;
import ae.tomoohrmdn.ramadan.utils.AppAction;
import ae.tomoohrmdn.ramadan.utils.AppLog;
import ae.tomoohrmdn.ramadan.utils.DialogUtils;
import ae.tomoohrmdn.ramadan.utils.IntentUtils;
import ae.tomoohrmdn.ramadan.view.Adapter.ItemAdapter;
import ae.tomoohrmdn.ramadan.view.Adapter.ItemDetailsAdapter;
import ae.tomoohrmdn.ramadan.view.Event.IClickCardView;

/**
 * Created by tareq on 06/11/2015.
 */
public class ItemDetailsFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private int position;

    private Show showObj = null;
    private boolean isViewInited = false;

    private TextView objectName;
    private TextView objectID;
    private ImageView photo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //Toast.makeText(getActivity(),"ID="+ID,Toast.LENGTH_LONG).show();

        OneFragmentActivity activity = ((OneFragmentActivity) getActivity());
        if (activity != null)
            activity.RemoveToolBarShadow();

        ShowDao showDao = new ShowDao();
        showObj = showDao.getById(ID);
        if(showDao!=null)
            showDao.setSeen(true,ID,showObj.ServerId);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_item_details, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        final FloatingActionButton fb = (FloatingActionButton) rootView.findViewById(R.id.fab);
        //DocumentView informationText=(DocumentView) rootView.findViewById(R.id.informationText);
        View header = rootView.findViewById(R.id.header);

        photo = (ImageView) rootView.findViewById(R.id.objectIcon);

        objectName = (TextView) rootView.findViewById(R.id.objectName);
        objectID = (TextView) rootView.findViewById(R.id.objectID);

        AppLog.i("showObj.Episodes() + > " + showObj.Episodes().size());


        if (showObj.Episodes() != null && showObj.Episodes().size()>0) {
            fb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AppAction.OpenVedio(showObj.Episodes().get(0).getId(),getActivity());
                }
            });


            ViewTreeObserver vto = fb.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (!isViewInited) {
                        int width = fb.getWidth();
                        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) fb.getLayoutParams();
                        AppLog.i("ne width ==>" + width);
                        AppLog.i("ne (width/2)*-1 ==>" + (width / 2) * -1);

                        params1.setMargins(0, 0, px(20), (width / 2) * -1);
                        fb.setLayoutParams(params1);
                        isViewInited = true;
                    }
                }
            });
        } else {
            fb.setVisibility(View.GONE);
            DialogUtils.OkDialog(getActivity(), "", getString(R.string.no_Episodes_found));

        }



        Picasso.with(getActivity()).load(showObj.Channel.LogoUrl).into(photo);
        objectID.setText(showObj.Channel.Name);
        objectName.setText(showObj.Name);

        AppLog.i("call ItemDetailsAdapter");

        mAdapter = new ItemDetailsAdapter(showObj.Episodes(),getActivity(),false,new IClickCardView() {
            @Override
            public void onClick(View v, long ID) {
                AppAction.OpenVedio(ID,getActivity());
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_object_details, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        //rRemove it
        menu.removeItem(R.id.question);

        if (showObj.isFavorite != null && showObj.isFavorite.equals(true)) {
            menu.removeItem(R.id.favorite_outline);
        } else menu.removeItem(R.id.favorite);

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        ShowDao showDao = new ShowDao();

        //noinspection SimplifiableIfStatement

        if (id == R.id.favorite) {
            showObj.isFavorite = false;
            showDao.Save(showObj);
            showDao.setFavorite(false, ID,showObj.ServerId);
            getActivity().invalidateOptionsMenu();
            return true;
        } else if (id == R.id.favorite_outline) {
            showObj.isFavorite = true;
            showDao.Save(showObj);
            showDao.setFavorite(true, ID,showObj.ServerId);
            getActivity().invalidateOptionsMenu();
            return true;
        } else if (id == R.id.question) {
            //AppAction.OpenActivityWithFRAGMENTSearch(MainActivity.this, ItemListFragment.class.getName());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}