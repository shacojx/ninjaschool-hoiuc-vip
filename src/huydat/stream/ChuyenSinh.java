/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package huydat.stream;

import huydat.server.Server;
import huydat.real.Char;
import huydat.real.Map;
import huydat.server.Manager;
import java.util.ArrayList;

/**
 *
 * @author Admin
 */
public class ChuyenSinh {
    public static ChuyenSinh chuyenSinh = null;
    public static boolean finish;
    public static boolean start;
    public ArrayList<Char> ninjas = new ArrayList();
    public Map[] map;
    public long time;
    public int level = 0;
    public boolean rest;
    public Object LOCK = new Object();
    Server server;
    
    public ChuyenSinh() {
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
        this.map[0] = new Map(162, null, null, null, null, null, null, null, this);
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
                ChuyenSinh.chuyenSinh = null;

            } catch (Exception e) {
                byte i;
                for (i = 0; i < this.map.length; ++i) {
                    if(this.map[i] != null) {
                        this.map[i].close();
                        this.map[i] = null;
                    }
                }
                ChuyenSinh.chuyenSinh = null;
            }
        }
    }

    public void finish() {
        synchronized (this) {
            if(!ChuyenSinh.finish) {
                ChuyenSinh.finish = true;
                ChuyenSinh.start = false;
                this.rest();
            }
        }
    }
    
    
}
