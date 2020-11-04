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
public class Pemesanan {
    private String no_faktur, kd_lapangan, tgl_sewa, jam_sewa, nama_tim, email, nama_admin, harga;
    
    public String getNo_Faktur(){
        return no_faktur;
    }
    public void setNo_Faktur(String no_faktur){
        this.no_faktur = no_faktur;
    }
    
    public String getKd_Lapangan(){
        return kd_lapangan;
    }
    public void setKd_Lapangan(String kd_lapangan){
        this.kd_lapangan = kd_lapangan;
    }
    
    public String getTgl_Sewa(){
        return tgl_sewa;
    }
    public void setTgl_Sewa(String tgl_sewa){
        this.tgl_sewa = tgl_sewa;
    }
    
    public String getJam_Sewa(){
        return jam_sewa;
    }
    public void setJam_Sewa(String jam_sewa){
        this.jam_sewa = jam_sewa;
    }
    
    public String getNama_Tim(){
        return nama_tim;
    }
    public void setNama_Tim(String nama_tim){
        this.nama_tim = nama_tim;
    }
    
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    
    public String getNama_Admin(){
        return nama_admin;
    }
    public void setNama_Admin(String nama_admin){
        this.nama_admin = nama_admin;
    }
    
    public String getHarga(){
        return harga;
    }
    public void setHarga(String harga){
        this.harga = harga;
    }
}
