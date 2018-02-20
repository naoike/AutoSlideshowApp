package jp.techacademy.naoki.ikegami.autoslideshowapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.view.View;
import android.widget.Button;




public class MainActivity extends AppCompatActivity {

    Button mNextButton;
    Button mBackButton;
    Button mStartButton;

    Cursor cursor;
    ImageView ImageView;

    private static final int PERMISSIONS_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mNextButton = (Button) findViewById(R.id.button1);
        mBackButton = (Button) findViewById(R.id.button2);
        mStartButton = (Button) findViewById(R.id.button3);
        ImageView = (ImageView) findViewById(R.id.imageView);


        // Android 6.0以降の場合 パーミッション
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                getContentsInfo();

            } else {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
            }

            // Android 5系以下の場合
        } else {
            getContentsInfo();
        }




        //進むボタン
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                cursor.moveToNext();

                int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                Long id = cursor.getLong(fieldIndex);
                Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);


                ImageView.setImageURI(imageUri);


            }

            });


        //戻るボタン
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                cursor.moveToPrevious();

                int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                Long id = cursor.getLong(fieldIndex);
                Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);


                ImageView.setImageURI(imageUri);


            }

        });


    }


    private void getContentsInfo() {          // 画像の情報を取得する
        ContentResolver resolver = getContentResolver();
        cursor = resolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // データの種類 第一引数
                null,
                null,
                null,
                null
        );

    }

}














