package com.github.on7labs.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.on7labs.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import io.square1.richtextlib.ui.RichContentView;
import io.square1.richtextlib.v2.RichTextV2;
import io.square1.richtextlib.v2.content.RichTextDocumentElement;

/**
 * Created by androidlover5842 on 21.3.2018.
 */

public class ListBuildDetail extends AppCompatActivity implements View.OnClickListener{
    private String name;
    private String date;
    private String developerName;
    private String developerEmail;
    private String bannerUrl;
    private String description;
    private String romUrl;
    private int version;
    private String credit;
    private String source;
    private String status;
   // private TextView textViewName,textViewDate,textViewDeveloperName,textViewLoadingImage,textViewDescription,textViewDeveloperEmail;
    private TextView textViewLoadingImage;
    private ImageView imageViewBanner;
    private Bundle bundle;
    private String colon=" : ";
    private Button btRomUrl;
    private StorageReference storageReference;
    private RichContentView RichBuild;
    private String paragraph;
    private RichTextDocumentElement.TextBuilder element;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_detail);
        bundle = getIntent().getExtras();
        name = bundle.getString("name");
        date = bundle.getString("date");
        developerName = bundle.getString("developerName");
        developerEmail = bundle.getString("developerEmail");
        bannerUrl = bundle.getString("bannerUrl");
        description = bundle.getString("description");
        romUrl = bundle.getString("romUrl");
        credit = bundle.getString("credits");
        version = bundle.getInt("version");
        status = bundle.getString("status");
        source = bundle.getString("source");

        /*textViewName=findViewById(R.id.tv_name);
        textViewDeveloperName=findViewById(R.id.tv_dev_name);
        textViewDate=findViewById(R.id.tv_date);
        textViewDescription=findViewById(R.id.tv_description);
        textViewDeveloperEmail=findViewById(R.id.tv_dev_email);*/
        textViewLoadingImage = findViewById(R.id.tv_loading);
        RichBuild = findViewById(R.id.rich_content);

        imageViewBanner = findViewById(R.id.banner);
        btRomUrl = findViewById(R.id.bt_rom_download);
        storageReference = FirebaseStorage.getInstance().getReference().child(bannerUrl);
        paragraph = "\n";

        /*textViewDate.setText("Date"+colon+date);
        textViewName.setText("Rom Name"+colon+name);
        textViewDeveloperName.setText("Developer name"+colon+developerName);
        textViewDeveloperEmail.setText("developer email"+colon+developerEmail);
        textViewDescription.setText("description"+colon+"\n"+description);*/
        element = new RichTextDocumentElement.TextBuilder(name);
        element.bold()
                .color(Color.BLUE)
                .underline(true)
                .sizeChange(1.5f)
                .center()
                .newLine()
                .append("Notes" + colon)
                .bold()
                .newLine()
                .append(description)
                .newLine()
                .newLine()
                .append("status" + colon + status)
                .bold()
                .newLine()
                .append("version" + colon + version)
                .bold()
                .newLine()
                .append("Developer name" + colon + developerName)
                .bold()
                .newLine()
                .append("Developer email" + colon + developerEmail)
                .underline(true)
                .sizeChange(0.9f)
                .bold()
                .newLine()
                .append("Publish date" + colon + date)
                .bold()
                .newLine();

        if (source != null)
        {
            element.append("source" + colon + source).bold().newLine();
    }

    if (credit!=null)
    {
        element
                .append("credits"+colon)
                .bold()
                .newLine()
                .append(credit)
                .bold();
    }

        RichBuild.setText(element.build());

        btRomUrl.setOnClickListener(this);
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (uri.toString()==null)
                {
                    textViewLoadingImage.setVisibility(View.GONE);
                    imageViewBanner.setImageDrawable(ContextCompat.getDrawable(ListBuildDetail.this, R.drawable.ic_no_thumbnail));
                }
                else
                {
                    textViewLoadingImage.setVisibility(View.GONE);
                    Glide.with(getApplicationContext())
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
        int id=view.getId();
        if (id==btRomUrl.getId())
        {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(romUrl));
            startActivity(browserIntent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
