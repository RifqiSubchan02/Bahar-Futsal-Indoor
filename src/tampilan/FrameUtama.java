/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tampilan;

import entity.Admin;
import entity.Lapangan;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import koneksi.Koneksi;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;


/**
 *
 * @author Windows
 */
public class FrameUtama extends javax.swing.JFrame {

    /**
     * Creates new form FrameUtama
     */
    private final Connection connection = Koneksi.getConnect();
    private TableModelLapangan tabmodelap;
    private List<Lapangan> loadDataLapangan;
    private DefaultTableModel tabmodepemesanan,tabmodediskon,tabmodepemesanandetail_pelunasan,tabmodedashboard,tabmodemember,tabmodedetailmember,tabmodedatamember,tabmodeupdatemember,tabmodeupdatedetailmember;
    private Image imageLapangan, imageAdmin, imageLogo;
    private Lapangan lapangan;
    private File file, fileOld, fileNew, fileAdmin, fileLogo;
    private static final String HOST = "smtp.gmail.com";
    private static final int PORT = 587;
    private static final boolean SSL_FLAG = true;
    private static final String userName = "baharfutsalindoor@gmail.com"; //Alamat email yang valid
    private static final String password = "bahar_futsal10"; //Password email anda
    private static final String fromAddress = userName;
    private final int hargasiang = 75000;
    
    public FrameUtama() {
        initComponents();
        /*---General---*/
        try{
            String path = new File(".").getCanonicalPath();
            fileLogo = new File(path+"\\src\\img\\Logo.jpeg");
            imageLogo = ImageIO.read(fileLogo);
        }catch(IOException e){
            System.err.println();
        }
        panelGambarLogo.setImage(imageLogo);
        datechooserPesan();
        cbkd_lap();
        cbkd_diskon();
        
        /*---Dashboard---*/
        TableModelDashboard();
        
        
        /*---Lapangan---*/
        tabmodelap = new TableModelLapangan();
        TabelLapangan_LapanganPanel.setModel(tabmodelap);
        loadDataLapangan = new ArrayList();
        loadDataLapangan();
        autoKodeLapangan();
        
        /*---Admin---*/
        attributesAdmin();
        
        /*---Member---*/
        cartModelMember();
        TableModelIDMember();
        cartUpdateModelMember();
        
        /*---Pesan---*/
        autoNoFaktur();
        cartModelPemesanan();
        
        /*---Pelunasan---*/
        TabelModelPemesananDetail_Pelunasan();
        
        /*---Diskon---*/
        TabelModelDiskon();
        autoKodeDiskon();
    }
    
    /*-----General-----*/
    private void datechooserPesan(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        cbtanggalpesan_PemesananPanel.setDateFormat(sdf);
        cbtglmulaisewa_MemberPanel.setDateFormat(sdf);
        cbtglmulaisewa_updateMemberPanel.setDateFormat(sdf);
        cbtgldashboardsewa_DashboardPanel.setDateFormat(sdf);
    }
    
    private void cbkd_lap(){
        String sql = "select kd_lapangan from lapangan";
        List<String> list = new ArrayList();
        try{
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                list.add(rs.getString(WIDTH));
            }
            cbkdlappesan_PemesananPanel.setModel(new javax.swing.DefaultComboBoxModel<>(list.toArray(new String[0])));
            cbkdlap_MemberPanel.setModel(new javax.swing.DefaultComboBoxModel<>(list.toArray(new String[0])));
            cbkdlap_updateMemberPanel.setModel(new javax.swing.DefaultComboBoxModel<>(list.toArray(new String[0])));
        }catch(SQLException e){
            
        }
    }
    
    private void cbkd_diskon(){
        String sql = "select kd_diskon from diskon";
        List<String> list = new ArrayList();
        String none = "--Pilih--";
        list.add(none);
        try{
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                list.add(rs.getString(WIDTH));
            }
            list.remove(1);
            cbkddiskon_PemesananPanel.setModel(new javax.swing.DefaultComboBoxModel<>(list.toArray(new String[0])));
        }catch(SQLException e){
            
        }
    }
    
    /*-----Method Dashboard-----*/
    private void TableModelDashboard(){
        String sql = "select kd_lapangan from lapangan";
        List<String> list = new ArrayList();
        try{
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            list.add("Jam / Kd_Lap");
            while(rs.next()){
                list.add(rs.getString(WIDTH));
            }
            Object[] Baris = list.toArray();
            tabmodedashboard = new DefaultTableModel(null,Baris);
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e);
        }
        TabelDashboard_DashboardPanel.setModel(tabmodedashboard);
        
        String []jam = {"8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
        String []row = {""};
        for(int i=0;i<16;i++){
            tabmodedashboard.addRow(row);
            tabmodedashboard.setValueAt(jam[i], i, 0);
        }
        
        String tanggal = cbtgldashboardsewa_DashboardPanel.getText();
        ArrayList Kdlap = new ArrayList();
        ArrayList Jam_sewa = new ArrayList();
        ArrayList Nama_tim = new ArrayList();
        String sql_datasewa = "select kd_lapangan, jam_sewa, nama_tim from pemesanan_detail where tgl_sewa='"+tanggal+"'";
        try{
            PreparedStatement statement = connection.prepareStatement(sql_datasewa);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                String kdlap = null;
                String jam_sewa = null;
                String nama_tim = null;
                kdlap = rs.getString(1).substring(2);
                jam_sewa = rs.getString(2);
                nama_tim = rs.getString(3);
                
                int jam_kalkulasi = Integer.parseInt(jam_sewa);
                jam_kalkulasi = jam_kalkulasi-8;
                jam_sewa = String.valueOf(jam_kalkulasi);
                
                Kdlap.add(kdlap);
                Jam_sewa.add(jam_sewa);
                Nama_tim.add(nama_tim);
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e);
        }
        for(int i=0;i<Kdlap.size();i++){
            int a = Integer.parseInt(Jam_sewa.get(i).toString());
            int b = Integer.parseInt(Kdlap.get(i).toString());
            tabmodedashboard.setValueAt(Nama_tim.get(i), a, b);
        }
    }
    
    /*-----Method Admin-----*/
    private void attributesAdmin(){
        try{
            String path = new File(".").getCanonicalPath();
            fileAdmin = new File(path+"\\image\\admin\\"+Admin.getID()+"\\"+Admin.getGambar());
            imageAdmin = ImageIO.read(fileAdmin);
        }catch(IOException e){
            
        }
        panelGambarAdmin.setImage(imageAdmin);
        txtnamaadmin_AkunPanel.setText(Admin.getNama());
        Id_label.setText(Admin.getID());
        jk_label.setText(Admin.getJ_Kelamin());
        tgllahir_label.setText(Admin.getTgl_Lahir());
        email_label.setText(Admin.getEmail());
        notelp_label.setText(Admin.getNo_Telp());
        alamat_label.setText(Admin.getAlamat());
    }
    
    private void updatePhoto(){
        fileNew = null;
        JFileChooser chooser = new JFileChooser(System.getProperty("user.home"));
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setFileFilter(new FileNameExtensionFilter("jpg|png|bmp", "jpg", "png", "bmp"));
        
        if(chooser.showOpenDialog(this)==JFileChooser.APPROVE_OPTION){
            fileNew = chooser.getSelectedFile();
            try{
                imageAdmin = ImageIO.read(fileNew);
                panelGambarAdmin.setImage(imageAdmin);
            }catch(IOException ex){
                Logger.getLogger(FrameUtama.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if(fileNew!=null){
            try{
                String path = new File(".").getCanonicalPath();
                fileOld = new File(path+"\\image\\admin\\"+Admin.getID()+"\\"+Admin.getGambar());
                fileOld.delete();
                FileUtils.copyFileToDirectory(fileNew, new File(path+"\\image\\admin\\"+Admin.getID()));
                String sql = "update admin set gambar=? where id='"+Admin.getID()+"'";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, fileNew.getName());
                statement.executeUpdate();
            }catch(IOException e){
                Logger.getLogger(FrameUtama.class.getName()).log(Level.SEVERE, null, e);
                JOptionPane.showMessageDialog(null, "Data Gagal Dikopi"+e);
            }catch(SQLException e2){
                JOptionPane.showMessageDialog(null, e2);
            }
        }
    }
    
    /*-----Method Lapangan-----*/
    private class TableModelLapangan extends AbstractTableModel{
        private List<Lapangan> list = new ArrayList();
        
        public void setData(List<Lapangan> list){
            this.list = list;
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return list.size();
        }

        @Override
        public int getColumnCount() {
            return 4;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch(columnIndex){
                case 0 : return list.get(rowIndex).getKd_lapangan();
                case 1 : return list.get(rowIndex).getTipe();
                case 2 : return list.get(rowIndex).getHarga();
                case 3 : return list.get(rowIndex).getGambar();
                default : return null;
            }
        }
        
        @Override
        public String getColumnName(int columnIndex) {
            switch(columnIndex){
                case 0 : return "Kode Lapangan";
                case 1 : return "Tipe";
                case 2 : return "Harga";
                case 3 : return "Gambar";
                default: return null;
            }
        }
        
        public void insertTable(Lapangan lapangan){
            list.add(lapangan);
            fireTableDataChanged();
        }
        
        public void updateTable(Lapangan lapangan, int rowIndex){
            list.set(rowIndex, lapangan);
            fireTableDataChanged();
        }
        
        public Lapangan get(int rowIndex){
            return list.get(rowIndex);
        }   
    }
    
    private void loadDataLapangan(){
        loadDataLapangan = Koneksi.getLapanganDAO().getAll();
        tabmodelap.setData(loadDataLapangan);
    }
    
    private void autoKodeLapangan(){
        String sql = "select max(right(kd_lapangan,2)) from lapangan";
        try{
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()){
                if(rs.first()==false){
                    txtkdlap_LapanganPanel.setText("L-01");
                }else{
                    rs.last();
                    int auto = rs.getInt(1)+1;
                    String nomor = String.valueOf(auto);
                    int noLong = nomor.length();
                    for(int a = 0;a<2-noLong;a++){
                        nomor = "0"+nomor;
                    }
                    txtkdlap_LapanganPanel.setText("L-"+nomor);
                }
            }
        }catch(SQLException e){
            Logger.getLogger(FrameUtama.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    private void clearLapangan(){
        cbtipe_LapanganPanel.setSelectedIndex(0);
        txthargalap_LapanganPanel.setText("");
        txtgambar_LapanganPanel.setText("");
        panelGambarLap.setImage(null);
        file = null;
        fileNew = null;
        fileOld = null;
    }
    
    private void JFileChooser(){
        JFileChooser chooser = new JFileChooser(System.getProperty("user.home"));
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setFileFilter(new FileNameExtensionFilter("jpg|png|bmp", "jpg", "png", "bmp"));
        
        if(chooser.showOpenDialog(this)==JFileChooser.APPROVE_OPTION){
            fileNew = chooser.getSelectedFile();
            try{
                imageLapangan = ImageIO.read(fileNew);
                panelGambarLap.setImage(imageLapangan);
            }catch(IOException ex){
                Logger.getLogger(FrameUtama.class.getName()).log(Level.SEVERE, null, ex);
            }
            txtgambar_LapanganPanel.setText(fileNew.getName());
        }
    }
    
    private void SaveLapangan(){
        try{
            String path = new File(".").getCanonicalPath();
            FileUtils.copyFileToDirectory(fileNew, new File(path+"\\image\\lapangan"));
        }catch(IOException e){
            Logger.getLogger(FrameUtama.class.getName()).log(Level.SEVERE, null, e);
        }
        lapangan = new Lapangan();
        lapangan.setKd_lapangan(txtkdlap_LapanganPanel.getText());
        lapangan.setTipe(cbtipe_LapanganPanel.getSelectedItem().toString());
        lapangan.setHarga(txthargalap_LapanganPanel.getText());
        lapangan.setGambar(fileNew.getName());
        Koneksi.getLapanganDAO().save(lapangan);
    }
    
    private void UpdateLapangan(){
        lapangan = new Lapangan();
        lapangan.setKd_lapangan(txtkdlap_LapanganPanel.getText());
        lapangan.setTipe(cbtipe_LapanganPanel.getSelectedItem().toString());
        lapangan.setHarga(txthargalap_LapanganPanel.getText());
        lapangan.setGambar(txtgambar_LapanganPanel.getText());
        Koneksi.getLapanganDAO().update(lapangan);
        int row = TabelLapangan_LapanganPanel.getSelectedRow();
        if(fileNew!=null){
            try{
                String path = new File(".").getCanonicalPath();
                fileOld = new File(path+"\\image\\lapangan\\"+tabmodelap.getValueAt(row, 3).toString());
                fileOld.delete();
                FileUtils.copyFileToDirectory(fileNew, new File(path+"\\image\\lapangan"));            
            }catch(IOException e){
                Logger.getLogger(FrameUtama.class.getName()).log(Level.SEVERE, null, e);
                JOptionPane.showMessageDialog(null, "Data Gagal Dikopi"+e);
            }
        }
    }
    
    private void DeleteLapangan(){
        lapangan = new Lapangan();
        lapangan.setKd_lapangan(txtkdlap_LapanganPanel.getText());
        lapangan.setGambar(txtgambar_LapanganPanel.getText());
        Koneksi.getLapanganDAO().delete(lapangan);
        try{
            String path = new File(".").getCanonicalPath();
            fileOld = new File(path+"\\image\\lapangan\\"+txtgambar_LapanganPanel.getText());
            fileOld.delete();
        }catch(IOException e){
            JOptionPane.showMessageDialog(null, "Gagal"+e);
        }
        
        String sql = "delete * from pemesanan_detail where kd_lapangan=?";
        try{
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, txtkdlap_LapanganPanel.getText());
            statement.executeUpdate();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    private void ClickTabelLapangan(){
        int row = TabelLapangan_LapanganPanel.getSelectedRow();
        String a = tabmodelap.getValueAt(row, 0).toString();
        String b = tabmodelap.getValueAt(row, 1).toString();
        String c = tabmodelap.getValueAt(row, 2).toString();
        String d = tabmodelap.getValueAt(row, 3).toString();
        
        txtkdlap_LapanganPanel.setText(a);
        cbtipe_LapanganPanel.setSelectedItem(b);
        txthargalap_LapanganPanel.setText(c);
        txtgambar_LapanganPanel.setText(d);        
        try{
            String path = new File(".").getCanonicalPath();
            file = new File(path+"\\image\\lapangan\\"+d);
            imageLapangan = ImageIO.read(file);
        }catch(IOException e){
            JOptionPane.showMessageDialog(null,"Gambar tidak ada \n"+e);
            panelGambarLap.setImage(null);
        }
        panelGambarLap.setImage(imageLapangan);
    }
    
    /*----Method Member----*/    
    protected void TableModelIDMember(){
        Object[] Baris = {"ID","Nama_tim","Kapten","Email","No. Hp","Periode","Biaya"};
        tabmodedatamember = new DefaultTableModel(null,Baris);
        datamembertabel_MemberPanel.setModel(tabmodedatamember);
        String sql = "select id,nama_tim,kapten,email,no_hp,periode,biaya from member";
        try{
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()){
                String a = rs.getString("id");
                String b = rs.getString("nama_tim");
                String c = rs.getString("kapten");
                String d = rs.getString("email");
                String e = rs.getString("no_hp");
                String f = rs.getString("periode");
                String g = rs.getString("biaya");
                String[]data = {a,b,c,d,e,f.substring(0, 7),g};
                tabmodedatamember.addRow(data);
            }
        }catch(SQLException e){
            
        }
    }
    
    private void cariIDMember(){
        Object[] Baris = {"ID","Nama_tim","Kapten","Email","No. Hp","Periode","Biaya"};
        tabmodedatamember = new DefaultTableModel(null,Baris);
        datamembertabel_MemberPanel.setModel(tabmodedatamember);
        String sql = "select id,nama_tim,kapten,email,no_hp,periode,biaya from member where id='"+txtcari_MemberPanel.getText()+"' or nama_tim='"+txtcari_MemberPanel.getText()+"'";
        try{
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()){
                String a = rs.getString("id");
                String b = rs.getString("nama_tim");
                String c = rs.getString("kapten");
                String d = rs.getString("email");
                String e = rs.getString("no_hp");
                String f = rs.getString("periode");
                String g = rs.getString("biaya");
                String[]data = {a,b,c,d,e,f.substring(0, 7),g};
                tabmodedatamember.addRow(data);
            }
        }catch(SQLException e){
            
        }
    }
    
    protected void cartModelMember(){
        Object[] Baris = {"Tgl_mulai","Tgl_selesai","Kd_lap","Jam_sewa"};
        tabmodemember = new DefaultTableModel(null,Baris);
    }
    
    private void addCartMember(){
        cartMember_MemberPanel.setModel(tabmodemember);
        String a = cbtglmulaisewa_MemberPanel.getText();
        int angka = Integer.parseInt(a.substring(0,2));
        for(int i=angka;i<=31;i+=7){
            angka = i;
        }
        String b = String.valueOf(angka)+a.substring(2,10);
        String c = cbkdlap_MemberPanel.getSelectedItem().toString();
        String d = cbjamsewa_MemberPanel.getSelectedItem().toString();
        String[]data = {a,b,c,d};
        tabmodemember.addRow(data);
    }
    
    protected void cartUpdateModelMember(){
        Object[] Baris = {"Tgl_mulai","Tgl_selesai","Kd_lap","Jam_sewa"};
        tabmodeupdatemember = new DefaultTableModel(null,Baris);
    }
    
    private void addCartUpdateMember(){
        cartMember_updateMemberPanel.setModel(tabmodeupdatemember);
        String a = cbtglmulaisewa_updateMemberPanel.getText();
        int angka = Integer.parseInt(a.substring(0,2));
        for(int i=angka;i<=31;i+=7){
            angka = i;
        }
        String b = String.valueOf(angka)+a.substring(2,10);
        String c = cbkdlap_updateMemberPanel.getSelectedItem().toString();
        String d = cbjamsewa_updateMemberPanel.getSelectedItem().toString();
        String[]data = {a,b,c,d};
        tabmodeupdatemember.addRow(data);
    }
    
    private void cekDetailMember(){
        Object[] Baris = {"Tgl_sewa","Kd_lap","Jam_sewa"};
        tabmodedetailmember = new DefaultTableModel(null,Baris);
        cartDetailMember_MemberPanel.setModel(tabmodedetailmember);
                
        for(int i=0;i<cartMember_MemberPanel.getRowCount();i++){
            String a = "";
            String b = "";
            String c = "";
            a = cartMember_MemberPanel.getValueAt(i, 0).toString();
            b = cartMember_MemberPanel.getValueAt(i, 2).toString();
            c = cartMember_MemberPanel.getValueAt(i, 3).toString();
            int angka = Integer.parseInt(a.substring(8,10));
            for(int j=angka;j<=31;j+=7){
                if(j<10){
                    a = cartMember_MemberPanel.getValueAt(i, 0).toString().substring(0,8)+"0"+String.valueOf(j);
                }else{
                    a = cartMember_MemberPanel.getValueAt(i, 0).toString().substring(0,8)+String.valueOf(j);
                }
                String[]data = {a,b,c};
                
                String kdlap=null;
                String tgl=null;
                String jam=null;
                String sql = "select kd_lapangan,tgl_sewa,jam_sewa from pemesanan_detail where kd_lapangan='"+data[1]+"' and tgl_sewa='"+data[0]+"' and jam_sewa='"+data[2]+"'";
                try{
                    PreparedStatement statement = connection.prepareStatement(sql);
                    ResultSet rs = statement.executeQuery();
                    while(rs.next()){
                        kdlap = rs.getString(1);
                        tgl = rs.getString(2);
                        jam = rs.getString(3);
                    }
                }catch(SQLException e){
                    
                }
        
                if(kdlap==null && tgl==null && jam==null){
                    if(tgl!=null){
                        if(tgl.equalsIgnoreCase(a)){
                            tabmodedetailmember.addRow(data);
                            cartMember_MemberPanel.setValueAt(a, i, 1);
                        }
                    }else{
                        tabmodedetailmember.addRow(data);
                        cartMember_MemberPanel.setValueAt(a, i, 1);
                    }
                }
                data = null;
                int jumlah = 0;
                String dp = String.valueOf(jumlah);
                txtdpmember_MemberPanel.setText(dp);
            }
        }
        
        if(cartDetailMember_MemberPanel.getRowCount()!=0){
            int jumlah = 0;
            int harga = 0;
            int diskon = 0;
            String sql1 = "select harga_diskon from diskon where kd_diskon='D-01'";
            try{
                PreparedStatement statement1 = connection.prepareStatement(sql1);
                ResultSet rs1 = statement1.executeQuery(sql1);
                while(rs1.next()){
                    diskon = rs1.getInt(1);
                }
            }catch(SQLException e){
                
            }
            for(int i=0;i<cartDetailMember_MemberPanel.getRowCount();i++){
                String sql2 = "select harga from lapangan where kd_lapangan='"+cartDetailMember_MemberPanel.getValueAt(i, 1)+"'";
                try{
                    PreparedStatement statement2 = connection.prepareStatement(sql2);
                    ResultSet rs2 = statement2.executeQuery(sql2);
                    while(rs2.next()){
                        harga = rs2.getInt(1);
                    }
                }catch(SQLException e){
                    
                }
                int jam = Integer.parseInt(cartDetailMember_MemberPanel.getValueAt(i, 2).toString());
                if(jam<16){
                    harga = hargasiang;
                }
                jumlah+=harga-diskon;
            }
            txtdpmember_MemberPanel.setText(String.valueOf(jumlah));
        }
    }
    
    private void cekUpdateDetailMember(){
        Object[] Baris = {"Tgl_sewa","Kd_lap","Jam_sewa"};
        tabmodeupdatedetailmember = new DefaultTableModel(null,Baris);
        cartDetailMember_updateMemberPanel.setModel(tabmodeupdatedetailmember);
                
        for(int i=0;i<cartMember_updateMemberPanel.getRowCount();i++){
            String a = "";
            String b = "";
            String c = "";
            a = cartMember_updateMemberPanel.getValueAt(i, 0).toString();
            b = cartMember_updateMemberPanel.getValueAt(i, 2).toString();
            c = cartMember_updateMemberPanel.getValueAt(i, 3).toString();
            int angka = Integer.parseInt(a.substring(8,10));
            for(int j=angka;j<=31;j+=7){
                if(j<10){
                    a = cartMember_updateMemberPanel.getValueAt(i, 0).toString().substring(0,8)+"0"+String.valueOf(j);
                }else{
                    a = cartMember_updateMemberPanel.getValueAt(i, 0).toString().substring(0,8)+String.valueOf(j);
                }
                String[]data = {a,b,c};
                
                String kdlap=null;
                String tgl=null;
                String jam=null;
                String sql = "select kd_lapangan,tgl_sewa,jam_sewa from pemesanan_detail where kd_lapangan='"+data[1]+"' and tgl_sewa='"+data[0]+"' and jam_sewa='"+data[2]+"'";
                try{
                    PreparedStatement statement = connection.prepareStatement(sql);
                    ResultSet rs = statement.executeQuery();
                    while(rs.next()){
                        kdlap = rs.getString(1);
                        tgl = rs.getString(2);
                        jam = rs.getString(3);
                    }
                }catch(SQLException e){
                    
                }
        
                if(kdlap==null && tgl==null && jam==null){
                    if(tgl!=null){
                        if(tgl.equalsIgnoreCase(a)){
                            tabmodeupdatedetailmember.addRow(data);
                            cartMember_updateMemberPanel.setValueAt(a, i, 1);
                        }
                    }else{
                        tabmodeupdatedetailmember.addRow(data);
                        cartMember_updateMemberPanel.setValueAt(a, i, 1);
                    }
                }
                data = null;
                int jumlah = 0;
                jumlah = 20000*cartDetailMember_updateMemberPanel.getRowCount();
                String dp = String.valueOf(jumlah);
                txtdpmember_updateMemberPanel.setText(dp);
            }
        }
        
        if(cartDetailMember_updateMemberPanel.getRowCount()!=0){
            int jumlah = 0;
            int harga = 0;
            int diskon = 0;
            String sql1 = "select harga_diskon from diskon where kd_diskon='D-01'";
            try{
                PreparedStatement statement1 = connection.prepareStatement(sql1);
                ResultSet rs1 = statement1.executeQuery(sql1);
                while(rs1.next()){
                    diskon = rs1.getInt(1);
                }
            }catch(SQLException e){
                
            }
            for(int i=0;i<cartDetailMember_updateMemberPanel.getRowCount();i++){
                String sql2 = "select harga from lapangan where kd_lapangan='"+cartDetailMember_updateMemberPanel.getValueAt(i, 1)+"'";
                try{
                    PreparedStatement statement2 = connection.prepareStatement(sql2);
                    ResultSet rs2 = statement2.executeQuery(sql2);
                    while(rs2.next()){
                        harga = rs2.getInt(1);
                    }
                }catch(SQLException e){
                    
                }
                int jam = Integer.parseInt(cartDetailMember_updateMemberPanel.getValueAt(i, 2).toString());
                if(jam<16){
                    harga = hargasiang;
                }
                jumlah+=harga-diskon;
            }
            txtdpmember_updateMemberPanel.setText(String.valueOf(jumlah));
        }
    }
    
    private void deleteCartMember(){
        int row = cartMember_MemberPanel.getSelectedRow();
        tabmodemember.removeRow(row);
    }
    
    private void deleteUpdateCartMember(){
        int row = cartMember_updateMemberPanel.getSelectedRow();
        tabmodeupdatemember.removeRow(row);
    }
    
    private void bersihMember(){
        txtnamatimmember_MemberPanel.setText("");
        txtkapten_MemberPanel.setText("");
        txtemail_MemberPanel.setText("");
        txtnohp_MemberPanel.setText("");
        cbkdlap_MemberPanel.setSelectedIndex(0);
        cbjamsewa_MemberPanel.setSelectedIndex(0);
        int baris1 = cartMember_MemberPanel.getRowCount();
        for(int i=0;i<baris1;i++){
            tabmodemember.removeRow(0);
        }
        int baris2 = cartDetailMember_MemberPanel.getRowCount();
        for(int i=0;i<baris2;i++){
            tabmodedetailmember.removeRow(0);
        }
        txtdpmember_MemberPanel.setText("0");
        
        btnsavemember_MemberPanel.setEnabled(false);
        btndeleteCart_MemberPanel.setEnabled(false);
    }
    
    private void bersihUpdateMember(){
        txtnamatimmember_updateMemberPanel.setText("");
        txtkapten_updateMemberPanel.setText("");
        txtemail_updateMemberPanel.setText("");
        txtnohp_updateMemberPanel.setText("");
        cbkdlap_updateMemberPanel.setSelectedIndex(0);
        cbjamsewa_updateMemberPanel.setSelectedIndex(0);
        int baris1 = cartMember_updateMemberPanel.getRowCount();
        for(int i=0;i<baris1;i++){
            tabmodeupdatemember.removeRow(0);
        }
        int baris2 = cartDetailMember_updateMemberPanel.getRowCount();
        for(int i=0;i<baris2;i++){
            tabmodeupdatedetailmember.removeRow(0);
        }
        txtdpmember_updateMemberPanel.setText("0");
        
        btnupdateMember_updateMemberPanel.setEnabled(false);
        btndeleteCart_updateMemberPanel.setEnabled(false);
    }
    
    private void saveMember(){
        try{
            String idmember = "M-";
            for(int i=0;i<3;i++){
                int a = (int) (10*Math.random());
                idmember+= String.valueOf(a);
            }
            
            String nofak = "BFI";
            for(int i=0;i<=4;i++){
                int a = (int) (10*Math.random());
                nofak+= String.valueOf(a);
            }
            
            String periode = "";
            int per = Integer.parseInt(cbtglmulaisewa_MemberPanel.getText().substring(5,7));
            switch(per){
                case 1 :
                    periode = "Januari";
                    break;
                case 2 :
                    periode = "Februari";
                    break;
                case 3 :
                    periode = "Maret";
                    break;
                case 4 :
                    periode = "April";
                    break;
                case 5 :
                    periode = "Mei";
                    break;
                case 6 :
                    periode = "Juni";
                    break;
                case 7 :
                    periode = "Juli";
                    break;
                case 8 :
                    periode = "Agustus";
                    break;
                case 9 :
                    periode = "September";
                    break;
                case 10 :
                    periode = "Oktober";
                    break;
                case 11 :
                    periode = "November";
                    break;
                case 12 :
                    periode = "Desember";
                    break;
            }
            
            periode += " "+cartMember_MemberPanel.getValueAt(0, 0).toString().substring(0,4);
            
            String toAddress = txtemail_MemberPanel.getText();
            String subject = "Struk Pemesanan Member";
            String headMessage = "===PROFIL MEMBER=== \nNo. Faktur : "+nofak+"\nPeriode : "+periode+"\nID Member : "+idmember+"\nNama Tim : "+txtnamatimmember_MemberPanel.getText()+"\nKapten : "+txtkapten_MemberPanel.getText()+"\nEmail : "+txtemail_MemberPanel.getText()+"\nNo. Hp : "+txtnohp_MemberPanel.getText()+"\nBiaya : "+txtdpmember_MemberPanel.getText()+"\n\n";
            String bodyMessage1 = "===VARIABEL WAKTU MEMBER=== \n";
            int no1 = 0;
            for(int i=0;i<cartMember_MemberPanel.getRowCount();i++){
                no1 = 1+i;
                bodyMessage1 += no1+".Tanggal Mulai : "+cartMember_MemberPanel.getValueAt(i, 0).toString()+"\n   Tanggal Selesai : "+cartMember_MemberPanel.getValueAt(i, 1).toString()+"\n   Kode Lapangan : "+cartMember_MemberPanel.getValueAt(i, 2).toString()+"\n   Jam Sewa : "+cartMember_MemberPanel.getValueAt(i, 3).toString()+"\n";
            }
            String bodyMessage2 = "===JADWAL MEMBER=== \n";
            int no2 = 0;
            for(int j=0;j<cartDetailMember_MemberPanel.getRowCount();j++){
                no2 = 1+j;
                bodyMessage2 += no2+".Tanggal Sewa : "+cartDetailMember_MemberPanel.getValueAt(j, 0).toString()+"\n   Kode Lapangan : "+cartDetailMember_MemberPanel.getValueAt(j, 1).toString()+"\n   Jam Sewa : "+cartDetailMember_MemberPanel.getValueAt(j, 2).toString()+"\n";
            }
            String footMessage = "Harap simpan struk ini sebagai bukti Member.";
            String fullMessage = headMessage+bodyMessage1+"\n"+bodyMessage2+"\n"+footMessage;
        
            Email email = new SimpleEmail();
            email.setHostName(HOST);
            email.setSmtpPort(PORT);
            email.setAuthenticator(new DefaultAuthenticator(userName,password));
            email.setSSLOnConnect(SSL_FLAG);
            email.setFrom(fromAddress);
            email.setSubject(subject);
            email.setMsg(fullMessage);
            email.addTo(toAddress);
            email.send();
            
            
            String sqlmember = "insert into member values(?,?,?,?,?,?,?)";
            PreparedStatement statement1 = connection.prepareStatement(sqlmember);
            statement1.setString(1, idmember);
            statement1.setString(2, txtnamatimmember_MemberPanel.getText());
            statement1.setString(3, txtkapten_MemberPanel.getText());
            statement1.setString(4, txtemail_MemberPanel.getText());
            statement1.setString(5, txtnohp_MemberPanel.getText());
            statement1.setString(6, cbtglmulaisewa_MemberPanel.getText());
            statement1.setString(7, txtdpmember_MemberPanel.getText());
            statement1.executeUpdate();
            
            String sql_detailmember = "insert into member_detail values(?,?,?,?,?)";
            for(int i=0;i<cartMember_MemberPanel.getRowCount();i++){
                PreparedStatement statement2 = connection.prepareStatement(sql_detailmember);
                statement2.setString(1, idmember);
                statement2.setString(2, cartMember_MemberPanel.getValueAt(i, 0).toString());
                statement2.setString(3, cartMember_MemberPanel.getValueAt(i, 1).toString());
                statement2.setString(4, cartMember_MemberPanel.getValueAt(i, 2).toString());
                statement2.setString(5, cartMember_MemberPanel.getValueAt(i, 3).toString());
                statement2.executeUpdate();
            }
            
            int dis = 0;
            String sql_diskon = "select harga_diskon from diskon where kd_diskon='D-01'";
            Statement statementdis = connection.prepareStatement(sql_diskon);
            ResultSet rsdis = statementdis.executeQuery(sql_diskon);
            while(rsdis.next()){
                dis = rsdis.getInt(1);
            }
            
            int harga = 0;
                      
            String sql_pemesanan = "insert into pemesanan_detail values(?,?,?,?,?,?,?,?,?,?)";
            for(int j=0;j<cartDetailMember_MemberPanel.getRowCount();j++){
                String sqlharga = "select harga from lapangan where kd_lapangan='"+cartDetailMember_MemberPanel.getValueAt(j, 1).toString()+"'";
                Statement statement3 = connection.prepareStatement(sqlharga);
                ResultSet rs = statement3.executeQuery(sqlharga);
                while(rs.next()){
                    harga = rs.getInt(1);
                }
                
                int jam = Integer.parseInt(cartDetailMember_MemberPanel.getValueAt(j, 2).toString());
                if(jam<16){
                    harga = hargasiang;
                }
                
                int kalkulasiharga = harga-dis;
                String kh = String.valueOf(kalkulasiharga);
                
                PreparedStatement statement4 = connection.prepareStatement(sql_pemesanan);
                statement4.setString(1, nofak);
                statement4.setString(2, cartDetailMember_MemberPanel.getValueAt(j, 1).toString());
                statement4.setString(3, cartDetailMember_MemberPanel.getValueAt(j, 0).toString());
                statement4.setString(4, cartDetailMember_MemberPanel.getValueAt(j, 2).toString());
                statement4.setString(5, txtnamatimmember_MemberPanel.getText());
                statement4.setString(6, txtemail_MemberPanel.getText());
                statement4.setString(7, Admin.getNama());
                statement4.setString(8, kh);
                statement4.setString(9, kh);
                statement4.setString(10, "Member");
                statement4.executeUpdate();
            }
            
            String sql_datasewa = "insert into data_sewa(no_faktur, tanggal_pemesanan, tanggal_pelunasan, id_member, id_admin, kd_diskon) values(?, current_timestamp(), current_timestamp(),?,?,?)";
            PreparedStatement statement5 = connection.prepareStatement(sql_datasewa);
            statement5.setString(1, nofak);
            statement5.setString(2, idmember);
            statement5.setString(3, Admin.getID());
            statement5.setString(4, "D-01");
            statement5.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Struk Pemesanan Member sudah terkirim.");
            NewMemberForm.setVisible(false);
        }catch(EmailException | SQLException e1){
            JOptionPane.showMessageDialog(null, e1);
        }
    }
    
    private void updateMember(){
        try{
            String nofak = "BFI";
            for(int i=0;i<=4;i++){
                int a = (int) (10*Math.random());
                nofak+= String.valueOf(a);
            }
            
            String periode = "";
            int per = Integer.parseInt(cbtglmulaisewa_updateMemberPanel.getText().substring(5,7));
            switch(per){
                case 1 :
                    periode = "Januari";
                    break;
                case 2 :
                    periode = "Februari";
                    break;
                case 3 :
                    periode = "Maret";
                    break;
                case 4 :
                    periode = "April";
                    break;
                case 5 :
                    periode = "Mei";
                    break;
                case 6 :
                    periode = "Juni";
                    break;
                case 7 :
                    periode = "Juli";
                    break;
                case 8 :
                    periode = "Agustus";
                    break;
                case 9 :
                    periode = "September";
                    break;
                case 10 :
                    periode = "Oktober";
                    break;
                case 11 :
                    periode = "November";
                    break;
                case 12 :
                    periode = "Desember";
                    break;
            }
            
            periode += " "+cartMember_updateMemberPanel.getValueAt(0, 0).toString().substring(0,4);
            
            String toAddress = txtemail_updateMemberPanel.getText();
            String subject = "Struk Update Pemesanan Member";
            String headMessage = "===PROFIL MEMBER=== \nNo. Faktur : "+nofak+"\nPeriode : "+periode+"\nID Member : "+txtidmember_updateMemberPanel.getText()+"\nNama Tim : "+txtnamatimmember_updateMemberPanel.getText()+"\nKapten : "+txtkapten_updateMemberPanel.getText()+"\nEmail : "+txtemail_updateMemberPanel.getText()+"\nNo. Hp : "+txtnohp_updateMemberPanel.getText()+"\n Biaya : "+txtdpmember_updateMemberPanel.getText()+"\n\n";
            String bodyMessage1 = "===VARIABEL WAKTU MEMBER=== \n";
            int no1 = 0;
            for(int i=0;i<cartMember_updateMemberPanel.getRowCount();i++){
                no1 = 1+i;
                bodyMessage1 += no1+".Tanggal Mulai : "+cartMember_updateMemberPanel.getValueAt(i, 0).toString()+"\n   Tanggal Selesai : "+cartMember_updateMemberPanel.getValueAt(i, 1).toString()+"\n   Kode Lapangan : "+cartMember_updateMemberPanel.getValueAt(i, 2).toString()+"\n   Jam Sewa : "+cartMember_updateMemberPanel.getValueAt(i, 3).toString()+"\n";
            }
            String bodyMessage2 = "===JADWAL MEMBER=== \n";
            int no2 = 0;
            for(int j=0;j<cartDetailMember_updateMemberPanel.getRowCount();j++){
                no2 = 1+j;
                bodyMessage2 += no2+".Tanggal Sewa : "+cartDetailMember_updateMemberPanel.getValueAt(j, 0).toString()+"\n   Kode Lapangan : "+cartDetailMember_updateMemberPanel.getValueAt(j, 1).toString()+"\n   Jam Sewa : "+cartDetailMember_updateMemberPanel.getValueAt(j, 2).toString()+"\n";
            }
            String footMessage = "Harap simpan struk ini sebagai bukti Member.";
            String fullMessage = headMessage+bodyMessage1+"\n"+bodyMessage2+"\n"+footMessage;
        
            Email email = new SimpleEmail();
            email.setHostName(HOST);
            email.setSmtpPort(PORT);
            email.setAuthenticator(new DefaultAuthenticator(userName,password));
            email.setSSLOnConnect(SSL_FLAG);
            email.setFrom(fromAddress);
            email.setSubject(subject);
            email.setMsg(fullMessage);
            email.addTo(toAddress);
            email.send();
            
            
            String sqlmember = "update member set nama_tim=?, kapten=?, email=?, no_hp=?, periode=?, biaya=? where id=?";
            PreparedStatement statement1 = connection.prepareStatement(sqlmember);
            statement1.setString(1, txtnamatimmember_updateMemberPanel.getText());
            statement1.setString(2, txtkapten_updateMemberPanel.getText());
            statement1.setString(3, txtemail_updateMemberPanel.getText());
            statement1.setString(4, txtnohp_updateMemberPanel.getText());
            statement1.setString(5, cbtglmulaisewa_updateMemberPanel.getText());
            statement1.setString(6, txtdpmember_updateMemberPanel.getText());
            statement1.setString(7, txtidmember_updateMemberPanel.getText());
            statement1.executeUpdate();
            
            String sql_detailmember = "insert into member_detail values(?,?,?,?,?)";
            for(int i=0;i<cartMember_updateMemberPanel.getRowCount();i++){
                PreparedStatement statement2 = connection.prepareStatement(sql_detailmember);
                statement2.setString(1, txtidmember_updateMemberPanel.getText());
                statement2.setString(2, cartMember_updateMemberPanel.getValueAt(i, 0).toString());
                statement2.setString(3, cartMember_updateMemberPanel.getValueAt(i, 1).toString());
                statement2.setString(4, cartMember_updateMemberPanel.getValueAt(i, 2).toString());
                statement2.setString(5, cartMember_updateMemberPanel.getValueAt(i, 3).toString());
                statement2.executeUpdate();
            }
            
            int dis = 0;
            String sql_diskon = "select harga_diskon from diskon where kd_diskon='D-01'";
            Statement statementdis = connection.prepareStatement(sql_diskon);
            ResultSet rsdis = statementdis.executeQuery(sql_diskon);
            while(rsdis.next()){
                dis = rsdis.getInt(1);
            }
            
            int harga = 0;
                      
            String sql_pemesanan = "insert into pemesanan_detail values(?,?,?,?,?,?,?,?,?,?)";
            for(int j=0;j<cartDetailMember_updateMemberPanel.getRowCount();j++){
                String sqlharga = "select harga from lapangan where kd_lapangan='"+cartDetailMember_updateMemberPanel.getValueAt(j, 1)+"'";
                Statement statement3 = connection.prepareStatement(sqlharga);
                ResultSet rs = statement3.executeQuery(sqlharga);
                while(rs.next()){
                    harga = rs.getInt(1);
                }
                
                int jam = Integer.parseInt(cartDetailMember_updateMemberPanel.getValueAt(j, 2).toString());
                if(jam<16){
                    harga = hargasiang;
                }
                
                int kalkulasiharga = harga-dis;
                String kh = String.valueOf(kalkulasiharga);
                
                PreparedStatement statement4 = connection.prepareStatement(sql_pemesanan);
                statement4.setString(1, nofak);
                statement4.setString(2, cartDetailMember_updateMemberPanel.getValueAt(j, 1).toString());
                statement4.setString(3, cartDetailMember_updateMemberPanel.getValueAt(j, 0).toString());
                statement4.setString(4, cartDetailMember_updateMemberPanel.getValueAt(j, 2).toString());
                statement4.setString(5, txtnamatimmember_updateMemberPanel.getText());
                statement4.setString(6, txtemail_updateMemberPanel.getText());
                statement4.setString(7, Admin.getNama());
                statement4.setString(8, kh);
                statement4.setString(9, kh);
                statement4.setString(10, "Member");
                statement4.executeUpdate();
            }
            
            String sql_datasewa = "insert into data_sewa(no_faktur, tanggal_pemesanan, tanggal_pelunasan, id_member, id_admin, kd_diskon) values(?, current_timestamp(), current_timestamp(),?,?,?)";
            PreparedStatement statement5 = connection.prepareStatement(sql_datasewa);
            statement5.setString(1, nofak);
            statement5.setString(2, txtidmember_updateMemberPanel.getText());
            statement5.setString(3, Admin.getID());
            statement5.setString(4, "D-01");
            statement5.executeUpdate();
            
            txtidmember_updateMemberPanel.setText("");
            JOptionPane.showMessageDialog(null, "Struk Update Kartu Member sudah terkirim.");
            UpdateMemberForm.setVisible(false);
        }catch(EmailException | SQLException e1){
            JOptionPane.showMessageDialog(null, e1);
        }
    }
    
    /*----Method Pesan----*/
    
    private void autoNoFaktur(){
        String nofak = "";
        for(int i=0;i<=4;i++){
            int a = (int) (10*Math.random());
            nofak+= String.valueOf(a);
        }
        txtnofakturpesan_PemesananPanel.setText("BFI"+nofak);
    }
    
    protected void cartModelPemesanan(){
        Object[] Baris = {"Kd_lap","Tgl_sewa","Jam_sewa","Harga","DP"};
        tabmodepemesanan = new DefaultTableModel(null,Baris);
    }
    
    private void totalPemesanan(){
        int jml = TabelCartPesan_PemesananPanel.getRowCount();
        int tot = 20000*jml;
        String total = String.valueOf(tot);
        txttotalpemesanan.setText(total);
    }
    
    private void addCartPemesanan(){
        int harga= 0;
        try{
            String sql = "select harga from lapangan where kd_lapangan='"+cbkdlappesan_PemesananPanel.getSelectedItem().toString()+"'";
            Statement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()){
                harga = rs.getInt(1);
            }
            
            int jam = Integer.parseInt(cbjamsewapesan_PemesananPanel.getSelectedItem().toString());
            if(jam<16){
                harga = hargasiang;
            }
            TabelCartPesan_PemesananPanel.setModel(tabmodepemesanan);
            String a = cbkdlappesan_PemesananPanel.getSelectedItem().toString();
            String b = cbtanggalpesan_PemesananPanel.getText();
            String c = cbjamsewapesan_PemesananPanel.getSelectedItem().toString();
            String d = String.valueOf(harga);
            String e = "20000";
            String[] data = {a,b,c,d,e};
            tabmodepemesanan.addRow(data);
            data = null;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e);
        }
        
    }
    
    private void deleteCart(){
        int row = TabelCartPesan_PemesananPanel.getSelectedRow();
        tabmodepemesanan.removeRow(row);
        if(TabelCartPesan_PemesananPanel.getRowCount()==0){
            cbtanggalpesan_PemesananPanel.setEnabled(true);
        }else{
            cbtanggalpesan_PemesananPanel.setEnabled(false);
        }
    }
    
    private void savePemesanan(){
        String toAddress = txtemailpesan_PemesananPanel.getText();
        String subject = "Struk Pemesanan Lapangan";
        String headMessage = "No.Faktur : "+txtnofakturpesan_PemesananPanel.getText()+"\nNama Tim : "+txtnamatimpesan_PemesananPanel.getText()+"\nAdmin : "+Admin.getNama()+"\n\n";
        String footMessage = "Harap simpan struk ini sebagai bukti transaksi.";
        String bodyMessage = null;
        String fullMessage = null;
        if(1<=TabelCartPesan_PemesananPanel.getRowCount()){
            bodyMessage = "Tanggal Sewa : "+TabelCartPesan_PemesananPanel.getValueAt(0, 1)+"\nKode Lapangan : "+TabelCartPesan_PemesananPanel.getValueAt(0, 0)+"\nJam Sewa : "+TabelCartPesan_PemesananPanel.getValueAt(0, 2)+"\nHarga : "+TabelCartPesan_PemesananPanel.getValueAt(0, 3)+"\nDP : Rp.20000 \n\n";
            if(2<=TabelCartPesan_PemesananPanel.getRowCount()){
                bodyMessage += "Tanggal Sewa : "+TabelCartPesan_PemesananPanel.getValueAt(1, 1)+"\nKode Lapangan : "+TabelCartPesan_PemesananPanel.getValueAt(1, 0)+"\nJam Sewa : "+TabelCartPesan_PemesananPanel.getValueAt(1, 2)+"\nHarga : "+TabelCartPesan_PemesananPanel.getValueAt(1, 3)+"\nDP : Rp.20000 \n\n";
                if(3<=TabelCartPesan_PemesananPanel.getRowCount()){
                    bodyMessage += "Tanggal Sewa : "+TabelCartPesan_PemesananPanel.getValueAt(2, 1)+"\nKode Lapangan : "+TabelCartPesan_PemesananPanel.getValueAt(2, 0)+"\nJam Sewa : "+TabelCartPesan_PemesananPanel.getValueAt(2, 2)+"\nHarga : "+TabelCartPesan_PemesananPanel.getValueAt(2, 3)+"\nDP : Rp.20000 \n\n";
                    if(4<=TabelCartPesan_PemesananPanel.getRowCount()){
                        bodyMessage += "Tanggal Sewa : "+TabelCartPesan_PemesananPanel.getValueAt(3, 1)+"\nKode Lapangan : "+TabelCartPesan_PemesananPanel.getValueAt(3, 0)+"\nJam Sewa : "+TabelCartPesan_PemesananPanel.getValueAt(3, 2)+"\nHarga : "+TabelCartPesan_PemesananPanel.getValueAt(3, 3)+"\nDP : Rp.20000 \n\n";
                    }
                }
            }
        }
        
        fullMessage = headMessage+bodyMessage+footMessage;
        
        /*---------------------------------------------------------*/
        String nofaktur = txtnofakturpesan_PemesananPanel.getText();
        String namatim = txtnamatimpesan_PemesananPanel.getText();
        String email2 = txtemailpesan_PemesananPanel.getText();
        String id_admin = Admin.getID();
        String nama_admin = Admin.getNama();
        String kd_diskon = null;
        if(cbkddiskon_PemesananPanel.getSelectedItem().toString()=="--Pilih--"){
            kd_diskon = "-";
        }else{
            kd_diskon = cbkddiskon_PemesananPanel.getSelectedItem().toString();
        }
        String sql_pesan = "insert into data_sewa(no_faktur, tanggal_pemesanan, id_admin, kd_diskon) values(?, current_timestamp(),?,?)";
        /*---------------------------------------------------------*/
        
        try{
            Email email = new SimpleEmail();
            email.setHostName(HOST);
            email.setSmtpPort(PORT);
            email.setAuthenticator(new DefaultAuthenticator(userName,password));
            email.setSSLOnConnect(SSL_FLAG);
            email.setFrom(fromAddress);
            email.setSubject(subject);
            email.setMsg(fullMessage);
            email.addTo(toAddress);
            email.send();
            
            try{
                PreparedStatement statement = connection.prepareStatement(sql_pesan);
                statement.setString(1, nofaktur);
                statement.setString(2, id_admin);
                statement.setString(3, kd_diskon);
                statement.executeUpdate();
            }catch(SQLException e1){
                JOptionPane.showMessageDialog(null, e1);
            }
            
            int rowcount = TabelCartPesan_PemesananPanel.getRowCount();
            String sql_detailpesan = "insert into pemesanan_detail values(?,?,?,?,?,?,?,?,?,?)";
            for(int x=0;x<rowcount;x++){
                String a = TabelCartPesan_PemesananPanel.getValueAt(x, 0).toString();
                String b = TabelCartPesan_PemesananPanel.getValueAt(x, 1).toString();
                String c = TabelCartPesan_PemesananPanel.getValueAt(x, 2).toString();
                String d = TabelCartPesan_PemesananPanel.getValueAt(x, 3).toString();
                String e = TabelCartPesan_PemesananPanel.getValueAt(x, 4).toString();
                String f = "Belum lunas";
                try{
                    PreparedStatement statement = connection.prepareStatement(sql_detailpesan);
                    statement.setString(1, nofaktur);
                    statement.setString(2, a);
                    statement.setString(3, b);
                    statement.setString(4, c);
                    statement.setString(5, namatim);
                    statement.setString(6, email2);
                    statement.setString(7, nama_admin);
                    statement.setString(8, d);
                    statement.setString(9, e);
                    statement.setString(10, f);
                    statement.executeUpdate();
                }catch(SQLException e2){
                    JOptionPane.showMessageDialog(null, e2);
                }
            }
            JOptionPane.showMessageDialog(null, "Struk pemesanan sudah terkirim ke alamat email pemesan");
            PemesananCartDialog.setVisible(false);
        }catch(EmailException e){
            JOptionPane.showMessageDialog(null, "Email tidak valid \n"+e);
            System.err.println(e);
        }
        
        
    }
    
    private void clearPemesanan(){
        cbkdlappesan_PemesananPanel.setSelectedIndex(0);
        cbjamsewapesan_PemesananPanel.setSelectedIndex(0);
        txtnamatimpesan_PemesananPanel.setText("");
        txtemailpesan_PemesananPanel.setText("");
        int baris = TabelCartPesan_PemesananPanel.getRowCount();
        for(int i=0;i<baris;i++){
            tabmodepemesanan.removeRow(0);
        }
        txttotalpemesanan.setText("0");
    }
    
    /*----Method Pelunasan----*/
    protected void TabelModelPemesananDetail_Pelunasan(){
        Object[] Baris = {"Kd_lap","Tgl_sewa","Jam_sewa","Harga","DP","Keterangan"};
        tabmodepemesanandetail_pelunasan = new DefaultTableModel(null,Baris);
    }
    
    private void cariPelunasan(){
        int rowcount = TabelDetailPemesanan_PelunasanPanel.getRowCount();
        if(rowcount!=0){
            for(int i=0;i<rowcount;i++){
                tabmodepemesanandetail_pelunasan.removeRow(0);
            }
        }
        
        String a=null,b=null,c=null,d=null,e=null,f=null;
        String sql1 = "select kd_lapangan,tgl_sewa,jam_sewa,harga,dp,keterangan from pemesanan_detail where (nama_tim='"+txtcari_PelunasanPanel.getText()+"' or no_faktur='"+txtcari_PelunasanPanel.getText()+"') and keterangan='Belum lunas'";
        try{
            Statement statement1 = connection.prepareStatement(sql1);
            ResultSet rs1 = statement1.executeQuery(sql1);
            while(rs1.next()){
                a = rs1.getString(1);
                b = rs1.getString(2);
                c = rs1.getString(3);
                d = rs1.getString(4);
                e = rs1.getString(5);
                f = rs1.getString(6);
                TabelDetailPemesanan_PelunasanPanel.setModel(tabmodepemesanandetail_pelunasan);
                String[]data = {a,b,c,d,e,f};
                tabmodepemesanandetail_pelunasan.addRow(data);
                data = null;
            }
        }catch(SQLException e1){
            JOptionPane.showMessageDialog(null, e1);
        }
    }
    
    private void prosesPelunasan(){
        try{
            String emailtext=null;
            String sql_cariemail = "select email from pemesanan_detail where no_faktur='"+txtnofaktur_PelunasanPanel.getText()+"'";
            try{
                Statement statement = connection.prepareStatement(sql_cariemail);
                ResultSet rs = statement.executeQuery(sql_cariemail);
                while(rs.next()){
                    emailtext = rs.getString(1);
                }
            }catch(SQLException f){
                JOptionPane.showMessageDialog(null, f);
            }
            
            int row = TabelDetailPemesanan_PelunasanPanel.getSelectedRow();
            String toAddress = emailtext;
            String subject = "Struk Pelunasan Lapangan";
            String headMessage = "No.Faktur : "+txtcari_PelunasanPanel.getText()+"\nNama Tim : "+txtnamatim_PelunasanPanel.getText()+"\nAdmin : "+Admin.getNama()+"\n\n";
            String bodyMessage = "Tanggal Sewa : "+TabelDetailPemesanan_PelunasanPanel.getValueAt(row, 1)+"\nKode Lapangan : "+TabelDetailPemesanan_PelunasanPanel.getValueAt(row, 0)+"\nJam Sewa : "+TabelDetailPemesanan_PelunasanPanel.getValueAt(row, 2)+"\nSisa Bayar : Rp."+txtsisabayar_PelunasanPanel.getText()+"\nDiskon : Rp."+txtdiskon_PelunasanPanel.getText()+"\nTotal Bayar : Rp."+txttotal_PelunasanPanel.getText()+"\n\n";
            String footMessage = "Harap simpan struk ini sebagai bukti pelunasan.";
            String fullMessage = headMessage+bodyMessage+footMessage;
        
            Email email = new SimpleEmail();
            email.setHostName(HOST);
            email.setSmtpPort(PORT);
            email.setAuthenticator(new DefaultAuthenticator(userName,password));
            email.setSSLOnConnect(SSL_FLAG);
            email.setFrom(fromAddress);
            email.setSubject(subject);
            email.setMsg(fullMessage);
            email.addTo(toAddress);
            email.send();
            
            String sql_datasewa = "update data_sewa set tanggal_pelunasan=curtime()";
            try{
                PreparedStatement statement = connection.prepareStatement(sql_datasewa);
                statement.executeUpdate();
            }catch(SQLException e1){
                JOptionPane.showMessageDialog(null, e1);
            }
            
            String ket = "Lunas";
            String sql_pemesanandetail = "update pemesanan_detail set keterangan=? where no_faktur=? and kd_lapangan=? and tgl_sewa=? and jam_sewa=?";
            try{
                PreparedStatement statement = connection.prepareStatement(sql_pemesanandetail);
                statement.setString(1, ket);
                statement.setString(2, txtcari_PelunasanPanel.getText());
                statement.setString(3, TabelDetailPemesanan_PelunasanPanel.getValueAt(row, 0).toString());
                statement.setString(4, TabelDetailPemesanan_PelunasanPanel.getValueAt(row, 1).toString());
                statement.setString(5, TabelDetailPemesanan_PelunasanPanel.getValueAt(row, 2).toString());
                statement.executeUpdate();
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, e);
            }
        
            String sql_pelunasandetail = "insert into pelunasan_detail values(?,?,?,?,?,?,?,?,?,?,?,?)";
            try{
                PreparedStatement statement = connection.prepareStatement(sql_pelunasandetail);
                statement.setString(1, txtcari_PelunasanPanel.getText());
                statement.setString(2, TabelDetailPemesanan_PelunasanPanel.getValueAt(row, 0).toString());
                statement.setString(3, TabelDetailPemesanan_PelunasanPanel.getValueAt(row, 1).toString());
                statement.setString(4, TabelDetailPemesanan_PelunasanPanel.getValueAt(row, 2).toString());
                statement.setString(5, txtnamatim_PelunasanPanel.getText());
                statement.setString(6, emailtext);
                statement.setString(7, Admin.getNama());
                statement.setString(8, TabelDetailPemesanan_PelunasanPanel.getValueAt(row, 3).toString());
                statement.setString(9, TabelDetailPemesanan_PelunasanPanel.getValueAt(row, 4).toString());
                statement.setString(10, txtsisabayar_PelunasanPanel.getText());
                statement.setString(11, txtdiskon_PelunasanPanel.getText());
                statement.setString(12, txttotal_PelunasanPanel.getText());
                statement.executeUpdate();
            }catch(SQLException g){
                JOptionPane.showMessageDialog(null, g);
            }
            JOptionPane.showMessageDialog(null, "Struk Pelunasan sudah terkirim");
            txtnofaktur_PelunasanPanel.setText("");
            txtnamatim_PelunasanPanel.setText("");
            txtsisabayar_PelunasanPanel.setText("");
            txtdiskon_PelunasanPanel.setText("");
            txttotal_PelunasanPanel.setText("");
        }catch(EmailException e){
            JOptionPane.showMessageDialog(null, "Pelunasan gagal");
        }
    }
    
    private void ClickTabelPemesananDetail_Pelunasan(){
        int row = TabelDetailPemesanan_PelunasanPanel.getSelectedRow();
        String a=null,b=null,c=null,d=null;
        String sql1 = "select no_faktur,nama_tim from pemesanan_detail where kd_lapangan='"+TabelDetailPemesanan_PelunasanPanel.getValueAt(row, 0).toString()+"' and tgl_sewa='"+TabelDetailPemesanan_PelunasanPanel.getValueAt(row, 1).toString()+"' and jam_sewa='"+TabelDetailPemesanan_PelunasanPanel.getValueAt(row, 2).toString()+"'";
        try{
            Statement statement1 = connection.prepareStatement(sql1);
            ResultSet rs1 = statement1.executeQuery(sql1);
            while(rs1.next()){
                a = rs1.getString(1);
                b = rs1.getString(2);
            }
        }catch(SQLException e){
            
        }
        txtnofaktur_PelunasanPanel.setText(a);
        txtnamatim_PelunasanPanel.setText(b);
        int harga = Integer.parseInt(TabelDetailPemesanan_PelunasanPanel.getValueAt(row, 3).toString());
        int dp = Integer.parseInt(TabelDetailPemesanan_PelunasanPanel.getValueAt(row, 4).toString());
        int sisa = harga-dp;
        txtsisabayar_PelunasanPanel.setText(String.valueOf(sisa));
        
        String sql2 = "select kd_diskon from data_sewa where no_faktur='"+a+"'";
        try{
            Statement statement2 = connection.prepareStatement(sql2);
            ResultSet rs2 = statement2.executeQuery(sql2);
            while(rs2.next()){
                c = rs2.getString(1);
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e);
        }
        
        String sql3 = "select harga_diskon from diskon where kd_diskon='"+c+"'";
        try{
            Statement statement3 = connection.prepareStatement(sql3);
            ResultSet rs3 = statement3.executeQuery(sql3);
            while(rs3.next()){
                d = rs3.getString(1);
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e);
        }
        if(d==null){
            d="0";
        }
        txtdiskon_PelunasanPanel.setText(d);
        int total = sisa - Integer.parseInt(d);
        txttotal_PelunasanPanel.setText(String.valueOf(total));
    }
    
    private void clearPelunasan(){
        txtcari_PelunasanPanel.setText("BFI");
        txtnofaktur_PelunasanPanel.setText("");
        txtnamatim_PelunasanPanel.setText("");
        txtsisabayar_PelunasanPanel.setText("");
        txtdiskon_PelunasanPanel.setText("");
        txttotal_PelunasanPanel.setText("");
        int rowcount = TabelDetailPemesanan_PelunasanPanel.getRowCount();
        if(rowcount!=0){
            for(int i=0;i<rowcount;i++){
                tabmodepemesanandetail_pelunasan.removeRow(0);
            }
        }
    }
    
    /*----Method Diskon----*/    
    protected void TabelModelDiskon(){
        Object[] Baris = {"Kode Diskon","Harga Diskon","Keterangan"};
        tabmodediskon = new DefaultTableModel(null,Baris);
        TabelDiskon_DiskonPanel.setModel(tabmodediskon);
        String sql = "select * from diskon";
        try{
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()){
                String a = rs.getString("kd_diskon");
                String b = rs.getString("harga_diskon");
                String c = rs.getString("keterangan");
                String[]data = {a,b,c};
                tabmodediskon.addRow(data);
            }
        }catch(SQLException e){
            
        }
    }
    
    private void ClickTabelDiskon(){
        int row = TabelDiskon_DiskonPanel.getSelectedRow();
        String a = tabmodediskon.getValueAt(row, 0).toString();
        String b = tabmodediskon.getValueAt(row, 1).toString();
        String c = tabmodediskon.getValueAt(row, 2).toString();
        
        txtkddiskon_DiskonPanel.setText(a);
        txthargadiskon_DiskonPanel.setText(b);
        txtketerangan_DiskonPanel.setText(c);
        btnSaveDiskon_DiskonPanel.setEnabled(false);
        btnUpdateDiskon_DiskonPanel.setEnabled(true);
        btnDeleteDiskon_DiskonPanel.setEnabled(true);
    }
    
    private void autoKodeDiskon(){
        String sql = "select max(right(kd_diskon,2)) from diskon";
        try{
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()){
                if(rs.first()==false){
                    txtkddiskon_DiskonPanel.setText("D-01");
                }else{
                    rs.last();
                    int auto = rs.getInt(1)+1;
                    String nomor = String.valueOf(auto);
                    int noLong = nomor.length();
                    for(int a = 0;a<2-noLong;a++){
                        nomor = "0"+nomor;
                    }
                    txtkddiskon_DiskonPanel.setText("D-"+nomor);
                }
            }
        }catch(SQLException e){
            Logger.getLogger(FrameUtama.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    private void saveDiskon(){
        String sql = "insert into diskon values(?,?,?)";
        try{
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, txtkddiskon_DiskonPanel.getText());
            statement.setString(2, txthargadiskon_DiskonPanel.getText());
            statement.setString(3, txtketerangan_DiskonPanel.getText());
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null,"Data Berhasil Disimpan");
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,"Gagal menyimpan data \n"+e);
        }
    }
    
    private void updateDiskon(){
        String sql = "update diskon set harga_diskon=?, keterangan=? where kd_diskon=?";
        try{
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, txthargadiskon_DiskonPanel.getText());
            statement.setString(2, txtketerangan_DiskonPanel.getText());
            statement.setString(3, txtkddiskon_DiskonPanel.getText());
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data berhasil diubah");
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Data gagal diubah \n"+e);
        }
    }
    
    private void deleteDiskon(){
        String sql = "delete from diskon where kd_diskon=?";
        try{
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, txtkddiskon_DiskonPanel.getText());
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null,"Data berhasil dihapus");
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,"Gagal hapus data \n"+e);
        }
    }
    
    private void clearDiskon(){
        txtkddiskon_DiskonPanel.setText("");
        txthargadiskon_DiskonPanel.setText("");
        txtketerangan_DiskonPanel.setText("");
        btnSaveDiskon_DiskonPanel.setEnabled(true);
        btnUpdateDiskon_DiskonPanel.setEnabled(false);
        btnDeleteDiskon_DiskonPanel.setEnabled(false);
    }
    
    private void previewReport(String filename, HashMap parameter, String jenis){
        try{
            File fileReport = new File("src\\report\\"+filename);
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(fileReport);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, connection);
            
            JasperExportManager.exportReportToPdfFile(jasperPrint, "C:\\"+jenis+".pdf");
            JOptionPane.showMessageDialog(null, "Laporan sudah disimpan pada direktori C:\\"+jenis+".pdf");
        }catch(JRException e){
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        PemesananCartDialog = new javax.swing.JDialog();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TabelCartPesan_PemesananPanel = new javax.swing.JTable();
        btnDeletefromCartPemesanan_PemesananPanel = new javax.swing.JButton();
        btnCheckout_PemesananPanel = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        txttotalpemesanan = new javax.swing.JTextField();
        NewMemberForm = new javax.swing.JDialog();
        jPanel3 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        txtnamatimmember_MemberPanel = new javax.swing.JTextField();
        txtkapten_MemberPanel = new javax.swing.JTextField();
        txtemail_MemberPanel = new javax.swing.JTextField();
        txtnohp_MemberPanel = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        cbkdlap_MemberPanel = new javax.swing.JComboBox<>();
        cbtglmulaisewa_MemberPanel = new datechooser.beans.DateChooserCombo();
        cbjamsewa_MemberPanel = new javax.swing.JComboBox<>();
        btnaddCart_MemberPanel = new javax.swing.JButton();
        btndeleteCart_MemberPanel = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        cartMember_MemberPanel = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        cartDetailMember_MemberPanel = new javax.swing.JTable();
        txtdpmember_MemberPanel = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        btnbersih_MemberPanel = new javax.swing.JButton();
        btnsavemember_MemberPanel = new javax.swing.JButton();
        PengaturanLabel16 = new java.awt.Label();
        UpdateMemberForm = new javax.swing.JDialog();
        jPanel11 = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        txtnamatimmember_updateMemberPanel = new javax.swing.JTextField();
        txtkapten_updateMemberPanel = new javax.swing.JTextField();
        txtemail_updateMemberPanel = new javax.swing.JTextField();
        txtnohp_updateMemberPanel = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        txtidmember_updateMemberPanel = new javax.swing.JTextField();
        PengaturanLabel17 = new java.awt.Label();
        jPanel12 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        cbkdlap_updateMemberPanel = new javax.swing.JComboBox<>();
        cbtglmulaisewa_updateMemberPanel = new datechooser.beans.DateChooserCombo();
        cbjamsewa_updateMemberPanel = new javax.swing.JComboBox<>();
        btnaddCart_updateMemberPanel = new javax.swing.JButton();
        btndeleteCart_updateMemberPanel = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        cartMember_updateMemberPanel = new javax.swing.JTable();
        jPanel14 = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        cartDetailMember_updateMemberPanel = new javax.swing.JTable();
        txtdpmember_updateMemberPanel = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        btnupdateMember_updateMemberPanel = new javax.swing.JButton();
        btnbersih_updateMemberPanel = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel15 = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        panelGambarLogo = new tampilan.PanelGambar();
        jPanel2 = new javax.swing.JPanel();
        UtamaPanel = new javax.swing.JPanel();
        DashboardPanel = new javax.swing.JPanel();
        PengaturanLabel13 = new java.awt.Label();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        TabelDashboard_DashboardPanel = new javax.swing.JTable();
        cbtgldashboardsewa_DashboardPanel = new datechooser.beans.DateChooserCombo();
        MemberPanel = new javax.swing.JPanel();
        Member = new java.awt.Label();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        datamembertabel_MemberPanel = new javax.swing.JTable();
        txtcari_MemberPanel = new javax.swing.JTextField();
        btncari_MemberPanel = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        PemesananPanel = new javax.swing.JPanel();
        PengaturanLabel14 = new java.awt.Label();
        jLabel6 = new javax.swing.JLabel();
        txtnofakturpesan_PemesananPanel = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        cbkdlappesan_PemesananPanel = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        cbjamsewapesan_PemesananPanel = new javax.swing.JComboBox<>();
        cbtanggalpesan_PemesananPanel = new datechooser.beans.DateChooserCombo();
        jLabel10 = new javax.swing.JLabel();
        txtnamatimpesan_PemesananPanel = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtemailpesan_PemesananPanel = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        btnAddtoCartPemesanan_PemesananPanel = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        cbkddiskon_PemesananPanel = new javax.swing.JComboBox<>();
        btnBersihPemesanan = new javax.swing.JButton();
        btnCart_PemesananPanel = new javax.swing.JButton();
        PelunasanPanel = new javax.swing.JPanel();
        PengaturanLabel15 = new java.awt.Label();
        jLabel22 = new javax.swing.JLabel();
        txtcari_PelunasanPanel = new javax.swing.JTextField();
        btnCari_PelunasanPanel = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        TabelDetailPemesanan_PelunasanPanel = new javax.swing.JTable();
        jLabel23 = new javax.swing.JLabel();
        txtnamatim_PelunasanPanel = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        txtsisabayar_PelunasanPanel = new javax.swing.JTextField();
        txtdiskon_PelunasanPanel = new javax.swing.JTextField();
        txttotal_PelunasanPanel = new javax.swing.JTextField();
        btnProses_PelunasanPanel = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        txtnofaktur_PelunasanPanel = new javax.swing.JTextField();
        jLabel64 = new javax.swing.JLabel();
        LapanganPanel = new javax.swing.JPanel();
        PengaturanLabel12 = new java.awt.Label();
        btnPilihGambarLapangan_LapanganPanel = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TabelLapangan_LapanganPanel = new javax.swing.JTable();
        btnSimpanLapangan_LapanganPanel = new javax.swing.JButton();
        btnUbahLapangan_LapanganPanel = new javax.swing.JButton();
        btnHapusLapangan_LapanganPanel = new javax.swing.JButton();
        panelGambarLap = new tampilan.PanelGambar();
        cbtipe_LapanganPanel = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtkdlap_LapanganPanel = new javax.swing.JTextField();
        txthargalap_LapanganPanel = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtgambar_LapanganPanel = new javax.swing.JTextField();
        btnBersihLapangan_LapanganPanel = new javax.swing.JButton();
        DiskonPanel = new javax.swing.JPanel();
        DiskonLabel = new java.awt.Label();
        jLabel16 = new javax.swing.JLabel();
        txtkddiskon_DiskonPanel = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txthargadiskon_DiskonPanel = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtketerangan_DiskonPanel = new javax.swing.JTextArea();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        TabelDiskon_DiskonPanel = new javax.swing.JTable();
        btnSaveDiskon_DiskonPanel = new javax.swing.JButton();
        btnUpdateDiskon_DiskonPanel = new javax.swing.JButton();
        btnDeleteDiskon_DiskonPanel = new javax.swing.JButton();
        btnBersih_DiskonPanel = new javax.swing.JButton();
        ProfilPanel = new javax.swing.JPanel();
        PengaturanLabel2 = new java.awt.Label();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        jPanel15 = new javax.swing.JPanel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        Id_label = new javax.swing.JLabel();
        jk_label = new javax.swing.JLabel();
        tgllahir_label = new javax.swing.JLabel();
        email_label = new javax.swing.JLabel();
        notelp_label = new javax.swing.JLabel();
        alamat_label = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        panelGambarAdmin = new tampilan.PanelGambar();
        txtnamaadmin_AkunPanel = new javax.swing.JLabel();
        LaporanPanel = new javax.swing.JPanel();
        DiskonLabel1 = new java.awt.Label();
        jPanel17 = new javax.swing.JPanel();
        jButton10 = new javax.swing.JButton();
        tfrom = new datechooser.beans.DateChooserCombo();
        cblap = new javax.swing.JComboBox<>();
        jLabel61 = new javax.swing.JLabel();
        tto = new datechooser.beans.DateChooserCombo();
        jLabel62 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();

        PemesananCartDialog.setLocation(new java.awt.Point(400, 250));
        PemesananCartDialog.setMinimumSize(new java.awt.Dimension(570, 200));

        TabelCartPesan_PemesananPanel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Kd_Lap", "Tgl_Sewa", "Jam_Sewa", "Harga", "DP"
            }
        ));
        TabelCartPesan_PemesananPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabelCartPesan_PemesananPanelMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(TabelCartPesan_PemesananPanel);

        btnDeletefromCartPemesanan_PemesananPanel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-minus-16.png"))); // NOI18N
        btnDeletefromCartPemesanan_PemesananPanel.setText("Delete from Cart");
        btnDeletefromCartPemesanan_PemesananPanel.setEnabled(false);
        btnDeletefromCartPemesanan_PemesananPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeletefromCartPemesanan_PemesananPanelActionPerformed(evt);
            }
        });

        btnCheckout_PemesananPanel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-save-as-16.png"))); // NOI18N
        btnCheckout_PemesananPanel.setText(">>>");
        btnCheckout_PemesananPanel.setEnabled(false);
        btnCheckout_PemesananPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckout_PemesananPanelActionPerformed(evt);
            }
        });

        jLabel21.setText("Total DP : Rp.");

        txttotalpemesanan.setText("0");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(btnDeletefromCartPemesanan_PemesananPanel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCheckout_PemesananPanel)
                        .addGap(140, 140, 140)
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txttotalpemesanan, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txttotalpemesanan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel21))
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnDeletefromCartPemesanan_PemesananPanel)
                        .addComponent(btnCheckout_PemesananPanel)))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout PemesananCartDialogLayout = new javax.swing.GroupLayout(PemesananCartDialog.getContentPane());
        PemesananCartDialog.getContentPane().setLayout(PemesananCartDialogLayout);
        PemesananCartDialogLayout.setHorizontalGroup(
            PemesananCartDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        PemesananCartDialogLayout.setVerticalGroup(
            PemesananCartDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        NewMemberForm.setLocation(new java.awt.Point(325, 80));
        NewMemberForm.setMinimumSize(new java.awt.Dimension(750, 600));

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Profil Member"));

        jLabel31.setText("Kapten");

        jLabel30.setText("Email");

        jLabel32.setText("No. Hp");

        jLabel36.setText("Nama Tim");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel31)
                    .addComponent(jLabel30)
                    .addComponent(jLabel32)
                    .addComponent(jLabel36))
                .addGap(27, 27, 27)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtkapten_MemberPanel)
                        .addComponent(txtemail_MemberPanel)
                        .addComponent(txtnamatimmember_MemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtnohp_MemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(56, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtnamatimmember_MemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel36))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtkapten_MemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtemail_MemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(txtnohp_MemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Variabel Member"));

        jLabel33.setText("Kode Lapangan");

        jLabel34.setText("Tanggal Mulai Sewa");

        jLabel35.setText("Jam Sewa");

        cbkdlap_MemberPanel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbjamsewa_MemberPanel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));

        btnaddCart_MemberPanel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-plus-16.png"))); // NOI18N
        btnaddCart_MemberPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnaddCart_MemberPanelActionPerformed(evt);
            }
        });

        btndeleteCart_MemberPanel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-minus-16.png"))); // NOI18N
        btndeleteCart_MemberPanel.setEnabled(false);
        btndeleteCart_MemberPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btndeleteCart_MemberPanelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(btnaddCart_MemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btndeleteCart_MemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel33)
                            .addComponent(jLabel34)
                            .addComponent(jLabel35))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbjamsewa_MemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbkdlap_MemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbtglmulaisewa_MemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(36, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(cbkdlap_MemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbtglmulaisewa_MemberPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                    .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel35)
                    .addComponent(cbjamsewa_MemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnaddCart_MemberPanel)
                    .addComponent(btndeleteCart_MemberPanel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Tabel Member"));

        cartMember_MemberPanel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tgl_mulai", "Tgl_selesai", "Kd_lapangan", "Jam_sewa"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        cartMember_MemberPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cartMember_MemberPanelMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(cartMember_MemberPanel);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Tabel Detail Member"));

        cartDetailMember_MemberPanel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tgl_sewa", "Kd_lapangan", "Jam_sewa"
            }
        ));
        jScrollPane7.setViewportView(cartDetailMember_MemberPanel);

        txtdpmember_MemberPanel.setText("0");

        jLabel37.setText("Total DP");

        jLabel38.setText(": Rp.");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel37)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel38)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtdpmember_MemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37)
                    .addComponent(txtdpmember_MemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel38))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        btnbersih_MemberPanel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-refresh-16.png"))); // NOI18N
        btnbersih_MemberPanel.setText("Refresh");
        btnbersih_MemberPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbersih_MemberPanelActionPerformed(evt);
            }
        });

        btnsavemember_MemberPanel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-save-as-16.png"))); // NOI18N
        btnsavemember_MemberPanel.setText("Simpan");
        btnsavemember_MemberPanel.setEnabled(false);
        btnsavemember_MemberPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsavemember_MemberPanelActionPerformed(evt);
            }
        });

        PengaturanLabel16.setBackground(new java.awt.Color(0, 0, 0));
        PengaturanLabel16.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        PengaturanLabel16.setForeground(new java.awt.Color(204, 204, 204));
        PengaturanLabel16.setText("Form New Member");

        javax.swing.GroupLayout NewMemberFormLayout = new javax.swing.GroupLayout(NewMemberForm.getContentPane());
        NewMemberForm.getContentPane().setLayout(NewMemberFormLayout);
        NewMemberFormLayout.setHorizontalGroup(
            NewMemberFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PengaturanLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(NewMemberFormLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(NewMemberFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(NewMemberFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(NewMemberFormLayout.createSequentialGroup()
                        .addComponent(btnsavemember_MemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnbersih_MemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        NewMemberFormLayout.setVerticalGroup(
            NewMemberFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NewMemberFormLayout.createSequentialGroup()
                .addComponent(PengaturanLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(NewMemberFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(NewMemberFormLayout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(NewMemberFormLayout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(NewMemberFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnsavemember_MemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnbersih_MemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(33, Short.MAX_VALUE))
        );

        UpdateMemberForm.setLocation(new java.awt.Point(325, 80));
        UpdateMemberForm.setMinimumSize(new java.awt.Dimension(750, 600));

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder("Profil Member"));

        jLabel39.setText("Nama Tim");

        jLabel40.setText("Kapten");

        jLabel41.setText("No. Hp");

        jLabel42.setText("ID");

        jLabel48.setText("Email");

        txtidmember_updateMemberPanel.setEditable(false);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel39)
                    .addComponent(jLabel40)
                    .addComponent(jLabel41)
                    .addComponent(jLabel42)
                    .addComponent(jLabel48))
                .addGap(27, 27, 27)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtemail_updateMemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtkapten_updateMemberPanel)
                        .addComponent(txtnamatimmember_updateMemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtnohp_updateMemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtidmember_updateMemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(56, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42)
                    .addComponent(txtidmember_updateMemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel39)
                    .addComponent(txtnamatimmember_updateMemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40)
                    .addComponent(txtkapten_updateMemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel48)
                    .addComponent(txtemail_updateMemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(txtnohp_updateMemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        PengaturanLabel17.setBackground(new java.awt.Color(0, 0, 0));
        PengaturanLabel17.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        PengaturanLabel17.setForeground(new java.awt.Color(204, 204, 204));
        PengaturanLabel17.setText("Form Update Member");

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("Variabel Member"));

        jLabel43.setText("Kode Lapangan");

        jLabel44.setText("Tanggal Mulai Sewa");

        jLabel45.setText("Jam Sewa");

        cbkdlap_updateMemberPanel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbjamsewa_updateMemberPanel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));

        btnaddCart_updateMemberPanel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-plus-16.png"))); // NOI18N
        btnaddCart_updateMemberPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnaddCart_updateMemberPanelActionPerformed(evt);
            }
        });

        btndeleteCart_updateMemberPanel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-minus-16.png"))); // NOI18N
        btndeleteCart_updateMemberPanel.setText("-");
        btndeleteCart_updateMemberPanel.setEnabled(false);
        btndeleteCart_updateMemberPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btndeleteCart_updateMemberPanelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(btnaddCart_updateMemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btndeleteCart_updateMemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel43)
                            .addComponent(jLabel44)
                            .addComponent(jLabel45))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbjamsewa_updateMemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbkdlap_updateMemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbtglmulaisewa_updateMemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(36, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43)
                    .addComponent(cbkdlap_updateMemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbtglmulaisewa_updateMemberPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                    .addComponent(jLabel44, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel45)
                    .addComponent(cbjamsewa_updateMemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnaddCart_updateMemberPanel)
                    .addComponent(btndeleteCart_updateMemberPanel))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("Tabel Member"));

        cartMember_updateMemberPanel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tgl_mulai", "Tgl_selesai", "Kd_lapangan", "Jam_sewa"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        cartMember_updateMemberPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cartMember_updateMemberPanelMouseClicked(evt);
            }
        });
        jScrollPane10.setViewportView(cartMember_updateMemberPanel);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder("Tabel Detail Member"));

        cartDetailMember_updateMemberPanel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tgl_sewa", "Kd_lapangan", "Jam_sewa"
            }
        ));
        jScrollPane11.setViewportView(cartDetailMember_updateMemberPanel);

        txtdpmember_updateMemberPanel.setText("0");

        jLabel46.setText("Total DP");

        jLabel47.setText(": Rp.");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel46)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel47)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtdpmember_updateMemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel46)
                    .addComponent(txtdpmember_updateMemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel47))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        btnupdateMember_updateMemberPanel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-save-as-16.png"))); // NOI18N
        btnupdateMember_updateMemberPanel.setText("Simpan");
        btnupdateMember_updateMemberPanel.setEnabled(false);
        btnupdateMember_updateMemberPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnupdateMember_updateMemberPanelActionPerformed(evt);
            }
        });

        btnbersih_updateMemberPanel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-refresh-16.png"))); // NOI18N
        btnbersih_updateMemberPanel.setText("Refresh");
        btnbersih_updateMemberPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbersih_updateMemberPanelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout UpdateMemberFormLayout = new javax.swing.GroupLayout(UpdateMemberForm.getContentPane());
        UpdateMemberForm.getContentPane().setLayout(UpdateMemberFormLayout);
        UpdateMemberFormLayout.setHorizontalGroup(
            UpdateMemberFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PengaturanLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, 721, Short.MAX_VALUE)
            .addGroup(UpdateMemberFormLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(UpdateMemberFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(UpdateMemberFormLayout.createSequentialGroup()
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(UpdateMemberFormLayout.createSequentialGroup()
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(UpdateMemberFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(UpdateMemberFormLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(UpdateMemberFormLayout.createSequentialGroup()
                                .addGap(91, 91, 91)
                                .addComponent(btnupdateMember_updateMemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnbersih_updateMemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        UpdateMemberFormLayout.setVerticalGroup(
            UpdateMemberFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, UpdateMemberFormLayout.createSequentialGroup()
                .addComponent(PengaturanLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(UpdateMemberFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(UpdateMemberFormLayout.createSequentialGroup()
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(UpdateMemberFormLayout.createSequentialGroup()
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(UpdateMemberFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnupdateMember_updateMemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnbersih_updateMemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        jButton3.setBackground(new java.awt.Color(0, 0, 0));
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-calendar-16.png"))); // NOI18N
        jButton3.setText("Dashboard");
        jButton3.setBorderPainted(false);
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.setFocusable(false);
        jButton3.setRequestFocusEnabled(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(0, 0, 0));
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-rent-16.png"))); // NOI18N
        jButton4.setText("Member");
        jButton4.setBorderPainted(false);
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.setFocusable(false);
        jButton4.setRequestFocusEnabled(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(0, 0, 0));
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-rent-16.png"))); // NOI18N
        jButton6.setText("Pemesanan");
        jButton6.setBorderPainted(false);
        jButton6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton6.setFocusable(false);
        jButton6.setRequestFocusEnabled(false);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("TRANSAKSI");

        jButton7.setBackground(new java.awt.Color(0, 0, 0));
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-rent-16.png"))); // NOI18N
        jButton7.setText("Pelunasan");
        jButton7.setBorderPainted(false);
        jButton7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton7.setFocusable(false);
        jButton7.setRequestFocusEnabled(false);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("DATA MASTER");

        jButton5.setBackground(new java.awt.Color(0, 0, 0));
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-stadium-16.png"))); // NOI18N
        jButton5.setText("Lapangan");
        jButton5.setBorderPainted(false);
        jButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton5.setFocusable(false);
        jButton5.setRequestFocusEnabled(false);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton8.setBackground(new java.awt.Color(0, 0, 0));
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-voucher-16.png"))); // NOI18N
        jButton8.setText("Diskon");
        jButton8.setBorderPainted(false);
        jButton8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton8.setFocusable(false);
        jButton8.setRequestFocusEnabled(false);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("LAINNYA");

        jButton9.setBackground(new java.awt.Color(0, 0, 0));
        jButton9.setForeground(new java.awt.Color(255, 255, 255));
        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-user-male-16.png"))); // NOI18N
        jButton9.setText("Profil");
        jButton9.setBorderPainted(false);
        jButton9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton9.setFocusable(false);
        jButton9.setRequestFocusEnabled(false);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton11.setBackground(new java.awt.Color(0, 0, 0));
        jButton11.setForeground(new java.awt.Color(255, 255, 255));
        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-news-16.png"))); // NOI18N
        jButton11.setText("Laporan");
        jButton11.setBorderPainted(false);
        jButton11.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton11.setFocusable(false);
        jButton11.setRequestFocusEnabled(false);
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setBackground(new java.awt.Color(0, 0, 0));
        jButton12.setForeground(new java.awt.Color(255, 255, 255));
        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-sign-out-16.png"))); // NOI18N
        jButton12.setText("Logout");
        jButton12.setBorderPainted(false);
        jButton12.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton12.setFocusable(false);
        jButton12.setRequestFocusEnabled(false);
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelGambarLogoLayout = new javax.swing.GroupLayout(panelGambarLogo);
        panelGambarLogo.setLayout(panelGambarLogoLayout);
        panelGambarLogoLayout.setHorizontalGroup(
            panelGambarLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 98, Short.MAX_VALUE)
        );
        panelGambarLogoLayout.setVerticalGroup(
            panelGambarLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 106, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jSeparator2)
                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator3)
                    .addComponent(jButton7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator4)
                    .addComponent(jButton8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(panelGambarLogo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelGambarLogo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton12)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setLayout(new java.awt.CardLayout());

        UtamaPanel.setBackground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout UtamaPanelLayout = new javax.swing.GroupLayout(UtamaPanel);
        UtamaPanel.setLayout(UtamaPanelLayout);
        UtamaPanelLayout.setHorizontalGroup(
            UtamaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 665, Short.MAX_VALUE)
        );
        UtamaPanelLayout.setVerticalGroup(
            UtamaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 616, Short.MAX_VALUE)
        );

        jPanel2.add(UtamaPanel, "card10");

        PengaturanLabel13.setBackground(new java.awt.Color(0, 0, 0));
        PengaturanLabel13.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        PengaturanLabel13.setForeground(new java.awt.Color(255, 255, 255));
        PengaturanLabel13.setText("Dashboard");

        jPanel10.setBackground(new java.awt.Color(0, 0, 0));
        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Jadwal Sewa", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 18), new java.awt.Color(255, 255, 255))); // NOI18N

        TabelDashboard_DashboardPanel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane8.setViewportView(TabelDashboard_DashboardPanel);
        if (TabelDashboard_DashboardPanel.getColumnModel().getColumnCount() > 0) {
            TabelDashboard_DashboardPanel.getColumnModel().getColumn(0).setHeaderValue("Title 1");
            TabelDashboard_DashboardPanel.getColumnModel().getColumn(1).setHeaderValue("Title 2");
            TabelDashboard_DashboardPanel.getColumnModel().getColumn(2).setHeaderValue("Title 3");
            TabelDashboard_DashboardPanel.getColumnModel().getColumn(3).setHeaderValue("Title 4");
        }

        cbtgldashboardsewa_DashboardPanel.addCommitListener(new datechooser.events.CommitListener() {
            public void onCommit(datechooser.events.CommitEvent evt) {
                cbtgldashboardsewa_DashboardPanelOnCommit(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(cbtgldashboardsewa_DashboardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbtgldashboardsewa_DashboardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout DashboardPanelLayout = new javax.swing.GroupLayout(DashboardPanel);
        DashboardPanel.setLayout(DashboardPanelLayout);
        DashboardPanelLayout.setHorizontalGroup(
            DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PengaturanLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 665, Short.MAX_VALUE)
            .addGroup(DashboardPanelLayout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        DashboardPanelLayout.setVerticalGroup(
            DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DashboardPanelLayout.createSequentialGroup()
                .addComponent(PengaturanLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(100, 100, 100)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 105, Short.MAX_VALUE))
        );

        jPanel2.add(DashboardPanel, "card2");

        Member.setBackground(new java.awt.Color(0, 0, 0));
        Member.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Member.setForeground(new java.awt.Color(255, 255, 255));
        Member.setText("Member");

        jPanel8.setBackground(new java.awt.Color(0, 0, 0));
        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Data Member", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 18), new java.awt.Color(255, 255, 255))); // NOI18N

        datamembertabel_MemberPanel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Nama_tim", "Kapten", "Email", "No. Hp", "Periode", "Biaya"
            }
        ));
        datamembertabel_MemberPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                datamembertabel_MemberPanelMouseClicked(evt);
            }
        });
        jScrollPane9.setViewportView(datamembertabel_MemberPanel);

        txtcari_MemberPanel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtcari_MemberPanelKeyPressed(evt);
            }
        });

        btncari_MemberPanel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-search-16.png"))); // NOI18N
        btncari_MemberPanel.setText("Cari");
        btncari_MemberPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncari_MemberPanelActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-add-user-male-16.png"))); // NOI18N
        jButton2.setText("Daftar Member");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 561, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(txtcari_MemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btncari_MemberPanel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton2)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtcari_MemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btncari_MemberPanel))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addContainerGap())
        );

        javax.swing.GroupLayout MemberPanelLayout = new javax.swing.GroupLayout(MemberPanel);
        MemberPanel.setLayout(MemberPanelLayout);
        MemberPanelLayout.setHorizontalGroup(
            MemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Member, javax.swing.GroupLayout.DEFAULT_SIZE, 665, Short.MAX_VALUE)
            .addGroup(MemberPanelLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        MemberPanelLayout.setVerticalGroup(
            MemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MemberPanelLayout.createSequentialGroup()
                .addComponent(Member, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(88, Short.MAX_VALUE))
        );

        jPanel2.add(MemberPanel, "card3");

        PengaturanLabel14.setBackground(new java.awt.Color(0, 0, 0));
        PengaturanLabel14.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        PengaturanLabel14.setForeground(new java.awt.Color(255, 255, 255));
        PengaturanLabel14.setText("Pemesanan");

        jLabel6.setText("No. Faktur");

        txtnofakturpesan_PemesananPanel.setEditable(false);
        txtnofakturpesan_PemesananPanel.setBackground(new java.awt.Color(255, 255, 255));

        jLabel7.setText("Nama Tim");

        cbkdlappesan_PemesananPanel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel8.setText("Email");

        jLabel9.setText("Tanggal Sewa");

        cbjamsewapesan_PemesananPanel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));

        jLabel10.setText("Kode Lapangan");

        jLabel11.setText("Jam Sewa");

        jLabel12.setText("WIB");

        btnAddtoCartPemesanan_PemesananPanel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-plus-16.png"))); // NOI18N
        btnAddtoCartPemesanan_PemesananPanel.setText("Add to Cart");
        btnAddtoCartPemesanan_PemesananPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddtoCartPemesanan_PemesananPanelActionPerformed(evt);
            }
        });

        jLabel20.setText("Kode Diskon");

        cbkddiskon_PemesananPanel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnBersihPemesanan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-refresh-16.png"))); // NOI18N
        btnBersihPemesanan.setText("Refresh");
        btnBersihPemesanan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBersihPemesananActionPerformed(evt);
            }
        });

        btnCart_PemesananPanel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-shopping-cart-16.png"))); // NOI18N
        btnCart_PemesananPanel.setText("Cart");
        btnCart_PemesananPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCart_PemesananPanelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PemesananPanelLayout = new javax.swing.GroupLayout(PemesananPanel);
        PemesananPanel.setLayout(PemesananPanelLayout);
        PemesananPanelLayout.setHorizontalGroup(
            PemesananPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PengaturanLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 665, Short.MAX_VALUE)
            .addGroup(PemesananPanelLayout.createSequentialGroup()
                .addGap(171, 171, 171)
                .addGroup(PemesananPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PemesananPanelLayout.createSequentialGroup()
                        .addComponent(btnAddtoCartPemesanan_PemesananPanel)
                        .addGap(18, 18, 18)
                        .addComponent(btnCart_PemesananPanel)
                        .addGap(18, 18, 18)
                        .addComponent(btnBersihPemesanan))
                    .addGroup(PemesananPanelLayout.createSequentialGroup()
                        .addGroup(PemesananPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11)
                            .addComponent(jLabel20))
                        .addGap(66, 66, 66)
                        .addGroup(PemesananPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbkddiskon_PemesananPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(PemesananPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtnofakturpesan_PemesananPanel)
                                .addComponent(txtnamatimpesan_PemesananPanel)
                                .addComponent(cbtanggalpesan_PemesananPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(PemesananPanelLayout.createSequentialGroup()
                                .addComponent(cbjamsewapesan_PemesananPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel12))
                            .addComponent(txtemailpesan_PemesananPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbkdlappesan_PemesananPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PemesananPanelLayout.setVerticalGroup(
            PemesananPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PemesananPanelLayout.createSequentialGroup()
                .addComponent(PengaturanLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(89, 89, 89)
                .addGroup(PemesananPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtnofakturpesan_PemesananPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(PemesananPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7)
                    .addComponent(txtnamatimpesan_PemesananPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(PemesananPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtemailpesan_PemesananPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(PemesananPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addGroup(PemesananPanelLayout.createSequentialGroup()
                        .addComponent(cbtanggalpesan_PemesananPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(PemesananPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbkdlappesan_PemesananPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))))
                .addGap(18, 18, 18)
                .addGroup(PemesananPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(cbjamsewapesan_PemesananPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addGap(18, 18, 18)
                .addGroup(PemesananPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(cbkddiskon_PemesananPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(PemesananPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddtoCartPemesanan_PemesananPanel)
                    .addComponent(btnCart_PemesananPanel)
                    .addComponent(btnBersihPemesanan))
                .addContainerGap(197, Short.MAX_VALUE))
        );

        jPanel2.add(PemesananPanel, "card5");

        PengaturanLabel15.setBackground(new java.awt.Color(0, 0, 0));
        PengaturanLabel15.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        PengaturanLabel15.setForeground(new java.awt.Color(255, 255, 255));
        PengaturanLabel15.setText("Pelunasan");

        jLabel22.setText("No. Faktur");

        txtcari_PelunasanPanel.setText("BFI");
        txtcari_PelunasanPanel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtcari_PelunasanPanelKeyPressed(evt);
            }
        });

        btnCari_PelunasanPanel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-search-16.png"))); // NOI18N
        btnCari_PelunasanPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCari_PelunasanPanelActionPerformed(evt);
            }
        });

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Detail Pemesanan"));

        TabelDetailPemesanan_PelunasanPanel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Kd_Lap", "Tgl_Sewa", "Jam_Sewa", "Harga", "DP", "Keterangan"
            }
        ));
        TabelDetailPemesanan_PelunasanPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabelDetailPemesanan_PelunasanPanelMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(TabelDetailPemesanan_PelunasanPanel);
        if (TabelDetailPemesanan_PelunasanPanel.getColumnModel().getColumnCount() > 0) {
            TabelDetailPemesanan_PelunasanPanel.getColumnModel().getColumn(4).setResizable(false);
        }

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 505, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel23.setText("Nama Tim");

        txtnamatim_PelunasanPanel.setEditable(false);

        jLabel24.setText("Sisa Bayar");

        jLabel25.setText("Diskon");

        jLabel26.setText("Total");

        txtsisabayar_PelunasanPanel.setEditable(false);

        txtdiskon_PelunasanPanel.setEditable(false);

        txttotal_PelunasanPanel.setEditable(false);

        btnProses_PelunasanPanel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-save-as-16.png"))); // NOI18N
        btnProses_PelunasanPanel.setText("Proses");
        btnProses_PelunasanPanel.setEnabled(false);
        btnProses_PelunasanPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProses_PelunasanPanelActionPerformed(evt);
            }
        });

        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-refresh-16.png"))); // NOI18N
        jButton13.setText("Refresh");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jLabel27.setText(": Rp.");

        jLabel28.setText(": Rp.");

        jLabel29.setText(": Rp.");

        txtnofaktur_PelunasanPanel.setEditable(false);

        jLabel64.setText("Cari No. Faktur/ Nama Tim");

        javax.swing.GroupLayout PelunasanPanelLayout = new javax.swing.GroupLayout(PelunasanPanel);
        PelunasanPanel.setLayout(PelunasanPanelLayout);
        PelunasanPanelLayout.setHorizontalGroup(
            PelunasanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PengaturanLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 665, Short.MAX_VALUE)
            .addGroup(PelunasanPanelLayout.createSequentialGroup()
                .addGroup(PelunasanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PelunasanPanelLayout.createSequentialGroup()
                        .addGap(75, 75, 75)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PelunasanPanelLayout.createSequentialGroup()
                        .addGap(156, 156, 156)
                        .addGroup(PelunasanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PelunasanPanelLayout.createSequentialGroup()
                                .addGap(75, 75, 75)
                                .addGroup(PelunasanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel22)
                                    .addComponent(jLabel23)))
                            .addComponent(jLabel64, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(32, 32, 32)
                        .addGroup(PelunasanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtnamatim_PelunasanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtnofaktur_PelunasanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtcari_PelunasanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(btnCari_PelunasanPanel))
                    .addGroup(PelunasanPanelLayout.createSequentialGroup()
                        .addGap(229, 229, 229)
                        .addGroup(PelunasanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PelunasanPanelLayout.createSequentialGroup()
                                .addComponent(btnProses_PelunasanPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton13))
                            .addGroup(PelunasanPanelLayout.createSequentialGroup()
                                .addGroup(PelunasanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel24)
                                    .addComponent(jLabel25)
                                    .addComponent(jLabel26))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(PelunasanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(PelunasanPanelLayout.createSequentialGroup()
                                        .addComponent(jLabel27)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtsisabayar_PelunasanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(PelunasanPanelLayout.createSequentialGroup()
                                        .addComponent(jLabel29)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txttotal_PelunasanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(PelunasanPanelLayout.createSequentialGroup()
                                        .addComponent(jLabel28)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtdiskon_PelunasanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PelunasanPanelLayout.setVerticalGroup(
            PelunasanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PelunasanPanelLayout.createSequentialGroup()
                .addComponent(PengaturanLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addGroup(PelunasanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCari_PelunasanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(PelunasanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtcari_PelunasanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel64)))
                .addGap(18, 18, 18)
                .addGroup(PelunasanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(txtnofaktur_PelunasanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(PelunasanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(txtnamatim_PelunasanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(PelunasanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(txtsisabayar_PelunasanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27))
                .addGap(18, 18, 18)
                .addGroup(PelunasanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(txtdiskon_PelunasanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28))
                .addGap(24, 24, 24)
                .addGroup(PelunasanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(txttotal_PelunasanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29))
                .addGap(27, 27, 27)
                .addGroup(PelunasanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton13)
                    .addComponent(btnProses_PelunasanPanel))
                .addContainerGap(108, Short.MAX_VALUE))
        );

        jPanel2.add(PelunasanPanel, "card7");

        PengaturanLabel12.setBackground(new java.awt.Color(0, 0, 0));
        PengaturanLabel12.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        PengaturanLabel12.setForeground(new java.awt.Color(255, 255, 255));
        PengaturanLabel12.setText("Lapangan");

        btnPilihGambarLapangan_LapanganPanel.setText("Cari");
        btnPilihGambarLapangan_LapanganPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPilihGambarLapangan_LapanganPanelActionPerformed(evt);
            }
        });

        TabelLapangan_LapanganPanel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Kode Lapangan", "Tipe", "Harga", "Gambar"
            }
        ));
        TabelLapangan_LapanganPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabelLapangan_LapanganPanelMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TabelLapangan_LapanganPanel);

        btnSimpanLapangan_LapanganPanel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-save-as-16.png"))); // NOI18N
        btnSimpanLapangan_LapanganPanel.setText("Simpan");
        btnSimpanLapangan_LapanganPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanLapangan_LapanganPanelActionPerformed(evt);
            }
        });

        btnUbahLapangan_LapanganPanel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-edit-16.png"))); // NOI18N
        btnUbahLapangan_LapanganPanel.setText("Ubah");
        btnUbahLapangan_LapanganPanel.setEnabled(false);
        btnUbahLapangan_LapanganPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahLapangan_LapanganPanelActionPerformed(evt);
            }
        });

        btnHapusLapangan_LapanganPanel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-delete-16.png"))); // NOI18N
        btnHapusLapangan_LapanganPanel.setText("Hapus");
        btnHapusLapangan_LapanganPanel.setEnabled(false);
        btnHapusLapangan_LapanganPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusLapangan_LapanganPanelActionPerformed(evt);
            }
        });

        panelGambarLap.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout panelGambarLapLayout = new javax.swing.GroupLayout(panelGambarLap);
        panelGambarLap.setLayout(panelGambarLapLayout);
        panelGambarLapLayout.setHorizontalGroup(
            panelGambarLapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 98, Short.MAX_VALUE)
        );
        panelGambarLapLayout.setVerticalGroup(
            panelGambarLapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 121, Short.MAX_VALUE)
        );

        cbtipe_LapanganPanel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Pilih--", "Vinyls", "Sintetis", "Parquette", "Semen" }));

        jLabel1.setText("Kode Lapangan");

        jLabel2.setText("Tipe");

        jLabel3.setText("Harga");

        jLabel4.setText("Rp.");

        txtkdlap_LapanganPanel.setEditable(false);
        txtkdlap_LapanganPanel.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setText("Gambar");

        txtgambar_LapanganPanel.setEditable(false);

        btnBersihLapangan_LapanganPanel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-refresh-16.png"))); // NOI18N
        btnBersihLapangan_LapanganPanel.setText("Refresh");
        btnBersihLapangan_LapanganPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBersihLapangan_LapanganPanelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout LapanganPanelLayout = new javax.swing.GroupLayout(LapanganPanel);
        LapanganPanel.setLayout(LapanganPanelLayout);
        LapanganPanelLayout.setHorizontalGroup(
            LapanganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PengaturanLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 665, Short.MAX_VALUE)
            .addGroup(LapanganPanelLayout.createSequentialGroup()
                .addGap(116, 116, 116)
                .addGroup(LapanganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(LapanganPanelLayout.createSequentialGroup()
                        .addGroup(LapanganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(LapanganPanelLayout.createSequentialGroup()
                                .addGroup(LapanganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel5))
                                .addGap(81, 81, 81)
                                .addGroup(LapanganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cbtipe_LapanganPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtkdlap_LapanganPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, LapanganPanelLayout.createSequentialGroup()
                                        .addComponent(txtgambar_LapanganPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnPilihGambarLapangan_LapanganPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(LapanganPanelLayout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txthargalap_LapanganPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addComponent(panelGambarLap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(LapanganPanelLayout.createSequentialGroup()
                        .addComponent(btnSimpanLapangan_LapanganPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnUbahLapangan_LapanganPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnHapusLapangan_LapanganPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnBersihLapangan_LapanganPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        LapanganPanelLayout.setVerticalGroup(
            LapanganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LapanganPanelLayout.createSequentialGroup()
                .addComponent(PengaturanLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(100, 100, 100)
                .addGroup(LapanganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(LapanganPanelLayout.createSequentialGroup()
                        .addGroup(LapanganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtkdlap_LapanganPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addGroup(LapanganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(cbtipe_LapanganPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addGroup(LapanganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txthargalap_LapanganPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addGroup(LapanganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtgambar_LapanganPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnPilihGambarLapangan_LapanganPanel)))
                    .addComponent(panelGambarLap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(LapanganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSimpanLapangan_LapanganPanel)
                    .addComponent(btnUbahLapangan_LapanganPanel)
                    .addComponent(btnHapusLapangan_LapanganPanel)
                    .addComponent(btnBersihLapangan_LapanganPanel))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(139, Short.MAX_VALUE))
        );

        txtkdlap_LapanganPanel.getAccessibleContext().setAccessibleName("");
        txtkdlap_LapanganPanel.getAccessibleContext().setAccessibleDescription("");

        jPanel2.add(LapanganPanel, "card4");

        DiskonLabel.setBackground(new java.awt.Color(0, 0, 0));
        DiskonLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        DiskonLabel.setForeground(new java.awt.Color(255, 255, 255));
        DiskonLabel.setText("Diskon");

        jLabel16.setText("Kode Diskon");

        txtkddiskon_DiskonPanel.setEditable(false);

        jLabel17.setText("Harga Diskon");

        jLabel18.setText("Keterangan");

        txtketerangan_DiskonPanel.setColumns(20);
        txtketerangan_DiskonPanel.setRows(5);
        jScrollPane3.setViewportView(txtketerangan_DiskonPanel);

        jLabel19.setText("Rp.");

        TabelDiskon_DiskonPanel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Kode Diskon", "Harga_Diskon", "Keterangan"
            }
        ));
        TabelDiskon_DiskonPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabelDiskon_DiskonPanelMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(TabelDiskon_DiskonPanel);
        if (TabelDiskon_DiskonPanel.getColumnModel().getColumnCount() > 0) {
            TabelDiskon_DiskonPanel.getColumnModel().getColumn(0).setHeaderValue("Kode Diskon");
            TabelDiskon_DiskonPanel.getColumnModel().getColumn(1).setHeaderValue("Harga_Diskon");
            TabelDiskon_DiskonPanel.getColumnModel().getColumn(2).setHeaderValue("Keterangan");
        }

        btnSaveDiskon_DiskonPanel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-save-as-16.png"))); // NOI18N
        btnSaveDiskon_DiskonPanel.setText("Simpan");
        btnSaveDiskon_DiskonPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveDiskon_DiskonPanelActionPerformed(evt);
            }
        });

        btnUpdateDiskon_DiskonPanel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-edit-16.png"))); // NOI18N
        btnUpdateDiskon_DiskonPanel.setText("Ubah");
        btnUpdateDiskon_DiskonPanel.setEnabled(false);
        btnUpdateDiskon_DiskonPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateDiskon_DiskonPanelActionPerformed(evt);
            }
        });

        btnDeleteDiskon_DiskonPanel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-delete-16.png"))); // NOI18N
        btnDeleteDiskon_DiskonPanel.setText("Hapus");
        btnDeleteDiskon_DiskonPanel.setEnabled(false);
        btnDeleteDiskon_DiskonPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteDiskon_DiskonPanelActionPerformed(evt);
            }
        });

        btnBersih_DiskonPanel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-refresh-16.png"))); // NOI18N
        btnBersih_DiskonPanel.setText("Refresh");
        btnBersih_DiskonPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBersih_DiskonPanelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout DiskonPanelLayout = new javax.swing.GroupLayout(DiskonPanel);
        DiskonPanel.setLayout(DiskonPanelLayout);
        DiskonPanelLayout.setHorizontalGroup(
            DiskonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(DiskonLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 665, Short.MAX_VALUE)
            .addGroup(DiskonPanelLayout.createSequentialGroup()
                .addGap(167, 167, 167)
                .addGroup(DiskonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(DiskonPanelLayout.createSequentialGroup()
                        .addGroup(DiskonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(jLabel17)
                            .addComponent(jLabel18))
                        .addGap(50, 50, 50)
                        .addGroup(DiskonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(DiskonPanelLayout.createSequentialGroup()
                                .addGroup(DiskonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnSaveDiskon_DiskonPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                                    .addComponent(btnUpdateDiskon_DiskonPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(DiskonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnDeleteDiskon_DiskonPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnBersih_DiskonPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(DiskonPanelLayout.createSequentialGroup()
                                .addComponent(jLabel19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txthargadiskon_DiskonPanel))
                            .addComponent(txtkddiskon_DiskonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        DiskonPanelLayout.setVerticalGroup(
            DiskonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DiskonPanelLayout.createSequentialGroup()
                .addComponent(DiskonLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(90, 90, 90)
                .addGroup(DiskonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(txtkddiskon_DiskonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(DiskonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(txthargadiskon_DiskonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19))
                .addGap(18, 18, 18)
                .addGroup(DiskonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(DiskonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(DiskonPanelLayout.createSequentialGroup()
                        .addComponent(btnSaveDiskon_DiskonPanel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnUpdateDiskon_DiskonPanel))
                    .addGroup(DiskonPanelLayout.createSequentialGroup()
                        .addComponent(btnDeleteDiskon_DiskonPanel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnBersih_DiskonPanel)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(130, Short.MAX_VALUE))
        );

        jPanel2.add(DiskonPanel, "card8");

        PengaturanLabel2.setBackground(new java.awt.Color(0, 0, 0));
        PengaturanLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        PengaturanLabel2.setForeground(new java.awt.Color(255, 255, 255));
        PengaturanLabel2.setText("Profil");

        jSeparator5.setBackground(new java.awt.Color(51, 51, 51));

        jSeparator6.setBackground(new java.awt.Color(51, 51, 51));

        jPanel15.setBackground(new java.awt.Color(0, 0, 0));

        jLabel49.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel49.setForeground(new java.awt.Color(255, 255, 255));
        jLabel49.setText("ID Admin");

        jLabel50.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(255, 255, 255));
        jLabel50.setText("Jenis Kelamin");

        jLabel51.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(255, 255, 255));
        jLabel51.setText("Tanggal Lahir");

        jLabel52.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(255, 255, 255));
        jLabel52.setText("Email");

        jLabel53.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(255, 255, 255));
        jLabel53.setText("No. Telp");

        jLabel54.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(255, 255, 255));
        jLabel54.setText("Alamat");

        jLabel55.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(255, 255, 255));
        jLabel55.setText(":");

        jLabel56.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel56.setForeground(new java.awt.Color(255, 255, 255));
        jLabel56.setText(":");

        jLabel57.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(255, 255, 255));
        jLabel57.setText(":");

        jLabel58.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(255, 255, 255));
        jLabel58.setText(":");

        jLabel59.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel59.setForeground(new java.awt.Color(255, 255, 255));
        jLabel59.setText(":");

        jLabel60.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel60.setForeground(new java.awt.Color(255, 255, 255));
        jLabel60.setText(":");

        Id_label.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Id_label.setForeground(new java.awt.Color(255, 255, 255));
        Id_label.setText(".....");

        jk_label.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jk_label.setForeground(new java.awt.Color(255, 255, 255));
        jk_label.setText(".....");

        tgllahir_label.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tgllahir_label.setForeground(new java.awt.Color(255, 255, 255));
        tgllahir_label.setText(".....");

        email_label.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        email_label.setForeground(new java.awt.Color(255, 255, 255));
        email_label.setText(".....");

        notelp_label.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        notelp_label.setForeground(new java.awt.Color(255, 255, 255));
        notelp_label.setText(".....");

        alamat_label.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        alamat_label.setForeground(new java.awt.Color(255, 255, 255));
        alamat_label.setText(".....");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel51)
                    .addComponent(jLabel50)
                    .addComponent(jLabel49)
                    .addComponent(jLabel52)
                    .addComponent(jLabel53)
                    .addComponent(jLabel54))
                .addGap(31, 31, 31)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel60)
                        .addGap(18, 18, 18)
                        .addComponent(alamat_label))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel57)
                        .addGap(18, 18, 18)
                        .addComponent(Id_label))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel56)
                        .addGap(18, 18, 18)
                        .addComponent(jk_label))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel55)
                        .addGap(18, 18, 18)
                        .addComponent(tgllahir_label))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel59)
                        .addGap(18, 18, 18)
                        .addComponent(notelp_label))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel58)
                        .addGap(18, 18, 18)
                        .addComponent(email_label)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel49)
                    .addComponent(jLabel57)
                    .addComponent(Id_label))
                .addGap(30, 30, 30)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel50)
                    .addComponent(jLabel56)
                    .addComponent(jk_label))
                .addGap(30, 30, 30)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel51)
                    .addComponent(jLabel55)
                    .addComponent(tgllahir_label))
                .addGap(30, 30, 30)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel52)
                    .addComponent(jLabel58)
                    .addComponent(email_label))
                .addGap(30, 30, 30)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel53)
                    .addComponent(jLabel59)
                    .addComponent(notelp_label))
                .addGap(30, 30, 30)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel54)
                    .addComponent(jLabel60)
                    .addComponent(alamat_label))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jPanel16.setBackground(new java.awt.Color(0, 0, 0));

        panelGambarAdmin.setBackground(new java.awt.Color(0, 0, 0));
        panelGambarAdmin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelGambarAdminMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelGambarAdminMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout panelGambarAdminLayout = new javax.swing.GroupLayout(panelGambarAdmin);
        panelGambarAdmin.setLayout(panelGambarAdminLayout);
        panelGambarAdminLayout.setHorizontalGroup(
            panelGambarAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 75, Short.MAX_VALUE)
        );
        panelGambarAdminLayout.setVerticalGroup(
            panelGambarAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 78, Short.MAX_VALUE)
        );

        txtnamaadmin_AkunPanel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtnamaadmin_AkunPanel.setForeground(new java.awt.Color(255, 255, 255));
        txtnamaadmin_AkunPanel.setText("Nama");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(panelGambarAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtnamaadmin_AkunPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtnamaadmin_AkunPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelGambarAdmin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout ProfilPanelLayout = new javax.swing.GroupLayout(ProfilPanel);
        ProfilPanel.setLayout(ProfilPanelLayout);
        ProfilPanelLayout.setHorizontalGroup(
            ProfilPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PengaturanLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ProfilPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ProfilPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator5)
                    .addComponent(jSeparator6, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        ProfilPanelLayout.setVerticalGroup(
            ProfilPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ProfilPanelLayout.createSequentialGroup()
                .addComponent(PengaturanLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.add(ProfilPanel, "card9");

        DiskonLabel1.setBackground(new java.awt.Color(0, 0, 0));
        DiskonLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        DiskonLabel1.setForeground(new java.awt.Color(255, 255, 255));
        DiskonLabel1.setText("Laporan");

        jPanel17.setBackground(new java.awt.Color(0, 0, 0));
        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Download PDF", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 18), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel17.setForeground(new java.awt.Color(255, 255, 255));

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-download-16.png"))); // NOI18N
        jButton10.setText("Download");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        cblap.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Presensi", "Pemesanan", "Pelunasan", "Member", "Member Detail" }));

        jLabel61.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel61.setForeground(new java.awt.Color(255, 255, 255));
        jLabel61.setText("s/d");

        jLabel62.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel62.setForeground(new java.awt.Color(255, 255, 255));
        jLabel62.setText("Jenis Laporan");

        jLabel63.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel63.setForeground(new java.awt.Color(255, 255, 255));
        jLabel63.setText("Periode");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel62)
                    .addComponent(jLabel63))
                .addGap(33, 33, 33)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tfrom, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(cblap, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel61)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tto, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(49, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cblap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel62))
                .addGap(18, 18, 18)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(tfrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(tto, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel61)
                    .addComponent(jLabel63))
                .addGap(18, 18, 18)
                .addComponent(jButton10)
                .addContainerGap(51, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout LaporanPanelLayout = new javax.swing.GroupLayout(LaporanPanel);
        LaporanPanel.setLayout(LaporanPanelLayout);
        LaporanPanelLayout.setHorizontalGroup(
            LaporanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(DiskonLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 665, Short.MAX_VALUE)
            .addGroup(LaporanPanelLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        LaporanPanelLayout.setVerticalGroup(
            LaporanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LaporanPanelLayout.createSequentialGroup()
                .addComponent(DiskonLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 313, Short.MAX_VALUE))
        );

        jPanel2.add(LaporanPanel, "card9");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnPilihGambarLapangan_LapanganPanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPilihGambarLapangan_LapanganPanelActionPerformed
        // TODO add your handling code here:
        JFileChooser();
    }//GEN-LAST:event_btnPilihGambarLapangan_LapanganPanelActionPerformed

    private void btnSimpanLapangan_LapanganPanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanLapangan_LapanganPanelActionPerformed
        // TODO add your handling code here:
        if(txtkdlap_LapanganPanel.getText()==null || cbtipe_LapanganPanel.getSelectedIndex()==0 || txthargalap_LapanganPanel.getText()==null || txtgambar_LapanganPanel.getText()==null || fileNew==null){
            JOptionPane.showMessageDialog(null, "Field masih kosong");
        }else{
            SaveLapangan();
            loadDataLapangan();
            clearLapangan();
            autoKodeLapangan();
            cbkd_lap();
            TableModelDashboard();
            btnSimpanLapangan_LapanganPanel.setEnabled(true);
            btnUbahLapangan_LapanganPanel.setEnabled(false);
            btnHapusLapangan_LapanganPanel.setEnabled(false);
            btnBersihLapangan_LapanganPanel.setEnabled(true);
        }
    }//GEN-LAST:event_btnSimpanLapangan_LapanganPanelActionPerformed

    private void btnUbahLapangan_LapanganPanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahLapangan_LapanganPanelActionPerformed
        // TODO add your handling code here:
        if(txtkdlap_LapanganPanel.getText()==null || cbtipe_LapanganPanel.getSelectedIndex()==0 || txthargalap_LapanganPanel.getText()==null || txtgambar_LapanganPanel.getText()==null){
            JOptionPane.showMessageDialog(null, "Field masih kosong");
        }else{
            int jawab = JOptionPane.showConfirmDialog(null, "Ubah data "+txtkdlap_LapanganPanel.getText()+" ?", "Data Lapangan", 0);
            if(jawab == JOptionPane.YES_OPTION){
                UpdateLapangan();
            }
            loadDataLapangan();
            clearLapangan();
            autoKodeLapangan();
            btnSimpanLapangan_LapanganPanel.setEnabled(true);
            btnUbahLapangan_LapanganPanel.setEnabled(false);
            btnHapusLapangan_LapanganPanel.setEnabled(false);
            btnBersihLapangan_LapanganPanel.setEnabled(true);
        }
    }//GEN-LAST:event_btnUbahLapangan_LapanganPanelActionPerformed

    private void btnHapusLapangan_LapanganPanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusLapangan_LapanganPanelActionPerformed
        // TODO add your handling code here:
        if(txtkdlap_LapanganPanel.getText()==null){
            JOptionPane.showMessageDialog(null, "Pilih data yang ingin dihapus");
        }else{
            int jawab = JOptionPane.showConfirmDialog(null, "Hapus data "+txtkdlap_LapanganPanel.getText()+" ?", "Data Lapangan", 0);
            if(jawab == JOptionPane.YES_OPTION){
                DeleteLapangan();
            }          
            loadDataLapangan();
            clearLapangan();
            autoKodeLapangan();
            cbkd_lap();
            TableModelDashboard();
            btnSimpanLapangan_LapanganPanel.setEnabled(true);
            btnUbahLapangan_LapanganPanel.setEnabled(false);
            btnHapusLapangan_LapanganPanel.setEnabled(false);
            btnBersihLapangan_LapanganPanel.setEnabled(true);
        }
    }//GEN-LAST:event_btnHapusLapangan_LapanganPanelActionPerformed

    private void btnBersihLapangan_LapanganPanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBersihLapangan_LapanganPanelActionPerformed
        // TODO add your handling code here:
        clearLapangan();
        loadDataLapangan();
        autoKodeLapangan();
        btnSimpanLapangan_LapanganPanel.setEnabled(true);
        btnUbahLapangan_LapanganPanel.setEnabled(false);
        btnHapusLapangan_LapanganPanel.setEnabled(false);
        btnBersihLapangan_LapanganPanel.setEnabled(true);
    }//GEN-LAST:event_btnBersihLapangan_LapanganPanelActionPerformed

    private void TabelLapangan_LapanganPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabelLapangan_LapanganPanelMouseClicked
        // TODO add your handling code here:
        ClickTabelLapangan();
        btnSimpanLapangan_LapanganPanel.setEnabled(false);
        btnUbahLapangan_LapanganPanel.setEnabled(true);
        btnHapusLapangan_LapanganPanel.setEnabled(true);
        btnBersihLapangan_LapanganPanel.setEnabled(true);
    }//GEN-LAST:event_TabelLapangan_LapanganPanelMouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        UtamaPanel.setVisible(false);
        DashboardPanel.setVisible(true);
        MemberPanel.setVisible(false);
        PemesananPanel.setVisible(false);
        PelunasanPanel.setVisible(false);
        LapanganPanel.setVisible(false);
        DiskonPanel.setVisible(false);
        ProfilPanel.setVisible(false);
        LaporanPanel.setVisible(false);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        UtamaPanel.setVisible(false);
        DashboardPanel.setVisible(false);
        MemberPanel.setVisible(true);
        PemesananPanel.setVisible(false);
        PelunasanPanel.setVisible(false);
        LapanganPanel.setVisible(false);
        DiskonPanel.setVisible(false);
        ProfilPanel.setVisible(false);
        LaporanPanel.setVisible(false);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        UtamaPanel.setVisible(false);
        DashboardPanel.setVisible(false);
        MemberPanel.setVisible(false);
        PemesananPanel.setVisible(true);
        PelunasanPanel.setVisible(false);
        LapanganPanel.setVisible(false);
        DiskonPanel.setVisible(false);
        ProfilPanel.setVisible(false);
        LaporanPanel.setVisible(false);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        UtamaPanel.setVisible(false);
        DashboardPanel.setVisible(false);
        MemberPanel.setVisible(false);
        PemesananPanel.setVisible(false);
        PelunasanPanel.setVisible(true);
        LapanganPanel.setVisible(false);
        DiskonPanel.setVisible(false);
        ProfilPanel.setVisible(false);
        LaporanPanel.setVisible(false);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        UtamaPanel.setVisible(false);
        DashboardPanel.setVisible(false);
        MemberPanel.setVisible(false);
        PemesananPanel.setVisible(false);
        PelunasanPanel.setVisible(false);
        LapanganPanel.setVisible(true);
        DiskonPanel.setVisible(false);
        ProfilPanel.setVisible(false);
        LaporanPanel.setVisible(false);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void btnAddtoCartPemesanan_PemesananPanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddtoCartPemesanan_PemesananPanelActionPerformed
        // TODO add your handling code here:
        String kdlap=null;
        String tgl=null;
        String jam=null;
        String sql = "select kd_lapangan,tgl_sewa,jam_sewa from pemesanan_detail where kd_lapangan='"+cbkdlappesan_PemesananPanel.getSelectedItem().toString()+"' and tgl_sewa='"+cbtanggalpesan_PemesananPanel.getText()+"' and jam_sewa='"+cbjamsewapesan_PemesananPanel.getSelectedItem().toString()+"'";
        try{
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                kdlap = rs.getString(1);
                tgl = rs.getString(2);
                jam = rs.getString(3);
            }
        }catch(SQLException e){
            
        }
        
        int a = 0;
        if(TabelCartPesan_PemesananPanel.getRowCount()==0){
            if(kdlap!=null && tgl!=null && jam!=null){
                JOptionPane.showMessageDialog(null, "Variabel sewa sudah terisi");
            }else{
                addCartPemesanan();
                totalPemesanan();
                if(TabelCartPesan_PemesananPanel.getRowCount()<4){
                    btnAddtoCartPemesanan_PemesananPanel.setEnabled(true);
                }else{
                    btnAddtoCartPemesanan_PemesananPanel.setEnabled(false);
                }
                btnCheckout_PemesananPanel.setEnabled(true);
            }
        }else{
            for(int i=0;i<TabelCartPesan_PemesananPanel.getRowCount();i++){
                if((TabelCartPesan_PemesananPanel.getValueAt(i, 0).toString().equals(cbkdlappesan_PemesananPanel.getSelectedItem().toString())) && (TabelCartPesan_PemesananPanel.getValueAt(i, 1).toString().equals(cbtanggalpesan_PemesananPanel.getText())) && (TabelCartPesan_PemesananPanel.getValueAt(i, 2).toString().equals(cbjamsewapesan_PemesananPanel.getSelectedItem().toString()))){
                    a = 1;
                }
            }
            if(a==1){
                JOptionPane.showMessageDialog(null, "Variabel sudah dimasukan");
            }else{
                if(kdlap!=null && tgl!=null && jam!=null){
                    JOptionPane.showMessageDialog(null, "Variabel sewa sudah terisi");
                }else{
                    addCartPemesanan();
                    totalPemesanan();
                    if(TabelCartPesan_PemesananPanel.getRowCount()<4){
                        btnAddtoCartPemesanan_PemesananPanel.setEnabled(true);
                    }else{
                        btnAddtoCartPemesanan_PemesananPanel.setEnabled(false);
                    }
                    btnCheckout_PemesananPanel.setEnabled(true);
                }
            }
        }
    }//GEN-LAST:event_btnAddtoCartPemesanan_PemesananPanelActionPerformed

    private void btnCheckout_PemesananPanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckout_PemesananPanelActionPerformed
        // TODO add your handling code here:
        int jawab = JOptionPane.showConfirmDialog(null, "Yakin ingin memesan ?", "Pemesanan", 0);
        if(jawab == JOptionPane.YES_OPTION){
            savePemesanan();
            clearPemesanan();
            autoNoFaktur();
            TableModelDashboard();
            if(TabelCartPesan_PemesananPanel.getRowCount()==0){
                btnDeletefromCartPemesanan_PemesananPanel.setEnabled(false);
                btnCheckout_PemesananPanel.setEnabled(false);
            }else{
                btnDeletefromCartPemesanan_PemesananPanel.setEnabled(true);
                btnCheckout_PemesananPanel.setEnabled(true);
            }
            cbtanggalpesan_PemesananPanel.setEnabled(true);
        }
    }//GEN-LAST:event_btnCheckout_PemesananPanelActionPerformed

    private void btnDeletefromCartPemesanan_PemesananPanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeletefromCartPemesanan_PemesananPanelActionPerformed
        // TODO add your handling code here:
        deleteCart();
        totalPemesanan();
        if(TabelCartPesan_PemesananPanel.getRowCount()==0){
            btnDeletefromCartPemesanan_PemesananPanel.setEnabled(false);
            btnCheckout_PemesananPanel.setEnabled(false);
        }else{
            btnDeletefromCartPemesanan_PemesananPanel.setEnabled(true);
            btnCheckout_PemesananPanel.setEnabled(true);
        }
        btnDeletefromCartPemesanan_PemesananPanel.setEnabled(false);
        btnAddtoCartPemesanan_PemesananPanel.setEnabled(true);
    }//GEN-LAST:event_btnDeletefromCartPemesanan_PemesananPanelActionPerformed

    private void btnSaveDiskon_DiskonPanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveDiskon_DiskonPanelActionPerformed
        // TODO add your handling code here:
        saveDiskon();
        TabelModelDiskon();
        clearDiskon();
        autoKodeDiskon();
        cbkd_diskon();
    }//GEN-LAST:event_btnSaveDiskon_DiskonPanelActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        UtamaPanel.setVisible(false);
        DashboardPanel.setVisible(false);
        MemberPanel.setVisible(false);
        PemesananPanel.setVisible(false);
        PelunasanPanel.setVisible(false);
        LapanganPanel.setVisible(false);
        DiskonPanel.setVisible(true);
        ProfilPanel.setVisible(false);
        LaporanPanel.setVisible(false);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void TabelDiskon_DiskonPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabelDiskon_DiskonPanelMouseClicked
        // TODO add your handling code here:
        ClickTabelDiskon();
    }//GEN-LAST:event_TabelDiskon_DiskonPanelMouseClicked

    private void btnUpdateDiskon_DiskonPanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateDiskon_DiskonPanelActionPerformed
        // TODO add your handling code here:
        updateDiskon();
        TabelModelDiskon();
        clearDiskon();
        autoKodeDiskon();
    }//GEN-LAST:event_btnUpdateDiskon_DiskonPanelActionPerformed

    private void btnDeleteDiskon_DiskonPanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteDiskon_DiskonPanelActionPerformed
        // TODO add your handling code here:
        if(txtkddiskon_DiskonPanel.getText().equals("D-01")){
            JOptionPane.showMessageDialog(null, "Gagal Dihapus");
        }else{
            int jawab = JOptionPane.showConfirmDialog(this, "Hapus data diskon "+txtkddiskon_DiskonPanel.getText(), "Data Diskon", 0);
            if(jawab == JOptionPane.YES_OPTION){
                deleteDiskon();
                TabelModelDiskon();
                clearDiskon();
                autoKodeDiskon();
                cbkd_diskon();
            }
        }
        
    }//GEN-LAST:event_btnDeleteDiskon_DiskonPanelActionPerformed

    private void btnBersih_DiskonPanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBersih_DiskonPanelActionPerformed
        // TODO add your handling code here:
        clearDiskon();
        autoKodeDiskon();
    }//GEN-LAST:event_btnBersih_DiskonPanelActionPerformed

    private void TabelCartPesan_PemesananPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabelCartPesan_PemesananPanelMouseClicked
        // TODO add your handling code here:
        btnDeletefromCartPemesanan_PemesananPanel.setEnabled(true);
    }//GEN-LAST:event_TabelCartPesan_PemesananPanelMouseClicked

    private void btnBersihPemesananActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBersihPemesananActionPerformed
        // TODO add your handling code here:
        clearPemesanan();
        totalPemesanan();
        autoNoFaktur();
    }//GEN-LAST:event_btnBersihPemesananActionPerformed

    private void btnCari_PelunasanPanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCari_PelunasanPanelActionPerformed
        cariPelunasan();
    }//GEN-LAST:event_btnCari_PelunasanPanelActionPerformed

    private void TabelDetailPemesanan_PelunasanPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabelDetailPemesanan_PelunasanPanelMouseClicked
        // TODO add your handling code here:
        ClickTabelPemesananDetail_Pelunasan();
        btnProses_PelunasanPanel.setEnabled(true);
    }//GEN-LAST:event_TabelDetailPemesanan_PelunasanPanelMouseClicked

    private void btnProses_PelunasanPanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProses_PelunasanPanelActionPerformed
        // TODO add your handling code here:
        int jawab = JOptionPane.showConfirmDialog(null, "Update pelunasan ?", "Form Pemesanan", 0);
        if(jawab == JOptionPane.YES_OPTION){
            prosesPelunasan();
            cariPelunasan();
            TableModelDashboard();
        }
        btnProses_PelunasanPanel.setEnabled(false);
    }//GEN-LAST:event_btnProses_PelunasanPanelActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
        clearPelunasan();
        btnProses_PelunasanPanel.setEnabled(false);
    }//GEN-LAST:event_jButton13ActionPerformed

    private void btnCart_PemesananPanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCart_PemesananPanelActionPerformed
        // TODO add your handling code here:
        PemesananCartDialog.setVisible(true);
    }//GEN-LAST:event_btnCart_PemesananPanelActionPerformed

    private void cbtgldashboardsewa_DashboardPanelOnCommit(datechooser.events.CommitEvent evt) {//GEN-FIRST:event_cbtgldashboardsewa_DashboardPanelOnCommit
        // TODO add your handling code here:
        TableModelDashboard();
    }//GEN-LAST:event_cbtgldashboardsewa_DashboardPanelOnCommit

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        NewMemberForm.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnaddCart_MemberPanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaddCart_MemberPanelActionPerformed
        // TODO add your handling code here:
        String kdlap=null;
        String tgl=null;
        String jam=null;
        String sql = "select kd_lapangan,tgl_sewa,jam_sewa from pemesanan_detail where kd_lapangan='"+cbkdlap_MemberPanel.getSelectedItem().toString()+"' and tgl_sewa='"+cbtglmulaisewa_MemberPanel.getText()+"' and jam_sewa='"+cbjamsewa_MemberPanel.getSelectedItem().toString()+"'";
        try{
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                kdlap = rs.getString(1);
                tgl = rs.getString(2);
                jam = rs.getString(3);
            }
        }catch(SQLException e){
            
        }
        
        int a=0;
        if(cartDetailMember_MemberPanel.getRowCount()==0){
            if(kdlap!=null && tgl!=null && jam!=null){
                JOptionPane.showMessageDialog(null, "Variabel sewa sudah terisi");
            }else{
                addCartMember();
                cekDetailMember();
                btnsavemember_MemberPanel.setEnabled(true);
            }
        }else{
            for(int i=0;i<cartDetailMember_MemberPanel.getRowCount();i++){
                if((cartDetailMember_MemberPanel.getValueAt(i, 1).toString().equals(cbkdlap_MemberPanel.getSelectedItem().toString())) && (cartDetailMember_MemberPanel.getValueAt(i, 0).toString().equals(cbtglmulaisewa_MemberPanel.getText())) && (cartDetailMember_MemberPanel.getValueAt(i, 2).toString().equals(cbjamsewa_MemberPanel.getSelectedItem().toString()))){
                    a = 1;
                }
            }
            if(a==1){
                JOptionPane.showMessageDialog(null, "Variabel sudah ada");
            }else{
                if(kdlap!=null && tgl!=null && jam!=null){
                    JOptionPane.showMessageDialog(null, "Variabel sewa sudah terisi");
                }else{
                    addCartMember();
                    cekDetailMember();
                    btnsavemember_MemberPanel.setEnabled(true);
                }
            }
        }
    }//GEN-LAST:event_btnaddCart_MemberPanelActionPerformed

    private void btnbersih_MemberPanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbersih_MemberPanelActionPerformed
        // TODO add your handling code here:
        bersihMember();
    }//GEN-LAST:event_btnbersih_MemberPanelActionPerformed

    private void btndeleteCart_MemberPanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btndeleteCart_MemberPanelActionPerformed
        // TODO add your handling code here:
        deleteCartMember();
        btnsavemember_MemberPanel.setEnabled(false);
        if(cartMember_MemberPanel.getRowCount()==0){
            btndeleteCart_MemberPanel.setEnabled(false);
            btnsavemember_MemberPanel.setEnabled(false);
        }else{
            btndeleteCart_MemberPanel.setEnabled(true);
        }
        btndeleteCart_MemberPanel.setEnabled(false);
        cekDetailMember();
        btnsavemember_MemberPanel.setEnabled(true);
    }//GEN-LAST:event_btndeleteCart_MemberPanelActionPerformed

    private void btnsavemember_MemberPanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsavemember_MemberPanelActionPerformed
        // TODO add your handling code here:
        if(txtnamatimmember_MemberPanel.getText().equals("")||txtkapten_MemberPanel.getText().equals("")||txtemail_MemberPanel.getText().equals("")||txtnohp_MemberPanel.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Field profil member masih kosong.");
        }else{
            int jawab = JOptionPane.showConfirmDialog(null, "Simpan Data Member ?", "Member", 0);
            if(jawab == JOptionPane.YES_OPTION){
                saveMember();
                TableModelDashboard();
                bersihMember();
                TableModelIDMember();
            }
        }
    }//GEN-LAST:event_btnsavemember_MemberPanelActionPerformed

    private void cartMember_MemberPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cartMember_MemberPanelMouseClicked
        // TODO add your handling code here:
        btndeleteCart_MemberPanel.setEnabled(true);
    }//GEN-LAST:event_cartMember_MemberPanelMouseClicked

    private void btncari_MemberPanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncari_MemberPanelActionPerformed
        // TODO add your handling code here:
        if(txtcari_MemberPanel.getText().equals("")){
            TableModelIDMember();
        }else /*if("M-".equals(txtcari_MemberPanel.getText().substring(0,2)))*/{
            cariIDMember();
        }/*else{
            JOptionPane.showMessageDialog(this, "Input yang anda masukan salah");
        }*/
    }//GEN-LAST:event_btncari_MemberPanelActionPerformed

    private void datamembertabel_MemberPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_datamembertabel_MemberPanelMouseClicked
        // TODO add your handling code here:
        UpdateMemberForm.setVisible(true);
        int row = datamembertabel_MemberPanel.getSelectedRow();
        txtidmember_updateMemberPanel.setText(datamembertabel_MemberPanel.getValueAt(row, 0).toString());
        txtnamatimmember_updateMemberPanel.setText(datamembertabel_MemberPanel.getValueAt(row, 1).toString());
        txtkapten_updateMemberPanel.setText(datamembertabel_MemberPanel.getValueAt(row, 2).toString());
        txtemail_updateMemberPanel.setText(datamembertabel_MemberPanel.getValueAt(row, 3).toString());
        txtnohp_updateMemberPanel.setText(datamembertabel_MemberPanel.getValueAt(row, 4).toString());
    }//GEN-LAST:event_datamembertabel_MemberPanelMouseClicked

    private void btnaddCart_updateMemberPanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaddCart_updateMemberPanelActionPerformed
        // TODO add your handling code here:
        String kdlap=null;
        String tgl=null;
        String jam=null;
        String sql = "select kd_lapangan,tgl_sewa,jam_sewa from pemesanan_detail where kd_lapangan='"+cbkdlap_updateMemberPanel.getSelectedItem().toString()+"' and tgl_sewa='"+cbtglmulaisewa_updateMemberPanel.getText()+"' and jam_sewa='"+cbjamsewa_updateMemberPanel.getSelectedItem().toString()+"'";
        try{
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                kdlap = rs.getString(1);
                tgl = rs.getString(2);
                jam = rs.getString(3);
            }
        }catch(SQLException e){
            
        }
        
        int a=0;
        if(cartDetailMember_updateMemberPanel.getRowCount()==0){
            if(kdlap!=null && tgl!=null && jam!=null){
                JOptionPane.showMessageDialog(null, "Variabel sewa sudah terisi");
            }else{
                addCartUpdateMember();
                cekUpdateDetailMember();
                btnupdateMember_updateMemberPanel.setEnabled(true);
            }
        }else{
            for(int i=0;i<cartDetailMember_updateMemberPanel.getRowCount();i++){
                if((cartDetailMember_updateMemberPanel.getValueAt(i, 1).toString().equals(cbkdlap_updateMemberPanel.getSelectedItem().toString())) && (cartDetailMember_updateMemberPanel.getValueAt(i, 0).toString().equals(cbtglmulaisewa_updateMemberPanel.getText())) && (cartDetailMember_updateMemberPanel.getValueAt(i, 2).toString().equals(cbjamsewa_updateMemberPanel.getSelectedItem().toString()))){
                    a = 1;
                }
            }
            if(a==1){
                JOptionPane.showMessageDialog(null, "Variabel sudah ada");
            }else{
                if(kdlap!=null && tgl!=null && jam!=null){
                    JOptionPane.showMessageDialog(null, "Variabel sewa sudah terisi");
                }else{
                    addCartUpdateMember();
                    cekUpdateDetailMember();
                    btnupdateMember_updateMemberPanel.setEnabled(true);
                }
            }
        }
    }//GEN-LAST:event_btnaddCart_updateMemberPanelActionPerformed

    private void btndeleteCart_updateMemberPanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btndeleteCart_updateMemberPanelActionPerformed
        // TODO add your handling code here:
        deleteUpdateCartMember();
        btnupdateMember_updateMemberPanel.setEnabled(false);
        if(cartMember_updateMemberPanel.getRowCount()==0){
            btndeleteCart_updateMemberPanel.setEnabled(false);
            btnupdateMember_updateMemberPanel.setEnabled(false);
        }else{
            btndeleteCart_updateMemberPanel.setEnabled(true);
        }
        btndeleteCart_updateMemberPanel.setEnabled(false);
        cekUpdateDetailMember();
        btnupdateMember_updateMemberPanel.setEnabled(true);
        if(cartMember_updateMemberPanel.getRowCount()==0){
            btnupdateMember_updateMemberPanel.setEnabled(false);
        }else{
            btnupdateMember_updateMemberPanel.setEnabled(true);
        }
    }//GEN-LAST:event_btndeleteCart_updateMemberPanelActionPerformed

    private void cartMember_updateMemberPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cartMember_updateMemberPanelMouseClicked
        // TODO add your handling code here:
        btndeleteCart_updateMemberPanel.setEnabled(true);
    }//GEN-LAST:event_cartMember_updateMemberPanelMouseClicked

    private void btnbersih_updateMemberPanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbersih_updateMemberPanelActionPerformed
        // TODO add your handling code here:
        bersihUpdateMember();
    }//GEN-LAST:event_btnbersih_updateMemberPanelActionPerformed

    private void btnupdateMember_updateMemberPanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnupdateMember_updateMemberPanelActionPerformed
        // TODO add your handling code here
        if(txtnamatimmember_updateMemberPanel.getText().equals("")||txtkapten_updateMemberPanel.getText().equals("")||txtemail_updateMemberPanel.getText().equals("")||txtnohp_updateMemberPanel.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Field profil member masih kosong.");
        }else{
            int jawab = JOptionPane.showConfirmDialog(null, "Update Data Member ?", "Member", 0);
            if(jawab == JOptionPane.YES_OPTION){
                updateMember();
                TableModelDashboard();
                bersihUpdateMember();
                TableModelIDMember();
            }
        }
    }//GEN-LAST:event_btnupdateMember_updateMemberPanelActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        UtamaPanel.setVisible(false);
        DashboardPanel.setVisible(false);
        MemberPanel.setVisible(false);
        PemesananPanel.setVisible(false);
        PelunasanPanel.setVisible(false);
        LapanganPanel.setVisible(false);
        DiskonPanel.setVisible(false);
        ProfilPanel.setVisible(true);
        LaporanPanel.setVisible(false);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void panelGambarAdminMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelGambarAdminMouseClicked
        // TODO add your handling code here:
        updatePhoto();
        attributesAdmin();
    }//GEN-LAST:event_panelGambarAdminMouseClicked

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
        int jawab = JOptionPane.showConfirmDialog(null, "Anda ingin keluar ?", "Logout", 0);
        if(jawab == JOptionPane.YES_OPTION){
            new Login().setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
        UtamaPanel.setVisible(false);
        DashboardPanel.setVisible(false);
        MemberPanel.setVisible(false);
        PemesananPanel.setVisible(false);
        PelunasanPanel.setVisible(false);
        LapanganPanel.setVisible(false);
        DiskonPanel.setVisible(false);
        ProfilPanel.setVisible(false);
        LaporanPanel.setVisible(true);
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
        HashMap parameter = new HashMap();
        boolean isError = false;
        
        try{
            DateFormat df1 = new SimpleDateFormat("M/d/yy");
            DateFormat df2 = new SimpleDateFormat("M/d/yy");
            Date date1 = df1.parse(tfrom.getText());
            Date date2 = df2.parse(tto.getText());
            
            parameter.put("from", date1);
            parameter.put("to", date2);
        }catch(ParseException e){
            JOptionPane.showMessageDialog(null, e);
            System.err.println("Error : " + e);
        }
        
        if(!isError){
            String LP = cblap.getSelectedItem().toString();
            switch(LP){
                case "Presensi":
                    previewReport("absensi.jasper",parameter,LP);
                    break;
                case "Pemesanan":
                    previewReport("pemesanan.jasper",parameter,LP);
                    break;
                case "Pelunasan":
                    previewReport("pelunasan.jasper",parameter,LP);
                    break;
                case "Member":
                    previewReport("member.jasper",parameter,LP);
                    break;
                case "Member Detail":
                    previewReport("member_detail.jasper",parameter,LP);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "eror");
                    break;
            }
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void txtcari_PelunasanPanelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcari_PelunasanPanelKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            btnCari_PelunasanPanelActionPerformed(null);
        }
    }//GEN-LAST:event_txtcari_PelunasanPanelKeyPressed

    private void txtcari_MemberPanelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcari_MemberPanelKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            btncari_MemberPanelActionPerformed(null);
        }
    }//GEN-LAST:event_txtcari_MemberPanelKeyPressed

    private void panelGambarAdminMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelGambarAdminMouseEntered
        // TODO add your handling code here:
        panelGambarAdmin.setCursor(Cursor.getPredefinedCursor(12));
    }//GEN-LAST:event_panelGambarAdminMouseEntered

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrameUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrameUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrameUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrameUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrameUtama().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel DashboardPanel;
    private java.awt.Label DiskonLabel;
    private java.awt.Label DiskonLabel1;
    private javax.swing.JPanel DiskonPanel;
    private javax.swing.JLabel Id_label;
    private javax.swing.JPanel LapanganPanel;
    private javax.swing.JPanel LaporanPanel;
    private java.awt.Label Member;
    private javax.swing.JPanel MemberPanel;
    private javax.swing.JDialog NewMemberForm;
    private javax.swing.JPanel PelunasanPanel;
    private javax.swing.JDialog PemesananCartDialog;
    private javax.swing.JPanel PemesananPanel;
    private java.awt.Label PengaturanLabel12;
    private java.awt.Label PengaturanLabel13;
    private java.awt.Label PengaturanLabel14;
    private java.awt.Label PengaturanLabel15;
    private java.awt.Label PengaturanLabel16;
    private java.awt.Label PengaturanLabel17;
    private java.awt.Label PengaturanLabel2;
    private javax.swing.JPanel ProfilPanel;
    private javax.swing.JTable TabelCartPesan_PemesananPanel;
    private javax.swing.JTable TabelDashboard_DashboardPanel;
    private javax.swing.JTable TabelDetailPemesanan_PelunasanPanel;
    private javax.swing.JTable TabelDiskon_DiskonPanel;
    private javax.swing.JTable TabelLapangan_LapanganPanel;
    private javax.swing.JDialog UpdateMemberForm;
    private javax.swing.JPanel UtamaPanel;
    private javax.swing.JLabel alamat_label;
    private javax.swing.JButton btnAddtoCartPemesanan_PemesananPanel;
    private javax.swing.JButton btnBersihLapangan_LapanganPanel;
    private javax.swing.JButton btnBersihPemesanan;
    private javax.swing.JButton btnBersih_DiskonPanel;
    private javax.swing.JButton btnCari_PelunasanPanel;
    private javax.swing.JButton btnCart_PemesananPanel;
    private javax.swing.JButton btnCheckout_PemesananPanel;
    private javax.swing.JButton btnDeleteDiskon_DiskonPanel;
    private javax.swing.JButton btnDeletefromCartPemesanan_PemesananPanel;
    private javax.swing.JButton btnHapusLapangan_LapanganPanel;
    private javax.swing.JButton btnPilihGambarLapangan_LapanganPanel;
    private javax.swing.JButton btnProses_PelunasanPanel;
    private javax.swing.JButton btnSaveDiskon_DiskonPanel;
    private javax.swing.JButton btnSimpanLapangan_LapanganPanel;
    private javax.swing.JButton btnUbahLapangan_LapanganPanel;
    private javax.swing.JButton btnUpdateDiskon_DiskonPanel;
    private javax.swing.JButton btnaddCart_MemberPanel;
    private javax.swing.JButton btnaddCart_updateMemberPanel;
    private javax.swing.JButton btnbersih_MemberPanel;
    private javax.swing.JButton btnbersih_updateMemberPanel;
    private javax.swing.JButton btncari_MemberPanel;
    private javax.swing.JButton btndeleteCart_MemberPanel;
    private javax.swing.JButton btndeleteCart_updateMemberPanel;
    private javax.swing.JButton btnsavemember_MemberPanel;
    private javax.swing.JButton btnupdateMember_updateMemberPanel;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JTable cartDetailMember_MemberPanel;
    private javax.swing.JTable cartDetailMember_updateMemberPanel;
    private javax.swing.JTable cartMember_MemberPanel;
    private javax.swing.JTable cartMember_updateMemberPanel;
    private javax.swing.JComboBox<String> cbjamsewa_MemberPanel;
    private javax.swing.JComboBox<String> cbjamsewa_updateMemberPanel;
    private javax.swing.JComboBox<String> cbjamsewapesan_PemesananPanel;
    private javax.swing.JComboBox<String> cbkddiskon_PemesananPanel;
    private javax.swing.JComboBox<String> cbkdlap_MemberPanel;
    private javax.swing.JComboBox<String> cbkdlap_updateMemberPanel;
    private javax.swing.JComboBox<String> cbkdlappesan_PemesananPanel;
    private javax.swing.JComboBox<String> cblap;
    private datechooser.beans.DateChooserCombo cbtanggalpesan_PemesananPanel;
    private datechooser.beans.DateChooserCombo cbtgldashboardsewa_DashboardPanel;
    private datechooser.beans.DateChooserCombo cbtglmulaisewa_MemberPanel;
    private datechooser.beans.DateChooserCombo cbtglmulaisewa_updateMemberPanel;
    private javax.swing.JComboBox<String> cbtipe_LapanganPanel;
    private javax.swing.JTable datamembertabel_MemberPanel;
    private javax.swing.JLabel email_label;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JLabel jk_label;
    private javax.swing.JLabel notelp_label;
    private tampilan.PanelGambar panelGambarAdmin;
    private tampilan.PanelGambar panelGambarLap;
    private tampilan.PanelGambar panelGambarLogo;
    private datechooser.beans.DateChooserCombo tfrom;
    private javax.swing.JLabel tgllahir_label;
    private datechooser.beans.DateChooserCombo tto;
    private javax.swing.JTextField txtcari_MemberPanel;
    private javax.swing.JTextField txtcari_PelunasanPanel;
    private javax.swing.JTextField txtdiskon_PelunasanPanel;
    private javax.swing.JTextField txtdpmember_MemberPanel;
    private javax.swing.JTextField txtdpmember_updateMemberPanel;
    private javax.swing.JTextField txtemail_MemberPanel;
    private javax.swing.JTextField txtemail_updateMemberPanel;
    private javax.swing.JTextField txtemailpesan_PemesananPanel;
    private javax.swing.JTextField txtgambar_LapanganPanel;
    private javax.swing.JTextField txthargadiskon_DiskonPanel;
    private javax.swing.JTextField txthargalap_LapanganPanel;
    private javax.swing.JTextField txtidmember_updateMemberPanel;
    private javax.swing.JTextField txtkapten_MemberPanel;
    private javax.swing.JTextField txtkapten_updateMemberPanel;
    private javax.swing.JTextField txtkddiskon_DiskonPanel;
    private javax.swing.JTextField txtkdlap_LapanganPanel;
    private javax.swing.JTextArea txtketerangan_DiskonPanel;
    private javax.swing.JLabel txtnamaadmin_AkunPanel;
    private javax.swing.JTextField txtnamatim_PelunasanPanel;
    private javax.swing.JTextField txtnamatimmember_MemberPanel;
    private javax.swing.JTextField txtnamatimmember_updateMemberPanel;
    private javax.swing.JTextField txtnamatimpesan_PemesananPanel;
    private javax.swing.JTextField txtnofaktur_PelunasanPanel;
    private javax.swing.JTextField txtnofakturpesan_PemesananPanel;
    private javax.swing.JTextField txtnohp_MemberPanel;
    private javax.swing.JTextField txtnohp_updateMemberPanel;
    private javax.swing.JTextField txtsisabayar_PelunasanPanel;
    private javax.swing.JTextField txttotal_PelunasanPanel;
    private javax.swing.JTextField txttotalpemesanan;
    // End of variables declaration//GEN-END:variables
}
