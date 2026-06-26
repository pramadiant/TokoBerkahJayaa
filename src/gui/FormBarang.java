package gui;

import config.Koneksi;
import config.AppStyle;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class FormBarang extends JPanel {

    private JTextField txtId, txtNama, txtHargaBeli, txtHarga, txtStok;
    private JComboBox<String> cbKategori, cbSatuan;
    private JTable tabelBarang;
    private DefaultTableModel modelTabel;
    private JButton btnSimpan, btnEdit, btnHapus, btnClear, btnTambahKategori;

    public FormBarang() {
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
        JLabel lblJudulHalaman = new JLabel("MANAJEMEN DATA BARANG");
        lblJudulHalaman.setFont(AppStyle.FONT_TITLE);
        lblJudulHalaman.setForeground(AppStyle.TEXT_NORMAL);
        pnlHeader.add(lblJudulHalaman);

        // --- CENTRAL CARD CONTAINER ---
        JPanel pnlMainCard = new JPanel(new BorderLayout());

        // --- BAGIAN KIRI: FORM INPUT ---
        JPanel pnlKiri = new JPanel(new BorderLayout(0, 10));
        pnlKiri.setPreferredSize(new Dimension(380, 0));

        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setBorder(null);

        GridBagConstraints gbc = new GridBagConstraints();

        txtId = new JTextField(); txtId.setEditable(false);
        AppStyle.addFormField(pnlForm, gbc, 0, "ID Barang:", txtId);

        txtNama = new JTextField();
        AppStyle.addFormField(pnlForm, gbc, 2, "Nama Barang:", txtNama);

        // Kategori
        gbc.gridy = 4; gbc.insets = new Insets(10, 0, 5, 0);
        pnlForm.add(new JLabel("Kategori:"), gbc);
        JPanel pnlKategori = new JPanel(new BorderLayout(5, 0));
        pnlKategori.setOpaque(false);
        cbKategori = new JComboBox<>();
        AppStyle.styleInput(cbKategori);
        btnTambahKategori = new JButton("+");
        btnTambahKategori.setBackground(AppStyle.BTN_PRIMARY);
        btnTambahKategori.setForeground(Color.WHITE);
        pnlKategori.add(cbKategori, BorderLayout.CENTER);
        pnlKategori.add(btnTambahKategori, BorderLayout.EAST);
        gbc.gridy = 5; gbc.insets = new Insets(0, 0, 10, 0);
        pnlForm.add(pnlKategori, gbc);

        cbSatuan = new JComboBox<>(new String[]{"Pcs", "Box", "Kg", "Liter", "Bungkus", "Botol"});
        cbSatuan.setEditable(true); // Bisa tambah satuan
        AppStyle.addFormField(pnlForm, gbc, 6, "Satuan:", cbSatuan);

        txtHargaBeli = new JTextField();
        txtHargaBeli.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) { AppStyle.validateNumberInput(e); }
        });
        AppStyle.addFormField(pnlForm, gbc, 8, "Harga Beli / Modal (Rp):", txtHargaBeli);

        txtHarga = new JTextField();
        txtHarga.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) { AppStyle.validateNumberInput(e); }
        });
        AppStyle.addFormField(pnlForm, gbc, 10, "Harga Jual (Rp):", txtHarga);

        txtStok = new JTextField();
        txtStok.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) { AppStyle.validateNumberInput(e); }
        });
        AppStyle.addFormField(pnlForm, gbc, 12, "Stok Awal:", txtStok);

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

        // --- BAGIAN KANAN: TABEL ---
        JPanel pnlKanan = new JPanel(new BorderLayout());

        modelTabel = new DefaultTableModel(new String[]{"ID", "Kategori", "Nama Barang", "Satuan", "Harga Beli", "Harga Jual", "Stok"}, 0){
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelBarang = new JTable(modelTabel);
        AppStyle.styleTable(tabelBarang);
        
        JScrollPane scrollPane = new JScrollPane(tabelBarang);
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

        loadKategori();
        loadDataTabel();
        clearForm(); // Init ID

        // --- EVENTS ---
        btnTambahKategori.addActionListener(e -> tambahKategoriBaru());
        btnSimpan.addActionListener(e -> simpanData());
        btnEdit.addActionListener(e -> editData());
        btnHapus.addActionListener(e -> hapusData());
        btnClear.addActionListener(e -> clearForm());
        
        tabelBarang.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = tabelBarang.getSelectedRow();
                if(r != -1){
                    txtId.setText(tabelBarang.getValueAt(r, 0).toString());
                    String catName = tabelBarang.getValueAt(r, 1).toString();
                    for(int i=0; i<cbKategori.getItemCount(); i++){
                        if(cbKategori.getItemAt(i).contains(catName)) cbKategori.setSelectedIndex(i);
                    }
                    txtNama.setText(tabelBarang.getValueAt(r, 2).toString());
                    cbSatuan.setSelectedItem(tabelBarang.getValueAt(r, 3).toString());
                    txtHargaBeli.setText(AppStyle.cleanRupiah(tabelBarang.getValueAt(r, 4).toString()));
                    txtHarga.setText(AppStyle.cleanRupiah(tabelBarang.getValueAt(r, 5).toString()));
                    txtStok.setText(tabelBarang.getValueAt(r, 6).toString());
                }
            }
        });
    }

    private void tambahKategoriBaru() {
        String namaKategoriBaru = JOptionPane.showInputDialog(this, "Masukkan Nama Kategori Baru:");
        if(namaKategoriBaru != null && !namaKategoriBaru.trim().isEmpty()) {
            try (Connection conn = Koneksi.getKoneksi()) {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO tb_kategori (nama_kategori) VALUES (?)");
                ps.setString(1, namaKategoriBaru.trim());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Kategori berhasil ditambahkan!");
                loadKategori();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal: " + ex.getMessage());
            }
        }
    }

    private void loadKategori() {
        cbKategori.removeAllItems();
        cbKategori.addItem("-- Pilih --");
        try (Connection conn = Koneksi.getKoneksi()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT id_kategori, nama_kategori FROM tb_kategori");
            while (rs.next()) cbKategori.addItem(rs.getInt(1) + " - " + rs.getString(2));
        } catch (Exception e) {}
    }

    private void loadDataTabel() {
        modelTabel.setRowCount(0);
        try (Connection conn = Koneksi.getKoneksi()) {
            String sql = "SELECT b.id_barang, k.nama_kategori, b.nama_barang, b.satuan, b.harga_beli, b.harga_jual, b.stok " +
                         "FROM tb_barang b JOIN tb_kategori k ON b.id_kategori = k.id_kategori";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) modelTabel.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), AppStyle.formatRupiah(rs.getDouble(5)), AppStyle.formatRupiah(rs.getDouble(6)), rs.getString(7)});
        } catch (Exception e) {}
    }

    private void simpanData() {
        if(cbKategori.getSelectedIndex() == 0 || txtNama.getText().isEmpty() || txtHargaBeli.getText().isEmpty() || txtHarga.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mohon lengkapi data!"); return;
        }
        try (Connection conn = Koneksi.getKoneksi()) {
            String sql = "INSERT INTO tb_barang VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, txtId.getText());
            pst.setInt(2, Integer.parseInt(cbKategori.getSelectedItem().toString().split(" - ")[0]));
            pst.setString(3, txtNama.getText());
            pst.setString(4, cbSatuan.getSelectedItem().toString());
            pst.setDouble(5, Double.parseDouble(txtHargaBeli.getText()));
            pst.setDouble(6, Double.parseDouble(txtHarga.getText()));
            pst.setInt(7, Integer.parseInt(txtStok.getText()));
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Tersimpan!");
            clearForm(); loadDataTabel();
        } catch (Exception e) { JOptionPane.showMessageDialog(this, e.getMessage()); }
    }

    private void editData() {
        if(cbKategori.getSelectedIndex() == 0 || txtNama.getText().isEmpty() || txtHargaBeli.getText().isEmpty() || txtHarga.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mohon lengkapi data!"); return;
        }
        try (Connection conn = Koneksi.getKoneksi()) {
            String sql = "UPDATE tb_barang SET id_kategori=?, nama_barang=?, satuan=?, harga_beli=?, harga_jual=?, stok=? WHERE id_barang=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(cbKategori.getSelectedItem().toString().split(" - ")[0]));
            pst.setString(2, txtNama.getText());
            pst.setString(3, cbSatuan.getSelectedItem().toString());
            pst.setDouble(4, Double.parseDouble(txtHargaBeli.getText()));
            pst.setDouble(5, Double.parseDouble(txtHarga.getText()));
            pst.setInt(6, Integer.parseInt(txtStok.getText()));
            pst.setString(7, txtId.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Diubah!");
            clearForm(); loadDataTabel();
        } catch (Exception e) { JOptionPane.showMessageDialog(this, e.getMessage()); }
    }

    private void hapusData() {
        if(JOptionPane.showConfirmDialog(this, "Hapus data ini?") == 0){
            try (Connection conn = Koneksi.getKoneksi()) {
                PreparedStatement pst = conn.prepareStatement("DELETE FROM tb_barang WHERE id_barang=?");
                pst.setString(1, txtId.getText());
                pst.executeUpdate();
                clearForm(); loadDataTabel();
            } catch (Exception e) { JOptionPane.showMessageDialog(this, "Gagal hapus!"); }
        }
    }

    private void clearForm() {
        txtId.setText(Koneksi.generateID("tb_barang", "BRG", "id_barang"));
        if(cbKategori.getItemCount() > 0) cbKategori.setSelectedIndex(0);
        txtNama.setText(""); 
        cbSatuan.setSelectedIndex(0); 
        txtHargaBeli.setText(""); 
        txtHarga.setText(""); 
        txtStok.setText("");
    }
}