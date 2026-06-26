package gui;

import config.Koneksi;
import config.AppStyle;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class FormUser extends JPanel {

    private JTextField txtId, txtNama, txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cbLevel;
    private JTable tabelUser;
    private DefaultTableModel modelTabel;
    private JButton btnSimpan, btnEdit, btnHapus, btnClear;

    public FormUser() {
        setLayout(new BorderLayout(0, 0));
        setBackground(AppStyle.BG_PRIMARY);
        setBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0));

        // --- HEADER ---
        JPanel pnlHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlHeader.setPreferredSize(new Dimension(0, 75));
        pnlHeader.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#cbd5e1")),
            BorderFactory.createEmptyBorder(0, 0, 15, 0)
        ));
        JLabel lblJudul = new JLabel("MANAJEMEN PENGGUNA (USER)");
        lblJudul.setFont(AppStyle.FONT_TITLE);
        lblJudul.setForeground(AppStyle.TEXT_NORMAL);
        pnlHeader.add(lblJudul);

        // --- CENTRAL CARD CONTAINER ---
        JPanel pnlMainCard = new JPanel(new BorderLayout());

        // --- FORM INPUT ---
        JPanel pnlKiri = new JPanel(new BorderLayout(0, 10));
        pnlKiri.setPreferredSize(new Dimension(380, 0));

        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setBorder(null);

        GridBagConstraints gbc = new GridBagConstraints();

        txtId = new JTextField(); txtId.setEditable(false);
        AppStyle.addFormField(pnlForm, gbc, 0, "ID User:", txtId);

        txtNama = new JTextField();
        txtNama.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) { AppStyle.validateNameInput(e); }
        });
        AppStyle.addFormField(pnlForm, gbc, 2, "Nama Lengkap (Hanya Huruf):", txtNama);

        txtUsername = new JTextField();
        txtUsername.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) { AppStyle.validateAlphanumericInput(e); }
        });
        AppStyle.addFormField(pnlForm, gbc, 4, "Username:", txtUsername);

        txtPassword = new JPasswordField();
        txtPassword.putClientProperty("JTextField.showRevealButton", true);
        txtPassword.putClientProperty("FlatClientProperties.style", "showRevealButton: true");
        AppStyle.addFormField(pnlForm, gbc, 6, "Password (Kosongkan bila tidak diubah):", txtPassword);

        cbLevel = new JComboBox<>(new String[]{"Admin", "Petugas"});
        AppStyle.addFormField(pnlForm, gbc, 8, "Hak Akses (Level):", cbLevel);

        // Bungkus form
        JScrollPane scrollForm = new JScrollPane(pnlForm);
        scrollForm.setBorder(BorderFactory.createEmptyBorder());
        scrollForm.setOpaque(false); scrollForm.getViewport().setOpaque(false);
        pnlKiri.add(scrollForm, BorderLayout.CENTER);

        // --- TOMBOL ---
        JPanel pnlTombol = new JPanel(new GridLayout(2, 2, 10, 10));
        pnlTombol.setBackground(Color.WHITE);
        pnlTombol.setBorder(new javax.swing.border.EmptyBorder(10, 0, 0, 0));
        btnSimpan = AppStyle.createButton("Simpan", AppStyle.BTN_SUCCESS);
        btnEdit = AppStyle.createButton("Edit", AppStyle.BTN_WARNING);
        btnHapus = AppStyle.createButton("Hapus", AppStyle.BTN_DANGER);
        btnClear = AppStyle.createButton("Clear", AppStyle.BTN_PRIMARY);

        pnlTombol.add(btnSimpan); pnlTombol.add(btnEdit);
        pnlTombol.add(btnHapus); pnlTombol.add(btnClear);
        pnlKiri.add(pnlTombol, BorderLayout.SOUTH);

        // --- TABEL ---
        JPanel pnlKanan = new JPanel(new BorderLayout());
        
        modelTabel = new DefaultTableModel(new String[]{"ID", "Nama Lengkap", "Username", "Level"}, 0){
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelUser = new JTable(modelTabel);
        AppStyle.styleTable(tabelUser);
        
        JScrollPane scrollPane = new JScrollPane(tabelUser);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        pnlKanan.add(scrollPane, BorderLayout.CENTER);
        
        // Terapkan gaya kartu terpadu
        AppStyle.styleUnifiedCard(pnlMainCard, pnlHeader, pnlKiri, pnlKanan);
        
        JPanel pnlBody = new JPanel(new BorderLayout());
        pnlBody.setOpaque(false);
        pnlBody.add(pnlKiri, BorderLayout.WEST);
        pnlBody.add(pnlKanan, BorderLayout.CENTER);
        
        add(pnlHeader, BorderLayout.NORTH);
        pnlMainCard.add(pnlBody, BorderLayout.CENTER);
        
        JPanel pnlContainer = new JPanel(new BorderLayout());
        pnlContainer.setOpaque(false);
        pnlContainer.setBorder(new javax.swing.border.EmptyBorder(20, 20, 20, 20));
        pnlContainer.add(pnlMainCard, BorderLayout.CENTER);
        add(pnlContainer, BorderLayout.CENTER);

        loadDataTabel();

        // --- EVENTS ---
        btnSimpan.addActionListener(e -> simpan());
        btnEdit.addActionListener(e -> edit());
        btnHapus.addActionListener(e -> hapus());
        btnClear.addActionListener(e -> clear());
        
        tabelUser.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = tabelUser.getSelectedRow();
                if(r != -1) {
                    txtId.setText(tabelUser.getValueAt(r, 0).toString());
                    txtNama.setText(tabelUser.getValueAt(r, 1).toString());
                    txtUsername.setText(tabelUser.getValueAt(r, 2).toString());
                    txtPassword.setText(""); // Jangan tampilkan password
                    cbLevel.setSelectedItem(tabelUser.getValueAt(r, 3).toString());
                }
            }
        });
    }

    private void loadDataTabel() {
        modelTabel.setRowCount(0);
        try (Connection conn = Koneksi.getKoneksi()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT id_user, nama_lengkap, username, level FROM tb_user");
            while(rs.next()) {
                modelTabel.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)});
            }
        } catch (Exception e) {}
    }

    private void simpan() {
        if(txtNama.getText().isEmpty() || txtUsername.getText().isEmpty() || txtPassword.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this, "Isi form dengan lengkap!"); return;
        }
        try (Connection conn = Koneksi.getKoneksi()) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO tb_user (nama_lengkap, username, password, level) VALUES (?, ?, ?, ?)");
            ps.setString(1, txtNama.getText());
            ps.setString(2, txtUsername.getText());
            ps.setString(3, new String(txtPassword.getPassword()));
            ps.setString(4, cbLevel.getSelectedItem().toString());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Berhasil simpan user baru!");
            clear(); loadDataTabel();
        } catch (Exception e) { JOptionPane.showMessageDialog(this, "Gagal: " + e.getMessage()); }
    }

    private void edit() {
        if(txtId.getText().isEmpty()) return;
        try (Connection conn = Koneksi.getKoneksi()) {
            String pass = new String(txtPassword.getPassword());
            String sql = pass.isEmpty() ? 
                "UPDATE tb_user SET nama_lengkap=?, username=?, level=? WHERE id_user=?" :
                "UPDATE tb_user SET nama_lengkap=?, username=?, level=?, password=? WHERE id_user=?";
                
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtNama.getText());
            ps.setString(2, txtUsername.getText());
            ps.setString(3, cbLevel.getSelectedItem().toString());
            if(pass.isEmpty()) {
                ps.setString(4, txtId.getText());
            } else {
                ps.setString(4, pass);
                ps.setString(5, txtId.getText());
            }
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Berhasil perbarui user!");
            clear(); loadDataTabel();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void hapus() {
        if(txtId.getText().isEmpty()) return;
        if(JOptionPane.showConfirmDialog(this, "Hapus user ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION) == 0) {
            try (Connection conn = Koneksi.getKoneksi()) {
                PreparedStatement ps = conn.prepareStatement("DELETE FROM tb_user WHERE id_user=?");
                ps.setString(1, txtId.getText());
                ps.executeUpdate();
                clear(); loadDataTabel();
            } catch (Exception e) { JOptionPane.showMessageDialog(this, "Gagal hapus!"); }
        }
    }

    private void clear() {
        txtId.setText("");
        txtNama.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
        cbLevel.setSelectedIndex(0);
    }
}
