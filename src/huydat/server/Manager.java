package huydat.server;

import huydat.template.EffectTemplate;
import huydat.template.NpcTemplate;
import huydat.template.ItemOptionTemplate;
import huydat.template.MapTemplate;
import huydat.template.SkillOptionTemplate;
import huydat.template.ItemTemplate;
import huydat.template.SkillTemplate;
import huydat.template.ShinwaTemplate;
import huydat.template.MobTemplate;
import huydat.real.Player;
import huydat.real.ClanMember;
import huydat.real.Waypoint;
import huydat.real.Npc;
import huydat.real.Option;
import huydat.real.ClanManager;
import huydat.real.Level;
import huydat.real.ItemSell;
import huydat.real.Item;
import huydat.real.Alert;
import huydat.real.Map;
import huydat.cache.Part;
import huydat.cache.PartImage;
import huydat.cache.SkillOptionTemplates;
import huydat.io.Message;
import huydat.io.SQLManager;
import huydat.io.Util;
import huydat.stream.Client;
import huydat.thiendiabang.ThienDiaData;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.time.Instant;
import java.util.*;

public class Manager {
    public static int baseWhiteListId = 0;
    public static HashMap<Integer, String> whiteList;
    
    public int acclimitIP = 1; /// so acc /ip
    public final String NoteacclimitIP = "chơi làm cặc gì 10 nick đủ rồi off bớt nick đi rồi vào tiếp";   // thong bao khi qua ip
    public static boolean backup;
    public int os;
    public int post;
    public String host;
    public static String mysql_part;
    public static String backup_part;
    public static String mysql_host;
    public static int mysql_port;
    public static String mysql_database_data;
    public static String mysql_database_ninja;
    public static String mysql_user;
    public static String mysql_pass;
    public static int max_level_up;
    public static int up_exp;
    public static int hoursUpdate = 1;
    public static boolean isClearSession = false;
    public static boolean isClearCloneLogin = false;
    public static boolean isSaveData = false;
    public static Alert alert = new Alert();
    private byte vsData;
    private byte vsMap;
    private byte vsSkill;
    private byte vsItem;
    private byte[][] tasks;
    private byte[][] maptasks;
    Lucky[] rotationluck;
    public byte event;
    public String[] NinjaS;
    public static ArrayList<NpcTemplate> npcs;
    public static ArrayList<Part> parts;
     public TaiXiu[] taixiu;
    public int tx;
    public ChanLEonl[] chanle;
    public int cl;
    //skill
    public static SkillOptionTemplates[] sOptionTemplates;
    public static int[] idMapLoad = new int[]{4, 5, 7, 8, 9, 11, 12, 13, 14, 15, 16, 18, 19, 24, 28, 29, 30, 31, 33, 34, 35, 36, 37, 39, 40, 41, 42, 46, 47, 48, 49, 50, 51, 52, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68};

    public Manager() {
             loadConfigFile();
             
        npcs = new ArrayList();
        parts = new ArrayList();
        this.rotationluck = new Lucky[2];
        this.event = 6;
        this.NinjaS = new String[]{"Chưa vào lớp", "Ninja Kiếm", "Ninja Phi Tiêu", "Ninja Kunai", "Ninja Cung", "Ninja Đao", "Ninja Quạt"};
        taixiu = new TaiXiu[1];this.taixiu[0] = new TaiXiu(); 
        chanle = new ChanLEonl[1];this.chanle[0] = new ChanLEonl(); 
        this.loadConfigFile();
        this.rotationluck[0] = new Lucky("Vòng xoay vip", (byte)0, (short)120, 1000000, 50000000, 1000000000);
        this.rotationluck[1] = new Lucky("Vòng xoay lượng", (byte)1, (short)120, 10000, 100000, 500000000);
        this.rotationluck[0].start();
        this.rotationluck[1].start();
        this.loadDataBase();
        this.loadVersion();
        
    }
      public void updatecl() {
        try {
            ResultSet red = SQLManager.stat.executeQuery("SELECT `server` FROM `server` WHERE `id` = 1"+";");
            if (red != null && red.first()) {
                int cl = Integer.parseInt(red.getString("server"));
                this.cl = cl;
                red.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
      
    public void updatetx() {
        try {
            ResultSet red = SQLManager.stat.executeQuery("SELECT `server` FROM `server` WHERE `id` = 1"+";");
            if (red != null && red.first()) {
                int tx = Integer.parseInt(red.getString("server"));
                this.tx = tx;
                red.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static Map getMapid(int id) {
        if(Server.maps != null) {
            synchronized (Server.maps) {
                Map map;
                short i;
                for (i = 0; i < Server.maps.length; ++i) {
                    map = Server.maps[i];
                    if (map != null && map.id == id) {
                        return map;
                    }
                }
                return null;
            }
        }
        return null;
    }

    private void loadConfigFile() {
        byte[] ab = GameSrc.loadFile("ninja.conf").toByteArray();
        if (ab == null) {
            System.out.println("Config file not found!");
            System.exit(0);
        }

        String data = new String(ab);
        HashMap<String, String> configMap = new HashMap();
        StringBuilder sbd = new StringBuilder();
        boolean bo = false;

        for(int i = 0; i <= data.length(); ++i) {
            char es;
            if (i != data.length() && (es = data.charAt(i)) != '\n') {
                if (es == '#') {
                    bo = true;
                }

                if (!bo) {
                    sbd.append(es);
                }
            } else {
                bo = false;
                String sbf = sbd.toString().trim();
                if (sbf != null && !sbf.equals("") && sbf.charAt(0) != '#') {
                    int j = sbf.indexOf(58);
                    if (j > 0) {
                        String key = sbf.substring(0, j).trim();
                        String value = sbf.substring(j + 1).trim();
                        configMap.put(key, value);
                        System.out.println("config: " + key + "-" + value);
                    }
                }
                sbd.setLength(0);
            }
        }

        if (configMap.containsKey("debug")) {
            Util.setDebug(Boolean.parseBoolean((String)configMap.get("debug")));
        } else {
            Util.setDebug(false);
        }

        if (configMap.containsKey("os")) {
            this.os = Integer.parseInt(configMap.get("os"));
        } else {
            this.os = 1;
        }

        if (configMap.containsKey("host")) {
            this.host = (String)configMap.get("host");
        } else {
            this.host = "localhost";
        }

        if (configMap.containsKey("post")) {
            this.post = Integer.parseInt((String)configMap.get("post"));
        } else {
            this.post = 14444;
        }

        if (configMap.containsKey("mysql-part")) {
            this.mysql_part = (String)configMap.get("mysql-part");
        } else {
            this.mysql_part = "C:\\";
        }

        if (configMap.containsKey("backup-part")) {
            this.backup_part = (String)configMap.get("backup-part");
        } else {
            this.backup_part = "C:\\";
        }

        if (configMap.containsKey("mysql-host")) {
            this.mysql_host = (String)configMap.get("mysql-host");
        } else {
            this.mysql_host = "localhost";
        }

        if (configMap.containsKey("mysql-port")) {
            this.mysql_port = Integer.parseInt((String)configMap.get("mysql-port"));
        } else {
            this.mysql_port = 3306;
        }

        if (configMap.containsKey("mysql-user")) {
            this.mysql_user = (String)configMap.get("mysql-user");
        } else {
            this.mysql_user = "root";
        }

        if (configMap.containsKey("mysql-password")) {
            this.mysql_pass = (String)configMap.get("mysql-password");
        } else {
            this.mysql_pass = "rewind";
        }

        if (configMap.containsKey("mysql-database_data")) {
            this.mysql_database_data = (String)configMap.get("mysql-database_data");
        } else {
            this.mysql_database_data = "data";
        }

        if (configMap.containsKey("mysql-database_ninja")) {
            this.mysql_database_ninja = (String)configMap.get("mysql-database_ninja");
        } else {
            this.mysql_database_ninja = "ninja";
        }

        if (configMap.containsKey("version-Data")) {
            this.vsData = Byte.parseByte((String)configMap.get("version-Data"));
        } else {
            this.vsData = 54;
        }

        if (configMap.containsKey("version-Map")) {
            this.vsMap = Byte.parseByte((String)configMap.get("version-Map"));
        } else {
            this.vsMap = 86;
        }

        if (configMap.containsKey("version-Skill")) {
            this.vsSkill = Byte.parseByte((String)configMap.get("version-Skill"));
        } else {
            this.vsSkill = 10;
        }

        if (configMap.containsKey("version-Item")) {
            this.vsItem = Byte.parseByte((String)configMap.get("version-Item"));
        } else {
            this.vsItem = 70;
        }

        if (configMap.containsKey("version-Event")) {
            this.event = Byte.parseByte((String)configMap.get("version-Event"));
        } else {
            this.event = 0;
        }

        if (configMap.containsKey("up-exp")) {
            up_exp = Integer.parseInt((String)configMap.get("up-exp"));
        } else {
            up_exp = 1;
        }

        if (configMap.containsKey("max-level-up")) {
            max_level_up = Integer.parseInt((String)configMap.get("max-level-up"));
        } else {
            max_level_up = 130;
        }
        if (configMap.containsKey("max-level-up")) {
            this.acclimitIP = Integer.parseInt((String)configMap.get("acclimitIP"));
        } else {
            this.acclimitIP = 10;
        }
        
    }

    private void loadDataBase() {
        SQLManager.create(this.mysql_host, this.mysql_port, this.mysql_database_data, this.mysql_user, this.mysql_pass);
        int i = 0;

        try {
            ResultSet res = SQLManager.stat.executeQuery("SELECT * FROM `tasks`;");
            if (res.last()) {
                this.tasks = new byte[res.getRow()][];
                this.maptasks = new byte[this.tasks.length][];
                res.beforeFirst();
            }

            JSONArray Option;
            byte j;
            while(res.next()) {
                JSONArray jarr = (JSONArray) JSONValue.parse(res.getString("tasks"));
                Option = (JSONArray)JSONValue.parse(res.getString("maptasks"));
                this.tasks[i] = new byte[jarr.size()];
                this.maptasks[i] = new byte[this.tasks.length];

                for(j = 0; j < jarr.size(); ++j) {
                    this.tasks[i][j] = Byte.parseByte(jarr.get(j).toString());
                    this.maptasks[i][j] = Byte.parseByte(Option.get(j).toString());
                }

                ++i;
            }

            res.close();
            i = 0;

            for(res = SQLManager.stat.executeQuery("SELECT * FROM `level`;"); res.next(); ++i) {
                Level level = new Level();
                level.level = Integer.parseInt(res.getString("level"));
                level.exps = Long.parseLong(res.getString("exps"));
                level.ppoint = Short.parseShort(res.getString("ppoint"));
                level.spoint = Short.parseShort(res.getString("spoint"));
                Level.entrys.add(level);
            }

            res.close();
            i = 0;

            for(res = SQLManager.stat.executeQuery("SELECT * FROM `effect`;"); res.next(); ++i) {
                EffectTemplate eff = new EffectTemplate();
                eff.id = Byte.parseByte(res.getString("id"));
                eff.type = Byte.parseByte(res.getString("type"));
                eff.name = res.getString("name");
                eff.iconId = Short.parseShort(res.getString("iconId"));
                EffectTemplate.entrys.add(eff);
            }
            res.close();

            i = 0;
            int k;
            for(res = SQLManager.stat.executeQuery("SELECT * FROM `mob`;"); res.next(); ++i) {
                MobTemplate md = new MobTemplate();
                md.id = Integer.parseInt(res.getString("id"));
                md.type = Byte.parseByte(res.getString("type"));
                md.name = res.getString("name");
                md.hp = Integer.parseInt(res.getString("hp"));
                md.rangeMove = Byte.parseByte(res.getString("rangeMove"));
                md.speed = Byte.parseByte(res.getString("speed"));
                Option = (JSONArray)JSONValue.parse(res.getString("item"));
                md.arrIdItem = new short[Option.size()];

                for(k = 0; k < Option.size(); ++k) {
                    md.arrIdItem[k] = Short.parseShort(Option.get(k).toString());
                }

                MobTemplate.entrys.add(md);
            }
            res.close();

            res = SQLManager.stat.executeQuery("SELECT * FROM `npc`;");
            NpcTemplate npc2;
            JSONArray jArr2;
            int size2;
            while(res.next()) {
                npc2 = new NpcTemplate();
                npc2.npcTemplateId = res.getInt("id");
                npc2.name = res.getString("name");
                npc2.headId = res.getShort("headId");
                npc2.bodyId = res.getShort("bodyId");
                npc2.legId = res.getShort("legId");
                Option = (JSONArray)JSONValue.parse(res.getString("menu"));
                k = Option.size();
                npc2.menu = new String[k][];
                int ii;
                for(ii = 0; ii < k; ++ii) {
                    jArr2 = (JSONArray)JSONValue.parse(Option.get(ii).toString());
                    size2 = jArr2.size();
                    npc2.menu[ii] = new String[size2];
                    int a;
                    for(a = 0; a < size2; ++a) {
                        npc2.menu[ii][a] = jArr2.get(a).toString();
                    }
                }
                npcs.add(npc2);
            }
            res.close();

            i = 0;
            res = SQLManager.stat.executeQuery("SELECT * FROM `map`;");
            if (res.last()) {
                MapTemplate.arrTemplate = new MapTemplate[res.getRow()];
                res.beforeFirst();
            }

            while(res.next()) {
                MapTemplate temp = new MapTemplate();
                temp.id = res.getInt("id");
                temp.tileID = res.getByte("tile");
                temp.bgID = res.getByte("backid");
                temp.name = res.getString("name");
                temp.typeMap = res.getByte("type");
                temp.maxplayers = res.getByte("maxplayer");
                temp.numarea = res.getByte("numzone");
                temp.x0 = res.getShort("x0");
                temp.y0 = res.getShort("y0");
                Option = (JSONArray)JSONValue.parse(res.getString("Vgo"));
                temp.vgo = new Waypoint[Option.size()];

                JSONArray jar2;
                Waypoint vg;
                for(j = 0; j < Option.size(); ++j) {
                    temp.vgo[j] = new Waypoint();
                    jar2 = (JSONArray)JSONValue.parse(Option.get(j).toString());
                    vg = temp.vgo[j];
                    vg.minX = Short.parseShort(jar2.get(0).toString());
                    vg.minY = Short.parseShort(jar2.get(1).toString());
                    vg.maxX = Short.parseShort(jar2.get(2).toString());
                    vg.maxY = Short.parseShort(jar2.get(3).toString());
                    vg.mapid = Short.parseShort(jar2.get(4).toString());
                    vg.goX = Short.parseShort(jar2.get(5).toString());
                    vg.goY = Short.parseShort(jar2.get(6).toString());
                }

                Option = (JSONArray)JSONValue.parse(res.getString("Mob"));
                temp.arMobid = new short[Option.size()];
                temp.arrMobx = new short[Option.size()];
                temp.arrMoby = new short[Option.size()];
                temp.arrMobstatus = new byte[Option.size()];
                temp.arrMoblevel = new int[Option.size()];
                temp.arrLevelboss = new byte[Option.size()];
                temp.arrisboss = new boolean[Option.size()];

                short l;
                for(l = 0; l < Option.size(); ++l) {
                    jar2 = (JSONArray)Option.get(l);
                    temp.arMobid[l] = Short.parseShort(jar2.get(0).toString());
                    temp.arrMoblevel[l] = Integer.parseInt(jar2.get(1).toString());
                    temp.arrMobx[l] = Short.parseShort(jar2.get(2).toString());
                    temp.arrMoby[l] = Short.parseShort(jar2.get(3).toString());
                    temp.arrMobstatus[l] = Byte.parseByte(jar2.get(4).toString());
                    temp.arrLevelboss[l] = Byte.parseByte(jar2.get(5).toString());
                    temp.arrisboss[l] = Boolean.parseBoolean(jar2.get(6).toString());
                }

                Option = (JSONArray)JSONValue.parse(res.getString("NPC"));
                temp.npc = new Npc[Option.size()];

                for(j = 0; j < Option.size(); ++j) {
                    temp.npc[j] = new Npc();
                    jar2 = (JSONArray)JSONValue.parse(Option.get(j).toString());
                    Npc npc = temp.npc[j];
                    npc.type = Byte.parseByte(jar2.get(0).toString());
                    npc.x = Short.parseShort(jar2.get(1).toString());
                    npc.y = Short.parseShort(jar2.get(2).toString());
                    npc.id = Byte.parseByte(jar2.get(3).toString());
                }
                MapTemplate.arrTemplate[i] = temp;
                i++;
            }

            res.close();
            res = SQLManager.stat.executeQuery("SELECT * FROM `optionitem`;");

            while(res.next()) {
                ItemOptionTemplate item2 = new ItemOptionTemplate();
                item2.id = res.getInt("id");
                item2.name = res.getString("name");
                item2.type = res.getByte("type");
                ItemTemplate.put(item2.id, item2);
            }
            res.close();

            i = 0;
            JSONObject job;
            for(res = SQLManager.stat.executeQuery("SELECT * FROM `item`;"); res.next(); ++i) {
                ItemTemplate item = new ItemTemplate();
                item.id = Short.parseShort(res.getString("id"));
                item.type = Byte.parseByte(res.getString("type"));
                item.nclass = Byte.parseByte(res.getString("class"));
                item.skill = Byte.parseByte(res.getString("skill"));
                item.gender = Byte.parseByte(res.getString("gender"));
                item.name = res.getString("name");
                item.description = res.getString("description");
                item.level = Byte.parseByte(res.getString("level"));
                item.iconID = Short.parseShort(res.getString("iconID"));
                item.part = Short.parseShort(res.getString("part"));
                item.isUpToUp = Byte.parseByte(res.getString("uptoup")) == 1;
                item.isExpires = Byte.parseByte(res.getString("isExpires")) == 1;
                item.seconds_expires = Long.parseLong(res.getString("secondsExpires"));
                item.saleCoinLock = Integer.parseInt(res.getString("saleCoinLock"));
                item.itemoption = new ArrayList();
                Option = (JSONArray)JSONValue.parse(res.getString("ItemOption"));

                huydat.real.Option option;
                for(k = 0; k < Option.size(); ++k) {
                    job = (JSONObject)Option.get(k);
                    option = new Option(Integer.parseInt(job.get("id").toString()), Integer.parseInt(job.get("param").toString()));
                    item.itemoption.add(option);
                }

                item.option1 = new ArrayList();
                Option = (JSONArray)JSONValue.parse(res.getString("Option1"));

                for(k = 0; k < Option.size(); ++k) {
                    job = (JSONObject)Option.get(k);
                    option = new Option(Integer.parseInt(job.get("id").toString()), Integer.parseInt(job.get("param").toString()));
                    item.option1.add(option);
                }

                item.option2 = new ArrayList();
                Option = (JSONArray)JSONValue.parse(res.getString("Option2"));

                for(k = 0; k < Option.size(); ++k) {
                    job = (JSONObject)Option.get(k);
                    option = new Option(Integer.parseInt(job.get("id").toString()), Integer.parseInt(job.get("param").toString()));
                    item.option2.add(option);
                }

                item.option3 = new ArrayList();
                Option = (JSONArray)JSONValue.parse(res.getString("Option3"));

                for(k = 0; k < Option.size(); ++k) {
                    job = (JSONObject)Option.get(k);
                    option = new Option(Integer.parseInt(job.get("id").toString()), Integer.parseInt(job.get("param").toString()));
                    item.option3.add(option);
                }

                ItemTemplate.entrys.add(item);
            }
            res.close();

            i = 0;
            for(res = SQLManager.stat.executeQuery("SELECT * FROM `skill`;"); res.next(); ++i) {
                SkillTemplate skill = new SkillTemplate();
                skill.id = Short.parseShort(res.getString("id"));
                skill.nclass = Byte.parseByte(res.getString("class"));
                skill.name = res.getString("name");
                skill.maxPoint = Byte.parseByte(res.getString("maxPoint"));
                skill.type = Byte.parseByte(res.getString("type"));
                skill.iconId = Short.parseShort(res.getString("iconId"));
                skill.desc = res.getString("desc");
                Option = (JSONArray)JSONValue.parse(res.getString("SkillTemplates"));
                Iterator var33 = Option.iterator();

                while(var33.hasNext()) {
                    Object template = var33.next();
                    JSONObject job2 = (JSONObject)template;
                    SkillOptionTemplate temp2 = new SkillOptionTemplate();
                    temp2.skillId = Short.parseShort(job2.get("skillId").toString());
                    temp2.point = Byte.parseByte(job2.get("point").toString());
                    temp2.level = Integer.parseInt(job2.get("level").toString());
                    temp2.manaUse = Short.parseShort(job2.get("manaUse").toString());
                    temp2.coolDown = Integer.parseInt(job2.get("coolDown").toString());
                    temp2.dx = Short.parseShort(job2.get("dx").toString());
                    temp2.dy = Short.parseShort(job2.get("dy").toString());
                    temp2.maxFight = Byte.parseByte(job2.get("maxFight").toString());
                    JSONArray Option2 = (JSONArray)JSONValue.parse(job2.get("options").toString());
                    Iterator var12 = Option2.iterator();

                    while(var12.hasNext()) {
                        Object option2 = var12.next();
                        JSONObject job3 = (JSONObject)option2;
                        Option op = new Option(Integer.parseInt(job3.get("id").toString()), Integer.parseInt(job3.get("param").toString()));
                        temp2.options.add(op);
                    }

                    skill.templates.add(temp2);
                }

                SkillTemplate.entrys.add(skill);
            }
            res.close();

            //part
            i = 0;
            res = SQLManager.stat.executeQuery("SELECT * FROM `nj_part`;");
            while (res.next()) {
                byte type = res.getByte("type");
                JSONArray jA = (JSONArray) JSONValue.parse(res.getString("part"));
                Part part = new Part(type);
                for (i = 0; i < part.pi.length; i++) {
                    JSONObject o = (JSONObject) jA.get(i);
                    part.pi[i] = new PartImage();
                    part.pi[i].id = ((Long) o.get("id")).shortValue();
                    part.pi[i].dx = ((Long) o.get("dx")).byteValue();
                    part.pi[i].dy = ((Long) o.get("dy")).byteValue();
                }
                parts.add(part);
            }
            res.close();
            
            try {
                res = SQLManager.stat.executeQuery("SELECT * FROM `optionskill`;");
                if (res.last()) {
                    sOptionTemplates = new SkillOptionTemplates[res.getRow()];
                    res.beforeFirst();
                }
                i = 0;
                while (res.next()) {
                    final SkillOptionTemplates sotemplate = new SkillOptionTemplates();
                    sotemplate.id = res.getInt("id");
                    sotemplate.name = res.getString("name");
                    sOptionTemplates[i] = sotemplate;
                    ++i;
                }
                res.close();
            }
            catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
            System.err.println("Lấy optionskill");
            
            i = 0;
            for(res = SQLManager.stat.executeQuery("SELECT * FROM `itemsell`;"); res.next(); ++i) {
                ItemSell sell = new ItemSell();
                sell.id = Integer.parseInt(res.getString("id"));
                sell.type = Byte.parseByte(res.getString("type"));
                Option = (JSONArray)JSONValue.parse(res.getString("ListItem"));
                if (Option != null) {
                    sell.item = new Item[Option.size()];

                    for(j = 0; j < Option.size(); ++j) {
                        job = (JSONObject)Option.get(j);
                        Item item2 = ItemTemplate.parseItem(Option.get(j).toString());
                        item2.buyCoin = Integer.parseInt(job.get("buyCoin").toString());
                        item2.buyCoinLock = Integer.parseInt(job.get("buyCoinLock").toString());
                        item2.buyGold = Integer.parseInt(job.get("buyGold").toString());
                        sell.item[j] = item2;
                    }
                }
                ItemSell.entrys.add(sell);
            }
            res.close();
        } catch (Exception var14) {
            System.out.println("Error i:" + i);
            var14.printStackTrace();
            System.exit(0);
        }

        SQLManager.close();
        this.setCache(0);
        this.setCache(1);
        this.setCache(2);
        SQLManager.create(this.mysql_host, this.mysql_port, this.mysql_database_ninja, this.mysql_user, this.mysql_pass);
        this.loadGame();
    }

    private void loadGame() {
        try {
            ResultSet res = SQLManager.stat.executeQuery("SELECT * FROM `clan`;");
            while(res.next()) {
                ClanManager clan = new ClanManager();
                clan.id = Integer.parseInt(res.getString("id"));
                clan.name = res.getString("name");
                clan.exp = res.getInt("exp");
                clan.level = res.getInt("level");
                clan.itemLevel = res.getInt("itemLevel");
                clan.coin = res.getInt("coin");
                clan.reg_date = res.getString("reg_date");
                clan.log = res.getString("log");
                clan.alert = res.getString("alert");
                clan.use_card = res.getByte("use_card");
                clan.openDun = res.getByte("openDun");
                clan.debt = res.getByte("debt");
                JSONArray jar = (JSONArray)JSONValue.parse(res.getString("members"));
                if (jar != null) {
                    for(short j = 0; j < jar.size(); ++j) {
                        JSONArray jar2 = (JSONArray)jar.get(j);
                        ClanMember mem = new ClanMember();
                        mem.charID = Integer.parseInt(jar2.get(0).toString());
                        mem.cName = jar2.get(1).toString();
                        mem.clanName = jar2.get(2).toString();
                        mem.typeclan = Byte.parseByte(jar2.get(3).toString());
                        mem.clevel = Integer.parseInt(jar2.get(4).toString());
                        mem.nClass = Byte.parseByte(jar2.get(5).toString());
                        mem.pointClan = Integer.parseInt(jar2.get(6).toString());
                        mem.pointClanWeek = Integer.parseInt(jar2.get(7).toString());
                        clan.members.add(mem);
                    }
                }

                jar = (JSONArray)JSONValue.parse(res.getString("items"));
                if (jar != null) {
                    for(byte k = 0; k < jar.size(); ++k) {
                        clan.items.add(ItemTemplate.parseItem(jar.get(k).toString()));
                    }
                }
                clan.week = res.getString("week");
                ClanManager.entrys.add(clan);
            }
            res.close();

            res = SQLManager.stat.executeQuery("SELECT * FROM `alert`;");
            String alert;
            while(res.next()) {
                alert = res.getString("content");
                Manager.alert.setAlert(alert);
            }
            res.close();

            res = SQLManager.stat.executeQuery("SELECT * FROM `thiendia` ORDER BY `id` DESC LIMIT 2;");
            int count = 0;
            while (res.next()) {
                int id = Integer.parseInt(res.getString("id"));
                String week = res.getString("week");
                int type = Integer.parseInt(res.getString("type"));
                if(type == 1) {
                    ThienDiaBangManager.thienDiaBangManager[0] = new ThienDiaBangManager(id, week, type);
                } else if(type == 2) {
                    ThienDiaBangManager.thienDiaBangManager[1] = new ThienDiaBangManager(id, week, type);
                }
                JSONArray jar = (JSONArray)JSONValue.parse(res.getString("data"));
                if (jar != null) {
                    for(short j = 0; j < jar.size(); ++j) {
                        JSONArray jar2 = (JSONArray)jar.get(j);
                        String nameData = jar2.get(0).toString();
                        int typeData = Integer.parseInt(jar2.get(1).toString());
                        int rankData = Integer.parseInt(jar2.get(2).toString());
                        if(type == 1) {
                            ThienDiaBangManager.diaBangList.put(nameData, new ThienDiaData(nameData, rankData, typeData));
                        } else if(type == 2) {
                            ThienDiaBangManager.thienBangList.put(nameData, new ThienDiaData(nameData, rankData, typeData));
                        }
                    }
                }
                count++;
            }
            if(count == 0) {
                String week = Util.toDateString(Date.from(Instant.now()));
                SQLManager.stat.executeUpdate("INSERT INTO thiendia(`id`,`week`,`type`) VALUES (1,'" + week + "',1);");
                SQLManager.stat.executeUpdate("INSERT INTO thiendia(`id`,`week`,`type`) VALUES (2,'" + week + "',2);");
                ThienDiaBangManager.thienDiaBangManager[0] = new ThienDiaBangManager(1, week, 1);
                ThienDiaBangManager.thienDiaBangManager[1] = new ThienDiaBangManager(2, week, 2);
            }
            ThienDiaBangManager.rankDiaBang = ThienDiaBangManager.diaBangList.size() + 1;
            ThienDiaBangManager.rankThienBang = ThienDiaBangManager.thienBangList.size() + 1;
            res.close();

            res = SQLManager.stat.executeQuery("SELECT * FROM `shinwa`;");
            while(res.next()) {
                int id = Integer.parseInt(res.getString("id"));
                JSONArray jar = (JSONArray)JSONValue.parse(res.getString("data"));
                if (jar != null) {
                    List<ShinwaTemplate> list = new ArrayList<>();
                    for(short j = 0; j < jar.size(); ++j) {
                        JSONArray jar2 = (JSONArray)jar.get(j);
                        Item item = ItemTemplate.parseItem(jar2.get(0).toString());
                        long timeStart = Long.parseLong(jar2.get(1).toString());
                        String seller = jar2.get(2).toString();
                        long price = Long.parseLong(jar2.get(3).toString());
                        list.add(new ShinwaTemplate(item, timeStart, seller, price));
                    }
                    ShinwaManager.entrys.put(id, list);
                }
            }
            res.close();

            SQLManager.stat.executeUpdate("UPDATE `ninja` SET `caveID`=-1;");
        } catch (Exception var8) {
            var8.printStackTrace();
            System.exit(0);
        }

    }

    private void setCache(int id) {
        try {
            ByteArrayOutputStream bas;
            DataOutputStream dos;
            switch(id) {
                case 0: {
                    bas = new ByteArrayOutputStream();
                    dos = new DataOutputStream(bas);
                    dos.writeByte(this.vsMap);
                    dos.writeByte(MapTemplate.arrTemplate.length);
                    int iii;
                    for (iii = 0; iii < MapTemplate.arrTemplate.length; ++iii) {
                        dos.writeUTF(MapTemplate.arrTemplate[iii].name);
                    }
                    dos.writeByte(npcs.size());
                    Iterator var17 = npcs.iterator();
                    while (var17.hasNext()) {
                        NpcTemplate npc = (NpcTemplate) var17.next();
                        dos.writeUTF(npc.name);
                        dos.writeShort(npc.headId);
                        dos.writeShort(npc.bodyId);
                        dos.writeShort(npc.legId);
                        String[][] menu = npc.menu;
                        dos.writeByte(menu.length);
                        String[][] var25 = menu;
                        int var8 = menu.length;
                        for (int var9 = 0; var9 < var8; ++var9) {
                            String[] m = var25[var9];
                            dos.writeByte(m.length);
                            String[] var11 = m;
                            int var12 = m.length;
                            for (int var13 = 0; var13 < var12; ++var13) {
                                String s = var11[var13];
                                dos.writeUTF(s);
                            }
                        }
                    }
                    dos.writeByte(MobTemplate.entrys.size());
                    var17 = MobTemplate.entrys.iterator();
                    MobTemplate mob;
                    while (var17.hasNext()) {
                        mob = (MobTemplate) var17.next();
                        dos.writeByte(mob.type);
                        dos.writeUTF(mob.name);
                        dos.writeInt(mob.hp);
                        dos.writeByte(mob.rangeMove);
                        dos.writeByte(mob.speed);
                    }
                    byte[] ab = bas.toByteArray();
                    GameSrc.saveFile("res/cache/map", ab);
                    dos.close();
                    bas.close();
                    break;
                }
                case 1: {
                    bas = new ByteArrayOutputStream();
                    dos = new DataOutputStream(bas);
                    dos.writeByte(this.vsItem);
                    Collection<ItemOptionTemplate> options = ItemTemplate.getOptions();
                    dos.writeByte(options.size());
                    Iterator var5 = options.iterator();
                    while (var5.hasNext()) {
                        ItemOptionTemplate item2 = (ItemOptionTemplate) var5.next();
                        dos.writeUTF(item2.name);
                        dos.writeByte(item2.type);
                    }

                    Collection<ItemTemplate> entrys = ItemTemplate.entrys;
                    dos.writeShort(entrys.size());
                    Iterator var22 = entrys.iterator();

                    ItemTemplate item3;
                    while (var22.hasNext()) {
                        item3 = (ItemTemplate) var22.next();
                        dos.writeByte(item3.type);
                        dos.writeByte(item3.gender);
                        dos.writeUTF(item3.name);
                        dos.writeUTF(item3.description);
                        dos.writeByte(item3.level);
                        dos.writeShort(item3.iconID);
                        dos.writeShort(item3.part);
                        dos.writeBoolean(item3.isUpToUp);
                    }
                    byte[] ab = bas.toByteArray();
                    GameSrc.saveFile("res/cache/item", ab);
                    dos.close();
                    bas.close();
                    break;
                }
                case 2: {
                    bas = new ByteArrayOutputStream();
                    dos = new DataOutputStream(bas);
                    dos.writeShort(Manager.parts.size());
                    for (Part p : Manager.parts) {
                        dos.writeByte(p.type);
                        for (PartImage pi : p.pi) {
                            dos.writeShort(pi.id);
                            dos.writeByte(pi.dx);
                            dos.writeByte(pi.dy);
                        }
                    }
                    byte[] ab = bas.toByteArray();
                    GameSrc.saveFile("cache/part", ab);
                    dos.close();
                    bas.close();
                    break;
                }
            }
        } catch (IOException var15) {
            var15.printStackTrace();
        }

    }

    private void loadVersion() {
        try {
            ByteArrayOutputStream bas = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bas);
            dos.writeByte(this.vsData);
            dos.writeByte(this.vsMap);
            dos.writeByte(this.vsSkill);
            dos.writeByte(this.vsItem);
            byte[] abc  = GameSrc.loadFile("res/cache/cacheVersion").toByteArray();
            int i;
            for (i = 0; i < abc.length; i++) {
                dos.writeByte(abc[i]);
            }
            byte[] ab = bas.toByteArray();
            GameSrc.saveFile("res/cache/version", ab);
            dos.close();
            bas.close();
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public static void getPackMessage(Player p) {
        Message m = null;
        try {
            m = new Message(-28);
            m.writer().writeByte(-123);
            byte[] ab = GameSrc.loadFile("res/cache/version").toByteArray();
            m.writer().write(ab);
            m.writer().flush();
            p.conn.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }
    

    public void sendData(Player p) {
        Message m = null;
        try {
            m = new Message(-28);
            m.writer().writeByte(-122);
            m.writer().writeByte(this.vsData);
            byte[] ab = GameSrc.loadFile("res/cache/data/nj_arrow").toByteArray();
            m.writer().writeInt(ab.length);
            m.writer().write(ab);
            ab = GameSrc.loadFile("res/cache/data/nj_effect").toByteArray();
            m.writer().writeInt(ab.length);
            m.writer().write(ab);
            ab = GameSrc.loadFile("res/cache/data/nj_image").toByteArray();
            m.writer().writeInt(ab.length);
            m.writer().write(ab);
            /*ab = GameSrc.loadFile("res/cache/data/nj_part").toByteArray();
            m.writer().writeInt(ab.length);
            m.writer().write(ab);*/
            ab = GameSrc.loadFile("cache/part").toByteArray();
            m.writer().writeInt(ab.length);
            m.writer().write(ab);
            ab = GameSrc.loadFile("res/cache/data/nj_skill").toByteArray();
            m.writer().writeInt(ab.length);
            m.writer().write(ab);
            m.writer().writeByte(this.tasks.length);
            byte i;
            for(i = 0; i < this.tasks.length; ++i) {
                m.writer().writeByte(this.tasks[i].length);
                for(byte j = 0; j < this.tasks[i].length; ++j) {
                    m.writer().writeByte(this.tasks[i][j]);
                    m.writer().writeByte(this.maptasks[i][j]);
                }
            }
            m.writer().writeByte(Level.entrys.size());
            Iterator var6 = Level.entrys.iterator();
            Level entry;
            while(var6.hasNext()) {
                entry = (Level)var6.next();
                m.writer().writeLong(entry.exps);
            }
            m.writer().writeByte(GameSrc.crystals.length);
            for(i = 0; i < GameSrc.crystals.length; ++i) {
                m.writer().writeInt(GameSrc.crystals[i]);
            }
            m.writer().writeByte(GameSrc.upClothe.length);
            for(i = 0; i < GameSrc.upClothe.length; ++i) {
                m.writer().writeInt(GameSrc.upClothe[i]);
            }
            m.writer().writeByte(GameSrc.upAdorn.length);
            for(i = 0; i < GameSrc.upAdorn.length; ++i) {
                m.writer().writeInt(GameSrc.upAdorn[i]);
            }
            m.writer().writeByte(GameSrc.upWeapon.length);
            for(i = 0; i < GameSrc.upWeapon.length; ++i) {
                m.writer().writeInt(GameSrc.upWeapon[i]);
            }
            m.writer().writeByte(GameSrc.coinUpCrystals.length);
            for(i = 0; i < GameSrc.coinUpCrystals.length; ++i) {
                m.writer().writeInt(GameSrc.coinUpCrystals[i]);
            }
            m.writer().writeByte(GameSrc.coinUpClothes.length);
            for(i = 0; i < GameSrc.coinUpClothes.length; ++i) {
                m.writer().writeInt(GameSrc.coinUpClothes[i]);
            }
            m.writer().writeByte(GameSrc.coinUpAdorns.length);
            for(i = 0; i < GameSrc.coinUpAdorns.length; ++i) {
                m.writer().writeInt(GameSrc.coinUpAdorns[i]);
            }
            m.writer().writeByte(GameSrc.coinUpWeapons.length);
            for(i = 0; i < GameSrc.coinUpWeapons.length; ++i) {
                m.writer().writeInt(GameSrc.coinUpWeapons[i]);
            }
            m.writer().writeByte(GameSrc.goldUps.length);
            for(i = 0; i < GameSrc.goldUps.length; ++i) {
                m.writer().writeInt(GameSrc.goldUps[i]);
            }
            m.writer().writeByte(GameSrc.maxPercents.length);
            for(i = 0; i < GameSrc.maxPercents.length; ++i) {
                m.writer().writeInt(GameSrc.maxPercents[i]);
            }
            m.writer().writeByte(EffectTemplate.entrys.size());
            for(i = 0; i < EffectTemplate.entrys.size(); ++i) {
                m.writer().writeByte((EffectTemplate.entrys.get(i)).id);
                m.writer().writeByte((EffectTemplate.entrys.get(i)).type);
                m.writer().writeUTF((EffectTemplate.entrys.get(i)).name);
                m.writer().writeShort((EffectTemplate.entrys.get(i)).iconId);
            }
            m.writer().flush();
            p.conn.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public static void chatKTG(String chat) {
        Message m = null;
        try {
            m = new Message(-25);
            m.writer().writeUTF(chat);
            m.writer().flush();
            Client.gI().NinjaMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m !=null) {
                m.cleanup();
            }
        }

    }

    public void Infochat(String chat) {
        Message m = null;
        try {
            m = new Message(-24);
            m.writer().writeUTF(chat);
            m.writer().flush();
            Client.gI().NinjaMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }

    /*protected void stop() {
    }*/

    protected void chatKTG(Player p, Message m) {
        try {
            String chat = m.reader().readUTF();
            m.cleanup();
            if (p.chatKTGdelay > System.currentTimeMillis()) {
                p.conn.sendMessageLog("Chờ sau " + (p.chatKTGdelay - System.currentTimeMillis()) / 1000L + "s.");
            } else {
                p.chatKTGdelay = System.currentTimeMillis() + 5000L;
                if (p.luong < 10) {
                    p.conn.sendMessageLog("Bạn không đủ 10 lượng trên người.");
                } else {
                    p.luongMessage(-10L);
                    serverChat(p.c.name, chat);
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

    public static void serverChat(String name, String s) {
        Message m = null;
        try {
            m = new Message(-21);
            m.writer().writeUTF(name);
            m.writer().writeUTF(s);
            m.writer().flush();
            Client.gI().NinjaMessage(m);

        } catch (Exception var3) {
            var3.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }

    public void sendTB(Player p, String title, String s) {
        Message m = null;
        try {
            m = new Message(53);
            m.writer().writeUTF(title);
            m.writer().writeUTF(s);
            m.writer().flush();
            p.conn.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }

    }

    public void close() {
        for (int i = 0; i < this.rotationluck.length; ++i) {
            this.rotationluck[i].close();
            this.rotationluck[i] = null;
        }
        this.rotationluck = null;
        for (int i = 0; i < Server.maps.length; ++i) {
            Server.maps[i].close();
            Server.maps[i] = null;
        }
        Server.maps = null;
        ClanManager.close();
    }
    
    public void sendMap(Player p) throws IOException {
        Message m = new Message(-28);
        try {
            m.writer().writeByte(-121);
            m.writer().writeByte(this.vsMap);
            m.writer().writeByte(MapTemplate.arrTemplate.length);
            for (short i = 0; i < MapTemplate.arrTemplate.length; ++i) {
                m.writer().writeUTF(MapTemplate.arrTemplate[i].name);
            }
            m.writer().writeByte(npcs.size());
            Iterator var17 = npcs.iterator();
            while (var17.hasNext()) {
                NpcTemplate npc = (NpcTemplate) var17.next();
                m.writer().writeUTF(npc.name);
                m.writer().writeShort(npc.headId);
                m.writer().writeShort(npc.bodyId);
                m.writer().writeShort(npc.legId);
                String[][] menu = npc.menu;
                m.writer().writeByte(menu.length);
                String[][] var25 = menu;
                int var8 = menu.length;
                for (int var9 = 0; var9 < var8; ++var9) {
                    String[] str = var25[var9];
                    m.writer().writeByte(str.length);
                    String[] var11 = str;
                    int var12 = str.length;
                    for (int var13 = 0; var13 < var12; ++var13) {
                        String s = var11[var13];
                        m.writer().writeUTF(s);
                    }
                }
            }
            m.writer().writeByte(MobTemplate.entrys.size());
            var17 = MobTemplate.entrys.iterator();
            MobTemplate mob;
            while (var17.hasNext()) {
                mob = (MobTemplate) var17.next();
                m.writer().writeByte(mob.type);
                m.writer().writeUTF(mob.name);
                m.writer().writeInt(mob.hp);
                m.writer().writeByte(mob.rangeMove);
                m.writer().writeByte(mob.speed);
            }
            m.writer().flush();
            p.conn.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }
    
    public void sendItem(Player p) throws IOException {

        Message m = new Message(-28);
        try {          
            m.writer().writeByte(-119);
            m.writer().writeByte(this.vsItem);
            Collection<ItemOptionTemplate> options = ItemTemplate.getOptions();
            m.writer().writeByte(options.size());
            Iterator var5 = options.iterator();
            while (var5.hasNext()) {
                ItemOptionTemplate item2 = (ItemOptionTemplate) var5.next();
                m.writer().writeUTF(item2.name);
                m.writer().writeByte(item2.type);
            }
            m.writer().writeShort(ItemTemplate.entrys.size());
            for (short j = 0; j < ItemTemplate.entrys.size(); ++j) {
                m.writer().writeByte(ItemTemplate.entrys.get(j).type);
                m.writer().writeByte(ItemTemplate.entrys.get(j).gender);
                m.writer().writeUTF(ItemTemplate.entrys.get(j).name);
                m.writer().writeUTF(ItemTemplate.entrys.get(j).description);
                m.writer().writeByte(ItemTemplate.entrys.get(j).level);
                m.writer().writeShort(ItemTemplate.entrys.get(j).iconID);
                m.writer().writeShort(ItemTemplate.entrys.get(j).part);
                m.writer().writeBoolean(ItemTemplate.entrys.get(j).isUpToUp);
            }
            m.writer().flush();
            p.conn.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }
    
   public void sendSkill(Player p) throws IOException {
        Message m = new Message(-28);
        try {
            m.writer().writeByte(-120);
            m.writer().writeByte(this.vsSkill);
            m.writer().writeByte(Manager.sOptionTemplates.length);
            for (short i = 0; i < Manager.sOptionTemplates.length; ++i) {
                m.writer().writeUTF(Manager.sOptionTemplates[i].name);
            }
            m.writer().writeByte(7);//nclasssize
            m.writer().writeUTF("Chưa vào lớp");
            m.writer().writeByte(1);//skillsize
            for (short j = 0; j < SkillTemplate.entrys.size(); ++j) {
                if (SkillTemplate.entrys.get(j).nclass == 0) {
                    m.writer().writeByte(SkillTemplate.entrys.get(j).id);
                    m.writer().writeUTF(SkillTemplate.entrys.get(j).name);
                    m.writer().writeByte(SkillTemplate.entrys.get(j).maxPoint);
                    m.writer().writeByte(SkillTemplate.entrys.get(j).type);
                    m.writer().writeShort(SkillTemplate.entrys.get(j).iconId);
                    m.writer().writeUTF(SkillTemplate.entrys.get(j).desc);
                    m.writer().writeByte(SkillTemplate.entrys.get(j).templates.size());
                    for (int k = 0; k < SkillTemplate.entrys.get(j).templates.size(); k++) {
                        m.writer().writeShort(SkillTemplate.entrys.get(j).templates.get(k).skillId);
                        m.writer().writeByte(SkillTemplate.entrys.get(j).templates.get(k).point);
                        m.writer().writeByte(SkillTemplate.entrys.get(j).templates.get(k).level);
                        m.writer().writeShort(SkillTemplate.entrys.get(j).templates.get(k).manaUse);
                        m.writer().writeInt(SkillTemplate.entrys.get(j).templates.get(k).coolDown);
                        m.writer().writeShort(SkillTemplate.entrys.get(j).templates.get(k).dx);
                        m.writer().writeShort(SkillTemplate.entrys.get(j).templates.get(k).dy);
                        m.writer().writeByte(SkillTemplate.entrys.get(j).templates.get(k).maxFight);
                        m.writer().writeByte(SkillTemplate.entrys.get(j).templates.get(k).options.size());
                        for (int l = 0; l < SkillTemplate.entrys.get(j).templates.get(k).options.size(); l++) {
                            m.writer().writeShort(SkillTemplate.entrys.get(j).templates.get(k).options.get(l).param);
                            m.writer().writeByte(SkillTemplate.entrys.get(j).templates.get(k).options.get(l).id);
                        }
                    }
                }
            }
            m.writer().writeUTF("Ninja Kiếm");
            m.writer().writeByte(14);
            for (short j = 0; j < SkillTemplate.entrys.size(); ++j) {
                if (SkillTemplate.entrys.get(j).nclass == 1) {
                    m.writer().writeByte(SkillTemplate.entrys.get(j).id);
                    m.writer().writeUTF(SkillTemplate.entrys.get(j).name);
                    m.writer().writeByte(SkillTemplate.entrys.get(j).maxPoint);
                    m.writer().writeByte(SkillTemplate.entrys.get(j).type);
                    m.writer().writeShort(SkillTemplate.entrys.get(j).iconId);
                    m.writer().writeUTF(SkillTemplate.entrys.get(j).desc);
                    m.writer().writeByte(SkillTemplate.entrys.get(j).templates.size());
                    for (int k = 0; k < SkillTemplate.entrys.get(j).templates.size(); k++) {
                        m.writer().writeShort(SkillTemplate.entrys.get(j).templates.get(k).skillId);
                        m.writer().writeByte(SkillTemplate.entrys.get(j).templates.get(k).point);
                        m.writer().writeByte(SkillTemplate.entrys.get(j).templates.get(k).level);
                        m.writer().writeShort(SkillTemplate.entrys.get(j).templates.get(k).manaUse);
                        m.writer().writeInt(SkillTemplate.entrys.get(j).templates.get(k).coolDown);
                        m.writer().writeShort(SkillTemplate.entrys.get(j).templates.get(k).dx);
                        m.writer().writeShort(SkillTemplate.entrys.get(j).templates.get(k).dy);
                        m.writer().writeByte(SkillTemplate.entrys.get(j).templates.get(k).maxFight);
                        m.writer().writeByte(SkillTemplate.entrys.get(j).templates.get(k).options.size());
                        for (int l = 0; l < SkillTemplate.entrys.get(j).templates.get(k).options.size(); l++) {
                            m.writer().writeShort(SkillTemplate.entrys.get(j).templates.get(k).options.get(l).param);
                            m.writer().writeByte(SkillTemplate.entrys.get(j).templates.get(k).options.get(l).id);
                        }
                    }
                }
            }
            m.writer().writeUTF("Ninja Phi Tiêu");
            m.writer().writeByte(14);
            for (short j = 0; j < SkillTemplate.entrys.size(); ++j) {
                if (SkillTemplate.entrys.get(j).nclass == 2) {
                    m.writer().writeByte(SkillTemplate.entrys.get(j).id);
                    m.writer().writeUTF(SkillTemplate.entrys.get(j).name);
                    m.writer().writeByte(SkillTemplate.entrys.get(j).maxPoint);
                    m.writer().writeByte(SkillTemplate.entrys.get(j).type);
                    m.writer().writeShort(SkillTemplate.entrys.get(j).iconId);
                    m.writer().writeUTF(SkillTemplate.entrys.get(j).desc);
                    m.writer().writeByte(SkillTemplate.entrys.get(j).templates.size());
                    for (int k = 0; k < SkillTemplate.entrys.get(j).templates.size(); k++) {
                        m.writer().writeShort(SkillTemplate.entrys.get(j).templates.get(k).skillId);
                        m.writer().writeByte(SkillTemplate.entrys.get(j).templates.get(k).point);
                        m.writer().writeByte(SkillTemplate.entrys.get(j).templates.get(k).level);
                        m.writer().writeShort(SkillTemplate.entrys.get(j).templates.get(k).manaUse);
                        m.writer().writeInt(SkillTemplate.entrys.get(j).templates.get(k).coolDown);
                        m.writer().writeShort(SkillTemplate.entrys.get(j).templates.get(k).dx);
                        m.writer().writeShort(SkillTemplate.entrys.get(j).templates.get(k).dy);
                        m.writer().writeByte(SkillTemplate.entrys.get(j).templates.get(k).maxFight);
                        m.writer().writeByte(SkillTemplate.entrys.get(j).templates.get(k).options.size());
                        for (int l = 0; l < SkillTemplate.entrys.get(j).templates.get(k).options.size(); l++) {
                            m.writer().writeShort(SkillTemplate.entrys.get(j).templates.get(k).options.get(l).param);
                            m.writer().writeByte(SkillTemplate.entrys.get(j).templates.get(k).options.get(l).id);
                        }
                    }
                }
            }
            m.writer().writeUTF("Ninja Kunai");
            m.writer().writeByte(14);
            for (short j = 0; j < SkillTemplate.entrys.size(); ++j) {
                if (SkillTemplate.entrys.get(j).nclass == 3) {
                    m.writer().writeByte(SkillTemplate.entrys.get(j).id);
                    m.writer().writeUTF(SkillTemplate.entrys.get(j).name);
                    m.writer().writeByte(SkillTemplate.entrys.get(j).maxPoint);
                    m.writer().writeByte(SkillTemplate.entrys.get(j).type);
                    m.writer().writeShort(SkillTemplate.entrys.get(j).iconId);
                    m.writer().writeUTF(SkillTemplate.entrys.get(j).desc);
                    m.writer().writeByte(SkillTemplate.entrys.get(j).templates.size());
                    for (int k = 0; k < SkillTemplate.entrys.get(j).templates.size(); k++) {
                        m.writer().writeShort(SkillTemplate.entrys.get(j).templates.get(k).skillId);
                        m.writer().writeByte(SkillTemplate.entrys.get(j).templates.get(k).point);
                        m.writer().writeByte(SkillTemplate.entrys.get(j).templates.get(k).level);
                        m.writer().writeShort(SkillTemplate.entrys.get(j).templates.get(k).manaUse);
                        m.writer().writeInt(SkillTemplate.entrys.get(j).templates.get(k).coolDown);
                        m.writer().writeShort(SkillTemplate.entrys.get(j).templates.get(k).dx);
                        m.writer().writeShort(SkillTemplate.entrys.get(j).templates.get(k).dy);
                        m.writer().writeByte(SkillTemplate.entrys.get(j).templates.get(k).maxFight);
                        m.writer().writeByte(SkillTemplate.entrys.get(j).templates.get(k).options.size());
                        for (int l = 0; l < SkillTemplate.entrys.get(j).templates.get(k).options.size(); l++) {
                            m.writer().writeShort(SkillTemplate.entrys.get(j).templates.get(k).options.get(l).param);
                            m.writer().writeByte(SkillTemplate.entrys.get(j).templates.get(k).options.get(l).id);
                        }
                    }
                }
            }
            m.writer().writeUTF("Ninja Cung");
            m.writer().writeByte(14);
            for (short j = 0; j < SkillTemplate.entrys.size(); ++j) {
                if (SkillTemplate.entrys.get(j).nclass == 4) {
                    m.writer().writeByte(SkillTemplate.entrys.get(j).id);
                    m.writer().writeUTF(SkillTemplate.entrys.get(j).name);
                    m.writer().writeByte(SkillTemplate.entrys.get(j).maxPoint);
                    m.writer().writeByte(SkillTemplate.entrys.get(j).type);
                    m.writer().writeShort(SkillTemplate.entrys.get(j).iconId);
                    m.writer().writeUTF(SkillTemplate.entrys.get(j).desc);
                    m.writer().writeByte(SkillTemplate.entrys.get(j).templates.size());
                    for (int k = 0; k < SkillTemplate.entrys.get(j).templates.size(); k++) {
                        m.writer().writeShort(SkillTemplate.entrys.get(j).templates.get(k).skillId);
                        m.writer().writeByte(SkillTemplate.entrys.get(j).templates.get(k).point);
                        m.writer().writeByte(SkillTemplate.entrys.get(j).templates.get(k).level);
                        m.writer().writeShort(SkillTemplate.entrys.get(j).templates.get(k).manaUse);
                        m.writer().writeInt(SkillTemplate.entrys.get(j).templates.get(k).coolDown);
                        m.writer().writeShort(SkillTemplate.entrys.get(j).templates.get(k).dx);
                        m.writer().writeShort(SkillTemplate.entrys.get(j).templates.get(k).dy);
                        m.writer().writeByte(SkillTemplate.entrys.get(j).templates.get(k).maxFight);
                        m.writer().writeByte(SkillTemplate.entrys.get(j).templates.get(k).options.size());
                        for (int l = 0; l < SkillTemplate.entrys.get(j).templates.get(k).options.size(); l++) {
                            m.writer().writeShort(SkillTemplate.entrys.get(j).templates.get(k).options.get(l).param);
                            m.writer().writeByte(SkillTemplate.entrys.get(j).templates.get(k).options.get(l).id);
                        }
                    }
                }
            }
            m.writer().writeUTF("Ninja Đao");
            m.writer().writeByte(14);
            for (short j = 0; j < SkillTemplate.entrys.size(); ++j) {
                if (SkillTemplate.entrys.get(j).nclass == 5) {
                    m.writer().writeByte(SkillTemplate.entrys.get(j).id);
                    m.writer().writeUTF(SkillTemplate.entrys.get(j).name);
                    m.writer().writeByte(SkillTemplate.entrys.get(j).maxPoint);
                    m.writer().writeByte(SkillTemplate.entrys.get(j).type);
                    m.writer().writeShort(SkillTemplate.entrys.get(j).iconId);
                    m.writer().writeUTF(SkillTemplate.entrys.get(j).desc);
                    m.writer().writeByte(SkillTemplate.entrys.get(j).templates.size());
                    for (int k = 0; k < SkillTemplate.entrys.get(j).templates.size(); k++) {
                        m.writer().writeShort(SkillTemplate.entrys.get(j).templates.get(k).skillId);
                        m.writer().writeByte(SkillTemplate.entrys.get(j).templates.get(k).point);
                        m.writer().writeByte(SkillTemplate.entrys.get(j).templates.get(k).level);
                        m.writer().writeShort(SkillTemplate.entrys.get(j).templates.get(k).manaUse);
                        m.writer().writeInt(SkillTemplate.entrys.get(j).templates.get(k).coolDown);
                        m.writer().writeShort(SkillTemplate.entrys.get(j).templates.get(k).dx);
                        m.writer().writeShort(SkillTemplate.entrys.get(j).templates.get(k).dy);
                        m.writer().writeByte(SkillTemplate.entrys.get(j).templates.get(k).maxFight);
                        m.writer().writeByte(SkillTemplate.entrys.get(j).templates.get(k).options.size());
                        for (int l = 0; l < SkillTemplate.entrys.get(j).templates.get(k).options.size(); l++) {
                            m.writer().writeShort(SkillTemplate.entrys.get(j).templates.get(k).options.get(l).param);
                            m.writer().writeByte(SkillTemplate.entrys.get(j).templates.get(k).options.get(l).id);
                        }
                    }
                }
            }
            m.writer().writeUTF("Ninja Quạt");
            m.writer().writeByte(14);
            for (short j = 0; j < SkillTemplate.entrys.size(); ++j) {
                if (SkillTemplate.entrys.get(j).nclass == 6) {
                    m.writer().writeByte(SkillTemplate.entrys.get(j).id);
                    m.writer().writeUTF(SkillTemplate.entrys.get(j).name);
                    m.writer().writeByte(SkillTemplate.entrys.get(j).maxPoint);
                    m.writer().writeByte(SkillTemplate.entrys.get(j).type);
                    m.writer().writeShort(SkillTemplate.entrys.get(j).iconId);
                    m.writer().writeUTF(SkillTemplate.entrys.get(j).desc);
                    m.writer().writeByte(SkillTemplate.entrys.get(j).templates.size());
                    for (int k = 0; k < SkillTemplate.entrys.get(j).templates.size(); k++) {
                        m.writer().writeShort(SkillTemplate.entrys.get(j).templates.get(k).skillId);
                        m.writer().writeByte(SkillTemplate.entrys.get(j).templates.get(k).point);
                        m.writer().writeByte(SkillTemplate.entrys.get(j).templates.get(k).level);
                        m.writer().writeShort(SkillTemplate.entrys.get(j).templates.get(k).manaUse);
                        m.writer().writeInt(SkillTemplate.entrys.get(j).templates.get(k).coolDown);
                        m.writer().writeShort(SkillTemplate.entrys.get(j).templates.get(k).dx);
                        m.writer().writeShort(SkillTemplate.entrys.get(j).templates.get(k).dy);
                        m.writer().writeByte(SkillTemplate.entrys.get(j).templates.get(k).maxFight);
                        m.writer().writeByte(SkillTemplate.entrys.get(j).templates.get(k).options.size());
                        for (int l = 0; l < SkillTemplate.entrys.get(j).templates.get(k).options.size(); l++) {
                            m.writer().writeShort(SkillTemplate.entrys.get(j).templates.get(k).options.get(l).param);
                            m.writer().writeByte(SkillTemplate.entrys.get(j).templates.get(k).options.get(l).id);
                        }
                    }
                }
            }
            m.writer().flush();
            p.conn.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    /*public void sendMap(Player p) {
        Message m = null;
        try {
            m = new Message(-28);
            m.writer().writeByte(-121);
            m.writer().write(Server.cache[1].toByteArray());
            m.writer().flush();
            p.conn.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }*/

    /*public void sendSkill(Player p) {
        Message m = null;
        try {
            m = new Message(-28);
            m.writer().writeByte(-120);
            m.writer().write(Server.cache[2].toByteArray());
            m.writer().flush();
            p.conn.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }*/

    /*public void sendItem(Player p) {
        Message m = null;
        try {
            m = new Message(-28);
            m.writer().writeByte(-119);
            m.writer().write(Server.cache[3].toByteArray());
            m.writer().flush();
            p.conn.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }*/
}
