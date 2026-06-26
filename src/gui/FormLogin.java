package gui;

import config.Koneksi;
import config.AppStyle;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class FormLogin extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnBatal;

    public FormLogin() {
        setTitle("Login - Toko Berkah Jaya");
        setSize(700, 450); // Diperlebar
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);
        
        JPanel panelUtama = new JPanel(new BorderLayout());
        panelUtama.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        setContentPane(panelUtama);

        // --- BAGIAN KIRI (Branding) ---
        JPanel pnlKiri = new JPanel(new GridBagLayout());
        pnlKiri.setBackground(AppStyle.BG_HEADER);
        pnlKiri.setPreferredSize(new Dimension(300, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx=0; gbc.gridy=0; gbc.insets = new Insets(10, 10, 10, 10);
        
        JLabel lblIkon;
        try {
            ImageIcon scaledImg = config.AppStyle.getScaledIcon("/resources/images/Logo_Pramadiant_Store.png", 150, 150);
            if (scaledImg != null) {
                lblIkon = new JLabel(scaledImg);
            } else {
                lblIkon = new JLabel("\uD83D\uDED2");
                lblIkon.setFont(new Font("Segoe UI", Font.PLAIN, 72));
                lblIkon.setForeground(Color.WHITE);
            }
        } catch (Exception e) {
            lblIkon = new JLabel("\uD83D\uDED2");
            lblIkon.setFont(new Font("Segoe UI", Font.PLAIN, 72));
            lblIkon.setForeground(Color.WHITE);
        }
        pnlKiri.add(lblIkon, gbc);
        
        gbc.gridy=1;
        JLabel lblToko = new JLabel("Toko Berkah Jaya");
        lblToko.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblToko.setForeground(Color.WHITE);
        pnlKiri.add(lblToko, gbc);
        
        gbc.gridy=2;
        JLabel lblSistem = new JLabel("by Pramadiant");
        lblSistem.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblSistem.setForeground(new Color(220, 220, 220));
        pnlKiri.add(lblSistem, gbc);
        
        panelUtama.add(pnlKiri, BorderLayout.WEST);

        // --- BAGIAN KANAN (Form) ---
        JPanel pnlKanan = new JPanel(new BorderLayout());
        pnlKanan.setBackground(Color.WHITE);
        pnlKanan.setBorder(new EmptyBorder(50, 40, 50, 40));
        
        JLabel lblJudul = new JLabel("Silakan Login");
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblJudul.setForeground(AppStyle.TEXT_NORMAL);
        lblJudul.setBorder(new EmptyBorder(0, 0, 30, 0));
        pnlKanan.add(lblJudul, BorderLayout.NORTH);
        
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBackground(Color.WHITE);
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL; gc.weightx = 1.0;

        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        txtPassword.putClientProperty("JTextField.showRevealButton", true);
        txtPassword.putClientProperty("FlatClientProperties.style", "showRevealButton: true");
        
        AppStyle.addFormField(pnlForm, gc, 0, "Username", txtUsername);
        AppStyle.addFormField(pnlForm, gc, 2, "Password", txtPassword);
        
        pnlKanan.add(pnlForm, BorderLayout.CENTER);

        // --- BUTTONS ---
        JPanel pnlTombol = new JPanel(new GridLayout(1, 2, 15, 0));
        pnlTombol.setBackground(Color.WHITE);
        pnlTombol.setBorder(new EmptyBorder(30, 0, 0, 0));

        btnLogin = AppStyle.createButton("LOGIN", AppStyle.BTN_PRIMARY);
        btnBatal = AppStyle.createButton("KELUAR", AppStyle.BTN_DANGER);
        pnlTombol.add(btnLogin);
        pnlTombol.add(btnBatal);
        
        pnlKanan.add(pnlTombol, BorderLayout.SOUTH);
        panelUtama.add(pnlKanan, BorderLayout.CENTER);

        // --- EVENTS ---
        btnLogin.addActionListener(this::prosesLogin);
        txtPassword.addActionListener(this::prosesLogin);
        btnBatal.addActionListener(e -> System.exit(0));
    }

    private void prosesLogin(ActionEvent e) {
        String user = txtUsername.getText();
        String pass = new String(txtPassword.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Isi username & password dulu bos!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = Koneksi.getKoneksi()) {
            String sql = "SELECT * FROM tb_user WHERE username=? AND password=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, user);
            pst.setString(2, pass);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                new FormMenuUtama(rs.getInt("id_user"), rs.getString("nama_lengkap"), rs.getString("level")).setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Akun nggak ketemu, cek lagi deh!", "Gagal", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try { 
            // 1. Tweak settingan FlatLaf agar bulat (rounded) seperti web UI
            UIManager.put("Component.arc", 16);
            UIManager.put("Button.arc", 16);
            UIManager.put("TextComponent.arc", 16);
            UIManager.put("Component.focusWidth", 2);
            UIManager.put("Component.innerFocusWidth", 0);
            
            // 2. Tweak warna biru primer
            UIManager.put("Component.focusColor", java.awt.Color.decode("#4b2464"));
            
            // 3. Terapkan FlatLaf Mac Light Theme
            com.formdev.flatlaf.themes.FlatMacLightLaf.setup();
        } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new FormLogin().setVisible(true));
    }
}