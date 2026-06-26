package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Koneksi {
    
    private static Connection mysqlconfig;
    
    public static Connection getKoneksi() throws SQLException {
        try {
            String url = "jdbc:mysql://localhost:3306/db_toko_berkah"; 
            String user = "root"; 
            String pass = "";     
            
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            mysqlconfig = DriverManager.getConnection(url, user, pass);
            
        } catch (Exception e) {
            System.err.println("Koneksi gagal: " + e.getMessage());
            JOptionPane.showMessageDialog(null, 
                "Koneksi Database Gagal!\nPastikan Laragon/MySQL sudah menyala.", 
                "Error Database", JOptionPane.ERROR_MESSAGE);
        }
        return mysqlconfig;
    }
    
    // Auto Increment ID Generator
    public static String generateID(String table, String prefix, String column) {
        String id = prefix + "001";
        try (Connection conn = getKoneksi()) {
            String sql = "SELECT MAX(RIGHT(" + column + ", 3)) FROM " + table + " WHERE " + column + " LIKE '" + prefix + "%'";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            if (rs.next() && rs.getString(1) != null) {
                int max = Integer.parseInt(rs.getString(1)) + 1;
                id = prefix + String.format("%03d", max);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
}