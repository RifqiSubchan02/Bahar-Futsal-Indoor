/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package koneksi;

import dao.AdminDAO;
import dao.LapanganDAO;
import dao.impl.AdminDAOImpl;
import dao.impl.LapanganDAOImpl;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Windows
 */
public class Koneksi {
    private static Connection koneksi;
    private static AdminDAO adminDAO;
    private static LapanganDAO lapanganDAO;
    public static Connection getConnect(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            try{
                String url = "jdbc:mysql://localhost/futsal";
                koneksi = DriverManager.getConnection(url,"root","");
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, e, "Database tidak ditemukan", 0);
            }
        }catch(ClassNotFoundException e){
            JOptionPane.showMessageDialog(null, e, "Driver tidak terhubung", 0);
        }
        return koneksi;
    }
    
    public static AdminDAO getAdminDAO(){
        if(adminDAO==null){
            adminDAO = new AdminDAOImpl(getConnect());
        }
        return adminDAO;
    }
    
    public static LapanganDAO getLapanganDAO(){
        if(lapanganDAO==null){
            lapanganDAO = new LapanganDAOImpl(getConnect());
        }
        return lapanganDAO;
    }
}