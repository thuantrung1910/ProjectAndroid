package com.example.doanmonhoc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    final String DATABASE_NAME = "LinhKienDB.db";
    SQLiteDatabase database;
    ListView listView;
    ArrayList<LinhKien> list;
    AdapterLinhKien adapter;
    Button btnAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addControls();
        readData();
    }

    private void addControls() {
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });
        listView = (ListView) findViewById(R.id.listView);
        list = new ArrayList<>();
        adapter = new AdapterLinhKien(this,list);
        listView.setAdapter(adapter);


    }
    private void readData(){
        database = Database.initDatabase(this, DATABASE_NAME );
        Cursor cursor = database.rawQuery("SELECT * FROM Cpu",null);
        list.clear();
        for(int i = 0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            int id = cursor.getInt(0);
            String ten = cursor.getString(1);
            String gia = cursor.getString(2);
            byte[] anh = cursor.getBlob(3);
            list.add(new LinhKien(id, ten, gia, anh));
        }
        adapter.notifyDataSetChanged();
    }
}