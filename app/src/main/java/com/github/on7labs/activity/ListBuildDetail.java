package com.github.on7labs.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.on7labs.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import io.square1.richtextlib.ui.RichContentView;
import io.square1.richtextlib.v2.content.RichTextDocumentElement;

/**
 * Created by androidlover5842 on 21.3.2018.
 */

public class ListBuildDetail extends AppCompatActivity implements View.OnClickListener {
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
    private String colon = " : ";
    private Button btRomUrl;
    private StorageReference storageReference;
    private RichContentView RichBuild;
    private String paragraph;
    private RichTextDocumentElement.TextBuilder element;
    private ImageView imageViewSS1, imageViewSS2, imageViewSS3, imageViewSS4, imageViewSS5;
    private String img1 = null, img2 = null, img3 = null, img4 = null, img5 = null;


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
        img1 = bundle.getString("img1");
        img2 = bundle.getString("img2");
        img3 = bundle.getString("img3");
        img4 = bundle.getString("img4");
        img5 = bundle.getString("img5");

        imageViewSS1 = findViewById(R.id.img_ss_1);
        imageViewSS2 = findViewById(R.id.img_ss_2);
        imageViewSS3 = findViewById(R.id.img_ss_3);
        imageViewSS4 = findViewById(R.id.img_ss_4);
        imageViewSS5 = findViewById(R.id.img_ss_5);

        TextView textViewNoSSFound, textViewProjectTitle, textViewProjectDesc;
        textViewNoSSFound = findViewById(R.id.tv_no_ss_found);
        textViewProjectTitle = findViewById(R.id.tv_project_title);
        textViewProjectDesc = findViewById(R.id.tv_project_desc);

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

        textViewProjectTitle.setText(name);
        textViewProjectDesc.setText(description);
        element = new RichTextDocumentElement.TextBuilder("");
        element.bold()
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

        if (source != null) {
            element.append("source" + colon + source).bold().newLine();
        }

        if (credit != null) {
            element
                    .append("credits" + colon)
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
                if (uri.toString() == null) {
                    textViewLoadingImage.setVisibility(View.GONE);
                    imageViewBanner.setImageDrawable(ContextCompat.getDrawable(ListBuildDetail.this, R.drawable.ic_no_thumbnail));
                } else {
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
        if (img1 != null) {
            textViewNoSSFound.setVisibility(View.GONE);
            imageViewSS1.setVisibility(View.VISIBLE);
            LoadScreenShots(imageViewSS1, img1);
        }
        if (img2 != null) {
            imageViewSS2.setVisibility(View.VISIBLE);
            LoadScreenShots(imageViewSS2, img2);
        }
        if (img3 != null) {
            imageViewSS3.setVisibility(View.VISIBLE);
            LoadScreenShots(imageViewSS3, img3);
        }
        if (img4 != null) {
            imageViewSS4.setVisibility(View.VISIBLE);
            LoadScreenShots(imageViewSS4, img4);
        }
        if (img5 != null) {
            imageViewSS5.setVisibility(View.VISIBLE);
            LoadScreenShots(imageViewSS5, img5);
        }
        imageViewBanner.setOnClickListener(this);
        imageViewSS1.setOnClickListener(this);
        imageViewSS2.setOnClickListener(this);
        imageViewSS3.setOnClickListener(this);
        imageViewSS4.setOnClickListener(this);
        imageViewSS5.setOnClickListener(this);
    }

    private void LoadScreenShots(final ImageView imageView, String ssUrl) {
        StorageReference firebaseStorage=FirebaseStorage.getInstance().getReference().child(ssUrl);

        Glide.with(getApplicationContext())
                .load(firebaseStorage)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        imageView.setImageDrawable(ContextCompat.getDrawable(ListBuildDetail.this, R.drawable.ic_no_thumbnail));
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imageView);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == btRomUrl.getId()) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(romUrl));
            startActivity(browserIntent);
        } else if (id == imageViewBanner.getId()) {
            if (bannerUrl != null) {
                startActivity(new Intent(ListBuildDetail.this, ViewImage.class).putExtra("url", bannerUrl));
            }
        } else if (id == imageViewSS1.getId()) {
            if (img1 != null) {
                startActivity(new Intent(ListBuildDetail.this, ViewImage.class).putExtra("url", img1));
            }
        } else if (id == imageViewSS2.getId()) {
            if (img2 != null)
                startActivity(new Intent(ListBuildDetail.this, ViewImage.class).putExtra("url", img2));
        } else if (id == imageViewSS3.getId()) {
            if (img3 != null)
                startActivity(new Intent(ListBuildDetail.this, ViewImage.class).putExtra("url", img3));
        } else if (id == imageViewSS4.getId()) {
            if (img4 != null)
                startActivity(new Intent(ListBuildDetail.this, ViewImage.class).putExtra("url", img4));
        } else if (id == imageViewSS5.getId()) {
            if (img5 != null)
                startActivity(new Intent(ListBuildDetail.this, ViewImage.class).putExtra("url", img5));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
