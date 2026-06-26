package gui;

import config.Koneksi;
import config.AppStyle;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

public class FormTransaksi extends JPanel {

    private JComboBox<String> cbCustomer;
    private JTextField txtCariCustomer, txtBayar, txtKembalian;
    private JLabel lblTotalBesar;
    private JButton btnCariCustomer, btnProses;
    private int idUserLogin; 
    private double totalBelanja = 0;
    
    // Grid & Cart panels
    private JPanel pnlKategoriPills;
    private JPanel pnlProdukGrid;
    private JPanel pnlCartList;
    
    // State
    private String activeKategoriId = null;
    private ArrayList<CartItem> listKeranjang = new ArrayList<>();

    // Helper static class to hold Cart state
    private static class CartItem {
        String idBarang;
        String namaBarang;
        double harga;
        int qty;
        int stokTersedia;
        
        CartItem(String id, String nama, double harga, int qty, int stok) {
            this.idBarang = id;
            this.namaBarang = nama;
            this.harga = harga;
            this.qty = qty;
            this.stokTersedia = stok;
        }
        
        double getSubtotal() {
            return harga * qty;
        }
    }

    public FormTransaksi(int idUserLogin) {
        this.idUserLogin = idUserLogin;
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
        JLabel lblJudul = new JLabel("MESIN KASIR - PENJUALAN");
        lblJudul.setFont(AppStyle.FONT_TITLE);
        lblJudul.setForeground(AppStyle.TEXT_NORMAL);
        pnlHeader.add(lblJudul);

        // --- CENTRAL CARD CONTAINER ---
        JPanel pnlMainCard = new JPanel(new BorderLayout());

        // --- BAGIAN KIRI: GRID PRODUK ---
        JPanel pnlKiri = new JPanel(new BorderLayout(10, 15));
        
        // Pills Kategori
        pnlKategoriPills = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlKategoriPills.setOpaque(false);
        pnlKiri.add(pnlKategoriPills, BorderLayout.NORTH);
        
        // Grid Produk Scroll
        pnlProdukGrid = new JPanel(new GridLayout(0, 3, 12, 12));
        pnlProdukGrid.setOpaque(false);
        
        JPanel gridWrapper = new JPanel(new BorderLayout());
        gridWrapper.setOpaque(false);
        gridWrapper.add(pnlProdukGrid, BorderLayout.NORTH);
        
        JScrollPane scrollGrid = new JScrollPane(gridWrapper);
        scrollGrid.setBorder(BorderFactory.createEmptyBorder());
        scrollGrid.setOpaque(false); scrollGrid.getViewport().setOpaque(false);
        pnlKiri.add(scrollGrid, BorderLayout.CENTER);

        // --- BAGIAN KANAN: DETAIL TRANSAKSI / CART ---
        JPanel pnlKanan = new JPanel(new BorderLayout(0, 10));
        pnlKanan.setPreferredSize(new Dimension(380, 0));
        
        // Cart Header (Pilih Customer)
        JPanel pnlCartHeader = new JPanel(new GridBagLayout());
        pnlCartHeader.setBackground(Color.WHITE);
        GridBagConstraints gbcH = new GridBagConstraints();
        gbcH.fill = GridBagConstraints.HORIZONTAL; gbcH.weightx = 1.0;
        
        JLabel lblCartTitle = new JLabel("Detail Transaksi");
        lblCartTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblCartTitle.setForeground(AppStyle.TEXT_NORMAL);
        gbcH.gridy = 0; gbcH.insets = new Insets(0, 0, 10, 0);
        pnlCartHeader.add(lblCartTitle, gbcH);
        
        // Cari Customer
        JPanel pnlCari = new JPanel(new BorderLayout(5, 0));
        pnlCari.setOpaque(false);
        txtCariCustomer = new JTextField(); AppStyle.styleInput(txtCariCustomer);
        txtCariCustomer.putClientProperty("JTextField.placeholderText", "Cari Customer...");
        btnCariCustomer = AppStyle.createButton("Cari", AppStyle.BTN_PRIMARY);
        btnCariCustomer.setPreferredSize(new Dimension(70, 32));
        pnlCari.add(txtCariCustomer, BorderLayout.CENTER);
        pnlCari.add(btnCariCustomer, BorderLayout.EAST);
        
        gbcH.gridy = 1; gbcH.insets = new Insets(0, 0, 5, 0);
        pnlCartHeader.add(pnlCari, gbcH);

        cbCustomer = new JComboBox<>();
        AppStyle.styleInput(cbCustomer);
        gbcH.gridy = 2; gbcH.insets = new Insets(0, 0, 10, 0);
        pnlCartHeader.add(cbCustomer, gbcH);
        pnlKanan.add(pnlCartHeader, BorderLayout.NORTH);

        // Cart List Scroll
        pnlCartList = new JPanel();
        pnlCartList.setBackground(Color.WHITE);
        pnlCartList.setLayout(new BoxLayout(pnlCartList, BoxLayout.Y_AXIS));
        JScrollPane scrollCart = new JScrollPane(pnlCartList);
        scrollCart.setBorder(BorderFactory.createEmptyBorder());
        scrollCart.getViewport().setBackground(Color.WHITE);
        pnlKanan.add(scrollCart, BorderLayout.CENTER);

        // Panel Checkout Bawah
        JPanel pnlCheckout = new JPanel(new BorderLayout(15, 0));
        pnlCheckout.setBackground(Color.WHITE);
        pnlCheckout.setBorder(new javax.swing.border.EmptyBorder(15, 0, 0, 0));

        // Total Belanja Screen Box (Kasir look)
        JPanel pnlTotBox = new JPanel(new BorderLayout());
        pnlTotBox.setBackground(Color.decode("#0f172a")); // Deep Slate
        pnlTotBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#1e293b"), 1, true),
            new javax.swing.border.EmptyBorder(8, 15, 8, 15)
        ));
        
        JLabel lblT = new JLabel("TOTAL BAYAR");
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblT.setForeground(Color.decode("#94a3b8")); // Slate 400
        
        lblTotalBesar = new JLabel("Rp 0");
        lblTotalBesar.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTotalBesar.setForeground(Color.decode("#34d399")); // Mint Green
        
        pnlTotBox.add(lblT, BorderLayout.NORTH);
        pnlTotBox.add(lblTotalBesar, BorderLayout.CENTER);
        pnlCheckout.add(pnlTotBox, BorderLayout.NORTH);

        // Grid Input Pembayaran
        JPanel pnlBayar = new JPanel(new GridBagLayout());
        pnlBayar.setOpaque(false);
        GridBagConstraints gbcB = new GridBagConstraints();
        gbcB.fill = GridBagConstraints.HORIZONTAL; gbcB.weightx = 1.0;
        gbcB.insets = new Insets(8, 0, 4, 0);
        
        gbcB.gridy = 0;
        pnlBayar.add(new JLabel("Tunai / Bayar:"), gbcB);
        
        gbcB.gridy = 1; gbcB.insets = new Insets(0, 0, 8, 0);
        txtBayar = new JTextField(); 
        txtBayar.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) { AppStyle.validateNumberInput(e); }
            public void keyReleased(KeyEvent e) { hitungKembalian(); }
        });
        AppStyle.styleInput(txtBayar);
        txtBayar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        pnlBayar.add(txtBayar, gbcB);
        
        gbcB.gridy = 2; gbcB.insets = new Insets(0, 0, 4, 0);
        pnlBayar.add(new JLabel("Kembalian:"), gbcB);
        
        gbcB.gridy = 3; gbcB.insets = new Insets(0, 0, 15, 0);
        txtKembalian = new JTextField(); 
        txtKembalian.setEditable(false); 
        AppStyle.styleInput(txtKembalian);
        txtKembalian.setFont(new Font("Segoe UI", Font.BOLD, 16));
        txtKembalian.setForeground(AppStyle.BTN_SUCCESS);
        pnlBayar.add(txtKembalian, gbcB);
        
        btnProses = AppStyle.createButton("PROSES CHECKOUT", AppStyle.BTN_SUCCESS);
        btnProses.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnProses.setPreferredSize(new Dimension(0, 45));
        gbcB.gridy = 4; gbcB.insets = new Insets(0, 0, 0, 0);
        pnlBayar.add(btnProses, gbcB);
        
        pnlCheckout.add(pnlBayar, BorderLayout.SOUTH);
        pnlKanan.add(pnlCheckout, BorderLayout.SOUTH);
        
        // Terapkan gaya kartu terpadu
        AppStyle.styleUnifiedCard(pnlMainCard, pnlHeader, pnlKiri, pnlKanan);
        pnlKiri.setBackground(AppStyle.BG_PRIMARY); // Agar kartu produk putih lebih menonjol (kontras tinggi)
        
        JPanel pnlBody = new JPanel(new BorderLayout());
        pnlBody.setOpaque(false);
        pnlBody.add(pnlKiri, BorderLayout.CENTER);
        pnlBody.add(pnlKanan, BorderLayout.EAST);
        
        add(pnlHeader, BorderLayout.NORTH);
        pnlMainCard.add(pnlBody, BorderLayout.CENTER);
        
        JPanel pnlContainer = new JPanel(new BorderLayout());
        pnlContainer.setOpaque(false);
        pnlContainer.setBorder(new javax.swing.border.EmptyBorder(20, 20, 20, 20));
        pnlContainer.add(pnlMainCard, BorderLayout.CENTER);
        add(pnlContainer, BorderLayout.CENTER);

        // --- INIT DATA ---
        loadCustomer("");
        renderKategoriPills();
        renderProdukGrid(null);

        // --- EVENTS ---
        btnCariCustomer.addActionListener(e -> loadCustomer(txtCariCustomer.getText()));
        btnProses.addActionListener(e -> prosesTransaksi());
    }

    private void loadCustomer(String keyword) {
        cbCustomer.removeAllItems(); cbCustomer.addItem("-- Pilih Customer --");
        try (Connection conn = Koneksi.getKoneksi()) {
            String sql = "SELECT id_customer, nama_customer FROM tb_customer WHERE nama_customer LIKE ? OR id_customer LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%"); ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while(rs.next()) cbCustomer.addItem(rs.getString(1) + " - " + rs.getString(2));
            if(cbCustomer.getItemCount() == 2) cbCustomer.setSelectedIndex(1);
        } catch (Exception e) {}
    }

    private void renderKategoriPills() {
        pnlKategoriPills.removeAll();
        
        // Add "Semua" pill
        JButton btnAll = createPillButton("Semua Produk", activeKategoriId == null);
        btnAll.addActionListener(e -> {
            activeKategoriId = null;
            renderKategoriPills();
            renderProdukGrid(null);
        });
        pnlKategoriPills.add(btnAll);
        pnlKategoriPills.add(Box.createRigidArea(new Dimension(8, 0)));
        
        try (Connection conn = Koneksi.getKoneksi()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT id_kategori, nama_kategori FROM tb_kategori");
            while (rs.next()) {
                String id = rs.getString(1);
                String nama = rs.getString(2);
                boolean isActive = id.equals(activeKategoriId);
                
                JButton btnPill = createPillButton(nama, isActive);
                btnPill.addActionListener(e -> {
                    activeKategoriId = id;
                    renderKategoriPills();
                    renderProdukGrid(id);
                });
                pnlKategoriPills.add(btnPill);
                pnlKategoriPills.add(Box.createRigidArea(new Dimension(8, 0)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        pnlKategoriPills.revalidate();
        pnlKategoriPills.repaint();
    }
    
    private JButton createPillButton(String text, boolean isActive) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMargin(new Insets(6, 14, 6, 14));
        btn.putClientProperty("JButton.buttonType", "roundRect");
        
        if (isActive) {
            btn.setBackground(AppStyle.BTN_PRIMARY);
            btn.setForeground(Color.WHITE);
            btn.putClientProperty("FlatClientProperties.style", 
                "borderColor: #4b2464;" +
                "borderWidth: 1;"
            );
        } else {
            btn.setBackground(Color.decode("#f1f5f9")); 
            btn.setForeground(Color.decode("#475569")); 
            btn.putClientProperty("FlatClientProperties.style", 
                "borderColor: #e2e8f0;" +
                "borderWidth: 1;"
            );
        }
        return btn;
    }

    private void renderProdukGrid(String idKategoriFilter) {
        pnlProdukGrid.removeAll();
        
        try (Connection conn = Koneksi.getKoneksi()) {
            String sql = "SELECT b.id_barang, b.nama_barang, b.harga_jual, b.stok, k.nama_kategori, b.satuan, b.id_kategori " +
                         "FROM tb_barang b JOIN tb_kategori k ON b.id_kategori = k.id_kategori";
            if (idKategoriFilter != null) {
                sql += " WHERE b.id_kategori = ?";
            }
            
            PreparedStatement ps = conn.prepareStatement(sql);
            if (idKategoriFilter != null) {
                ps.setString(1, idKategoriFilter);
            }
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String id = rs.getString("id_barang");
                String nama = rs.getString("nama_barang");
                double harga = rs.getDouble("harga_jual");
                int stok = rs.getInt("stok");
                String namaKategori = rs.getString("nama_kategori");
                String satuan = rs.getString("satuan");
                
                // Card Panel
                JPanel card = new JPanel(new BorderLayout(8, 8));
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.decode("#e2e8f0"), 1, true),
                    BorderFactory.createEmptyBorder(15, 12, 15, 12)
                ));
                
                // Content (Nama, Stock, Harga)
                JPanel pnlContent = new JPanel(new GridLayout(3, 1, 2, 2));
                pnlContent.setOpaque(false);
                pnlContent.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
                
                JLabel lblNama = new JLabel(nama);
                lblNama.setFont(new Font("Segoe UI", Font.BOLD, 13));
                lblNama.setHorizontalAlignment(SwingConstants.CENTER);
                
                JLabel lblStok = new JLabel("Stok: " + stok + " " + satuan);
                lblStok.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                lblStok.setForeground(Color.decode("#64748b"));
                lblStok.setHorizontalAlignment(SwingConstants.CENTER);
                
                JLabel lblHarga = new JLabel(AppStyle.formatRupiah(harga));
                lblHarga.setFont(new Font("Segoe UI", Font.BOLD, 13));
                lblHarga.setForeground(AppStyle.BTN_PRIMARY);
                lblHarga.setHorizontalAlignment(SwingConstants.CENTER);
                
                pnlContent.add(lblNama);
                pnlContent.add(lblStok);
                pnlContent.add(lblHarga);
                card.add(pnlContent, BorderLayout.CENTER);
                
                // Button
                JButton btnAdd = AppStyle.createButton("+ Tambah", AppStyle.BTN_PRIMARY);
                btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 11));
                btnAdd.setPreferredSize(new Dimension(0, 30));
                
                if (stok <= 0) {
                    btnAdd.setText("Stok Habis");
                    btnAdd.setEnabled(false);
                    btnAdd.setBackground(Color.decode("#cbd5e1"));
                } else {
                    btnAdd.addActionListener(e -> {
                        // Check if already in cart
                        CartItem found = null;
                        for (CartItem ci : listKeranjang) {
                            if (ci.idBarang.equals(id)) {
                                found = ci; break;
                            }
                        }
                        
                        if (found != null) {
                            if (found.qty < stok) {
                                found.qty++;
                                renderKeranjang();
                            } else {
                                JOptionPane.showMessageDialog(this, "Stok tidak mencukupi!");
                            }
                        } else {
                            listKeranjang.add(new CartItem(id, nama, harga, 1, stok));
                            renderKeranjang();
                        }
                    });
                }
                card.add(btnAdd, BorderLayout.SOUTH);
                
                pnlProdukGrid.add(card);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        pnlProdukGrid.revalidate();
        pnlProdukGrid.repaint();
    }

    private void renderKeranjang() {
        pnlCartList.removeAll();
        totalBelanja = 0;
        
        for (CartItem item : listKeranjang) {
            totalBelanja += item.getSubtotal();
            
            JPanel pnlItem = new JPanel(new BorderLayout(8, 2));
            pnlItem.setBackground(Color.WHITE);
            pnlItem.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#f1f5f9"), 1, true),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
            ));
            pnlItem.setMaximumSize(new Dimension(Short.MAX_VALUE, 50));
            
            // Kiri: Nama & Subtotal
            JPanel pnlInfo = new JPanel(new GridLayout(2, 1));
            pnlInfo.setOpaque(false);
            JLabel lblNama = new JLabel(item.namaBarang);
            lblNama.setFont(new Font("Segoe UI", Font.BOLD, 12));
            JLabel lblSub = new JLabel(AppStyle.formatRupiah(item.getSubtotal()));
            lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            lblSub.setForeground(Color.decode("#64748b"));
            pnlInfo.add(lblNama);
            pnlInfo.add(lblSub);
            pnlItem.add(pnlInfo, BorderLayout.CENTER);
            
            // Kanan: Control Qty & Delete
            JPanel pnlControl = new JPanel(new FlowLayout(FlowLayout.RIGHT, 3, 0));
            pnlControl.setOpaque(false);
            
            JButton btnMinus = new JButton("-");
            btnMinus.setPreferredSize(new Dimension(22, 22));
            btnMinus.setFont(new Font("Segoe UI", Font.BOLD, 10));
            btnMinus.setMargin(new Insets(0, 0, 0, 0));
            btnMinus.addActionListener(e -> {
                if (item.qty > 1) {
                    item.qty--;
                    renderKeranjang();
                } else {
                    listKeranjang.remove(item);
                    renderKeranjang();
                }
            });
            
            JTextField txtQty = new JTextField(String.valueOf(item.qty));
            txtQty.setFont(new Font("Segoe UI", Font.BOLD, 11));
            txtQty.setHorizontalAlignment(JTextField.CENTER);
            txtQty.setPreferredSize(new Dimension(32, 22));
            txtQty.setBorder(BorderFactory.createLineBorder(Color.decode("#cbd5e1"), 1));
            
            txtQty.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent evt) {
                    AppStyle.validateNumberInput(evt);
                }
            });
            
            txtQty.addActionListener(e -> {
                updateQtyFromText(txtQty, item);
            });
            
            txtQty.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusLost(java.awt.event.FocusEvent e) {
                    updateQtyFromText(txtQty, item);
                }
            });
            
            JButton btnPlus = new JButton("+");
            btnPlus.setPreferredSize(new Dimension(22, 22));
            btnPlus.setFont(new Font("Segoe UI", Font.BOLD, 10));
            btnPlus.setMargin(new Insets(0, 0, 0, 0));
            btnPlus.addActionListener(e -> {
                if (item.qty < item.stokTersedia) {
                    item.qty++;
                    renderKeranjang();
                } else {
                    JOptionPane.showMessageDialog(this, "Stok tidak mencukupi!");
                }
            });
            
            JButton btnDel = new JButton("x");
            btnDel.setBackground(Color.decode("#fee2e2"));
            btnDel.setForeground(Color.decode("#ef4444"));
            btnDel.setPreferredSize(new Dimension(22, 22));
            btnDel.setFont(new Font("Segoe UI", Font.BOLD, 10));
            btnDel.setMargin(new Insets(0, 0, 0, 0));
            btnDel.addActionListener(e -> {
                listKeranjang.remove(item);
                renderKeranjang();
            });
            
            pnlControl.add(btnMinus);
            pnlControl.add(txtQty);
            pnlControl.add(btnPlus);
            pnlControl.add(Box.createRigidArea(new Dimension(3, 0)));
            pnlControl.add(btnDel);
            
            pnlItem.add(pnlControl, BorderLayout.EAST);
            
            pnlCartList.add(pnlItem);
            pnlCartList.add(Box.createRigidArea(new Dimension(0, 6))); 
        }
        
        lblTotalBesar.setText(AppStyle.formatRupiah(totalBelanja));
        hitungKembalian();
        pnlCartList.revalidate();
        pnlCartList.repaint();
    }

    private void updateQtyFromText(JTextField txtQty, CartItem item) {
        try {
            int newQty = Integer.parseInt(txtQty.getText().trim());
            if (newQty <= 0) {
                listKeranjang.remove(item);
                renderKeranjang();
            } else if (newQty > item.stokTersedia) {
                JOptionPane.showMessageDialog(this, "Stok tidak mencukupi! Maksimal: " + item.stokTersedia);
                txtQty.setText(String.valueOf(item.qty));
            } else {
                if (item.qty != newQty) {
                    item.qty = newQty;
                    renderKeranjang();
                }
            }
        } catch (NumberFormatException ex) {
            txtQty.setText(String.valueOf(item.qty));
        }
    }

    private void hitungKembalian() {
        try {
            double bayar = Double.parseDouble(txtBayar.getText());
            double kembali = bayar - totalBelanja;
            if(kembali >= 0) txtKembalian.setText(AppStyle.formatRupiah(kembali));
            else txtKembalian.setText("Uang Kurang!");
        } catch (Exception e) {
            txtKembalian.setText("");
        }
    }

    private void prosesTransaksi() {
        if (listKeranjang.isEmpty()) { JOptionPane.showMessageDialog(this, "Keranjang kosong!"); return; }
        if (cbCustomer.getSelectedIndex() <= 0) { JOptionPane.showMessageDialog(this, "Pilih customer!"); return; }

        double bayar;
        try {
            bayar = Double.parseDouble(txtBayar.getText());
            if (bayar < totalBelanja) { JOptionPane.showMessageDialog(this, "Uang pembayaran kurang!"); return; }
        } catch (Exception e) { JOptionPane.showMessageDialog(this, "Masukkan nominal pembayaran yang benar!"); return; }

        double kembalian = bayar - totalBelanja;
        String idCustomer = cbCustomer.getSelectedItem().toString().split(" - ")[0];
        String namaCustomer = cbCustomer.getSelectedItem().toString().contains(" - ")
            ? cbCustomer.getSelectedItem().toString().split(" - ", 2)[1] : idCustomer;

        try (Connection conn = Koneksi.getKoneksi()) {
            conn.setAutoCommit(false);
            try {
                // Generate no_faktur otomatis
                String noFaktur = Koneksi.generateID("tb_penjualan", "FKTR", "no_faktur");

                // Insert ke tb_penjualan (Master) — sekarang simpan uang_bayar & kembalian
                PreparedStatement psJual = conn.prepareStatement(
                    "INSERT INTO tb_penjualan (no_faktur, tgl_transaksi, id_customer, total_bayar, uang_bayar, kembalian, id_user) VALUES (?, CURDATE(), ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
                );
                psJual.setString(1, noFaktur);
                psJual.setString(2, idCustomer);
                psJual.setDouble(3, totalBelanja);
                psJual.setDouble(4, bayar);
                psJual.setDouble(5, kembalian);
                psJual.setInt(6, idUserLogin);
                psJual.executeUpdate();

                // Dapatkan id_jual (auto increment)
                ResultSet rsJual = psJual.getGeneratedKeys();
                int idJual = 0;
                if (rsJual.next()) idJual = rsJual.getInt(1);

                // Insert ke tb_detail_penjualan & Update stok
                for (CartItem item : listKeranjang) {
                    PreparedStatement psDetail = conn.prepareStatement(
                        "INSERT INTO tb_detail_penjualan (id_jual, id_barang, harga_satuan, jumlah_beli, subtotal) VALUES (?, ?, ?, ?, ?)"
                    );
                    psDetail.setInt(1, idJual);
                    psDetail.setString(2, item.idBarang);
                    psDetail.setDouble(3, item.harga);
                    psDetail.setInt(4, item.qty);
                    psDetail.setDouble(5, item.getSubtotal());
                    psDetail.executeUpdate();

                    PreparedStatement psStock = conn.prepareStatement(
                        "UPDATE tb_barang SET stok = stok - ? WHERE id_barang = ?"
                    );
                    psStock.setInt(1, item.qty);
                    psStock.setString(2, item.idBarang);
                    psStock.executeUpdate();
                }

                conn.commit();

                // Simpan copy keranjang untuk struk SEBELUM di-clear
                ArrayList<CartItem> itemsForStruk = new ArrayList<>(listKeranjang);
                double totalForStruk = totalBelanja;
                final double finalBayar = bayar;
                final double finalKembalian = kembalian;
                final String finalNoFaktur = noFaktur;
                final String finalNamaCustomer = namaCustomer;

                // Reset form
                listKeranjang.clear();
                renderKeranjang();
                txtBayar.setText(""); txtKembalian.setText("");
                cbCustomer.setSelectedIndex(0); txtCariCustomer.setText("");
                renderProdukGrid(activeKategoriId);

                // Tampilkan struk pembelian
                cetakStruk(finalNoFaktur, finalNamaCustomer, finalBayar, finalKembalian, itemsForStruk, totalForStruk);

            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan sistem!");
        }
    }

    private void cetakStruk(String noFaktur, String namaCustomer, double bayar, double kembalian,
                             ArrayList<CartItem> items, double total) {
        JDialog dialog = new JDialog((java.awt.Frame) SwingUtilities.getWindowAncestor(this), "Struk Pembelian", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(420, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        // === PANEL STRUK (tampilan receipt thermal) ===
        JPanel pnlStruk = new JPanel();
        pnlStruk.setLayout(new BoxLayout(pnlStruk, BoxLayout.Y_AXIS));
        pnlStruk.setBackground(Color.WHITE);
        pnlStruk.setBorder(BorderFactory.createEmptyBorder(20, 28, 20, 28));

        final String SEP  = "- - - - - - - - - - - - - - - -";
        final String THICK = "================================";

        // Header Toko
        addStrukLabel(pnlStruk, "TOKO BERKAH JAYA",     new Font("Monospaced", Font.BOLD,  15), true);
        addStrukLabel(pnlStruk, "Jl. Raya No. 1, Jakarta", new Font("Monospaced", Font.PLAIN, 10), true);
        addStrukLabel(pnlStruk, "Telp: 021-12345678",    new Font("Monospaced", Font.PLAIN, 10), true);
        pnlStruk.add(Box.createRigidArea(new Dimension(0, 4)));
        addStrukLabel(pnlStruk, THICK, new Font("Monospaced", Font.PLAIN, 10), true);

        // Info Transaksi
        String tgl = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date());
        addStrukLabel(pnlStruk, "No Faktur : " + noFaktur,    new Font("Monospaced", Font.PLAIN, 10), false);
        addStrukLabel(pnlStruk, "Tanggal   : " + tgl,         new Font("Monospaced", Font.PLAIN, 10), false);
        addStrukLabel(pnlStruk, "Customer  : " + namaCustomer, new Font("Monospaced", Font.PLAIN, 10), false);
        addStrukLabel(pnlStruk, SEP, new Font("Monospaced", Font.PLAIN, 10), true);

        // Daftar Item
        for (CartItem item : items) {
            addStrukLabel(pnlStruk, item.namaBarang, new Font("Monospaced", Font.BOLD, 10), false);
            String detail = String.format("  %dx %-12s  %s",
                item.qty, AppStyle.formatRupiah(item.harga), AppStyle.formatRupiah(item.getSubtotal()));
            addStrukLabel(pnlStruk, detail, new Font("Monospaced", Font.PLAIN, 10), false);
        }

        addStrukLabel(pnlStruk, THICK, new Font("Monospaced", Font.PLAIN, 10), true);

        // Totals
        addStrukLabel(pnlStruk, String.format("%-12s  %s", "TOTAL",     AppStyle.formatRupiah(total)),
            new Font("Monospaced", Font.BOLD,  11), false);
        addStrukLabel(pnlStruk, String.format("%-12s  %s", "TUNAI",     AppStyle.formatRupiah(bayar)),
            new Font("Monospaced", Font.PLAIN, 10), false);
        addStrukLabel(pnlStruk, String.format("%-12s  %s", "KEMBALIAN", AppStyle.formatRupiah(kembalian)),
            new Font("Monospaced", Font.BOLD,  12), false);

        addStrukLabel(pnlStruk, THICK, new Font("Monospaced", Font.PLAIN, 10), true);
        pnlStruk.add(Box.createRigidArea(new Dimension(0, 6)));
        addStrukLabel(pnlStruk, "** Terima Kasih **",           new Font("Monospaced", Font.BOLD,   11), true);
        addStrukLabel(pnlStruk, "Barang yang sudah dibeli",     new Font("Monospaced", Font.ITALIC,  9), true);
        addStrukLabel(pnlStruk, "tidak dapat dikembalikan.",    new Font("Monospaced", Font.ITALIC,  9), true);

        // Wrapper scroll
        JScrollPane scrollStruk = new JScrollPane(pnlStruk);
        scrollStruk.setBorder(BorderFactory.createLineBorder(Color.decode("#e2e8f0"), 1));
        scrollStruk.getViewport().setBackground(Color.WHITE);

        JPanel pnlContent = new JPanel(new BorderLayout());
        pnlContent.setBackground(Color.decode("#f1f5f9"));
        pnlContent.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));
        pnlContent.add(scrollStruk, BorderLayout.CENTER);
        dialog.add(pnlContent, BorderLayout.CENTER);

        // === PANEL TOMBOL ===
        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 12));
        pnlBtn.setBackground(Color.WHITE);
        pnlBtn.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.decode("#e2e8f0")));

        JButton btnPrint = AppStyle.createButton("Cetak Struk", AppStyle.BTN_PRIMARY);
        btnPrint.setPreferredSize(new Dimension(150, 36));
        JButton btnClose = AppStyle.createButton("Tutup", Color.decode("#64748b"));
        btnClose.setPreferredSize(new Dimension(100, 36));

        btnPrint.addActionListener(e -> {
            java.awt.print.PrinterJob pj = java.awt.print.PrinterJob.getPrinterJob();
            pj.setJobName("Struk - " + noFaktur);
            pj.setPrintable((graphics, pageFormat, pageIndex) -> {
                if (pageIndex > 0) return java.awt.print.Printable.NO_SUCH_PAGE;
                graphics.translate((int) pageFormat.getImageableX(), (int) pageFormat.getImageableY());
                pnlStruk.printAll(graphics);
                return java.awt.print.Printable.PAGE_EXISTS;
            });
            if (pj.printDialog()) {
                try { pj.print(); }
                catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(dialog, "Gagal mencetak: " + ex.getMessage());
                }
            }
        });

        btnClose.addActionListener(e -> dialog.dispose());
        pnlBtn.add(btnPrint);
        pnlBtn.add(btnClose);
        dialog.add(pnlBtn, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void addStrukLabel(JPanel panel, String text, Font font, boolean center) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(font);
        lbl.setAlignmentX(center ? Component.CENTER_ALIGNMENT : Component.LEFT_ALIGNMENT);
        lbl.setHorizontalAlignment(center ? SwingConstants.CENTER : SwingConstants.LEFT);
        panel.add(lbl);
        panel.add(Box.createRigidArea(new Dimension(0, 2)));
    }
}