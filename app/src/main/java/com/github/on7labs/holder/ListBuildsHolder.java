package com.github.on7labs.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.on7labs.R;

/**
 * Created by androidlover5842 on 21.3.2018.
 */

public class ListBuildsHolder extends RecyclerView.ViewHolder {
    private String ref;
    private Context context;
    private String name;
    private String date;
    private String developerName;
    private String developerEmail;
    private String bannerUrl;
    private String description;
    private String romUrl;
    private View v;
    private TextView textViewName,textViewDate,textViewDeveloperName;

    public ListBuildsHolder(View itemView,String ref,Context context) {
        super(itemView);
        this.ref=ref;
        this.context=context;
        this.v=itemView;
        textViewName=itemView.findViewById(R.id.tv_name);
        textViewDeveloperName=itemView.findViewById(R.id.tv_dev_name);
        textViewDate=itemView.findViewById(R.id.tv_date);
    }
    public void bind(String name,String date,String developerName,String developerEmail,String bannerUrl,String description,String romUrl){
        this.name=name;
        this.date=date;
        this.developerEmail=developerEmail;
        this.developerName=developerName;
        this.bannerUrl=bannerUrl;
        this.description=description;
        this.romUrl=romUrl;
        textViewDate.setText(date);
        textViewName.setText(name);
        textViewDeveloperName.setText(developerName);
    }
}
