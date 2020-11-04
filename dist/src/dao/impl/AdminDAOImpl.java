/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.impl;

import dao.AdminDAO;
import entity.Admin;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.swing.JOptionPane;
import tampilan.FrameUtama;
import tampilan.Login;

/**
 *
 * @author Windows
 */
public class AdminDAOImpl implements AdminDAO{
    private Connection connection;
    public AdminDAOImpl(Connection connection){
        this.connection = connection;
    }

    @Override
    public void save(Admin admin) {
        try{
            String sql1 = "insert into admin(id,password,nama,j_kelamin,tgl_lahir,email,no_telp,alamat,gambar) values(?,?,?,?,?,?,?,?,?)";
            PreparedStatement statement1 = connection.prepareStatement(sql1);
            statement1.setString(1, admin.getID());
            statement1.setString(2, admin.getPassword());
            statement1.setString(3, admin.getNama());
            statement1.setString(4, admin.getJ_Kelamin());
            statement1.setString(5, admin.getTgl_Lahir());
            statement1.setString(6, admin.getEmail());
            statement1.setString(7, admin.getNo_Telp());
            statement1.setString(8, admin.getAlamat());
            statement1.setString(9, admin.getGambar());
            statement1.executeUpdate();
            
            String sql2= "alter table admin_absensi add "+admin.getNama()+" varchar(30) not null default 'Absen'";
            PreparedStatement statement2 = connection.prepareStatement(sql2);
            statement2.executeUpdate();
            JOptionPane.showMessageDialog(null, "Pendaftaran Berhasil");
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Pendaftaran Gagal \n"+e);
        }
    }

    @Override
    public void delete(Admin admin) {
        String sql = "delete from admin where id=?";
        try{
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, admin.getID());
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null,"Akun anda berhasil dihapus");
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,"Akun anda gagal dihapus \n"+e);
        }
    }

    @Override
    public List<Admin> getAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
