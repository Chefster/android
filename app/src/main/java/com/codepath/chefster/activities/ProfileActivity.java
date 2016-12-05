package com.codepath.chefster.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.chefster.ChefsterApplication;
import com.codepath.chefster.R;
import com.codepath.chefster.client.FirebaseClient;
import com.codepath.chefster.models.User;

import org.parceler.Parcels;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends AppCompatActivity {
    static final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath;
    private User user;
    boolean editClicked;
    Uri photoUri;

    @BindView(R.id.ivBackArrow) ImageView ivBackArrow;
    @BindView(R.id.profile_image) ImageView ivProfilePhoto;
    @BindView(R.id.tvProfileName) TextView tvProfileName;
    @BindView(R.id.tvProfileEmail) TextView tvProfileEmail;
    @BindView(R.id.buttonProfileName) Button buttonProfileName;
    @BindView(R.id.ivProfileCamera) ImageView ivProfileCamera;
    @BindView(R.id.buttonEdit) Button buttonEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_activity);

        ButterKnife.bind(this);

/*        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.getBackground().setAlpha(0);*/
/*
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);*/

        Intent intent = getIntent();
        user = Parcels.unwrap(intent.getParcelableExtra("user"));

        editClicked = false;
        if (user.getImageUrl() != null)
            photoUri = Uri.parse(user.getImageUrl());

        if ( user.getImageUrl() != ""){
            Glide.with(this).load(user.getImageUrl()).asBitmap()
                    .placeholder(R.drawable.profile_avatar_placeholder_large)
                    .into(ivProfilePhoto);
        }

        if (user.getFirstName() != "" & user.getFirstName() != null)
            tvProfileName.setText(user.getFirstName());
        tvProfileEmail.setText(user.getEmail());

        buttonProfileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 changeNameDialog();

            }
        });

        ivProfileCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if  (! editClicked ) {
                    buttonProfileName.setVisibility(View.VISIBLE);
                    ivProfileCamera.setVisibility(View.VISIBLE);
                    buttonEdit.setText("DONE");
                    editClicked = true;
                }
                else{
                    buttonProfileName.setVisibility(View.INVISIBLE);
                    ivProfileCamera.setVisibility(View.INVISIBLE);
                    buttonEdit.setText("EDIT");
                    editClicked = false;
                    FirebaseClient.updateUserInformation(tvProfileName.getText().toString(), photoUri.toString());
                    Snackbar.make(findViewById(R.id.activity_profile_acticity), R.string.snackbar_text, Snackbar.LENGTH_INDEFINITE)
                            .setDuration(3000).show();
                }
            }
        });


        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    public void changeNameDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        final EditText edittext = new EditText(this);
        InputFilter[] fa= new InputFilter[1];
        fa[0] = new InputFilter.LengthFilter(30);
        edittext.setFilters(fa);

        alert.setMessage("Enter Name");
        alert.setTitle("Profile Name");

        alert.setView(edittext);

        alert.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String name = edittext.getText().toString();
                tvProfileName.setText(name);
                //FirebaseClient.updateUserInformation(name,user.getImageUrl());
                Toast.makeText(getApplication(),"Name Changed",Toast.LENGTH_SHORT).show();
            }
        });

        alert.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        alert.show();
    }

    @Override
    public void onBackPressed() {
        Intent intent= new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    /* Take a Photo with the Camera App */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.codepath.chefster.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    /* Save image File*/
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /*Get the Thumbnail*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
           photoUri = FileProvider.getUriForFile(this, "com.codepath.chefster.fileprovider", new File(mCurrentPhotoPath));
       //    Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
      //     Bitmap mImageBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 20, bitmap.getHeight() / 20, true);
           Glide.with(this).load(photoUri).asBitmap().into(ivProfilePhoto);

       }
    }
}
