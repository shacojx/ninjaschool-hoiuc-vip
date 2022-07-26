package huydat.real;

import java.util.ArrayList;

public class CheckCLLuong {
    public String name;
    public String item;
    public String time;

    public static ArrayList<CheckCLLuong> checkCLLuongArrayList = new ArrayList<>();

    public CheckCLLuong(String name, String item, String time) {
        this.name = name;
        this.item = item;
        this.time = time;
    }
}

