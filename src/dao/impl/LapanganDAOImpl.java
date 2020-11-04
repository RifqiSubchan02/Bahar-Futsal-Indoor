/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.impl;

import dao.LapanganDAO;
import entity.Lapangan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Windows
 */
public class LapanganDAOImpl implements LapanganDAO{
    private Connection connection;
    public LapanganDAOImpl(Connection connection){
        this.connection = connection;
    }
    
    @Override
    public void save(Lapangan lapangan){
        String sql = "insert into lapangan(kd_lapangan,tipe,harga,gambar) values(?,?,?,?)";
        try{
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, lapangan.getKd_lapangan());
            statement.setString(2, lapangan.getTipe());
            statement.setString(3, lapangan.getHarga());
            statement.setString(4, lapangan.getGambar());
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null,"Data Berhasil Disimpan");
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,"Gagal menyimpan data \n"+e);
        }
    }
    
    @Override
    public void update(Lapangan lapangan){
        String sql = "update lapangan set tipe=?, harga=?, gambar=? where kd_lapangan=?";
        try{
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, lapangan.getTipe());
            statement.setString(2, lapangan.getHarga());
            statement.setString(3, lapangan.getGambar());
            statement.setString(4, lapangan.getKd_lapangan());
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data berhasil diubah");
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Data gagal diubah \n"+e);
        }
    }
    
    @Override
    public void delete(Lapangan lapangan){
        String sql = "delete from lapangan where kd_lapangan=?";
        try{
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, lapangan.getKd_lapangan());
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null,"Data berhasil dihapus");
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,"Gagal hapus data \n"+e);
        }
    }
    
    @Override
    public List<Lapangan> getAll(){
        String sql = "select kd_lapangan, tipe, harga, gambar from lapangan";
        List<Lapangan> list = new ArrayList();
        Lapangan lapangan = null;
        try{
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            
            while(rs.next()){
                lapangan = new Lapangan();
                lapangan.setKd_lapangan(rs.getString(1));
                lapangan.setTipe(rs.getString(2));
                lapangan.setHarga(rs.getString(3));
                lapangan.setGambar(rs.getString(4));
                list.add(lapangan);
                lapangan = null;
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Gagal load data lapangan \n"+e);
        }
        return list;
    }
}
