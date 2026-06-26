package gui;

import config.Koneksi;
import config.AppStyle;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class FormDashboard extends JPanel {

    private FormMenuUtama parentFrame;
    private int idUser;
    private String namaLengkap;
    private String level;

    // KPI Labels
    private JLabel lblKPI1, lblKPI2, lblKPI3, lblKPI4;
    private JTable tabelRecent;
    private DefaultTableModel modelTabel;

    public FormDashboard(FormMenuUtama parentFrame, int idUser, String namaLengkap, String level) {
        this.parentFrame = parentFrame;
        this.idUser = idUser;
        this.namaLengkap = namaLengkap;
        this.level = level;

        setLayout(new BorderLayout(0, 0));
        setBackground(AppStyle.BG_PRIMARY);
        setBorder(new EmptyBorder(0, 0, 0, 0));

        // --- HEADER (NORTH) ---
        JPanel pnlHeader = new JPanel(new GridLayout(2, 1, 0, 4));
        pnlHeader.setOpaque(true);
        pnlHeader.setBackground(AppStyle.BG_SIDEBAR);
        pnlHeader.setPreferredSize(new Dimension(0, 95));
        pnlHeader.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#cbd5e1")),
            BorderFactory.createEmptyBorder(20, 20, 15, 20)
        ));

        JLabel lblJudul = new JLabel("DASHBOARD OVERVIEW");
        lblJudul.setFont(AppStyle.FONT_TITLE);
        lblJudul.setForeground(Color.WHITE);
        pnlHeader.add(lblJudul);

        JLabel lblSub = new JLabel("Welcome back, " + namaLengkap + " (" + level + "). Here's what's happening today.");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(Color.decode("#e2e8f0"));
        pnlHeader.add(lblSub);

        add(pnlHeader, BorderLayout.NORTH);

        // --- CENTRAL PANEL ---
        JPanel pnlBody = new JPanel(new BorderLayout(0, 25));
        pnlBody.setOpaque(false);

        // 1. KPI Cards Grid (Top)
        JPanel pnlKPI = new JPanel(new GridLayout(1, 4, 15, 0));
        pnlKPI.setOpaque(false);

        if (level.equalsIgnoreCase("Admin")) {
            // KPI 1: Total Produk
            JPanel card1 = createKPICard("TOTAL PRODUK", "0 Produk", "#4B2464");
            lblKPI1 = (JLabel) card1.getClientProperty("valueLabel");
            pnlKPI.add(card1);

            // KPI 2: Total Pelanggan
            JPanel card2 = createKPICard("TOTAL CUSTOMERS", "0 Customer", "#10b981");
            lblKPI2 = (JLabel) card2.getClientProperty("valueLabel");
            pnlKPI.add(card2);

            // KPI 3: Total Pendapatan
            JPanel card3 = createKPICard("PENDAPATAN KOTOR", "Rp 0", "#f59e0b");
            lblKPI3 = (JLabel) card3.getClientProperty("valueLabel");
            pnlKPI.add(card3);

            // KPI 4: Total Keuntungan
            JPanel card4 = createKPICard("KEUNTUNGAN BERSIH", "Rp 0", "#ec4899");
            lblKPI4 = (JLabel) card4.getClientProperty("valueLabel");
            pnlKPI.add(card4);
        } else {
            // KPI 1: Total Produk
            JPanel card1 = createKPICard("TOTAL PRODUK", "0 Produk", "#4B2464");
            lblKPI1 = (JLabel) card1.getClientProperty("valueLabel");
            pnlKPI.add(card1);

            // KPI 2: Total Pelanggan
            JPanel card2 = createKPICard("TOTAL CUSTOMERS", "0 Customer", "#10b981");
            lblKPI2 = (JLabel) card2.getClientProperty("valueLabel");
            pnlKPI.add(card2);

            // KPI 3: Transaksi Hari Ini
            JPanel card3 = createKPICard("TRANSAKSI SAYA (HARI INI)", "0 Transaksi", "#8b5cf6");
            lblKPI3 = (JLabel) card3.getClientProperty("valueLabel");
            pnlKPI.add(card3);

            // KPI 4: Penjualan Hari Ini
            JPanel card4 = createKPICard("PENJUALAN SAYA (HARI INI)", "Rp 0", "#f59e0b");
            lblKPI4 = (JLabel) card4.getClientProperty("valueLabel");
            pnlKPI.add(card4);
        }
        pnlBody.add(pnlKPI, BorderLayout.NORTH);

        // 2. Lower split panel (Center)
        JPanel pnlSplit = new JPanel(new GridLayout(1, 2, 20, 0));
        pnlSplit.setOpaque(false);

        // Left Card: Recent Transactions Table
        JPanel cardRecent = new JPanel(new BorderLayout());
        cardRecent.setBackground(Color.WHITE);
        cardRecent.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#e2e8f0"), 1, true),
            new EmptyBorder(15, 18, 15, 18)
        ));

        JLabel lblRecentTitle = new JLabel("5 Transaksi Terbaru Toko");
        lblRecentTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblRecentTitle.setForeground(AppStyle.TEXT_NORMAL);
        lblRecentTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        cardRecent.add(lblRecentTitle, BorderLayout.NORTH);

        modelTabel = new DefaultTableModel(new String[]{"No. Faktur", "Kasir / Petugas", "Total Belanja"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tabelRecent = new JTable(modelTabel);
        AppStyle.styleTable(tabelRecent);

        JScrollPane scrollTable = new JScrollPane(tabelRecent);
        scrollTable.setBorder(BorderFactory.createEmptyBorder());
        scrollTable.getViewport().setBackground(Color.WHITE);
        cardRecent.add(scrollTable, BorderLayout.CENTER);

        pnlSplit.add(cardRecent);

        // Right Card: Quick Shortcut Panels
        JPanel cardShortcuts = new JPanel(new BorderLayout());
        cardShortcuts.setBackground(Color.WHITE);
        cardShortcuts.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#e2e8f0"), 1, true),
            new EmptyBorder(15, 18, 15, 18)
        ));

        JLabel lblShortcutTitle = new JLabel("Akses Cepat Fitur");
        lblShortcutTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblShortcutTitle.setForeground(AppStyle.TEXT_NORMAL);
        lblShortcutTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        cardShortcuts.add(lblShortcutTitle, BorderLayout.NORTH);

        JPanel pnlShortcutsGrid = new JPanel(new GridLayout(0, 1, 0, 12));
        pnlShortcutsGrid.setOpaque(false);

        if (level.equalsIgnoreCase("Admin")) {
            pnlShortcutsGrid.add(createShortcutRow("Point of Sale", "Buka mesin kasir untuk melayani pembeli", "kasir", "#4B2464"));
            pnlShortcutsGrid.add(createShortcutRow("Master Products", "Kelola daftar produk, stok awal, dan harga modal", "barang", "#10b981"));
            pnlShortcutsGrid.add(createShortcutRow("Sales Report", "Lihat grafik omset dan laporan detail laba rugi toko", "laporan", "#ec4899"));
        } else {
            pnlShortcutsGrid.add(createShortcutRow("Point of Sale", "Buka mesin kasir untuk melayani pembeli", "kasir", "#4B2464"));
            pnlShortcutsGrid.add(createShortcutRow("Master Customers", "Kelola data pelanggan tetap atau member toko", "customer", "#10b981"));
        }
        cardShortcuts.add(pnlShortcutsGrid, BorderLayout.CENTER);

        pnlSplit.add(cardShortcuts);

        pnlBody.add(pnlSplit, BorderLayout.CENTER);

        JPanel pnlContainer = new JPanel(new BorderLayout());
        pnlContainer.setOpaque(false);
        pnlContainer.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnlContainer.add(pnlBody, BorderLayout.CENTER);
        add(pnlContainer, BorderLayout.CENTER);

        loadDashboardData();
    }

    private JPanel createKPICard(String title, String value, String accentColorHex) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 4, 0, 0, Color.decode(accentColorHex)),
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#e2e8f0"), 1, true),
                new EmptyBorder(15, 20, 15, 20)
            )
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblTitle.setForeground(Color.decode("#64748b"));
        card.add(lblTitle, BorderLayout.NORTH);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblValue.setForeground(Color.decode("#1e293b"));
        card.add(lblValue, BorderLayout.CENTER);

        card.putClientProperty("valueLabel", lblValue);
        return card;
    }

    private JPanel createShortcutRow(String title, String subtitle, String cardName, String accentColorHex) {
        JPanel row = new JPanel(new BorderLayout(15, 0));
        row.setBackground(Color.decode("#f8fafc"));
        row.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#e2e8f0"), 1, true),
            new EmptyBorder(10, 15, 10, 15)
        ));
        row.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel pnlText = new JPanel(new GridLayout(2, 1, 0, 2));
        pnlText.setOpaque(false);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setForeground(AppStyle.TEXT_NORMAL);
        pnlText.add(lblTitle);

        JLabel lblSub = new JLabel(subtitle);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSub.setForeground(Color.decode("#64748b"));
        pnlText.add(lblSub);

        row.add(pnlText, BorderLayout.CENTER);

        JButton btnGo = new JButton("\u2192");
        btnGo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGo.setFocusPainted(false);
        btnGo.setOpaque(false);
        btnGo.setContentAreaFilled(false);
        btnGo.setBorderPainted(false);
        btnGo.setForeground(Color.decode(accentColorHex));
        row.add(btnGo, BorderLayout.EAST);

        // Click listeners
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                parentFrame.navigateTo(cardName);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                row.setBackground(Color.decode("#f1f5f9"));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                row.setBackground(Color.decode("#f8fafc"));
            }
        };

        row.addMouseListener(ma);
        btnGo.addMouseListener(ma);

        return row;
    }

    public void loadDashboardData() {
        modelTabel.setRowCount(0);

        try (Connection conn = Koneksi.getKoneksi()) {
            // 1. Common KPIs (Products & Customers)
            int totalProducts = 0;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM tb_barang")) {
                if (rs.next()) totalProducts = rs.getInt(1);
            }
            lblKPI1.setText(String.format("%,d Produk", totalProducts));

            int totalCustomers = 0;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM tb_customer")) {
                if (rs.next()) totalCustomers = rs.getInt(1);
            }
            lblKPI2.setText(String.format("%,d Customers", totalCustomers));

            // 2. Role-specific KPIs
            if (level.equalsIgnoreCase("Admin")) {
                double totalRevenue = 0;
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT IFNULL(SUM(total_bayar), 0) FROM tb_penjualan")) {
                    if (rs.next()) totalRevenue = rs.getDouble(1);
                }
                lblKPI3.setText(AppStyle.formatRupiah(totalRevenue));

                double totalProfit = 0;
                String profitSql = "SELECT IFNULL(SUM((d.harga_satuan - b.harga_beli) * d.jumlah_beli), 0) " +
                                   "FROM tb_detail_penjualan d " +
                                   "JOIN tb_barang b ON d.id_barang = b.id_barang";
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(profitSql)) {
                    if (rs.next()) totalProfit = rs.getDouble(1);
                }
                lblKPI4.setText(AppStyle.formatRupiah(totalProfit));

            } else {
                int myTxCount = 0;
                String myTxSql = "SELECT COUNT(*) FROM tb_penjualan WHERE id_user = ? AND tgl_transaksi = CURDATE()";
                try (PreparedStatement ps = conn.prepareStatement(myTxSql)) {
                    ps.setInt(1, idUser);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) myTxCount = rs.getInt(1);
                    }
                }
                lblKPI3.setText(String.format("%,d Transaksi", myTxCount));

                double mySalesSum = 0;
                String mySalesSql = "SELECT IFNULL(SUM(total_bayar), 0) FROM tb_penjualan WHERE id_user = ? AND tgl_transaksi = CURDATE()";
                try (PreparedStatement ps = conn.prepareStatement(mySalesSql)) {
                    ps.setInt(1, idUser);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) mySalesSum = rs.getDouble(1);
                    }
                }
                lblKPI4.setText(AppStyle.formatRupiah(mySalesSum));
            }

            // 3. Recent Transactions (Top 5)
            String recentSql = "SELECT p.no_faktur, u.nama_lengkap, p.total_bayar " +
                               "FROM tb_penjualan p " +
                               "JOIN tb_user u ON p.id_user = u.id_user " +
                               "ORDER BY p.id_jual DESC LIMIT 5";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(recentSql)) {
                while (rs.next()) {
                    modelTabel.addRow(new Object[]{
                        rs.getString("no_faktur"),
                        rs.getString("nama_lengkap"),
                        AppStyle.formatRupiah(rs.getDouble("total_bayar"))
                    });
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
