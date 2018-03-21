package com.github.on7labs.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.on7labs.R;
import com.github.on7labs.holder.ListBuildsHolder;
import com.github.on7labs.model.ListBuildModel;
import com.github.on7labs.util.FirebaseProgressBar;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by androidlover5842 on 21.3.2018.
 */
@SuppressLint("ValidFragment")
public class ListBuildsCommon extends Fragment {
    private String ref;
    private FirebaseRecyclerAdapter adapter;
    private RecyclerView lvBuilds;
    private Query query;
    private FirebaseRecyclerOptions options;
    private boolean bottom;
    private LinearLayoutManager layoutManager;
    private View view;
    public ListBuildsCommon(){}

    public ListBuildsCommon(String ref){
        this.ref=ref;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_list_builds,container,false);
        this.view=v;
        lvBuilds = v.findViewById(R.id.rv_list_build);
        query = FirebaseDatabase.getInstance()
                .getReference(ref);
        options = new FirebaseRecyclerOptions.Builder()
                .setQuery(query,ListBuildModel.class)
                .build();
        query.keepSynced(true);

        adapter = new FirebaseRecyclerAdapter<ListBuildModel, ListBuildsHolder>(options) {
            @Override
            public ListBuildsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view1 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_build, parent, false);
                return new ListBuildsHolder(view1,ref,getContext());
            }

            @Override
            protected void onBindViewHolder(@NonNull ListBuildsHolder holder,
                                            int position,
                                            @NonNull ListBuildModel model) {
                holder.bind(
                        model.getName(),
                        model.getDate(),
                        model.getDeveloperName(),
                        model.getDeveloperEmail(),
                        model.getBannerUrl(),
                        model.getDescription(),
                        model.getRomUrl()
                );
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                ProgressBar();
            }
        };

        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ProgressBar();
        if (bottom)
        {
            layoutManager.setStackFromEnd(true);
        }
        lvBuilds.setLayoutManager(layoutManager);
        lvBuilds.setAdapter(adapter);
        if (savedInstanceState==null) {
            return v;
        }else {
            return null;
        }
    }
    private void ProgressBar(){
        ProgressBar progressBar= view.findViewById(R.id.pb_builds);
        TextView textView= view.findViewById(R.id.tv_no_build);
        new FirebaseProgressBar(progressBar, textView, adapter, ref);
    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
