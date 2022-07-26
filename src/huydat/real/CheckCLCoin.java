package huydat.real;

import java.util.ArrayList;

public class CheckCLCoin {
    public String name;
    public String item;
    public String time;

    public static ArrayList<CheckCLCoin> checkCLCoinArrayList = new ArrayList<>();

    public CheckCLCoin(String name, String item, String time) {
        this.name = name;
        this.item = item;
        this.time = time;
    }
}

