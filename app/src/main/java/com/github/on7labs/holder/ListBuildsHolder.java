package com.github.on7labs.holder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
import com.github.on7labs.R;
import com.github.on7labs.activity.ActivityAddBuild;
import com.github.on7labs.activity.ListBuildDetail;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import es.dmoral.toasty.Toasty;

/**
 * Created by androidlover5842 on 21.3.2018.
 */

public class ListBuildsHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, AdapterView.OnItemLongClickListener {
    private String ref;
    private Context context;
    private String name;
    private String date;
    private String developerName;
    private String developerEmail;
    private String bannerUrl;
    private String description;
    private String romUrl;
    private int version = 0;
    private String credits;
    private String source;
    private String status;
    private String key;
    private TextView textViewName, textViewDate, textViewDeveloperName;
    private ImageView imageViewBanner;
    private String colon = " : ";
    private CardView cardView;
    private Intent DetailActivityIntent;
    private Intent AddBuildIntent;
    private StorageReference storageReference;
    private FloatingActionButton fabRomStatus;

    public ListBuildsHolder(View itemView, String ref, Context context) {
        super(itemView);
        this.ref = ref;
        this.context = context;
        textViewName = itemView.findViewById(R.id.tv_name);
        textViewDeveloperName = itemView.findViewById(R.id.tv_dev_name);
        textViewDate = itemView.findViewById(R.id.tv_date);
        imageViewBanner = itemView.findViewById(R.id.img_banner);
        cardView = itemView.findViewById(R.id.cv_build);
        fabRomStatus=itemView.findViewById(R.id.fab_status);
        cardView.setOnClickListener(this);
    }

    public void BindScreenShots(String screenShot1){
        DetailActivityIntent.putExtra("img1",screenShot1);
        AddBuildIntent.putExtra("img1",screenShot1);
    }

    public void BindScreenShots(String screenShot1,String screenShot2){
        DetailActivityIntent.putExtra("img1",screenShot1);
        DetailActivityIntent.putExtra("img2",screenShot2);
        AddBuildIntent.putExtra("img1",screenShot1);
        AddBuildIntent.putExtra("img2",screenShot2);
    }

    public void BindScreenShots(String screenShot1,String screenShot2,String screenShot3){
        DetailActivityIntent.putExtra("img1",screenShot1);
        DetailActivityIntent.putExtra("img2",screenShot2);
        DetailActivityIntent.putExtra("img3",screenShot3);
        AddBuildIntent.putExtra("img1",screenShot1);
        AddBuildIntent.putExtra("img2",screenShot2);
        AddBuildIntent.putExtra("img3",screenShot3);

    }

    public void BindScreenShots(String screenShot1,String screenShot2,String screenShot3,String screenShot4){
        DetailActivityIntent.putExtra("img1",screenShot1);
        DetailActivityIntent.putExtra("img2",screenShot2);
        DetailActivityIntent.putExtra("img3",screenShot3);
        DetailActivityIntent.putExtra("img4",screenShot4);
        AddBuildIntent.putExtra("img1",screenShot1);
        AddBuildIntent.putExtra("img2",screenShot2);
        AddBuildIntent.putExtra("img3",screenShot3);
        AddBuildIntent.putExtra("img4",screenShot4);
    }

    public void BindScreenShots(String screenShot1,String screenShot2,String screenShot3,String screenShot4,String screenShot5){
        DetailActivityIntent.putExtra("img1",screenShot1);
        DetailActivityIntent.putExtra("img2",screenShot2);
        DetailActivityIntent.putExtra("img3",screenShot3);
        DetailActivityIntent.putExtra("img4",screenShot4);
        DetailActivityIntent.putExtra("img5",screenShot5);
        AddBuildIntent.putExtra("img1",screenShot1);
        AddBuildIntent.putExtra("img2",screenShot2);
        AddBuildIntent.putExtra("img3",screenShot3);
        AddBuildIntent.putExtra("img4",screenShot4);
        AddBuildIntent.putExtra("img5",screenShot5);
    }

    @SuppressLint("ResourceAsColor")
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
                     String source,
                     String key) {
        this.name = name;
        this.date = date;
        this.developerEmail = developerEmail;
        this.developerName = developerName;
        this.bannerUrl = bannerUrl;
        this.description = description;
        this.romUrl = romUrl;
        this.version = version;
        this.status = status;
        this.credits = credits;
        this.source = source;
        this.key = key;
        if (status.equals("Stable"))
            fabRomStatus.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
        if (status.equals("Beta"))
            fabRomStatus.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
        if (status.equals("Alpha"))
            fabRomStatus.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
        if (status.equals("Pre release"))
            fabRomStatus.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
        if (status.equals("Intial release"))
            fabRomStatus.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));

        DetailActivityIntent = new Intent(context, ListBuildDetail.class);
        AddBuildIntent=new Intent(context, ActivityAddBuild.class);
        storageReference = FirebaseStorage.getInstance().getReference().child(bannerUrl);
        cardView.setOnLongClickListener(this);
        DetailActivityIntent.putExtra("name", name);
        DetailActivityIntent.putExtra("date", date);
        DetailActivityIntent.putExtra("developerName", developerName);
        DetailActivityIntent.putExtra("developerEmail", developerEmail);
        DetailActivityIntent.putExtra("bannerUrl", bannerUrl);
        DetailActivityIntent.putExtra("description", description);
        DetailActivityIntent.putExtra("romUrl", romUrl);
        DetailActivityIntent.putExtra("version", version);
        DetailActivityIntent.putExtra("credits", credits);
        DetailActivityIntent.putExtra("status", status);
        DetailActivityIntent.putExtra("source", source);
        AddBuildIntent.putExtra("fromHolder", true);
        AddBuildIntent.putExtra("name", name);
        AddBuildIntent.putExtra("bannerUrl", bannerUrl);
        AddBuildIntent.putExtra("description", description);
        AddBuildIntent.putExtra("romUrl", romUrl);
        AddBuildIntent.putExtra("version", version);
        AddBuildIntent.putExtra("credits", credits);
        AddBuildIntent.putExtra("status", status);
        AddBuildIntent.putExtra("source", source);
        AddBuildIntent.putExtra("key", key);

        textViewDate.setText("Date" + colon + date);
        textViewName.setText("Rom Name" + colon + name);
        textViewDeveloperName.setText("Developer name" + colon + developerName);
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (uri.toString() == null) {
                    imageViewBanner.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_no_thumbnail));
                } else {
                    Glide.with(context.getApplicationContext())
                            .load(uri.toString())
                            .into(imageViewBanner);
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
        int id = view.getId();
        if (id == cardView.getId()) {
            context.startActivity(DetailActivityIntent);
        }
    }

    @Override
    public boolean onLongClick(View view) {
        final int id = view.getId();
        if (id == cardView.getId()) {
            if (FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(developerEmail)) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter(context, android.R.layout.select_dialog_item);
                arrayAdapter.add("Edit");
                arrayAdapter.add("Delete");
                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        if (strName.equals("Edit")) {
                            context.startActivity(AddBuildIntent);
                        } else if (strName.equals("Delete")) {
                            AlertDialog.Builder builderInner = new AlertDialog.Builder(context);
                            builderInner.setMessage(name);
                            builderInner.setTitle("Are you sure you want to delete this project ?");
                            builderInner.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    FirebaseDatabase.getInstance().getReference(ref).child(key).removeValue();
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
            }else {
                Toasty.error(context, "You can't edit this post!").show();
            }

        }
        return false;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        return false;
    }
}
