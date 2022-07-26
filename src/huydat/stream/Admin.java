package huydat.stream;

import huydat.server.Server;
import huydat.real.ClanManager;
import huydat.server.Manager;
import huydat.server.ShinwaManager;
import huydat.server.ThienDiaBangManager;

public class Admin implements Runnable{
    private int timeCount;
    private Server server;

    public Admin(int minues, Server server) {
        this.timeCount = minues;
        this.server = server;
    }

    public void run() {
        try {
            while (timeCount > 0) {
                Manager.serverChat("Thông báo bảo trì","Hệ thống sẽ bảo trì sau " + timeCount + " phút nữa. Vui lòng thoát game trước thời gian bảo trì, để tránh mất vật phẩm. Xin cảm ơn!");
                timeCount--;
                Thread.sleep(60000);
            }
            if(timeCount == 0) {
                ClanManager.close();
                ThienDiaBangManager.close();
                ShinwaManager.close();
                this.server.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
