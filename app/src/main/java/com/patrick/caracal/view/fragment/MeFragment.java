package com.patrick.caracal.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by Patrick on 16/6/13.
 */

public class MeFragment extends Fragment {

    public static MeFragment newInstance() {

        Bundle args = new Bundle();

        MeFragment fragment = new MeFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
