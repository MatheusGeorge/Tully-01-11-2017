package com.example.edu.menutb.view.notification;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.edu.menutb.R;

/**
 * Created by Edu on 18/07/2017.
 */

public class NotificationAcitivity extends Fragment {
    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.notifications, container, false);
        getActivity().setTitle("");
        return myView;
    }
}
