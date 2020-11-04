/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skripsi;
import javax.swing.JOptionPane;
import tampilan.Loading;
import tampilan.Login;
/**
 *
 * @author Windows
 */
public class Skripsi {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Loading load = new Loading();
        load.setVisible(true);
        for(int i=0; i<=100; i++){
            try{
                load.getProgessBar().setValue(i);
                Thread.sleep(40);
            }catch(InterruptedException e){
                JOptionPane.showMessageDialog(null, "Loading Gagal \n"+e);
            }
        }
        load.dispose();
        new Login().setVisible(true);
    }
    
}
