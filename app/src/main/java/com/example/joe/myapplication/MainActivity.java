package com.example.joe.myapplication;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    private final static int RESULT_CAMERA = 1001;
    private final static int REQUEST_PERMISSION = 1002;

    private ImageView imageView;
    private Uri cameraUri;
    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("debug","onCreate()");
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.image);

        Button cameraButton = findViewById(R.id.button);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Android 6, API 23以上でパーミッシンの確認
                if (Build.VERSION.SDK_INT >= 23) {
                    //checkPermission();
                }
                else {
                    cameraIntent();
                }
            }
        });
    }

    private void cameraIntent(){
        Log.d("debug","cameraIntent()");

        // 保存先のフォルダーを作成
        File cameraFolder = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES),"IMG");
        cameraFolder.mkdirs();

        // 保存ファイル名
        String fileName = new SimpleDateFormat(
                "ddHHmmss", Locale.TAIWAN).format(new Date());
        filePath = String.format("%s/%s.jpg", cameraFolder.getPath(),fileName);
        Log.d("debug","filePath:"+filePath);

        // capture画像のファイルパス
        File cameraFile = new File(filePath);
        cameraUri = FileProvider.getUriForFile(
                MainActivity.this,
                getApplicationContext().getPackageName() + ".provider",
                cameraFile);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
        startActivityForResult(intent, RESULT_CAMERA);

        Log.d("debug","startActivityForResult()");
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent intent) {
        if (requestCode == RESULT_CAMERA) {

            if(cameraUri != null){
                imageView.setImageURI(cameraUri);

                registerDatabase(filePath);
            }
            else{
                Log.d("debug","cameraUri == null");
            }
        }
    }

    // アンドロイドのデータベースへ登録する
    private void registerDatabase(String file) {
        ContentValues contentValues = new ContentValues();
        ContentResolver contentResolver = MainActivity.this.getContentResolver();
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        contentValues.put("_data", file);
        contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
    }

    // Runtime Permission check
//    private void checkPermission(){
//        // 既に許可している
//        if (ActivityCompat.checkSelfPermission(this,
//                WRITE_EXTERNAL_STORAGE) ==
//                PackageManager.PERMISSION_GRANTED){
//            cameraIntent();
//        }
//        // 拒否していた場合
//        else{
//            requestPermission();
//        }
//    }

    // 許可を求める
    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);

        } else {
            Toast toast = Toast.makeText(this,
                    "許可されないとアプリが実行できません",
                    Toast.LENGTH_SHORT);
            toast.show();

            ActivityCompat.requestPermissions(this,
                    new String[]{WRITE_EXTERNAL_STORAGE,},
                    REQUEST_PERMISSION);

        }
    }

    // 結果の受け取り
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        Log.d("debug","onRequestPermissionsResult()");

        if (requestCode == REQUEST_PERMISSION) {
            // 使用が許可された
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                cameraIntent();

            } else {
                // それでも拒否された時の対応
                Toast toast = Toast.makeText(this,
                        "これ以上なにもできません", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d("debug","onStart()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("debug","onRestart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("debug","onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("debug","onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("debug","onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("debug","onDestroy()");
    }

}