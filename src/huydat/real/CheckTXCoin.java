package huydat.real;

import java.util.ArrayList;

public class CheckTXCoin {
    public String name;
    public String item;
    public String time;

    public static ArrayList<CheckTXCoin> checkTXCoinArrayList = new ArrayList<>();

    public CheckTXCoin(String name, String item, String time) {
        this.name = name;
        this.item = item;
        this.time = time;
    }
}

