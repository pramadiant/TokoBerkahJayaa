package gui;

import config.Koneksi;
import config.AppStyle;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class FormCustomer extends JPanel {

    private JTextField txtId, txtNama, txtTelepon;
    private JTextArea txtAlamat;
    private JTable tabelCustomer;
    private DefaultTableModel modelTabel;
    private JButton btnSimpan, btnEdit, btnHapus, btnClear;

    public FormCustomer() {
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
        JLabel lblJudul = new JLabel("MANAJEMEN DATA CUSTOMER");
        lblJudul.setFont(AppStyle.FONT_TITLE);
        lblJudul.setForeground(AppStyle.TEXT_NORMAL);
        pnlHeader.add(lblJudul);

        // --- CENTRAL CARD CONTAINER ---
        JPanel pnlMainCard = new JPanel(new BorderLayout());

        // --- FORM INPUT KIRI ---
        JPanel pnlKiri = new JPanel(new BorderLayout(0, 10));
        pnlKiri.setPreferredSize(new Dimension(380, 0));

        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setBorder(null);

        GridBagConstraints gbc = new GridBagConstraints();
        
        txtId = new JTextField(); 
        AppStyle.addFormField(pnlForm, gbc, 0, "ID Customer:", txtId);

        txtNama = new JTextField();
        txtNama.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) { AppStyle.validateNameInput(e); }
        });
        AppStyle.addFormField(pnlForm, gbc, 2, "Nama Lengkap (Hanya Huruf):", txtNama);

        txtTelepon = new JTextField();
        txtTelepon.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) { AppStyle.validateNumberInput(e); }
        });
        AppStyle.addFormField(pnlForm, gbc, 4, "Nomor Telepon:", txtTelepon);

        // Alamat khusus karena JTextArea
        gbc.gridy = 6; gbc.insets = new Insets(10, 0, 5, 0);
        pnlForm.add(new JLabel("Alamat:"), gbc);
        txtAlamat = new JTextArea(4, 20);
        txtAlamat.setFont(AppStyle.FONT_INPUT);
        txtAlamat.setLineWrap(true);
        txtAlamat.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#e2e8f0")),
            new javax.swing.border.EmptyBorder(5, 5, 5, 5)
        ));
        gbc.gridy = 7; gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 1.0;
        pnlForm.add(new JScrollPane(txtAlamat), gbc);

        // Wrap di JScrollPane supaya tidak kepotong ke bawah
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
        
        modelTabel = new DefaultTableModel(new String[]{"ID", "Nama", "Alamat", "Telepon"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelCustomer = new JTable(modelTabel);
        AppStyle.styleTable(tabelCustomer);
        
        JScrollPane scrollPane = new JScrollPane(tabelCustomer);
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
        clear(); // Untuk init ID otomatis

        // --- EVENTS ---
        btnSimpan.addActionListener(e -> simpan());
        btnEdit.addActionListener(e -> edit());
        btnHapus.addActionListener(e -> hapus());
        btnClear.addActionListener(e -> clear());
        
        tabelCustomer.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = tabelCustomer.getSelectedRow();
                if(r != -1) {
                    txtId.setText(tabelCustomer.getValueAt(r, 0).toString());
                    txtId.setEditable(false);
                    txtNama.setText(tabelCustomer.getValueAt(r, 1).toString());
                    txtAlamat.setText(tabelCustomer.getValueAt(r, 2).toString());
                    txtTelepon.setText(tabelCustomer.getValueAt(r, 3).toString());
                }
            }
        });
    }

    private void loadDataTabel() {
        modelTabel.setRowCount(0);
        try (Connection conn = Koneksi.getKoneksi()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM tb_customer");
            while(rs.next()) modelTabel.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)});
        } catch (Exception e) {}
    }

    private void simpan() {
        if(txtNama.getText().isEmpty()) return;
        try (Connection conn = Koneksi.getKoneksi()) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO tb_customer VALUES (?, ?, ?, ?)");
            ps.setString(1, txtId.getText());
            ps.setString(2, txtNama.getText());
            ps.setString(3, txtAlamat.getText());
            ps.setString(4, txtTelepon.getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Berhasil simpan!");
            clear(); loadDataTabel();
        } catch (Exception e) { JOptionPane.showMessageDialog(this, e.getMessage()); }
    }

    private void edit() {
        try (Connection conn = Koneksi.getKoneksi()) {
            PreparedStatement ps = conn.prepareStatement("UPDATE tb_customer SET nama_customer=?, alamat=?, telepon=? WHERE id_customer=?");
            ps.setString(1, txtNama.getText());
            ps.setString(2, txtAlamat.getText());
            ps.setString(3, txtTelepon.getText());
            ps.setString(4, txtId.getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Berhasil perbarui!");
            clear(); loadDataTabel();
        } catch (Exception e) {}
    }

    private void hapus() {
        if(JOptionPane.showConfirmDialog(this, "Hapus?", "Hapus", JOptionPane.YES_NO_OPTION) == 0) {
            try (Connection conn = Koneksi.getKoneksi()) {
                PreparedStatement ps = conn.prepareStatement("DELETE FROM tb_customer WHERE id_customer=?");
                ps.setString(1, txtId.getText());
                ps.executeUpdate();
                clear(); loadDataTabel();
            } catch (Exception e) {}
        }
    }

    private void clear() {
        // ID OTOMATIS
        txtId.setText(Koneksi.generateID("tb_customer", "CUST", "id_customer"));
        txtId.setEditable(false);
        txtNama.setText(""); txtAlamat.setText(""); txtTelepon.setText("");
    }
}