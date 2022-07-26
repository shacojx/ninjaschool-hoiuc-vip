package huydat.server;

import huydat.stream.Client;
import huydat.stream.Dun;
import huydat.stream.Admin;
import huydat.stream.GiaTocChien;
import huydat.real.CheckCLLuong;
import huydat.real.Player;
import huydat.real.Language;
import huydat.real.Char;
import huydat.real.CheckCLCoin;
import huydat.real.CheckTXLuong;
import huydat.real.ClanManager;
import huydat.real.Level;
import huydat.real.CheckTXCoin;
import huydat.real.Item;
import huydat.io.Message;
import huydat.io.SQLManager;
import huydat.io.Util;
import huydat.template.ItemTemplate;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import java.io.IOException;
import java.sql.ResultSet;
import java.time.Instant;
import java.util.Date;
import huydat.real.CheckCLXu;
import huydat.real.CheckTXXu;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Draw {
    public static void Draw(Player p, Message m) throws SQLException {
        try {
            short menuId = m.reader().readShort();
            String str = m.reader().readUTF();
                Util.Debug("menuId " + menuId + " str " + str);
                byte b = -1;
                     if(m.reader().available()  > 0) {
                    try {
                        b = m.reader().readByte();
                    }
                    catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                m.cleanup();
                switch (menuId) {                    
                               case 1: {
                        if (p.c.quantityItemyTotal(279) <= 0) {
                            break;
                        }
                        Char c = Client.gI().getNinja(str);

                        if (c != null && c.tileMap != null && c.tileMap.map != null &&  !c.tileMap.map.LangCo() && c.tileMap.map.getXHD() == -1 && c.mapid != 111 && c.mapid != 133 && !c.tileMap.map.mapChienTruong() && !c.tileMap.map.mapLDGT() && !c.tileMap.map.mapBossTuanLoc() && !c.tileMap.map.mapGTC()) {
                            if(p.c.level < 60 && c.tileMap.map.VDMQ()) {
                                p.conn.sendMessageLog("Trình độ của bạn chưa đủ để di chuyển tới đây");
                                return;
                            }
                            if(p.c.tileMap.map.mapGTC() || p.c.tileMap.map.mapChienTruong() || p.c.tileMap.map.id == 111 || p.c.tileMap.map.mapTuTien()) {
                                p.c.typepk = 0;
                                Service.ChangTypePkId(p.c, (byte)0);
                            }
                            p.c.tileMap.leave(p);
                            p.c.get().x = c.get().x;
                            p.c.get().y = c.get().y;
                            c.tileMap.Enter(p);
                            return;
                        }
                        p.sendAddchatYellow("Vị trí người này không thể đi tới");
                        break;
                    }
                               case 118:
                    p.nameUS = str;
                    Char a18 = Client.gI().getNinja(str);
                   if(a18 != null && a18.id == p.c.id) {
                        p.conn.sendMessageLog("Không thể tặng hoa cho chính mình");
                        return;
                    }
                   if(a18 == null) {
                       p.sendAddchatYellow("Nhân vật này không tồn tại hoặc không online.");
                        return;
                    }
                    if(a18.gender !=0) {
                        p.conn.sendMessageLog("Chỉ có thể tặng hoa cho nhận vật nữ");
                        return;
                    }
                    
                        
                     if(p.c.quantityItemyTotal(392) < 1  ) {
                         p.conn.sendMessageLog("Bạn không có đủ giỏ hoa");
                    return;
                     }
                     
                     
                    if (a18 != null  ) {
                        p.tanggiohoa();
    
                    break;
                   } else {
                        p.sendAddchatYellow("Nhân vật này không tồn tại hoặc không online.");
                    }
                    break;
                     case 1699: {
                        if (str.equals("")) {
                            Service.chatNPC(p, (short) 33, "Không hợp lệ");
                            return;
                        }
                        Char nameLove = Client.gI().getNinja(str);
                        if (nameLove != null) {
                            Player sendMsLove = Client.gI().getPlayer(nameLove.p.username);
                            nameLove.sendRealLove = 1;
                            nameLove.senderLove = p.c.name;
                            sendMsLove.sendAddchatYellow("Bạn có một lời đệ nghị kết hôn vui lòng đến NPC Kame để xác nhận !");
                            break;
                        } else {
                            p.sendAddchatYellow("Nhân vật này không tồn tại hoặc không online.");
                            return;
                        }
                    }
                    case 119: {
                        String check = str.replaceAll("\\s+", "");
                        if(!Util.isNumericInt(str) || check.equals("") || !Util.isNumericInt(str)) {
                            Service.chatNPC(p, (short)33, "Giá trị tiền cược nhập vào không đúng");
                            break;
                        }
                        long soluong = Integer.parseInt(str);
                        if (soluong <= 0) {
                        p.lockAcc();
                        }
                        if (p.c.quantityItemyTotal(386) >= 8*soluong && p.c.quantityItemyTotal(393) >= 1*soluong && p.c.quantityItemyTotal(394) >= 1*soluong && p.c.quantityItemyTotal(395) >= 1*soluong) {
                           
                            if (p.c.getBagNull() == 0) {
                                p.conn.sendMessageLog("Hành trang không đủ chỗ trống");
                            } else {
                                p.c.removeItemBags(386, (int)(8*soluong));
                                p.c.removeItemBags(393, (int)(1*soluong));
                                p.c.removeItemBags(394, (int)(1*soluong));
                                p.c.removeItemBags(395, (int)(1*soluong));
                               
                                Item it = ItemTemplate.itemDefault(389);
                                it.quantity = (int)(1*soluong);
                                p.c.addItemBag(true, it);
                            }
                            return;
                        } else {
                            Service.chatNPC(p, (short) 33, "Hành trang của con không có đủ nguyên liệu");
                        }
                        break;
                    }
                
                    case 66:
                                                p.nameUS = str;
                                                                        Char a5 = Client.gI().getNinja(str);
                                                                                                if (a5 != null) {
                                                                                                                                Server.menu.sendWrite(p, (short) 67, "Nhập Coin:");
                                                                                                } else {
                                                                                                                                p.sendAddchatYellow("Nhân vật này không tồn tại hoặc không online.");
                                                                                                }
                                                                                                                        break;
                    case 67:
                                                p.coinGF = str;
                                                                        p.sendCoin();
                                                                                                break;
                                                                                            
                       case 16: {
                        p.nameUS = str;
                        Char userGF = Client.gI().getNinja(str);
                        if (userGF == null) {
                            p.conn.sendMessageLog("Người chơi không tồn tại hoặc không online.");
                            break;
                        }
                        if (userGF.p.c.name == p.c.name) {
                            p.conn.sendMessageLog("Không thể tặng cho chính mình.");
                            break;
                        }
                        if (userGF != null) {
                            Service.sendInputDialog(p, (short) 17, "Nhập số vé (1 vé = 10 lượng của bản thân) :");
                            break;
                        }
                        break;
                    }
                    case 17: {
                        p.tangluong = str;
                        p.vetangluong();
                        break;
                    }
                          case 222:
                        if(!Util.isNumeric(str) || str.equals("")) {
                            p.conn.sendMessageLog("Giá trị nhập vào không hợp lệ");
                            return;
                        }
                        int jointai = Integer.parseInt(str);
                        if (jointai <= 0) {
                            p.lockAcc();
                            return;
                        } 
                        if (jointai % 10 != 0) {
                            p.conn.sendMessageLog("Chia hết cho 10.");
                            return;
                        } 
                        Server.manager.taixiu[0].joinTai(p, jointai);
                        break;
                      case 223:
                        if(!Util.isNumeric(str) || str.equals("")) {
                            p.conn.sendMessageLog("Giá trị nhập vào không hợp lệ");
                            return;
                        }
                        int joinxiu = Integer.parseInt(str);
                        if (joinxiu <= 0) {
                          p.lockAcc();
                          return;
                        } 
                        if (joinxiu % 10 != 0) {
                          p.conn.sendMessageLog("Chia hết cho 10.");
                          return;
                        } 
                        Server.manager.taixiu[0].joinXiu(p, joinxiu);
                        break;                         
                       case 225:
                        if(!Util.isNumeric(str) || str.equals("")) {
                            p.conn.sendMessageLog("Giá trị nhập vào không hợp lệ");
                            return;
                        }
                        int joinle = Integer.parseInt(str);
                        if (joinle <= 0) {
                          p.lockAcc();
                          return;
                        } 
                        if (joinle % 10 != 0) {
                          p.conn.sendMessageLog("Chia hết cho 10.");
                          return;
                        } 
                        Server.manager.chanle[0].joinle(p, joinle);
                        break;
                           case 224:
                        if(!Util.isNumeric(str) || str.equals("")) {
                            p.conn.sendMessageLog("Giá trị nhập vào không hợp lệ");
                            return;
                        }
                        int joinchan = Integer.parseInt(str);
                        if (joinchan <= 0) {
                            p.lockAcc();
                            return;
                        } 
                        if (joinchan % 10 != 0) {
                            p.conn.sendMessageLog("Chia hết cho 10.");
                            return;
                        } 
                        Server.manager.chanle[0].joinchan(p, joinchan);
                        break;
                case 120: {
                        String check = str.replaceAll("\\s+", "");
                        if(!Util.isNumericInt(str) || check.equals("") || !Util.isNumericInt(str)) {
                            Service.chatNPC(p, (short)33, "Giá trị tiền cược nhập vào không đúng");
                            break;
                        }
                        long soluong = Integer.parseInt(str);
                        if (soluong <= 0) {
                        p.lockAcc();
                        }
                        if (p.c.quantityItemyTotal(387) >= 8*soluong && p.c.quantityItemyTotal(393) >= 1*soluong && p.c.quantityItemyTotal(394) >= 1*soluong && p.c.quantityItemyTotal(395) >= 1*soluong) {
                           
                            if (p.c.getBagNull() == 0) {
                                p.conn.sendMessageLog("Hành trang không đủ chỗ trống");
                            } else {
                                p.c.removeItemBags(387, (int)(8*soluong));
                                p.c.removeItemBags(393, (int)(1*soluong));
                                p.c.removeItemBags(394, (int)(1*soluong));
                                p.c.removeItemBags(395, (int)(1*soluong));
                               
                                Item it = ItemTemplate.itemDefault(390);
                                it.quantity = (int)(1*soluong);
                                p.c.addItemBag(true, it);
                            }
                            return;
                        } else {
                            Service.chatNPC(p, (short) 33, "Hành trang của con không có đủ nguyên liệu");
                        }
                        break;
                    }
                case 121: {
                        String check = str.replaceAll("\\s+", "");
                        if(!Util.isNumericInt(str) || check.equals("") || !Util.isNumericInt(str)) {
                            Service.chatNPC(p, (short)33, "Giá trị tiền cược nhập vào không đúng");
                            break;
                        }
                        long soluong = Integer.parseInt(str);
                        if (soluong <= 0) {
                        p.lockAcc();
                        }
                        if (p.c.quantityItemyTotal(388) >= 8*soluong && p.c.quantityItemyTotal(393) >= 1*soluong && p.c.quantityItemyTotal(394) >= 1*soluong && p.c.quantityItemyTotal(395) >= 1*soluong) {
                           
                            if (p.c.getBagNull() == 0) {
                                p.conn.sendMessageLog("Hành trang không đủ chỗ trống");
                            } else {
                                p.c.removeItemBags(388, (int)(8*soluong));
                                p.c.removeItemBags(393, (int)(1*soluong));
                                p.c.removeItemBags(394, (int)(1*soluong));
                                p.c.removeItemBags(395, (int)(1*soluong));
                               
                                Item it = ItemTemplate.itemDefault(391);
                                it.quantity = (int)(1*soluong);
                                p.c.addItemBag(true, it);
                            }
                            return;
                        } else {
                            Service.chatNPC(p, (short) 33, "Hành trang của con không có đủ nguyên liệu");
                        }
                        break;
                    }
                case 122: {
                        String check = str.replaceAll("\\s+", "");
                        if(!Util.isNumericInt(str) || check.equals("") || !Util.isNumericInt(str)) {
                            Service.chatNPC(p, (short)33, "Giá trị tiền cược nhập vào không đúng");
                            break;
                        }
                        long soluong = Integer.parseInt(str);
                        if (soluong <= 0) {
                        p.lockAcc();
                        }
                        if (p.c.quantityItemyTotal(386) >= 8*soluong && p.c.quantityItemyTotal(387) >= 8*soluong &&p.c.quantityItemyTotal(388) >= 8*soluong&& p.c.quantityItemyTotal(393) >= 1*soluong && p.c.quantityItemyTotal(394) >= 1*soluong && p.c.quantityItemyTotal(395) >= 1*soluong) {
                           
                            if (p.c.getBagNull() == 0) {
                                p.conn.sendMessageLog("Hành trang không đủ chỗ trống");
                            } else {
                                 p.c.removeItemBags(386, (int)(8*soluong));
                                  p.c.removeItemBags(387, (int)(8*soluong));
                                p.c.removeItemBags(388, (int)(8*soluong));
                                p.c.removeItemBags(393, (int)(1*soluong));
                                p.c.removeItemBags(394, (int)(1*soluong));
                                p.c.removeItemBags(395, (int)(1*soluong));
                               
                                Item it = ItemTemplate.itemDefault(392);
                                it.quantity = (int)(1*soluong);
                                p.c.addItemBag(true, it);
                            }
                            return;
                        } else {
                            Service.chatNPC(p, (short) 33, "Hành trang của con không có đủ nguyên liệu");
                        }
                        break;
                    }
                
case 123:
                    p.nameUS = str;
                    Char a19 = Client.gI().getNinja(str);
                   if(a19 != null && a19.id == p.c.id) {
                        p.conn.sendMessageLog("Không thể tặng hoa cho chính mình");
                        return;
                    }
                   if(a19 == null) {
                       p.sendAddchatYellow("Nhân vật này không tồn tại hoặc không online.");
                        return;
                    }
                    if(a19.gender !=0) {
                        p.conn.sendMessageLog("Chỉ có thể tặng hoa cho nhận vật nữ");
                        return;
                    }
                    
                        
                     if(p.c.quantityItemyTotal(389) < 1  ) {
                          p.sendAddchatYellow("Bạn không có đủ giỏ hoa hồng đỏ");
                    return;
                     }
                     
                     
                    if (a19 != null  ) {
                        p.tanghoado();
    
                    break;
                   } else {
                        p.sendAddchatYellow("Nhân vật này không tồn tại hoặc không online.");
                    }
                    break;
                    case 124:
                    p.nameUS = str;
                    Char a20 = Client.gI().getNinja(str);
                   if(a20 != null && a20.id == p.c.id) {
                        p.conn.sendMessageLog("Không thể tặng hoa cho chính mình");
                        return;
                    }
                   if(a20 == null) {
                       p.sendAddchatYellow("Nhân vật này không tồn tại hoặc không online.");
                        return;
                    }
                    if(a20.gender !=0) {
                        p.conn.sendMessageLog("Chỉ có thể tặng hoa cho nhận vật nữ");
                        return;
                    }
                    
                        
                     if(p.c.quantityItemyTotal(390) < 1  ) {
                         p.sendAddchatYellow("Bạn không có đủ giỏ hoa hồng vàng");
                    return;
                     }
                     
                     
                    if (a20 != null  ) {
                        p.tanghoavang();
    
                    break;
                   } else {
                        p.sendAddchatYellow("Nhân vật này không tồn tại hoặc không online.");
                    }
                    break;
                    case 125:
                    p.nameUS = str;
                    Char a21 = Client.gI().getNinja(str);
                   if(a21 != null && a21.id == p.c.id) {
                        p.conn.sendMessageLog("Không thể tặng hoa cho chính mình");
                        return;
                    }
                   if(a21 == null) {
                       p.sendAddchatYellow("Nhân vật này không tồn tại hoặc không online.");
                        return;
                    }
                    if(a21.gender !=0) {
                        p.conn.sendMessageLog("Chỉ có thể tặng hoa cho nhận vật nữ");
                        return;
                    }
                    
                        
                     if(p.c.quantityItemyTotal(391) < 1  ) {
                         p.conn.sendMessageLog("Bạn không có đủ giỏ hoa hồng xanh");
                    return;
                     }
                     
                     
                    if (a21 != null  ) {
                        p.tanghoaxanh();
    
                    break;
                   } else {
                        p.sendAddchatYellow("Nhân vật này không tồn tại hoặc không online.");
                    }
                    break;
                    case 2: {
                        Char temp = Client.gI().getNinja(str);
                        if(temp != null) {
                            Char friendNinja = p.c.tileMap.getNinja(temp.id);
                            if(friendNinja != null && friendNinja.id == p.c.id) {
                                Service.chatNPC(p, (short) 0, Language.NAME_LOI_DAI);
                            } else if (friendNinja != null && friendNinja.id != p.c.id) {
                                p.sendRequestBattleToAnother(friendNinja, p.c);
                                Service.chatNPC(p, (short) 0, Language.SEND_MESS_LOI_DAI);
                            } else {
                                Service.chatNPC(p, (short) 0, Language.NOT_IN_ZONE);
                            }
                        } else {
                            Service.chatNPC(p, (short) 0, "Người chơi này không ở trong cùng khu với con hoặc không tồn tại, ta không thể gửi lời mời!");
                        }
                        break;
                    }
                    case 10: {
                        p.passnew = "";
                        p.passold = str;
                        p.changePassword();
                        Server.menu.sendWrite(p, (short) 11, "Nhập mật khẩu mới");
                        break;
                    }
                    case 11: {
                        p.passnew = str;
                        p.changePassword();
                        break;
                    }
                     case 18: {
                        p.atmnew = "";
                        p.atmold = str;
                        p.changePassatm();
                        Server.menu.sendWrite(p, (short) 19, "Nhập mật khẩu ATM mới");
                        break;
                    }
                    case 19: {
                        p.atmnew = str;
                        p.changePassatm();
                        break;
                    }
                       case 22: {                       
                       
        Menu.doMenuArray(p, new String[]{"Xem Thông tin","Xem Thông tin"});
                        break;
                    }
                case 20: {                       
                        p.passatm = str;
                        p.checkpassatm();                        
                                                             p.upluongMessage(-1000);
                                               p.typemenu = 9998;
        Menu.doMenuArray(p, new String[]{"Xem Thông tin","tặng lượng","đổi coin sang lượng","đổi coin sang xu","mở hành trang","xoá sạch rương đồ","bật/tắt nhận exp","số vé tặng lượng"});
        break;
    
                    }
                    case 21: {
                        p.passatm = str;
                        p.passatm();
                        break;
                    }
                    case 3: {
                        String check = str.replaceAll("\\s+", "");
                        if(!Util.isNumericLong(str) || check.equals("") || !Util.isNumericInt(str)) {
                            Service.chatNPC(p, (short)37, "Giá trị tiền cược nhập vào không đúng");
                            break;
                        }
                        long tienCuoc = Long.parseLong(str);
                        if(tienCuoc > p.c.xu || p.c.xu < 1000) {
                            Service.chatNPC(p, (short)37, "Con không đủ xu để đặt cược");
                            break;
                        }
                        if(tienCuoc < 1000 || tienCuoc%50!=0 ) {
                            Service.chatNPC(p, (short)37, "Xu cược phải lớn hơn 1000 xu và chia hết cho 50");
                            break;
                        }
                        Dun dun = null;
                        if(p.c.dunId != -1) {
                            if (Dun.duns.containsKey(p.c.dunId)) {
                                dun = Dun.duns.get(p.c.dunId);
                            }
                        }
                        if(dun != null) {
                            if(dun.c1.id == p.c.id) {
                                if(dun.tienCuocTeam2 != 0 && dun.tienCuocTeam2 != tienCuoc) {
                                    Service.chatNPC(p, (short)37, "Đối thủ của con đã đặt cược " + Util.getFormatNumber(dun.tienCuocTeam2) + " xu con hãy đặt lại đi!");
                                    return;
                                }
                                if(dun.tienCuocTeam1 != 0) {
                                    Service.chatNPC(p, (short)37, "Con đã đặt cược trước đó rồi.");
                                    return;
                                }

                                dun.tienCuocTeam1 = tienCuoc;
                                p.c.upxuMessage(-tienCuoc);
                                Service.chatNPC(p, (short)37, "Con đã đặt cược " + dun.tienCuocTeam1 + " xu");
                                dun.c2.p.sendAddchatYellow("Người chơi " + dun.c1.name + " đã được cược "+ Util.getFormatNumber(dun.tienCuocTeam1) + " xu.");

                            } else if(dun.c2.id == p.c.id) {
                                if(dun.tienCuocTeam1 != 0 && dun.tienCuocTeam1 != tienCuoc) {
                                    Service.chatNPC(p, (short)37, "Đối thủ của con đã đặt cược " + Util.getFormatNumber(dun.tienCuocTeam1) + " xu con hãy đặt lại đi!");
                                    return;
                                }
                                if(dun.tienCuocTeam2 != 0) {
                                    Service.chatNPC(p, (short)37, "Con đã đặt cược trước đó rồi.");
                                    return;
                                }

                                dun.tienCuocTeam2 = tienCuoc;
                                p.c.upxuMessage(-tienCuoc);
                                Service.chatNPC(p, (short)37, "Con đã đặt cược " + Util.getFormatNumber(dun.tienCuocTeam2) + " xu");
                                dun.c1.p.sendAddchatYellow("Người chơi " + dun.c2.name + " đã được cược "+ Util.getFormatNumber(dun.tienCuocTeam2) + " xu.");
                            }

                            if(dun.tienCuocTeam1 != 0 && dun.tienCuocTeam2 != 0 && dun.tienCuocTeam1 == dun.tienCuocTeam2 && dun.team1.size() > 0 && dun.team2.size() > 0) {
                                if(dun.tienCuocTeam1 >= 1000000L) {
                                    Manager.serverChat("Server: ", "Người chơi " + dun.c1.name + " ("+dun.c1.level+")"
                                            + " đang thách đấu với " + dun.c2.name + " ("+dun.c2.level+"): " + Util.getFormatNumber(dun.tienCuocTeam1) +" xu tại lôi đài, hãy mau mau đến xem và cổ vũ.");
                                }
                                dun.startDun();
                            }
                        }
                        else {
                            return;
                        }
                        break;
                    }
//skgiaikhac
                         //Làm nước dưa hấu
                       case 252: {
                        String check = str.replaceAll("\\s+", "");
                        if(!Util.isNumericInt(str) || check.equals("") || !Util.isNumericInt(str)) {
                            Service.chatNPC(p, (short)33, "Giá trị tiền cược nhập vào không đúng");
                            break;
                        }
                        
                        long soluong = Integer.parseInt(str);
                         if (soluong <= 0) {
                        p.lockAcc();
                        }
                        if (p.c.quantityItemyTotal(945) >= 5*soluong && p.c.quantityItemyTotal(950) >= 5*soluong && p.c.quantityItemyTotal(949) >= 4*soluong ) {                      
                            if(p.c.yen < 50000*soluong ) {
                                p.conn.sendMessageLog("Không đủ yên để làm bánh");
                                return;
                            }
                            if(p.c.xu  < 40000*soluong ) {
                                p.conn.sendMessageLog("Không đủ xu để làm bánh");
                                return;
                            }
                             if(p.luong  < 100*soluong ) {
                                p.conn.sendMessageLog("Không đủ lượng để làm bánh");
                                return;
                            }
                            if (p.c.getBagNull() == 0) {
                                p.conn.sendMessageLog("Hành trang không đủ chỗ trống");
                            } else {
                                p.c.removeItemBags(945, (int)(5*soluong));
                                p.c.removeItemBags(950, (int)(5*soluong));
                                p.c.removeItemBags(949, (int)(3*soluong));
                                p.c.upyenMessage(-(50000*soluong));
                                 p.upluongMessage(-(100*soluong));
                                p.c.upxuMessage(-(40000*soluong));
                                Item it = ItemTemplate.itemDefault(948);
                                it.quantity = (int)(1*soluong);
                                p.c.addItemBag(true, it);
                            }
                            return;
                        } else {
                            Service.chatNPC(p, (short) 33, "Hành trang của con không có đủ nguyên liệu");
                        }
                        break;
                    }
     
                                           // lam nước mía
                    case 253: {
                       String check = str.replaceAll("\\s+", "");
                        if(!Util.isNumericInt(str) || check.equals("") || !Util.isNumericInt(str)) {
                            Service.chatNPC(p, (short)33, "Giá trị tiền cược nhập vào không đúng");
                            break;
                        }
                        
                        long soluong = Integer.parseInt(str);
                         if (soluong <= 0) {
                        p.lockAcc();
                        }
                        if (p.c.quantityItemyTotal(946) >= 5*soluong && p.c.quantityItemyTotal(950) >= 3*soluong && p.c.quantityItemyTotal(949) >= 3*soluong ) {                      
                            if(p.c.yen < 50000*soluong ) {
                                p.conn.sendMessageLog("Không đủ yên để làm bánh");
                                return;
                            }
                            if(p.c.xu  < 30000*soluong ) {
                                p.conn.sendMessageLog("Không đủ xu để làm bánh");
                                return;
                            }
                             if(p.luong  < 50*soluong ) {
                                p.conn.sendMessageLog("Không đủ lượng để làm bánh");
                                return;
                            }
                            if (p.c.getBagNull() == 0) {
                                p.conn.sendMessageLog("Hành trang không đủ chỗ trống");
                            } else {
                                p.c.removeItemBags(946, (int)(5*soluong));
                                p.c.removeItemBags(950, (int)(5*soluong));
                                p.c.removeItemBags(949, (int)(3*soluong));
                                p.c.upyenMessage(-(50000*soluong));
                                 p.upluongMessage(-(50*soluong));
                                p.c.upxuMessage(-(30000*soluong));
                                Item it = ItemTemplate.itemDefault(947);
                                it.quantity = (int)(1*soluong);
                                p.c.addItemBag(true, it);
                            }
                            return;
                        } else {
                            Service.chatNPC(p, (short) 33, "Hành trang của con không có đủ nguyên liệu");
                        }
                        break;
                    }
                    case 1406:{
                if (str.equals("") || Integer.parseInt(str) < 0 || Integer.parseInt(str) > 99){
                    p.conn.sendMessageLog("Vui lòng nhập đúng số từ 0 - 99");
                    return;
                }else {
                    int numLucky = Integer.parseInt(str);
                    p.submitNumLucky(numLucky);
                }
                break;
                }
            case 1405: {
                if (str.equals("") || Integer.parseInt(str) < 1 || Integer.parseInt(str) > 100000){
                    p.conn.sendMessageLog("Số tiền cược không hợp lệ");
                    return;
                }else {
                    int coinLucky = Integer.parseInt(str);
                    p.submitCoinLucky(coinLucky);
                }
                break;
            }
            case 1400: {
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
                        p.conn.sendMessageLog("Chưa thống kê \nkết quả ngày " + dateFormatFile.format(date) +" \nvui lòng quay lại sau");
                        return;
                    }
                    red.close();
                    if (dt.toString().equals(dateFormatFile.format(date).toString())) {
                        red = SQLManager.stat.executeQuery("SELECT `xoso` FROM `player` WHERE `username`='"+p.username+"' LIMIT 1;");
                        red.first();
                        int numXS = red.getInt("xoso");
                        red = SQLManager.stat.executeQuery("SELECT `coinXS` FROM `player` WHERE `username`='"+p.username+"' LIMIT 1;");
                        red.first();
                        int numcoinXS = red.getInt("coinXS");
                        // Check nếu trùng số đặt cược
                        if (numXS == rs){
                            p.conn.sendMessageLog("Chúc mừng con đã trúng thưởng số " + numXS);
                            Manager.chatKTG("" + p.c.name + " nhân phẩm thượng thừa đã đoán trúng KQXS ngày " + dateFormatFile.format(date) +" \nvề số " + numXS + ".");
                            p.upluongMessage(numcoinXS * 80); // Tỉ lệ thưởng 1 ăn 80 đã test lệnh OK
                            // Reset để mai chơi
                            p.xoso = -1;
                            SQLManager.stat.executeUpdate("UPDATE `player` SET `xoso`='" + p.xoso +"' WHERE `id` ='" + p.id + "' LIMIT 1;");
                            p.coinXS = -1;
                            SQLManager.stat.executeUpdate("UPDATE `player` SET `coinXS`='" + p.coinXS +"' WHERE `id` ='" + p.id + "' LIMIT 1;");
                        } else {
                            // Nếu không trùng số sẽ báo nút này
                            p.conn.sendMessageLog("Rất tiếc, chúc con may mắn lần sau!");
                            // Reset để mai chơi
                            p.xoso = -1;
                            SQLManager.stat.executeUpdate("UPDATE `player` SET `xoso`='" + p.xoso +"' WHERE `id` ='" + p.id + "' LIMIT 1;");
                            p.coinXS = -1;
                            SQLManager.stat.executeUpdate("UPDATE `player` SET `coinXS`='" + p.coinXS +"' WHERE `id` ='" + p.id + "' LIMIT 1;");
                        }
                        // Check nếu đã nhấn nhận thưởng rồi sẽ báo nút này
                        if (numXS == -1){
                            p.conn.sendMessageLog("Con đã nhận kết quả rồi, ngày mai quay trở lại đặt cược tiếp tục nhé!");
                        }
                    } else {
                        p.conn.sendMessageLog("Lỗi không xác định!");
                    }
                    return; 
                }
            
               //     sk 10/3 tre xanh
                      case 250: {
                        String check = str.replaceAll("\\s+", "");
                        if(!Util.isNumericInt(str) || check.equals("") || !Util.isNumericInt(str)) {
                            Service.chatNPC(p, (short)33, "Giá trị tiền cược nhập vào không đúng");
                            break;
                        }
                        
                        long soluong = Integer.parseInt(str);
                        if (soluong <= 0) {
                        p.lockAcc();
                        }
                        
                        if (p.c.quantityItemyTotal(590) >= 100*soluong ) {     
                              int itemtruoc = p.c.quantityItemyTotal(592);
                            if(p.c.xu  < 50000*soluong ) {
                                p.conn.sendMessageLog("Không đủ xu để làm bánh");
                                return;
                            }
                            if (p.c.getBagNull() == 0) {
                                p.conn.sendMessageLog("Hành trang không đủ chỗ trống");
                            } else {
                                p.c.removeItemBags(590, (int)(100*soluong));
                                p.c.upxuMessage(-(50000*soluong));
                                Item it = ItemTemplate.itemDefault(592);
                                it.quantity = (int)(1*soluong);
                                p.c.addItemBag(true, it);
                                  int itemsau= p.c.quantityItemyTotal(592);
                                  p.lssktrexanh(p.c.name, itemtruoc, itemsau);
                            }
                            return;
                        } else {
                            Service.chatNPC(p, (short) 33, "Hành trang của con không có đủ nguyên liệu");
                        }
                        break;
                    }
                  //    sk 10 /3 tre vang
                        case 251: {
                        String check = str.replaceAll("\\s+", "");
                        if(!Util.isNumericInt(str) || check.equals("") || !Util.isNumericInt(str)) {
                            Service.chatNPC(p, (short)33, "Giá trị tiền cược nhập vào không đúng");
                            break;
                        }
                        
                        long soluong = Integer.parseInt(str);
                        if (soluong <= 0) {
                        p.lockAcc();
                        }
                        
                        if (p.c.quantityItemyTotal(591) >= 100*soluong ) {  
                               int itemtruoc = p.c.quantityItemyTotal(593);
                            if(p.luong  < 50000*soluong ) {
                                p.conn.sendMessageLog("Không đủ lượng để làm bánh");
                                return;
                            }
                            if (p.c.getBagNull() == 0) {
                                p.conn.sendMessageLog("Hành trang không đủ chỗ trống");
                            } else {
                                p.c.removeItemBags(591, (int)(100*soluong));
                                p.upluongMessage(-(100*soluong));
                                Item it = ItemTemplate.itemDefault(593);
                                it.quantity = (int)(1*soluong);
                                p.c.addItemBag(true, it);
                              int itemsau= p.c.quantityItemyTotal(593);
                                  p.lssktrevang(p.c.name, itemtruoc, itemsau);
                            }
                            return;
                        } else {
                            Service.chatNPC(p, (short) 33, "Hành trang của con không có đủ nguyên liệu");
                        }
                        break;
                    }
                    //Làm bánh chưng
                    case 110: {
                        String check = str.replaceAll("\\s+", "");
                        if(!Util.isNumericInt(str) || check.equals("") || !Util.isNumericInt(str)) {
                            Service.chatNPC(p, (short)33, "Giá trị tiền cược nhập vào không đúng");
                            break;
                        }
                        
                        long soluong = Integer.parseInt(str);
                        if (soluong <= 0) {
                        p.lockAcc();
                        }
                        
                        if (p.c.quantityItemyTotal(638) >= 3*soluong && p.c.quantityItemyTotal(639) >= 5*soluong && p.c.quantityItemyTotal(640) >= 1*soluong && p.c.quantityItemyTotal(641) >= 3*soluong && p.c.quantityItemyTotal(642) >= 2*soluong ) {
                            if(p.c.yen < 50000*soluong ) {
                                p.conn.sendMessageLog("Không đủ yên để làm bánh");
                                return;
                            }
                            if(p.c.xu  < 50000*soluong ) {
                                p.conn.sendMessageLog("Không đủ xu để làm bánh");
                                return;
                            }
                            if (p.c.getBagNull() == 0) {
                                p.conn.sendMessageLog("Hành trang không đủ chỗ trống");
                            } else {
                                p.c.removeItemBags(638, (int)(3*soluong));
                                p.c.removeItemBags(639, (int)(5*soluong));
                                p.c.removeItemBags(640, (int)(1*soluong));
                                p.c.removeItemBags(641, (int)(3*soluong));
                                p.c.removeItemBags(642, (int)(2*soluong));
                                p.c.upyenMessage(-(50000*soluong));
                                p.c.upxuMessage(-(50000*soluong));
                                Item it = ItemTemplate.itemDefault(643);
                                it.quantity = (int)(1*soluong);
                                p.c.addItemBag(true, it);
                            }
                            return;
                        } else {
                            Service.chatNPC(p, (short) 33, "Hành trang của con không có đủ nguyên liệu");
                        }
                        break;
                    }
                    //Làm bánh tét
                    case 111: {
                        String check = str.replaceAll("\\s+", "");
                        if(!Util.isNumericInt(str) || check.equals("") || !Util.isNumericInt(str)) {
                            Service.chatNPC(p, (short)33, "Giá trị tiền cược nhập vào không đúng");
                            break;
                        }
                        long soluong = Integer.parseInt(str);
                        if (soluong <= 0) {
                        p.lockAcc();
                        }
                        if (p.c.quantityItemyTotal(638) >= 2*soluong && p.c.quantityItemyTotal(639) >= 4*soluong && p.c.quantityItemyTotal(641) >= 2*soluong && p.c.quantityItemyTotal(642) >= 4*soluong) {
                            if(p.c.yen < 40000*soluong ) {
                                p.conn.sendMessageLog("Không đủ yên để làm bánh");
                                return;
                            }
                            if(p.c.xu  < 40000*soluong ) {
                                p.conn.sendMessageLog("Không đủ xu để làm bánh");
                                return;
                            }
                            if (p.c.getBagNull() == 0) {
                                p.conn.sendMessageLog("Hành trang không đủ chỗ trống");
                            } else {
                                p.c.removeItemBags(638, (int)(2*soluong));
                                p.c.removeItemBags(639, (int)(4*soluong));
                                p.c.removeItemBags(641, (int)(2*soluong));
                                p.c.removeItemBags(642, (int)(4*soluong));
                                p.c.upyenMessage(-(40000*soluong));
                                p.c.upxuMessage(-(40000*soluong));
                                Item it = ItemTemplate.itemDefault(644);
                                it.quantity = (int)(1*soluong);
                                p.c.addItemBag(true, it);
                            }
                            return;
                        } else {
                            Service.chatNPC(p, (short) 33, "Hành trang của con không có đủ nguyên liệu");
                        }
                        break;
                    }
                    case 41_0:
                        p.nameUS = str;
                        Char n = Client.gI().getNinja(str);
                        if (n != null) {
                            Service.sendInputDialog(p, (short)41_0_0, "ID vật phẩm :");
                        } else {
                            p.sendAddchatYellow("Người chơi không tồn tại hoặc không online");
                        }
                        break;
                    case 41_0_0:
                        p.idItemGF = str;
                        if (p.idItemGF != null) {
                            Service.sendInputDialog(p, (short)41_0_1, "Nhập số lượng :");
                        } else {
                            p.sendAddchatYellow("Nhập sai");
                        }
                        break;
                    case 41_0_1:
                        p.itemQuantityGF = str;
                        p.sendItem();
                        break;
                    case 41_1:
                        p.nameUS = str;
                        Char u = Client.gI().getNinja(str);
                        if (u != null) {
                            Service.sendInputDialog(p, (short)41_1_0, "ID vật phẩm :");
                        } else {
                            p.sendAddchatYellow("Người chơi không tồn tại hoặc không online");
                        }
                        break;
                    case 41_1_0:
                        p.idItemGF = str;
                        if (p.idItemGF != null) {
                            Service.sendInputDialog(p, (short)41_1_1, "Nhập số lượng :");
                        } else {
                            p.sendAddchatYellow("Nhập sai");
                        }
                        break;
                    case 41_1_1:
                        p.itemQuantityGF = str;
                        if (p.idItemGF != null) {
                            Service.sendInputDialog(p, (short)41_1_2, "Nhập cấp độ cho trang bị :");
                        } else {
                            p.sendAddchatYellow("Nhập sai");
                        }
                        break;
                    case 41_1_2:
                        p.itemUpgradeGF = str;
                        if (p.idItemGF != null) {
                            Service.sendInputDialog(p, (short)41_1_3, "Nhập hệ trang bị:");
                        } else {
                            p.sendAddchatYellow("Nhập sai");
                        }
                        break;
                    case 41_1_3:
                        p.itemSysGF = str;
                        p.sendTB();
                        break;
                    //pháo
                    case 112: {
                        String check = str.replaceAll("\\s+", "");
                        if(!Util.isNumericInt(str) || check.equals("") || !Util.isNumericInt(str)) {
                            Service.chatNPC(p, (short)33, "Giá trị tiền cược nhập vào không đúng");
                            break;
                        }
                        long soluong = Integer.parseInt(str);
                        if (soluong <= 0) {
                        p.lockAcc();
                        }
                        if (p.c.quantityItemyTotal(674) >= 10*soluong){
                            if(p.c.yen < 30000*soluong ) {
                                p.conn.sendMessageLog("Không đủ yên để làm bánh");
                                return;
                            }
                            if(p.c.xu  < 30000*soluong ) {
                                p.conn.sendMessageLog("Không đủ xu để làm bánh");
                                return;
                            }
                            if (p.c.getBagNull() == 0) {
                                p.conn.sendMessageLog("Hành trang không đủ chỗ trống");
                            } else {
                                p.c.removeItemBags(674, (int)(10*soluong));
                                 p.c.upyenMessage(-(30000*soluong));
                                 p.c.upxuMessage(-(30000*soluong));
                                Item it = ItemTemplate.itemDefault(675);
                                it.quantity = (int)(1*soluong);
                                p.c.addItemBag(true, it);
                            }
                            return;
                        } else {
                            Service.chatNPC(p, (short) 33, "Hành trang của con không có đủ nguyên liệu");
                        }
                        break;
                    }
                    
      /*              case 113:
                    p.nameUS = str;
                    Char a10 = Client.gI().getNinja(str);
                    if(a10 != null && a10.id == p.c.id) {
                        p.conn.sendMessageLog("Không thể lì xì cho bản thân.");
                        return;
                    }
                    if (a10 != null ) {
                        p.sendDo();
                    break;
                    } else {
                        p.sendAddchatYellow("Nhân vật này không tồn tại hoặc không online.");
                    }
                    break;
                    
                    //thiệp chúc thường
                    case 114:
                    p.nameUS = str;
                    Char a12 = Client.gI().getNinja(str);
                    
                    if(a12 != null && a12.id == p.c.id) {
                        p.conn.sendMessageLog("Không thể chúc chính mình,đừng thủ dâm tinh thần như thế chứ.");
                        return;
                    }
                    if (a12 != null) {
                        Service.sendInputDialog(p, (short) 116, "Nhập lời nhắn:");
                    } else {
                        p.sendAddchatYellow("Nhân vật này không tồn tại hoặc không online.");
                    }
                    break;
                    
                    //thiệp chúc vip
                    case 115:
                    p.nameUS = str;
                    Char a13 = Client.gI().getNinja(str);
                    if(a13 != null && a13.id == p.c.id) {
                        p.conn.sendMessageLog("Không thể chúc chính mình,đừng thủ dâm tinh thần như thế chứ.");
                        return;
                    }
                    if (a13 != null) {
                       Service.sendInputDialog(p, (short) 117, "Nhập lời nhắn:");
                    } else {
                        p.sendAddchatYellow("Nhân vật này không tồn tại hoặc không online.");
                    }
                    break;
                    
                    //loi nhan thuong
                    case 116:
                    p.messTB = str;
                    p.sendTB();
                    break; 
                    
                    //loi nhan vip
                    case 117:
                    p.messTB = str;
                    p.sendTB2x();
                    break; */
                    //gift code
                    case 4: {
                        String check = str.replaceAll("\\s+", "");
                        if(check.equals("") && !Util.isNumeric(str)){
                            p.conn.sendMessageLog("Mã Gift code nhập vào không hợp lệ.");
                            break;
                        }
                        if (!(Util.checkNumInt("^[a-zA-Z0-9]+$"))) {
                             p.conn.sendMessageLog("Lỗi xác nhận mã Gift code. Hãy liên hệ Admin để biết thêm chi tiết.");
                }
                        check = check.toUpperCase();
                        try {
                          if (!(Util.checkNumInt("^[a-zA-Z0-9]+$"))) {
                             p.conn.sendMessageLog("Lỗi xác nhận mã Gift code. Hãy liên hệ Admin để biết thêm chi tiết.");
                }
                            synchronized (Server.LOCK_MYSQL) {
                                ResultSet red = SQLManager.stat.executeQuery("SELECT * FROM `gift_code` WHERE `code` LIKE '" + check + "';");
                                if (red != null && red.first()) {
                                    int id = red.getInt("id");
                                    String code = red.getString("code");
                                    JSONArray jar = (JSONArray) JSONValue.parse(red.getString("item_id"));
                                    if(p.c.getBagNull() < jar.size()) {
                                        p.conn.sendMessageLog(Language.NOT_ENOUGH_BAG);
                                        break;
                                    }
                                    int j;
                                    int[] itemId = new int[jar.size()];
                                    for (j = 0; j < jar.size(); j++) {
                                        itemId[j] = Integer.parseInt(jar.get(j).toString());
                                    }
                                    jar = (JSONArray) JSONValue.parse(red.getString("item_quantity"));
                                    long[] itemQuantity = new long[jar.size()];
                                    for (j = 0; j < jar.size(); j++) {
                                        itemQuantity[j] = Long.parseLong(jar.get(j).toString());
                                    }
                                    jar = (JSONArray) JSONValue.parse(red.getString("item_isLock"));
                                    byte[] itemIsLock = new byte[jar.size()];
                                    for (j = 0; j < jar.size(); j++) {
                                        itemIsLock[j] = Byte.parseByte(jar.get(j).toString());
                                    }
                                    jar = (JSONArray) JSONValue.parse(red.getString("item_expires"));
                                    long[] itemExpires = new long[jar.size()];
                                    for (j = 0; j < jar.size(); j++) {
                                        itemExpires[j] = Long.parseLong(jar.get(j).toString());
                                    }

                                    int isPlayer = red.getInt("isPlayer");
                                    int isTime = red.getInt("isTime");
                                    if(isPlayer == 1) {
                                        jar = (JSONArray) JSONValue.parse(red.getString("player"));
                                        boolean checkUser = false;
                                        for (j = 0; j < jar.size(); j++) {
                                            if(jar.get(j).toString().equals(p.username)){
                                                checkUser = true;
                                                break;
                                            }
                                        }
                                         if (!(Util.checkNumInt("^[a-zA-Z0-9]+$"))) {
                             p.conn.sendMessageLog("Lỗi xác nhận mã Gift code. Hãy liên hệ Admin để biết thêm chi tiết.");
                               red.close();
                                 break;
                }
                                        if(!checkUser) {
                                            p.conn.sendMessageLog("Bạn không thể sử dụng mã Gift Code này.");
                                            red.close();
                                            break;
                                        }
                                    }
                                    if(isTime == 1) {
                                        if(Date.from(Instant.now()).compareTo(Util.getDate(red.getString("time"))) > 0) {
                                            p.conn.sendMessageLog("Mã Gift code này đã hết hạn sử dụng.");
                                            red.close();
                                            break;
                                        }
                                    }
                                    red.close();
                                    red = SQLManager.stat.executeQuery("SELECT * FROM `history_gift` WHERE `player_id` = "+p.id+" AND `code` = '"+code+"';");
                                    if(red != null && red.first()) {
                                        p.conn.sendMessageLog("Bạn đã sử dụng mã Gift code này rồi.");
                                    }
                                    
                                    else {
                                        if(itemId.length == itemQuantity.length) {
                                            ItemTemplate data2;
                                            int i;
                                            for(i = 0; i < itemId.length; i++) {
                                                if(itemId[i] == -3) {
                                                    p.c.upyenMessage(itemQuantity[i]);
                                                } else if(itemId[i] == -2) {
                                                    p.c.upxuMessage(itemQuantity[i]);
                                                } else if(itemId[i] == -1) {
                                                    p.upluongMessage(itemQuantity[i]);
                                                } else {
                                                    data2 = ItemTemplate.ItemTemplateId(itemId[i]);
                                                    if(data2 != null) {
                                                        Item itemup;
                                                        if (data2.type < 10) {
                                                            if (data2.type == 1) {
                                                                itemup = ItemTemplate.itemDefault(itemId[i]);
                                                                itemup.sys = GameSrc.SysClass(data2.nclass);
                                                            } else {
                                                                byte sys = (byte) Util.nextInt(1, 3);
                                                                itemup = ItemTemplate.itemDefault(itemId[i], sys);
                                                            }
                                                        } else {
                                                            itemup = ItemTemplate.itemDefault(itemId[i]);
                                                        }
                                                        itemup.quantity = (int)itemQuantity[i];
                                                        if(itemIsLock[i] == 0) {
                                                            itemup.isLock = false;
                                                        } else {
                                                            itemup.isLock = true;
                                                        }
                                                        if(itemExpires[i] != -1) {
                                                            itemup.isExpires = true;
                                                            itemup.expires = System.currentTimeMillis() + itemExpires[i];
                                                        } else {
                                                            itemup.isExpires = false;
                                                        }
                                                        p.c.addItemBag(true, itemup);
                                                    }
                                                }
                                            }
                                            String sqlSET = "("+p.id+", '" +code+ "', '"+Util.toDateString(Date.from(Instant.now()))+"', '"+Util.toDateString(Date.from(Instant.now()))+"', '"+Util.toDateString(Date.from(Instant.now()))+"');";
                                            SQLManager.stat.executeUpdate("INSERT INTO `history_gift` (`player_id`,`code`,`time`, `created_at`, `updated_at`) VALUES " + sqlSET);
                                        }
                                         
                                        else {
                                            p.conn.sendMessageLog("Lỗi xác nhận mã Gift code. Hãy liên hệ Admin để biết thêm chi tiết.");
                                        }
                                    }
                                    jar.clear();
                                    red.close();
                                    break;
                                }
                                else {
                                    p.conn.sendMessageLog("Mã Gift code này đã được sử dụng hoặc không tồn tại.");
                                    red.close();
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }

                    //Mời gia tộc chiến
                    case 5: {
                        ClanManager temp = ClanManager.getClanName(str);
                        ClanManager temp2 = ClanManager.getClanName(p.c.clan.clanName);
                        if(temp != null) {
                            String tocTruong = temp.getmain_name();
                            Char _charTT = Client.gI().getNinja(tocTruong);
                            if(_charTT != null && _charTT.id == p.c.id) {
                                Service.chatNPC(p, (short) 32, "Ngươi muốn thách đấu gia tộc của chính mình à.");
                            } else if (_charTT != null && _charTT.id != p.c.id) {
                                if(temp.gtcID != -1 && temp.gtcClanName != null) {
                                    Service.chatNPC(p, (short) 32, "Gia tộc này đang có lời mời từ gia tộc khác");
                                    return;
                                }
                                Service.startYesNoDlg(_charTT.p, (byte) 4, "Gia tộc "+p.c.clan.clanName+" muốn thách đấu với gia tộc của bạn. Bạn có đồng ý?");
                                GiaTocChien giaTocChien = new GiaTocChien();
                                temp.gtcID = giaTocChien.gtcID;
                                temp.gtcClanName = p.c.clan.clanName;
                                temp2.gtcID = giaTocChien.gtcID;
                                temp2.gtcClanName = str;
                                Service.chatNPC(p, (short) 32, "Ta đã gửi lời mời thách đấu tới gia tộc " + str);
                            } else {
                                Service.chatNPC(p, (short) 32, "Tộc trưởng gia tộc đối phương không online hoặc không tồn tại. Không thể gửi lời mời.");
                            }
                        } else {
                            Service.chatNPC(p, (short) 32, "Gia tộc này không tồn tại, ta không thể gửi lời mời!");
                        }
                        break;
                    }
                    
                    
                    case 44_0_0: {
                        String check = str.replaceAll("\\s+", "");
                        if(!Util.isNumericInt(str) || check.equals("") || check.equals("0") || !Util.isNumericInt(str)) {
                            Service.chatNPC(p, (short)44, "Giá trị nhập vào không đúng");
                            break;
                        }
                        int joincoin = Integer.parseInt(str);
                        try {
                                if (joincoin <= 0 || joincoin %10 !=0) {
                                    p.conn.sendMessageLog("?????");
                                    return;
                                }
                                if(joincoin <= p.coin) {
                                    p.coin = p.coin - joincoin;
                                    SQLManager.stat.executeUpdate("UPDATE `player` SET `coin`=" + p.coin + " WHERE `id`=" + p.id + " LIMIT 1;");
                                    p.conn.sendMessageLog("Chờ 15 giây để biết kết quả.");
                                    int TimeSeconds =15;
                                    while (TimeSeconds > 0) {
                                        TimeSeconds--;
                                        Thread.sleep(1000);
                                    }
                                        int x = Util.nextInt(1, 6);
                                        int y = Util.nextInt(1, 6);
                                        int z = Util.nextInt(1, 6);
                                            if (4 <= (x+y+z) && (x+y+z) <= 10) {
                                                Server.manager.sendTB(p, "Kết quả", "Số hệ thống quay ra : "+ x +" "+y+" "+z
                                                                +"\nTổng là : " + (x+y+z)
                                                                +"\nBạn đã cược : " + joincoin + " coin vào Tài"
                                                                + "\nKết quả : Xỉu"
                                                                + "\nCòn cái nịt."
                                                                + "\nSố coin hiện tại của bạn là : " + p.coin);
                                                CheckTXCoin.checkTXCoinArrayList.add(new CheckTXCoin(p.c.name, "Kết quả Xỉu ("+(x+y+z)+") thua " + joincoin + " coin", Util.toDateString(Date.from(Instant.now()))));
                                                SQLManager.stat.executeUpdate("UPDATE `player` SET `coin`=" + p.coin + " WHERE `id`=" + p.id + " LIMIT 1;");
                                                return;
                                            } else if (x==y && x==z) {
                                                Server.manager.sendTB(p, "Kết quả", "Số hệ thống quay ra : "+ x +" "+y+" "+z
                                                                +"\nTổng là : " + (x+y+z)
                                                                +"\nBạn đã cược : " + joincoin + " coin vào Tài"
                                                                + "\nKết quả : Tam hoa"
                                                                + "\nCòn cái nịt."
                                                                + "\nSố coin hiện tại của bạn là : " + p.coin);
                                                CheckTXCoin.checkTXCoinArrayList.add(new CheckTXCoin(p.c.name, "Kết quả Tam Hoa ("+(x+y+z)+") thua " + joincoin + " coin", Util.toDateString(Date.from(Instant.now()))));
                                                SQLManager.stat.executeUpdate("UPDATE `player` SET `coin`=" + p.coin + " WHERE `id`=" + p.id + " LIMIT 1;");
                                                return;
                                            } else if ((x+y+z) > 10){
                                                p.coin = p.coin + joincoin*19/10;
                                                Server.manager.sendTB(p, "Kết quả", "Số hệ thống quay ra là : "+ x +" "+y+" "+z
                                                                +"\nTổng là : " + (x+y+z)
                                                                +"\nBạn đã cược : " + joincoin + " coin vào Tài"
                                                                + "\nKết quả : Tài"
                                                                + "\nVề bờ"
                                                                + "\nSố coin hiện tại của bạn là : " + p.coin);
                                                CheckTXCoin.checkTXCoinArrayList.add(new CheckTXCoin(p.c.name, "Kết quả Tài ("+(x+y+z)+") ăn " + joincoin*19/10 + " coin", Util.toDateString(Date.from(Instant.now()))));
                                                SQLManager.stat.executeUpdate("UPDATE `player` SET `coin`=" + p.coin + " WHERE `id`=" + p.id + " LIMIT 1;");
                                                return;
                                            }
                                } else {
                                    p.conn.sendMessageLog("Bạn không đủ coin để chơi.");
                                }
                        } catch (Exception e) {
                            e.printStackTrace();
                            p.conn.sendMessageLog("Lỗi.");
                        }
                        break;
                    }
                    
                    case 44_0_1: {
                        String check = str.replaceAll("\\s+", "");
                        if(!Util.isNumericInt(str) || check.equals("") || check.equals("0") || !Util.isNumericInt(str)) {
                            Service.chatNPC(p, (short)44, "Giá trị nhập vào không đúng");
                            break;
                        }
                        int joincoinx = Integer.parseInt(str);
                        try {
                                if (joincoinx <= 0 || joincoinx %10 !=0) {
                                    p.conn.sendMessageLog("?????");
                                    return;
                                }
                                if(joincoinx <= p.coin) {
                                    p.coin = p.coin - joincoinx;
                                    SQLManager.stat.executeUpdate("UPDATE `player` SET `coin`=" + p.coin + " WHERE `id`=" + p.id + " LIMIT 1;");
                                    p.conn.sendMessageLog("Chờ 15 giây để biết kết quả.");
                                    int TimeSeconds =15;
                                    while (TimeSeconds > 0) {
                                        TimeSeconds--;
                                        Thread.sleep(1000);
                                    }
                                        int x = Util.nextInt(1, 6);
                                        int y = Util.nextInt(1, 6);
                                        int z = Util.nextInt(1, 6);
                                            if (4 > (x+y+z) || (x+y+z) > 10) {
                                                Server.manager.sendTB(p, "Kết quả", "Số hệ thống quay ra : "+ x +" "+y+" "+z
                                                                +"\nTổng là : " + (x+y+z)
                                                                +"\nBạn đã cược : " + joincoinx + " coin vào Xỉu"
                                                                + "\nKết quả : Tài"
                                                                + "\nCòn cái nịt."
                                                                + "\nSố coin hiện tại của bạn là : " + p.coin);
                                                CheckTXCoin.checkTXCoinArrayList.add(new CheckTXCoin(p.c.name, "Kết quả Tài ("+(x+y+z)+") thua " + joincoinx + " coin", Util.toDateString(Date.from(Instant.now()))));
                                                SQLManager.stat.executeUpdate("UPDATE `player` SET `coin`=" + p.coin + " WHERE `id`=" + p.id + " LIMIT 1;");
                                                return;
                                            } else if (x==y && x==z) {
                                                Server.manager.sendTB(p, "Kết quả", "Số hệ thống quay ra : "+ x +" "+y+" "+z
                                                                +"\nTổng là : " + (x+y+z)
                                                                +"\nBạn đã cược : " + joincoinx + " coin vào Xỉu"
                                                                + "\nKết quả : Tam hoa"
                                                                + "\nCòn cái nịt."
                                                                + "\nSố coin hiện tại của bạn là : " + p.coin);
                                                CheckTXCoin.checkTXCoinArrayList.add(new CheckTXCoin(p.c.name, "Kết quả Tam Hoa ("+(x+y+z)+") thua " + joincoinx + " coin", Util.toDateString(Date.from(Instant.now()))));
                                                SQLManager.stat.executeUpdate("UPDATE `player` SET `coin`=" + p.coin + " WHERE `id`=" + p.id + " LIMIT 1;");
                                                return;
                                            } else if ((x+y+z) <= 10 && (x+y+z) >= 4 ){
                                                p.coin = p.coin + joincoinx*19/10;
                                                Server.manager.sendTB(p, "Kết quả", "Số hệ thống quay ra là : "+ x +" "+y+" "+z
                                                                +"\nTổng là : " + (x+y+z)
                                                                +"\nBạn đã cược : " + joincoinx + " coin vào Xỉu"
                                                                + "\nKết quả : Xỉu"
                                                                + "\nVề bờ"
                                                                + "\nSố coin hiện tại của bạn là : " + p.coin);
                                                CheckTXCoin.checkTXCoinArrayList.add(new CheckTXCoin(p.c.name, "Kết quả Xỉu ("+(x+y+z)+") ăn " + joincoinx*19/10 + " coin", Util.toDateString(Date.from(Instant.now()))));
                                                SQLManager.stat.executeUpdate("UPDATE `player` SET `coin`=" + p.coin + " WHERE `id`=" + p.id + " LIMIT 1;");
                                                return;
                                            }
                                } else {
                                    p.conn.sendMessageLog("Bạn không đủ coin để chơi.");
                                }
                        } catch (Exception e) {
                            e.printStackTrace();
                            p.conn.sendMessageLog("Lỗi.");
                        }
                        break;
                    }
                    // cltx xu
                    case 46_0_0: {
                        String check = str.replaceAll("\\s+", "");
                        if(!Util.isNumericInt(str) || check.equals("") || check.equals("0") || !Util.isNumericInt(str)) {
                            Service.chatNPC(p, (short)45, "Giá trị nhập vào không đúng");
                            break;
                        }
                        long joinxut = Integer.parseInt(str);
                        try {
                                if (joinxut <= 0 || joinxut %10 !=0) {
                                    p.conn.sendMessageLog("?????");
                                    return;
                                }
                                if(joinxut <= p.c.xu) {
                                    p.c.upxuMessage(-joinxut);
                                    int TimeSeconds =15;
                                    p.conn.sendMessageLog("Chờ 15 giây để biết kết quả.");
                                    while (TimeSeconds > 0) {
                                        TimeSeconds--;
                                        Thread.sleep(1000);
                                    }
                                        int x = Util.nextInt(1, 6);
                                        int y = Util.nextInt(1, 6);
                                        int z = Util.nextInt(1, 6);
                                            if (4 <= (x+y+z) && (x+y+z) <= 10) {
                                                Server.manager.sendTB(p, "Kết quả", "Số hệ thống quay ra : "+ x +" "+y+" "+z
                                                                +"\nTổng là : " + (x+y+z)
                                                                +"\nBạn đã cược : " + joinxut + " xu vào Tài"
                                                                + "\nKết quả : Xỉu"
                                                                + "\nCòn cái nịt.");
                                                CheckTXXu.checkTXXuArrayList.add(new CheckTXXu(p.c.name, "Kết quả Xỉu ("+(x+y+z)+") thua " + joinxut + " xu", Util.toDateString(Date.from(Instant.now()))));
                                                return;
                                            } else if (x==y && x==z) {
                                                Server.manager.sendTB(p, "Kết quả", "Số hệ thống quay ra : "+ x +" "+y+" "+z
                                                                +"\nTổng là : " + (x+y+z)
                                                                +"\nBạn đã cược : " + joinxut + " xu vào Tài"
                                                                + "\nKết quả : Tam hoa"
                                                                + "\nCòn cái nịt.");
                                                CheckTXXu.checkTXXuArrayList.add(new CheckTXXu(p.c.name, "Kết quả Tam Hoa ("+(x+y+z)+") thua " + joinxut + " xu", Util.toDateString(Date.from(Instant.now()))));
                                                return;
                                            } else if ((x+y+z) > 10){
                                                p.c.upxuMessage(joinxut*19/10L);
                                                Server.manager.sendTB(p, "Kết quả", "Số hệ thống quay ra là : "+ x +" "+y+" "+z
                                                                +"\nTổng là : " + (x+y+z)
                                                                +"\nBạn đã cược : " + joinxut + " xu vào Tài"
                                                                + "\nKết quả : Tài"
                                                                + "\nVề bờ");
                                                CheckTXXu.checkTXXuArrayList.add(new CheckTXXu(p.c.name, "Kết quả Tài ("+(x+y+z)+") ăn " + joinxut*19/10 + " xu", Util.toDateString(Date.from(Instant.now()))));
                                                return;
                                            }
                                } else {
                                    p.conn.sendMessageLog("Bạn không đủ xu để chơi.");
                                }
                        } catch (Exception e) {
                            e.printStackTrace();
                            p.conn.sendMessageLog("Lỗi.");
                        }
                        break;
                    }
                    case 46_0_1: {
                        String check = str.replaceAll("\\s+", "");
                        if(!Util.isNumericInt(str) || check.equals("") || check.equals("0") || !Util.isNumericInt(str)) {
                            Service.chatNPC(p, (short)45, "Giá trị nhập vào không đúng");
                            break;
                        }
                        int joinxux = Integer.parseInt(str);
                        try {
                                if (joinxux <= 0 || joinxux %10 !=0) {
                                    p.conn.sendMessageLog("?????");
                                    return;
                                }
                                if(joinxux <= p.c.xu) {
                                    p.c.upxuMessage(-joinxux);
                                    p.conn.sendMessageLog("Chờ 15 giây để biết kết quả.");int TimeSeconds =15;
                                    while (TimeSeconds > 0) {
                                        TimeSeconds--;
                                        Thread.sleep(1000);
                                    }
                                        int x = Util.nextInt(1, 6);
                                        int y = Util.nextInt(1, 6);
                                        int z = Util.nextInt(1, 6);
                                            if (4 > (x+y+z) || (x+y+z) > 10) {
                                                Server.manager.sendTB(p, "Kết quả", "Số hệ thống quay ra : "+ x +" "+y+" "+z
                                                                +"\nTổng là : " + (x+y+z)
                                                                +"\nBạn đã cược : " + joinxux + " xu vào Xỉu"
                                                                + "\nKết quả : Tài"
                                                                + "\nCòn cái nịt.");
                                                CheckTXXu.checkTXXuArrayList.add(new CheckTXXu(p.c.name, "Kết quả Tài ("+(x+y+z)+") thua " + joinxux + " xu", Util.toDateString(Date.from(Instant.now()))));
                                                return;
                                            } else if (x==y && x==z) {
                                                Server.manager.sendTB(p, "Kết quả", "Số hệ thống quay ra : "+ x +" "+y+" "+z
                                                                +"\nTổng là : " + (x+y+z)
                                                                +"\nBạn đã cược : " + joinxux + " xu vào Xỉu"
                                                                + "\nKết quả : Tam hoa"
                                                                + "\nCòn cái nịt.");
                                                CheckTXXu.checkTXXuArrayList.add(new CheckTXXu(p.c.name, "Kết quả Tam Hoa ("+(x+y+z)+") thua " + joinxux + " xu", Util.toDateString(Date.from(Instant.now()))));
                                                return;
                                            } else if ((x+y+z) <= 10 && (x+y+z) >= 4 ){
                                                p.c.upxuMessage(joinxux*19/10L);
                                                Server.manager.sendTB(p, "Kết quả", "Số hệ thống quay ra là : "+ x +" "+y+" "+z
                                                                +"\nTổng là : " + (x+y+z)
                                                                +"\nBạn đã cược : " + joinxux + " xu vào Xỉu"
                                                                + "\nKết quả : Xỉu"
                                                                + "\nVề bờ");
                                                CheckTXXu.checkTXXuArrayList.add(new CheckTXXu(p.c.name, "Kết quả Xỉu ("+(x+y+z)+") ăn " + joinxux*19/10 + " xu", Util.toDateString(Date.from(Instant.now()))));
                                                
                                                return;
                                            }
                                } else {
                                    p.conn.sendMessageLog("Bạn không đủ xu để chơi.");
                                }
                        } catch (Exception e) {
                            e.printStackTrace();
                            p.conn.sendMessageLog("Lỗi.");
                        }
                        break;
                    }
                    case 46_1_0:{
                        String check = str.replaceAll("\\s+", "");
                        if(!Util.isNumericInt(str) || check.equals("") || check.equals("0") || !Util.isNumericInt(str)) {
                            Service.chatNPC(p, (short)45, "Giá trị nhập vào không đúng");
                            break;
                        }
                        long joinxuc = Integer.parseInt(str);
                        try {
                                if (joinxuc <= 0 || joinxuc %10 !=0) {
                                    p.conn.sendMessageLog("?????");
                                    return;
                                }
                                if(joinxuc <= p.c.xu) {
                                    p.c.upxuMessage(-joinxuc);
                                    p.conn.sendMessageLog("Chờ 15 giây để biết kết quả.");
                                    int TimeSeconds =15;
                                    while (TimeSeconds > 0) {
                                        TimeSeconds--;
                                        Thread.sleep(1000);
                                    }
                                        int x = Util.nextInt(1, 9);
                                        int y = Util.nextInt(1, 9);
                                        int z = Util.nextInt(1, 9);
                                        int t = Util.nextInt(1, 9);
                                            if ( (x+y+z+t) %2 != 0 ) {
                                                Server.manager.sendTB(p, "Kết quả", "Số hệ thống quay ra : "+ x +" "+y+" "+z+" "+t
                                                                +"\nTổng là : " + (x+y+z+t)
                                                                +"\nBạn đã cược : " + joinxuc + " xu vào Chẵn"
                                                                + "\nKết quả : Lẻ"
                                                                + "\nCòn cái nịt.");
                                                CheckCLXu.checkCLXuArrayList.add(new CheckCLXu(p.c.name, "Kết quả Lẻ ("+(x+y+z+t)+") thua " + joinxuc + " xu", Util.toDateString(Date.from(Instant.now()))));
                                                return;
                                            } else if ((x+y+z+t) %2 == 0 ){
                                                p.c.upxuMessage(joinxuc*19/10L);
                                                Server.manager.sendTB(p, "Kết quả", "Số hệ thống quay ra là : "+ x +" "+y+" "+z+" "+t
                                                                +"\nTổng là : " + (x+y+z+t)
                                                                +"\nBạn đã cược : " + joinxuc + " xu vào Chẵn"
                                                                + "\nKết quả : Chẵn"
                                                                + "\nVề bờ");
                                                CheckCLXu.checkCLXuArrayList.add(new CheckCLXu(p.c.name, "Kết quả Chẵn ("+(x+y+z+t)+") ăn " + joinxuc*19/10 + " xu", Util.toDateString(Date.from(Instant.now()))));
                                                return;
                                            }
                                } else {
                                    p.conn.sendMessageLog("Bạn không đủ xu để chơi.");
                                }
                        } catch (Exception e) {
                            e.printStackTrace();
                            p.conn.sendMessageLog("Lỗi.");
                        }
                        break;
                    }
                    case 46_1_1:{
                        String check = str.replaceAll("\\s+", "");
                        if(!Util.isNumericInt(str) || check.equals("") || check.equals("0") || !Util.isNumericInt(str)) {
                            Service.chatNPC(p, (short)45, "Giá trị nhập vào không đúng");
                            break;
                        }
                        long joinxul = Integer.parseInt(str);
                        try {
                                if (joinxul <= 0 || joinxul %10 !=0) {
                                    p.conn.sendMessageLog("?????");
                                    return;
                                }
                                if(joinxul <= p.c.xu) {
                                    p.c.upxuMessage(-joinxul);
                                    p.conn.sendMessageLog("Chờ 15 giây để biết kết quả.");
                                    int TimeSeconds =15;
                                    while (TimeSeconds > 0) {
                                        TimeSeconds--;
                                        Thread.sleep(1000);
                                    }
                                        int x = Util.nextInt(1, 9);
                                        int y = Util.nextInt(1, 9);
                                        int z = Util.nextInt(1, 9);
                                        int t = Util.nextInt(1, 9);
                                            if ( (x+y+z+t) %2 == 0 ) {
                                                Server.manager.sendTB(p, "Kết quả", "Số hệ thống quay ra : "+ x +" "+y+" "+z+" "+t
                                                                +"\nTổng là : " + (x+y+z+t)
                                                                +"\nBạn đã cược : " + joinxul + " xu vào Lẻ"
                                                                + "\nKết quả : Chẵn"
                                                                + "\nCòn cái nịt.");
                                                CheckCLXu.checkCLXuArrayList.add(new CheckCLXu(p.c.name, "Kết quả Chẵn ("+(x+y+z+t)+") thua " + joinxul + " xu", Util.toDateString(Date.from(Instant.now()))));
                                                return;
                                            } else if ((x+y+z+t) %2 != 0 ){
                                                p.c.upxuMessage(joinxul*19/10L);
                                                Server.manager.sendTB(p, "Kết quả", "Số hệ thống quay ra là : "+ x +" "+y+" "+z+" "+t
                                                                +"\nTổng là : " + (x+y+z+t)
                                                                +"\nBạn đã cược : " + joinxul + " xu vào Lẻ"
                                                                + "\nKết quả : Lẻ"
                                                                + "\nVề bờ");
                                                CheckCLXu.checkCLXuArrayList.add(new CheckCLXu(p.c.name, "Kết quả Lẻ ("+(x+y+z+t)+") ăn " + joinxul*19/10 + " xu", Util.toDateString(Date.from(Instant.now()))));
                                                return;
                                            }
                                } else {
                                    p.conn.sendMessageLog("Bạn không đủ xu để chơi.");
                                }
                        } catch (Exception e) {
                            e.printStackTrace();
                            p.conn.sendMessageLog("Lỗi.");
                        }
                        break;
                    }
                    case 1234: {
                        String check = str.replaceAll("\\s+", "");
                        if(!Util.isNumericInt(str) || check.equals("") || check.equals("-") || check.equals("0")) {
                            Service.chatNPC(p, (short)36, "Giá trị coin nhập vào không đúng");
                            break;
                        }
                        int luong = Integer.parseInt(str);
                        try {
                            ResultSet red = SQLManager.stat.executeQuery("SELECT `coin` FROM `player` WHERE `id` = "+p.id+";");
                            if (red != null && red.first()) {
                                int coin = Integer.parseInt(red.getString("coin"));
                                if(luong <= p.luong) {
                                    if (coin <= 0) {
                                   p.lockAcc();
                                    }
                                    int coinnew = coin + (luong /2);
                                    p.upluongMessage(-luong);
                                    SQLManager.stat.executeUpdate("UPDATE `player` SET `coin`=" + coinnew + " WHERE `id`=" + p.id + " LIMIT 1;");
                                    p.flush();
                                red.close();
                                } else {
                                    p.conn.sendMessageLog("Bạn không đủ coin để đổi ra lượng.");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            p.conn.sendMessageLog("Lỗi đổi coin.");
                        }
                        break;
                    }
                    case 44_1_0:{
                        String check = str.replaceAll("\\s+", "");
                        if(!Util.isNumericInt(str) || check.equals("") || check.equals("0") || !Util.isNumericInt(str)) {
                            Service.chatNPC(p, (short)44, "Giá trị nhập vào không đúng");
                            break;
                        }
                        int joincoinc = Integer.parseInt(str);
                        try {
                                if (joincoinc <= 0 || joincoinc %10 !=0) {
                                    p.conn.sendMessageLog("?????");
                                    return;
                                }
                                if(joincoinc <= p.coin) {
                                    p.coin = p.coin - joincoinc;
                                    SQLManager.stat.executeUpdate("UPDATE `player` SET `coin`=" + p.coin + " WHERE `id`=" + p.id + " LIMIT 1;");
                                    p.conn.sendMessageLog("Chờ 15 giây để biết kết quả.");
                                    int TimeSeconds =15;
                                    while (TimeSeconds > 0) {
                                        TimeSeconds--;
                                        Thread.sleep(1000);
                                    }
                                        int x = Util.nextInt(1, 9);
                                        int y = Util.nextInt(1, 9);
                                        int z = Util.nextInt(1, 9);
                                        int t = Util.nextInt(1, 9);
                                            if ( (x+y+z+t) %2 != 0 ) {
                                                Server.manager.sendTB(p, "Kết quả", "Số hệ thống quay ra : "+ x +" "+y+" "+z+" "+t
                                                                +"\nTổng là : " + (x+y+z+t)
                                                                +"\nBạn đã cược : " + joincoinc + " coin vào Chẵn"
                                                                + "\nKết quả : Lẻ"
                                                                + "\nCòn cái nịt."
                                                                + "\nSố coin hiện tại của bạn là : " + p.coin);
                                                CheckCLCoin.checkCLCoinArrayList.add(new CheckCLCoin(p.c.name, "Kết quả Lẻ ("+(x+y+z+t)+") thua " + joincoinc + " coin", Util.toDateString(Date.from(Instant.now()))));
                                                SQLManager.stat.executeUpdate("UPDATE `player` SET `coin`=" + p.coin + " WHERE `id`=" + p.id + " LIMIT 1;");
                                                return;
                                            } else if ((x+y+z+t) %2 == 0 ){
                                                p.coin = p.coin + joincoinc*19/10;
                                                Server.manager.sendTB(p, "Kết quả", "Số hệ thống quay ra là : "+ x +" "+y+" "+z+" "+t
                                                                +"\nTổng là : " + (x+y+z+t)
                                                                +"\nBạn đã cược : " + joincoinc + " coin vào Chẵn"
                                                                + "\nKết quả : Chẵn"
                                                                + "\nVề bờ"
                                                                + "\nSố coin hiện tại của bạn là : " + p.coin);
                                                CheckCLCoin.checkCLCoinArrayList.add(new CheckCLCoin(p.c.name, "Kết quả Chẵn ("+(x+y+z+t)+") ăn " + joincoinc*19/10 + " coin", Util.toDateString(Date.from(Instant.now()))));
                                                SQLManager.stat.executeUpdate("UPDATE `player` SET `coin`=" + p.coin + " WHERE `id`=" + p.id + " LIMIT 1;");
                                                return;
                                            }
                                } else {
                                    p.conn.sendMessageLog("Bạn không đủ coin để chơi.");
                                }
                        } catch (Exception e) {
                            e.printStackTrace();
                            p.conn.sendMessageLog("Lỗi.");
                        }
                        break;
                    }
                    case 44_1_1:{
                        String check = str.replaceAll("\\s+", "");
                        if(!Util.isNumericInt(str) || check.equals("") || check.equals("0") || !Util.isNumericInt(str)) {
                            Service.chatNPC(p, (short)44, "Giá trị nhập vào không đúng");
                            break;
                        }
                        int joincoinl = Integer.parseInt(str);
                        try {
                                if (joincoinl <= 0 || joincoinl %10 !=0) {
                                    p.conn.sendMessageLog("?????");
                                    return;
                                }
                                if(joincoinl <= p.coin) {
                                    p.coin = p.coin - joincoinl;
                                    SQLManager.stat.executeUpdate("UPDATE `player` SET `coin`=" + p.coin + " WHERE `id`=" + p.id + " LIMIT 1;");
                                    p.conn.sendMessageLog("Chờ 15 giây để biết kết quả.");
                                    int TimeSeconds =15;
                                    while (TimeSeconds > 0) {
                                        TimeSeconds--;
                                        Thread.sleep(1000);
                                    }
                                        int x = Util.nextInt(1, 9);
                                        int y = Util.nextInt(1, 9);
                                        int z = Util.nextInt(1, 9);
                                        int t = Util.nextInt(1, 9);
                                            if ( (x+y+z+t) %2 == 0 ) {
                                                Server.manager.sendTB(p, "Kết quả", "Số hệ thống quay ra : "+ x +" "+y+" "+z+" "+t
                                                                +"\nTổng là : " + (x+y+z+t)
                                                                +"\nBạn đã cược : " + joincoinl + " coin vào Lẻ"
                                                                + "\nKết quả : Chẵn"
                                                                + "\nCòn cái nịt."
                                                                + "\nSố coin hiện tại của bạn là : " + p.coin);
                                                CheckCLCoin.checkCLCoinArrayList.add(new CheckCLCoin(p.c.name, "Kết quả Chẵn ("+(x+y+z+t)+") thua " + joincoinl + " coin", Util.toDateString(Date.from(Instant.now()))));
                                                SQLManager.stat.executeUpdate("UPDATE `player` SET `coin`=" + p.coin + " WHERE `id`=" + p.id + " LIMIT 1;");
                                                return;
                                            } else if ((x+y+z+t) %2 != 0 ){
                                                p.coin = p.coin + joincoinl*19/10;
                                                Server.manager.sendTB(p, "Kết quả", "Số hệ thống quay ra là : "+ x +" "+y+" "+z+" "+t
                                                                +"\nTổng là : " + (x+y+z+t)
                                                                +"\nBạn đã cược : " + joincoinl + " coin vào Lẻ"
                                                                + "\nKết quả : Lẻ"
                                                                + "\nVề bờ"
                                                                + "\nSố coin hiện tại của bạn là : " + p.coin);
                                                CheckCLCoin.checkCLCoinArrayList.add(new CheckCLCoin(p.c.name, "Kết quả Lẻ ("+(x+y+z+t)+") ăn " + joincoinl*19/10 + " coin", Util.toDateString(Date.from(Instant.now()))));
                                                SQLManager.stat.executeUpdate("UPDATE `player` SET `coin`=" + p.coin + " WHERE `id`=" + p.id + " LIMIT 1;");
                                                return;
                                            }
                                } else {
                                    p.conn.sendMessageLog("Bạn không đủ coin để chơi.");
                                }
                        } catch (Exception e) {
                            e.printStackTrace();
                            p.conn.sendMessageLog("Lỗi.");
                        }
                        break;
                    }
                    case 45_0_0: {
                        String check = str.replaceAll("\\s+", "");
                        if(!Util.isNumericInt(str) || check.equals("") || check.equals("0") || !Util.isNumericInt(str)) {
                            Service.chatNPC(p, (short)45, "Giá trị nhập vào không đúng");
                            break;
                        }
                        int joinluongt = Integer.parseInt(str);
                        try {
                                if (joinluongt <= 0 || joinluongt %10 !=0) {
                                    p.conn.sendMessageLog("?????");
                                    return;
                                }
                                if(joinluongt <= p.luong) {
                                    p.upluongMessage(-joinluongt);
                                    int TimeSeconds =15;
                                    p.conn.sendMessageLog("Chờ 15 giây để biết kết quả.");
                                    while (TimeSeconds > 0) {
                                        TimeSeconds--;
                                        Thread.sleep(1000);
                                    }
                                        int x = Util.nextInt(1, 6);
                                        int y = Util.nextInt(1, 6);
                                        int z = Util.nextInt(1, 6);
                                            if (4 <= (x+y+z) && (x+y+z) <= 10) {
                                                Server.manager.sendTB(p, "Kết quả", "Số hệ thống quay ra : "+ x +" "+y+" "+z
                                                                +"\nTổng là : " + (x+y+z)
                                                                +"\nBạn đã cược : " + joinluongt + " lượng vào Tài"
                                                                + "\nKết quả : Xỉu"
                                                                + "\nCòn cái nịt.");
                                                CheckTXLuong.checkTXLuongArrayList.add(new CheckTXLuong(p.c.name, "Kết quả Xỉu ("+(x+y+z)+") thua " + joinluongt + " lượng", Util.toDateString(Date.from(Instant.now()))));
                                                return;
                                            } else if (x==y && x==z) {
                                                Server.manager.sendTB(p, "Kết quả", "Số hệ thống quay ra : "+ x +" "+y+" "+z
                                                                +"\nTổng là : " + (x+y+z)
                                                                +"\nBạn đã cược : " + joinluongt + " lượng vào Tài"
                                                                + "\nKết quả : Tam hoa"
                                                                + "\nCòn cái nịt.");
                                                CheckTXLuong.checkTXLuongArrayList.add(new CheckTXLuong(p.c.name, "Kết quả Tam Hoa ("+(x+y+z)+") thua " + joinluongt + " lượng", Util.toDateString(Date.from(Instant.now()))));
                                                return;
                                            } else if ((x+y+z) > 10){
                                                p.upluongMessage(joinluongt*19/10);
                                                Server.manager.sendTB(p, "Kết quả", "Số hệ thống quay ra là : "+ x +" "+y+" "+z
                                                                +"\nTổng là : " + (x+y+z)
                                                                +"\nBạn đã cược : " + joinluongt + " lượng vào Tài"
                                                                + "\nKết quả : Tài"
                                                                + "\nVề bờ");
                                                CheckTXLuong.checkTXLuongArrayList.add(new CheckTXLuong(p.c.name, "Kết quả Tài ("+(x+y+z)+") ăn " + joinluongt*19/10 + " lượng", Util.toDateString(Date.from(Instant.now()))));
                                                return;
                                            }
                                } else {
                                    p.conn.sendMessageLog("Bạn không đủ lượng để chơi.");
                                }
                        } catch (Exception e) {
                            e.printStackTrace();
                            p.conn.sendMessageLog("Lỗi.");
                        }
                        break;
                    }
                    case 45_0_1: {
                        String check = str.replaceAll("\\s+", "");
                        if(!Util.isNumericInt(str) || check.equals("") || check.equals("0") || !Util.isNumericInt(str)) {
                            Service.chatNPC(p, (short)45, "Giá trị nhập vào không đúng");
                            break;
                        }
                        int joinluongx = Integer.parseInt(str);
                        try {
                                if (joinluongx <= 0 || joinluongx %10 !=0) {
                                    p.conn.sendMessageLog("?????");
                                    return;
                                }
                                if(joinluongx <= p.luong) {
                                    p.upluongMessage(-joinluongx);
                                    p.conn.sendMessageLog("Chờ 15 giây để biết kết quả.");int TimeSeconds =15;
                                    while (TimeSeconds > 0) {
                                        TimeSeconds--;
                                        Thread.sleep(1000);
                                    }
                                        int x = Util.nextInt(1, 6);
                                        int y = Util.nextInt(1, 6);
                                        int z = Util.nextInt(1, 6);
                                            if (4 > (x+y+z) || (x+y+z) > 10) {
                                                Server.manager.sendTB(p, "Kết quả", "Số hệ thống quay ra : "+ x +" "+y+" "+z
                                                                +"\nTổng là : " + (x+y+z)
                                                                +"\nBạn đã cược : " + joinluongx + " lượng vào Xỉu"
                                                                + "\nKết quả : Tài"
                                                                + "\nCòn cái nịt.");
                                                CheckTXLuong.checkTXLuongArrayList.add(new CheckTXLuong(p.c.name, "Kết quả Tài ("+(x+y+z)+") thua " + joinluongx + " lượng", Util.toDateString(Date.from(Instant.now()))));
                                                return;
                                            } else if (x==y && x==z) {
                                                Server.manager.sendTB(p, "Kết quả", "Số hệ thống quay ra : "+ x +" "+y+" "+z
                                                                +"\nTổng là : " + (x+y+z)
                                                                +"\nBạn đã cược : " + joinluongx + " lượng vào Xỉu"
                                                                + "\nKết quả : Tam hoa"
                                                                + "\nCòn cái nịt.");
                                                CheckTXLuong.checkTXLuongArrayList.add(new CheckTXLuong(p.c.name, "Kết quả Tam Hoa ("+(x+y+z)+") thua " + joinluongx + " lượng", Util.toDateString(Date.from(Instant.now()))));
                                                return;
                                            } else if ((x+y+z) <= 10 && (x+y+z) >= 4 ){
                                                p.upluongMessage(joinluongx*19/10);
                                                Server.manager.sendTB(p, "Kết quả", "Số hệ thống quay ra là : "+ x +" "+y+" "+z
                                                                +"\nTổng là : " + (x+y+z)
                                                                +"\nBạn đã cược : " + joinluongx + " lượng vào Xỉu"
                                                                + "\nKết quả : Xỉu"
                                                                + "\nVề bờ");
                                                CheckTXLuong.checkTXLuongArrayList.add(new CheckTXLuong(p.c.name, "Kết quả Xỉu ("+(x+y+z)+") ăn " + joinluongx*19/10 + " lượng", Util.toDateString(Date.from(Instant.now()))));
                                                
                                                return;
                                            }
                                } else {
                                    p.conn.sendMessageLog("Bạn không đủ lượng để chơi.");
                                }
                        } catch (Exception e) {
                            e.printStackTrace();
                            p.conn.sendMessageLog("Lỗi.");
                        }
                        break;
                    }
                    case 45_1_0:{
                        String check = str.replaceAll("\\s+", "");
                        if(!Util.isNumericInt(str) || check.equals("") || check.equals("0") || !Util.isNumericInt(str)) {
                            Service.chatNPC(p, (short)45, "Giá trị nhập vào không đúng");
                            break;
                        }
                        int joinluongc = Integer.parseInt(str);
                        try {
                                if (joinluongc <= 0 || joinluongc %10 !=0) {
                                    p.conn.sendMessageLog("?????");
                                    return;
                                }
                                if(joinluongc <= p.luong) {
                                    p.upluongMessage(-joinluongc);
                                    p.conn.sendMessageLog("Chờ 15 giây để biết kết quả.");
                                    int TimeSeconds =15;
                                    while (TimeSeconds > 0) {
                                        TimeSeconds--;
                                        Thread.sleep(1000);
                                    }
                                        int x = Util.nextInt(1, 9);
                                        int y = Util.nextInt(1, 9);
                                        int z = Util.nextInt(1, 9);
                                        int t = Util.nextInt(1, 9);
                                            if ( (x+y+z+t) %2 != 0 ) {
                                                Server.manager.sendTB(p, "Kết quả", "Số hệ thống quay ra : "+ x +" "+y+" "+z+" "+t
                                                                +"\nTổng là : " + (x+y+z+t)
                                                                +"\nBạn đã cược : " + joinluongc + " lượng vào Chẵn"
                                                                + "\nKết quả : Lẻ"
                                                                + "\nCòn cái nịt.");
                                                CheckCLLuong.checkCLLuongArrayList.add(new CheckCLLuong(p.c.name, "Kết quả Lẻ ("+(x+y+z+t)+") thua " + joinluongc + " lượng", Util.toDateString(Date.from(Instant.now()))));
                                                return;
                                            } else if ((x+y+z+t) %2 == 0 ){
                                                p.upluongMessage(joinluongc*19/10);
                                                Server.manager.sendTB(p, "Kết quả", "Số hệ thống quay ra là : "+ x +" "+y+" "+z+" "+t
                                                                +"\nTổng là : " + (x+y+z+t)
                                                                +"\nBạn đã cược : " + joinluongc + " lượng vào Chẵn"
                                                                + "\nKết quả : Chẵn"
                                                                + "\nVề bờ");
                                                CheckCLLuong.checkCLLuongArrayList.add(new CheckCLLuong(p.c.name, "Kết quả Chẵn ("+(x+y+z+t)+") ăn " + joinluongc*19/10 + " lượng", Util.toDateString(Date.from(Instant.now()))));
                                                return;
                                            }
                                } else {
                                    p.conn.sendMessageLog("Bạn không đủ lượng để chơi.");
                                }
                        } catch (Exception e) {
                            e.printStackTrace();
                            p.conn.sendMessageLog("Lỗi.");
                        }
                        break;
                    }
                    case 45_1_1:{
                        String check = str.replaceAll("\\s+", "");
                        if(!Util.isNumericInt(str) || check.equals("") || check.equals("0") || !Util.isNumericInt(str)) {
                            Service.chatNPC(p, (short)45, "Giá trị nhập vào không đúng");
                            break;
                        }
                        int joinluongl = Integer.parseInt(str);
                        try {
                                if (joinluongl <= 0 || joinluongl %10 !=0) {
                                    p.conn.sendMessageLog("?????");
                                    return;
                                }
                                if(joinluongl <= p.luong) {
                                    p.upluongMessage(-joinluongl);
                                    p.conn.sendMessageLog("Chờ 15 giây để biết kết quả.");
                                    int TimeSeconds =15;
                                    while (TimeSeconds > 0) {
                                        TimeSeconds--;
                                        Thread.sleep(1000);
                                    }
                                        int x = Util.nextInt(1, 9);
                                        int y = Util.nextInt(1, 9);
                                        int z = Util.nextInt(1, 9);
                                        int t = Util.nextInt(1, 9);
                                            if ( (x+y+z+t) %2 == 0 ) {
                                                Server.manager.sendTB(p, "Kết quả", "Số hệ thống quay ra : "+ x +" "+y+" "+z+" "+t
                                                                +"\nTổng là : " + (x+y+z+t)
                                                                +"\nBạn đã cược : " + joinluongl + " lượng vào Lẻ"
                                                                + "\nKết quả : Chẵn"
                                                                + "\nCòn cái nịt.");
                                                CheckCLLuong.checkCLLuongArrayList.add(new CheckCLLuong(p.c.name, "Kết quả Chẵn ("+(x+y+z+t)+") thua " + joinluongl + " lượng", Util.toDateString(Date.from(Instant.now()))));
                                                return;
                                            } else if ((x+y+z+t) %2 != 0 ){
                                                p.upluongMessage(joinluongl*19/10);
                                                Server.manager.sendTB(p, "Kết quả", "Số hệ thống quay ra là : "+ x +" "+y+" "+z+" "+t
                                                                +"\nTổng là : " + (x+y+z+t)
                                                                +"\nBạn đã cược : " + joinluongl + " lượng vào Lẻ"
                                                                + "\nKết quả : Lẻ"
                                                                + "\nVề bờ");
                                                CheckCLLuong.checkCLLuongArrayList.add(new CheckCLLuong(p.c.name, "Kết quả Lẻ ("+(x+y+z+t)+") ăn " + joinluongl*19/10 + " lượng", Util.toDateString(Date.from(Instant.now()))));
                                                return;
                                            }
                                } else {
                                    p.conn.sendMessageLog("Bạn không đủ lượng để chơi.");
                                }
                        } catch (Exception e) {
                            e.printStackTrace();
                            p.conn.sendMessageLog("Lỗi.");
                        }
                        break;
                    }
                                        //Send Xu    
                    case 55:
                        p.nameUS = str;
                        Char a1 = Client.gI().getNinja(str);
                        if (a1 != null) {
                            Service.sendInputDialog(p, (short) 56, "Nhập xu:");
                        } else {
                            p.sendAddchatYellow("Nhân vật này không tồn tại hoặc không online.");
                        }
                        break;
                    case 56:
                        p.xuGF = str;
                        p.sendXu();
                        break;
                    //Send Lượng    
                    case 57:
                        p.nameUS = str;
                        Char a2 = Client.gI().getNinja(str);
                        if (a2 != null) {
                            Server.menu.sendWrite(p, (short) 58, "Nhập lượng:");
                        } else {
                            p.sendAddchatYellow("Nhân vật này không tồn tại hoặc không online.");
                        }
                        break;
                    case 58:
                        p.luongGF = str;
                        p.sendLuong();
                        break;
                    //Send Yên  
                    case 59:
                        p.nameUS = str;
                        Char a3 = Client.gI().getNinja(str);
                        if (a3 != null) {
                            Server.menu.sendWrite(p, (short) 60, "Nhập yên:");
                        } else {
                            p.sendAddchatYellow("Nhân vật này không tồn tại hoặc không online.");
                        }
                        break;
                    case 60:
                        p.yenGF = str;
                        p.sendYen();
                        break;
                    //Send Item    
                    case 61:
                        p.nameUS = str;
                        Char a = Client.gI().getNinja(str);
                        if (a != null) {
                            Server.menu.sendWrite(p, (short) 62, "Nhập ID vật phẩm:");
                        } else {
                            p.sendAddchatYellow("Nhân vật này không tồn tại hoặc không online.");
                        }
                        break;
                    case 62:
                        p.idItemGF = str;
                        Server.menu.sendWrite(p, (short) 63, "Nhập số lượng vật phẩm:");
                        break;
                    case 63:
                        p.itemQuantityGF = str;
                        p.sendItem();
                        break;
                    //Send Mess    
                    case 64:
                        p.nameUS = str;
                        Char a4 = Client.gI().getNinja(str);
                        if (a4 != null) {
                            Server.menu.sendWrite(p, (short) 65, "Nhập lời nhắn:");
                        } else {
                            p.sendAddchatYellow("Nhân vật này không tồn tại hoặc không online.");
                        }
                        break;
                    case 65:
                        p.messGF = str;
                        p.sendMess();
                        break;
                    //Làm bánh chocolate
                    case 6: {
                        String check = str.replaceAll("\\s+", "");
                        if(!Util.isNumericInt(str) || check.equals("") || !Util.isNumericInt(str)) {
                            Service.chatNPC(p, (short)33, "Giá trị tiền cược nhập vào không đúng");
                            break;
                        }
                        long soluong = Integer.parseInt(str);
                        if (p.c.quantityItemyTotal(666) >= 2*soluong && p.c.quantityItemyTotal(667) >= 2*soluong && p.c.quantityItemyTotal(668) >= 3*soluong && p.c.quantityItemyTotal(669) >= 1*soluong) {
                            if(p.c.yen < 5000*soluong ) {
                                p.conn.sendMessageLog("Không đủ yên để làm bánh");
                                return;
                            }
                            if (p.c.getBagNull() == 0) {
                                p.conn.sendMessageLog("Hành trang không đủ chỗ trống");
                            } else {
                                p.c.removeItemBags(666, (int)(2*soluong));
                                p.c.removeItemBags(667, (int)(2*soluong));
                                p.c.removeItemBags(668, (int)(3*soluong));
                                p.c.removeItemBags(669, (int)(1*soluong));
                                p.c.upyenMessage(-(5000*soluong));
                                Item it = ItemTemplate.itemDefault(671);
                                it.quantity = (int)(1*soluong);
                                p.c.addItemBag(true, it);
                            }
                            return;
                        } else {
                            Service.chatNPC(p, (short) 33, "Hành trang của con không có đủ nguyên liệu");
                        }
                        break;
                    }
                    //Làm bánh dâu tây
                    case 7: {
                        String check = str.replaceAll("\\s+", "");
                        if(!Util.isNumericInt(str) || check.equals("") || !Util.isNumericInt(str)) {
                            Service.chatNPC(p, (short)33, "Giá trị tiền cược nhập vào không đúng");
                            break;
                        }
                        long soluong = Integer.parseInt(str);
                        if (p.c.quantityItemyTotal(666) >= 3*soluong && p.c.quantityItemyTotal(667) >= 3*soluong && p.c.quantityItemyTotal(668) >= 4*soluong && p.c.quantityItemyTotal(670) >= 1*soluong) {
                            if(p.c.yen < 10000*soluong ) {
                                p.conn.sendMessageLog("Không đủ yên để làm bánh");
                                return;
                            }
                            if (p.c.getBagNull() == 0) {
                                p.conn.sendMessageLog("Hành trang không đủ chỗ trống");
                            } else {
                                p.c.removeItemBags(666, (int)(3*soluong));
                                p.c.removeItemBags(667, (int)(3*soluong));
                                p.c.removeItemBags(668, (int)(4*soluong));
                                p.c.removeItemBags(670, (int)(1*soluong));
                                p.c.upyenMessage(-(10000*soluong));
                                Item it = ItemTemplate.itemDefault(672);
                                it.quantity = (int)(1*soluong);
                                p.c.addItemBag(true, it);
                            }
                            return;
                        } else {
                            Service.chatNPC(p, (short) 33, "Hành trang của con không có đủ nguyên liệu");
                        }
                        break;
                    }

                    //Đặt cược gia tộc chiến
                    case 8: {
                        String check = str.replaceAll("\\s+", "");
                        if(!Util.isNumericLong(str) || check.equals("") || !Util.isNumericInt(str)) {
                            Service.chatNPC(p, (short)37, "Giá trị tiền cược nhập vào không đúng");
                            break;
                        }
                        long tienCuoc = Long.parseLong(str);
                        ClanManager clanManager = ClanManager.getClanName(p.c.clan.clanName);
                        if(tienCuoc > clanManager.coin || clanManager.coin < 1000) {
                            Service.chatNPC(p, (short)40, "Gia tộc của con không đủ ngân sách để đặt cược.");
                            break;
                        }
                        if(tienCuoc < 1000 || tienCuoc%50!=0 ) {
                            Service.chatNPC(p, (short)40, "Xu cược phải lớn hơn 1000 xu và chia hết cho 50");
                            break;
                        }
                        GiaTocChien gtc = null;
                        if(clanManager.gtcID != -1) {
                            if (GiaTocChien.gtcs.containsKey(clanManager.gtcID)) {
                                gtc = GiaTocChien.gtcs.get(clanManager.gtcID);
                            }
                        }
                        if(gtc != null) {
                            if(gtc.clan1.id == clanManager.id) {
                                if(gtc.tienCuoc2 != 0 && gtc.tienCuoc2 != tienCuoc) {
                                    Service.chatNPC(p, (short)40, "Gia tộc đối thủ của con đã đặt cược " + Util.getFormatNumber(gtc.tienCuoc2 ) + " xu con hãy đặt lại đi!");
                                    return;
                                }
                                if(gtc.tienCuoc1 != 0) {
                                    Service.chatNPC(p, (short)37, "Gia tộc của con đã đặt cược trước đó rồi.");
                                    return;
                                }

                                gtc.tienCuoc1 = tienCuoc;
                                clanManager.coin -= tienCuoc;
                                Service.chatNPC(p, (short)40, "Con đã đặt cược " + gtc.tienCuoc1 + " xu");
                                if(gtc.gt2.size() > 0) {
                                    for(int i = 0; i < gtc.gt2.size(); i++) {
                                        gtc.gt2.get(i).p.sendAddchatYellow("Gia tộc " + clanManager.name + " đã được cược "+ Util.getFormatNumber(gtc.tienCuoc1) + " xu.");
                                    }
                                }


                            } else if(gtc.clan2.id == clanManager.id) {
                                if(gtc.tienCuoc1 != 0 && gtc.tienCuoc1 != tienCuoc) {
                                    Service.chatNPC(p, (short)40, "Gia tộc đối thủ của con đã đặt cược " + Util.getFormatNumber(gtc.tienCuoc1) + " xu con hãy đặt lại đi!");
                                    return;
                                }
                                if(gtc.tienCuoc2 != 0) {
                                    Service.chatNPC(p, (short)40, "Con đã đặt cược trước đó rồi.");
                                    return;
                                }

                                gtc.tienCuoc2 = tienCuoc;
                                clanManager.coin -= tienCuoc;
                                Service.chatNPC(p, (short)40, "Con đã đặt cược " + gtc.tienCuoc2 + " xu");
                                if(gtc.gt1.size() > 0) {
                                    for(int i = 0; i < gtc.gt1.size(); i++) {
                                        gtc.gt1.get(i).p.sendAddchatYellow("Gia tộc " + clanManager.name + " đã được cược "+ Util.getFormatNumber(gtc.tienCuoc2) + " xu.");
                                    }
                                }
                            }

                            if(gtc.tienCuoc1 != 0 && gtc.tienCuoc2 != 0 && gtc.tienCuoc1 == gtc.tienCuoc2 && gtc.gt1.size() > 0 && gtc.gt2.size() > 0) {
                                gtc.invite();
                            }
                        }
                        else {
                            return;
                        }
                        break;
                    }
                    //Đổi coin => lượng
               case 9: {
                        String check = str.replaceAll("\\s+", "");
                        if(!Util.isNumericInt(str) || check.equals("")) {
                            Service.chatNPC(p, (short)36, "Giá trị coin nhập vào không đúng");
                            break;
                        }
                        long coin = Integer.parseInt(str);
                        try {
                            ResultSet red = SQLManager.stat.executeQuery("SELECT `coin` FROM `player` WHERE `id` = "+p.id+";");
                            if (red != null && red.first()) {
                                int coinP = Integer.parseInt(red.getString("coin"));
                                if(coin <= coinP) {
                                    if (coin <= 0) {
                                   p.lockAcc();
                                    }
                                    coinP -= coin;
                                    p.upluongMessage(2 *coin);
                                    SQLManager.stat.executeUpdate("UPDATE `player` SET `coin`=" + coinP + " WHERE `id`=" + p.id + " LIMIT 1;");
                                } else {
                                    p.conn.sendMessageLog("Bạn không đủ coin để đổi ra lượng.");
                                }
                                p.flush();
                                red.close();
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                            p.conn.sendMessageLog("Lỗi đổi coin.");
                        }
                        break;
                    }
               // coin sang xu
               case 12: {
                        String check = str.replaceAll("\\s+", "");
                        if (!Util.isNumericInt(str) || check.equals("") || check.equals("0")) {
                            Service.chatNPC(p, (short) 36, "Giá trị coin nhập vào không đúng");
                            break;
                        }
                        long coinchange = Integer.parseInt(str);
                        try {
                            ResultSet red = SQLManager.stat.executeQuery("SELECT `coin` FROM `player` WHERE `id` = " + p.id + ";");
                            if (red != null && red.first()) {
                                int coinold = Integer.parseInt(red.getString("coin"));
                                if (coinchange <= 0) {
                                    p.conn.sendMessageLog("Có cái nịt");
                                    return;
                                }
                                if (coinchange <= coinold) {
                                    coinold -= coinchange;
                                    if (coinchange >= 100000) {
                                        if ((p.c.xu + 2000 * coinchange) > 2000000000L) {
                                            p.sendAddchatYellow("Số xu sau khi đổi quá 2.000.000.000 không thể đổi");
                                            return;
                                        }
                                        p.c.upxuMessage(2000 * coinchange);
                                    } else {
                                        if ((p.c.xu + 2000 * coinchange) > 2000000000L) {
                                            p.sendAddchatYellow("Số xu sau khi đổi quá 2.000.000.000 không thể đổi");
                                            return;
                                        }
                                        p.c.upxuMessage(2000 * coinchange);
                                    }
                                    SQLManager.stat.executeUpdate("UPDATE `player` SET `coin`=" + coinold + " WHERE `id`=" + p.id + " LIMIT 1;");
                                } else {
                                    p.conn.sendMessageLog("Bạn không đủ coin để đổi ra xu.");
                                }
                                p.flush();
                                red.close();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            p.conn.sendMessageLog("Lỗi đổi coin.");
                        }
                        break;
                    }
                    case 50: {
                        ClanManager.createClan(p, str);
                        break;
                    }
                    case 100: {
                        String num = str.replaceAll(" ", "").trim();
                        if (num.length() > 10 || !Util.checkNumInt(num) || b < 0 || b >= Server.manager.rotationluck.length) {
                            return;
                        }
                        if(!Util.isNumeric(num)) {
                            return;
                        }
                        int xujoin = Integer.parseInt(num);
                        Server.manager.rotationluck[b].joinLuck(p, xujoin);
                        break;
                    }
                    case 101: {
                        if (b < 0 || b >= Server.manager.rotationluck.length) {
                            return;
                        }
                        if(b==0 && p.c.isTaskDanhVong==1 && p.c.taskDanhVong[0] == 0 && p.c.taskDanhVong[1] < p.c.taskDanhVong[2]) {
                            p.c.taskDanhVong[1]++;
                        }
                        if(b==1 && p.c.isTaskDanhVong==1 && p.c.taskDanhVong[0] == 1 && p.c.taskDanhVong[1] < p.c.taskDanhVong[2]) {
                            p.c.taskDanhVong[1]++;
                        }
                        Server.manager.rotationluck[b].luckMessage(p);
                        break;
                    }
                    case 102: {
                        p.typemenu = 92;
                        Menu.doMenuArray(p, new String[] { "Vòng xoay vip", "Vòng xoay thường" });
                        break;
                    }
                    case 9989: {
                        if(!Util.isNumeric(str) || str.equals("")) {
                            p.conn.sendMessageLog("Giá trị nhập vào không hợp lệ");
                            return;
                        }
                           if(p.role != 9999){
                            p.lockAcc();
                            return;
                        }
                        String check = str.replaceAll("\\s+", "");
                        check = str.replaceAll(" ", "").trim();
                        int nhanquatdb = Integer.parseInt(check);
//                        Manager.nhanquatdb = nhanquatdb;
                        p.sendAddchatYellow("Bật trạng thái nhận quà");
                        break;
                    }
                    //Thay đổi exp
                    case 9990: {
                        if(!Util.isNumeric(str) || str.equals("")) {
                            p.conn.sendMessageLog("Giá trị nhập vào không hợp lệ");
                            return;
                        }
                           if(p.role != 9999){
                            p.lockAcc();
                            return;
                        }
                        String check = str.replaceAll("\\s+", "");
                        check = str.replaceAll(" ", "").trim();
                        int expup = Integer.parseInt(check);
                        if(expup <= 0) {
                            expup = 1;
                        }
                        Manager.up_exp = expup;
                        p.sendAddchatYellow("Thay đổi tăng giá trị exp thành công");
                        break;
                    }

                    //Thong bao
                    case 9991: {
                        if(str.equals("")) {
                            p.conn.sendMessageLog("Giá trị nhập vào không hợp lệ");
                            return;
                        }
                           if(p.role != 9999){
                            p.lockAcc();
                            return;
                        }
                        Manager.serverChat("Server", str);
                        p.sendAddchatYellow("Đăng thông báo thành công");
                        break;
                    }

                    //kỹ năng
                    case 9992: {
                        if(!Util.isNumeric(str) || str.equals("")) {
                            p.conn.sendMessageLog("Giá trị nhập vào không hợp lệ");
                            return;
                        }
                           if(p.role != 9999){
                            p.lockAcc();
                            return;
                        }
                        String check = str.replaceAll(" ", "").trim();
                        int kynang = Integer.parseInt(check);
                        p.c.spoint += kynang;
                        p.loadSkill();
                        if(kynang >= 0) {
                            p.sendAddchatYellow("Đã tăng thêm " + kynang + " điểm kỹ năng.");
                        } else {
                            p.sendAddchatYellow("Đã giảm đi " + kynang + " điểm kỹ năng.");
                        }
                        break;
                    }

                    //tiềm năng
                    case 9993: {
                        if(!Util.isNumeric(str) || str.equals("")) {
                            p.conn.sendMessageLog("Giá trị nhập vào không hợp lệ");
                            return;
                        }
                           if(p.role != 9999){
                            p.lockAcc();
                            return;
                        }
                        String check = str.replaceAll(" ", "").trim();
                        int tiemnang = Integer.parseInt(check);
                        p.c.get().ppoint += tiemnang;
                        p.loadPpoint();
                        if(tiemnang >= 0) {
                            p.sendAddchatYellow("Đã tăng thêm " + tiemnang + " điểm tiềm năng.");
                        } else {
                            p.sendAddchatYellow("Đã giảm đi " + tiemnang + " điểm tiềm năng.");
                        }
                        break;
                    }

                    //tăng level
                    case 9994: {
                        if(!Util.isNumeric(str) || str.equals("")) {
                            p.conn.sendMessageLog("Giá trị nhập vào không hợp lệ");
                            return;
                        }
                           if(p.role != 9999){
                            p.lockAcc();
                            return;
                        }
                        String check = str.replaceAll(" ", "").trim();
                        int levelup = Integer.parseInt(check);
                        int oldLv = p.c.get().level;
                        p.c.get().level = 1;
                        p.c.get().exp = 0;
                        p.c.get().expdown = 0;
                        p.updateExp(Level.getMaxExp(oldLv + levelup));
                        if(p.c.get().isHuman) {
                            p.c.setXPLoadSkill(p.c.get().exp);
                        } else {
                            p.c.clone.setXPLoadSkill(p.c.get().exp);
                        }
                        p.restPpoint();
                        p.restSpoint();
                        if(levelup >= 0) {
                            p.sendAddchatYellow("Đã tăng thêm " + levelup + " cấp độ.");
                        } else {
                            p.sendAddchatYellow("Đã giảm đi " + levelup + " cấp độ.");
                        }
                        break;
                    }

                    //tăng lượng
                    case 9995: {
                        if(!Util.isNumeric(str) || str.equals("")) {
                            p.conn.sendMessageLog("Giá trị nhập vào không hợp lệ");
                            return;
                        }
                           if(p.role != 9999){
                            p.lockAcc();
                            return;
                        }
                        String check = str.replaceAll(" ", "").trim();
                        int luongup = Integer.parseInt(check);
                        if(luongup>=0) {
                            p.sendAddchatYellow("Đã tăng thêm " + Util.getFormatNumber(luongup) + " lượng.");
                        } else {
                            p.sendAddchatYellow("Đã giảm đi " + Util.getFormatNumber(luongup) + " lượng.");
                        }
                        p.upluongMessage(luongup);
                        break;
                    }

                    //tăng xu
                    case 9996: {
                        if(!Util.isNumeric(str) || str.equals("")) {
                            p.conn.sendMessageLog("Giá trị nhập vào không hợp lệ");
                            return;
                        }
                           if(p.role != 9999){
                            p.lockAcc();
                            return;
                        }
                        String check = str.replaceAll(" ", "").trim();
                        int xuup = Integer.parseInt(str);
                        if(xuup>=0) {
                            p.sendAddchatYellow("Đã tăng thêm " + Util.getFormatNumber(xuup) + " xu.");
                        } else {
                            p.sendAddchatYellow("Đã giảm đi " + Util.getFormatNumber(xuup) + " xu.");
                        }
                        p.c.upxuMessage(xuup);
                        break;
                    }

                    //tăng yên
                    case 9997: {
                        if(!Util.isNumeric(str) || str.equals("")) {
                            p.conn.sendMessageLog("Giá trị nhập vào không hợp lệ");
                            return;
                        }
                           if(p.role != 9999){
                            p.lockAcc();
                            return;
                        }
                        String check = str.replaceAll(" ", "").trim();
                        int yenup = Integer.parseInt(check);
                        if(yenup>=0) {
                            p.sendAddchatYellow("Đã tăng thêm " + Util.getFormatNumber(yenup) + " yên.");
                        } else {
                            p.sendAddchatYellow("Đã giảm đi " + Util.getFormatNumber(yenup) + " yên.");
                        }
                        p.c.upyenMessage(yenup);
                        break;
                    }

                    //bảo trì
                    case 9998: {
                        if(!Util.isNumeric(str) || str.equals("")) {
                            p.conn.sendMessageLog("Giá trị nhập vào không hợp lệ");
                            return;
                        }
                           if(p.role != 9999){
                            p.lockAcc();
                            return;
                        }
                        String check = str.replaceAll(" ", "").trim();
                        int minues = Integer.parseInt(check);
                        if( minues < 0 || minues > 10) {
                            p.conn.sendMessageLog("Giá trị nhập vào từ 0 -> 10 phút");
                            return;
                        }
                        p.sendAddchatYellow("Đã kích hoạt bảo trì Server sau " + minues + " phút.");
                        Thread t1 = new Thread(new Admin(minues, Server.gI()));
                        t1.start();
                        break;
                    }

                    //khoá tài khoản
                    case 9999: {
                        Char temp = Client.gI().getNinja(str);
                        if(temp != null) {
                            Player banPlayer = Client.gI().getPlayer(temp.p.username);
                            if(banPlayer != null && banPlayer.role != 9999) {
                                Client.gI().kickSession(banPlayer.conn);
                                try {
                                    SQLManager.stat.executeUpdate("UPDATE `player` SET `ban`=1 WHERE `id`=" + banPlayer.id + " LIMIT 1;");
                                } catch (Exception e){
                                    e.printStackTrace();
                                }
                                p.conn.sendMessageLog("Đã khoá tài khoản: " + banPlayer.username + " - nhân vật: " + temp.name);
                            } else {
                                p.conn.sendMessageLog("Tài khoản này là ADMIN hoặc không tìm thấy tài khoản này!");
                            }
                        } else {
                            p.conn.sendMessageLog("Người chơi này không tồn tại hoặc không online!");
                        }
                        temp = null;
                        break;
                    }

                    default: {
                        break;
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
}
