package ae.tomoohrmdn.ramadan.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.List;

import ae.tomoohrmdn.ramadan.R;
import ae.tomoohrmdn.ramadan.Service.DataLoading;
import ae.tomoohrmdn.ramadan.db.Dao.ShowDao;
import ae.tomoohrmdn.ramadan.db.Entity.Show;
import ae.tomoohrmdn.ramadan.ui.activity.OneFragmentActivity;
import ae.tomoohrmdn.ramadan.utils.AppAction;
import ae.tomoohrmdn.ramadan.utils.AppLog;
import ae.tomoohrmdn.ramadan.view.Adapter.ItemAdapter;
import ae.tomoohrmdn.ramadan.view.Event.IClickCardView;
import ae.tomoohrmdn.ramadan.view.Event.IDataLodingCollectionListener;

/**
 * Created by tareq on 06/11/2015.
 */
public class ItemListFragment extends BaseFragment
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        ShowDao showDao = new ShowDao();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_refresh) {
            AppLog.i("AppAction.getNewoffer(getActivity());");
            AppAction.getNewoffer(getActivity());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void FillDate(){
        ShowDao offerDao=new ShowDao();
        List<Show> offerList=null;

        AppLog.i("MenuId=="+MenuId);

        if(MenuId!=0)
            offerList= offerDao.getAllByMenu(MenuId);
        else if(searchText!="")
            offerList= offerDao.getAll(searchText);
        else offerList= offerDao.getAll();

        mAdapter = new ItemAdapter(offerList,getActivity(),new IClickCardView() {
            @Override
            public void onClick(View v, long ID) {
                AppAction.OpenActivityWithFRAGMENT(getActivity(), ItemDetailsFragment.class.getName(), ID);
            }
        });
        mRecyclerView.setAdapter(mAdapter);

    }
}
