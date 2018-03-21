package com.github.on7labs.holder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.on7labs.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

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
    private TextView textViewName,textViewDate,textViewDeveloperName,textViewLoadingImage;
    private ImageView imageViewBanner;
    private String colon=" : ";
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
    }
    public void bind(String name,String date,String developerName,String developerEmail,String bannerUrl,String description,String romUrl){
        this.name=name;
        this.date=date;
        this.developerEmail=developerEmail;
        this.developerName=developerName;
        this.bannerUrl=bannerUrl;
        this.description=description;
        this.romUrl=romUrl;
        textViewDate.setText("Date"+colon+date);
        textViewName.setText("Rom Name"+colon+name);
        textViewDeveloperName.setText("Developer name"+colon+developerName);
        new ImageTask().execute();
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
                textViewLoadingImage.setText("No Thumbnail");
            }else {
                textViewLoadingImage.setVisibility(View.GONE);
                imageViewBanner.setImageBitmap(bmp);
            }
        }
    }
}
