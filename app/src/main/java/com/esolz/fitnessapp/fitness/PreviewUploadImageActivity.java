package com.esolz.fitnessapp.fitness;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.helper.AppConfig;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by ltp on 11/07/15.
 */
public class PreviewUploadImageActivity extends AppCompatActivity {

    ImageView imgPreview;
    RelativeLayout rlBtnCancel, rlBtnDone;
    ProgressBar imgUploadPBar;

    String Current_PATH = "", exception = "", urlResponse = "", urlGoal = "", urlCurrent = "", IMG_TYPE = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esolz_camera_preview);

        imgUploadPBar = (ProgressBar) findViewById(R.id.img_upload_pbar);
        imgUploadPBar.setVisibility(View.GONE);
        imgPreview = (ImageView) findViewById(R.id.preview_img);
        rlBtnCancel = (RelativeLayout) findViewById(R.id.rl_btn_cancel);
        rlBtnDone = (RelativeLayout) findViewById(R.id.rl_btn_done);

        Current_PATH = getIntent().getExtras().getString("IMG");
        Picasso.with(getApplicationContext()).load("file://" + Current_PATH.trim()).fit().centerCrop().into(imgPreview);

        Log.e("IMG_TYPE", getIntent().getExtras().getString("imgType"));

        rlBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rlBtnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urlGoal = "http://esolz.co.in/lab6/ptplanner/app_control/client_goal_image_upload?client_id=" + AppConfig.loginDatatype.getSiteUserId();
                urlCurrent = "http://esolz.co.in/lab6/ptplanner/app_control/client_current_image_upload?client_id=" + AppConfig.loginDatatype.getSiteUserId();

                IMG_TYPE = getIntent().getExtras().getString("imgType");

                if (IMG_TYPE.equals("currentImg")) {
                    uploadCurrentIMG(urlCurrent);
                } else {
                    uploadGoalIMG(urlGoal);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    public void uploadCurrentIMG(final String imgPath) {

        AsyncTask<Void, Void, Void> uploadIMG = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                imgUploadPBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... para) {
                exception = "";
                try {
                    HttpParams params = new BasicHttpParams();
                    params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
                    HttpClient client = new DefaultHttpClient();
                    client = new DefaultHttpClient(params);
                    HttpPost post = new HttpPost(imgPath);
                    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                    builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                    Log.d("Upload Image : ", Current_PATH);
                    File file12 = new File(compressImage(Current_PATH));
                    builder.addBinaryBody("client_current_image", file12, ContentType.APPLICATION_OCTET_STREAM, "fitness_current_image.png");
                    HttpEntity entity = builder.build();
                    post.setEntity(entity);
                    HttpResponse response = client.execute(post);
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(
                                    response.getEntity().getContent(), "UTF-8"));
                    String sResponse;
                    StringBuilder s = new StringBuilder();
                    while ((sResponse = reader.readLine()) != null) {
                        s = s.append(sResponse);
                    }
                    Log.d("Response: ", s.toString());
                } catch (Exception ex) {
                    Log.e("Debug", "error: " + ex.getMessage(), ex);
                    exception = ex.toString();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                imgUploadPBar.setVisibility(View.GONE);
                if (exception.equals("")) {
                    Intent intent = new Intent(PreviewUploadImageActivity.this, LandScreenActivity.class);
                    intent.putExtra("MSG", "IMGUPLOAD");
                    startActivity(intent);
                } else {
                    Log.d("Exception : ", exception);
                    Toast.makeText(PreviewUploadImageActivity.this, "Server not responding....", Toast.LENGTH_LONG).show();
                }
            }

        };
        uploadIMG.execute();
    }

    public void uploadGoalIMG(final String imgPath) {

        AsyncTask<Void, Void, Void> uploadIMG = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                imgUploadPBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... para) {
                exception = "";
                try {
                    HttpParams params = new BasicHttpParams();
                    params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
                    HttpClient client = new DefaultHttpClient();
                    client = new DefaultHttpClient(params);
                    HttpPost post = new HttpPost(imgPath);
                    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                    builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                    Log.d("Upload Image : ", Current_PATH);
                    File file12 = new File(compressImage(Current_PATH));
                    builder.addBinaryBody("client_goal_image", file12, ContentType.APPLICATION_OCTET_STREAM, "fitness_goal_image.png");
                    HttpEntity entity = builder.build();
                    post.setEntity(entity);
                    HttpResponse response = client.execute(post);
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(
                                    response.getEntity().getContent(), "UTF-8"));
                    String sResponse;
                    StringBuilder s = new StringBuilder();
                    while ((sResponse = reader.readLine()) != null) {
                        s = s.append(sResponse);
                    }
                    Log.d("Response: ", s.toString());
                } catch (Exception ex) {
                    Log.e("Debug", "error: " + ex.getMessage(), ex);
                    exception = ex.toString();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                imgUploadPBar.setVisibility(View.GONE);
                if (exception.equals("")) {
                    Intent intent = new Intent(PreviewUploadImageActivity.this, LandScreenActivity.class);
                    intent.putExtra("MSG", "IMGUPLOAD");
                    startActivity(intent);
                } else {
                    Log.d("Exception : ", exception);
                    Toast.makeText(PreviewUploadImageActivity.this, "Server not responding....", Toast.LENGTH_LONG).show();
                }
            }

        };
        uploadIMG.execute();
    }

    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;
        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }
        options.inSampleSize = calculateInSampleSize(options, actualWidth,
                actualHeight);
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            // load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,
                    Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2,
                middleY - bmp.getHeight() / 2, new Paint(
                        Paint.FILTER_BITMAP_FLAG));

        // check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);
            scaledBitmap.compress(Bitmap.CompressFormat.PNG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory()
                .getPath(), "Fitness/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/"
                + System.currentTimeMillis() + ".png");
        return uriSting;

    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null,
                null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor
                    .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options,
                                     int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }
}

