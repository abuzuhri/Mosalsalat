package ae.tomoohrmdn.ramadan.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ae.tomoohrmdn.ramadan.R;


/**
 * Created by tareq on 06/11/2015.
 */
public class FragmentLoginPager extends Fragment {

    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    public static final FragmentLoginPager newInstance(String message)
    {

        FragmentLoginPager f = new FragmentLoginPager();

        Bundle bdl = new Bundle(1);

        bdl.putString(EXTRA_MESSAGE, message);

        f.setArguments(bdl);

        return f;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        String message = getArguments().getString(EXTRA_MESSAGE);

        View v = inflater.inflate(R.layout.fragment_login_pager, container, false);

        ImageView img=(ImageView) v.findViewById(R.id.imageView);
        img.setImageResource(R.drawable.fab_bg_normal);


        return v;

    }

}