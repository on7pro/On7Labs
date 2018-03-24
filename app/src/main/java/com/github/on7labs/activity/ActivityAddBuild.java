package com.github.on7labs.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dd.processbutton.iml.ActionProcessButton;
import com.github.on7labs.R;
import com.github.on7labs.model.ListBuildModel;
import com.github.on7labs.model.ScreenShots;
import com.github.on7labs.util.DateUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import org.angmarch.views.NiceSpinner;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import yogesh.firzen.filelister.FileListerDialog;
import yogesh.firzen.filelister.OnFileSelectedListener;

/**
 * Created by androidlover5842 on 22.3.2018.
 */

public class ActivityAddBuild extends AppCompatActivity implements View.OnClickListener {
    private ActionProcessButton btSubmit;
    private ImageView imageViewBanner;
    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FileListerDialog fileListerDialog;
    private int version = 0;
    private String email, name, imgpath = "noimg", date, romName, aboutRom, stabilityStatus = "Stable", url, sourceCode, Credits, key, type="Rom";
    private EditText editTextName, editTextAboutRom, editTextVersion, editTextUrl, editTextSourceCode, editTextCredits;
    private UploadTask uploadTask;
    private ListBuildModel listBuildModel;
    private ScreenShots screenShots;
    private NiceSpinner niceSpinnerReleaseStatus;
    private NiceSpinner niceSpinnerReleaseType;
    private boolean fromHolder;
    private Bundle bundle;
    final List<String> iconList = new LinkedList<>(Arrays.asList("AOSP N", "AOSP O", "CrDroid", "Remix OS","Cyanogenmod","Lineage OS","Darkness Redefined"));
    private ImageView imageViewSS1,imageViewSS2,imageViewSS3,imageViewSS4,imageViewSS5;
    private String img1=null,img2=null,img3=null,img4=null,img5=null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_build);
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        bundle = getIntent().getExtras();
        btSubmit = findViewById(R.id.btSubmit);
        imageViewBanner = findViewById(R.id.img_banner);
        editTextName = findViewById(R.id.ed_project_name);
        editTextAboutRom = findViewById(R.id.ed_about_rom);
        editTextVersion = findViewById(R.id.ed_version);
        editTextUrl = findViewById(R.id.ed_url);
        editTextSourceCode = findViewById(R.id.ed_source_code);
        editTextCredits = findViewById(R.id.ed_credits);
        niceSpinnerReleaseStatus = findViewById(R.id.spinner_release_status);
        niceSpinnerReleaseType = findViewById(R.id.spinner_release_type);
        imageViewSS1=findViewById(R.id.img_ss_1);
        imageViewSS2=findViewById(R.id.img_ss_2);
        imageViewSS3=findViewById(R.id.img_ss_3);
        imageViewSS4=findViewById(R.id.img_ss_4);
        imageViewSS5=findViewById(R.id.img_ss_5);

        final List<String> releaseStatusList = new LinkedList<>(Arrays.asList("Stable", "Beta", "Alpha", "Pre release", "Initial release"));
        final List<String> releaseType = new LinkedList<>(Arrays.asList("Rom", "Recovery", "Kernel", "Mod"));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        fromHolder = bundle.getBoolean("fromHolder");
        date = DateUtils.getDate();
        email = firebaseAuth.getCurrentUser().getEmail();
        name = firebaseAuth.getCurrentUser().getDisplayName();
        if (fromHolder) {
            romName = bundle.getString("name");
            aboutRom = bundle.getString("description");
            url = bundle.getString("romUrl");
            Credits = bundle.getString("credits");
            version = bundle.getInt("version");
            stabilityStatus = bundle.getString("status");
            sourceCode = bundle.getString("source");
            key = bundle.getString("key");
            editTextName.setText(romName);
            editTextAboutRom.setText(aboutRom);
            editTextVersion.setText(String.valueOf(version));
            editTextUrl.setText(url);
            img1=bundle.getString("img1");
            img2=bundle.getString("img2");
            img3=bundle.getString("img3");
            img4=bundle.getString("img4");
            img5=bundle.getString("img5");

            if (sourceCode != null) {
                editTextSourceCode.setText(sourceCode);
            }
            if (Credits != null) {
                editTextCredits.setText(Credits);
            }
            final String path = bundle.getString("bannerUrl");
            loadImages(imageViewBanner,path,0);
            if (img1!=null)
            {
                loadImages(imageViewSS1,img1,1);
            }
            if (img2!=null)
                loadImages(imageViewSS2,img2,2);
            if (img3!=null)
                loadImages(imageViewSS3,img3,3);
            if (img4!=null)
                loadImages(imageViewSS4,img4,4);
            if (img4!=null)
                loadImages(imageViewSS5,img5,5);
        }

        fileListerDialog = FileListerDialog.createFileListerDialog(this);
        fileListerDialog.setFileFilter(FileListerDialog.FILE_FILTER.IMAGE_ONLY);

        btSubmit.setMode(ActionProcessButton.Mode.PROGRESS);
        btSubmit.setMode(ActionProcessButton.Mode.ENDLESS);

        fileListerDialog.setOnFileSelectedListener(new OnFileSelectedListener() {
            @Override
            public void onFileSelected(File file, String path) {
                Glide.with(getApplicationContext())
                        .load(path)
                        .into(imageViewBanner);
                imgpath=path;
            }
        });

        niceSpinnerReleaseStatus.attachDataSource(releaseStatusList);
        niceSpinnerReleaseType.attachDataSource(releaseType);
        niceSpinnerReleaseStatus.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                stabilityStatus = releaseStatusList.get(i);
            }
        });
        niceSpinnerReleaseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type = releaseType.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btSubmit.setOnClickListener(this);
        imageViewBanner.setOnClickListener(this);
        imageViewSS1.setOnClickListener(this);
        imageViewSS2.setOnClickListener(this);
        imageViewSS3.setOnClickListener(this);
        imageViewSS4.setOnClickListener(this);
        imageViewSS5.setOnClickListener(this);
    }

    private void loadImages(final ImageView imageView, final String thatThingUrl, final int value){
        Uri uri=Uri.parse(thatThingUrl);
        final File localFile=new File(getCacheDir()+File.separator,uri.getLastPathSegment() );
        firebaseStorage.getReference().child(thatThingUrl).getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Glide.with(getApplicationContext()).load(localFile).into(imageView);
                if (value==0)
                    imgpath = localFile.getAbsolutePath();
                else if (value==1)
                    img1=thatThingUrl;
                else if (value==2)
                    img2=thatThingUrl;
                else if (value==3)
                    img3=thatThingUrl;
                else if (value==4)
                    img4=thatThingUrl;
                else if (value==5)
                    img5=thatThingUrl;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }

    private void SelectScreenShots(final ImageView imageView, final int value){
        FileListerDialog filea = FileListerDialog.createFileListerDialog(this);
        filea.setFileFilter(FileListerDialog.FILE_FILTER.IMAGE_ONLY);
        filea.setOnFileSelectedListener(new OnFileSelectedListener() {
            @Override
            public void onFileSelected(File file, final String path) {
                final Uri uri = Uri.fromFile(new File(path));
                btSubmit.setEnabled(false);
                btSubmit.setProgress(50);
                Toasty.info(ActivityAddBuild.this,"Uploading "+path, Toast.LENGTH_SHORT).show();
                uploadTask = firebaseStorage.getReference().child("images/screenshots/" + uri.getLastPathSegment()).putFile(uri);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Glide.with(getApplicationContext()).load(path).into(imageView);
                        btSubmit.setEnabled(true);
                        btSubmit.setProgress(0);
                        if(value==1)
                        {
                            img1="images/screenshots/" + uri.getLastPathSegment();

                        }else if (value==2)
                        {
                            img2="images/screenshots/" + uri.getLastPathSegment();

                        }else if (value==3)
                        {
                            img3="images/screenshots/" + uri.getLastPathSegment();

                        }else if (value==4)
                        {
                            img4="images/screenshots/" + uri.getLastPathSegment();

                        }else if (value==5)
                        {
                            img5="images/screenshots/" + uri.getLastPathSegment();

                        }

                        Toasty.success(ActivityAddBuild.this,"Uploaded", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        btSubmit.setEnabled(true);
                        btSubmit.setProgress(0);
                        Toasty.error(ActivityAddBuild.this,"Failed to upload", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        filea.show();

    }

    private void SelectBanner(){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, android.R.layout.select_dialog_item);
        arrayAdapter.add("Popular Projects");
        arrayAdapter.add("Custom");
        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);
                if (strName.equals("Custom")) {
                    fileListerDialog.show();
                } else if (strName.equals("Popular Projects")) {
                    final ArrayAdapter<String> arrayInnerAdapter = new ArrayAdapter(ActivityAddBuild.this, android.R.layout.select_dialog_item);
                    for (int i=0;i<iconList.size();i++)
                    {
                        arrayInnerAdapter.add(iconList.get(i));
                    }
                    AlertDialog.Builder builderInner = new AlertDialog.Builder(ActivityAddBuild.this);
                    builderInner.setAdapter(arrayInnerAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, final int i) {
                            String fname=iconList.get(i)+".png";
                            final File localFile=new File(getCacheDir()+File.separator, fname);
                            firebaseStorage.getReference().child("images/roms/"+fname).getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Glide.with(getApplicationContext()).load(localFile).into(imageViewBanner);
                                    imgpath = localFile.getAbsolutePath();
                                    editTextName.setText(iconList.get(i));
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                }
                            });

                        }
                    });
                    builderInner.show();
                }
            }
        });
        builderSingle.show();

    }

    private boolean CheckFields() {
        romName = editTextName.getText().toString();
        aboutRom = editTextAboutRom.getText().toString();
        try {
            version = Integer.parseInt(editTextVersion.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        url = editTextUrl.getText().toString();
        sourceCode = editTextSourceCode.getText().toString();
        Credits = editTextCredits.getText().toString();
        if (imgpath.equals("noimg")) {
            Toasty.error(getBaseContext(), "Select banner", Toast.LENGTH_SHORT).show();
            return false;
        } else if (romName.isEmpty()) {
            Toasty.error(getBaseContext(), "Enter the project name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (aboutRom.isEmpty()) {
            Toasty.error(getBaseContext(), "Enter project description", Toast.LENGTH_SHORT).show();
            return false;
        } else if (stabilityStatus == null) {
            Toasty.error(getBaseContext(), "Select project stability status", Toast.LENGTH_SHORT).show();
            return false;
        } else if (version == 0) {
            Toasty.error(getBaseContext(), "Enter project version", Toast.LENGTH_SHORT).show();
            return false;
        } else if (url.isEmpty()) {
            Toasty.error(getBaseContext(), "Enter project download url", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public void onClick(View view) {
        final int id = view.getId();
        if (id == btSubmit.getId()) {
            if (CheckFields()) {
                if (URLUtil.isValidUrl(url)) {
                    final Uri uri = Uri.fromFile(new File(imgpath));
                    uploadTask = firebaseStorage.getReference().child("images/" + uri.getLastPathSegment()).putFile(uri);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if (img1!=null)
                            {
                                screenShots=new ScreenShots(img1);
                                if (img1!=null && img2!=null)
                                {
                                    screenShots=new ScreenShots(img1,img2);
                                    if (img1!=null && img2!=null && img3!=null)
                                    {
                                        screenShots=new ScreenShots(img1,img2,img3);
                                        if (img1!=null && img2!=null && img3!=null && img4!=null)
                                        {
                                            screenShots=new ScreenShots(img1,img2,img3,img4);
                                            if (img1!=null && img2!=null && img3!=null && img4!=null && img5!=null)
                                            {
                                                screenShots=new ScreenShots(img1,img2,img3,img4,img5);
                                            }
                                        }
                                    }
                                }
                            }

                            if (!sourceCode.isEmpty()) {
                                if (!Credits.isEmpty() && !sourceCode.isEmpty()) {
                                    listBuildModel = new ListBuildModel(romName, date, name, email, aboutRom, "images/" + uri.getLastPathSegment(), version, stabilityStatus, url, Credits, sourceCode,screenShots);
                                } else {
                                    listBuildModel = new ListBuildModel(romName, date, name, email, aboutRom, "images/" + uri.getLastPathSegment(), version, stabilityStatus, url, sourceCode, false,screenShots);

                                }
                            } else if (!Credits.isEmpty()) {
                                if (!Credits.isEmpty() && !sourceCode.isEmpty()) {
                                    listBuildModel = new ListBuildModel(romName, date, name, email, aboutRom, "images/" + uri.getLastPathSegment(), version, stabilityStatus, url, Credits, sourceCode,screenShots);
                                } else {
                                    listBuildModel = new ListBuildModel(romName, date, name, email, aboutRom, "images/" + uri.getLastPathSegment(), version, stabilityStatus, url, Credits,screenShots);

                                }
                            } else {
                                listBuildModel = new ListBuildModel(romName, date, name, email, aboutRom, "images/" + uri.getLastPathSegment(), version, stabilityStatus, url,screenShots);
                            }

                            if (fromHolder) {
                                firebaseDatabase.getReference(type).child(key).setValue(listBuildModel);
                            } else {
                                firebaseDatabase.getReference(type).push().setValue(listBuildModel);
                            }
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
                } else {
                    Toasty.error(getBaseContext(), "Enter a valid url", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (id == imageViewBanner.getId()) {
            SelectBanner();
        }
        else if (id==imageViewSS1.getId())
        {
            SelectScreenShots(imageViewSS1,1);
        }

        else if (id==imageViewSS2.getId())
        {
            SelectScreenShots(imageViewSS2,2);
        }
        else if (id==imageViewSS3.getId())
        {
            SelectScreenShots(imageViewSS3,3);
        }
        else if (id==imageViewSS4.getId())
        {
            SelectScreenShots(imageViewSS4,4);
        }
        else if (id==imageViewSS5.getId())
        {
            SelectScreenShots(imageViewSS5,5);
        }

    }
}
