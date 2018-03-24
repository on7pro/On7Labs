package com.github.on7labs;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.on7labs.activity.ActivityAddBuild;
import com.github.on7labs.activity.LoginActivity;
import com.github.on7labs.fragment.CreditsFragment;
import com.github.on7labs.fragment.FragmentHome;
import com.github.on7labs.util.Config;
import com.github.updater.Updater;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private View NavView;
    private TextView textViewName, textViewEmail;
    private FirebaseAuth firebaseAuth;
    private String email, name;
    private Uri profileUrl;
    private ImageView imageViewProfile;
    private Toolbar toolbar;
    public static FloatingActionButton floatingActionButtonAddThread;
    private FirebaseDatabase firebaseDatabase;

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

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        name = firebaseAuth.getCurrentUser().getDisplayName();
        email = firebaseAuth.getCurrentUser().getEmail();
        profileUrl = firebaseAuth.getCurrentUser().getPhotoUrl();
        NavigationView navigationView = findViewById(R.id.nav_view);
        NavView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        textViewName = NavView.findViewById(R.id.tv_nav_name);
        textViewEmail = NavView.findViewById(R.id.tv_nav_email);
        imageViewProfile = NavView.findViewById(R.id.nav_imageView);
        floatingActionButtonAddThread = findViewById(R.id.fab_add_thread);

        textViewName.setText(name);
        textViewEmail.setText(email);
        new ProfileTask().execute();
        navigationView.setNavigationItemSelectedListener(this);
        updateFrame(new FragmentHome(), getString(R.string.home));
        Query query = firebaseDatabase.getReference("Developers").orderByChild("email").equalTo(email);
        query.keepSynced(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    floatingActionButtonAddThread.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        floatingActionButtonAddThread.setOnClickListener(this);
        checkPermission();
        requestPermission();
        new Updater(this, Config.APP_VERSION,Config.APP_UPDATE_JSON_URL,false);
    }

    private boolean checkPermission() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int result = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (result == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    private void requestPermission() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toasty.warning(MainActivity.this, "Please allow permission in App SettingsActivity.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            switch (requestCode) {
                case 1:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Log.e("value", "Permission Granted .");
                    } else {
                        Log.e("value", "Permission Denied .");
                        finish();
                    }
                    break;
            }
        }
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
            updateFrame(new FragmentHome(), getString(R.string.home));
        }
        else if (id==R.id.nav_credits)
        {
            updateFrame(new CreditsFragment(),"Contributors");
        }
        else if (id == R.id.nav_logout) {
            firebaseAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void updateFrame(Fragment fragment, String title) {
        toolbar.setTitle(title);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, fragment);
        ft.commit();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == floatingActionButtonAddThread.getId()) {
            startActivity(new Intent(MainActivity.this, ActivityAddBuild.class).putExtra("formHolder", false));
        }
    }

    private class ProfileTask extends AsyncTask<Void, Void, Void> {
        private Bitmap bmp;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL(profileUrl.toString());
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
