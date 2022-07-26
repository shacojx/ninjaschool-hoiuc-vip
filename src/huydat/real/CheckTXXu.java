package huydat.real;

import java.util.ArrayList;

public class CheckTXXu {
    public String name;
    public String item;
    public String time;

    public static ArrayList<CheckTXXu> checkTXXuArrayList = new ArrayList<>();

    public CheckTXXu(String name, String item, String time) {
        this.name = name;
        this.item = item;
        this.time = time;
    }
}

