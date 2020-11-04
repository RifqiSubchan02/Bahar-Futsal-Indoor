/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

/**
 *
 * @author Windows
 */
public class Lapangan {
    private String kd_lapangan, tipe, harga, gambar;
    
    public String getKd_lapangan(){
        return kd_lapangan;
    }    
    public void setKd_lapangan(String kd_lapangan){
        this.kd_lapangan = kd_lapangan;
    }
    
    public String getTipe(){
        return tipe;
    }
    public void setTipe(String tipe){
        this.tipe = tipe;
    }
    
    public String getHarga(){
        return harga;
    }
    public void setHarga(String harga){
        this.harga = harga;
    }
    
    public String getGambar(){
        return gambar;
    }
    public void setGambar(String gambar){
        this.gambar = gambar;
    }
}
