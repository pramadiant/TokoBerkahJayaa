package gui;

import config.AppStyle;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class FormMenuUtama extends JFrame {

    private int idUser;
    private String namaLengkap, level;
    private JPanel pnlContent;
    private CardLayout cardLayout;
    
    // Simpan daftar panel tombol agar mudah diubah warnanya
    private ArrayList<JPanel> listMenuPanels = new ArrayList<>();

    public FormMenuUtama(int idUser, String namaLengkap, String level) {
        this.idUser = idUser;
        this.namaLengkap = namaLengkap;
        this.level = level;

        setTitle("Toko Berkah Jaya - " + namaLengkap + " (" + level + ")");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- SIDEBAR ---
        JPanel pnlSidebar = new JPanel();
        pnlSidebar.setBackground(AppStyle.BG_SIDEBAR);
        pnlSidebar.setPreferredSize(new Dimension(280, 0));
        pnlSidebar.setLayout(new BoxLayout(pnlSidebar, BoxLayout.Y_AXIS));
        pnlSidebar.setBorder(new EmptyBorder(30, 15, 30, 15)); // Margin luar sidebar

        // Brand
        JPanel pnlBrand = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        pnlBrand.setOpaque(false);
        pnlBrand.setMaximumSize(new Dimension(300, 60));
        
        JLabel lblLogo;
        try {
            ImageIcon scaledImg = AppStyle.getScaledIcon("/resources/images/Logo_Pramadiant_Store.png", 45, 45);
            if (scaledImg != null) {
                lblLogo = new JLabel(scaledImg);
            } else {
                lblLogo = new JLabel("IP");
                lblLogo.setOpaque(true);
                lblLogo.setBackground(AppStyle.BTN_PRIMARY);
                lblLogo.setForeground(Color.WHITE);
                lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 20));
                lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
                lblLogo.setPreferredSize(new Dimension(45, 45));
                lblLogo.setBorder(BorderFactory.createEmptyBorder());
            }
        } catch (Exception e) {
            lblLogo = new JLabel("IP");
            lblLogo.setOpaque(true);
            lblLogo.setBackground(AppStyle.BTN_PRIMARY);
            lblLogo.setForeground(Color.WHITE);
            lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 20));
            lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
            lblLogo.setPreferredSize(new Dimension(45, 45));
            lblLogo.setBorder(BorderFactory.createEmptyBorder());
        }
        
        JPanel pnlTextBrand = new JPanel(new GridLayout(2, 1));
        pnlTextBrand.setOpaque(false);
        JLabel lblTitle = new JLabel("Toko Berkah Jaya");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        JLabel lblSubtitle = new JLabel(namaLengkap + " | " + level);
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitle.setForeground(new Color(180, 180, 180));
        pnlTextBrand.add(lblTitle);
        pnlTextBrand.add(lblSubtitle);
        
        pnlBrand.add(lblLogo);
        pnlBrand.add(pnlTextBrand);
        
        pnlSidebar.add(pnlBrand);
        pnlSidebar.add(Box.createRigidArea(new Dimension(0, 40)));

        // --- CONTENT AREA ---
        cardLayout = new CardLayout();
        pnlContent = new JPanel(cardLayout);
        pnlContent.setBackground(AppStyle.BG_PRIMARY);

        pnlContent.add(new FormDashboard(this, idUser, namaLengkap, level), "home");
        pnlContent.add(new FormCustomer(), "customer");
        pnlContent.add(new FormTransaksi(idUser), "kasir");
        if (level.equalsIgnoreCase("Admin")) {
            pnlContent.add(new FormBarang(), "barang");
            pnlContent.add(new FormUser(), "user");
            pnlContent.add(new FormLaporan(), "laporan");
        }
        
        // Buat Tombol Menu
        JPanel btnHome = buatMenuSidebar("Dashboard", "home", pnlSidebar);
        JPanel btnKasir = buatMenuSidebar("Point of Sale", "kasir", pnlSidebar);
        JPanel btnCustomer = buatMenuSidebar("Master Customers", "customer", pnlSidebar); 
        
        if (level.equalsIgnoreCase("Admin")) {
            buatMenuSidebar("Master Products", "barang", pnlSidebar);
            buatMenuSidebar("User Management", "user", pnlSidebar);
            buatMenuSidebar("Sales Report", "laporan", pnlSidebar);
        }

        pnlSidebar.add(Box.createVerticalGlue());
        
        // Tombol Logout
        JPanel pnlLogout = buatMenuSidebar("Logout", "logout", pnlSidebar);
        
        add(pnlSidebar, BorderLayout.WEST);
        add(pnlContent, BorderLayout.CENTER);
        
        // Aktifkan menu pertama
        setActiveMenu(listMenuPanels.get(0));
    }



    // Menggunakan JPanel sebagai "Pill" menu agar bisa dibentuk melengkung dengan FlatLaf
    private JPanel buatMenuSidebar(String teks, String cardName, JPanel parentSidebar) {
        JPanel panelMenu = new JPanel(new BorderLayout());
        panelMenu.setMaximumSize(new Dimension(300, 45));
        panelMenu.setPreferredSize(new Dimension(250, 45));
        panelMenu.setOpaque(false); // Transparan secara default
        panelMenu.setBorder(new EmptyBorder(0, 15, 0, 15));
        
        // Label teks
        JLabel lbl = new JLabel(teks);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(new Color(200, 200, 200));
        panelMenu.add(lbl, BorderLayout.CENTER);
        
        // Event hover & click
        panelMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!panelMenu.getName().equals("active")) {
                    panelMenu.setOpaque(true);
                    panelMenu.setBackground(Color.decode("#5C2880")); // Hover purple
                    panelMenu.repaint();
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!panelMenu.getName().equals("active")) {
                    panelMenu.setOpaque(false);
                    panelMenu.repaint();
                }
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if(cardName.equals("logout")) {
                    if(JOptionPane.showConfirmDialog(FormMenuUtama.this, "Yakin keluar?", "Logout", JOptionPane.YES_NO_OPTION) == 0){
                        new FormLogin().setVisible(true);
                        dispose();
                    }
                    return;
                }
                navigateTo(cardName);
            }
        });
        
        // Kita beri nama panel ini untuk referensi aktif/tidak
        panelMenu.setName("inactive");
        panelMenu.putClientProperty("cardName", cardName);
        
        if(!cardName.equals("logout")) listMenuPanels.add(panelMenu);
        
        parentSidebar.add(panelMenu);
        parentSidebar.add(Box.createRigidArea(new Dimension(0, 5))); // Jarak antar menu
        return panelMenu;
    }

    private void setActiveMenu(JPanel activePanel) {
        for (JPanel panel : listMenuPanels) {
            panel.setName("inactive");
            panel.setOpaque(false);
            JLabel l = (JLabel) panel.getComponent(0);
            l.setForeground(new Color(200, 200, 200));
            panel.repaint();
        }
        activePanel.setName("active");
        activePanel.setOpaque(true);
        // Pakai warna primary container (seperti di HTML)
        activePanel.setBackground(Color.decode("#6b308f")); 
        
        // Supaya ujungnya melengkung, kita override paintComponent atau manfaatkan panel default
        // Di sini FlatLaf akan membuat flat panel, jika ingin pill, kita biarkan solid.
        JLabel l = (JLabel) activePanel.getComponent(0);
        l.setForeground(Color.WHITE);
        activePanel.repaint();
    }

    public void navigateTo(String cardName) {
        JPanel targetPanel = null;
        for (JPanel panel : listMenuPanels) {
            if (cardName.equals(panel.getClientProperty("cardName"))) {
                targetPanel = panel;
                break;
            }
        }
        
        if (targetPanel != null) {
            setActiveMenu(targetPanel);
        }
        cardLayout.show(pnlContent, cardName);
        
        // Update font color
        for (JPanel p : listMenuPanels) {
            JLabel l = (JLabel) p.getComponent(0);
            l.setForeground(p.getName().equals("active") ? Color.WHITE : new Color(200, 200, 200));
        }
        
        // Reload dashboard data when showing home
        if (cardName.equals("home")) {
            for (Component comp : pnlContent.getComponents()) {
                if (comp instanceof FormDashboard) {
                    ((FormDashboard) comp).loadDashboardData();
                    break;
                }
            }
        }
    }
}