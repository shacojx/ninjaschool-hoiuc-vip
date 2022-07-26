package huydat.real;

import huydat.io.Util;
import huydat.template.ItemTemplate;

import java.util.ArrayList;
import java.util.List;

public class Item {
    public short id;
    public boolean isLock;
    public byte upgrade;
    public boolean isExpires;
    public int quantity;
    public long expires;
    public int saleCoinLock;
    public int buyCoin;
    public int buyCoinLock;
    public int buyGold;
    public byte sys;
    public ArrayList<Option> options;
    public List<Item> ngocs;
    private static final short[] DEFAULT_RANDOM_ITEM_IDS = new short[]{7, 8, 9, 436, 437, 438, 695};

    public Item() {
        this.id = -1;
        this.isLock = false;
        this.setUpgrade(0);
        this.isExpires = false;
        this.quantity = 1;
        this.expires = -1L;
        this.saleCoinLock = 0;
        this.buyCoin = 0;
        this.buyCoinLock = 0;
        this.buyGold = 0;
        this.sys = 0;
        this.options = new ArrayList<Option>();
        this.ngocs = new ArrayList();
    }
public void upBKnext(byte next) {
        this.setUpgrade(this.getUpgrade() + next);
        if (this.options != null) {
        short i;
        Option op;
        for (i = 0; i<this.options.size(); ++i) {
            op = this.options.get(i);
            if (op.id == 82 || op.id == 83) {
                Option ops = op;
                ops.param += 400 * next;
            }
           else if (op.id == 84 || op.id == 81 || op.id == 95 || op.id == 96 | op.id == 97) {
                Option ops2 = op;
                ops2.param += 50 * next;
            }
           else if (op.id == 87 || op.id == 88 || op.id == 89 || op.id == 90) {
                Option ops3 = op;
                ops3.param += 250 * next;
            }
           else if (op.id == 92 || op.id == 80 || op.id == 94 || op.id == 86) {
                Option ops4 = op;
                ops4.param += 15 * next;
            }
        }
    }
    }
    public Item clone() {
        Item item = new Item();
        item.id = this.id;
        item.isLock = this.isLock;
        item.setUpgrade(this.getUpgrade());
        item.isExpires = this.isExpires;
        item.quantity = this.quantity;
        item.expires = this.expires;
        item.saleCoinLock = this.saleCoinLock;
        item.buyCoin = this.buyCoin;
        item.buyCoinLock = this.buyCoinLock;
        item.buyGold = this.buyGold;
        item.sys = this.sys;
        for (int i = 0; i < this.options.size(); ++i) {
            item.options.add(new Option(this.options.get(i).id, this.options.get(i).param));
        }
        item.ngocs.addAll(this.ngocs);
        return item;
    }

    public int getUpMax() {
        ItemTemplate data = ItemTemplate.ItemTemplateId(this.id);
        if (data.level >= 1 && data.level < 20) {
            return 4;
        }
        if (data.level >= 20 && data.level < 40) {
            return 8;
        }
        if (data.level >= 40 && data.level < 50) {
            return 12;
        }
        if (data.level >= 50 && data.level < 60) {
            return 14;
        }
        return 16;
    }
    
    public void upgradeNext(byte next) {
        this.setUpgrade(this.getUpgrade() + next);
        if (this.options != null) {
            short i;
            Option itemOption;
            for (i = 0; i < this.options.size(); ++i) {
                itemOption = this.options.get(i);
                if (itemOption.id == 6 || itemOption.id == 7) {
                    Option option = itemOption;
                    option.param += 15 * next;
                }
                else if (itemOption.id == 8 || itemOption.id == 9 || itemOption.id == 19) {
                    Option option2 = itemOption;
                    option2.param += 10 * next;
                }
                else if (itemOption.id == 10 || itemOption.id == 11 || itemOption.id == 12 || itemOption.id == 13 || itemOption.id == 14 || itemOption.id == 15 || itemOption.id == 17 || itemOption.id == 18 || itemOption.id == 20) {
                    Option option3 = itemOption;
                    option3.param += 5 * next;
                }
                else if (itemOption.id == 21 || itemOption.id == 22 || itemOption.id == 23 || itemOption.id == 24 || itemOption.id == 25 || itemOption.id == 26) {
                    Option option4 = itemOption;
                    option4.param += 150 * next;
                }
                else if (itemOption.id == 16) {
                    Option option5 = itemOption;
                    option5.param += 3 * next;
                }
            }
        }
    }
    
    public void ncMatNa(byte next) {
        this.setUpgrade(this.getUpgrade() + next);
        if (this.options != null) {
            short i;
            Option itemOption;
            for (i = 0; i < this.options.size(); ++i) {
                itemOption = this.options.get(i);
                if (itemOption.id == 87){
                    Option option6 = itemOption;
                    option6.param += 10000 * next;
                }               
                else if (itemOption.id == 94){
                    Option option8 = itemOption;
                    option8.param += next;
                }
                else if (itemOption.id == 113){
                    Option option9 = itemOption;
                    option9.param += 200 * next;
                }              
                else if (itemOption.id == 114){
                    Option option9 = itemOption;
                    option9.param += 50 * next;
                }
            }
        }
    }
    
    public void ncYoroi(byte next) {
        this.setUpgrade(this.getUpgrade() + next);
        if (this.options != null) {
            short i;
            Option itemOption;
            for (i = 0; i < this.options.size(); ++i) {
                itemOption = this.options.get(i);
                if (itemOption.id == 82 || itemOption.id == 83){
                    Option option = itemOption;
                    option.param += 3126 * next;
                }
                else if (itemOption.id == 84){
                    Option option2 = itemOption;
                    option2.param += 10 * next;
                }
                else if (itemOption.id == 81|| itemOption.id == 79){
                    Option option3 = itemOption;
                    option3.param += 1 * next;
                }
                else if (itemOption.id == 80){
                    Option option3 = itemOption;
                    option3.param += 50 * next;
                }
            }
        }
    }
    
    public void NhanVKTop(int param){
        if (this.options != null) {          
            short i;
            Option itemOption;
            if(param == 300) {
                for (i = 0; i < this.options.size(); ++i) {
                    itemOption = this.options.get(i);
                    if (itemOption.id == 0 || itemOption.id == 1) {
                        Option option = itemOption;
                        option.param = 1000;
                    }
                    if (itemOption.id == 8 || itemOption.id == 9) {                 
                       Option option2 = itemOption;
                       option2.param = 300;
                    }
                }
            }
            else if (param == 500){
                for (i = 0; i < this.options.size(); ++i) {
                    itemOption = this.options.get(i);
                    if (itemOption.id == 0 || itemOption.id == 1) {
                        Option option = itemOption;
                        option.param = 2000;
                    }
                    if (itemOption.id == 8 || itemOption.id == 9) {                 
                       Option option2 = itemOption;
                       option2.param = 500;
                    }
                }
            }
        }
    }
    
    public void NhanUngLong(){
        if (this.options != null) {          
            short i;
            Option itemOption;
            for (i = 0; i < this.options.size(); ++i) {
                itemOption = this.options.get(i);
                if (itemOption.id == 133 || itemOption.id == 134) {
                    Option option = itemOption;
                    option.param = Util.nextInt(1, 12);
                }
                if (itemOption.id == 6) {
                    Option option2 = itemOption;
                    option2.param = Util.nextInt(10000, 68000);
                }
            }
        }
    }
    
    public int VKTop(){
        if (this.options != null) {
            short i;
            Option itemOption;
            for (i = 0; i < this.options.size(); ++i) {
                itemOption = this.options.get(i);
                if (itemOption.id == 8 || itemOption.id == 9) {
                    Option option = itemOption;
                    if(option.param >= 300){
                        return option.param;
                    }
                    else if(option.param >= 500){
                        return option.param;
                    }
                    return option.param;
                }
            }
        }
        return 0;
    }

    public int getOptionShopMin(int opid, int param) {
        if (opid == 0 || opid == 1 || opid == 21 || opid == 22 || opid == 23 || opid == 24 || opid == 25 || opid == 26) {
            return param - 50 + 1;
        }
        if (opid == 6 || opid == 7 || opid == 8 || opid == 9 || opid == 19) {
            return param - 10 + 1;
        }
        if (opid == 2 || opid == 3 || opid == 4 || opid == 5 || opid == 10 || opid == 11 || opid == 12 || opid == 13 || opid == 14 || opid == 15 || opid == 17 || opid == 18 || opid == 20) {
            return param - 5 + 1;
        }
        if (opid == 16) {
            return param - 3 + 1;
        }
        return param;
    }

    public boolean isTypeBody() {
        return ItemTemplate.isTypeBody(this.id);
    }

    public boolean isTypeNgocKham() {
        return ItemTemplate.isTypeNgocKham(this.id);
    }

    public ItemTemplate getData() {
        return ItemTemplate.ItemTemplateId(this.id);
    }

    public byte getUpgrade() {
        return this.upgrade;
    }

    public void setUpgrade(int upgrade) {
        this.upgrade = (byte)upgrade;
    }

    protected boolean isTypeTask() {
        ItemTemplate data = this.getData();
        return data.type == 23 || data.type == 24 || data.type == 25;
    }

    public boolean isLock() {
        return this.isLock;
    }
    
    /*public boolean isTL() {
        boolean check = false;
        if (this.options != null) {
            short i;
            Option itemOption;
            for (i = 0; i < this.options.size(); ++i) {
                itemOption = this.options.get(i);
                if (itemOption.id == 85){
                    check = true;
                    break;                
                }
            }
        }
        return check;
    }*/

    public void setLock(boolean lock) {
        this.isLock = lock;
    }

 
}
