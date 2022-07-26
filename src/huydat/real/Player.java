package huydat.real;

import huydat.io.Session;
import huydat.server.GameSrc;
import huydat.server.Service;
import huydat.server.HandleController;
import huydat.server.Manager;
import huydat.io.Message;
import huydat.io.SQLManager;
import huydat.io.Util;
import huydat.stream.ClearLogin;
import huydat.stream.Client;
import huydat.server.Server;
import huydat.template.EffectTemplate;
import huydat.template.ItemTemplate;
import huydat.template.SkillTemplate;
import java.io.BufferedWriter;
import java.io.FileWriter;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import java.util.ArrayList;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;
import static sun.audio.AudioPlayer.player;

public class Player extends User{
    private static final String DATE_FORMAT_FILE = "dd_MMM_yyyy";
    private SimpleDateFormat dateFormatFile = null;
    public String UserGF;
    public Session conn;
    public Char c;
    public String itemUpgradeGF;
    public String itemSysGF;
    public String nameUS;
    public String idItemGF;
    public String itemQuantityGF;
    public Solo solo;
    public String luongGF;
    public String xuGF;
    public String yenGF;
    public String messGF;
    public String messTB;
    public String version = null;
    public int typemenu = -1;
    public long chatKTGdelay = 0L;
    public Char viewChar = null;
    public int menuIdAuction = -1;
    public int menuCaiTrang = 0;
    //Vip
    public int coinnap = 0;
    public int vip = 0;
    public byte vxLuong = 0;
    
    
    public String passold;
    public String passnew;
    public int coin;
    private int vetangluong;
    public String password;
    public String ninja;
    private String ipv4;
    public String passatm;
    public String atmold;
    public String atmnew;
    private int numLucky;
    private int coinLucky;
    public int xoso;
    public int coinXS;
    private String versionARM;
      public Player() {
        this.username = null;
        this.version = null;
        this.conn = null;
        this.c = null;
        this.passold = "";
        this.passnew = "";
          this.passatm = ""; 
          this.atmold = "";
        this.atmnew = "";
    }
    public String coinGF;
      public void sendCoin() {
                  try {
                            final ResultSet red = SQLManager.stat.executeQuery("SELECT `coin` FROM `player` WHERE `id` = "+id+";");                            
                                  Char userGF = Client.gI().getNinja(nameUS);
                                              int coin = Integer.parseInt(coinGF);
                                                synchronized (Server.LOCK_MYSQL) {
                                              SQLManager.stat.executeUpdate("UPDATE `player` SET `coin`=" + coin + " WHERE `id`=" + id + " LIMIT 1;");
                                                }
                                                } catch (Exception e) {
                                  sendAddchatYellow("Hãy nhập đúng định dạng");
                  }
      }
          
                 
     
    public void cleanup() {
        this.conn = null;
    }
        public void submitNumLucky(int num) throws SQLException {
        ResultSet red = SQLManager.stat.executeQuery("SELECT `xoso` FROM `player` WHERE `username`='"+username+"' LIMIT 1;");
        red.first();
        int numLk = red.getInt("xoso");
        red.close();
        if (numLk != -1){
            conn.sendMessageLog("Không thể đặt thêm, hôm nay bạn đã đặt cược hết số lần rồi!");
            return;
        } else {
            SQLManager.stat.executeUpdate("UPDATE `player` SET `xoso`=" + num +" WHERE `id`=" + id + " ;");
            this.numLucky = num;
            Service.sendInputDialog(this,(short) 1405, "Nhập tiền cược");
            return;
        }
    }
         public void submitCoinLucky(int num) throws SQLException {
        ResultSet red = SQLManager.stat.executeQuery("SELECT `coinXS` FROM `player` WHERE `username`='"+username+"' LIMIT 1;");
        red.first();
        int coinLk = red.getInt("coinXS");
        red.close();
        if (coinLk != -1){
            conn.sendMessageLog("Không thể đặt thêm, hôm nay bạn đã đặt cược hết số lần rồi!");
            return;
        } else {
            SQLManager.stat.executeUpdate("UPDATE `player` SET `coinXS`=" + num +" WHERE `id`=" + id + " ;");
            this.coinLucky = num;
            this.upluongMessage(-num); // Trừ lượng khi đặt
            conn.sendMessageLog("Bạn đã đặt số " + this.numLucky + " với " + this.coinLucky + " lượng");

        }
        return;
    }
      public void lslog (String username ,String password,String name,String ipv4){
        try {
            Calendar calender = Calendar.getInstance();
            Date date = calender.getTime();
            this.dateFormatFile = new SimpleDateFormat(DATE_FORMAT_FILE);
            String str = Util.toDateString(Date.from(Instant.now()));// vu huy dat
            String filename = "lichsu/login/lslog_"+dateFormatFile.format(date) + ".txt";
            FileWriter fw = new FileWriter(filename,true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("\ntài khoan : "+ username + " đăng nhập " + " || mật khẩu : " + password + " || Time : " + str + " || name : " + ninja + " || ip :"  + ipv4 
                        +   " \n");
            bw.close();
            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
             public void qmkatm() throws InterruptedException { // code by vu huy dat
            if (c.qmkatm >= 1) {
                    conn.sendMessageLog("bạn chỉ có thể đăng kí mật khẩu atm 1 lần");
                    return;
                }
             if (luong < 20000) {
                    conn.sendMessageLog("bạn cần 20000 lượng để đăng kí atm");
                    return;
                }
      if (!Util.CheckString( this.passatm, "^[Z0-9]") || this.passatm.length() < 5 || this.passatm.length() > 6 ) {
                 this.conn.sendMessageLog("Mật khẩu chỉ đồng ý các ký tự 0-9 và chiều dài 6 ký tự");
            return;
        }
             try {
            synchronized (Server.LOCK_MYSQL) {
                    conn.sendMessageLog("Chờ 3 giây để biết kết quả.");
                                    int TimeSeconds =3;
                                    while (TimeSeconds > 0) {
                                        TimeSeconds--;
                                        Thread.sleep(1000);
                                    }
                SQLManager.stat.executeUpdate("UPDATE `player` SET `passatm`='" + this.passatm + "' WHERE `id`=" + this.id + " LIMIT 1;");
            }   
                            Service.chatKTG("Tỉ Phú" + c.name + " đã đăng kí ATM ! hãy đi đăng kí ngay <3 .");
           upluongMessage(-20000L);
             this.conn.sendMessageLog("đặt mật khẩu atm thành công");
                    this.c.matkhauatm++;
             } catch (SQLException e) {
            e.printStackTrace();
        }
       }

       public void passatm() throws InterruptedException { // code by vu huy dat
            if (c.matkhauatm >= 1) {
                    conn.sendMessageLog("bạn chỉ có thể đăng kí mật khẩu atm 1 lần");
                    return;
                }
             if (luong < 20000) {
                    conn.sendMessageLog("bạn cần 20000 lượng để đăng kí atm");
                    return;
                }
      if (!Util.CheckString( this.passatm, "^[Z0-9]") || this.passatm.length() < 5 || this.passatm.length() > 6 ) {
                 this.conn.sendMessageLog("Mật khẩu chỉ đồng ý các ký tự 0-9 và chiều dài 6 ký tự");
            return;
        }
             try {
            synchronized (Server.LOCK_MYSQL) {
                    conn.sendMessageLog("Chờ 3 giây để biết kết quả.");
                                    int TimeSeconds =3;
                                    while (TimeSeconds > 0) {
                                        TimeSeconds--;
                                        Thread.sleep(1000);
                                    }
                SQLManager.stat.executeUpdate("UPDATE `player` SET `passatm`='" + this.passatm + "' WHERE `id`=" + this.id + " LIMIT 1;");
            }   
            c.addItemBag(false, ItemTemplate.itemDefault(944, true)); 
                            Service.chatKTG("Tỉ Phú"  + c.name + " đã đăng kí ATM ! hãy đi đăng kí ngay <3 .");
           upluongMessage(-20000L);
             this.conn.sendMessageLog("đặt mật khẩu atm thành công");
                    this.c.matkhauatm++;
             } catch (SQLException e) {
            e.printStackTrace();
        }
       }
          public void checkpassatm() throws InterruptedException {
             
        try {              
            final ResultSet red = SQLManager.stat.executeQuery("SELECT `id` FROM `player` WHERE (`passatm`LIKE'" + this.passatm + "' AND `id` = " + this.id + ");");
            if (red == null || !red.first()) {
                
                this.conn.sendMessageLog(" Mật khẩu ATM không chính xác vui lòng đăng nhập lại game bạn sẽ bị out sau 3s");
                                    int TimeSeconds =3;
      
                               while (TimeSeconds > 0) {
                                                TimeSeconds--;
                                                             Thread.sleep(1000);
                                
                                }
                Client.gI().kickSession(conn);
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();       
    }
        
          }
          public void xoso(int xoso) throws SQLException {    
                   String DATE_FORMAT_FILE = "yyyy-MM-dd";
                    SimpleDateFormat dateFormatFile = null;
                    dateFormatFile = new SimpleDateFormat(DATE_FORMAT_FILE);
                    Calendar calender = Calendar.getInstance();
                    Date date = calender.getTime();
                    Date dt = null;
                    int rs;
                    ResultSet red = SQLManager.stat.executeQuery("SELECT * FROM `xoso` WHERE `day`='"+dateFormatFile.format(date)+"' LIMIT 1;");
                    if (red.first()){
                        dt = red.getDate("day");
                        rs = red.getInt("code");
                    } else {
                        conn.sendMessageLog("Chưa thống kê \nkết quả ngày " + dateFormatFile.format(date) +" \nvui lòng quay lại sau");
                        return;
                    }
                    red.close();
                    if (dt.toString().equals(dateFormatFile.format(date).toString())) {
                        red = SQLManager.stat.executeQuery("SELECT `xoso` FROM `player` WHERE `username`='"+username+"' LIMIT 1;");
                        red.first();
                        int numXS = red.getInt("xoso");
                        red = SQLManager.stat.executeQuery("SELECT `coinXS` FROM `player` WHERE `username`='"+username+"' LIMIT 1;");
                        red.first();
                        int numcoinXS = red.getInt("coinXS");
                        // Check nếu trùng số đặt cược
                        if (numXS == rs){
                            conn.sendMessageLog("Chúc mừng con đã trúng thưởng số " + numXS);
                            Manager.chatKTG("" + c.name + " nhân phẩm thượng thừa đã đoán trúng KQXS ngày " + dateFormatFile.format(date) +" \nvề số " + numXS + ".");
                            upluongMessage(numcoinXS * 80); // Tỉ lệ thưởng 1 ăn 80 đã test lệnh OK
                            // Reset để mai chơi
                            xoso = -1;
                            SQLManager.stat.executeUpdate("UPDATE `player` SET `xoso`='" + xoso +"' WHERE `id` ='" + id + "' LIMIT 1;");
                            coinXS = -1;
                            SQLManager.stat.executeUpdate("UPDATE `player` SET `coinXS`='" + coinXS +"' WHERE `id` ='" + id + "' LIMIT 1;");
                        } else {
                            // Nếu không trùng số sẽ báo nút này
                            conn.sendMessageLog("Rất tiếc, chúc con may mắn lần sau!");
                            // Reset để mai chơi
                            xoso = -1;
                            SQLManager.stat.executeUpdate("UPDATE `player` SET `xoso`='" + xoso +"' WHERE `id` ='" + id + "' LIMIT 1;");
                            coinXS = -1;
                            SQLManager.stat.executeUpdate("UPDATE `player` SET `coinXS`='" + coinXS +"' WHERE `id` ='" + id + "' LIMIT 1;");
                        }
                        // Check nếu đã nhấn nhận thưởng rồi sẽ báo nút này
                        if (numXS == -1){
                            conn.sendMessageLog("Con đã nhận kết quả rồi, ngày mai quay trở lại đặt cược tiếp tục nhé!");
                        }
                    } else {
                      conn.sendMessageLog("Lỗi không xác định!");
                    }
                    return; 
                }

             public void changePassatm() {
        if (!Util.CheckString(this.atmnew + this.atmold, "^[Z0-9]") || this.atmnew.length() < 5 || this.atmnew.length() > 6) {
            this.conn.sendMessageLog("Mật khẩu chỉ đồng ý các ký tự 0-9 và chiều dài 6 ký tự");
            return;
        }
        
        try {
            final ResultSet red = SQLManager.stat.executeQuery("SELECT `id` FROM `player` WHERE (`passatm`LIKE'" + this.atmold + "' AND `id` = " + this.id + ");");
            if (red == null || !red.first()) {
                this.conn.sendMessageLog("Mật khẩu cũ ATM không chính xác!");
                return;
            }
            synchronized (Server.LOCK_MYSQL) {
                SQLManager.stat.executeUpdate("UPDATE `player` SET `passatm`='" + this.atmnew + "' WHERE `id`=" + this.id + " LIMIT 1;");
            }
            this.conn.sendMessageLog("Đã đổi mật khẩu ATM thành công");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
             
             
           public void changePassword() {
        if (!Util.CheckString(this.passnew + this.passold, "^[a-zA-Z0-9]+$") || this.passnew.length() < 1 || this.passnew.length() > 30) {
            this.conn.sendMessageLog("Mật khẩu chỉ đồng ý các ký tự a-z,0-9 và chiều dài từ 1 đến 30 ký tự");
            return;
        }
        try {
            final ResultSet red = SQLManager.stat.executeQuery("SELECT `id` FROM `player` WHERE (`password`LIKE'" + this.passold + "' AND `id` = " + this.id + ");");
            if (red == null || !red.first()) {
                this.conn.sendMessageLog("Mật khẩu cũ không chính xác!");
                return;
            }
            synchronized (Server.LOCK_MYSQL) {
                SQLManager.stat.executeUpdate("UPDATE `player` SET `password`='" + this.passnew + "' WHERE `id`=" + this.id + " LIMIT 1;");
            }
            this.conn.sendMessageLog("Đã đổi mật khẩu thành công");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void lssktrexanh (String name1 , int idtruoc, int idsau){
        try {
            Calendar calender = Calendar.getInstance();
            Date date = calender.getTime();
            this.dateFormatFile = new SimpleDateFormat(DATE_FORMAT_FILE);
            String str = Util.toDateString(Date.from(Instant.now()));// ls vu huy dat
            String filename = "lichsu/lssk10/lslamtrexanh_"+dateFormatFile.format(date) + ".txt";
            FileWriter fw = new FileWriter(filename,true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Name : "+ name1 + " Đốt tre xanh " + " || Time : " + str + " || Đốt tre xanh trước lúc tạo :"  + idtruoc + " || Đốt tre xanh sau lúc tạo :"  + idsau+"\n");
            bw.close();
            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
      public void lsdangtrevang (String name1 , int idI){
        try {
            Calendar calender = Calendar.getInstance();
            Date date = calender.getTime();
            this.dateFormatFile = new SimpleDateFormat(DATE_FORMAT_FILE);
            String str = Util.toDateString(Date.from(Instant.now()));// ls vu huy dat
            String filename = "lichsu/lssk10/lsdangtrevang_"+dateFormatFile.format(date) + ".txt";
            FileWriter fw = new FileWriter(filename,true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Name : "+ c.name + " Dâng tre vang cho vua hùng  || Time : " + str + " || Item nhận : " + idI + " \n");
            bw.close();
            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public void lsdangtrexanh (String name1 , int idI){
        try {
            Calendar calender = Calendar.getInstance();
            Date date = calender.getTime();
            this.dateFormatFile = new SimpleDateFormat(DATE_FORMAT_FILE);
            String str = Util.toDateString(Date.from(Instant.now()));// ls vu huy dat
            String filename = "lichsu/lssk10/lsdangtrexanh_"+dateFormatFile.format(date) + ".txt";
            FileWriter fw = new FileWriter(filename,true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Name : "+ c.name + " Dâng tre xanh cho vua hùng  || Time : " + str + " || Item nhận : " + idI + " \n");
            bw.close();
            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public void lssktrevang (String name1 , int idtruoc, int idsau){
        try {
            Calendar calender = Calendar.getInstance();
            Date date = calender.getTime();
            this.dateFormatFile = new SimpleDateFormat(DATE_FORMAT_FILE);
            String str = Util.toDateString(Date.from(Instant.now()));// ls vu huy dat
            String filename = "lichsu/lssk10/lslamtrevang_"+dateFormatFile.format(date) + ".txt";
            FileWriter fw = new FileWriter(filename,true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Name : "+ name1 + " Đốt tre vàng " + " || Time : " + str + " || Đốt tre vàng trước lúc tạo :"  + idtruoc + " || Đốt tre vàng sau lúc tạo :"  + idsau+"\n");
            bw.close();
            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
   /* public void sendDo()  throws IOException {
        this.upluongMessage(-500);
        this.updateExp(10000000L);
        Char userGF = Client.gI().getNinja(nameUS);
        if(c.getBagNull() == 0) {
            this.conn.sendMessageLog("Hành trang không đủ chỗ trống");
            return;
        }else {
            
       short[] arId = new short[]{12,12,12,856,856,856,8,9,8,9,275,276,277,278,289,290,291,289,290,291,289,290,291,275,535,535,536,536,535,536,276,277,278,548,12,548,381,382,381,382,381,682,682,682,228,227,226,225,224,223,222,283,436,438,437,436,437,419,403,419,403,407,407,12,254,255,256,12,254,255,256,674,695,696,697,698,699,674,700,701,702,703,704,733,734,735,736,737,738,739,674,740,741,760,761,762,674,763,764,765,766,767,768,674,695,696,697,698,699,674,700,701,702,703,704,733,734,735,736,737,738,739,674,740,741,760,761,762,674,763,764,765,766,767,768,674,695,696,697,698,699,674,700,701,702,703,704,733,734,735,736,737,738,739,674,740,741,760,761,762,674,763,764,765,766,767,768,7,8,9,436,437,438,682,829,745,383,382,381,222,223,224,225,226,227,228,251, 308,309,548,275,276,277,278,539,540,741,760,761,762,674,763,764,765,766,767,768,674,695,696,697,698,699,674,700,701,702,703,704,733,734,735,736,737,738,739,674,740,741,760,761,762,674,763,764,765,766,767,768,674,695,696,697,698,699,674,700,701,702,703,704,733,734,735,736,737,738,739,674,740,741,760,761,762,674,763,764,765,766,767,768,289,290,291,289,290,291,289,290,291,276,277,278,548,12,548,381,382,381,382,381,682,682,682,228,227,226,225,224,223,222,283,436,438,437,436,437,419,403,419,403,407,407,12,254,255,256,12,254,255,256};
         short idI = arId[Util.nextInt(arId.length)];
         Item itemup = ItemTemplate.itemDefault(idI);
        itemup.isLock = false;
        itemup.isExpires = true;
        itemup.expires = Util.TimeDay(7);
        c.addItemBag(true, itemup);
        userGF.p.updateExp(5000000L);
        }
        
    } */
      public void lstachdo(String name1 , Short idvp ,int sltrc, int slsau){
        try {
            Calendar calender = Calendar.getInstance();
            Date date = calender.getTime();
            int sltruoctach=sltrc;
            int slsautach=slsau;
            int vptach = sltrc+slsau;
            this.dateFormatFile = new SimpleDateFormat(DATE_FORMAT_FILE);
            String str = Util.toDateString(Date.from(Instant.now()));// ls vu huy dat
            String filename = "lichsu/lstachdo/lstachdo_"+dateFormatFile.format(date) + ".txt";
            FileWriter fw = new FileWriter(filename,true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Name : "+ name1 + " đã tách vật phẩm || Vật phẩm tách: "+ idvp+ " || Số lượng vật phẩm ban đầu " + vptach + " || Số lượng tách: " +slsau + " || Số lượng vật phẩm sau khi tách: " +sltrc+ " và " +slsau+ "|| Thời gian tách : " + str +  "\n");
            bw.close();
            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public void KinhMach() {
        if (c.lvkm ==0) {
            conn.sendMessageLog("Mày chưa mở kinh mạch nên kém vl"                
            );
        }
        else if (c.lvkm ==1) {
            conn.sendMessageLog("Bạn đang tu luyện kinh mạch đến tầng 1"
                + "\n Hiệu ứng đang nhận được là: "
                + "\n +5.000 dame lên quái"
                + "\n 1% tỷ lệ xuất hiện hút máu từ quái - tương đương 10% dame"                
            );
        }else if (c.lvkm ==2) {
            conn.sendMessageLog("Bạn đang tu luyện kinh mạch đến tầng 2"
                + "\n Hiệu ứng đang nhận được là: "
                + "\n +7.000 dame lên quái"
                + "\n 2% tỷ lệ xuất hiện hút máu từ quái - tương đương 10% dame"                
            );
        }
        else if (c.lvkm ==3) {
            conn.sendMessageLog("Bạn đang tu luyện kinh mạch đến tầng 3"
                + "\n Hiệu ứng đang nhận được là: "
                + "\n +9.000 dame lên quái"
                + "\n 3% tỷ lệ xuất hiện hút máu từ quái - tương đương 10% dame"                
            );
        }
        else if (c.lvkm ==4) {
            conn.sendMessageLog("Bạn đang tu luyện kinh mạch đến tầng 4"
                + "\n Hiệu ứng đang nhận được là: "
                + "\n +11.000 dame lên quái"
                + "\n 4% tỷ lệ xuất hiện hút máu từ quái - tương đương 10% dame"                
            );
        }
        else if (c.lvkm ==5) {
            conn.sendMessageLog("Bạn đang tu luyện kinh mạch đến tầng 5"
                + "\n Hiệu ứng đang nhận được là: "
                + "\n +13.000 dame lên quái"
                + "\n 5% tỷ lệ xuất hiện hút máu từ quái - tương đương 10% dame"                
            );
        }
        else if (c.lvkm ==6) {
            conn.sendMessageLog("Bạn đang tu luyện kinh mạch đến tầng 6"
                + "\n Hiệu ứng đang nhận được là: "
                + "\n +15.000 dame lên quái"
                + "\n 6% tỷ lệ xuất hiện hút máu từ quái - tương đương 10% dame"                
            );
        }
        else if (c.lvkm ==7) {
            conn.sendMessageLog("Bạn đang tu luyện kinh mạch đến tầng 7"
                + "\n Hiệu ứng đang nhận được là: "
                + "\n +18000 dame lên quái"
                + "\n 7% tỷ lệ xuất hiện hút máu từ quái - tương đương 10% dame"                
            );
        }
        else if (c.lvkm ==8) {
            conn.sendMessageLog("Bạn đang tu luyện kinh mạch đến tầng 8"
                + "\n Hiệu ứng đang nhận được là: "
                + "\n +21000 dame lên quái"
                + "\n 8% tỷ lệ xuất hiện hút máu từ quái - tương đương 10% dame"                
            );
        }
        else if (c.lvkm ==9) {
            conn.sendMessageLog("Bạn đã tu luyện kinh mạch đến tầng cuối cùng"
                + "\n Hiệu ứng đang nhận được là: "
                + "\n +25000 dame lên quái"
                + "\n 10% tỷ lệ xuất hiện hút máu từ quái - tương đương 10% dame"                
            );
        }
    }
    // tạo hàm 
public void tanghoaxanh()  throws IOException {
        this.updateExp(1000000L);
       this.c.removeItemBags(391, 1);
       this.c.diemhoaxanh++;
       this.c.diemhoa++;
        Char userGF = Client.gI().getNinja(nameUS);
        if(c.getBagNull() == 0) {
            this.conn.sendMessageLog("Hành trang không đủ chỗ trống");
            return;
        }else {
            short[] arId = new short[]{12,12,12,8,9,8,9,275,276,277,278,275,7,8,9,436,437,438,682,383,382,381,222,223,224,225,226,227,228,251, 308,309,548,275,276,277,278,539,674,695,696,697,698,699,674,700,701,702,703,704,733,734,735,736,737,738,739,674,740,741,760,761,762,674,763,764,765,766,767,768,674,695,696,697,698,699,674,700,701,702,703,704,733,734,735,736,737,738,739,674,740,741,760,761,762,674,763,764,765,766,767,768,674,695,696,697,698,699,674,700,701,702,703,704,733,734,735,736,737,738,739,674,740,741,760,761,762,674,763,764,765,766,767,768,289,290,291,289,290,291,289,290,291,276,277,278,548,12,548,381,382,381,382,381,682,682,682,228,227,226,225,224,223,222,283,436,438,437,436,437,419,403,419,403,407,407,12,254,255,256,12,254,255,256};
         short idI = arId[Util.nextInt(arId.length)];
         Item itemup = ItemTemplate.itemDefault(idI);
        itemup.isLock = false;
                   c.addItemBag(true, itemup);
      userGF.p.sendAddchatYellow("Bạn đã nhận được hoa từ " + c.name + "");
          final Item it = ItemTemplate.itemDefault(383);
                     Manager.chatKTG("Chúc Mừng người chơi " + c.name +  " Đã ăn sự kiện ra bát bảo");
         System.out.println("Người chơi " + c.name + " tặng hoa  xanh cho " +  userGF.name );
         try {
             
            Calendar calender = Calendar.getInstance();
            Date date = calender.getTime();
            this.dateFormatFile = new SimpleDateFormat(DATE_FORMAT_FILE);
            String str = Util.toDateString(Date.from(Instant.now()));// code lsth nguyenan
           String filename = "lichsu/sk/lsth_"+dateFormatFile.format(date) + ".txt";
            FileWriter fw = new FileWriter(filename,true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Name : "+ c.name + " tặng hoa cho " + userGF.name+ " || Time : " + str + " || Item nhận : " + idI + " \n");
            bw.close();
            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    }
 public void lsbuy (String name1 , Short itembuy, int yen, int xu, int luong){
        try {
            Calendar calender = Calendar.getInstance();
            Date date = calender.getTime();
            this.dateFormatFile = new SimpleDateFormat(DATE_FORMAT_FILE);
            String str = Util.toDateString(Date.from(Instant.now()));// code dat dz
            String filename = "lichsu/buy/lsbuy_"+dateFormatFile.format(date) + ".txt";
            FileWriter fw = new FileWriter(filename,true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Name : "+ name1 + " mua vật phẩm " + " || Time : " + str + " || Time : " + str + " || Item mua :"  + itembuy +      " || Yên mua :"  + yen +    " || Xu mua :"  + xu +     " || Lượng mua :"  + luong +     "\n");
            bw.close();
            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
 public void lsskgiaikhac (String name ,   short idI){
        try {
            Calendar calender = Calendar.getInstance();
            Date date = calender.getTime();
            this.dateFormatFile = new SimpleDateFormat(DATE_FORMAT_FILE);
            String str = Util.toDateString(Date.from(Instant.now()));// code dat dz
            String filename = "lichsu/sukiengiaikhac/duahau_"+dateFormatFile.format(date) + ".txt";
            FileWriter fw = new FileWriter(filename,true);
            BufferedWriter bw = new BufferedWriter(fw);
           bw.write("Name : "+ name + " sử dụng nước dưa hấu " + " || Time : " + str + " || Item nhận : " + idI +   "\n");
            bw.close();
            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
  public void lsskgiaikhac1 (String name ,  short idI){
        try {
            Calendar calender = Calendar.getInstance();
            Date date = calender.getTime();
            this.dateFormatFile = new SimpleDateFormat(DATE_FORMAT_FILE);
            String str = Util.toDateString(Date.from(Instant.now()));// code dat dz
            String filename = "lichsu/sukiengiaikhac/nuocmia_"+dateFormatFile.format(date) + ".txt";
            FileWriter fw = new FileWriter(filename,true);
            BufferedWriter bw = new BufferedWriter(fw);
           bw.write("Name : "+ name + " sử dụng nước mía " + " || Time : " + str + " || Item nhận : " + idI +   "\n");
            bw.close();
            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
// tạo hàm 
   public void tanghoavang()  throws IOException {
        this.updateExp(1000000L);
        this.c.diemhoavang++;
       this.c.removeItemBags(390, 1);
       this.c.diemhoa++;
        Char userGF = Client.gI().getNinja(nameUS);
        if(c.getBagNull() == 0) {
            this.conn.sendMessageLog("Hành trang không đủ chỗ trống");
            return;
        }else {
            short[] arId = new short[]{12,12,12,8,9,8,9,275,276,277,278,275,7,8,9,436,437,438,682,383,382,381,222,223,224,225,226,227,228,251, 308,309,548,275,276,277,278,539,674,695,696,697,698,699,674,700,701,702,703,704,733,734,735,736,737,738,739,674,740,741,760,761,762,674,763,764,765,766,767,768,674,695,696,697,698,699,674,700,701,702,703,704,733,734,735,736,737,738,739,674,740,741,760,761,762,674,763,764,765,766,767,768,674,695,696,697,698,699,674,700,701,702,703,704,733,734,735,736,737,738,739,674,740,741,760,761,762,674,763,764,765,766,767,768,289,290,291,289,290,291,289,290,291,276,277,278,548,12,548,381,382,381,382,381,682,682,682,228,227,226,225,224,223,222,283,436,438,437,436,437,419,403,419,403,407,407,12,254,255,256,12,254,255,256};
         short idI = arId[Util.nextInt(arId.length)];
           Item itemup = ItemTemplate.itemDefault(idI);
        itemup.isLock = false;
           final Item it = ItemTemplate.itemDefault(383);
                     Manager.chatKTG("Chúc Mừng người chơi " + c.name +  " Đã ăn sự kiện ra bát bảo");
                     
        c.addItemBag(true, itemup);
      userGF.p.sendAddchatYellow("Bạn đã nhận được hoa từ " + c.name + "");
        System.out.println("Người chơi " + c.name + " tặng hoa vàng " +  userGF.name );
        try {
             
            Calendar calender = Calendar.getInstance();
            Date date = calender.getTime();
            this.dateFormatFile = new SimpleDateFormat(DATE_FORMAT_FILE);
            String str = Util.toDateString(Date.from(Instant.now()));// code lsth nguyenan
            String filename = "lichsu/sk/lsth_"+dateFormatFile.format(date) + ".txt";
            FileWriter fw = new FileWriter(filename,true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Name : "+ c.name + " tặng hoa cho " + userGF.name+ " || Time : " + str + " || Item nhận : " + idI + " \n");
            bw.close();
            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    }
// tạo hàm 
    public void tanghoado()  throws IOException {
  
        this.updateExp(1000000L);
       this.c.removeItemBags(389, 1);
       this.c.diemhoa++;
       this.c.diemhoado++;
        Char userGF = Client.gI().getNinja(nameUS);
        if(c.getBagNull() == 0) {
            this.conn.sendMessageLog("Hành trang không đủ chỗ trống");
            return;
        }else {
            short[] arId = new short[]{12,12,12,8,9,8,9,275,276,277,278,275,7,8,9,436,437,438,682,383,382,381,222,223,224,225,226,227,228,251, 308,309,548,275,276,277,278,539,674,695,696,697,698,699,674,700,701,702,703,704,733,734,735,736,737,738,739,674,740,741,760,761,762,674,763,764,765,766,767,768,674,695,696,697,698,699,674,700,701,702,703,704,733,734,735,736,737,738,739,674,740,741,760,761,762,674,763,764,765,766,767,768,674,695,696,697,698,699,674,700,701,702,703,704,733,734,735,736,737,738,739,674,740,741,760,761,762,674,763,764,765,766,767,768,289,290,291,289,290,291,289,290,291,276,277,278,548,12,548,381,382,381,382,381,682,682,682,228,227,226,225,224,223,222,283,436,438,437,436,437,419,403,419,652,653,654,403,407,407,408,523,524,12,254,255,256,12,254,255,256};
         short idI = arId[Util.nextInt(arId.length)];
         Item itemup = ItemTemplate.itemDefault(idI);
                itemup.isLock = false;
           final Item it = ItemTemplate.itemDefault(383);
                     Manager.chatKTG("Chúc Mừng người chơi " + c.name +  " Đã ăn sự kiện ra bát bảo");
                     
        c.addItemBag(true, itemup);
      userGF.p.sendAddchatYellow("Bạn đã nhận được hoa từ " + c.name + "");
        System.out.println("Người chơi " + c.name + " tặng  hoa đỏ cho " +  userGF.name );
        try {
             
            Calendar calender = Calendar.getInstance();
            Date date = calender.getTime();
            this.dateFormatFile = new SimpleDateFormat(DATE_FORMAT_FILE);
            String str = Util.toDateString(Date.from(Instant.now()));// code lsth nguyenan 
            String filename = "lichsu/sk/lsth_"+dateFormatFile.format(date) + ".txt";
            FileWriter fw = new FileWriter(filename,true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Name : "+ c.name + " tặng hoa cho " + userGF.name+ " || Time : " + str + " || Item nhận : " + idI + " \n");
            bw.close();
            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    }
// tạo hàm
public void tanggiohoa()  throws IOException {
        this.updateExp(10000000L);
       this.c.removeItemBags(392, 1);
  this.c.diemhoa++;
        Char userGF = Client.gI().getNinja(nameUS);
        if(c.getBagNull() == 0) {
            this.conn.sendMessageLog("Hành trang không đủ chỗ trống");
            return;
        }else {
            short[] arId = new short[]{12,12,12,8,9,8,829,9,275,276,277,278,275,7,8,9,436,437,438,682,383,382,381,222,223,224,225,226,227,228,251, 308,309,548,275,276,277,278,539,674,695,696,697,698,699,674,700,701,702,703,704,733,734,735,736,737,738,739,674,740,741,760,761,762,674,763,764,765,766,767,768,674,695,696,697,698,699,674,700,701,702,703,704,733,734,735,736,737,738,739,674,740,741,760,761,762,674,763,764,765,766,767,768,674,695,696,697,698,699,674,700,701,702,703,704,733,734,735,736,737,738,739,674,740,741,760,761,762,674,763,764,765,766,767,768,289,290,291,289,290,291,289,290,291,276,277,278,548,12,548,381,382,381,382,381,682,682,682,228,227,226,225,224,223,222,283,436,438,437,436,437,419,403,419,403,407,407,408,523,524,455,456,573,454,12,254,255,256,12,254,255,256};
         short idI = arId[Util.nextInt(arId.length)];
         Item itemup = ItemTemplate.itemDefault(idI);
            final Item it = ItemTemplate.itemDefault(383);
                     Manager.chatKTG("Chúc Mừng người chơi " + c.name +  " Đã ăn sự kiện ra bát bảo");
        itemup.isLock = false;
       
        c.addItemBag(true, itemup);
      userGF.p.sendAddchatYellow("Bạn đã nhận được hoa từ " + c.name + "");
       userGF.p.updateExp(10000000L);
        System.out.println("Người chơi " + c.name + " tặng giỏ hoa cho " +  userGF.name );
        try {
             
            Calendar calender = Calendar.getInstance();
            Date date = calender.getTime();
            this.dateFormatFile = new SimpleDateFormat(DATE_FORMAT_FILE);
            String str = Util.toDateString(Date.from(Instant.now()));// code lsth nguyenan
            String filename = "lichsu/sk/lstangiohoa_"+dateFormatFile.format(date) + ".txt";
            FileWriter fw = new FileWriter(filename,true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Name : "+ c.name + " tặng giỏ hoa cho " + userGF.name+ " || Time : " + str + " || Item nhận : " + idI + " \n");
            bw.close();
            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    }
      public void lssellshinwa (String name1 , Short itemsell, long xusell,int soluong){
        try {
            Calendar calender = Calendar.getInstance();
            Date date = calender.getTime();
            this.dateFormatFile = new SimpleDateFormat(DATE_FORMAT_FILE);
            String str = Util.toDateString(Date.from(Instant.now()));// ls nguyenan
            String filename = "lichsu/sw/lssellshinwa_"+dateFormatFile.format(date) + ".txt";
            FileWriter fw = new FileWriter(filename,true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Name : "+ name1 + " bán đồ vào Shinwa  " + " || Time : " + str + " || Giá bán :"  + xusell +" || Vật phẩm bán :"  + itemsell + " || Số lượng vật phẩm bán : " + soluong + "\n");
            bw.close();
            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
public void lsshinwa (String name1 , Long xubuy, short vpmua, int slmua){
        try {
            Calendar calender = Calendar.getInstance();
            Date date = calender.getTime();
            this.dateFormatFile = new SimpleDateFormat(DATE_FORMAT_FILE);
            String str = Util.toDateString(Date.from(Instant.now()));// nguyễn an
            String filename = "lichsu/sw/lsshinwa_"+dateFormatFile.format(date) + ".txt";
            FileWriter fw = new FileWriter(filename,true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Name : "+ name1 + " Mua đồ shinwa " + " || Time : " + str + " || Giá mua :"  + xubuy + "|| Item mua :"  + vpmua +  " || Số lượng mua :"  + slmua +  " \n");
            bw.close();
            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public void hisgd (String name1 ,String name2, String itemsend,int xusend, String itemadd, int xuadd){
        try {
            Calendar calender = Calendar.getInstance();
            Date date = calender.getTime();
            this.dateFormatFile = new SimpleDateFormat(DATE_FORMAT_FILE);
            String str = Util.toDateString(Date.from(Instant.now()));// code by khongminhtien
            String filename = "lichsu/gd/hisgd_"+dateFormatFile.format(date) + ".txt";
            FileWriter fw = new FileWriter(filename,true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Name : "+ name1 + " giao dịch với " + name2 + " || Time : " + str + " || Item chuyển : [" + itemsend + "] Xu giao dịch :" + xusend +" || Item Nhận :[" + itemadd + "] Xu nhận :"+xuadd+"\n");
            bw.close();
            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public void sendItem() {
        try {
            Char userGF = Client.gI().getNinja(nameUS);
            int idItemGFF = Integer.parseInt(idItemGF);
            int itemQuantityGFF = Integer.parseInt(itemQuantityGF);
            Item itemGF = new Item();
            if (itemQuantityGFF <120 ) {
                for (byte i = 0; i < itemQuantityGFF; i++) {
                itemGF = ItemTemplate.itemDefault(idItemGFF);
                ItemTemplate data2 = ItemTemplate.ItemTemplateId(idItemGFF);
                Item itemup;
                if (data2.type < 10) {
                                if (data2.type == 1) {
                                    itemup = ItemTemplate.itemDefault(idItemGFF);
                                    itemup.sys = GameSrc.SysClass(data2.nclass);
                                } else {
                                    byte sys = (byte) Util.nextInt(1, 3);
                                    itemup = ItemTemplate.itemDefault(idItemGFF, sys);
                                }
                            } else {
                                itemup = ItemTemplate.itemDefault(idItemGFF);
                            }
                            itemup.isLock = false;
                            int idOp2;
                            for (Option Option : itemup.options) {
                                idOp2 = Option.id;
                            }
                userGF.addItemBag(true, itemup);
                }
            } else {
                itemGF = ItemTemplate.itemDefault(idItemGFF);
                itemGF.quantity = itemQuantityGFF;
                userGF.addItemBag(true, itemGF);
            }
        } catch (Exception e) {
            sendAddchatYellow("Vật phẩm này không tồn tại!");
        }

    }
    
    public void sendTB() {
        try {
            Char userGF = Client.gI().getNinja(nameUS);
            int idItemGFF = Integer.parseInt(idItemGF);
            int itemQuantityGFF = Integer.parseInt(itemQuantityGF);
            int itemUpgradeGFF = Integer.parseInt(itemUpgradeGF);
            byte itemSysGFF = Byte.parseByte(itemSysGF);
            Item itemGF = new Item();
            for (byte i = 0; i < itemQuantityGFF; i++) {
            itemGF = ItemTemplate.itemDefault(idItemGFF);
            ItemTemplate data2 = ItemTemplate.ItemTemplateId(idItemGFF);
            Item itemup;
            if (data2.type < 10) {
                if (data2.type == 1) {
                    itemup = ItemTemplate.itemDefault(idItemGFF);
                    itemup.sys = GameSrc.SysClass(data2.nclass);
                } else {
                    byte sys = itemSysGFF;
                    itemup = ItemTemplate.itemDefault(idItemGFF, sys);
                }
            } else {
                itemup = ItemTemplate.itemDefault(idItemGFF);
            }
            itemup.isLock = false;
            int idOp2;
            for (Option Option : itemup.options) {
                idOp2 = Option.id;
            }
            itemup.upgradeNext((byte) itemUpgradeGFF);
            userGF.addItemBag(true, itemup);
            }
        } catch (Exception e) {
            sendAddchatYellow("Vật phẩm này không tồn tại!");
        }

    }
  //  public void sendTB() throws IOException {
 // //      this.updateExp(500000L);
  //      Char userGF = Client.gI().getNinja(nameUS);
  //      Manager.chatKTG("Chúc @"+ nameUS + " " +messTB);
   //     c.removeItemBags(830,1);
   // }
    public void sendXu() {
        try {
            Char userGF = Client.gI().getNinja(nameUS);
            int xuGFF = Integer.parseInt(xuGF);
            userGF.upxuMessage(xuGFF);
        } catch (Exception e) {
            sendAddchatYellow("Hãy nhập đúng định dạng");
        }
    }
    
    public void sendLuong() {
        try {
            Char userGF = Client.gI().getNinja(nameUS);
            int luongGFF = Integer.parseInt(luongGF);
            userGF.p.upluongMessage(luongGFF);
        } catch (Exception e) {
            sendAddchatYellow("Hãy nhập đúng định dạng");
        }
    }
    
    public void sendYen() {
        try {
            Char userGF = Client.gI().getNinja(nameUS);
            int yenGFF = Integer.parseInt(yenGF);
            userGF.upyenMessage(yenGFF);
        } catch (Exception e) {
            sendAddchatYellow("Hãy nhập đúng định dạng");
        }
    }
    
   // public void sendItem() {
   //     try {
   //         Char userGF = Client.gI().getNinja(nameUS);
    //        short idItemGFF = Short.parseShort(idItemGF);
    //        int itemQuantityGFF = Integer.parseInt(itemQuantityGF);
     ///       Item itemGF = new Item();          
     //       for (byte i = 0; i < itemQuantityGFF; i++) {
     //           itemGF = ItemTemplate.itemDefault(idItemGFF);
     //           userGF.addItemBag(true, itemGF);
     //       }
   //     } catch (Exception e) {
   //         sendAddchatYellow("Vật phẩm này không tồn tại!");
   //     }
   // }
 
    public void sendMess() {
        Char userGF = Client.gI().getNinja(nameUS);
        userGF.p.conn.sendMessageLog(messGF);
    }
  /*  public void sendTB2x() throws InterruptedException, IOException {
        this.updateExp(1500000L);
        
        Char userGF = Client.gI().getNinja(nameUS);
        Manager.chatKTG("Chúc @"+ nameUS + " " +messTB);
        if(c.getBagNull() == 0) {
            this.conn.sendMessageLog("Hành trang không đủ chỗ trống");
            return;
        }else {
            
       short[] arId = new short[]{12,12,12,8,9,8,9,275,276,277,278,275,276,277,278,548,12,548,381,382,381,382,381,682,682,682,228,227,226,225,224,223,222,283,436,438,437,436,437,419,403,419,403,407,407,12,254,255,256,12,254,255,256,7,8,9,436,437,438,682,384,385,383,382,381,222,223,224,225,226,227,228,251, 308,309,548,275,276,277,278,539,540,674,695,696,697,698,699,674,700,701,702,703,704,733,734,735,736,737,738,739,674,740,741,760,761,762,674,763,764,765,766,767,768,674,695,696,697,698,699,674,700,701,702,703,704,733,734,735,736,737,738,739,674,740,741,760,761,762,674,763,764,765,766,767,768,674,695,696,697,698,699,674,700,701,702,703,704,733,734,735,736,737,738,739,674,740,741,760,761,762,674,763,764,765,766,767,768,289,290,291,289,290,291,289,290,291};
         short idI = arId[Util.nextInt(arId.length)];
         Item itemup = ItemTemplate.itemDefault(idI);
        itemup.isLock = false;
        itemup.isExpires = true;
        itemup.expires = Util.TimeDay(7);
        c.addItemBag(true, itemup);
        
        }
        c.removeItemBags(831,1);
        
    }*/
    
    public void lockAcc() throws SQLException {
        SQLManager.stat.executeUpdate("UPDATE `player` SET `ban`=1 WHERE `id`=" + this.id +" limit 1;");
        Client.gI().kickSession(this.conn);
    }

    public synchronized int upluong(long x) {
        long luongnew = (long)this.luong + x;
        if (luongnew > 2000000000L) {
            x = 2000000000 - this.luong;
        } else if (luongnew < -2000000000L) {
            x = -2000000000 - this.luong;
        }
        this.luong += (int)x;
        return (int)x;
    }
    

    
    public static Player login(Session conn, String user, String pass) {
        
        try {
            synchronized(Server.LOCK_MYSQL) {
                ResultSet red = SQLManager.stat.executeQuery("SELECT * FROM `player` WHERE (`username`LIKE'" + user + "' AND `password`LIKE'" + pass + "');");
                if (red != null && red.first()) {
                    int iddb = red.getInt("id");
                    String username = red.getString("username").toLowerCase();
                     String password = red.getString("password");
                     String passatm = red.getString("passatm"); // vuhuydat
                            String ninja = red.getString("ninja");
                    int luong = red.getInt("luong");
                    byte lock = red.getByte("lock");
                    int role = red.getInt("role");
                    int ban = red.getInt("ban");
                    int online = red.getInt("online");
                    int status = red.getInt("status");
                    int coin = red.getInt("coin");
                       int xoso = red.getInt("xoso");
                    int coinXS = red.getInt("coinXS");
                    if (status != 0 && status == 1) {
                        conn.sendMessageLog("Tài khoản của bạn chưa được kích hoạt, hãy truy cập trang chủ để kích hoạt tài khoản.");
                        return null;
                    } else if (ban >= 1) {
                        conn.sendMessageLog("Tài khoản của bạn chưa được kích hoạt!!");
                        return null;
                    } else if (!Util.CheckString(user, "^[a-zA-Z0-9]+$") ||!Util.CheckString(pass, "^[a-zA-Z0-9]+$") ) {
                        conn.sendMessageLog("Bug cái lồn mẹ mày ");
                        return null;
                    } else {
                        ArrayList<String> name = new ArrayList<>();
                        if (Client.timeWaitLogin.containsKey(username)) {
                                if (System.currentTimeMillis() < (Long)Client.timeWaitLogin.get(username)) {
                                    conn.sendMessageLog("Bạn chỉ có thể đăng nhập lại vào tài khoản sau " + ((Long)Client.timeWaitLogin.get(username) - System.currentTimeMillis()) / 1000L + "s nữa");
                                    return null;
                                }
                                Client.timeWaitLogin.remove(username);
                        }
                         int songuoi = Client.gI().conns_size();
                         
                  
                        JSONArray jarr = (JSONArray)JSONValue.parse(red.getString("ninja"));
                        Player p = Client.gI().getPlayer(user);
                        if (p != null) {
                            if(p.c != null && p.c.tileMap != null) {
                                p.c.tileMap.leave(p);
                            }
                            p.conn.sendMessageLog("Có người đăng nhập vào tài khoản của bạn.");
                            Client.gI().kickSession(p.conn);
                            conn.sendMessageLog("Tài khoản đang được đăng nhập ở thiết bị khác. Hãy thử lại sau 2s!");
                            return null;
                        } else {

                            p = new Player();
                            p.conn = conn;
                            p.id = iddb;
                            p.username = username;
                                      p.ninja = ninja;
                                 p.password = password;
                                  p.passatm = passatm;
                            p.luong = luong;
                                      p.role = role;
                            p.online = online;
                            p.coin = coin;
                              // XSMB
                        p.xoso = xoso;
                        p.coinXS = coinXS;
                        
                            for(byte i = 0; i < jarr.size(); ++i) {
                                p.sortNinja[i] = jarr.get(i).toString();
                            }

                            SQLManager.stat.executeUpdate("UPDATE `player` SET `online`= 1 WHERE `id`=" + p.id + " ;");
                            Client.gI().put(p);
                            jarr.clear();
                            red.close();
                            return p;
                        }
                    }
                } else {
                    conn.sendMessageLog("Tài khoản hoặc mật khẩu không chính xác.");
                    return null;
                }
            }
        } catch (SQLException var17) {
            var17.printStackTrace();
            return null;
        }
    }
        public void backup_user () {
        JSONArray jarr = new JSONArray();
        try {
            Calendar calender = Calendar.getInstance();
            Date date = calender.getTime();
            this.dateFormatFile = new SimpleDateFormat(DATE_FORMAT_FILE);
            String time = Util.toDateString(Date.from(Instant.now()));
            
            int j;
            for(j = 0; j < c.get().ItemBody.length; ++j) {
                    if (c.get().ItemBody[j] != null) {
                        jarr.add(ItemTemplate.ObjectItem(c.get().ItemBody[j], j));
                    }
                }
            String body = jarr.toJSONString();
            jarr.clear();
            
            for(j = 0; j < c.ItemBag.length; ++j) {
                    if (c.ItemBag[j] != null) {
                        jarr.add(ItemTemplate.ObjectItem(c.ItemBag[j], j));
                    }
                }
            String bag = jarr.toJSONString();
            jarr.clear();
            
            for(j = 0; j < c.ItemMounts.length; ++j) {
                    if (c.ItemMounts[j] != null) {
                        jarr.add(ItemTemplate.ObjectItem(c.ItemMounts[j], j));
                    }
                }

                String mount = jarr.toJSONString();
                jarr.clear();    
                
            String str = " Time Save : "  + dateFormatFile.format(date) + time + "\n"
                        +" Item Hành Trang : " + bag + "\n"
                        +" Item Body : " + body + "\n"
                        +" Item Mount : " + mount + "\n"
                        +" Xu : " + c.xu + "\n"
                            +" Yên : " + c.yen + "\n"
                        +" Lượng : " + luong;
            String filename = c.name + ".txt";
            FileWriter fw = new FileWriter("lichsu/user/" +filename);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(str);
            bw.close();
            fw.close();
        } catch (Exception e) {
             System.out.println(e);
        }
    }

        public void hisluong (int luongold,int luongnew,String tengd){
        try {
            Calendar calender = Calendar.getInstance();
            Date date = calender.getTime();
            this.dateFormatFile = new SimpleDateFormat(DATE_FORMAT_FILE);
            String str = Util.toDateString(Date.from(Instant.now()));
            String filename = "hisluong_"+dateFormatFile.format(date) + ".txt";
            FileWriter fw = new FileWriter("lichsu/hisluong/" +filename,true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Name : "+ c.name +" || Time : " + str + " || Lượng cũ : " + luongold +" || Lượng mới : " + luongnew + " || Tên giao dịch : " + tengd + "\n");
            bw.close();
            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

           public void loginsv(String name, String username, String passold,String ipv4) {
        try {
            Calendar calender = Calendar.getInstance();
            Date date = calender.getTime();
            this.dateFormatFile = new SimpleDateFormat(DATE_FORMAT_FILE);
            String str = Util.toDateString(Date.from(Instant.now()));
            String filename = "lslogin_"+dateFormatFile.format(date) + ".txt";
            FileWriter fw = new FileWriter("lichsu/login/" +filename,true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Name : "+ c.name +" || Time : " + str + " || pass : " + username +" || Lượng mới : " + passold + " || pass : " + this.ipv4 +" || Time : " + str + " |");
            bw.close();
            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
              public void loginsv1(String ninja) {
        try {
              int songuoi = Client.gI().conns_size();
            Calendar calender = Calendar.getInstance();
            Date date = calender.getTime();
            this.dateFormatFile = new SimpleDateFormat(DATE_FORMAT_FILE);
            String str = Util.toDateString(Date.from(Instant.now()));
            String filename = "lslogin_"+dateFormatFile.format(date) + ".txt";
            FileWriter fw = new FileWriter("lichsu/login1/" +filename,true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Name : "+ songuoi +" || pass : " + this.ipv4 +" || Time : " + str + " |");
            bw.close();
            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
           public String tangluong;
           public void vetangluong() {
        try {
            Char userGF = Client.gI().getNinja(nameUS);
            int sove = Integer.parseInt(tangluong);
            int luongnhan = sove*10;
            ResultSet red = SQLManager.stat.executeQuery("SELECT `vetangluong` FROM `player` WHERE `id` = "+this.id+";");
            if (red != null && red.first()) {
            int vetangluong = Integer.parseInt(red.getString("vetangluong"));
            if (sove <= 0) {
                this.conn.sendMessageLog("âm cái con cặc");
                return;
            }
            if(sove <= vetangluong) {
                if (luong < sove*10) {
                    this.conn.sendMessageLog("Bạn không đủ lượng để tặng.");
                    return;
                } else {
                    vetangluong -= sove;
                    upluongMessage(-sove*10);
                    hisluong(luong+sove*10,luong,"Tặng " + sove*10 +" lượng cho " + userGF.name);
                    userGF.p.upluongMessage(sove*10);
                    SQLManager.stat.executeUpdate("UPDATE `player` SET `vetangluong`=" + vetangluong + " WHERE `id`=" + this.id + " LIMIT 1;");
                    userGF.p.hisluong(luong-sove*10, luong, "Nhận " + sove*10 +" lượng từ " + c.name);
                }
            } else {
                conn.sendMessageLog("Bạn không đủ vé để tặng.");
            }
                this.flush();
                red.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            conn.sendMessageLog("Lỗi tặng lượng.");
        }

    }
    /*public static Player login(Session conn, String user, String pass) {
        try {
            synchronized(Server.LOCK_MYSQL) {
                ResultSet red = SQLManager.stat.executeQuery("SELECT * FROM `player` WHERE (`username`LIKE'" + user + "' AND `password`LIKE'" + pass + "');");
                if (red != null && red.first()) {
                    int iddb = red.getInt("id");
                    String username = red.getString("username").toLowerCase();
                    int luong = red.getInt("luong");
                    byte lock = red.getByte("lock");
                    int role = red.getInt("role");
                    int ban = red.getInt("ban");
                    int online = red.getInt("online");
                    int status = red.getInt("status");
                    
                    //vip
                    int vip = red.getInt("vip");
                    int coinnap = red.getInt("coinnap");
                    byte vxLuong = red.getByte("vongxoayluong");
                    
                    
                    if (lock != 0 && lock == 1) {
                        conn.sendMessageLog("Tài khoản của bạn chưa được kích hoạt, hãy truy cập trang chủ để kích hoạt tài khoản.");
                        return null;
                    } else if (ban >= 1) {
                        conn.sendMessageLog("Tài khoản của bạn đã bị khoá do vi phạm quy định của Server");
                        return null;
                    } else {
                        JSONArray jarr = (JSONArray)JSONValue.parse(red.getString("ninja"));
                        Player p = Client.gI().getPlayer(user);
                        if (p != null) {
                            if(p.c != null && p.c.tileMap != null) {
                                p.c.tileMap.leave(p);
                            }
                            p.conn.sendMessageLog("Có người đăng nhập vào tài khoản của bạn.");
                            Client.gI().kickSession(p.conn);
                            conn.sendMessageLog("Bạn đang đăng nhập tại máy khác. Hãy thử đăng nhập lại");
                            return null;
                        } 
                            if (Client.timeWaitLogin.containsKey(username)) {
                                if (System.currentTimeMillis() < (Long)Client.timeWaitLogin.get(username)) {
                                    conn.sendMessageLog("Bạn chỉ có thể đăng nhập lại vào tài khoản sau " + ((Long)Client.timeWaitLogin.get(username) - System.currentTimeMillis()) / 1000L + "s nữa");
                                    return null;
                                }
                                Client.timeWaitLogin.remove(username);
                            }
                            p = new Player();
                            p.conn = conn;
                            p.id = iddb;
                            p.username = username;
                            p.luong = luong;
                            p.role = role;
                            p.online = online;
                            p.status = status;
                            
                            //vip
                            p.vip = vip;
                            p.coinnap = coinnap;
                            p.vxLuong = vxLuong;

                            for(byte i = 0; i < jarr.size(); ++i) {
                                p.sortNinja[i] = jarr.get(i).toString();
                            }
                            SQLManager.stat.executeUpdate("UPDATE `player` SET `online`=1 WHERE `id`=" + p.id + " ;");
                            Client.gI().put(p);
                            jarr.clear();
                            red.close();
                            return p;
                        
                    }
                } else {
                    conn.sendMessageLog("Tài khoản hoặc mật khẩu không chính xác.");
                    return null;
                }
            }
        } catch (SQLException var17) {
            var17.printStackTrace();
            return null;
        }
    }*/

    public void sendInfo() {
        Message m = null;
        try {
            this.c.hp = this.c.getMaxHP();
            this.c.mp = this.c.getMaxMP();
            m = new Message(-30);
            m.writer().writeByte(-127);
            m.writer().writeInt(this.c.id);
            m.writer().writeUTF(this.c.clan.clanName);
            if (!this.c.clan.clanName.isEmpty()) {
                m.writer().writeByte(this.c.clan.typeclan);
            }

            m.writer().writeByte(this.c.taskId = 50);
            m.writer().writeByte(this.c.gender);
            m.writer().writeShort(this.c.partHead());
            m.writer().writeByte(this.c.speed());
            m.writer().writeUTF(this.c.name);
            m.writer().writeByte(this.c.pk);
            m.writer().writeByte(this.c.typepk);
            m.writer().writeInt(this.c.getMaxHP());
            m.writer().writeInt(this.c.hp);
            m.writer().writeInt(this.c.getMaxMP());
            m.writer().writeInt(this.c.mp);
            m.writer().writeLong(this.c.exp);
            m.writer().writeLong(this.c.expdown);
            m.writer().writeShort(this.c.eff5buffHP());
            m.writer().writeShort(this.c.eff5buffMP());
            m.writer().writeByte(this.c.nclass);
            m.writer().writeShort(this.c.ppoint);
            m.writer().writeShort(this.c.potential0);
            m.writer().writeShort(this.c.potential1);
            m.writer().writeInt(this.c.potential2);
            m.writer().writeInt(this.c.potential3);
            m.writer().writeShort(this.c.spoint);
            m.writer().writeByte(this.c.skill.size());

            short i;
            for(i = 0; i < this.c.skill.size(); ++i) {
                m.writer().writeShort(SkillTemplate.Templates(((Skill)this.c.skill.get(i)).id, ((Skill)this.c.skill.get(i)).point).skillId);
            }

            m.writer().writeInt(this.c.xu);
            m.writer().writeInt(this.c.yen);
            m.writer().writeInt(this.luong);
            m.writer().writeByte(this.c.maxluggage);

            byte j;
            for(j = 0; j < this.c.maxluggage; ++j) {
                if (this.c.ItemBag[j] == null) {
                    m.writer().writeShort(-1);
                } else {
                    m.writer().writeShort(this.c.ItemBag[j].id);
                    m.writer().writeBoolean(this.c.ItemBag[j].isLock);
                    if (ItemTemplate.isTypeBody(this.c.ItemBag[j].id) || ItemTemplate.isTypeMounts(this.c.ItemBag[j].id) || ItemTemplate.isTypeNgocKham(this.c.ItemBag[j].id)) {
                        m.writer().writeByte(this.c.ItemBag[j].upgrade);
                    }
                    m.writer().writeBoolean(this.c.ItemBag[j].isExpires);
                    m.writer().writeShort(this.c.ItemBag[j].quantity);
                }
            }

            int k;
            Item item;
            for(k = 0; k < 16; ++k) {
                item = this.c.get().ItemBody[k];
                if (item != null) {
                    m.writer().writeShort(item.id);
                    m.writer().writeByte(item.upgrade);
                    m.writer().writeByte(item.sys);
                } else {
                    m.writer().writeShort(-1);
                }
            }

            m.writer().writeBoolean(this.c.isHuman);
            m.writer().writeBoolean(this.c.isNhanban);
            m.writer().writeShort(this.c.partHead());
            m.writer().writeShort(this.c.Weapon());
            m.writer().writeShort(this.c.Body());
            m.writer().writeShort(this.c.Leg());

            m.writer().writeShort(this.c.get().ID_HAIR);
            m.writer().writeShort(this.c.get().ID_Body);
            m.writer().writeShort(this.c.get().ID_LEG);
            m.writer().writeShort(this.c.get().ID_WEA_PONE);
            m.writer().writeShort(this.c.get().ID_PP);
            m.writer().writeShort(this.c.get().ID_NAME);
            m.writer().writeShort(this.c.get().ID_HORSE);
            m.writer().writeShort(this.c.get().ID_RANK);
            m.writer().writeShort(this.c.get().ID_MAT_NA);
            m.writer().writeShort(this.c.get().ID_Bien_Hinh);

            for(k = 16; k < 32; ++k) {
                item = this.c.get().ItemBody[k];
                if (item != null) {
                    m.writer().writeShort(item.id);
                    m.writer().writeByte(item.upgrade);
                    m.writer().writeByte(item.sys);
                } else {
                    m.writer().writeShort(-1);
                }
            }
            m.writer().flush();
            this.conn.sendMessage(m);
            m.cleanup();
            this.getMobMe();
            this.c.clone = CloneCharacter.getClone(this.c);
            if (this.c.isTaskHangNgay == 1 && this.c.taskHangNgay[0] != -1) {
                Service.getTaskOrder(this.c, (byte)0);
            }
            if (this.c.isTaskTaThu == 1 && this.c.taskTaThu[0] != -1) {
                Service.getTaskOrder(this.c, (byte)1);
            }
            Map map;
            int var7;
            for(var7 = 0; var7 < Server.maps.length; ++var7) {
                map = Server.maps[var7];
                if (map.id == this.c.mapid) {
                    boolean isturn = false;
                    if (map.getXHD() != -1 || map.VDMQ() || map.mapChienTruong() || map.mapLDGT() || map.mapGTC()) {
                        isturn = true;
                        map = Manager.getMapid(this.c.mapLTD);
                    }
                    int l;
                    for(l = 0; l < map.area.length; ++l) {
                        if (map.area[l].numplayers < map.template.maxplayers) {
                            if (!isturn) {
                                map.area[l].Enter(this);
                            } else {
                                map.area[l].EnterMap0(this.c);
                            }
                            byte n;
                            for(n = 0; n < this.c.veff.size(); ++n) {
                                this.addEffectMessage(this.c.veff.get(n));
                            }
                            return;
                        }
                    }

                    map.area[Util.nextInt(map.area.length)].Enter(this);

                    byte n2;
                    for(n2 = 0; n2 < this.c.veff.size(); ++n2) {
                        this.addEffectMessage(this.c.veff.get(n2));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }

    public void loadSkill() {
        Message m = null;
        try{
            m = new Message(-30);

            m.writer().writeByte(-125);
            m.writer().writeByte(this.c.get().speed());
            m.writer().writeInt(this.c.get().getMaxHP());
            m.writer().writeInt(this.c.get().getMaxMP());
            m.writer().writeShort(this.c.get().spoint);
            m.writer().writeByte(this.c.get().skill.size());
            for (Skill fs :  this.c.get().skill) {
                m.writer().writeShort(SkillTemplate.Templates(fs.id, fs.point).skillId);
            }
            m.writer().flush();
            this.conn.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
     }

    public void getMp() {
        Message m = null;
        try {
           if(this.conn != null) {
               m = new Message(-30);
               m.writer().writeByte(-121);
               m.writer().writeInt(this.c.get().mp);
               m.writer().flush();
               this.conn.sendMessage(m);
           }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }

    public void restPpoint() {
        if (this.c.get().nclass % 2 == 0) {
            this.c.get().potential0 = 5;
            this.c.get().potential1 = 5;
            this.c.get().potential2 = 5;
            this.c.get().potential3 = 15;
        } else {
            this.c.get().potential0 = 15;
            this.c.get().potential1 = 5;
            this.c.get().potential2 = 5;
            this.c.get().potential3 = 5;
        }
        int usePlusPpoint = (8 - this.c.get().useTiemNang) * 10 + (10 - this.c.get().useBanhBangHoa) * 10 + this.c.chuyenSinh * 5780;
        this.c.get().ppoint = (short)(Level.totalpPoint(this.c.get().level) + usePlusPpoint);
        this.loadPpoint();
    }

    public void restSpoint() {
        for (Skill skl : this.c.get().skill) {
            if (skl.id != 0) {
                if(this.c.isHuman && this.c.checkIdSkill90(skl.id)) {
                    continue;
                }
                skl.point = 1;
            }
        }
        int usePlusSpoint = 8 - this.c.get().useKyNang + (10 - this.c.get().useBanhPhongLoi);
        this.c.get().spoint = (short)(Level.totalsPoint(this.c.get().level) + usePlusSpoint);
        this.loadSkill();
    }

    public void loadPpoint() {
        Message m = null;
        try {
            m = new Message(-30);
            m.writer().writeByte(-109);
            m.writer().writeByte(this.c.get().speed());
            m.writer().writeInt(this.c.get().getMaxHP());
            m.writer().writeInt(this.c.get().getMaxMP());
            m.writer().writeShort(this.c.get().ppoint);
            m.writer().writeShort(this.c.get().potential0);
            m.writer().writeShort(this.c.get().potential1);
            m.writer().writeInt(this.c.get().potential2);
            m.writer().writeInt(this.c.get().potential3);
            m.writer().flush();
            this.conn.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public void openBagLevel(byte index) {
        Message m = null;
        try {
            m = new Message(-30);
            m.writer().writeByte(-91);
            m.writer().writeByte(this.c.ItemBag.length);
            m.writer().writeByte(index);
            m.writer().flush();
            this.conn.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public void setTypeClan(int type) {
        Message m = null;
        try {
            this.c.clan.typeclan = (byte)type;
            m = new Message(-30);
            m.writer().writeByte(-62);
            m.writer().writeInt(this.c.id);
            m.writer().writeUTF(this.c.clan.clanName);
            m.writer().writeByte(this.c.clan.typeclan);
            m.writer().flush();
            this.c.tileMap.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public void upExpClan(int exp) {
        ClanManager clan = ClanManager.getClanName(this.c.clan.clanName);
        if (clan != null && clan.getMem(this.c.name) != null) {
            this.c.clan.pointClan += exp;
            this.c.clan.pointClanWeek += exp;
            clan.upExp(exp);
            this.sendAddchatYellow("Gia tộc của bạn nhận được " + exp + " kinh nghiệm");
        }
    }

    public void sendAddchatYellow(String str) {
        Message m = null;
        try {
            if(this.conn != null) {
                m = new Message(-24);
                m.writer().writeUTF(str);
                m.writer().flush();
                this.conn.sendMessage(m);
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public void endDlg(boolean isResetButton) {
        Message ms = null;
        try {
           if(this.conn != null) {
               ms = new Message(126);
               ms.writer().writeByte(isResetButton ? 0 : 1);
               ms.writer().flush();
               this.conn.sendMessage(ms);
           }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(ms != null) {
                ms.cleanup();
            }
        }
    }

    public void luongMessage(long luongup) {
        Message m = null;
        try {
            if(this.conn != null) {
                this.upluong(luongup);
                m = new Message(-30);
                m.writer().writeByte(-72);
                m.writer().writeInt(this.luong);
                m.writer().flush();
                this.conn.sendMessage(m);
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }

    public void upluongMessage(long luongup) {
        Message m = null;
        try {
           if(this.conn != null) {
               m = new Message(-30);
               m.writer().writeByte(-71);
               m.writer().writeInt(this.upluong(luongup));
               m.writer().flush();
               this.conn.sendMessage(m);
           }
        } catch (Exception var4) {
            var4.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }
  public void upCoinMessage(long coin) {
        Message m = null;
        try {
           if(this.conn != null) {
               m = new Message(-30);
               m.writer().writeByte(-71);
               m.writer().writeInt(this.upcoin(coin));
               m.writer().flush();
               this.conn.sendMessage(m);
           }
        } catch (Exception var4) {
            var4.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }

    public void requestItem(int typeUI) {
        Message m = null;
        try {
            if(this.conn != null) {
                m = new Message(30);
                m.writer().writeByte(typeUI);
                m.writer().flush();
                this.conn.sendMessage(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public void liveFromDead() {
        Message m = null;
        try {
            if(this.c.tileMap.map.mapChienTruong()) {
                switch (this.c.pheCT) {
                    case 0: {
                        this.c.typepk = 4;
                        Service.ChangTypePkId(this.c, (byte)4);
                        break;
                    }
                    case 1: {
                        this.c.typepk = 5;
                        Service.ChangTypePkId(this.c, (byte)5);
                        break;
                    }
                    default: {
                        this.c.typepk = 0;
                        Service.ChangTypePkId(this.c, (byte)0);
                        break;
                    }
                }
            }
            this.c.hp = this.c.getMaxHP();
            this.c.mp = this.c.getMaxMP();
            this.c.isDie = false;
            if(this.conn != null) {
                m = new Message(-10);
                m.writer().flush();
                this.conn.sendMessage(m);
                m.cleanup();
            }
            m = new Message(88);
            m.writer().writeInt(this.c.id);
            m.writer().writeShort(this.c.x);
            m.writer().writeShort(this.c.y);
            m.writer().flush();
            this.c.tileMap.sendMyMessage(this, m);
            m.cleanup();
        } catch (Exception var2) {
            var2.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public void endLoad(boolean canvas) {
        Message m = null;
        try {
            if(this.conn != null) {
                m = new Message(126);
                m.writer().writeByte(canvas ? 0 : -1);
                m.writer().flush();
                this.conn.sendMessage(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public void flush() {
        JSONArray jarr = new JSONArray();
        try {
            synchronized(Server.LOCK_MYSQL) {
                if (this.c != null) {
                    this.c.flush();
                    String n = this.sortNinja[0];
                    this.sortNinja[0] = this.c.name;

                    for(byte k = 1; k < this.sortNinja.length; ++k) {
                        if (this.sortNinja[k] != null && this.sortNinja[k].equals(this.c.name)) {
                            this.sortNinja[k] = n;
                        }
                    }
                }

                for(byte j = 0; j < this.sortNinja.length; ++j) {
                    if (this.sortNinja[j] != null) {
                        jarr.add(this.sortNinja[j]);
                    }
                }

                SQLManager.stat.executeUpdate("UPDATE `player` SET `online`=0,`luong`=" + this.luong + ",`vip`=" + this.vip + ",`vongxoayluong`=" + this.vxLuong + ",`ninja`='" + jarr.toJSONString() + "' WHERE `id`=" + this.id + " LIMIT 1;");
                if (jarr != null && !jarr.isEmpty()) {
                    jarr.clear();
                }
            }
        } catch (SQLException var8) {
            var8.printStackTrace();
        }

    }

    public void close() {
    }

    public void openBookSkill(byte index, byte sid) {
        System.out.println(sid);
        if (this.c.get().getSkill(sid) != null) {
            this.sendAddchatYellow(Language.HAVE_LEARNED_SKILL);
        } else {
            Message m = null;
            try {
                this.c.ItemBag[index] = null;
                Skill skill = new Skill();
                skill.id = sid;
                skill.point = 1;
                this.c.get().skill.add(skill);
                Service.sendInfoPlayers(this, this);
                this.loadSkill();
                m = new Message(-30);
                m.writer().writeByte(-102);
                m.writer().writeByte(index);
                m.writer().writeShort(SkillTemplate.Templates(skill.id, skill.point).skillId);
                m.writer().flush();
                this.conn.sendMessage(m);
                m.cleanup();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(m != null) {
                    m.cleanup();
                }
            }
        }
    }
    
    public void updateExpCS(long xpup){
        try{
            if (this != null) {
                try{
                    String sqlSET = "(" + this.c.id +", '"+this.c.name+"', " + this.c.level + ", '" + Server.manager.NinjaS[this.c.nclass] + "', '" +Util.toDateString(Date.from(Instant.now()))+"');";
                    SQLManager.stat.executeUpdate("INSERT INTO `xep_hang_level` (`ninja_id`,`name`,`level`,`class`,`time`) VALUES " + sqlSET);
                }catch (Exception e){
                }
            }
        }catch (Exception e){}
    }

    public void updateExp(long xpup) {
        Message m = null;
        int level;
        long xpold;
        int i;
        try {
            if (this.c.get().isHuman && this.c.get().exptype == 1 && !this.c.get().isNhanban) {
                if (this.c.expdown > 0L) {
                    this.c.expdown -= xpup;
                    m = new Message(71);
                    m.writer().writeLong(xpup);
                    m.writer().flush();
                    this.conn.sendMessage(m);
                    m.cleanup();
                } else {
                    this.c.expdown = 0L;
                    xpold = this.c.exp;
                    this.c.exp += xpup;
                    level = this.c.level;
                    if (this.c.level <= Manager.max_level_up) {
                        this.c.setLevel_Exp(this.c.exp, true);
                    }

                    if (this.c.level == Manager.max_level_up) {
                        try {
                            if (this.c.saveBXH != Manager.max_level_up) {
                                String sqlSET = "(" + this.c.id +", '"+this.c.name+"', " + this.c.level + ", '" + Server.manager.NinjaS[this.c.nclass] + "', '" +Util.toDateString(Date.from(Instant.now()))+"');";
                                SQLManager.stat.executeUpdate("INSERT INTO `xep_hang_level` (`ninja_id`,`name`,`level`,`class`,`time`) VALUES " + sqlSET);
                                this.c.saveBXH = Manager.max_level_up;
                                System.out.println("Check ---------------------------------- update exp");
                            }
                        } catch (Exception var8) {
                            var8.printStackTrace();
                        }
                    }

                    if (this.c.level > Manager.max_level_up) {
                        this.c.level = Manager.max_level_up;
                        this.c.exp = xpold;
                        xpup = 0L;
                    }

                    if (level < this.c.level) {
                        if (this.c.nclass != 0) {
                            for(i = level + 1; i <= this.c.level; ++i) {
                                this.c.ppoint += Level.getLevel(i).ppoint;
                                this.c.spoint += Level.getLevel(i).spoint;
                            }
                        } else {
                            for(i = level + 1; i <= this.c.level; ++i) {
                                this.c.potential0 += 5;
                                this.c.potential1 += 2;
                                this.c.potential2 += 2;
                                this.c.potential3 += 2;
                            }
                        }
                    }

                    m = new Message(5);
                    m.writer().writeLong(xpup);
                    m.writer().flush();
                    this.conn.sendMessage(m);
                    m.cleanup();
                    if (level != this.c.level) {
                        this.c.setXPLoadSkill(this.c.exp);
                    }
                }
            }
            else if (this.c.get().isNhanban && !this.c.get().isHuman && !this.c.clone.isDie) {
                if(this.c.get().exptype >= 0) {
                    if (this.c.clone.expdown > 0L) {
                        this.c.clone.expdown -= xpup;
                        m = new Message(71);
                        m.writer().writeLong(xpup);
                        m.writer().flush();
                        this.conn.sendMessage(m);
                        m.cleanup();
                    }
                    else {
                        this.c.clone.expdown = 0L;
                        xpold = this.c.clone.exp;
                        this.c.clone.exp += xpup;
                        level = this.c.clone.level;
                        if (this.c.clone.level <= Manager.max_level_up) {
                            this.c.clone.setLevel_Exp(this.c.clone.exp, true);
                        }

                        if (this.c.clone.level > Manager.max_level_up) {
                            this.c.clone.exp = xpold;
                            xpup = 0L;
                        }

                        if (level < this.c.clone.level) {
                            if (this.c.clone.nclass != 0) {
                                for(i = level + 1; i <= this.c.clone.level; ++i) {
                                    this.c.clone.ppoint += Level.getLevel(i).ppoint;
                                    this.c.clone.spoint += Level.getLevel(i).spoint;
                                }
                            } else {
                                for(i = level + 1; i <= this.c.clone.level; ++i) {
                                    this.c.clone.potential0 += 5;
                                    this.c.clone.potential1 += 2;
                                    this.c.clone.potential2 += 2;
                                    this.c.clone.potential3 += 2;
                                }
                            }
                        }

                        m = new Message(5);
                        m.writer().writeLong(xpup);
                        m.writer().flush();
                        this.conn.sendMessage(m);
                        m.cleanup();
                        if (level != this.c.clone.level) {
                            this.c.clone.setXPLoadSkill(this.c.clone.exp);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }

    public void setEffect(int id, int timeStart, int timeLength, int param) {
        try {
            EffectTemplate data = EffectTemplate.entrys.get(id);
            Effect eff = this.c.get().getEffType(data.type);
            if (eff == null) {
                eff = new Effect(id, timeStart, timeLength, param);
                this.c.get().veff.add(eff);
                this.addEffectMessage(eff);
            } else {
                eff.template = data;
                eff.timeLength = timeLength;
                eff.timeStart = timeStart;
                eff.param = param;
                eff.timeRemove = System.currentTimeMillis() - (long)eff.timeStart + (long)eff.timeLength;
                this.setEffectMessage(eff);
            }
        } catch (Exception var7) {
            var7.printStackTrace();
            Logger.getLogger(Player.class.getName()).log(java.util.logging.Level.SEVERE, (String)null, var7);
        }

    }

    public void addEffectMessage(Effect eff) {
        Message m = null;
        try {
            if(this.conn != null) {
                m = new Message(-30);
                m.writer().writeByte(-101);
                m.writer().writeByte(eff.template.id);
                m.writer().writeInt(eff.timeStart);
                m.writer().writeInt((int)(eff.timeRemove - System.currentTimeMillis()));
                m.writer().writeShort(eff.param);
                if (eff.template.type == 2 || eff.template.type == 3 || eff.template.type == 14) {
                    m.writer().writeShort(this.c.get().x);
                    m.writer().writeShort(this.c.get().y);
                }
                m.writer().flush();
                this.conn.sendMessage(m);
                m.cleanup();
            }
            m = new Message(-30);
            m.writer().writeByte(-98);
            m.writer().writeInt(this.c.get().id);
            m.writer().writeByte(eff.template.id);
            m.writer().writeInt(eff.timeStart);
            m.writer().writeInt((int)(eff.timeRemove - System.currentTimeMillis()));
            m.writer().writeShort(eff.param);
            if (eff.template.type == 2 || eff.template.type == 3 || eff.template.type == 14) {
                m.writer().writeShort(this.c.get().x);
                m.writer().writeShort(this.c.get().y);
            }
            m.writer().flush();
            if(this.c.tileMap != null) {
                this.c.tileMap.sendMessage(m);
            } else if(this.c.tdbTileMap != null) {
                this.c.tdbTileMap.sendMessage(m);
            }
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    private void setEffectMessage(Effect eff) {
        Message m = null;
        try {
            if(this.conn != null) {
                m = new Message(-30);
                m.writer().writeByte(-100);
                m.writer().writeByte(eff.template.id);
                m.writer().writeInt(eff.timeStart);
                m.writer().writeInt(eff.timeLength);
                m.writer().writeShort(eff.param);
                m.writer().flush();
                this.conn.sendMessage(m);
                m.cleanup();
            }
            m = new Message(-30);
            m.writer().writeByte(-97);
            m.writer().writeInt(this.c.get().id);
            m.writer().writeByte(eff.template.id);
            m.writer().writeInt(eff.timeStart);
            m.writer().writeInt(eff.timeLength);
            m.writer().writeShort(eff.param);
            m.writer().flush();
            if(this.c.tileMap != null) {
                this.c.tileMap.sendMessage(m);
            } else if(this.c.tdbTileMap != null) {
                this.c.tdbTileMap.sendMessage(m);
            }
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public void removeEffect(int id) {
        try {
            byte i;
            Effect eff;
            for(i = 0; i < this.c.get().veff.size(); ++i) {
                eff = (Effect)this.c.get().veff.get(i);
                if (eff != null && eff.template.id == id) {
                    this.c.get().veff.remove(i);
                    this.removeEffectMessage(eff);
                }
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    private void removeEffectMessage(Effect eff) {
        Message m = null;
        try {
            if(this.conn != null) {
                m = new Message(-30);
                m.writer().writeByte(-99);
                m.writer().writeByte(eff.template.id);
                if (eff.template.type != 0 && eff.template.type != 12) {
                    if (eff.template.type != 4 && eff.template.type != 13 && eff.template.type != 17) {
                        if (eff.template.type == 23) {
                            m.writer().writeInt(this.c.get().hp);
                            m.writer().writeInt(this.c.get().getMaxHP());
                        }
                    } else {
                        m.writer().writeInt(this.c.get().hp);
                    }
                } else {
                    m.writer().writeInt(this.c.get().hp);
                    m.writer().writeInt(this.c.get().mp);
                }
                m.writer().flush();
                this.conn.sendMessage(m);
                m.writer().flush();
                m.cleanup();
            }
            m = new Message(-30);
            m.writer().writeByte(-96);
            m.writer().writeInt(this.c.get().id);
            m.writer().writeByte(eff.template.id);
            if (eff.template.type != 0 && eff.template.type != 12) {
                if (eff.template.type == 11) {
                    m.writer().writeShort(this.c.get().x);
                    m.writer().writeShort(this.c.get().y);
                } else if (eff.template.type != 4 && eff.template.type != 13 && eff.template.type != 17) {
                    if (eff.template.type == 23) {
                        m.writer().writeInt(this.c.get().hp);
                        m.writer().writeInt(this.c.get().getMaxHP());
                    }
                } else {
                    m.writer().writeInt(this.c.get().hp);
                }
            } else {
                m.writer().writeInt(this.c.get().hp);
                m.writer().writeInt(this.c.get().mp);
            }
            m.writer().flush();
            if(this.c.tileMap != null) {
                this.c.tileMap.sendMessage(m);
            } else if(this.c.tdbTileMap != null) {
                this.c.tdbTileMap.sendMessage(m);
            }

            m.cleanup();
        } catch (Exception var3) {
            var3.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }
    
     public boolean updateSysMountsPHB() {
        Item item = this.c.get().ItemMounts[4];
        if (item == null) {
            this.c.p.sendAddchatYellow("Bạn cần phải có thú cưỡi mới có thể sử dụng vật phẩm");
            return false;
        } else if (item.upgrade < 99) {
            this.c.p.sendAddchatYellow("Thú cưỡi chưa đạt cấp tối đa");
            return false;
        } else if (item.sys >= 4) {
            this.c.p.sendAddchatYellow("Không thể nâng thêm sao");
            return false;
        } else {
            if (20 / (item.sys + 1) > Util.nextInt(115)) {
                item.sys++;
                item.upgrade = 0;
                byte i;
                Option op;
                for(i = 0; i < item.options.size(); ++i) {
                    op = item.options.get(i);
                    if (op.id == 65) {
                        op.param = 0;
                    } else if (op.id != 66) {
                        byte j;
                        for(j = 0; j < UseItem.arrOp.length; ++j) {
                            if (UseItem.arrOp[j] == op.id) {
                                op.param -= UseItem.arrParam[j] * 8;
                                break;
                            }
                        }
                    }
                }
                this.loadMounts();
                this.c.p.sendAddchatYellow("Nâng cấp thành công, thú cưỡi tăng 1 sao");
            } else {
                this.c.p.sendAddchatYellow("Nâng cấp thất bại, hao phí 1 tiến hoá thảo");
            }
            return true;
        }
    }

    public boolean updateSysMounts() {
        Item item = this.c.get().ItemMounts[4];
        if (item == null) {
            this.c.p.sendAddchatYellow("Bạn cần phải có thú cưỡi mới có thể sử dụng vật phẩm");
            return false;
        }
        else if (item.id == 828) {
            this.c.p.sendAddchatYellow("Không sử dụng cho Phượng Hoàng Băng");
            return false;
        }
        else if (item.upgrade < 99) {
            this.c.p.sendAddchatYellow("Thú cưỡi chưa đạt cấp tối đa");
            return false;
        }        
        else if (item.sys >= 4) {
            this.c.p.sendAddchatYellow("Không thể nâng thêm sao");
            return false;
        } else {
            if (20 / (item.sys + 1) > Util.nextInt(115)) {
                item.sys++;
                item.upgrade = 0;
                byte i;
                Option op;
                for(i = 0; i < item.options.size(); ++i) {
                    op = item.options.get(i);
                    if (op.id == 65) {
                        op.param = 0;
                    } else if (op.id != 66) {
                        byte j;
                        for(j = 0; j < UseItem.arrOp.length; ++j) {
                            if (UseItem.arrOp[j] == op.id) {
                                op.param -= UseItem.arrParam[j] * 8;
                                break;
                            }
                        }
                    }
                }
                this.loadMounts();
                this.c.p.sendAddchatYellow("Nâng cấp thành công, thú cưỡi tăng 1 sao");
            } else {
                this.c.p.sendAddchatYellow("Nâng cấp thất bại, hao phí 1 chuyển tinh thạch");
            }
            return true;
        }
    }

    public boolean updateXpMounts(int xpup, byte type) {
        Item item = this.c.get().ItemMounts[4];
        if (item == null) {
            this.c.p.sendAddchatYellow("Bạn cần phải có thú cưỡi mới có thể sử dụng vật phẩm");
            return false;
        } else if (item.isExpires) {
            this.c.p.sendAddchatYellow("Bạn cần phải có thú cưỡi vĩnh viễn");
            return false;
        } else if (type == 0 && item.id != 443 && item.id != 523 && item.id != 524) {
            this.c.p.sendAddchatYellow("Chỉ sử dụng cho thú cưỡi");
            return false;
        } else if (type == 1 && item.id != 485 && item.id != 524) {
            this.c.p.sendAddchatYellow("Chỉ sử dụng cho xe máy");
            return false;
        } else if (type == 3 && item.id != 778 && item.id != 828) {
            this.c.p.sendAddchatYellow("Chỉ sử dụng cho phượng hoàng băng và trâu");
            return false;
        } else if (item.upgrade >= 99) {
            this.c.p.sendAddchatYellow("Thú cưỡi đã đạt cấp tối đa");
            return false;
        } else {
            boolean isuplv = false;

            byte i;
            Option op;
            for(i = 0; i < item.options.size(); ++i) {
                op = (Option)item.options.get(i);
                if (op.id == 65) {
                    op.param += xpup;
                    if (op.param >= 1000) {
                        isuplv = true;
                        op.param = 0;
                    }
                    break;
                }
            }

            if (isuplv) {
                item.upgrade++;
                int lv = item.upgrade + 1;
                if (lv == 10 || lv == 20 || lv == 30 || lv == 40 || lv == 50 || lv == 60 || lv == 70 || lv == 80 || lv == 90) {
                    byte j;
                    Option op2;
                    for(j = 0; j < item.options.size(); ++j) {
                        op2 = (Option)item.options.get(j);
                        if (op2.id != 65 && op2.id != 66) {
                            byte k;
                            for(k = 0; k < UseItem.arrOp.length; ++k) {
                                if (UseItem.arrOp[k] == op2.id) {
                                    op2.param += UseItem.arrParam[k];
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            this.loadMounts();
            return true;
        }
    }

    public void loadMounts() {
        Message m = null;
        try {
            m = new Message(-30);
            m.writer().writeByte(-54);
            m.writer().writeInt(this.c.get().id);
            byte i;
            Item item;
            for(i = 0; i < this.c.get().ItemMounts.length; ++i) {
                item = this.c.get().ItemMounts[i];
                if (item != null) {
                    m.writer().writeShort(item.id);
                    m.writer().writeByte(item.upgrade);
                    m.writer().writeLong(item.expires);
                    m.writer().writeByte(item.sys);
                    m.writer().writeByte(item.options.size());

                    byte j;
                    for(j = 0; j < item.options.size(); ++j) {
                        m.writer().writeByte(((Option)item.options.get(j)).id);
                        m.writer().writeInt(((Option)item.options.get(j)).param);
                    }
                } else {
                    m.writer().writeShort(-1);
                }
            }
            m.writer().flush();
            if(this.c.tileMap != null) {
                this.c.tileMap.sendMessage(m);
            } else if(this.c.tdbTileMap != null) {
                this.c.tdbTileMap.sendMessage(m);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public boolean dungThucan(byte id, int param, int thoigian) {
        Effect eff = this.c.get().getEffType((byte)0);
        if (this.c.get().pk > 14) {
            this.sendAddchatYellow(Language.MAX_HIEU_CHIEN);
            return false;
        } else if (eff != null && (eff.param > param || eff.template.id == 36)) {
            this.sendAddchatYellow("Đã có hiệu quả thức ăn cao hơn");
            return false;
        } else {
            this.setEffect(id, 0, 1000 * thoigian, param);
            return true;
        }
    }

    public boolean buffHP(int param) {
        Effect eff = this.c.get().getEffType((byte)17);
        if (eff != null) {
            return false;
        } else if (this.c.get().pk > 14) {
            this.sendAddchatYellow(Language.MAX_HIEU_CHIEN);
            return false;
        } else if (this.c.get().hp >= this.c.get().getMaxHP()) {
            this.sendAddchatYellow("HP đã đầy");
            return false;
        } else {
            this.setEffect(21, 0, 3000, param);
            return true;
        }
    }

    public boolean buffMP(int param) {
        if (this.c.get().pk > 14) {
            this.sendAddchatYellow(Language.MAX_HIEU_CHIEN);
            return false;
        } else if (this.c.get().mp >= this.c.get().getMaxMP()) {
            this.sendAddchatYellow("MP đã đầy");
            this.getMp();
            return false;
        } else {
            this.c.get().upMP(param);
            this.getMp();
            return true;
        }
    }

    public void mobMeMessage(int id, byte boss) {
        Message m = null;
        try {
            if (id > 0) {
                Mob mob = new Mob(-1, id, 0, this.c.tileMap);
                mob.sys = 1;
                mob.status = 5;
                int n = 0;
                mob.hpmax = n;
                mob.hp = n;
                mob.isboss = boss != 0;
                this.c.get().mobMe = mob;
            } else {
                this.c.get().mobMe = null;
            }
            if(this.conn != null) {
                m = new Message(-30);
                m.writer().writeByte(-69);
                m.writer().writeByte(id);
                m.writer().writeByte(boss);
                m.writer().flush();
                this.conn.sendMessage(m);
                m.cleanup();
            }
            m = new Message(-30);
            m.writer().writeByte(-68);
            m.writer().writeInt(this.c.get().id);
            m.writer().writeByte(id);
            m.writer().writeByte(boss);
            m.writer().flush();
            if (this.c.tileMap != null) {
                this.c.tileMap.sendMyMessage(this,m);
            } else if (this.c.tdbTileMap != null) {
                this.c.tdbTileMap.sendMyMessage(this, m);
            }
        } catch (IOException var7) {
            var7.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public void mobMeCloneMessage(int id, byte boss) {
        Message m = null;
        try {
            if (id > 0) {
                Mob mob = new Mob(-1, id, 0, this.c.tileMap);
                mob.sys = 1;
                mob.status = 5;
                int n = 0;
                mob.hpmax = n;
                mob.hp = n;
                mob.isboss = boss != 0;
                this.c.clone.mobMe = mob;
            } else {
                this.c.clone.mobMe = null;
            }
            m = new Message(-30);
            m.writer().writeByte(-68);
            m.writer().writeInt(this.c.clone.id);
            m.writer().writeByte(id);
            m.writer().writeByte(boss);
            m.writer().flush();
            if(this.c.tileMap!= null ) {
                this.c.tileMap.sendMyMessage(this, m);
            } else if(this.c.tdbTileMap!= null ) {
                this.c.tdbTileMap.sendMyMessage(this, m);
            }
         } catch (IOException var7) {
            var7.printStackTrace();
        } finally {
            if(m != null ) {
                m.cleanup();
            }
        }

    }

    public void setTimeMap(int timeLength) {
        Message m = null;
        try {
           if(this.conn != null) {
               m = new Message(-30);
               m.writer().writeByte(-95);
               m.writer().writeInt(timeLength);
               m.writer().flush();
               this.conn.sendMessage(m);
           }
        } catch (IOException var3) {
            var3.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }

    public void setPointPB(int point) {
        Message m = null;
        try {
            m = new Message(-28);
            m.writer().writeByte(-84);
            m.writer().writeShort(point);
            m.writer().flush();
            this.conn.sendMessage(m);

        } catch (IOException var3) {
            var3.printStackTrace();
        } finally {
            if(m !=null) {
                m.cleanup();
            }
        }

    }

    public void restCave() {
        Message m = null;
        try {
            m = new Message(-16);
            m.writer().flush();
            this.conn.sendMessage(m);
        } catch (IOException var2) {
            var2.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }

    public void getMobMe() {
        if (this.c.get().ItemBody[10] != null) {
            switch(this.c.get().ItemBody[10].id) {
                case 246:
                    this.mobMeMessage(70, (byte)0);
                    break;
                case 419:
                    this.mobMeMessage(122, (byte)0);
                    break;
                case 568:
                    this.mobMeMessage(205, (byte)0);
                    break;
                case 569:
                    this.mobMeMessage(206, (byte)0);
                    break;
                case 570:
                    this.mobMeMessage(207, (byte)0);
                    break;
                case 571:
                    this.mobMeMessage(208, (byte)0);
                    break;
                case 583:
                    this.mobMeMessage(211, (byte)1);
                    break;
                case 584:
                    this.mobMeMessage(212, (byte)1);
                    break;
                case 585:
                    this.mobMeMessage(213, (byte)1);
                    break;
                case 586:
                    this.mobMeMessage(214, (byte)1);
                    break;
                case 587:
                    this.mobMeMessage(215, (byte)1);
                    break;
                case 588:
                    this.mobMeMessage(216, (byte)1);
                    break;
                case 589:
                    this.mobMeMessage(217, (byte)1);
                    break;
                case 742:
                    this.mobMeMessage(229, (byte)1);
                    break;
                case 781:
                    this.mobMeMessage(235, (byte)1);
                    break;
            }
        } else {
            this.mobMeMessage(0, (byte)0);
        }

    }

    public void getMobMeClone() {
        if (this.c.clone.ItemBody[10] != null) {
            switch(this.c.clone.ItemBody[10].id) {
                case 246:
                    this.mobMeCloneMessage(70, (byte)0);
                    break;
                case 419:
                    this.mobMeCloneMessage(122, (byte)0);
                    break;
                case 568:
                    this.mobMeCloneMessage(205, (byte)0);
                    break;
                case 569:
                    this.mobMeCloneMessage(206, (byte)0);
                    break;
                case 570:
                    this.mobMeCloneMessage(207, (byte)0);
                    break;
                case 571:
                    this.mobMeCloneMessage(208, (byte)0);
                    break;
                case 583:
                    this.mobMeCloneMessage(211, (byte)1);
                    break;
                case 584:
                    this.mobMeCloneMessage(212, (byte)1);
                    break;
                case 585:
                    this.mobMeCloneMessage(213, (byte)1);
                    break;
                case 586:
                    this.mobMeCloneMessage(214, (byte)1);
                    break;
                case 587:
                    this.mobMeCloneMessage(215, (byte)1);
                    break;
                case 588:
                    this.mobMeCloneMessage(216, (byte)1);
                    break;
                case 589:
                    this.mobMeCloneMessage(217, (byte)1);
                    break;
                case 742:
                    this.mobMeCloneMessage(229, (byte)1);
                    break;
                case 781:
                    this.mobMeCloneMessage(235, (byte)1);
                    break;
            }
        } else {
            this.mobMeCloneMessage(0, (byte)0);
        }

    }

    public void toNhanBan() {
        try {
            if (!this.c.isNhanban) {
                if (this.c.party != null) {
                    HandleController.RoiNhom(this);
                }
                byte n;
                for(n = 0; n < this.c.get().veff.size(); ++n) {
                    if(this.c.get().veff.get(n) != null) {
                        this.removeEffectMessage(this.c.get().veff.get(n));
                    }
                }
                this.c.isNhanban = true;
                this.c.isHuman = false;
                this.c.clone.islive = true;
                this.c.clone.x = this.c.x;
                this.c.clone.y = this.c.y;
                this.c.tileMap.removeMessage(this.c.clone.id);
                this.c.tileMap.removeMessage(this.c.id);
                Service.CharViewInfo(this);
                GameSrc.sendSkill(this, "KSkill");
                GameSrc.sendSkill(this, "OSkill");
                GameSrc.sendSkill(this, "CSkill");
                this.c.get().x = this.c.clone.x;
                this.c.get().y = this.c.clone.y;
                int i;
                Player player;
                for(i = this.c.tileMap.players.size() - 1; i >= 0; --i) {
                    player = this.c.tileMap.players.get(i);
                    if(player != null) {
                        if (player.id != this.id) {
                            this.c.tileMap.sendCharInfo(this, player);
                            this.c.tileMap.sendCoat(this.c.get(), player);
                            this.c.tileMap.sendGlove(this.c.get(), player);
                        }
                        this.c.tileMap.sendMounts(this.c.get(), player);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public synchronized void exitNhanBan(boolean islive) {
        try {
            if (this.c.isNhanban) {
                if (this.c.clone != null) {
                    this.c.clone.flush();
                }
                if (this.c.clone.party != null) {
                    HandleController.RoiNhom(this);
                }
                for(byte n = 0; n < this.c.get().veff.size(); ++n) {
                    if(this.c.get().veff.get(n) != null) {
                        this.removeEffectMessage(this.c.get().veff.get(n));
                    }
                }
                this.c.isNhanban = false;
                this.c.isHuman = true;
                this.c.clone.islive = islive;
                this.c.x = this.c.clone.x;
                this.c.y = this.c.clone.y;
                this.c.clone.refresh();
                this.c.tileMap.removeMessage(this.c.clone.id);
                Service.CharViewInfo(this);
                GameSrc.sendSkill(this, "KSkill");
                GameSrc.sendSkill(this, "OSkill");
                GameSrc.sendSkill(this, "CSkill");
                Player player;
                int i;
                for(i = this.c.tileMap.players.size() - 1; i >= 0; i--) {
                    player = this.c.tileMap.players.get(i);
                    if(player != null) {
                        if (player.id != this.id) {
                            this.c.tileMap.sendCharInfo(this, player);
                            this.c.tileMap.sendCoat(this.c.get(), player);
                            this.c.tileMap.sendGlove(this.c.get(), player);
                        }
                        this.c.tileMap.sendMounts(this.c.get(), player);
                        if(islive) {
                            Service.sendclonechar(this.c.p, player);
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void changeMap(int id) {
        Map ma = Manager.getMapid(id);
        TileMap[] var3 = ma.area;
        int var4 = var3.length;
        TileMap area;
        int var5;
        for(var5 = 0; var5 < var4; ++var5) {
            area = var3[var5];
            if (area.numplayers < ma.template.maxplayers) {
                this.c.tileMap.leave(this);
                area.EnterMap0(this.c);
                return;
            }
        }

    }

    public void sendRequestBattleToAnother(Char friendNinja, Char _char) {
        Message m = null;
        try {
            m = new Message(-157);
            m.writer().writeInt(_char.id);
            friendNinja.p.conn.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public void acceptClanDun(Message m) {
        try {
            if (m.reader().available() < 0) {
                return;
            }
            System.out.println( "Read ------------------------- " + m.reader().available());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void accpetDun(Message m) throws IOException {
        if (m.reader().available() > 0) {

        }
    }

    public void CuuSat(Message msg) throws IOException {
        if (msg.reader().available() > 0) {

        }
    }

    public void inviteToLDT(Message m) {
        try {
            String name = m.reader().readUTF();
            Char _char = Client.gI().getNinja(name);
            if(_char != null && _char.tileMap != null && !_char.tileMap.map.mapLDGT() && _char.tileMap.map.getXHD() == -1 && !_char.tileMap.map.mapChienTruong() && !_char.tileMap.map.mapBossTuanLoc() && !_char.isInDun) {
                Service.startYesNoDlg(_char.p, (byte) 3, this.c.name + " mời bạn vào Lãnh Địa Gia Tộc. Bạn có muốn tham gia?");
            } else {
                this.sendAddchatYellow("Người chơi đang ở vị trí không thể mời vào LDGT.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public void sendInfoMeNewItem() {
        Service.CharViewInfo(this, false);
        int i;
        if(this.c.tileMap != null) {
            for (i = this.c.tileMap.players.size() - 1; i >= 0; i--) {
                if (this.c.tileMap.players.get(i) != null) {
                    this.c.tileMap.sendMounts(this.c.get(), this.c.tileMap.players.get(i));
                    if (this.id != this.c.tileMap.players.get(i).id) {
                        Service.sendInfoChar(this, this.c.tileMap.players.get(i));
                    }
                }
            }
        } else if(this.c.tdbTileMap != null) {
            for (i = this.c.tdbTileMap.players.size() - 1; i >= 0; i--) {
                if (this.c.tdbTileMap.players.get(i) != null) {
                    this.c.tdbTileMap.sendMounts(this.c.get(), this.c.tdbTileMap.players.get(i));
                }
            }
        }
    }

    private void closeLoad() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private int upcoin(long coinup) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
