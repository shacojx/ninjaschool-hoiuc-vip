package huydat.real;

import java.util.ArrayList;

public class CheckTXLuong {
    public String name;
    public String item;
    public String time;

    public static ArrayList<CheckTXLuong> checkTXLuongArrayList = new ArrayList<>();

    public CheckTXLuong(String name, String item, String time) {
        this.name = name;
        this.item = item;
        this.time = time;
    }
}

