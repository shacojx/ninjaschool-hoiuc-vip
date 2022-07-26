package huydat.server;

import java.util.ArrayList;
import huydat.real.Language;
import huydat.io.Message;
import huydat.io.Util;
import huydat.io.SQLManager;
import huydat.real.Char;
import huydat.real.Player;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import huydat.io.SQLManager;
/**
 *
 * @author huy dat
 */



public class ChanLEonl {
    public int totalle;
    public int totalchan;
       public String name;
    public String item;
    public short playerchan;
    public short playerle;
    
    public long time;
    public long timewait;
    
    public boolean start;
    public boolean chan;
    public boolean le;
    
    public long tongrandom;
    public String chanorle;
    
    public ArrayList<Char> teamchan;
    public ArrayList<Char> teamle;
    
    // Khởi tạo
    public ChanLEonl() {
        this.totalchan = 0;
        this.totalle = 0;
        this.playerchan = 0;
        this.playerle = 0;
        this.teamchan = new ArrayList<Char>();
        this.teamle = new ArrayList<Char>();
        this.chan = false;
        this.le = false;
        this.time = 60;
        this.start = true;
         this.name = name;
        this.item = item;
        this.time = time;
    }

    //Thông tin kết quả.
    public void InfoChanLe(Player p) {
           int x = Util.nextInt(1, 9);
       int y = Util.nextInt(1, 9);
        int z = Util.nextInt(1, 9);
       int t = Util.nextInt(1, 9);
        if (p.c.chan == false && p.c.le == false) {
            Server.manager.sendTB(p, "Chẵn Lẻ", "Tổng tiền cược chẵn : " + this.totalchan + "\n"
                                          + "Tổng cược lẻ : " + this.totalle + "\n"
                                          + "Thời gian : " + time + " giây\n"
                                          + "Kết quả phiên trước : " + this.chanorle + " - "+ this.tongrandom + "\n"
                                          + p.c.chanle);
        } else {
            Server.manager.sendTB(p, "Chẵn Lẻ", "Tổng tiền cược chẵn : " + this.totalchan + "\n"
                                          + "Tổng cược lẻ : " + this.totalle + "\n"
                                          + "Thời gian : " + this.time + " giây\n"
                                          + "Kết quả phiên trước : " + this.chanorle + " - "+ this.tongrandom + "\n"
                                          + "Bạn đặt cược " + p.c.joincl + " lượng vào " + p.c.chanle);
        }
    }
    
    //random để lấy kết quả
    private void random() {
         int x = Util.nextInt(1, 9);
       int y = Util.nextInt(1, 9);
        int z = Util.nextInt(1, 9);
       int t = Util.nextInt(1, 9);
        
        this.tongrandom = x+y+z+t;
        if (this.tongrandom %2 != 0 ) {
            this.chanorle = "lẻ";
            this.le = true;
            this.chan = false;
        }
        else if (this.tongrandom %2 == 0) {
            this.chanorle = "chẵn";
            this.chan = true;
            this.le = false;
        }
        SoiCau.soicau.add(new SoiCau("Kết quả : " + chanorle,"Số : " + this.tongrandom ,Util.toDateString(Date.from(Instant.now()))));
   //  Service.chatKTG( "Kết quả :  Số hệ thống quay ra : "+ x +" + "+y+" + "+z+" + "+t + " = " + this.tongrandom + " ra " + chanorle  );       
    }
    
    public void Start() {
        if(this.start == true) {
            if (this.time > 0) {
                this.time -= 1;
            }
            if (this.time == 0) {
                this.start = false;
                this.timewait = 10;
                this.random();
                this.Wait();
            }
        }
    }
    
    public void Wait() {
        while(this.start == false) {
            if (this.timewait > 0) {
                this.timewait -= 1;
            }
            if (this.chan == true) {
                for (int i = 0; i< this.teamchan.size();i++) {
                    Char c = this.teamchan.get(i);
                    if (c.jointx > 0) {
                        c.p.upluongMessage(c.p.c.jointx*19/10);
                        c.jointx = 0;
                        c.chan = false;
                        c.le = false;
                        
                        this.chan = false;
                        c.datatx();
                    }
                }
                for (int i = 0; i< this.teamle.size();i++) {
                    Char c = this.teamle.get(i);
                    c.jointx = 0;
                    c.le = false;
                    c.chan = false;
                  
                    this.le = false;
                    c.datatx();
                }
                this.totalchan =0;
                this.teamchan.clear();
                this.totalle =0;
                this.teamle.clear();
            }
            if (this.le == true) {
                for (int i = 0; i< this.teamle.size();i++) {
                    Char c = this.teamle.get(i);
                    if (c.jointx > 0) {
                        c.p.upluongMessage(c.p.c.jointx*19/10);
                        c.jointx = 0;
                        c.le = false;
                        c.chan = false;
                       
                        this.le = false;
                        c.datatx();
                    }
                }
                for (int i = 0; i< this.teamchan.size();i++) {
                    Char c = this.teamchan.get(i);
                    c.jointx = 0;
                    c.chan = false;
                    c.le = false;
                    this.chan = false;
                   
                    c.datatx();
                }
                this.totalle = 0;
                this.teamle.clear();
                this.totalchan = 0;
                this.teamchan.clear();
            }
            if (this.timewait == 0) {
                this.time = 60;
                this.start = true;
                this.Start();
            }
        }
    }
    
    // đặt cược chan
    public void joinchan(Player p, int joinchan) {
        if (this.time <= 10L) {
            p.conn.sendMessageLog("Đã hết thời gian đặt cược.");
            return;
        }
        if (joinchan > p.luong) {
            p.conn.sendMessageLog("Bạn không đủ lượng.");
            return;
        }
        if (p.c.le == true) {
            p.conn.sendMessageLog("Bạn đã đặt lẻ.");
            return;
        }
        this.totalchan += joinchan;
        p.c.jointx += joinchan;
        p.upluongMessage(-joinchan);
        p.c.chan = true;
        p.c.le = false;
        this.teamchan.add(p.c);
        p.c.datatx();
    }
    
    // đặt cược xỉu
    public void joinle(Player p, int joinle){
        if (this.time <= 10L) {
            p.conn.sendMessageLog("Đã hết thời gian đặt cược.");
            return;
        }
        if (joinle > p.luong) {
            p.conn.sendMessageLog("Bạn không đủ lượng.");
            return;
        }
        if (p.c.chan == true) {
            p.conn.sendMessageLog("Bạn đã đặt chẵn.");
            return;
        }
        this.totalle += joinle;
        p.c.jointx += joinle;
        p.upluongMessage(-joinle);
        p.c.xiu = true;
        p.c.chan = false;
        this.teamle.add(p.c);
        p.c.datatx();
    }
   
}
