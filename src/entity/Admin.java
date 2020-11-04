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
public class Admin {
    private static String id, password, nama, j_kelamin, tgl_lahir, email, no_telp, alamat, gambar;
    
    public static String getID(){
        return id;
    }
    public static void setID(String id){
        Admin.id = id;
    }
    
    public static String getPassword(){
        return password;
    }
    public static void setPassword(String password){
        Admin.password = password;
    }
    
    public static String getNama(){
        return nama;
    }
    public static void setNama(String nama){
        Admin.nama = nama;
    }
    
    public static String getJ_Kelamin(){
        return j_kelamin;
    }
    public static void setJ_Kelamin(String j_kelamin){
        Admin.j_kelamin = j_kelamin;
    }
    
    public static String getTgl_Lahir(){
        return tgl_lahir;
    }
    public static void setTgl_Lahir(String tgl_lahir){
        Admin.tgl_lahir = tgl_lahir;
    }
    
    public static String getEmail(){
        return email;
    }
    public static void setEmail(String email){
        Admin.email = email;
    }
    
    public static String getNo_Telp(){
        return no_telp;
    }
    public static void setNo_Telp(String no_telp){
        Admin.no_telp = no_telp;
    }
    
    public static String getAlamat(){
        return alamat;
    }
    public static void setAlamat(String alamat){
        Admin.alamat = alamat;
    }
    
    public static String getGambar(){
        return gambar;
    }
    public static void setGambar(String gambar){
        Admin.gambar = gambar;
    }
}
