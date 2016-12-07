package com.codepath.chefster.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.codepath.chefster.ChefsterApplication;
import com.codepath.chefster.R;
import com.codepath.chefster.models.Dish;
import com.codepath.chefster.views.ShareDishView;

import org.parceler.Parcels;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShareActivity extends BaseActivity implements ShareDishView.OnLaunchCameraListener {
    public final String APP_TAG = "MyCustomApp";
    private static final int RC_IMAGE_CAPTURE = 1;

    @BindView(R.id.linear_layout_share_dish_frames) LinearLayout mainLinearLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private UUID uuid;
    String currentPhotoPath;
    String photoDish;

    List<Dish> cookedDishes;
    HashMap<String, ShareDishView> shareDishViewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.left_arrow);
            getSupportActionBar().setTitle("Share Dish");
        }

        Intent startingIntent = getIntent();
        cookedDishes = Parcels.unwrap(startingIntent.getParcelableExtra(ChefsterApplication.SELECTED_DISHES_KEY));

        shareDishViewList = new HashMap<>();
        for (Dish dish : cookedDishes) {
            ShareDishView shareDishView = new ShareDishView(this, dish);
            shareDishViewList.put(dish.getTitle(), shareDishView);
            mainLinearLayout.addView(shareDishView);
        }
    }

    @OnClick(R.id.share_button)
    public void sharePhotos() {
        ArrayList<Uri> photoUrisList = new ArrayList<>();
        for (int i = 0; i < cookedDishes.size(); i++) {
            ShareDishView shareDishView = (ShareDishView) mainLinearLayout.getChildAt(i);
            List<String> pathList = shareDishView.getImagesList();
            for (String path : pathList) {
                Uri photoUri = FileProvider.getUriForFile(this, "com.codepath.chefster.fileprovider", new File(path));
                photoUrisList.add(photoUri);
            }
        }

        if (photoUrisList.size() == 0) {
            Toast.makeText(this, "There's nothing to share!", Toast.LENGTH_SHORT).show();
        } else {
            final Intent shareIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            shareIntent.setType("image/jpg");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Look what I cooked with Chefster!");
            StringBuilder mealString = new StringBuilder();
            for (Dish dish : cookedDishes) {
                mealString.append(dish.getTitle() + ", ");
            }
            mealString.setLength(mealString.length() - 2);
            mealString.append(". Totally recommend it!");

            shareIntent.putExtra(Intent.EXTRA_TEXT, mealString.toString());
            shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, photoUrisList);
            startActivity(Intent.createChooser(shareIntent, "Share image using"));
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        uuid = UUID.randomUUID();

        String imageFileName = "JPEG_" + uuid.toString();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

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
                startActivityForResult(takePictureIntent, RC_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            ShareDishView shareDishView = shareDishViewList.get(photoDish);
            if (shareDishView != null) {
                shareDishView.addToImagesList(currentPhotoPath);
            }
        }
    }

    @Override
    public void launchCamera(String dishName) {
        photoDish = dishName;
        dispatchTakePictureIntent();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
