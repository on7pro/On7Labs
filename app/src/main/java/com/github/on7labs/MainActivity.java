package com.github.on7labs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.on7labs.fragment.FragmentHome;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private View NavView;
    private TextView textViewName,textViewEmail;
    private FirebaseAuth firebaseAuth;
    private String email,name;
    private Uri profileUrl;
    private ImageView imageViewProfile;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        firebaseAuth=FirebaseAuth.getInstance();
        name=firebaseAuth.getCurrentUser().getDisplayName();
        email=firebaseAuth.getCurrentUser().getEmail();
        profileUrl=firebaseAuth.getCurrentUser().getPhotoUrl();
        NavigationView navigationView =  findViewById(R.id.nav_view);
        NavView= navigationView.inflateHeaderView(R.layout.nav_header_main);
        textViewName=NavView.findViewById(R.id.tv_nav_name);
        textViewEmail=NavView.findViewById(R.id.tv_nav_email);
        imageViewProfile=NavView.findViewById(R.id.nav_imageView);

        textViewName.setText(name);
        textViewEmail.setText(email);
        new ProfileTask().execute();
        navigationView.setNavigationItemSelectedListener(this);
        updateFrame(new FragmentHome(),"Home");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            updateFrame(new FragmentHome(),"Home");
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void updateFrame(Fragment fragment,String title){
        toolbar.setTitle(title);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main,fragment);
        ft.commit();
    }

    private class ProfileTask extends AsyncTask<Void,Void,Void>{
        private Bitmap bmp;
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL  url = new URL(profileUrl.toString());
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
            imageViewProfile.setImageBitmap(bmp);
        }
    }
}
