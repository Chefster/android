package com.codepath.chefster.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.chefster.ChefsterApplication;
import com.codepath.chefster.R;
import com.codepath.chefster.models.User;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends AppCompatActivity {
    @BindView(R.id.profileToolbar) Toolbar toolbar;
    @BindView(R.id.profile_image) ImageView ivProfilePhoto;
    @BindView(R.id.tvProfileName) TextView tvProfileName;
    @BindView(R.id.tvProfileEmail) TextView tvProfileEmail;
    @BindView(R.id.buttonProfileName) Button buttonProfileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_activity);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        Intent intent = getIntent();
        User user = Parcels.unwrap(intent.getParcelableExtra("user"));

        if ( user.getImageUrl() != ""){
            Glide.with(this).load(user.getImageUrl()).asBitmap()
                    .placeholder(R.drawable.profile_avatar_placeholder_large)
                    .into(ivProfilePhoto);
        }
        tvProfileName.setText(user.getFirstName());
        tvProfileEmail.setText(user.getEmail());

        buttonProfileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeNameDialog();
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
}
