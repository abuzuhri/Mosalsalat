package ae.tomoohrmdn.ramadan.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import ae.tomoohrmdn.ramadan.utils.AppAction;

/**
 * Created by tareq on 06/11/2015.
 */
public class BaseFragment extends Fragment
{
    protected Long ID;

    protected int px(float dips)
    {
        float DP = getResources().getDisplayMetrics().density;
        return Math.round(dips * DP);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set Item ID
        if(getArguments()!=null) {
            ID = getArguments().getLong(AppAction.EXTRA.IDEXTRA, 0);
        }
    }

    public void setSubTitle(String txt){
        ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle(txt);
    }
    public void setTitle(String txt) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(txt);
    }
}
