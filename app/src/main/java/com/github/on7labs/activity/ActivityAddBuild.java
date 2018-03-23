package com.github.on7labs.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dd.processbutton.iml.ActionProcessButton;
import com.github.on7labs.R;
import com.github.on7labs.model.ListBuildModel;
import com.github.on7labs.util.DateUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.isapanah.awesomespinner.AwesomeSpinner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import yogesh.firzen.filelister.FileListerDialog;
import yogesh.firzen.filelister.OnFileSelectedListener;

/**
 * Created by androidlover5842 on 22.3.2018.
 */

public class ActivityAddBuild extends AppCompatActivity implements View.OnClickListener{
    private ActionProcessButton btSubmit;
    private ImageView imageViewBanner;
    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FileListerDialog fileListerDialog;
    private int version=0;
    private String email,name,imgpath="noimg",date,romName,aboutRom,stabilityStatus,url,sourceCode,Credits;
    private EditText editTextName,editTextAboutRom,editTextVersion,editTextUrl,editTextSourceCode,editTextCredits;
    private UploadTask uploadTask;
    private ListBuildModel listBuildModel;
    private AwesomeSpinner awesomeSpinnerStatus;
    private boolean fromHolder;
    private Bundle bundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_build);
        firebaseStorage=FirebaseStorage.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        bundle = getIntent().getExtras();
        btSubmit = findViewById(R.id.btSubmit);
        imageViewBanner=findViewById(R.id.img_banner);
        editTextName=findViewById(R.id.ed_project_name);
        editTextAboutRom=findViewById(R.id.ed_about_rom);
        editTextVersion=findViewById(R.id.ed_version);
        editTextUrl=findViewById(R.id.ed_url);
        editTextSourceCode=findViewById(R.id.ed_source_code);
        editTextCredits=findViewById(R.id.ed_credits);
        awesomeSpinnerStatus = findViewById(R.id.spinner_status);
        fromHolder=bundle.getBoolean("fromHolder");
        date= DateUtils.getDate();
        email=firebaseAuth.getCurrentUser().getEmail();
        name=firebaseAuth.getCurrentUser().getDisplayName();
        if (fromHolder)
        {
            romName = bundle.getString("name");
            aboutRom = bundle.getString("description");
            url = bundle.getString("romUrl");
            Credits= bundle.getString("credits");
            version = bundle.getInt("version");
            stabilityStatus = bundle.getString("status");
            sourceCode = bundle.getString("source");
            editTextName.setText(romName);
            editTextAboutRom.setText(aboutRom);
           editTextVersion.setText(String.valueOf(version));
            editTextUrl.setText(url);
            if (sourceCode!=null)
            {
                editTextSourceCode.setText(sourceCode);
            }
            if (Credits!=null)
            {
                editTextCredits.setText(Credits);
            }
            final String path = bundle.getString("bannerUrl");
            File localFile = null;
            try {
                localFile= File.createTempFile("images", "jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }

            final File finalLocalFile = localFile;
            firebaseStorage.getReference().child(path).getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Glide.with(getApplicationContext()).load(finalLocalFile).into(imageViewBanner);
                    imgpath= finalLocalFile.getAbsolutePath();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });


        }

        fileListerDialog = FileListerDialog.createFileListerDialog(this);
        fileListerDialog.setFileFilter(FileListerDialog.FILE_FILTER.IMAGE_ONLY);

        btSubmit.setMode(ActionProcessButton.Mode.PROGRESS);
        btSubmit.setMode(ActionProcessButton.Mode.ENDLESS);
        List<String> categories = new ArrayList();
        categories.add("Stable");
        categories.add("Beta");
        categories.add("Alpha");
        categories.add("Pre release");
        categories.add("Initial release");

        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);

        awesomeSpinnerStatus.setAdapter(categoriesAdapter);

        fileListerDialog.setOnFileSelectedListener(new OnFileSelectedListener() {
            @Override
            public void onFileSelected(File file, String path) {
                Glide.with(getApplicationContext())
                        .load(path)
                        .into(imageViewBanner);
            }
        });
        awesomeSpinnerStatus.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
            @Override
            public void onItemSelected(int i, String s) {
                stabilityStatus=s;
            }
        });
        btSubmit.setOnClickListener(this);
        imageViewBanner.setOnClickListener(this);
    }


    private boolean CheckFields(){
        romName=editTextName.getText().toString();
        aboutRom=editTextAboutRom.getText().toString();
        try {
            version= Integer.parseInt(editTextVersion.getText().toString());
        }catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
        url=editTextUrl.getText().toString();
        sourceCode=editTextSourceCode.getText().toString();
        Credits=editTextCredits.getText().toString();
        if (imgpath.equals("noimg"))
        {
            Toast.makeText(getBaseContext(),"Please select banner",Toast.LENGTH_SHORT).show();
            return false;
        }else if (romName.isEmpty())
        {
            Toast.makeText(getBaseContext(),"Please enter rom name",Toast.LENGTH_SHORT).show();
            return false;
        } else if (aboutRom.isEmpty())
        {
            Toast.makeText(getBaseContext(),"Please enter about rom",Toast.LENGTH_SHORT).show();
            return false;
        }else if (stabilityStatus==null)
        {
            Toast.makeText(getBaseContext(),"Please select stability status",Toast.LENGTH_SHORT).show();
            return false;
        }else if (version==0)
        {
            Toast.makeText(getBaseContext(),"Please enter version name",Toast.LENGTH_SHORT).show();
            return false;
        }else if (url.isEmpty())
        {
            Toast.makeText(getBaseContext(),"Please enter rom url",Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        if (id==btSubmit.getId())
        {
            if (CheckFields())
            {
                if (URLUtil.isValidUrl(url) ) {
                    final Uri uri = Uri.fromFile(new File(imgpath));
                    uploadTask = firebaseStorage.getReference().child("images/" + uri.getLastPathSegment()).putFile(uri);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if (!sourceCode.isEmpty())
                            {
                                if (!Credits.isEmpty() && !sourceCode.isEmpty())
                                {
                                    listBuildModel = new ListBuildModel(romName, date, name, email, aboutRom, "images/" + uri.getLastPathSegment(), version, stabilityStatus, url,Credits, sourceCode);
                                }else {
                                    listBuildModel = new ListBuildModel(romName, date, name, email, aboutRom, "images/" + uri.getLastPathSegment(), version, stabilityStatus, url,sourceCode,false);

                                }
                            }else
                            if (!Credits.isEmpty())
                            {
                                if (!Credits.isEmpty() && !sourceCode.isEmpty())
                                {
                                    listBuildModel = new ListBuildModel(romName, date, name, email, aboutRom, "images/" + uri.getLastPathSegment(), version, stabilityStatus, url,Credits, sourceCode);
                                }else {
                                    listBuildModel = new ListBuildModel(romName, date, name, email, aboutRom, "images/" + uri.getLastPathSegment(), version, stabilityStatus, url,Credits);

                                }
                            }else {
                                listBuildModel = new ListBuildModel(romName, date, name, email, aboutRom, "images/" + uri.getLastPathSegment(), version, stabilityStatus, url);
                            }

                            firebaseDatabase.getReference("Rom").push().setValue(listBuildModel);
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            btSubmit.setEnabled(true);
                            btSubmit.setProgress(0);
                        }
                    });
                    btSubmit.setEnabled(false);
                    btSubmit.setProgress(50);
                }else {
                    Toast.makeText(getBaseContext(),"Please enter valid url",Toast.LENGTH_SHORT).show();
                }
            }
        }
        else if (id==imageViewBanner.getId())
        {
            fileListerDialog.show();
        }
    }
}
