package com.hkim00.moves;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;

import com.hkim00.moves.fragments.PhotoAlertDialogFragment;
import com.hkim00.moves.util.BitmapScaler;
import com.hkim00.moves.util.UncaughtExceptionHandler;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SignUpActivity extends AppCompatActivity implements PhotoAlertDialogFragment.PhotoDialogListener {

    private String TAG = "SignUpActivity";
    private final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    private final static int PICK_PHOTO_CODE = 1046;

    private String photoFileName = "photo.jpg";
    private File photoFile;

    private ImageView ivProfilePic;
    private Button btnGetSarted, btnAddPhoto, btnProfilePic;
    private EditText etUsername, etPassword;
    private ParseUser user = new ParseUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler(this));
        setContentView(R.layout.activity_signup);

        setupActionBar();

        getViewIds();

        setupButtons();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    private void getViewIds() {
        btnGetSarted = findViewById(R.id.btnGetStarted);
        etUsername = findViewById(R.id.etUsername2);
        etPassword = findViewById(R.id.etPassword2);
        ivProfilePic = findViewById(R.id.ivProfilePic);
        btnAddPhoto = findViewById(R.id.btnAddPhoto);
        btnProfilePic = findViewById(R.id.btnProfilePic);
    }

    private void setupButtons() {
        btnGetSarted.setOnClickListener(view -> signup());
        btnAddPhoto.setOnClickListener(view -> showAlertDialog());
        btnProfilePic.setOnClickListener(view -> showAlertDialog());
    }

    private void signup() {
        user.setUsername(etUsername.getText().toString().toLowerCase());
        user.setPassword(etPassword.getText().toString());

        user.signUpInBackground(e -> {
            if (e == null) {
                if (photoFile != null) {
                    user.put("profilePhoto", new ParseFile(photoFile));
                    user.saveInBackground();
                } else {
                    user.saveInBackground();
                }

                Log.i(TAG, "Signup successful!");
                Intent intent = new Intent(SignUpActivity.this, Intro2Activity.class);
                startActivity(intent);
                finish();
            } else {
                Log.e(TAG, e.getMessage());
                if (etUsername.getText().toString().equals("") || etPassword.getText().toString().equals("")) {
                    Toast.makeText(this, "Please fill out all fields!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showAlertDialog() {
        FragmentManager fm = getSupportFragmentManager();
        PhotoAlertDialogFragment alertDialog = PhotoAlertDialogFragment.newInstance("Get photo from photo library or camera");
        alertDialog.show(fm, "fragment_alert");
    }


    @Override
    public void onFinishDialog(boolean fromCamera) {
        if (fromCamera) {
            launchCamera();
        } else {
            launchPhotoPicker();
        }
    }

    private void setupActionBar() {
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_grey)));

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar_lt);
        getSupportActionBar().setElevation(2);

        Button btnLeft = findViewById(R.id.btnLeft);
        btnLeft.setOnClickListener(view -> onBackPressed());
    }

    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(SignUpActivity.this, "com.hkim00.moves", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    private File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory");
        }

        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    private void launchPhotoPicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(SignUpActivity.this, "com.hkim00.moves", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, PICK_PHOTO_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap takenImage = rotateBitmapOrientation(photoFile.getAbsolutePath());

                File takenPhotoUri = getPhotoFileUri(photoFileName);
                Bitmap rawTakenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
                Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(rawTakenImage, 75);

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

                try {
                    File resizedFile = getPhotoFileUri(photoFileName + "_resized");
                    resizedFile.createNewFile();
                    FileOutputStream fos = new FileOutputStream(resizedFile);

                    fos.write(bytes.toByteArray());
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ivProfilePic.setImageBitmap(takenImage);

                if (photoFile != null || ivProfilePic.getDrawable() != null) {
                    btnAddPhoto.setText("Replace Image");
                } else {
                    btnAddPhoto.setText("Add photo");
                }

            }
        } else if (requestCode == PICK_PHOTO_CODE) {
            if (data != null) {
                Uri photoUri = data.getData();
                Bitmap selectedImage = null;
                try {
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ivProfilePic.setImageBitmap(selectedImage);
            }
        }
    }

    /*
     * Code rotate from CodePath
     */
    private Bitmap rotateBitmapOrientation(String photoFilePath) {
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoFilePath, bounds);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(photoFilePath, opts);

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(photoFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;

        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);

        return rotatedBitmap;
    }
}
