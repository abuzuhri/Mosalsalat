package ae.tomoohrmdn.ramadan.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.List;

import ae.tomoohrmdn.ramadan.R;
import ae.tomoohrmdn.ramadan.Service.DataLoading;
import ae.tomoohrmdn.ramadan.db.Dao.EpisodeDao;
import ae.tomoohrmdn.ramadan.db.Dao.ShowDao;
import ae.tomoohrmdn.ramadan.db.Entity.Episode;
import ae.tomoohrmdn.ramadan.db.Entity.Show;
import ae.tomoohrmdn.ramadan.utils.AppAction;
import ae.tomoohrmdn.ramadan.utils.AppLog;
import ae.tomoohrmdn.ramadan.view.Adapter.ItemAdapter;
import ae.tomoohrmdn.ramadan.view.Adapter.ItemDetailsAdapter;
import ae.tomoohrmdn.ramadan.view.Event.IClickCardView;
import ae.tomoohrmdn.ramadan.view.Event.IDataLodingCollectionListener;

/**
 * Created by tareq on 06/17/2015.
 */
public class ItemListEpisodeFragment extends BaseFragment
{
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private int position,MenuId;
    private String searchText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        position= getArguments().getInt(AppAction.EXTRA.POSITION,0);
        MenuId= getArguments().getInt(AppAction.EXTRA.MENUID,0);
        AppLog.i("MenuId =>" + MenuId);
        searchText= getArguments().getString(AppAction.EXTRA.SEARCHEXTRA, "");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_item_list, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        FillDate();
        //mRecyclerView.setItemAnimator(new FeedItemAnimator());
        final Activity activity=getActivity();

        FloatingActionButton fab=(FloatingActionButton) rootView.findViewById(R.id.fab);
        //fab.attachToRecyclerView(mRecyclerView);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // AppAction.OpenActivityWithFRAGMENT(activity, OneFragmentActivity.class, ItemDetailsFragment.class.getName());


            }
        });

        return rootView;
    }


    private void FillDate(){
        EpisodeDao episodeDao=new EpisodeDao();

        AppLog.i("episodeDao.getAll() >> "+episodeDao.getAll().size());

        mAdapter = new ItemDetailsAdapter(episodeDao.getAll(),getActivity(),new IClickCardView() {
            @Override
            public void onClick(View v, long ID) {
                AppAction.OpenVedio(ID,getActivity());
            }
        });
        mRecyclerView.setAdapter(mAdapter);

    }
}
