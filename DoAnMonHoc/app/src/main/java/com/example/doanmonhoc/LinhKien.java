package com.example.doanmonhoc;

public class LinhKien {
    public int id;
    public String ten;

    public LinhKien(int id, String ten, String gia, byte[] anh) {
        this.id = id;
        this.ten = ten;
        this.gia = gia;
        this.anh = anh;
    }

    public  String gia;
    public byte[] anh;
}
