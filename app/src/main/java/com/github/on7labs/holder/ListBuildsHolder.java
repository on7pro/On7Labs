package com.github.on7labs.holder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.on7labs.R;
import com.github.on7labs.activity.ListBuildDetail;

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
                imageViewBanner.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_no_thumbnail));
            }else {
                textViewLoadingImage.setVisibility(View.GONE);
                imageViewBanner.setImageBitmap(bmp);
            }
        }
    }
}
