package gui;

import config.Koneksi;
import config.AppStyle;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class FormLaporan extends JPanel {

    private JLabel lblTotalTx, lblTotalRev, lblTotalQty, lblTotalProfit;
    private JTable tabelLaporan;
    private DefaultTableModel modelTabel;
    private JButton btnRefresh;

    public FormLaporan() {
        setLayout(new BorderLayout(0, 0));
        setBackground(AppStyle.BG_PRIMARY);
        setBorder(new EmptyBorder(0, 0, 0, 0));

        // --- HEADER ---
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(true);
        pnlHeader.setBackground(AppStyle.BG_SIDEBAR);
        pnlHeader.setPreferredSize(new Dimension(0, 95));
        pnlHeader.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#cbd5e1")),
            BorderFactory.createEmptyBorder(20, 20, 15, 20)
        ));

        JLabel lblJudul = new JLabel("LAPORAN PENJUALAN (ADMIN)");
        lblJudul.setFont(AppStyle.FONT_TITLE);
        lblJudul.setForeground(Color.WHITE);
        pnlHeader.add(lblJudul, BorderLayout.WEST);

        btnRefresh = AppStyle.createButton("Refresh", AppStyle.BTN_PRIMARY);
        btnRefresh.setPreferredSize(new Dimension(120, 35));
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRefresh.addActionListener(e -> loadData());
        pnlHeader.add(btnRefresh, BorderLayout.EAST);

        add(pnlHeader, BorderLayout.NORTH);

        // --- CENTRAL CONTENT ---
        JPanel pnlContent = new JPanel(new BorderLayout(0, 20));
        pnlContent.setOpaque(false);

        // 1. KPI Cards Panel (Top)
        JPanel pnlKPI = new JPanel(new GridLayout(1, 4, 15, 0));
        pnlKPI.setOpaque(false);

        // Card 1: Total Transaksi
        JPanel cardTx = createKPICard("TOTAL TRANSAKSI", "0 Transaksi", "#4B2464");
        lblTotalTx = (JLabel) cardTx.getClientProperty("valueLabel");
        pnlKPI.add(cardTx);

        // Card 2: Total Pendapatan
        JPanel cardRev = createKPICard("TOTAL PENDAPATAN", "Rp 0", "#10b981");
        lblTotalRev = (JLabel) cardRev.getClientProperty("valueLabel");
        pnlKPI.add(cardRev);

        // Card 3: Total Item Terjual
        JPanel cardQty = createKPICard("TOTAL ITEM TERJUAL", "0 Unit", "#f59e0b");
        lblTotalQty = (JLabel) cardQty.getClientProperty("valueLabel");
        pnlKPI.add(cardQty);

        // Card 4: Total Keuntungan
        JPanel cardProfit = createKPICard("TOTAL KEUNTUNGAN", "Rp 0", "#ec4899");
        lblTotalProfit = (JLabel) cardProfit.getClientProperty("valueLabel");
        pnlKPI.add(cardProfit);

        pnlContent.add(pnlKPI, BorderLayout.NORTH);

        // 2. Table Panel (Center)
        JPanel pnlTableCard = new JPanel(new BorderLayout());
        pnlTableCard.setBackground(Color.WHITE);
        pnlTableCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#e2e8f0"), 1, true),
            new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblTableTitle = new JLabel("Riwayat Transaksi Penjualan");
        lblTableTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTableTitle.setForeground(AppStyle.TEXT_NORMAL);
        lblTableTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        pnlTableCard.add(lblTableTitle, BorderLayout.NORTH);

        modelTabel = new DefaultTableModel(
            new String[]{"No. Faktur", "Tanggal", "Nama Customer", "Detail Barang (Qty)", "Total Bayar"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tabelLaporan = new JTable(modelTabel);
        AppStyle.styleTable(tabelLaporan);

        JScrollPane scrollTable = new JScrollPane(tabelLaporan);
        scrollTable.setBorder(BorderFactory.createEmptyBorder());
        scrollTable.getViewport().setBackground(Color.WHITE);
        pnlTableCard.add(scrollTable, BorderLayout.CENTER);

        pnlContent.add(pnlTableCard, BorderLayout.CENTER);

        JPanel pnlContainer = new JPanel(new BorderLayout());
        pnlContainer.setOpaque(false);
        pnlContainer.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnlContainer.add(pnlContent, BorderLayout.CENTER);
        add(pnlContainer, BorderLayout.CENTER);

        // Load data on start
        loadData();
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

        // Save reference to change text later
        card.putClientProperty("valueLabel", lblValue);

        return card;
    }

    private void loadData() {
        modelTabel.setRowCount(0);

        try (Connection conn = Koneksi.getKoneksi()) {
            // 1. Load KPI Data
            String sqlKPI = "SELECT COUNT(*) AS total_tx, IFNULL(SUM(total_bayar), 0) AS total_rev FROM tb_penjualan";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlKPI)) {
                if (rs.next()) {
                    int totalTx = rs.getInt("total_tx");
                    double totalRev = rs.getDouble("total_rev");

                    lblTotalTx.setText(String.format("%,d Transaksi", totalTx));
                    lblTotalRev.setText(AppStyle.formatRupiah(totalRev));
                }
            }

            String sqlQty = "SELECT IFNULL(SUM(jumlah_beli), 0) AS total_items FROM tb_detail_penjualan";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlQty)) {
                if (rs.next()) {
                    int totalQty = rs.getInt("total_items");
                    lblTotalQty.setText(String.format("%,d Unit", totalQty));
                }
            }

            String sqlProfit = "SELECT IFNULL(SUM((d.harga_satuan - b.harga_beli) * d.jumlah_beli), 0) AS total_profit " +
                               "FROM tb_detail_penjualan d " +
                               "JOIN tb_barang b ON d.id_barang = b.id_barang";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlProfit)) {
                if (rs.next()) {
                    double totalProfit = rs.getDouble("total_profit");
                    lblTotalProfit.setText(AppStyle.formatRupiah(totalProfit));
                }
            }

            // 2. Load Table Data
            String sqlTable = "SELECT " +
                    "    p.no_faktur, " +
                    "    p.tgl_transaksi, " +
                    "    IFNULL(c.nama_customer, 'Umum/Guest') AS nama_customer, " +
                    "    GROUP_CONCAT(CONCAT(b.nama_barang, ' (', d.jumlah_beli, ')') SEPARATOR ', ') AS detail_barang, " +
                    "    p.total_bayar " +
                    "FROM tb_penjualan p " +
                    "LEFT JOIN tb_customer c ON p.id_customer = c.id_customer " +
                    "LEFT JOIN tb_detail_penjualan d ON p.id_jual = d.id_jual " +
                    "LEFT JOIN tb_barang b ON d.id_barang = b.id_barang " +
                    "GROUP BY p.id_jual " +
                    "ORDER BY p.tgl_transaksi DESC, p.id_jual DESC";

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlTable)) {
                while (rs.next()) {
                    String noFaktur = rs.getString("no_faktur");
                    Date tgl = rs.getDate("tgl_transaksi");
                    String customer = rs.getString("nama_customer");
                    String detail = rs.getString("detail_barang");
                    double total = rs.getDouble("total_bayar");

                    if (detail == null) {
                        detail = "-";
                    }

                    modelTabel.addRow(new Object[]{
                        noFaktur,
                        tgl != null ? tgl.toString() : "-",
                        customer,
                        detail,
                        AppStyle.formatRupiah(total)
                    });
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat memuat data laporan!");
        }
    }
}
