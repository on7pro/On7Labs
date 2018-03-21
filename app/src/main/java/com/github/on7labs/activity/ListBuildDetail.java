package com.github.on7labs.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.on7labs.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

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
    private TextView textViewName,textViewDate,textViewDeveloperName,textViewLoadingImage,textViewDescription,textViewDeveloperEmail;
    private ImageView imageViewBanner;
    private Bundle bundle;
    private String colon=" : ";
    private Button btRomUrl;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_detail);
        bundle = getIntent().getExtras();
        name= bundle.getString("name");
        date= bundle.getString("date");
        developerName= bundle.getString("developerName");
        developerEmail= bundle.getString("developerEmail");
        bannerUrl= bundle.getString("bannerUrl");
        description= bundle.getString("description");
        romUrl= bundle.getString("romUrl");

        textViewName=findViewById(R.id.tv_name);
        textViewDeveloperName=findViewById(R.id.tv_dev_name);
        textViewDate=findViewById(R.id.tv_date);
        imageViewBanner=findViewById(R.id.banner);
        textViewLoadingImage=findViewById(R.id.tv_loading);
        textViewDescription=findViewById(R.id.tv_description);
        textViewDeveloperEmail=findViewById(R.id.tv_dev_email);
        btRomUrl=findViewById(R.id.bt_rom_download);

        textViewDate.setText("Date"+colon+date);
        textViewName.setText("Rom Name"+colon+name);
        textViewDeveloperName.setText("Developer name"+colon+developerName);
        textViewDeveloperEmail.setText("developer email"+colon+developerEmail);
        textViewDescription.setText("description"+colon+"\n"+description);
        btRomUrl.setOnClickListener(this);
        new ImageTask().execute();

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

    private class ImageTask extends AsyncTask<Void,Void,Void> {
        private Bitmap bmp;
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL(bannerUrl);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (bmp==null)
            {
                textViewLoadingImage.setVisibility(View.GONE);
                imageViewBanner.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_no_thumbnail));
            }else {
                textViewLoadingImage.setVisibility(View.GONE);
                imageViewBanner.setImageBitmap(bmp);
            }
        }
    }
}
