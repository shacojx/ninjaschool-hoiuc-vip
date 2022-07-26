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
 * @author Tien
 */
public class TaiXiu {
    public int totalxiu;
    public int totaltai;
    
    public short playertai;
    public short playerxiu;
    
    public long time;
    public long timewait;
    
    public boolean start;
    public boolean tai;
    public boolean xiu;
    
    public long tongrandom;
    public String taiorxiu;
    
    public ArrayList<Char> teamtai;
    public ArrayList<Char> teamxiu;
    
    // Khởi tạo
    public TaiXiu() {
        this.totaltai = 0;
        this.totalxiu = 0;
        this.playertai = 0;
        this.playerxiu = 0;
        this.teamtai = new ArrayList<Char>();
        this.teamxiu = new ArrayList<Char>();
        this.tai = false;
        this.xiu = false;
        this.time = 60;
        this.start = true;
    }
    
    //Thông tin kết quả.
    public void InfoTaiXiu(Player p) {
        
        if (p.c.tai == false && p.c.xiu == false) {
            Server.manager.sendTB(p, "Tài Xỉu", "Tổng tiền cược Tài : " + this.totaltai + "\n"
                                          + "Tổng cược Xỉu : " + this.totalxiu + "\n"
                                          + "Thời gian : " + this.time + " giây\n"
                                          + "Kết quả phiên trước : " + this.taiorxiu + " - "+ this.tongrandom + "\n"
                                          + p.c.taixiu);
        } else {
            Server.manager.sendTB(p, "Tài Xỉu", "Tổng tiền cược Tài : " + this.totaltai + "\n"
                                          + "Tổng cược Xỉu : " + this.totalxiu + "\n"
                                          + "Thời gian : " + this.time + " giây\n"
                                          + "Kết quả phiên trước : " + this.taiorxiu + " - "+ this.tongrandom + "\n"
                                          + "Bạn đặt cược " + p.c.jointx + " lượng vào " + p.c.taixiu);
        }
    }
    
    //random để lấy kết quả
    private void random() {
        long a = Util.nextInt(1,6);
        long b = Util.nextInt(1,6);
        long c = Util.nextInt(1,6);
        
        this.tongrandom = a+b+c;
        if (3 <= this.tongrandom && this.tongrandom <= 10) {
            this.taiorxiu = "Xỉu";
            this.xiu = true;
            this.tai = false;
        }
        else if (this.tongrandom > 10) {
            this.taiorxiu = "Tài";
            this.tai = true;
            this.xiu = false;
        }
        SoiCau.soicau.add(new SoiCau("Kết quả : " + taiorxiu,"Số : " + this.tongrandom ,Util.toDateString(Date.from(Instant.now()))));
        //      Service.chatKTG( "Kết quả :  Số :" + a + " + " + b +" +  " + c + " = " + this.tongrandom + " ra " + taiorxiu  );       
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
            if (this.tai == true) {
                for (int i = 0; i< this.teamtai.size();i++) {
                    Char c = this.teamtai.get(i);
                    if (c.jointx > 0) {
                        c.p.upluongMessage(c.p.c.jointx*19/10);
                        c.jointx = 0;
                        c.tai = false;
                        c.xiu = false;
                        
                        this.tai = false;
                        c.datatx();
                    }
                }
                for (int i = 0; i< this.teamxiu.size();i++) {
                    Char c = this.teamxiu.get(i);
                    c.jointx = 0;
                    c.xiu = false;
                    c.tai = false;
                  
                    this.xiu = false;
                    c.datatx();
                }
                this.totaltai =0;
                this.teamtai.clear();
                this.totalxiu =0;
                this.teamxiu.clear();
            }
            if (this.xiu == true) {
                for (int i = 0; i< this.teamxiu.size();i++) {
                    Char c = this.teamxiu.get(i);
                    if (c.jointx > 0) {
                        c.p.upluongMessage(c.p.c.jointx*19/10);
                        c.jointx = 0;
                        c.xiu = false;
                        c.tai = false;
                       
                        this.xiu = false;
                        c.datatx();
                    }
                }
                for (int i = 0; i< this.teamtai.size();i++) {
                    Char c = this.teamtai.get(i);
                    c.jointx = 0;
                    c.tai = false;
                    c.xiu = false;
                    this.tai = false;
                   
                    c.datatx();
                }
                this.totalxiu = 0;
                this.teamxiu.clear();
                this.totaltai = 0;
                this.teamtai.clear();
            }
            if (this.timewait == 0) {
                this.time = 60;
                this.start = true;
                this.Start();
            }
        }
    }
    
    // đặt cược tài
    public void joinTai(Player p, int jointai) {
        if (this.time <= 10L) {
            p.conn.sendMessageLog("Đã hết thời gian đặt cược.");
            return;
        }
        if (jointai > p.luong) {
            p.conn.sendMessageLog("Bạn không đủ lượng.");
            return;
        }
        if (p.c.xiu == true) {
            p.conn.sendMessageLog("Bạn đã đặt xỉu.");
            return;
        }
        this.totaltai += jointai;
        p.c.jointx += jointai;
        p.upluongMessage(-jointai);
        p.c.tai = true;
        p.c.xiu = false;
        this.teamtai.add(p.c);
        p.c.datatx();
    }
    
    // đặt cược xỉu
    public void joinXiu(Player p, int joinxiu){
        if (this.time <= 10L) {
            p.conn.sendMessageLog("Đã hết thời gian đặt cược.");
            return;
        }
        if (joinxiu > p.luong) {
            p.conn.sendMessageLog("Bạn không đủ lượng.");
            return;
        }
        if (p.c.tai == true) {
            p.conn.sendMessageLog("Bạn đã đặt tài.");
            return;
        }
        this.totalxiu += joinxiu;
        p.c.jointx += joinxiu;
        p.upluongMessage(-joinxiu);
        p.c.xiu = true;
        p.c.tai = false;
        this.teamxiu.add(p.c);
        p.c.datatx();
    }
   
}
