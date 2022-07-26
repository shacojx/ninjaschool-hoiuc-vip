package huydat.stream;

import huydat.server.Server;
import huydat.real.Char;
import huydat.real.Map;
import huydat.server.Manager;

import java.util.*;

public class TuTien {
    public static TuTien tuTien = null;
    public static boolean tuTien50 = false;
    public static boolean tuTien100 = false;
    public static boolean finish;
    public static boolean start;
    public ArrayList<Char> ninjas = new ArrayList();
    public Map[] map;
    public long time;
    public int level = 0;
    public boolean rest;
    public Object LOCK = new Object();
    Server server;

    public TuTien() {
        this.server = Server.gI();
        this.map = new Map[1];
        this.rest = false;
        this.initMap();
        for(byte i = 0; i < this.map.length; ++i) {
            this.map[i].timeMap = this.time;
            this.map[i].start();
        }

    }

    private void initMap() {
        if(TuTien.tuTien50){
            this.map[0] = new Map(162, null, null, null, null, null, null, this, null);
        }
        if(TuTien.tuTien100){
            this.map[0] = new Map(163, null, null, null, null, null, null, this, null);
        }
    }


    public void rest() {
        if (!this.rest) {
            this.rest = true;
            try {
                synchronized (this) {
                    Map ma;
                    Char _char;
                    while (this.ninjas.size() > 0) {
                        _char = this.ninjas.get(0);
                        if(_char != null) {
                            _char.tileMap.leave(_char.p);
                            _char.p.restCave();
                            ma = Manager.getMapid(_char.mapLTD);
                            byte k;
                            for (k = 0; k < ma.area.length; ++k) {
                                if (ma.area[k].numplayers < ma.template.maxplayers) {
                                    ma.area[k].EnterMap0(_char);
                                    break;
                                }
                            }
                        }
                    }
                }
                byte i;
                for (i = 0; i < this.map.length; ++i) {
                    this.map[i].close();
                    this.map[i] = null;
                }
                TuTien.tuTien = null;

            } catch (Exception e) {
                byte i;
                for (i = 0; i < this.map.length; ++i) {
                    if(this.map[i] != null) {
                        this.map[i].close();
                        this.map[i] = null;
                    }
                }
                TuTien.tuTien = null;
            }
        }
    }

    public void finish() {
        synchronized (this) {
            if(!TuTien.finish) {
                TuTien.finish = true;
                TuTien.start = false;
                this.rest();
            }
        }
    }
}
