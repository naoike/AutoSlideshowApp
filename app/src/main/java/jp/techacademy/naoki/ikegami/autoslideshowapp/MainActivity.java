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
import android.os.Handler;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    Button mNextButton;
    Button mBackButton;
    Button mStartButton;

    Cursor cursor;
    ImageView ImageView;
    Timer mTimer;
    Handler mHandler = new Handler();

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


        //再生ボタン
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mTimer == null) {


                    mStartButton.setText("停止");
                    //進むボタンと戻るボタン　タップ不可
                    mNextButton.setEnabled(false);
                    mBackButton.setEnabled(false);

                    // タイマーの作成
                    mTimer = new Timer();
                    // タイマーの始動
                    mTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (cursor.moveToNext()) {

                                    } else {
                                        cursor.moveToFirst();
                                    }

                                    image();
                                }

                            });
                        }
                    }, 2000, 2000);
                }
                else {
                    mTimer.cancel();
                    mTimer = null;

                    mStartButton.setText("再生");
                    //進むボタンと戻るボタン　タップ可
                    mNextButton.setEnabled(true);
                    mBackButton.setEnabled(true);

                }

        }
    });



                    //進むボタン
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cursor.moveToNext()) {

                } else {
                    cursor.moveToFirst();
                }

                image();


            }

            });


        //戻るボタン
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cursor.moveToPrevious()) {

                } else {
                    cursor.moveToLast();
                }

                image();


            }

        });



        try{ if (cursor.moveToFirst()) {image();}
        }
        catch(Exception e){
        }



        //最初の画面を表示
        if (cursor.moveToFirst()) {

        image();

        }

    }


    private void image(){            //画像表示のメソッド化

        int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
        Long id = cursor.getLong(fieldIndex);
        Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

        ImageView.setImageURI(imageUri);

    }

    private void getContentsInfo() {      // 画像の情報を取得する
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














