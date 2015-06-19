package ae.tomoohrmdn.ramadan.ui.activity;

/**
 * Created by tareq on 06/11/2015.
 */

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.appevents.AppEventsLogger;
import com.google.gson.Gson;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

import java.lang.reflect.Field;
import java.util.Locale;

import ae.tomoohrmdn.ramadan.R;
import ae.tomoohrmdn.ramadan.application.OverridePendingUtil;
import ae.tomoohrmdn.ramadan.ui.fragment.ItemListEpisodeFragment;
import ae.tomoohrmdn.ramadan.ui.fragment.ItemListFragment;
import ae.tomoohrmdn.ramadan.ui.fragment.SettingFragment;
import ae.tomoohrmdn.ramadan.utils.AppAction;
import ae.tomoohrmdn.ramadan.utils.AppConstant;
import ae.tomoohrmdn.ramadan.utils.AppLog;
import ae.tomoohrmdn.ramadan.utils.Login.SocialUser;


public class BaseActivity  extends AppCompatActivity {
    protected   Toolbar toolbar;
    public View shadowView;
    public View toolbarContainer;
    public boolean toolbarHomeButtonAnimating;
    public Menu menu;

    protected View searchContainer;
    protected EditText toolbarSearchView;
    protected ImageView searchClearButton;
    protected Drawer result;


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);



    }

    @Override
    public void finish() {
        super.finish();
        OverridePendingUtil.out(this);
    }

    @Override
    public void onBackPressed() {
        if(result!=null && result.isDrawerOpen())
            result.closeDrawer();
        else {
            super.onBackPressed();
        }
    }


    protected  void MaterialSearchSetup(boolean isOpen){
        searchContainer = findViewById(R.id.search_container);
        toolbarSearchView = (EditText) findViewById(R.id.search_view);
        searchClearButton = (ImageView) findViewById(R.id.search_clear);

        // Setup search container view
        try {
            // Set cursor colour to white
            // http://stackoverflow.com/a/26544231/1692770
            // https://github.com/android/platform_frameworks_base/blob/kitkat-release/core/java/android/widget/TextView.java#L562-564
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(toolbarSearchView, R.drawable.edittext_whitecursor);
        } catch (Exception ignored) {
        }

        // Search text changed listener
        toolbarSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Search(s.toString());
                //Fragment mainFragment = getFragmentManager().findFragmentById(R.id.container);
                //if (mainFragment != null && mainFragment instanceof MainListFragment) {
                //    ((MainListFragment) mainFragment).search(s.toString());
                //}
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Clear search text when clear button is tapped
        searchClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbarSearchView.setText("");
            }
        });


        // Hide the search view
        if(isOpen)
            searchContainer.setVisibility(View.VISIBLE);
        else searchContainer.setVisibility(View.GONE);

    }


    public  void  selectFragment(Fragment fragment){
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
        }
    }

    private void Search(String txt){
        Fragment fragment= Fragment.instantiate(this, ItemListFragment.class.getName());
        Bundle args = new Bundle();
        args.putString(AppAction.EXTRA.SEARCHEXTRA, txt);
        fragment.setArguments(args);
        selectFragment(fragment);
    }


    protected void CreateDrawer(final Toolbar toolbar){

        SharedPreferences mSharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mSharedPreferences.getString(AppConstant.SharedPreferenceNames.SocialUser, "");
        SocialUser obj = gson.fromJson(json, SocialUser.class);


        final IProfile profile = new ProfileDrawerItem().withName(obj.name).withEmail(obj.email).withIcon(obj.avatarURL);

        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.drawerheader)
                .addProfiles(
                        profile
                )
                .build();


        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.ic_drawer_home).withIdentifier(AppConstant.AppDrawer.Home.id).withIcon(FontAwesome.Icon.faw_dashboard),
                        new PrimaryDrawerItem().withName(R.string.ic_drawer_episode).withIdentifier(AppConstant.AppDrawer.NewEpisode.id).withIcon(GoogleMaterial.Icon.gmd_new_releases),
                        new PrimaryDrawerItem().withName(R.string.ic_drawer_series).withIdentifier(AppConstant.AppDrawer.Series.id).withIcon(FontAwesome.Icon.faw_file_movie_o),
                        new PrimaryDrawerItem().withName(R.string.ic_drawer_program).withIdentifier(AppConstant.AppDrawer.Program.id).withIcon(GoogleMaterial.Icon.gmd_video_library),
                        new PrimaryDrawerItem().withName(R.string.ic_drawer_topshows).withIdentifier(AppConstant.AppDrawer.TopShows.id).withIcon(FontAwesome.Icon.faw_star),
                        new PrimaryDrawerItem().withName(R.string.ic_drawer_favorites).withIdentifier(AppConstant.AppDrawer.Favorites.id).withIcon(GoogleMaterial.Icon.gmd_favorite)

                )
                .addStickyDrawerItems(new PrimaryDrawerItem().withName(R.string.ic_drawer_settings).withIdentifier(AppConstant.AppDrawer.Settings.id).withIcon(GoogleMaterial.Icon.gmd_settings))
                .withSelectedItem(1)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            if (drawerItem instanceof Nameable) {
                                    toolbar.setTitle(((Nameable) drawerItem).getNameRes());

                            }

                            selectItem(drawerItem.getIdentifier());
                        }
                        return false;
                    }
                })
                .withActionBarDrawerToggleAnimated(true)
                .build();
    }



    public  void  selectItem(int filter){
        Fragment fragment = null;
        Bundle args = new Bundle();
        AppLog.i("position=> " + filter);


        SetToolbarShadow();
        FragmentManager fragmentManager = getSupportFragmentManager();


        if (filter == AppConstant.AppDrawer.Home.id) {
            fragment = new ItemListFragment();
        }else if (filter == AppConstant.AppDrawer.Series.id) {
            fragment = new ItemListFragment();
        }else if (filter == AppConstant.AppDrawer.Program.id) {
            fragment = new ItemListFragment();
        }else if (filter == AppConstant.AppDrawer.TopShows.id) {
            fragment = new ItemListFragment();
        }else if (filter == AppConstant.AppDrawer.Favorites.id) {
            fragment = new ItemListFragment();
        }else if (filter == AppConstant.AppDrawer.NewEpisode.id) {
            fragment = new ItemListEpisodeFragment();
        }else if (filter == AppConstant.AppDrawer.Settings.id) {
            fragment=new SettingFragment();
        }

        args.putInt(AppAction.EXTRA.MENUID, filter);
        fragment.setArguments(args);

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (fragment != null) {
            if(fragment.isAdded()) {
                transaction.show(fragment);
            }else {
                // Insert the fragment by replacing any existing fragment
                transaction.replace(R.id.frame_container, fragment).commit();
            }

        }

    }

    public void SetupToolbarShadow(){
        //Shadow View
        shadowView=findViewById(R.id.shadow_main_activity);
        toolbarContainer=findViewById(R.id.toolbar);
        // Solve Android bug in API < 21 by app custom shadow
        SetToolbarShadow();
    }
    public void SetToolbarShadow()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            shadowView.setVisibility(View.GONE);
            final float scale = getResources().getDisplayMetrics().density;
            toolbarContainer.setElevation(5f*scale);
        }else{
            shadowView.setVisibility(View.VISIBLE);
        }
    }


    public void RemoveToolBarShadow() {
        if(shadowView!=null)
            shadowView.setVisibility(View.GONE);
        if(toolbarContainer!=null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                toolbarContainer.setElevation(0);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }


}
