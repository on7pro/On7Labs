package com.github.on7labs.holder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.github.on7labs.R;
import com.github.on7labs.activity.ActivityAddBuild;
import com.github.on7labs.activity.ListBuildDetail;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by androidlover5842 on 21.3.2018.
 */

public class ListBuildsHolder extends RecyclerView.ViewHolder implements View.OnClickListener ,View.OnLongClickListener,AdapterView.OnItemLongClickListener{
    private String ref;
    private Context context;
    private String name;
    private String date;
    private String developerName;
    private String developerEmail;
    private String bannerUrl;
    private String description;
    private String romUrl;
    private int version=0;
    private String credits;
    private String source;
    private String status;
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
    public void bind(String name,
                     String date,
                     String developerName,
                     String developerEmail,
                     String bannerUrl,
                     String description,
                     String romUrl,
                     String status,
                     int version,
                     String credits,
                     String source){
        this.name=name;
        this.date=date;
        this.developerEmail=developerEmail;
        this.developerName=developerName;
        this.bannerUrl=bannerUrl;
        this.description=description;
        this.romUrl=romUrl;
        this.version=version;
        this.status=status;
        this.credits=credits;
        this.source=source;
        DetailActivityIntent=new Intent(context,ListBuildDetail.class);
        storageReference =  FirebaseStorage.getInstance().getReference().child(bannerUrl);
        if (FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(developerEmail)) {
            cardView.setOnLongClickListener(this);
        }
        DetailActivityIntent.putExtra("name",name);
        DetailActivityIntent.putExtra("date",date);
        DetailActivityIntent.putExtra("developerName",developerName);
        DetailActivityIntent.putExtra("developerEmail",developerEmail);
        DetailActivityIntent.putExtra("bannerUrl",bannerUrl);
        DetailActivityIntent.putExtra("description",description);
        DetailActivityIntent.putExtra("romUrl",romUrl);
        DetailActivityIntent.putExtra("version",version);
        DetailActivityIntent.putExtra("credits",credits);
        DetailActivityIntent.putExtra("status",status);
        DetailActivityIntent.putExtra("source",source);

        textViewDate.setText("Date"+colon+date);
        textViewName.setText("Rom Name"+colon+name);
        textViewDeveloperName.setText("Developer name"+colon+developerName);
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
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        if(id==cardView.getId())
        {
            context.startActivity(DetailActivityIntent);
        }
    }

    @Override
    public boolean onLongClick(View view) {
        int id=view.getId();
        if (id==cardView.getId())
        {
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter(context, android.R.layout.select_dialog_item);
            arrayAdapter.add("Edit");
            arrayAdapter.add("Delete");
            builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, int which) {
                    String strName = arrayAdapter.getItem(which);
                    if (strName.equals("Edit"))
                    {
                        context.startActivity(
                                new Intent(context, ActivityAddBuild.class)
                                        .putExtra("fromHolder",true)
                                        .putExtra("name",name)
                                        .putExtra("bannerUrl",bannerUrl)
                                        .putExtra("description",description)
                                        .putExtra("romUrl",romUrl)
                                        .putExtra("version",version)
                                        .putExtra("credits",credits)
                                        .putExtra("status",status)
                                        .putExtra("source",source)
                        );
                    }else if (strName.equals("Delete")) {
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(context);
                        builderInner.setMessage(name);
                        builderInner.setTitle("Are you sure you want to delete this project ?");
                        builderInner.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builderInner.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                           dialog.dismiss();
                            }
                        });
                        builderInner.show();
                    }
                }
            });
            builderSingle.show();

        }
        return false;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        return false;
    }
}
