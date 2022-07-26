package huydat.server;

import huydat.io.Session;
import huydat.io.Message;
import huydat.io.SQLManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class GameCanvas {

    public static void addInfoDlg(Session session, String s) {
        Message msg = null;
        try {
            msg = Service.messageNotMap((byte) (-86));
            msg.writer().writeUTF(s);
            msg.writer().flush();
            session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public static void getDataEffect(Session session, short id) {
        Message msg = null;
        try {
            if (session != null) {
                //System.out.println("Lấy data " + id);
                //byte[] ab = GameSrc.loadFile("res/Effect/x" + session.zoomLevel + "/DataEffect/" + id).toByteArray();
                ResultSet rs = SQLManager.stat.executeQuery("SELECT * FROM `nsohuyda_data`.`Effdata` WHERE `id` = " + id + ";");
                byte[] ab;
                rs.last();
                if (rs.getRow() == 0) {
                    ab = GameSrc.loadFile("res/Effect/x" + session.zoomLevel + "/DataEffect/" + id).toByteArray();
                } else {
                    rs.beforeFirst();
                    ab = Getdata(rs, id);
                }
                rs.close();
                if (ab != null) {
                    if (id == 21) {
                        ab[6] = 127;
                        ab[8] = 127;
                        ab[9] = 127;
                        ab[11] = 127;
                        ab[12] = 127;
                        ab[13] = 127;
                        ab[14] = 127;
                        ab[18] = 127;
                        ab[19] = 127;
                        ab[22] = 127;
                        ab[23] = 127;
                        ab[24] = 127;
                        ab[29] = -60;
                        ab[31] = 70;
                        ab[37] = -60;
                        ab[39] = 70;
                        ab[45] = -60;
                        ab[47] = 70;
                        ab[53] = -60;
                        ab[55] = 70;
                        ab[59] = 127;

                        msg = new Message(125);
                        msg.writer().write(ab);
                        msg.writer().flush();
                        session.sendMessage(msg);
                    } else {
                        ab[1] = (byte) id; //du co cai nay luon :v
                        msg = new Message(125);
                        msg.writer().write(ab);
                        msg.writer().flush();
                        session.sendMessage(msg);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public static void startOKDlg(Session session, String info) {
        Message msg = null;
        try {
            msg = new Message(-26);
            msg.writer().writeUTF(info);
            msg.writer().flush();
            session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public static void addEffect(Session session, byte b, int vId, short id, int timelive, int miliSecondWait, boolean isHead) {
        Message msg = null;
        try {
            if (session != null) {
                msg = new Message(125);
                msg.writer().writeByte(0);
                msg.writer().writeByte(b);
                if (b == 1) {
                    msg.writer().writeByte(vId);
                } else {
                    msg.writer().writeInt(vId);
                }
                msg.writer().writeShort(id);
                msg.writer().writeInt(timelive);
                msg.writer().writeByte(miliSecondWait);
                msg.writer().writeByte(isHead ? 1 : 0);
                msg.writer().flush();
                session.sendMessage(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public static void getImgEffect(Session session, short id) {
        Message msg = null;
        try {
            if (session != null) {
                //System.out.println("Lấy ảnh " + id);
                byte[] ab = GameSrc.loadFile("res/Effect/x" + session.zoomLevel + "/ImgEffect/ImgEffect " + id + ".png").toByteArray();
                if (ab != null) {
                    msg = new Message(125);
                    msg.writer().writeByte(1);
                    msg.writer().writeByte(id);
                    msg.writer().writeInt(ab.length);
                    msg.writer().write(ab);
                    msg.writer().flush();
                    session.sendMessage(msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public static byte[] Getdata(ResultSet rs, short id) {
        byte[] ab2 = new byte[217];
        try {
            rs = SQLManager.stat.executeQuery("SELECT * FROM `nsohuyda_data`.`Effdata` WHERE `id` = " + id + ";");
            while (rs.next()) {

                JSONObject jsob = (JSONObject) JSONValue.parse(rs.getString("frames"));
                jsob = (JSONObject) jsob.get("frames");
                JSONObject jsob2 = jsob;
                String name = Getname(jsob);
                if (Integer.parseInt(rs.getString("type")) != jsob2.size()) {
                    continue;
                }
                for (int i = 0; i < jsob2.size(); i++) {
                    if (i > 9) {
                        continue;
                    }
                    jsob = (JSONObject) jsob2.get((name + i + ".png"));
                    jsob = (JSONObject) jsob.get("frame");
                    ab2[5 + i * 5] = (byte) i;
                    ab2[6 + i * 5] = (byte) Integer.parseInt(jsob.get("x").toString());
                    ab2[7 + i * 5] = (byte) Integer.parseInt(jsob.get("y").toString());
                    ab2[8 + i * 5] = (byte) Integer.parseInt(jsob.get("w").toString());
                    ab2[9 + i * 5] = (byte) Integer.parseInt(jsob.get("h").toString());
                }

                byte dx = (byte) Integer.parseInt(rs.getString("dx"));
                byte dy = (byte) Integer.parseInt(rs.getString("dy"));
                byte fr = (byte) Integer.parseInt(rs.getString("front"));
                
                ab2[0] = 2;
                ab2[1] = (byte) id;
                ab2[2] = 0;
                ab2[3] = -43;
                ab2[4] = 8;

                ab2[45] = 0;
                ab2[46] = 9;
                ab2[47] = 1;

                ab2[48] = -1;
                ab2[49] = dx;
                ab2[50] = -1;
                ab2[51] = dy;

                ab2[52] = 0;
                ab2[53] = 0;
                ab2[54] = fr;
                ab2[55] = 1;

                ab2[56] = -1;
                ab2[57] = dx;
                ab2[58] = -1;
                ab2[59] = dy;

                ab2[60] = 1;
                ab2[61] = 0;
                ab2[62] = fr;
                ab2[63] = 1;

                ab2[64] = -1;
                ab2[65] = dx;
                ab2[66] = -1;
                ab2[67] = dy;

                ab2[68] = 2;
                ab2[69] = 0;
                ab2[70] = fr;
                ab2[71] = 1;

                ab2[72] = -1;
                ab2[73] = dx;
                ab2[74] = -1;
                ab2[75] = dy;

                ab2[76] = 3;
                ab2[77] = 0;
                ab2[78] = fr;
                ab2[79] = 1;

                ab2[80] = -1;
                ab2[81] = dx;
                ab2[82] = -1;
                ab2[83] = dy;

                ab2[84] = 4;
                ab2[85] = 0;
                ab2[86] = fr;
                ab2[87] = 1;

                ab2[88] = -1;
                ab2[89] = dx;
                ab2[90] = -1;
                ab2[91] = dy;

                ab2[92] = 5;
                ab2[93] = 0;
                ab2[94] = fr;
                ab2[95] = 1;

                ab2[96] = -1;
                ab2[97] = dx;
                ab2[98] = -1;
                ab2[99] = dy;

                ab2[100] = 6;
                ab2[101] = 0;
                ab2[102] = fr;
                ab2[103] = 1;

                ab2[104] = -1;
                ab2[105] = dx;
                ab2[106] = -1;
                ab2[107] = dy;

                ab2[108] = 7;
                ab2[109] = 0;
                ab2[110] = fr;
                ab2[111] = 0;
                ab2[112] = 15;
                ab2[113] = 0;
                ab2[114] = 0;
                ab2[115] = 0;
                ab2[116] = 0;
                ab2[117] = 0;
                ab2[118] = 1;
                ab2[119] = 0;
                ab2[120] = 1;
                ab2[121] = 0;
                ab2[122] = 2;
                ab2[123] = 0;
                ab2[124] = 2;
                ab2[125] = 0;
                ab2[126] = 3;
                ab2[127] = 0;
                ab2[128] = 3;
                ab2[129] = 0;
                ab2[130] = 4;
                ab2[131] = 0;
                ab2[132] = 4;
                ab2[133] = 0;
                ab2[134] = 5;
                ab2[135] = 0;
                ab2[136] = 5;
                ab2[137] = 0;
                ab2[138] = 6;
                ab2[139] = 0;
                ab2[140] = 6;
                ab2[141] = 0;
                ab2[142] = 7;
                ab2[143] = 0;
                ab2[144] = 7;
                ab2[145] = 0;
                ab2[146] = 0;
                ab2[147] = 0;
                ab2[148] = 0;
                ab2[149] = 0;
                ab2[150] = 1;
                ab2[151] = 0;
                ab2[152] = 1;
                ab2[153] = 0;
                ab2[154] = 2;
                ab2[155] = 0;
                ab2[156] = 2;
                ab2[157] = 0;
                ab2[158] = 3;
                ab2[159] = 0;
                ab2[160] = 3;
                ab2[161] = 0;
                ab2[162] = 4;
                ab2[163] = 0;
                ab2[164] = 4;
                ab2[165] = 0;
                ab2[166] = 5;
                ab2[167] = 0;
                ab2[168] = 5;
                ab2[169] = 0;
                ab2[170] = 6;
                ab2[171] = 0;
                ab2[172] = 6;
                ab2[173] = 0;
                ab2[174] = 7;
                ab2[175] = 0;
                ab2[176] = 7;
                ab2[177] = 0;
                ab2[178] = 0;
                ab2[179] = 0;
                ab2[180] = 0;
                ab2[181] = 0;
                ab2[182] = 1;
                ab2[183] = 0;
                ab2[184] = 1;
                ab2[185] = 0;
                ab2[186] = 2;
                ab2[187] = 0;
                ab2[188] = 2;
                ab2[189] = 0;
                ab2[190] = 3;
                ab2[191] = 0;
                ab2[192] = 3;
                ab2[193] = 0;
                ab2[194] = 4;
                ab2[195] = 0;
                ab2[196] = 4;
                ab2[197] = 0;
                ab2[198] = 5;
                ab2[199] = 0;
                ab2[200] = 5;
                ab2[201] = 0;
                ab2[202] = 6;
                ab2[203] = 0;
                ab2[204] = 6;
                ab2[205] = 0;
                ab2[206] = 7;
                ab2[207] = 0;
                ab2[208] = 7;
                ab2[209] = 0;
                ab2[210] = 8;
                ab2[211] = 0;
                ab2[212] = 8;
                ab2[213] = 0;
                ab2[214] = 0;
                ab2[215] = 0;
                ab2[216] = 0;
            }
            System.out.println("load datasql id - " + id);
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ab2;
    }

    public static String Getname(JSONObject s22) {
        String s = s22.toString();
        String s2 = "";
        int dem = 2;
        while (!s2.contains(".png")) {
            s2 += s.charAt(dem++);
        }
        System.out.println(s2.substring(0, s2.length() - 5));
        return (s2.substring(0, s2.length() - 5));
    }

}
