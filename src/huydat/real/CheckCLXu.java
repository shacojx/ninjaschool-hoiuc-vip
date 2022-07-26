package huydat.real;

import java.util.ArrayList;

public class CheckCLXu {
    public String name;
    public String item;
    public String time;

    public static ArrayList<CheckCLXu> checkCLXuArrayList = new ArrayList<>();

    public CheckCLXu(String name, String item, String time) {
        this.name = name;
        this.item = item;
        this.time = time;
    }
}

