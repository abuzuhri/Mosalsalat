package ae.tomoohrmdn.ramadan.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

import ae.tomoohrmdn.ramadan.R;
import ae.tomoohrmdn.ramadan.utils.AppAction;
import ae.tomoohrmdn.ramadan.utils.AppLog;

/**
 * Created by tareq on 06/11/2015.
 */
public class OneFragmentActivity extends BaseActivity
{

private  Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        SetupToolbarShadow();
        Intent intent = getIntent();

        // Handle Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String FragmentName = intent.getStringExtra(AppAction.EXTRA.FRAGMENTEXTRA);
        AppLog.i("FragmentName = > " + FragmentName);



        Fragment fragment= Fragment.instantiate(this, FragmentName);

        //Pass Item  It to Fragment
        long ItemID = intent.getLongExtra(AppAction.EXTRA.IDEXTRA,0);
        boolean IsSearch = intent.getBooleanExtra(AppAction.EXTRA.SEARCHEXTRA,false);
        if(IsSearch) {
            MaterialSearchSetup(true);
        }

        Bundle args = new Bundle();
        args.putLong(AppAction.EXTRA.IDEXTRA, ItemID);
        fragment.setArguments(args);
        selectFragment(fragment);



    }

    @Override
    protected void onResume() {
        super.onResume();
        if(toolbar!=null)
            toolbar.setTitle(R.string.app_name);
    }
}
