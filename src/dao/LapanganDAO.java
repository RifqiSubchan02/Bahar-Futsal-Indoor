/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Lapangan;
import java.util.List;

/**
 *
 * @author Windows
 */
public interface LapanganDAO {
    public void save(Lapangan lapangan);
    public void update(Lapangan lapangan);
    public void delete(Lapangan lapangan);
    public List<Lapangan> getAll();
}
