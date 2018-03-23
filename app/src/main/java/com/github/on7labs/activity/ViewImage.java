package com.github.on7labs.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.on7labs.R;

/**
 * Created by androidlover5842 on 23.3.2018.
 */

public class ViewImage extends Activity {
    private String url;
    private Bundle bundle;
    private PhotoView photoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        bundle=getIntent().getExtras();
        photoView=findViewById(R.id.photo_view);
        url=bundle.getString("url");
        Glide.with(getApplicationContext()).load(Uri.parse(url)).into(photoView);

    }
}
