/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package huydat.server;


import java.util.ArrayList;

public class SoiCau {
  public String ketqua;
  
  public String soramdom;
  
  public String time;
  
  public static ArrayList<SoiCau> soicau = new ArrayList<>();
  
  public SoiCau(String name, String tong, String time) {
    this.ketqua = name;
    this.soramdom = tong;
    this.time = time;
  }
  
  public static void clear() {
    soicau = new ArrayList<>();
  }
}
