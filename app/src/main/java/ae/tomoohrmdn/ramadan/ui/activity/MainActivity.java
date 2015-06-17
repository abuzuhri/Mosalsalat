package ae.tomoohrmdn.ramadan.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import ae.tomoohrmdn.ramadan.R;
import ae.tomoohrmdn.ramadan.Service.DataLoading;
import ae.tomoohrmdn.ramadan.db.Entity.Show;
import ae.tomoohrmdn.ramadan.ui.fragment.ItemListFragment;
import ae.tomoohrmdn.ramadan.utils.AppAction;
import ae.tomoohrmdn.ramadan.utils.AppConstant;
import ae.tomoohrmdn.ramadan.utils.AppLog;
import ae.tomoohrmdn.ramadan.view.Event.IDataLodingCollectionListener;

/**
 * Created by tareq on 06/11/2015.
 */
public class MainActivity extends BaseActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Handle Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        CreateDrawer(toolbar);
        SetupToolbarShadow();

        selectItem(AppConstant.AppDrawer.Home.id);


        AppAction.getNewoffer(this);
    }



    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle(R.string.app_name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu=menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search) {
            long AppId=R.id.search;
            AppAction.OpenActivityWithFRAGMENTSearch(MainActivity.this, ItemListFragment.class.getName());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
