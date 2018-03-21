package com.github.on7labs.holder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.github.on7labs.R;
import com.github.on7labs.activity.ListBuildDetail;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by androidlover5842 on 21.3.2018.
 */

public class ListBuildsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
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
    private TextView textViewName,textViewDate,textViewDeveloperName,textViewLoadingImage;
    private ImageView imageViewBanner;
    private String colon=" : ";
    private CardView cardView;
    private Intent DetailActivityIntent;
    private StorageReference storageReference;
    public ListBuildsHolder(View itemView,String ref,Context context) {
        super(itemView);
        this.ref=ref;
        this.context=context;
        this.v=itemView;
        textViewName=itemView.findViewById(R.id.tv_name);
        textViewDeveloperName=itemView.findViewById(R.id.tv_dev_name);
        textViewDate=itemView.findViewById(R.id.tv_date);
        imageViewBanner=itemView.findViewById(R.id.img_banner);
        textViewLoadingImage=itemView.findViewById(R.id.tv_loading);
        cardView=itemView.findViewById(R.id.cv_build);

        cardView.setOnClickListener(this);
    }
    public void bind(String name,String date,String developerName,String developerEmail,String bannerUrl,String description,String romUrl){
        this.name=name;
        this.date=date;
        this.developerEmail=developerEmail;
        this.developerName=developerName;
        this.bannerUrl=bannerUrl;
        this.description=description;
        this.romUrl=romUrl;
        DetailActivityIntent=new Intent(context,ListBuildDetail.class);
        storageReference =  FirebaseStorage.getInstance().getReference().child(bannerUrl);

        DetailActivityIntent.putExtra("name",name);
        DetailActivityIntent.putExtra("date",date);
        DetailActivityIntent.putExtra("developerName",developerName);
        DetailActivityIntent.putExtra("developerEmail",developerEmail);
        DetailActivityIntent.putExtra("bannerUrl",bannerUrl);
        DetailActivityIntent.putExtra("description",description);
        DetailActivityIntent.putExtra("romUrl",romUrl);

        textViewDate.setText("Date"+colon+date);
        textViewName.setText("Rom Name"+colon+name);
        textViewDeveloperName.setText("Developer name"+colon+developerName);
        new ImageTask().execute();
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        if(id==cardView.getId())
        {
            context.startActivity(DetailActivityIntent);
        }
    }

    private class ImageTask extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... voids) {
            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    if (uri.toString()==null)
                    {
                        textViewLoadingImage.setVisibility(View.GONE);
                        imageViewBanner.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_no_thumbnail));
                    }
                    else
                    {
                        textViewLoadingImage.setVisibility(View.GONE);
                        Glide.with(context)
                                .load(uri.toString())
                                .into(imageViewBanner );
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

            return null;
        }
    }
}
