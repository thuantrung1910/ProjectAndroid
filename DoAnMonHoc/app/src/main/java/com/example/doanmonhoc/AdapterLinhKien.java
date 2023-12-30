package com.example.doanmonhoc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterLinhKien extends BaseAdapter {
    public AdapterLinhKien(Activity context, ArrayList<LinhKien> list) {
        this.context = context;
        this.list = list;
    }

    Activity context;
    ArrayList<LinhKien>list;

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.listview_row, null);
        ImageView imgHinhDaiDien = (ImageView) row.findViewById(R.id.imgHinhDaiDien);
        TextView txtId = row.findViewById(R.id.txtId);
        TextView txtTen = row.findViewById(R.id.txtTen);
        TextView txtGia = row.findViewById(R.id.txtGia);
        Button btnXoa = row.findViewById(R.id.btnXoa);
        Button btnSua = row.findViewById(R.id.btnSua);

        LinhKien linhKien = list.get(position);
        txtId.setText(linhKien.id + "");
        txtTen.setText(linhKien.ten);
        txtGia.setText(linhKien.gia);

        Bitmap bmHinhDaiDien = BitmapFactory.decodeByteArray(linhKien.anh, 0 , linhKien.anh.length);
        imgHinhDaiDien.setImageBitmap(bmHinhDaiDien);

        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("ID", linhKien.id);
                context.startActivity(intent);
            }
        });
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setTitle("Xác Nhận Xóa");
                builder.setMessage("Bạn Có Muốn Xóa Sản Phẩm Này Không?");
                builder.setPositiveButton("có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete(linhKien.id);
                            }
                        });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        return row;
    }

    private void delete(int idLinhKien) {
        SQLiteDatabase database = Database.initDatabase(context, "LinhKienDB.db");
        database.delete("Cpu", "ID = ?", new String[]{idLinhKien + ""});
        list.clear();
        Cursor cursor = database.rawQuery("SELECT * FROM Cpu",null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String ten = cursor.getString(1);
            String gia = cursor.getString(2);
            byte[] anh = cursor.getBlob(3);

            list.add(new LinhKien(id,ten,gia,anh));
        }
        notifyDataSetChanged();
    }
}

