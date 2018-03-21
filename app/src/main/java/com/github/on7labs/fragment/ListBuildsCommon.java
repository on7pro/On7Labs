package com.github.on7labs.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.on7labs.R;

/**
 * Created by androidlover5842 on 21.3.2018.
 */
@SuppressLint("ValidFragment")
public class ListBuildsCommon extends Fragment {

    public ListBuildsCommon(){}

    public ListBuildsCommon(String ref){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_list_builds,container,false);
        return v;
    }
}
