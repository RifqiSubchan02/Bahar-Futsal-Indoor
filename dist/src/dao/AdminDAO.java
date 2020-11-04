/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Admin;
import java.util.List;

/**
 *
 * @author Windows
 */
public interface AdminDAO {
    public void save(Admin admin);
    public void delete(Admin admin);
    public List<Admin> getAll();
}
