package com.example.doanmonhoc;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class UpdateActivity extends AppCompatActivity {
    final String DATABASE_NAME = "LinhKienDB.db";
    final int REQUEST_CHOOSE_PHOTO = 321;
    final  int RESQUEST_TAKE_PHOTO = 123;
    Button btnChonHinh, btnChupHinh, btnLuu, btnHuy;
    EditText edtTen,edtGia;
    ImageView imgHinhDaiDien;
    int id = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        addControls();
        addEvent();
        initUI();
    }
    private void addEvent(){
        btnChonHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });
        btnChupHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }
    private void initUI() {
        Intent intent = getIntent();
        id = intent.getIntExtra("ID",-1);
        SQLiteDatabase database = Database.initDatabase(this, DATABASE_NAME );
        Cursor cursor = database.rawQuery("SELECT * FROM Cpu ",null);
        cursor.moveToFirst();
        String ten = cursor.getString(1);
        String gia = cursor.getString(2);
        byte[] anh = cursor.getBlob(3);

        Bitmap bitmap = BitmapFactory.decodeByteArray(anh, 0,anh.length);
        imgHinhDaiDien.setImageBitmap(bitmap);
        edtGia.setText(gia);
        edtTen.setText(ten);
    }

    private void addControls() {
        btnChonHinh = (Button) findViewById(R.id.btnChonHinh);
        btnChupHinh = (Button) findViewById(R.id.btnChupHinh);
        btnHuy = (Button) findViewById(R.id.btnHuy);
        btnLuu = (Button) findViewById(R.id.btnLuu);
        edtGia = (EditText) findViewById(R.id.edtGia);
        edtTen = (EditText) findViewById(R.id.edtTen);
        imgHinhDaiDien = (ImageView) findViewById(R.id.imgHinhDaiDien);

    }
    private void takePicture(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, RESQUEST_TAKE_PHOTO);
    }
    private void choosePhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CHOOSE_PHOTO) {
                try {
                    Uri imageUri = data.getData();
                    InputStream is = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    imgHinhDaiDien.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }else if (requestCode ==RESQUEST_TAKE_PHOTO){
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imgHinhDaiDien.setImageBitmap(bitmap);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void update(){
        String ten = edtTen.getText().toString();
        String gia = edtGia.getText().toString();

        byte[] anh = getByteArrayFromImageView(imgHinhDaiDien);
        ContentValues contentValues = new ContentValues();
        contentValues.put("ten",ten);
        contentValues.put("gia",gia);
        contentValues.put("Anh",anh);

        SQLiteDatabase database = Database.initDatabase(this, "LinhKienDB.db");
        database.update("Cpu",contentValues, "id = ?", new String[]{id + ""});
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    private void cancel(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    private  byte[] getByteArrayFromImageView(ImageView imgv){
        BitmapDrawable drawable = ( BitmapDrawable)  imgv.getDrawable();
        Bitmap bmp = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100,stream);
        byte[] byteArray = stream.toByteArray();
        return  byteArray;
    }
}