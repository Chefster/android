package com.codepath.chefster.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.codepath.chefster.ChefsterApplication;
import com.codepath.chefster.R;
import com.codepath.chefster.models.Dish;
import com.codepath.chefster.views.ShareDishView;

import org.parceler.Parcels;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShareActivity extends BaseActivity implements ShareDishView.OnLaunchCameraListener {
    public final String APP_TAG = "MyCustomApp";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;

    @BindView(R.id.linear_layout_share_dish_frames) LinearLayout mainLinearLayout;

    int numOfPhotosTaken = 0;
    List<Dish> cookedDishes;
    HashMap<String, ShareDishView> shareDishViewList;
    String lastFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        ButterKnife.bind(this);

        Intent startingIntent = getIntent();
        cookedDishes = Parcels.unwrap(startingIntent.getParcelableExtra(ChefsterApplication.SELECTED_DISHES_KEY));

        shareDishViewList = new HashMap<>();
        for (Dish dish : cookedDishes) {
            ShareDishView shareDishView = new ShareDishView(this, dish);
            shareDishViewList.put(dish.getTitle(), shareDishView);
            mainLinearLayout.addView(shareDishView);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String dishName = lastFileName.split("_")[0];
                ShareDishView shareDishView = shareDishViewList.get(dishName);
                if (shareDishView != null) {
                    shareDishView.addToImagesList(getPhotoFileUri("photo" + numOfPhotosTaken + ".jpg"));
                }
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Returns the Uri for a photo stored on disk given the fileName
    public Uri getPhotoFileUri(String fileName) {
        // Only continue if the SD Card is mounted
        if (isExternalStorageAvailable()) {
            // Get safe storage directory for photos
            // Use `getExternalFilesDir` on Context to access package-specific directories.
            // This way, we don't need to request external read/write runtime permissions.
            File mediaStorageDir = new File(
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
                Log.d(APP_TAG, "failed to create directory");
            }

            // Return the file target for the photo based on filename
            return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + fileName));
        }
        return null;
    }

    // Returns true if external storage for photos is available
    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    @OnClick(R.id.text_view_share_button)
    public void sharePhotos() {
        ArrayList<Uri> photoUrisList = new ArrayList<>();
        for (int i = 0; i < cookedDishes.size(); i++) {
            ShareDishView shareDishView = (ShareDishView) mainLinearLayout.getChildAt(i);
            photoUrisList.addAll(shareDishView.getImagesList());
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

    @Override
    public void launchCamera(String dish) {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        lastFileName = dish + "_" + ++numOfPhotosTaken + ".jpg";
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri(lastFileName)); // set the image file name

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }
}
