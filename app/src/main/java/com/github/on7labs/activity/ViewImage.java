package com.github.on7labs.activity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.on7labs.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

/**
 * Created by androidlover5842 on 23.3.2018.
 */

public class ViewImage extends Activity {
    private String url;
    private Bundle bundle;
    private PhotoView photoView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        bundle=getIntent().getExtras();
        photoView=findViewById(R.id.photo_view);
        progressBar=findViewById(R.id.progressBar);
        url=bundle.getString("url");

        Glide.with(getApplicationContext())
                .load(FirebaseStorage.getInstance().getReference().child(url))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        photoView.setImageDrawable(getResources().getDrawable(R.drawable.ic_no_thumbnail,getTheme()));
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);

                        return false;
                    }
                })
                .into(photoView);


    }
}
